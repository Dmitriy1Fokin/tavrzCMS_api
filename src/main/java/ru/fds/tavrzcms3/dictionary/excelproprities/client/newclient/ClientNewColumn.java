package ru.fds.tavrzcms3.dictionary.excelproprities.client.newclient;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public final class ClientNewColumn {
    IndividualNew individual;
    LegalEntityNew legalEntity;

    public ClientNewColumn(IndividualNew individual,
                           LegalEntityNew legalEntity){
        this.individual = individual;
        this.legalEntity = legalEntity;
    }

    @Value("${excel_table.import.client_new.client_manager_id}")
    int clientManager;
    @Value("${excel_table.import.client_new.employee_id}")
    int employee;
}
