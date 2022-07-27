package me.herohe.commonweb.comment;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class CommentSpecs {

    public static Specification<Comment> isBest() {
        return (root, query, builder) -> builder.isTrue(root.get(Comment_.best));
    }

    public static Specification<Comment> isGood() {
        return new Specification<Comment>() {
            @Override
            public Predicate toPredicate(Root<Comment> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                return builder.greaterThan(root.get(Comment_.UP), 10);
            }
        };
    }
}
