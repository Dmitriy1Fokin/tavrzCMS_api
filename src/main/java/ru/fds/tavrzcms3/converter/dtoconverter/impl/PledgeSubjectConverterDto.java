package ru.fds.tavrzcms3.converter.dtoconverter.impl;

import org.springframework.stereotype.Component;
import ru.fds.tavrzcms3.converter.dtoconverter.ConverterDto;
import ru.fds.tavrzcms3.dictionary.TypeOfCollateral;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.domain.embedded.PledgeSubjectAuto;
import ru.fds.tavrzcms3.domain.embedded.PledgeSubjectBuilding;
import ru.fds.tavrzcms3.domain.embedded.PledgeSubjectEquipment;
import ru.fds.tavrzcms3.domain.embedded.PledgeSubjectLandLease;
import ru.fds.tavrzcms3.domain.embedded.PledgeSubjectLandOwnership;
import ru.fds.tavrzcms3.domain.embedded.PledgeSubjectRoom;
import ru.fds.tavrzcms3.domain.embedded.PledgeSubjectSecurities;
import ru.fds.tavrzcms3.domain.embedded.PledgeSubjectTBO;
import ru.fds.tavrzcms3.domain.embedded.PledgeSubjectVessel;
import ru.fds.tavrzcms3.dto.MainCharacteristic;
import ru.fds.tavrzcms3.dto.PledgeSubjectAutoDto;
import ru.fds.tavrzcms3.dto.PledgeSubjectBuildingDto;
import ru.fds.tavrzcms3.dto.PledgeSubjectDto;
import ru.fds.tavrzcms3.dto.PledgeSubjectEquipmentDto;
import ru.fds.tavrzcms3.dto.PledgeSubjectLandLeaseDto;
import ru.fds.tavrzcms3.dto.PledgeSubjectLandOwnershipDto;
import ru.fds.tavrzcms3.dto.PledgeSubjectRoomDto;
import ru.fds.tavrzcms3.dto.PledgeSubjectSecuritiesDto;
import ru.fds.tavrzcms3.dto.PledgeSubjectTboDto;
import ru.fds.tavrzcms3.dto.PledgeSubjectVesselDto;
import ru.fds.tavrzcms3.dto.PrimaryIdentifier;
import java.util.Objects;

@Component
public class PledgeSubjectConverterDto implements ConverterDto<PledgeSubject, PledgeSubjectDto> {

    private final PledgeSubjectAutoConverter pledgeSubjectAutoConverter;
    private final PledgeSubjectEquipmentConverter pledgeSubjectEquipmentConverter;
    private final PledgeSubjectBuildingConverter pledgeSubjectBuildingConverter;
    private final PledgeSubjectLandLeaseConverter pledgeSubjectLandLeaseConverter;
    private final PledgeSubjectLandOwnershipConverter pledgeSubjectLandOwnershipConverter;
    private final PledgeSubjectRoomConverter pledgeSubjectRoomConverter;
    private final PledgeSubjectSecuritiesConverter pledgeSubjectSecuritiesConverter;
    private final PledgeSubjectTboConverter pledgeSubjectTboConverter;
    private final PledgeSubjectVesselConverter pledgeSubjectVesselConverter;

    public PledgeSubjectConverterDto(PledgeSubjectAutoConverter pledgeSubjectAutoConverter,
                                     PledgeSubjectEquipmentConverter pledgeSubjectEquipmentConverter,
                                     PledgeSubjectBuildingConverter pledgeSubjectBuildingConverter,
                                     PledgeSubjectLandLeaseConverter pledgeSubjectLandLeaseConverter,
                                     PledgeSubjectLandOwnershipConverter pledgeSubjectLandOwnershipConverter,
                                     PledgeSubjectRoomConverter pledgeSubjectRoomConverter,
                                     PledgeSubjectSecuritiesConverter pledgeSubjectSecuritiesConverter,
                                     PledgeSubjectTboConverter pledgeSubjectTboConverter,
                                     PledgeSubjectVesselConverter pledgeSubjectVesselConverter) {
        this.pledgeSubjectAutoConverter = pledgeSubjectAutoConverter;
        this.pledgeSubjectEquipmentConverter = pledgeSubjectEquipmentConverter;
        this.pledgeSubjectBuildingConverter = pledgeSubjectBuildingConverter;
        this.pledgeSubjectLandLeaseConverter = pledgeSubjectLandLeaseConverter;
        this.pledgeSubjectLandOwnershipConverter = pledgeSubjectLandOwnershipConverter;
        this.pledgeSubjectRoomConverter = pledgeSubjectRoomConverter;
        this.pledgeSubjectSecuritiesConverter = pledgeSubjectSecuritiesConverter;
        this.pledgeSubjectTboConverter = pledgeSubjectTboConverter;
        this.pledgeSubjectVesselConverter = pledgeSubjectVesselConverter;
    }

