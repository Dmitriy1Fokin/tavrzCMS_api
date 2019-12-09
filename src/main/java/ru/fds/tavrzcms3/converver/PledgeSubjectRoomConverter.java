package ru.fds.tavrzcms3.converver;

import ru.fds.tavrzcms3.domain.*;
import ru.fds.tavrzcms3.dto.PledgeSubjectRoomDto;
import ru.fds.tavrzcms3.service.*;

import java.util.ArrayList;
import java.util.List;

public class PledgeSubjectRoomConverter implements ConverterDto<PledgeSubjectRoom, PledgeSubjectRoomDto> {
    private final PledgeAgreementService pledgeAgreementService;
    private final CostHistoryService costHistoryService;
    private final MonitoringService monitoringService;
    private final EncumbranceService encumbranceService;
    private final InsuranceService insuranceService;
    private final MarketSegmentService marketSegmentService;

    public PledgeSubjectRoomConverter(PledgeAgreementService pledgeAgreementService,
                                          CostHistoryService costHistoryService,
                                          MonitoringService monitoringService,
                                          EncumbranceService encumbranceService,
                                          InsuranceService insuranceService,
                                          MarketSegmentService marketSegmentService) {
        this.pledgeAgreementService = pledgeAgreementService;
        this.costHistoryService = costHistoryService;
        this.monitoringService = monitoringService;
        this.encumbranceService = encumbranceService;
        this.insuranceService = insuranceService;
        this.marketSegmentService = marketSegmentService;
    }

    @Override
    public PledgeSubjectRoom toEntity(PledgeSubjectRoomDto dto) {
        List<PledgeAgreement> pledgeAgreementCollection = pledgeAgreementService.getPledgeAgreementsByIds(dto.getPledgeAgreementsIds());
        List<CostHistory> historyCollection = costHistoryService.getCostHistorybyIds(dto.getCostHistoriesIds());
        List<Monitoring> monitoringCollection = monitoringService.getMonitoringByIds(dto.getMonitoringIds());
        List<Encumbrance> encumbranceCollection = encumbranceService.getEncumbranceByIds(dto.getEncumbrancesIds());
        List<Insurance> insuranceCollection = insuranceService.getInsurancesByIds(dto.getInsurancesIds());
        MarketSegment marketSegmentRoom = marketSegmentService.getMarketSegmentById(dto.getMarketSegmentRoom()).orElse(null);
        MarketSegment marketSegmentBuilding = marketSegmentService.getMarketSegmentById(dto.getMarketSegmentBuilding()).orElse(null);

        return PledgeSubjectRoom.builder()
                .pledgeSubjectId(dto.getPledgeSubjectId())
                .name(dto.getName())
                .liquidity(dto.getLiquidity())
                .rsDz(dto.getRsDz())
                .rsZz(dto.getRsZz())
                .zsDz(dto.getZsDz())
                .zsZz(dto.getZsZz())
                .ss(dto.getSs())
                .dateMonitoring(dto.getDateMonitoring())
                .dateConclusion(dto.getDateConclusion())
                .statusMonitoring(dto.getStatusMonitoring())
                .typeOfCollateral(dto.getTypeOfCollateral())
                .typeOfPledge(dto.getTypeOfPledge())
                .typeOfMonitoring(dto.getTypeOfMonitoring())
                .adressRegion(dto.getAdressRegion())
                .adressDistrict(dto.getAdressDistrict())
                .adressCity(dto.getAdressCity())
                .adressStreet(dto.getAdressStreet())
                .adressBuilbing(dto.getAdressBuilbing())
                .adressPemises(dto.getAdressPemises())
                .insuranceObligation(dto.getInsuranceObligation())
                .pledgeAgreements(pledgeAgreementCollection)
                .costHistories(historyCollection)
                .monitorings(monitoringCollection)
                .encumbrances(encumbranceCollection)
                .insurances(insuranceCollection)
                .area(dto.getArea())
                .cadastralNum(dto.getCadastralNum())
                .conditionalNum(dto.getConditionalNum())
                .floorLocation(dto.getFloorLocation())
                .marketSegmentRoom(marketSegmentRoom)
                .marketSegmentBuilding(marketSegmentBuilding)
                .build();
    }

