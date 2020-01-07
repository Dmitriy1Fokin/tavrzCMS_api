package ru.fds.tavrzcms3.dictionary.excelproprities.client.newclient;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public final class LegalEntityNew {

    @Value("${excel_table.import.client_new.legal_entity.organizational_form}")
    int organizationForm;
    @Value("${excel_table.import.client_new.legal_entity.name}")
    int name;
    @Value("${excel_table.import.client_new.legal_entity.inn}")
    int inn;
}
