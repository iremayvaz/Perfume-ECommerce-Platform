package com.iremayvaz.repository.specs;

import com.iremayvaz.model.entity.Product;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
// Sınıfı final yapar
// Tüm alan ve metotlar otomatik static olur
// Default constructor private olur
// Yani bu sınıf sadece yardımcı metotlar içerir demektir
public class ProductSpecifications {
    public Specification<Product> filterByColumn(String column, String content) {
        return (root, // sorgunun kökü : FROM Product p
                query, // Tüm sorgu iskeleti (SELECT - FROM - WHERE - ORDER BY)
                criteriaBuilder) -> { // WHERE koşulunu kurarkenki araç kutusu
            if (column == null || content == null || content.trim().isEmpty()) {
                return criteriaBuilder.conjunction(); // TRUE döner yani her şeyi geçir demek
            }

            try{
                if(column.equalsIgnoreCase("price") || column.equalsIgnoreCase("rating")){ // NUMERİK
                    return criteriaBuilder.equal(root.get(column), content);
                } else { // STRING
                    return criteriaBuilder.like(
                            criteriaBuilder.lower(root.get(column)),
                            "%" + content.toLowerCase() + "%"
                    );
                }
            } catch (IllegalArgumentException e) {
                return criteriaBuilder.disjunction(); // FALSE döner yani boş kayıt döner
            }
        };
    }
}