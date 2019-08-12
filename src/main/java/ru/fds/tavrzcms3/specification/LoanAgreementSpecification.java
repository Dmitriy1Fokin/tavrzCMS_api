package ru.fds.tavrzcms3.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.LoanAgreement;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.Date;

public class LoanAgreementSpecification implements Specification<LoanAgreement> {
    private SearchCriteria criteria;

//    public LoanAgreementSpecification(){
//        criteria = new SearchCriteria();
//    }

    public LoanAgreementSpecification(SearchCriteria searchCriteria){
        criteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<LoanAgreement> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if(criteria.getOperation().equalsIgnoreCase(">")){
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
        if(criteria.getOperation().equalsIgnoreCase(">=")){
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
        if(criteria.getOperation().equalsIgnoreCase("<")){
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
        if(criteria.getOperation().equalsIgnoreCase("<=")){
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
        if(criteria.getOperation().equalsIgnoreCase(":")){
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
