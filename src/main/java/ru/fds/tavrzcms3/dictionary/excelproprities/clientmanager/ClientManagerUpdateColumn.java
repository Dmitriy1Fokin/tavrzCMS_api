package ru.fds.tavrzcms3.dictionary.excelproprities.clientmanager;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public final class ClientManagerUpdateColumn {

    @Value("${excel_table.import.client_manager_update.client_manager_id}")
    int clientManagerId;
    @Value("${excel_table.import.client_manager_update.surname}")
    int surname;
    @Value("${excel_table.import.client_manager_update.name}")
    int name;
    @Value("${excel_table.import.client_manager_update.patronymic}")
    int patronymic;
}