    @Override
    public PledgeSubject toEntity(PledgeSubjectDto dto) {
        PledgeSubjectAuto pledgeSubjectAuto = null;
        PledgeSubjectEquipment pledgeSubjectEquipment = null;
        PledgeSubjectBuilding pledgeSubjectBuilding = null;
        PledgeSubjectLandLease pledgeSubjectLandLease = null;
        PledgeSubjectLandOwnership pledgeSubjectLandOwnership = null;
        PledgeSubjectRoom pledgeSubjectRoom = null;
        PledgeSubjectSecurities pledgeSubjectSecurities = null;
        PledgeSubjectTBO pledgeSubjectTBO = null;
        PledgeSubjectVessel pledgeSubjectVessel = null;

        if(Objects.nonNull(dto.getPledgeSubjectAutoDto())) {
            pledgeSubjectAuto = pledgeSubjectAutoConverter.toEntity(dto.getPledgeSubjectAutoDto());
        }else if(Objects.nonNull(dto.getPledgeSubjectEquipmentDto())) {
            pledgeSubjectEquipment = pledgeSubjectEquipmentConverter.toEntity(dto.getPledgeSubjectEquipmentDto());
        }else if(Objects.nonNull(dto.getPledgeSubjectBuildingDto())) {
            pledgeSubjectBuilding = pledgeSubjectBuildingConverter.toEntity(dto.getPledgeSubjectBuildingDto());
        }else if(Objects.nonNull(dto.getPledgeSubjectLandLeaseDto())) {
            pledgeSubjectLandLease = pledgeSubjectLandLeaseConverter.toEntity(dto.getPledgeSubjectLandLeaseDto());
        }else if(Objects.nonNull(dto.getPledgeSubjectLandOwnershipDto())) {
            pledgeSubjectLandOwnership = pledgeSubjectLandOwnershipConverter.toEntity(dto.getPledgeSubjectLandOwnershipDto());
        }else if(Objects.nonNull(dto.getPledgeSubjectRoomDto())) {
            pledgeSubjectRoom = pledgeSubjectRoomConverter.toEntity(dto.getPledgeSubjectRoomDto());
        }else if(Objects.nonNull(dto.getPledgeSubjectSecuritiesDto())) {
            pledgeSubjectSecurities = pledgeSubjectSecuritiesConverter.toEntity(dto.getPledgeSubjectSecuritiesDto());
        }else if(Objects.nonNull(dto.getPledgeSubjectTboDto())) {
            pledgeSubjectTBO = pledgeSubjectTboConverter.toEntity(dto.getPledgeSubjectTboDto());
        }else if(Objects.nonNull(dto.getPledgeSubjectVesselDto()))
            pledgeSubjectVessel = pledgeSubjectVesselConverter.toEntity(dto.getPledgeSubjectVesselDto());

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
                .pledgeSubjectAuto(pledgeSubjectAuto)
                .pledgeSubjectEquipment(pledgeSubjectEquipment)
                .pledgeSubjectBuilding(pledgeSubjectBuilding)
                .pledgeSubjectLandLease(pledgeSubjectLandLease)
                .pledgeSubjectLandOwnership(pledgeSubjectLandOwnership)
                .pledgeSubjectRoom(pledgeSubjectRoom)
                .pledgeSubjectSecurities(pledgeSubjectSecurities)
                .pledgeSubjectTBO(pledgeSubjectTBO)
                .pledgeSubjectVessel(pledgeSubjectVessel)
                .build();
    }

