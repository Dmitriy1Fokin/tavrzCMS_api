package ru.fds.tavrzcms3.converver;

import org.springframework.stereotype.Component;
import ru.fds.tavrzcms3.domain.*;
import ru.fds.tavrzcms3.dto.PledgeSubjectVesselDto;
import ru.fds.tavrzcms3.service.*;

import java.util.List;

@Component
public class PledgeSubjectVesselConverter implements ConverterDto<PledgeSubjectVessel, PledgeSubjectVesselDto> {

    private final PledgeAgreementService pledgeAgreementService;
    private final CostHistoryService costHistoryService;
    private final MonitoringService monitoringService;
    private final EncumbranceService encumbranceService;
    private final InsuranceService insuranceService;
    private final PledgeSubjectConverterDto pledgeSubjectConverterDto;

    public PledgeSubjectVesselConverter(PledgeAgreementService pledgeAgreementService,
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
    public PledgeSubjectVessel toEntity(PledgeSubjectVesselDto dto) {
        List<PledgeAgreement> pledgeAgreementCollection = pledgeAgreementService.getPledgeAgreementsByIds(dto.getPledgeSubjectDto().getPledgeAgreementsIds());
        List<CostHistory> historyCollection = costHistoryService.getCostHistorybyIds(dto.getPledgeSubjectDto().getCostHistoriesIds());
        List<Monitoring> monitoringCollection = monitoringService.getMonitoringByIds(dto.getPledgeSubjectDto().getMonitoringIds());
        List<Encumbrance> encumbranceCollection = encumbranceService.getEncumbranceByIds(dto.getPledgeSubjectDto().getEncumbrancesIds());
        List<Insurance> insuranceCollection = insuranceService.getInsurancesByIds(dto.getPledgeSubjectDto().getInsurancesIds());

        return PledgeSubjectVessel.builder()
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
                .imo(dto.getImo())
                .mmsi(dto.getMmsi())
                .flag(dto.getFlag())
                .vesselType(dto.getVesselType())
                .grossTonnage(dto.getGrossTonnage())
                .deadweight(dto.getDeadweight())
                .yearBuilt(dto.getYearBuilt())
                .statusVessel(dto.getStatusVessel())
                .build();
    }

    @Override
    public PledgeSubjectVesselDto toDto(PledgeSubjectVessel entity) {

        return PledgeSubjectVesselDto.builder()
                .pledgeSubjectDto(pledgeSubjectConverterDto.toDto(entity))
                .imo(entity.getImo())
                .mmsi(entity.getMmsi())
                .flag(entity.getFlag())
                .vesselType(entity.getVesselType())
                .grossTonnage(entity.getGrossTonnage())
                .deadweight(entity.getDeadweight())
                .yearBuilt(entity.getYearBuilt())
                .statusVessel(entity.getStatusVessel())
                .build();
    }
}
