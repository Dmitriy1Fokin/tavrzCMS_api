package ru.fds.tavrzcms3.dictionary.excelproprities;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.fds.tavrzcms3.dictionary.excelproprities.client.newclient.ClientNewColumn;
import ru.fds.tavrzcms3.dictionary.excelproprities.client.updateclient.ClientUpdateColumn;
import ru.fds.tavrzcms3.dictionary.excelproprities.clientmanager.ClientManagerNewColumn;
import ru.fds.tavrzcms3.dictionary.excelproprities.clientmanager.ClientManagerUpdateColumn;
import ru.fds.tavrzcms3.dictionary.excelproprities.costhistory.CostHistoryNewColumn;
import ru.fds.tavrzcms3.dictionary.excelproprities.costhistory.CostHistoryUpdateColumn;
import ru.fds.tavrzcms3.dictionary.excelproprities.encumbrance.EncumbranceNewColumn;
import ru.fds.tavrzcms3.dictionary.excelproprities.encumbrance.EncumbranceUpdateColumn;
import ru.fds.tavrzcms3.dictionary.excelproprities.insurance.InsuranceNewColumn;
import ru.fds.tavrzcms3.dictionary.excelproprities.insurance.InsuranceUpdateColumn;
import ru.fds.tavrzcms3.dictionary.excelproprities.loanagreement.LoanAgreementNewColumn;
import ru.fds.tavrzcms3.dictionary.excelproprities.loanagreement.LoanAgreementUpdateColumn;
import ru.fds.tavrzcms3.dictionary.excelproprities.monitoring.MonitoringNewColumn;
import ru.fds.tavrzcms3.dictionary.excelproprities.monitoring.MonitoringUpdateColumn;
import ru.fds.tavrzcms3.dictionary.excelproprities.pledgeagreement.PledgeAgreementNewColumn;
import ru.fds.tavrzcms3.dictionary.excelproprities.pledgeagreement.PledgeAgreementUpdateColumn;
import ru.fds.tavrzcms3.dictionary.excelproprities.pledgesubject.newps.PledgeSubjectNewColumn;
import ru.fds.tavrzcms3.dictionary.excelproprities.pledgesubject.updateps.PledgeSubjectUpdateColumn;

@Getter
@Component
public final class ExcelColumnNum {

    PledgeSubjectNewColumn pledgeSubjectNew;
    PledgeSubjectUpdateColumn pledgeSubjectUpdate;
    ClientNewColumn clientNew;
    ClientUpdateColumn clientUpdate;
    LoanAgreementNewColumn loanAgreementNew;
    LoanAgreementUpdateColumn loanAgreementUpdate;
    PledgeAgreementNewColumn pledgeAgreementNew;
    PledgeAgreementUpdateColumn pledgeAgreementUpdate;
    CostHistoryNewColumn costHistoryNew;
    CostHistoryUpdateColumn costHistoryUpdate;
    MonitoringNewColumn monitoringNew;
    MonitoringUpdateColumn monitoringUpdate;
    EncumbranceNewColumn encumbranceNew;
    EncumbranceUpdateColumn encumbranceUpdate;
    InsuranceNewColumn insuranceNew;
    InsuranceUpdateColumn insuranceUpdate;
    ClientManagerNewColumn clientManagerNew;
    ClientManagerUpdateColumn clientManagerUpdate;

    public ExcelColumnNum(PledgeSubjectNewColumn pledgeSubjectNewColumn,
                          PledgeSubjectUpdateColumn pledgeSubjectUpdateColumn,
                          ClientNewColumn clientNewColumn,
                          ClientUpdateColumn clientUpdateColumn,
                          LoanAgreementNewColumn loanAgreementNewColumn,
                          LoanAgreementUpdateColumn loanAgreementUpdateColumn,
                          PledgeAgreementNewColumn pledgeAgreementNewColumn,
                          PledgeAgreementUpdateColumn pledgeAgreementUpdateColumn,
                          CostHistoryNewColumn costHistoryNewColumn,
                          CostHistoryUpdateColumn costHistoryUpdateColumn,
                          MonitoringNewColumn monitoringNewColumn,
                          MonitoringUpdateColumn monitoringUpdateColumn,
                          EncumbranceNewColumn encumbranceNewColumn,
                          EncumbranceUpdateColumn encumbranceUpdateColumn,
                          InsuranceNewColumn insuranceNewColumn,
                          InsuranceUpdateColumn insuranceUpdateColumn,
                          ClientManagerNewColumn clientManagerNewColumn,
                          ClientManagerUpdateColumn clientManagerUpdateColumn){
        pledgeSubjectNew = pledgeSubjectNewColumn;
        pledgeSubjectUpdate = pledgeSubjectUpdateColumn;
        clientNew = clientNewColumn;
        clientUpdate = clientUpdateColumn;
        loanAgreementNew = loanAgreementNewColumn;
        loanAgreementUpdate = loanAgreementUpdateColumn;
        pledgeAgreementNew = pledgeAgreementNewColumn;
        pledgeAgreementUpdate = pledgeAgreementUpdateColumn;
        costHistoryNew = costHistoryNewColumn;
        costHistoryUpdate = costHistoryUpdateColumn;
        monitoringNew = monitoringNewColumn;
        monitoringUpdate = monitoringUpdateColumn;
        encumbranceNew = encumbranceNewColumn;
        encumbranceUpdate = encumbranceUpdateColumn;
        insuranceNew = insuranceNewColumn;
        insuranceUpdate = insuranceUpdateColumn;
        clientManagerNew = clientManagerNewColumn;
        clientManagerUpdate = clientManagerUpdateColumn;
    }

    @Value("${excel_table.import.start_row}")
    int startRow;
    @Value("${excel_table.import.delimiter}")
    String delimiter;
}