    @Override
    public PledgeSubjectDto toDto(PledgeSubject entity) {
        PledgeSubjectAutoDto pledgeSubjectAutoDto = null;
        PledgeSubjectEquipmentDto pledgeSubjectEquipmentDto = null;
        PledgeSubjectBuildingDto pledgeSubjectBuildingDto = null;
        PledgeSubjectLandLeaseDto pledgeSubjectLandLeaseDto = null;
        PledgeSubjectLandOwnershipDto pledgeSubjectLandOwnershipDto = null;
        PledgeSubjectRoomDto pledgeSubjectRoomDto = null;
        PledgeSubjectSecuritiesDto pledgeSubjectSecuritiesDto = null;
        PledgeSubjectTboDto pledgeSubjectTboDto = null;
        PledgeSubjectVesselDto pledgeSubjectVesselDto = null;

        if(Objects.nonNull(entity.getPledgeSubjectAuto())) {
            pledgeSubjectAutoDto = pledgeSubjectAutoConverter.toDto(entity.getPledgeSubjectAuto());
        }else if(Objects.nonNull(entity.getPledgeSubjectEquipment())) {
            pledgeSubjectEquipmentDto = pledgeSubjectEquipmentConverter.toDto(entity.getPledgeSubjectEquipment());
        }else if(Objects.nonNull(entity.getPledgeSubjectBuilding())) {
            pledgeSubjectBuildingDto = pledgeSubjectBuildingConverter.toDto(entity.getPledgeSubjectBuilding());
        }else if(Objects.nonNull(entity.getPledgeSubjectLandLease())) {
            pledgeSubjectLandLeaseDto = pledgeSubjectLandLeaseConverter.toDto(entity.getPledgeSubjectLandLease());
        }else if(Objects.nonNull(entity.getPledgeSubjectLandOwnership())) {
            pledgeSubjectLandOwnershipDto = pledgeSubjectLandOwnershipConverter.toDto(entity.getPledgeSubjectLandOwnership());
        }else if(Objects.nonNull(entity.getPledgeSubjectRoom())) {
            pledgeSubjectRoomDto = pledgeSubjectRoomConverter.toDto(entity.getPledgeSubjectRoom());
        }else if(Objects.nonNull(entity.getPledgeSubjectSecurities())) {
            pledgeSubjectSecuritiesDto = pledgeSubjectSecuritiesConverter.toDto(entity.getPledgeSubjectSecurities());
        }else if(Objects.nonNull(entity.getPledgeSubjectTBO())) {
            pledgeSubjectTboDto = pledgeSubjectTboConverter.toDto(entity.getPledgeSubjectTBO());
        }else if(Objects.nonNull(entity.getPledgeSubjectVessel()))
            pledgeSubjectVesselDto = pledgeSubjectVesselConverter.toDto(entity.getPledgeSubjectVessel());

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
                .fullAddress(getFullAddress(entity))
                .mainCharacteristic(getMainCharacteristic(entity))
                .primaryIdentifier(getPrimaryIdentifier(entity))
                .pledgeSubjectAutoDto(pledgeSubjectAutoDto)
                .pledgeSubjectEquipmentDto(pledgeSubjectEquipmentDto)
                .pledgeSubjectBuildingDto(pledgeSubjectBuildingDto)
                .pledgeSubjectLandLeaseDto(pledgeSubjectLandLeaseDto)
                .pledgeSubjectLandOwnershipDto(pledgeSubjectLandOwnershipDto)
                .pledgeSubjectRoomDto(pledgeSubjectRoomDto)
                .pledgeSubjectSecuritiesDto(pledgeSubjectSecuritiesDto)
                .pledgeSubjectTboDto(pledgeSubjectTboDto)
                .pledgeSubjectVesselDto(pledgeSubjectVesselDto)
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

        if(entity.getTypeOfCollateral() == TypeOfCollateral.AUTO){
            if(Objects.nonNull(entity.getPledgeSubjectAuto().getHorsepower()))
                mainCharacteristic = String.valueOf(entity.getPledgeSubjectAuto().getHorsepower());
            typeOfMainCharacteristic = "л.с.";

        }else if(entity.getTypeOfCollateral() ==  TypeOfCollateral.EQUIPMENT){
            if(Objects.nonNull(entity.getPledgeSubjectEquipment().getProductivity()))
                mainCharacteristic = String.valueOf(entity.getPledgeSubjectEquipment().getProductivity());
            if(Objects.nonNull(entity.getPledgeSubjectEquipment().getTypeOfProductivity())
                    && !entity.getPledgeSubjectEquipment().getTypeOfProductivity().isEmpty())
                typeOfMainCharacteristic = entity.getPledgeSubjectEquipment().getTypeOfProductivity();

        }else if(entity.getTypeOfCollateral() == TypeOfCollateral.BUILDING){
            mainCharacteristic = String.valueOf(entity.getPledgeSubjectBuilding().getArea());
            typeOfMainCharacteristic = typeOfMainCharacteristicForRealty;

        }else if(entity.getTypeOfCollateral() == TypeOfCollateral.LAND_LEASE){
            mainCharacteristic = String.valueOf(entity.getPledgeSubjectLandLease().getArea());
            typeOfMainCharacteristic = typeOfMainCharacteristicForRealty;

        }else if(entity.getTypeOfCollateral() == TypeOfCollateral.LAND_OWNERSHIP){
            mainCharacteristic = String.valueOf(entity.getPledgeSubjectLandOwnership().getArea());
            typeOfMainCharacteristic = typeOfMainCharacteristicForRealty;

        }else if(entity.getTypeOfCollateral() == TypeOfCollateral.PREMISE){
            mainCharacteristic = String.valueOf(entity.getPledgeSubjectRoom().getArea());
            typeOfMainCharacteristic = typeOfMainCharacteristicForRealty;

        }else if(entity.getTypeOfCollateral() == TypeOfCollateral.TBO){
            mainCharacteristic = String.valueOf(entity.getPledgeSubjectTBO().getCountOfTBO());
            typeOfMainCharacteristic = "кол-во";

        }else if(entity.getTypeOfCollateral() == TypeOfCollateral.VESSEL){
            mainCharacteristic = String.valueOf(entity.getPledgeSubjectVessel().getDeadweight());
            typeOfMainCharacteristic = "дейдвейт";
        }

        return new MainCharacteristic(mainCharacteristic, typeOfMainCharacteristic);
    }

