package ru.fds.tavrzcms3.specification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import ru.fds.tavrzcms3.dictionary.Operations;

@AllArgsConstructor
@Getter
@Builder
public class SearchCriteriaNestedAttribute {
    private String nestedObjectName;
    private String key;
    private Operations operation;
    private Object value;
    private boolean predicate;
}
