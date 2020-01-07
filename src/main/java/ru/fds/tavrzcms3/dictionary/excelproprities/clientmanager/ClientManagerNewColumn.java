package ru.fds.tavrzcms3.dictionary.excelproprities.clientmanager;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public final class ClientManagerNewColumn {

    @Value("${excel_table.import.client_manager_new.surname}")
    int surname;
    @Value("${excel_table.import.client_manager_new.name}")
    int name;
    @Value("${excel_table.import.client_manager_new.patronymic}")
    int patronymic;
}
