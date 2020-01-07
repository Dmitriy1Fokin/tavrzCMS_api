package ru.fds.tavrzcms3.dictionary.excelproprities.client.newclient;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public final class IndividualNew {

    @Value("${excel_table.import.client_new.individual.surname}")
    int surname;
    @Value("${excel_table.import.client_new.individual.name}")
    int name;
    @Value("${excel_table.import.client_new.individual.patronymic}")
    int patronymic;
    @Value("${excel_table.import.client_new.individual.pasport_number}")
    int pasport;
    
}
