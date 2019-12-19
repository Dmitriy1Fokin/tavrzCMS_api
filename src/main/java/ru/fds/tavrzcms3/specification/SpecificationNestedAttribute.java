package ru.fds.tavrzcms3.specification;


import org.springframework.data.jpa.domain.Specification;
import ru.fds.tavrzcms3.dictionary.Operations;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;

public class SpecificationNestedAttribute<T, N> implements Specification{

    private Join<T, N> join;
    private final SearchCriteriaNestedAttribute criteria;

    public SpecificationNestedAttribute(SearchCriteriaNestedAttribute criteria){
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
        join = root.join(criteria.getNestedObjectName());
        if(criteria.getOperation() == Operations.GREATER){
            if(criteria.getValue().getClass() == String.class) {

                return criteriaBuilder.greaterThan(join.<String>get(criteria.getKey()), criteria.getValue().toString());
            }
            else if(criteria.getValue().getClass() == Date.class){
                return criteriaBuilder.greaterThan(join.<Date> get(criteria.getKey()), (Date) criteria.getValue());
            }
            else {
                return null;
            }
        }
        if(criteria.getOperation() == Operations.GREATER_EQUAL){
            if(criteria.getValue().getClass() == String.class) {
                return criteriaBuilder.greaterThanOrEqualTo(join.<String>get(criteria.getKey()), criteria.getValue().toString());
            }
            else if(criteria.getValue().getClass() == Date.class){
                return criteriaBuilder.greaterThanOrEqualTo(join.<Date> get(criteria.getKey()), (Date) criteria.getValue());
            }
            else {
                return null;
            }
        }
        if(criteria.getOperation() == Operations.LESS){
            if(criteria.getValue().getClass() == String.class) {
                return criteriaBuilder.lessThan(join.<String>get(criteria.getKey()), criteria.getValue().toString());
            }
            else if(criteria.getValue().getClass() == Date.class){
                return criteriaBuilder.lessThan(join.<Date> get(criteria.getKey()), (Date) criteria.getValue());
            }
            else {
                return null;
            }
        }
        if(criteria.getOperation() == Operations.LESS_EQUAL){
            if(criteria.getValue().getClass() == String.class) {
                return criteriaBuilder.lessThanOrEqualTo(join.<String>get(criteria.getKey()), criteria.getValue().toString());
            }
            else if(criteria.getValue().getClass() == Date.class){
                return criteriaBuilder.lessThanOrEqualTo(join.<Date> get(criteria.getKey()), (Date) criteria.getValue());
            }
            else {
                return null;
            }
        }
        if(criteria.getOperation() == Operations.EQUAL_IGNORE_CASE){
            if(join.get(criteria.getKey()).getJavaType() == String.class){
                return criteriaBuilder.like(criteriaBuilder.lower(join.<String> get(criteria.getKey())), "%" + criteria.getValue().toString().toLowerCase() + "%");
            }
            else {
                return criteriaBuilder.equal(join.get(criteria.getKey()), criteria.getValue());
            }

        }
        return null;
    }
}
