package ru.fds.tavrzcms3.specification;

import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T>{
    void withCriteria(SearchCriteria searchCriteria);
    void withNestedAttributeCriteria(SearchCriteriaNestedAttribute searchCriteriaNestedAttribute);
    Specification<T> buildSpecification();
}
