package ru.fds.tavrzcms3.dictionary.excelproprities.client.updateclient;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public final class IndividualUpdate {

    @Value("${excel_table.import.client_update.individual.surname}")
    int surname;
    @Value("${excel_table.import.client_update.individual.name}")
    int name;
    @Value("${excel_table.import.client_update.individual.patronymic}")
    int patronymic;
    @Value("${excel_table.import.client_update.individual.pasport_number}")
    int pasport;
}
