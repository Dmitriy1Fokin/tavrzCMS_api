package ru.fds.tavrzcms3.domain;

import java.util.Date;

public interface DomainWithDate extends Domain{
    Date getDate();
    void setDate(Date date);
}
