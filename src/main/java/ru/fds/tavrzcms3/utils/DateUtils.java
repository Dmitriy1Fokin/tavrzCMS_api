package ru.fds.tavrzcms3.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DateUtils {

    public LocalDate getFirstDateInThisMonthInLastYear(){
        LocalDate now = LocalDate.now();
        return LocalDate.of(now.getYear()-1, now.getMonthValue(), 1);
    }

    public LocalDate getLastDateInThisMonthInLastYear(){
        return getFirstDateInThisMonthInLastYear().plusMonths(1);
    }

    public LocalDate getFirstDateInThisMonth(){
        LocalDate now = LocalDate.now();
        return LocalDate.of(now.getYear(), now.getMonthValue(), 1);
    }

    public LocalDate getLastDateInThisMonth(){
        return getFirstDateInThisMonth().plusMonths(1);
    }
}
