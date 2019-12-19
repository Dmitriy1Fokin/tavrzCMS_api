package ru.fds.tavrzcms3.specification;

import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T>{
    void with(SearchCriteria searchCriteria);
    void withNestedAttribute(SearchCriteriaNestedAttribute searchCriteriaNestedAttribute);
    Specification<T> build();
}
