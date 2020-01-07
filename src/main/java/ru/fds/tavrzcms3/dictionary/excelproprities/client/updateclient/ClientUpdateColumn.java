package ru.fds.tavrzcms3.dictionary.excelproprities.client.updateclient;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public final class ClientUpdateColumn {
    IndividualUpdate individual;
    LegalEntityUpdate legalEntity;

    public ClientUpdateColumn(IndividualUpdate individual,
                              LegalEntityUpdate legalEntity){
        this.individual = individual;
        this.legalEntity = legalEntity;
    }

    @Value("${excel_table.import.client_update.client_id}")
    int clientId;
    @Value("${excel_table.import.client_update.client_manager_id}")
    int clientManager;
    @Value("${excel_table.import.client_update.employee_id}")
    int employee;
}
