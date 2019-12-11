package ru.fds.tavrzcms3.converver;

import org.springframework.stereotype.Component;
import ru.fds.tavrzcms3.domain.*;
import ru.fds.tavrzcms3.dto.MainCharacteristic;
import ru.fds.tavrzcms3.dto.PledgeSubjectDto;
import ru.fds.tavrzcms3.dto.PrimaryIdentifier;
import ru.fds.tavrzcms3.service.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class PledgeSubjectConverterDto implements ConverterDto<PledgeSubject, PledgeSubjectDto> {

    private final PledgeAgreementService pledgeAgreementService;
    private final CostHistoryService costHistoryService;
    private final MonitoringService monitoringService;
    private final EncumbranceService encumbranceService;
    private final InsuranceService insuranceService;

    public PledgeSubjectConverterDto(PledgeAgreementService pledgeAgreementService,
                                     CostHistoryService costHistoryService,
                                     MonitoringService monitoringService,
                                     EncumbranceService encumbranceService,
                                     InsuranceService insuranceService) {
        this.pledgeAgreementService = pledgeAgreementService;
        this.costHistoryService = costHistoryService;
        this.monitoringService = monitoringService;
        this.encumbranceService = encumbranceService;
        this.insuranceService = insuranceService;
    }

    @Override
    public PledgeSubject toEntity(PledgeSubjectDto dto) {
        List<PledgeAgreement> pledgeAgreementCollection = pledgeAgreementService.getPledgeAgreementsByIds(dto.getPledgeAgreementsIds());
        List<CostHistory> historyCollection = costHistoryService.getCostHistorybyIds(dto.getCostHistoriesIds());
        List<Monitoring> monitoringCollection = monitoringService.getMonitoringByIds(dto.getMonitoringIds());
        List<Encumbrance> encumbranceCollection = encumbranceService.getEncumbranceByIds(dto.getEncumbrancesIds());
        List<Insurance> insuranceCollection = insuranceService.getInsurancesByIds(dto.getInsurancesIds());

        return PledgeSubject.builder()
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
                .build();
    }

    @Override
    public PledgeSubjectDto toDto(PledgeSubject entity) {
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

        return PledgeSubjectDto.builder()
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
                .fullAddress(getFullAddress(entity))
                .mainCharacteristic(getMainCharacteristic(entity))
                .primaryIdentifier(getPrimaryIdentifier(entity))
                .build();
    }

    private String getFullAddress(PledgeSubject entity){
        StringBuilder fullAddress = new StringBuilder();
        if(Objects.nonNull(entity.getAdressRegion()) && !entity.getAdressRegion().isEmpty())
            fullAddress.append(entity.getAdressRegion());
        if(Objects.nonNull(entity.getAdressDistrict()) && !entity.getAdressDistrict().isEmpty())
            fullAddress.append(", ").append(entity.getAdressDistrict());
        if(Objects.nonNull(entity.getAdressCity()) && !entity.getAdressCity().isEmpty())
            fullAddress.append(", ").append(entity.getAdressCity());
        if(Objects.nonNull(entity.getAdressStreet()) && !entity.getAdressStreet().isEmpty())
            fullAddress.append(", ").append(entity.getAdressStreet());
        if(Objects.nonNull(entity.getAdressBuilbing()) && !entity.getAdressBuilbing().isEmpty())
            fullAddress.append(", ").append(entity.getAdressBuilbing());
        if(Objects.nonNull(entity.getAdressPemises()) && !entity.getAdressPemises().isEmpty())
            fullAddress.append(", ").append(entity.getAdressPemises());

        return fullAddress.toString();
    }

    private MainCharacteristic getMainCharacteristic(PledgeSubject entity){
        String mainCharacteristic = "-";
        String typeOfMainCharacteristic = "-";
        String typeOfMainCharacteristicForRealty = "кв.м.";

        if(entity instanceof PledgeSubjectAuto){
            if(Objects.nonNull(((PledgeSubjectAuto) entity).getHorsepower()))
                mainCharacteristic = String.valueOf(((PledgeSubjectAuto) entity).getHorsepower());
            typeOfMainCharacteristic = "л.с.";

        }else if(entity instanceof PledgeSubjectEquipment){
            if(Objects.nonNull(((PledgeSubjectEquipment) entity).getProductivity()))
                mainCharacteristic = String.valueOf(((PledgeSubjectEquipment) entity).getProductivity());
            if(Objects.nonNull(((PledgeSubjectEquipment) entity).getTypeOfProductivity())
                    && !((PledgeSubjectEquipment) entity).getTypeOfProductivity().isEmpty())
                typeOfMainCharacteristic = ((PledgeSubjectEquipment) entity).getTypeOfProductivity();

        }else if(entity instanceof PledgeSubjectBuilding){
            mainCharacteristic = String.valueOf(((PledgeSubjectBuilding) entity).getArea());
            typeOfMainCharacteristic = typeOfMainCharacteristicForRealty;

        }else if(entity instanceof PledgeSubjectLandLease){
            mainCharacteristic = String.valueOf(((PledgeSubjectLandLease) entity).getArea());
            typeOfMainCharacteristic = typeOfMainCharacteristicForRealty;

        }else if(entity instanceof PledgeSubjectLandOwnership){
            mainCharacteristic = String.valueOf(((PledgeSubjectLandOwnership) entity).getArea());
            typeOfMainCharacteristic = typeOfMainCharacteristicForRealty;

        }else if(entity instanceof PledgeSubjectRoom){
            mainCharacteristic = String.valueOf(((PledgeSubjectRoom) entity).getArea());
            typeOfMainCharacteristic = typeOfMainCharacteristicForRealty;

        }else if(entity instanceof PledgeSubjectTBO){
            if(Objects.nonNull(((PledgeSubjectTBO) entity).getCountOfTBO()))
                mainCharacteristic = String.valueOf(((PledgeSubjectTBO) entity).getCountOfTBO());
            typeOfMainCharacteristic = "кол-во";

        }else if(entity instanceof PledgeSubjectVessel){
            mainCharacteristic = String.valueOf(((PledgeSubjectVessel) entity).getDeadweight());
            typeOfMainCharacteristic = "дейдвейт";
        }

        return new MainCharacteristic(mainCharacteristic, typeOfMainCharacteristic);
    }

    private PrimaryIdentifier getPrimaryIdentifier(PledgeSubject entity){
        String primaryIdentifier = "-";
        String typeOfPrimaryIdentifier = "-";
        String typeOfPrimaryIdentifierForRealty = "Кад№";

        if(entity instanceof PledgeSubjectAuto){
            primaryIdentifier = String.valueOf(((PledgeSubjectAuto) entity).getVin());
            typeOfPrimaryIdentifier = "VIN";

        }else if(entity instanceof PledgeSubjectEquipment){
            if(Objects.nonNull(((PledgeSubjectEquipment) entity).getSerialNum())
                    && !((PledgeSubjectEquipment) entity).getSerialNum().isEmpty())
                primaryIdentifier = String.valueOf(((PledgeSubjectEquipment) entity).getSerialNum());
            typeOfPrimaryIdentifier = "Завод№";

        }else if(entity instanceof PledgeSubjectBuilding){
            primaryIdentifier = String.valueOf(((PledgeSubjectBuilding) entity).getCadastralNum());
            typeOfPrimaryIdentifier = typeOfPrimaryIdentifierForRealty;

        }else if(entity instanceof PledgeSubjectLandLease){
            primaryIdentifier = String.valueOf(((PledgeSubjectLandLease) entity).getCadastralNum());
            typeOfPrimaryIdentifier = typeOfPrimaryIdentifierForRealty;

        }else if(entity instanceof PledgeSubjectLandOwnership){
            primaryIdentifier = String.valueOf(((PledgeSubjectLandOwnership) entity).getCadastralNum());
            typeOfPrimaryIdentifier = typeOfPrimaryIdentifierForRealty;

        }else if(entity instanceof PledgeSubjectRoom){
            primaryIdentifier = String.valueOf(((PledgeSubjectRoom) entity).getCadastralNum());
            typeOfPrimaryIdentifier = typeOfPrimaryIdentifierForRealty;

        }else if(entity instanceof PledgeSubjectVessel){
            primaryIdentifier = String.valueOf(((PledgeSubjectVessel) entity).getImo());
            typeOfPrimaryIdentifier = "IMO";
        }

        return new PrimaryIdentifier(primaryIdentifier, typeOfPrimaryIdentifier);
    }
}
