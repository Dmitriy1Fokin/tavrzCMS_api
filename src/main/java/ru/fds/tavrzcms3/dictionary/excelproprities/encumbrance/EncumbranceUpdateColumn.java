package ru.fds.tavrzcms3.dictionary.excelproprities.encumbrance;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public final class EncumbranceUpdateColumn {

    @Value("${excel_table.import.encumbrance_update.encumbrance_id}")
    int encumbranceId;
    @Value("${excel_table.import.encumbrance_update.pledge_subject_id}")
    int pledgeSubjectId;
    @Value("${excel_table.import.encumbrance_update.name}")
    int name;
    @Value("${excel_table.import.encumbrance_update.type_of_encumbrance}")
    int typeOfEncumbrance;
    @Value("${excel_table.import.encumbrance_update.in_favor_of_whom}")
    int inFavorOfWhom;
    @Value("${excel_table.import.encumbrance_update.date_begin}")
    int dateBegin;
    @Value("${excel_table.import.encumbrance_update.date_end}")
    int dateEnd;
    @Value("${excel_table.import.encumbrance_update.num_of_encumbrance}")
    int numOfEncumbrance;
}
