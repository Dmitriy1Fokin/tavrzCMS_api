package ru.fds.tavrzcms3.service;

public interface MessageService {
    void sendNewLoanAgreement(Long loanAgreementId);
    void sendExistLoanAgreement(Long loanAgreementId);
    void sendNewPledgeAgreement(Long pledgeAgreementId);
    void sendExistPledgeAgreement(Long pledgeAgreementId);
    void sendNewPledgeSubject(Long pledgeSubjectId);
    void sendExistPledgeSubject(Long pledgeSubjectId);
    void sendExecuteAudit();

}
