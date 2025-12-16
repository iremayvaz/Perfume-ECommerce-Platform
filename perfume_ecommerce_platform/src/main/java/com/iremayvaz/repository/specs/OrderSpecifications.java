package com.iremayvaz.repository.specs;

import com.iremayvaz.model.entity.Order;
import com.iremayvaz.model.enums.OrderState;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;

@UtilityClass
public class OrderSpecifications {

    public Specification<Order> search(String term) {
        return (root, query, cb) -> {
            if (term == null || term.isBlank()) return cb.conjunction();

            String raw = term.trim();
            String v = raw.toLowerCase();

            var codeLike = cb.like(cb.lower(root.get("code")), like(v));

            var user = root.join("user"); // Order -> User join
            var firstLike = cb.like(cb.lower(user.get("firstName")), like(v));
            var lastLike  = cb.like(cb.lower(user.get("lastName")), like(v));
            var fullNameLike = cb.like(
                    cb.lower(cb.concat(cb.concat(user.get("firstName"), " "), user.get("lastName"))),
                    like(v)
            );
            var userLike = cb.or(firstLike, lastLike, fullNameLike);

            // "PAID" gibi bir şey yazıldıysa state filtresi de eklensin
            try {
                OrderState st = OrderState.valueOf(raw.toUpperCase(Locale.ROOT));
                var stateEq = cb.equal(root.get("state"), st);
                return cb.or(codeLike, userLike, stateEq);
            } catch (IllegalArgumentException ex) {
                return cb.or(codeLike, userLike);
            }
        };
    }

    private static String like(String v) {
        return "%" + v + "%";
    }
}

