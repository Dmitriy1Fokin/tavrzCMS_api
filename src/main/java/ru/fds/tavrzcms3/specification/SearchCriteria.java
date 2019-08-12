package ru.fds.tavrzcms3.specification;

public class SearchCriteria {
    private String key;
    private String operation;
    private Object value;
    private boolean predicate;

    public SearchCriteria(String key, String operation, Object value, boolean predicate){
        this.key = key;
        this.operation = operation;
        this.value = value;
        this.predicate = predicate;
    }

    public SearchCriteria(){}


    public String getKey() {
        return key;
    }

    public String getOperation() {
        return operation;
    }

    public Object getValue() {
        return value;
    }

    public boolean isPredicate() {
        return predicate;
    }
}
