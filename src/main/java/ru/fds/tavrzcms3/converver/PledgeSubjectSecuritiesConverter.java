package ru.fds.tavrzcms3.converver;

import org.springframework.stereotype.Component;
import ru.fds.tavrzcms3.domain.*;
import ru.fds.tavrzcms3.dto.PledgeSubjectSecuritiesDto;
import ru.fds.tavrzcms3.service.*;

import java.util.List;

@Component
public class PledgeSubjectSecuritiesConverter implements ConverterDto<PledgeSubjectSecurities, PledgeSubjectSecuritiesDto> {

    private final PledgeAgreementService pledgeAgreementService;
    private final CostHistoryService costHistoryService;
    private final MonitoringService monitoringService;
    private final EncumbranceService encumbranceService;
    private final InsuranceService insuranceService;
    private final PledgeSubjectConverterDto pledgeSubjectConverterDto;

    public PledgeSubjectSecuritiesConverter(PledgeAgreementService pledgeAgreementService,
                                            CostHistoryService costHistoryService,
                                            MonitoringService monitoringService,
                                            EncumbranceService encumbranceService,
                                            InsuranceService insuranceService,
                                            PledgeSubjectConverterDto pledgeSubjectConverterDto) {
        this.pledgeAgreementService = pledgeAgreementService;
        this.costHistoryService = costHistoryService;
        this.monitoringService = monitoringService;
        this.encumbranceService = encumbranceService;
        this.insuranceService = insuranceService;
        this.pledgeSubjectConverterDto = pledgeSubjectConverterDto;
    }

    @Override
    public PledgeSubjectSecurities toEntity(PledgeSubjectSecuritiesDto dto) {
        List<PledgeAgreement> pledgeAgreementCollection = pledgeAgreementService.getPledgeAgreementsByIds(dto.getPledgeSubjectDto().getPledgeAgreementsIds());
        List<CostHistory> historyCollection = costHistoryService.getCostHistorybyIds(dto.getPledgeSubjectDto().getCostHistoriesIds());
        List<Monitoring> monitoringCollection = monitoringService.getMonitoringByIds(dto.getPledgeSubjectDto().getMonitoringIds());
        List<Encumbrance> encumbranceCollection = encumbranceService.getEncumbranceByIds(dto.getPledgeSubjectDto().getEncumbrancesIds());
        List<Insurance> insuranceCollection = insuranceService.getInsurancesByIds(dto.getPledgeSubjectDto().getInsurancesIds());

        return PledgeSubjectSecurities.builder()
                .pledgeSubjectId(dto.getPledgeSubjectDto().getPledgeSubjectId())
                .name(dto.getPledgeSubjectDto().getName())
                .liquidity(dto.getPledgeSubjectDto().getLiquidity())
                .rsDz(dto.getPledgeSubjectDto().getRsDz())
                .rsZz(dto.getPledgeSubjectDto().getRsZz())
                .zsDz(dto.getPledgeSubjectDto().getZsDz())
                .zsZz(dto.getPledgeSubjectDto().getZsZz())
                .ss(dto.getPledgeSubjectDto().getSs())
                .dateMonitoring(dto.getPledgeSubjectDto().getDateMonitoring())
                .dateConclusion(dto.getPledgeSubjectDto().getDateConclusion())
                .statusMonitoring(dto.getPledgeSubjectDto().getStatusMonitoring())
                .typeOfCollateral(dto.getPledgeSubjectDto().getTypeOfCollateral())
                .typeOfPledge(dto.getPledgeSubjectDto().getTypeOfPledge())
                .typeOfMonitoring(dto.getPledgeSubjectDto().getTypeOfMonitoring())
                .adressRegion(dto.getPledgeSubjectDto().getAdressRegion())
                .adressDistrict(dto.getPledgeSubjectDto().getAdressDistrict())
                .adressCity(dto.getPledgeSubjectDto().getAdressCity())
                .adressStreet(dto.getPledgeSubjectDto().getAdressStreet())
                .adressBuilbing(dto.getPledgeSubjectDto().getAdressBuilbing())
                .adressPemises(dto.getPledgeSubjectDto().getAdressPemises())
                .insuranceObligation(dto.getPledgeSubjectDto().getInsuranceObligation())
                .pledgeAgreements(pledgeAgreementCollection)
                .costHistories(historyCollection)
                .monitorings(monitoringCollection)
                .encumbrances(encumbranceCollection)
                .insurances(insuranceCollection)
                .nominalValue(dto.getNominalValue())
                .actualValue(dto.getActualValue())
                .typeOfSecurities(dto.getTypeOfSecurities())
                .build();
    }

    @Override
    public PledgeSubjectSecuritiesDto toDto(PledgeSubjectSecurities entity) {

        return PledgeSubjectSecuritiesDto.builder()
                .pledgeSubjectDto(pledgeSubjectConverterDto.toDto(entity))
                .nominalValue(entity.getNominalValue())
                .actualValue(entity.getActualValue())
                .typeOfSecurities(entity.getTypeOfSecurities())
                .build();
    }
}
