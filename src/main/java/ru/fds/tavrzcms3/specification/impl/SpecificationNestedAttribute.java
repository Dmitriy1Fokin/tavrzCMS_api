package ru.fds.tavrzcms3.specification.impl;


import org.springframework.data.jpa.domain.Specification;
import ru.fds.tavrzcms3.dictionary.Operations;
import ru.fds.tavrzcms3.specification.SearchCriteriaNestedAttribute;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;

public class SpecificationNestedAttribute<T, N> implements Specification{

    private final SearchCriteriaNestedAttribute criteria;

    public SpecificationNestedAttribute(SearchCriteriaNestedAttribute criteria){
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
        Join<T, N> join = root.join(criteria.getNestedObjectName());
        if(criteria.getOperation() == Operations.GREATER){
            if(criteria.getValue().getClass() == String.class) {

                return criteriaBuilder.greaterThan(join.<String>get(criteria.getKey()), criteria.getValue().toString());
            }
            else if(criteria.getValue().getClass() == LocalDate.class){
                return criteriaBuilder.greaterThan(join.<LocalDate> get(criteria.getKey()), (LocalDate) criteria.getValue());
            }
            else {
                return null;
            }
        }
        if(criteria.getOperation() == Operations.GREATER_EQUAL){
            if(criteria.getValue().getClass() == String.class) {
                return criteriaBuilder.greaterThanOrEqualTo(join.<String>get(criteria.getKey()), criteria.getValue().toString());
            }
            else if(criteria.getValue().getClass() == LocalDate.class){
                return criteriaBuilder.greaterThanOrEqualTo(join.<LocalDate> get(criteria.getKey()), (LocalDate) criteria.getValue());
            }
            else {
                return null;
            }
        }
        if(criteria.getOperation() == Operations.LESS){
            if(criteria.getValue().getClass() == String.class) {
                return criteriaBuilder.lessThan(join.<String>get(criteria.getKey()), criteria.getValue().toString());
            }
            else if(criteria.getValue().getClass() == LocalDate.class){
                return criteriaBuilder.lessThan(join.<LocalDate> get(criteria.getKey()), (LocalDate) criteria.getValue());
            }
            else {
                return null;
            }
        }
        if(criteria.getOperation() == Operations.LESS_EQUAL){
            if(criteria.getValue().getClass() == String.class) {
                return criteriaBuilder.lessThanOrEqualTo(join.<String>get(criteria.getKey()), criteria.getValue().toString());
            }
            else if(criteria.getValue().getClass() == LocalDate.class){
                return criteriaBuilder.lessThanOrEqualTo(join.<LocalDate> get(criteria.getKey()), (LocalDate) criteria.getValue());
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
