package ru.fds.tavrzcms3.converter.dtoconverter.impl;

import org.springframework.stereotype.Component;
import ru.fds.tavrzcms3.converter.dtoconverter.ConverterDto;
import ru.fds.tavrzcms3.domain.LoanAgreement;
import ru.fds.tavrzcms3.domain.PledgeAgreement;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.dto.PledgeAgreementDto;
import ru.fds.tavrzcms3.service.ClientService;
import ru.fds.tavrzcms3.service.LoanAgreementService;
import ru.fds.tavrzcms3.service.PledgeAgreementService;
import ru.fds.tavrzcms3.service.PledgeSubjectService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class PledgeAgreementConverterDto implements ConverterDto<PledgeAgreement, PledgeAgreementDto> {

    private final ClientService clientService;
    private final LoanAgreementService loanAgreementService;
    private final PledgeSubjectService pledgeSubjectService;
    private final PledgeAgreementService pledgeAgreementService;

    public PledgeAgreementConverterDto(ClientService clientService,
                                       LoanAgreementService loanAgreementService,
                                       PledgeSubjectService pledgeSubjectService,
                                       PledgeAgreementService pledgeAgreementService) {
        this.clientService = clientService;
        this.loanAgreementService = loanAgreementService;
        this.pledgeSubjectService = pledgeSubjectService;
        this.pledgeAgreementService = pledgeAgreementService;
    }

    @Override
    public PledgeAgreement toEntity(PledgeAgreementDto dto) {
        List<LoanAgreement> loanAgreementList;
        if(dto.getLoanAgreementsIds() != null)
            loanAgreementList = loanAgreementService.getLoanAgreementsByIds(dto.getLoanAgreementsIds());
        else
            loanAgreementList = Collections.emptyList();

        List<PledgeSubject> pledgeSubjectList;
        if(dto.getPledgeSubjectsIds() != null)
            pledgeSubjectList = pledgeSubjectService.getPledgeSubjectByIds(dto.getPledgeSubjectsIds());
        else
            pledgeSubjectList = Collections.emptyList();

        return PledgeAgreement.builder()
                .pledgeAgreementId(dto.getPledgeAgreementId())
                .numPA(dto.getNumPA())
                .dateBeginPA(dto.getDateBeginPA())
                .dateEndPA(dto.getDateEndPA())
                .pervPosl(dto.getPervPosl())
                .statusPA(dto.getStatusPA())
                .noticePA(dto.getNoticePA())
                .zsDz(dto.getZsDz())
                .zsZz(dto.getZsZz())
                .rsDz(dto.getRsDz())
                .rsZz(dto.getRsZz())
                .ss(dto.getSs())
                .loanAgreements(loanAgreementList)
                .client(clientService.getClientById(dto.getClientId()).orElse(null))
                .pledgeSubjects(pledgeSubjectList)
                .build();
    }

    @Override
    public PledgeAgreementDto toDto(PledgeAgreement entity) {
        List<Long> loanAgreementDtoList = new ArrayList<>();
        for(LoanAgreement la : loanAgreementService.getAllLoanAgreementByPledgeAgreement(entity))
            loanAgreementDtoList.add(la.getLoanAgreementId());

        List<Long> pledgeSubjectDtoList = new ArrayList<>();
        List<PledgeSubject> pledgeSubjectList = pledgeSubjectService.getPledgeSubjectByPledgeAgreement(entity);
        for (PledgeSubject ps : pledgeSubjectList)
            pledgeSubjectDtoList.add(ps.getPledgeSubjectId());

        List<String> briefInfoAboutCollateral = new ArrayList<>();
        for(int i = 0; i < pledgeSubjectList.size() && i <= 5; i++){
            briefInfoAboutCollateral.add(pledgeSubjectList.get(i).getName());
        }

        List<String> typesOfCollateral = pledgeAgreementService.getTypeOfCollateral(entity);

        List<LocalDate> datesOfConclusions = pledgeAgreementService.getDatesOfConclusion(entity);

        List<LocalDate> datesOfMonitoring = pledgeAgreementService.getDatesOfMonitoring(entity);

        List<String> resultsOfMonitoring = pledgeAgreementService.getResultsOfMonitoring(entity);

        return PledgeAgreementDto.builder()
                .pledgeAgreementId(entity.getPledgeAgreementId())
                .numPA(entity.getNumPA())
                .dateBeginPA(entity.getDateBeginPA())
                .dateEndPA(entity.getDateEndPA())
                .pervPosl(entity.getPervPosl())
                .statusPA(entity.getStatusPA())
                .noticePA(entity.getNoticePA())
                .zsDz(entity.getZsDz())
                .zsZz(entity.getZsZz())
                .rsDz(entity.getRsDz())
                .rsZz(entity.getRsZz())
                .ss(entity.getSs())
                .loanAgreementsIds(loanAgreementDtoList)
                .clientId(entity.getClient().getClientId())
                .pledgeSubjectsIds(pledgeSubjectDtoList)
                .briefInfoAboutCollateral(briefInfoAboutCollateral)
                .typesOfCollateral(typesOfCollateral)
                .datesOfConclusions(datesOfConclusions)
                .datesOfMonitoring(datesOfMonitoring)
                .resultsOfMonitoring(resultsOfMonitoring)
                .clientName(clientService.getFullNameClient(entity.getClient().getClientId()))
                .build();
    }
}