    @Override
    public PledgeSubjectRoomDto toDto(PledgeSubjectRoom entity) {
        List<Long> pledgeAgreementsIds = new ArrayList<>();
        for (PledgeAgreement pa : pledgeAgreementService.getAllPledgeAgreementByPLedgeSubject(entity))
            pledgeAgreementsIds.add(pa.getPledgeAgreementId());

        List<Long> costHistoriesIds = new ArrayList<>();
        for (CostHistory ch : costHistoryService.getCostHistoryPledgeSubject(entity))
            costHistoriesIds.add(ch.getCostHistoryId());

        List<Long> monitoringIds = new ArrayList<>();
        for(Monitoring mon : monitoringService.getMonitoringByPledgeSubject(entity))
            monitoringIds.add(mon.getMonitoringId());

        List<Long> encumbrancesIds = new ArrayList<>();
        for(Encumbrance enc : encumbranceService.getEncumbranceByPledgeSubject(entity))
            encumbrancesIds.add(enc.getEncumbranceId());

        List<Long> insurancesIds = new ArrayList<>();
        for(Insurance ins : insuranceService.getInsurancesByPledgeSubject(entity))
            insurancesIds.add(ins.getInsuranceId());

        StringBuilder fullAddress = new StringBuilder();
        if(!entity.getAdressRegion().isEmpty())
            fullAddress.append(entity.getAdressRegion());
        if(!entity.getAdressDistrict().isEmpty())
            fullAddress.append(", ").append(entity.getAdressDistrict());
        if(!entity.getAdressCity().isEmpty())
            fullAddress.append(", ").append(entity.getAdressCity());
        if(!entity.getAdressStreet().isEmpty())
            fullAddress.append(", ").append(entity.getAdressStreet());
        if(!entity.getAdressBuilbing().isEmpty())
            fullAddress.append(", ").append(entity.getAdressBuilbing());
        if(!entity.getAdressPemises().isEmpty())
            fullAddress.append(", ").append(entity.getAdressPemises());

        return PledgeSubjectRoomDto.builder()
                .pledgeSubjectId(entity.getPledgeSubjectId())
                .name(entity.getName())
                .liquidity(entity.getLiquidity())
                .rsDz(entity.getRsDz())
                .rsZz(entity.getRsZz())
                .zsDz(entity.getZsDz())
                .zsZz(entity.getZsZz())
                .ss(entity.getSs())
                .dateMonitoring(entity.getDateMonitoring())
                .dateConclusion(entity.getDateConclusion())
                .statusMonitoring(entity.getStatusMonitoring())
                .typeOfCollateral(entity.getTypeOfCollateral())
                .typeOfPledge(entity.getTypeOfPledge())
                .typeOfMonitoring(entity.getTypeOfMonitoring())
                .adressRegion(entity.getAdressRegion())
                .adressDistrict(entity.getAdressDistrict())
                .adressCity(entity.getAdressCity())
                .adressStreet(entity.getAdressStreet())
                .adressBuilbing(entity.getAdressBuilbing())
                .adressPemises(entity.getAdressPemises())
                .insuranceObligation(entity.getInsuranceObligation())
                .pledgeAgreementsIds(pledgeAgreementsIds)
                .costHistoriesIds(costHistoriesIds)
                .monitoringIds(monitoringIds)
                .encumbrancesIds(encumbrancesIds)
                .insurancesIds(insurancesIds)
                .area(entity.getArea())
                .cadastralNum(entity.getCadastralNum())
                .conditionalNum(entity.getConditionalNum())
                .floorLocation(entity.getFloorLocation())
                .marketSegmentRoom(entity.getMarketSegmentRoom().getMarketSegmentId())
                .marketSegmentBuilding(entity.getMarketSegmentBuilding().getMarketSegmentId())
                .fullAddress(fullAddress.toString())
                .build();
    }
}
