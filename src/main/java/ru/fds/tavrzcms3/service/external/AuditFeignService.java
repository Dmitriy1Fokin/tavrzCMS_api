package ru.fds.tavrzcms3.service.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.fds.tavrzcms3.dto.external.AuditResultDto;

import java.util.Collection;

@FeignClient("${feign_tavrz_cms_audit.name}")
public interface AuditFeignService {

    @GetMapping("/audit/loan_agreement")
    Collection<AuditResultDto> getAuditResultAboutLoanAgreement(@RequestParam("loanAgreementId") Long loanAgreementId);

    @GetMapping("/audit/pledge_agreement")
    Collection<AuditResultDto> getAuditResultAboutPledgeAgreement(@RequestParam("pledgeAgreementId") Long pledgeAgreementId);

    @GetMapping("/audit/pledge_subject")
    Collection<AuditResultDto> getAuditResultAboutPledgeSubject(@RequestParam("pledgeSubjectId") Long pledgeSubjectId);

    @GetMapping("/audit")
    Collection<AuditResultDto> getAuditResult(Pageable pageable);

    @PutMapping("/audit/ignore")
    AuditResultDto setIgnore(@RequestParam("auditResultId") String auditResultId);

    @PutMapping("/audit/actual")
    AuditResultDto setActual(@RequestParam("auditResultId") String auditResultId);
}
