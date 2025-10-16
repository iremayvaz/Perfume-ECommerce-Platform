package com.iremayvaz.repository.specs;

import com.iremayvaz.model.entity.Product;
import com.iremayvaz.model.enums.Concentration;
import com.iremayvaz.model.enums.Gender;
import com.iremayvaz.model.enums.NoteType;
import com.iremayvaz.model.enums.Season;
import jakarta.persistence.criteria.JoinType;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

import static com.iremayvaz.model.enums.NoteType.*;

@UtilityClass
// Sınıfı final yapar
// Tüm alan ve metotlar otomatik static olur
// Default constructor private olur
// Yani bu sınıf sadece yardımcı metotlar içerir demektir
public class ProductSpecifications {
    public Specification<Product> filterByColumn(String column, String content) {
        return (root, query, cb) -> {
            if (column == null || content == null || content.isBlank()) {
                return cb.conjunction(); // TRUE
            }

            final String col = column.toLowerCase();

            try {
                switch (col) {
                    case "price":
                        return cb.equal(root.get("price"), new BigDecimal(content));

                    case "rating":
                        return cb.equal(root.get("rating"), Double.valueOf(content));

                    case "accord":
                        var s1 = categoryAccordContains(content);
                        return s1 == null ? cb.conjunction() : s1.toPredicate(root, query, cb);

                    case "gender":
                        var g = com.iremayvaz.model.enums.Gender.valueOf(content.toUpperCase());
                        var s2 = genderIs(g);
                        return s2 == null ? cb.conjunction() : s2.toPredicate(root, query, cb);

                    case "concentration":
                        var conc = com.iremayvaz.model.enums.Concentration.valueOf(content.toUpperCase());
                        var s3 = concentrationIs(conc);
                        return s3 == null ? cb.conjunction() : s3.toPredicate(root, query, cb);

                    case "season":
                        var season = com.iremayvaz.model.enums.Season.valueOf(content.toUpperCase());
                        var s4 = seasonIs(season);
                        return s4 == null ? cb.conjunction() : s4.toPredicate(root, query, cb);

                    case "topnotes": {
                        query.distinct(true);
                        var s = noteNameContainsIn(com.iremayvaz.model.enums.NoteType.TOP, content);
                        return s == null ? cb.conjunction() : s.toPredicate(root, query, cb);
                    }
                    case "heartnotes": {
                        query.distinct(true);
                        var s = noteNameContainsIn(com.iremayvaz.model.enums.NoteType.HEART, content);
                        return s == null ? cb.conjunction() : s.toPredicate(root, query, cb);
                    }
                    case "basenotes": {
                        query.distinct(true);
                        var s = noteNameContainsIn(com.iremayvaz.model.enums.NoteType.BASE, content);
                        return s == null ? cb.conjunction() : s.toPredicate(root, query, cb);
                    }

                    default:
                        // STRING alanlar için case-insensitive LIKE
                        return cb.like(cb.lower(root.get(column)), like(content));
                }
            } catch (IllegalArgumentException ex) {
                // Geçersiz enum/alan ismi vs.
                return cb.disjunction(); // FALSE
            }
        };
    }


    public static Specification<Product> categoryAccordContains(String accord){
        if (accord == null || accord.isBlank()) return null;
        return (r,c,cb) -> cb.like(
                cb.lower(r.join("category", JoinType.LEFT).get("accord")), like(accord)
        );
    }

    public static Specification<Product> genderIs(Gender gender){
        if (gender == null) return null;
        return (r,c,cb) -> cb.equal(r.join("category", JoinType.LEFT).get("gender"), gender);
    }

    public static Specification<Product> concentrationIs(Concentration concentration){
        if (concentration == null) return null;
        return (r,c,cb) -> cb.equal(r.join("category", JoinType.LEFT).get("concentrationName"), concentration);
    }

    public static Specification<Product> seasonIs(Season season){
        if (season == null) return null;
        return (r,c,cb) -> cb.equal(r.join("category", JoinType.LEFT).get("season"), season);
    }

    public static Specification<Product> noteNameContainsIn(NoteType type, String query) {
        if (type == null || query == null || query.isBlank()) return null;
        return (root, cq, cb) -> {
            cq.distinct(true);
            var like = "%" + query.toLowerCase() + "%";
            String path = switch (type) {
                case TOP   -> "topNotes";
                case HEART -> "heartNotes";
                case BASE  -> "baseNotes";
            };

            return cb.like(cb.lower(root.join(path, JoinType.LEFT).get("noteName")), like);
        };
    }


    private static String like(String v){ return "%" + v.toLowerCase() + "%"; }
}