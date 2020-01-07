package ru.fds.tavrzcms3.dictionary.excelproprities.encumbrance;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public final class EncumbranceNewColumn {

    @Value("${excel_table.import.encumbrance_new.pledge_subject_id}")
    int pledgeSubjectId;
    @Value("${excel_table.import.encumbrance_new.name}")
    int name;
    @Value("${excel_table.import.encumbrance_new.type_of_encumbrance}")
    int typeOfEncumbrance;
    @Value("${excel_table.import.encumbrance_new.in_favor_of_whom}")
    int inFavorOfWhom;
    @Value("${excel_table.import.encumbrance_new.date_begin}")
    int dateBegin;
    @Value("${excel_table.import.encumbrance_new.date_end}")
    int dateEnd;
    @Value("${excel_table.import.encumbrance_new.num_of_encumbrance}")
    int numOfEncumbrance;
}
