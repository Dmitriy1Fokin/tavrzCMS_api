package ru.fds.tavrzcms3.domain;

public interface DomainWithType extends Domain{
    String getType();
    void setType(String type);
}
