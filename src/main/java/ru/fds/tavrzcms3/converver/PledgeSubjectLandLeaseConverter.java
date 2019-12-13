package ru.fds.tavrzcms3.converver;

import org.springframework.stereotype.Component;
import ru.fds.tavrzcms3.domain.*;
import ru.fds.tavrzcms3.dto.PledgeSubjectLandLeaseDto;
import ru.fds.tavrzcms3.service.*;

import java.util.List;

@Component
public class PledgeSubjectLandLeaseConverter implements ConverterDto<PledgeSubjectLandLease, PledgeSubjectLandLeaseDto> {

    private final PledgeAgreementService pledgeAgreementService;
    private final CostHistoryService costHistoryService;
    private final MonitoringService monitoringService;
    private final EncumbranceService encumbranceService;
    private final InsuranceService insuranceService;
    private final LandCategoryService landCategoryService;
    private final PledgeSubjectConverterDto pledgeSubjectConverterDto;

    public PledgeSubjectLandLeaseConverter(PledgeAgreementService pledgeAgreementService,
                                           CostHistoryService costHistoryService,
                                           MonitoringService monitoringService,
                                           EncumbranceService encumbranceService,
                                           InsuranceService insuranceService,
                                           LandCategoryService landCategoryService,
                                           PledgeSubjectConverterDto pledgeSubjectConverterDto) {
        this.pledgeAgreementService = pledgeAgreementService;
        this.costHistoryService = costHistoryService;
        this.monitoringService = monitoringService;
        this.encumbranceService = encumbranceService;
        this.insuranceService = insuranceService;
        this.landCategoryService = landCategoryService;
        this.pledgeSubjectConverterDto = pledgeSubjectConverterDto;
    }

    @Override
    public PledgeSubjectLandLease toEntity(PledgeSubjectLandLeaseDto dto) {
        List<PledgeAgreement> pledgeAgreementCollection = pledgeAgreementService.getPledgeAgreementsByIds(dto.getPledgeSubjectDto().getPledgeAgreementsIds());
        List<CostHistory> historyCollection = costHistoryService.getCostHistorybyIds(dto.getPledgeSubjectDto().getCostHistoriesIds());
        List<Monitoring> monitoringCollection = monitoringService.getMonitoringByIds(dto.getPledgeSubjectDto().getMonitoringIds());
        List<Encumbrance> encumbranceCollection = encumbranceService.getEncumbranceByIds(dto.getPledgeSubjectDto().getEncumbrancesIds());
        List<Insurance> insuranceCollection = insuranceService.getInsurancesByIds(dto.getPledgeSubjectDto().getInsurancesIds());

        return PledgeSubjectLandLease.builder()
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
                .permittedUse(dto.getPermittedUse())
                .builtUp(dto.getBuiltUp())
                .cadastralNumOfBuilding(dto.getCadastralNumOfBuilding())
                .dateBeginLease(dto.getDateBeginLease())
                .dateEndLease(dto.getDateEndLease())
                .landCategory(dto.getLandCategory())
                .build();
    }

    @Override
    public PledgeSubjectLandLeaseDto toDto(PledgeSubjectLandLease entity) {

        return PledgeSubjectLandLeaseDto.builder()
                .pledgeSubjectDto(pledgeSubjectConverterDto.toDto(entity))
                .area(entity.getArea())
                .cadastralNum(entity.getCadastralNum())
                .conditionalNum(entity.getConditionalNum())
                .permittedUse(entity.getPermittedUse())
                .builtUp(entity.getBuiltUp())
                .cadastralNumOfBuilding(entity.getCadastralNumOfBuilding())
                .dateBeginLease(entity.getDateBeginLease())
                .dateEndLease(entity.getDateEndLease())
                .landCategory(entity.getLandCategory())
                .build();
    }
}
