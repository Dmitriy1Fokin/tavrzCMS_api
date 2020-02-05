package ru.fds.tavrzcms3.service.impl;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.fds.tavrzcms3.service.MessageService;

@Component
public class MessageServiceImpl implements MessageService {

    @Value("${queue_name.audit_new_loan_agreement}")
    private String queueNewLoanAgreement;
    @Value("${queue_name.audit_exist_loan_agreement}")
    private String queueExistLoanAgreement;
    @Value("${queue_name.audit_new_pledge_agreement}")
    private String queueNewPledgeAgreement;
    @Value("${queue_name.audit_exist_pledge_agreement}")
    private String queueExistPledgeAgreement;
    @Value("${queue_name.audit_new_pledge_subject}")
    private String queueNewPledgeSubject;
    @Value("${queue_name.audit_exist_pledge_subject}")
    private String queueExistPledgeSubject;

    private final AmqpTemplate amqpTemplate;

    public MessageServiceImpl(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    @Override
    public void sendNewLoanAgreement(Long loanAgreementId) {
        amqpTemplate.convertAndSend(queueNewLoanAgreement, loanAgreementId);
    }

    @Override
    public void sendExistLoanAgreement(Long loanAgreementId) {
        amqpTemplate.convertAndSend(queueExistLoanAgreement, loanAgreementId);
    }

    @Override
    public void sendNewPledgeAgreement(Long pledgeAgreementId) {
        amqpTemplate.convertAndSend(queueNewPledgeAgreement, pledgeAgreementId);
    }

    @Override
    public void sendExistPledgeAgreement(Long pledgeAgreementId) {
        amqpTemplate.convertAndSend(queueExistPledgeAgreement, pledgeAgreementId);
    }

    @Override
    public void sendNewPledgeSubject(Long pledgeSubjectId) {
        amqpTemplate.convertAndSend(queueNewPledgeSubject, pledgeSubjectId);
    }

    @Override
    public void sendExistPledgeSubject(Long pledgeSubjectId) {
        amqpTemplate.convertAndSend(queueExistPledgeSubject, pledgeSubjectId);
    }
}
