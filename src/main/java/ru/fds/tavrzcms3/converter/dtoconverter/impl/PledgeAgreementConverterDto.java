package ru.fds.tavrzcms3.converter.dtoconverter.impl;

import org.springframework.stereotype.Component;
import ru.fds.tavrzcms3.converter.dtoconverter.ConverterDto;
import ru.fds.tavrzcms3.domain.PledgeAgreement;
import ru.fds.tavrzcms3.dto.PledgeAgreementDto;
import ru.fds.tavrzcms3.service.ClientService;
import ru.fds.tavrzcms3.service.PledgeAgreementService;

import java.time.LocalDate;
import java.util.List;

@Component
public class PledgeAgreementConverterDto implements ConverterDto<PledgeAgreement, PledgeAgreementDto> {

    private final ClientService clientService;
    private final PledgeAgreementService pledgeAgreementService;

    public PledgeAgreementConverterDto(ClientService clientService,
                                       PledgeAgreementService pledgeAgreementService) {
        this.clientService = clientService;
        this.pledgeAgreementService = pledgeAgreementService;
    }

    @Override
    public PledgeAgreement toEntity(PledgeAgreementDto dto) {
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
                .client(clientService.getClientById(dto.getClientId()).orElse(null))
                .build();
    }

    @Override
    public PledgeAgreementDto toDto(PledgeAgreement entity) {
        List<String> briefInfoAboutCollateral = pledgeAgreementService.getBriefInfoAboutCollateral(entity);
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
                .clientId(entity.getClient().getClientId())
                .briefInfoAboutCollateral(briefInfoAboutCollateral)
                .typesOfCollateral(typesOfCollateral)
                .datesOfConclusions(datesOfConclusions)
                .datesOfMonitoring(datesOfMonitoring)
                .resultsOfMonitoring(resultsOfMonitoring)
                .clientName(clientService.getFullNameClient(entity.getClient().getClientId()))
                .build();
    }
}
