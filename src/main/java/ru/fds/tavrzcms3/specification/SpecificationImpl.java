package ru.fds.tavrzcms3.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.fds.tavrzcms3.dictionary.Operations;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;

public class SpecificationImpl implements Specification {

    private SearchCriteria criteria;

    public SpecificationImpl(SearchCriteria searchCriteria){
        criteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {


        if(criteria.getOperation() == Operations.GREATER){
            if(criteria.getValue().getClass() == String.class) {
                return criteriaBuilder.greaterThan(root.<String>get(criteria.getKey()), criteria.getValue().toString());
            }
            else if(criteria.getValue().getClass() == Date.class){
                return criteriaBuilder.greaterThan(root.<Date> get(criteria.getKey()), (Date) criteria.getValue());
            }
            else {
                return null;
            }
        }
        if(criteria.getOperation() == Operations.GREATER_EQUAL){
            if(criteria.getValue().getClass() == String.class) {
                return criteriaBuilder.greaterThanOrEqualTo(root.<String>get(criteria.getKey()), criteria.getValue().toString());
            }
            else if(criteria.getValue().getClass() == Date.class){
                return criteriaBuilder.greaterThanOrEqualTo(root.<Date> get(criteria.getKey()), (Date) criteria.getValue());
            }
            else {
                return null;
            }
        }
        if(criteria.getOperation() == Operations.LESS){
            if(criteria.getValue().getClass() == String.class) {
                return criteriaBuilder.lessThan(root.<String>get(criteria.getKey()), criteria.getValue().toString());
            }
            else if(criteria.getValue().getClass() == Date.class){
                return criteriaBuilder.lessThan(root.<Date> get(criteria.getKey()), (Date) criteria.getValue());
            }
            else {
                return null;
            }
        }
        if(criteria.getOperation() == Operations.LESS_EQUAL){
            if(criteria.getValue().getClass() == String.class) {
                return criteriaBuilder.lessThanOrEqualTo(root.<String>get(criteria.getKey()), criteria.getValue().toString());
            }
            else if(criteria.getValue().getClass() == Date.class){
                return criteriaBuilder.lessThanOrEqualTo(root.<Date> get(criteria.getKey()), (Date) criteria.getValue());
            }
            else {
                return null;
            }
        }
        if(criteria.getOperation() == Operations.EQUAL_IGNORE_CASE){
            if(root.get(criteria.getKey()).getJavaType() == String.class){
                return criteriaBuilder.like(criteriaBuilder.lower(root.<String> get(criteria.getKey())), "%" + criteria.getValue().toString().toLowerCase() + "%");
            }
            else {
                return criteriaBuilder.equal(root.get(criteria.getKey()), criteria.getValue());
            }

        }
        return null;
    }
}
