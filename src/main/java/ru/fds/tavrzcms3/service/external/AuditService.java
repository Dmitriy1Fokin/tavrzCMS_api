package ru.fds.tavrzcms3.service.external;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.fds.tavrzcms3.dto.external.AuditResultDto;

import java.util.Collection;

@Service
public class AuditService {
    private final AuditFeignService auditFeignService;

    public AuditService(AuditFeignService auditFeignService) {
        this.auditFeignService = auditFeignService;
    }

    public Collection<AuditResultDto> getAuditResultAboutLoanAgreement(Long id){
        return auditFeignService.getAuditResultAboutLoanAgreement(id);
    }

    public Collection<AuditResultDto> getAuditResultAboutPledgeAgreement(Long id){
        return auditFeignService.getAuditResultAboutPledgeAgreement(id);
    }

    public Collection<AuditResultDto> getAuditResultAboutPledgeSubject(Long id){
        return auditFeignService.getAuditResultAboutPledgeSubject(id);
    }

    public Collection<AuditResultDto> getAuditResult(Pageable pageable){
        return auditFeignService.getAuditResult(pageable);
    }

    public AuditResultDto setIgnore(String id){
        return auditFeignService.setIgnore(id);
    }

    public AuditResultDto setActual(String id){
        return auditFeignService.setActual(id);
    }


}
