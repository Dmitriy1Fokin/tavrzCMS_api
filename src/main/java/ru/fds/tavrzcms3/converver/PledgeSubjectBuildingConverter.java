package ru.fds.tavrzcms3.converver;

import ru.fds.tavrzcms3.domain.*;
import ru.fds.tavrzcms3.dto.PledgeSubjectBuildingDto;
import ru.fds.tavrzcms3.service.*;

import java.util.List;

public class PledgeSubjectBuildingConverter implements ConverterDto<PledgeSubjectBuilding, PledgeSubjectBuildingDto> {

    private final PledgeAgreementService pledgeAgreementService;
    private final CostHistoryService costHistoryService;
    private final MonitoringService monitoringService;
    private final EncumbranceService encumbranceService;
    private final InsuranceService insuranceService;
    private final MarketSegmentService marketSegmentService;
    private final PledgeSubjectConverterDto pledgeSubjectConverterDto;

    public PledgeSubjectBuildingConverter(PledgeAgreementService pledgeAgreementService,
                                          CostHistoryService costHistoryService,
                                          MonitoringService monitoringService,
                                          EncumbranceService encumbranceService,
                                          InsuranceService insuranceService,
                                          MarketSegmentService marketSegmentService,
                                          PledgeSubjectConverterDto pledgeSubjectConverterDto) {
        this.pledgeAgreementService = pledgeAgreementService;
        this.costHistoryService = costHistoryService;
        this.monitoringService = monitoringService;
        this.encumbranceService = encumbranceService;
        this.insuranceService = insuranceService;
        this.marketSegmentService = marketSegmentService;
        this.pledgeSubjectConverterDto = pledgeSubjectConverterDto;
    }

    @Override
    public PledgeSubjectBuilding toEntity(PledgeSubjectBuildingDto dto) {
        List<PledgeAgreement> pledgeAgreementCollection = pledgeAgreementService.getPledgeAgreementsByIds(dto.getPledgeSubjectDto().getPledgeAgreementsIds());
        List<CostHistory> historyCollection = costHistoryService.getCostHistorybyIds(dto.getPledgeSubjectDto().getCostHistoriesIds());
        List<Monitoring> monitoringCollection = monitoringService.getMonitoringByIds(dto.getPledgeSubjectDto().getMonitoringIds());
        List<Encumbrance> encumbranceCollection = encumbranceService.getEncumbranceByIds(dto.getPledgeSubjectDto().getEncumbrancesIds());
        List<Insurance> insuranceCollection = insuranceService.getInsurancesByIds(dto.getPledgeSubjectDto().getInsurancesIds());
        MarketSegment marketSegment = marketSegmentService.getMarketSegmentById(dto.getMarketSegmentId()).orElse(null);

        return PledgeSubjectBuilding.builder()
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
                .area(dto.getArea())
                .cadastralNum(dto.getCadastralNum())
                .conditionalNum(dto.getConditionalNum())
                .readinessDegree(dto.getReadinessDegree())
                .yearOfConstruction(dto.getYearOfConstruction())
                .marketSegment(marketSegment)
                .build();
    }

    @Override
    public PledgeSubjectBuildingDto toDto(PledgeSubjectBuilding entity) {

        return PledgeSubjectBuildingDto.builder()
                .pledgeSubjectDto(pledgeSubjectConverterDto.toDto(entity))
                .area(entity.getArea())
                .cadastralNum(entity.getCadastralNum())
                .conditionalNum(entity.getConditionalNum())
                .readinessDegree(entity.getReadinessDegree())
                .yearOfConstruction(entity.getYearOfConstruction())
                .marketSegmentId(entity.getMarketSegment().getMarketSegmentId())
                .build();
    }
}
