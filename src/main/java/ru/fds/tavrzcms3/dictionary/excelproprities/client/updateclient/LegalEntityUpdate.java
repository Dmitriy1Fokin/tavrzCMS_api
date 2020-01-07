package ru.fds.tavrzcms3.dictionary.excelproprities.client.updateclient;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public final class LegalEntityUpdate {
    
    @Value("${excel_table.import.client_update.legal_entity.organizational_form}")
    int organizationForm;
    @Value("${excel_table.import.client_update.legal_entity.name}")
    int name;
    @Value("${excel_table.import.client_update.legal_entity.inn}")
    int inn;
}