    private PrimaryIdentifier getPrimaryIdentifier(PledgeSubject entity){
        String primaryIdentifier = "-";
        String typeOfPrimaryIdentifier = "-";
        String typeOfPrimaryIdentifierForRealty = "Кад№";

        if(entity.getTypeOfCollateral() == TypeOfCollateral.AUTO){
            primaryIdentifier = String.valueOf(entity.getPledgeSubjectAuto().getVin());
            typeOfPrimaryIdentifier = "VIN";

        }else if(entity.getTypeOfCollateral() ==  TypeOfCollateral.EQUIPMENT){
            if(Objects.nonNull(entity.getPledgeSubjectEquipment().getSerialNum())
                    && !entity.getPledgeSubjectEquipment().getSerialNum().isEmpty())
                primaryIdentifier = String.valueOf(entity.getPledgeSubjectEquipment().getSerialNum());
            typeOfPrimaryIdentifier = "Завод№";

        }else if(entity.getTypeOfCollateral() == TypeOfCollateral.BUILDING){
            primaryIdentifier = String.valueOf(entity.getPledgeSubjectBuilding().getCadastralNum());
            typeOfPrimaryIdentifier = typeOfPrimaryIdentifierForRealty;

        }else if(entity.getTypeOfCollateral() == TypeOfCollateral.LAND_LEASE){
            primaryIdentifier = String.valueOf(entity.getPledgeSubjectLandLease().getCadastralNum());
            typeOfPrimaryIdentifier = typeOfPrimaryIdentifierForRealty;

        }else if(entity.getTypeOfCollateral() == TypeOfCollateral.LAND_OWNERSHIP){
            primaryIdentifier = String.valueOf(entity.getPledgeSubjectLandOwnership().getCadastralNum());
            typeOfPrimaryIdentifier = typeOfPrimaryIdentifierForRealty;

        }else if(entity.getTypeOfCollateral() == TypeOfCollateral.PREMISE){
            primaryIdentifier = String.valueOf(entity.getPledgeSubjectRoom().getCadastralNum());
            typeOfPrimaryIdentifier = typeOfPrimaryIdentifierForRealty;

        }else if(entity.getTypeOfCollateral() == TypeOfCollateral.VESSEL){
            primaryIdentifier = String.valueOf(entity.getPledgeSubjectVessel().getImo());
            typeOfPrimaryIdentifier = "IMO";
        }

        return new PrimaryIdentifier(primaryIdentifier, typeOfPrimaryIdentifier);
    }
}
