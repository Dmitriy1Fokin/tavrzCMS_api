package ru.fds.tavrzcms3.specification;

import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T>{
    void with(String key, String operation, Object value, boolean predicate);
    Specification<T> build();
}
