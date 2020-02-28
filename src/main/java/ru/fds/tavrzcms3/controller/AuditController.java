package ru.fds.tavrzcms3.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.fds.tavrzcms3.dto.external.AuditResultDto;
import ru.fds.tavrzcms3.service.external.AuditService;

import java.util.Collection;

@RestController
@RequestMapping("/audit")
public class AuditController {
    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping("/loan_agreement")
    public Collection<AuditResultDto> getAuditResultAboutLoanAgreement(@RequestParam("loanAgreementId") Long loanAgreementId){
        return auditService.getAuditResultAboutLoanAgreement(loanAgreementId);
    }

    @GetMapping("/loan_agreement/actual")
    public Collection<AuditResultDto> getActualAuditResultAboutLoanAgreement(@RequestParam("loanAgreementId") Long loanAgreementId){
        return auditService.getActualAuditResultAboutLoanAgreement(loanAgreementId);
    }

    @GetMapping("/loan_agreement/ignore")
    public Collection<AuditResultDto> getIgnoreAuditResultAboutLoanAgreement(@RequestParam("loanAgreementId") Long loanAgreementId){
        return auditService.getIgnoreAuditResultAboutLoanAgreement(loanAgreementId);
    }

    @GetMapping("/pledge_agreement")
    public Collection<AuditResultDto> getAuditResultAboutPledgeAgreement(@RequestParam("pledgeAgreementId") Long pledgeAgreementId){
        return auditService.getAuditResultAboutPledgeAgreement(pledgeAgreementId);
    }

    @GetMapping("/pledge_agreement/actual")
    public Collection<AuditResultDto> getActualAuditResultAboutPledgeAgreement(@RequestParam("pledgeAgreementId") Long pledgeAgreementId){
        return auditService.getActualAuditResultAboutPledgeAgreement(pledgeAgreementId);
    }

    @GetMapping("/pledge_agreement/ignore")
    public Collection<AuditResultDto> getIgnoreAuditResultAboutPledgeAgreement(@RequestParam("pledgeAgreementId") Long pledgeAgreementId){
        return auditService.getIgnoreAuditResultAboutPledgeAgreement(pledgeAgreementId);
    }

    @GetMapping("/pledge_subject")
    public Collection<AuditResultDto> getAuditResultAboutPledgeSubject(@RequestParam("pledgeSubjectId") Long pledgeSubjectId){
        return auditService.getAuditResultAboutPledgeSubject(pledgeSubjectId);
    }

    @GetMapping("/pledge_subject/actual")
    public Collection<AuditResultDto> getActualAuditResultAboutPledgeSubject(@RequestParam("pledgeSubjectId") Long pledgeSubjectId){
        return auditService.getActualAuditResultAboutPledgeSubject(pledgeSubjectId);
    }

    @GetMapping("/pledge_subject/ignore")
    public Collection<AuditResultDto> getIgnoreAuditResultAboutPledgeSubject(@RequestParam("pledgeSubjectId") Long pledgeSubjectId){
        return auditService.getIgnoreAuditResultAboutPledgeSubject(pledgeSubjectId);
    }

    @GetMapping()
    public Collection<AuditResultDto> getAuditResult(Pageable pageable){
        return auditService.getAuditResult(pageable);
    }

    @GetMapping("/executeAudit")
    public void executeAudit(){
        auditService.executeAudit();
    }

    @GetMapping("/ignore")
    public AuditResultDto setIgnore(@RequestParam("auditResultId") String auditResultId){
        return auditService.setIgnore(auditResultId);
    }

    @GetMapping("/actual")
    public AuditResultDto setActual(@RequestParam("auditResultId") String auditResultId){
        return auditService.setActual(auditResultId);
    }


}
