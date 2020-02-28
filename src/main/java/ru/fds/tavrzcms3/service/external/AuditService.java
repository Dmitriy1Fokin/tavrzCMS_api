package ru.fds.tavrzcms3.service.external;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.fds.tavrzcms3.dto.external.AuditResultDto;
import ru.fds.tavrzcms3.service.MessageService;

import java.util.Collection;

@Service
public class AuditService {
    private final AuditFeignService auditFeignService;
    private final MessageService messageService;

    public AuditService(AuditFeignService auditFeignService,
                        MessageService messageService) {
        this.auditFeignService = auditFeignService;
        this.messageService = messageService;
    }

    public Collection<AuditResultDto> getAuditResultAboutLoanAgreement(Long id){
        return auditFeignService.getAuditResultAboutLoanAgreement(id);
    }

    public Collection<AuditResultDto> getActualAuditResultAboutLoanAgreement(Long id){
        return auditFeignService.getActualAuditResultAboutLoanAgreement(id);
    }

    public Collection<AuditResultDto> getIgnoreAuditResultAboutLoanAgreement(Long id){
        return auditFeignService.getIgnoreAuditResultAboutLoanAgreement(id);
    }


    public Collection<AuditResultDto> getAuditResultAboutPledgeAgreement(Long id){
        return auditFeignService.getAuditResultAboutPledgeAgreement(id);
    }

    public Collection<AuditResultDto> getActualAuditResultAboutPledgeAgreement(Long id){
        return auditFeignService.getActualAuditResultAboutPledgeAgreement(id);
    }

    public Collection<AuditResultDto> getIgnoreAuditResultAboutPledgeAgreement(Long id){
        return auditFeignService.getIgnoreAuditResultAboutPledgeSubject(id);
    }

    public Collection<AuditResultDto> getAuditResultAboutPledgeSubject(Long id){
        return auditFeignService.getAuditResultAboutPledgeSubject(id);
    }

    public Collection<AuditResultDto> getActualAuditResultAboutPledgeSubject(Long id){
        return auditFeignService.getActualAuditResultAboutPledgeSubject(id);
    }

    public Collection<AuditResultDto> getIgnoreAuditResultAboutPledgeSubject(Long id){
        return auditFeignService.getIgnoreAuditResultAboutPledgeSubject(id);
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

    public void executeAudit(){
        messageService.sendExecuteAudit();
    }

}
