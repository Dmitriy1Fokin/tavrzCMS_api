package ru.fds.tavrzcms3.dto;

import org.springframework.stereotype.Component;
import ru.fds.tavrzcms3.converver.*;
import ru.fds.tavrzcms3.domain.*;
import ru.fds.tavrzcms3.service.PledgeSubjectService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class DtoFactory {

    private final PledgeSubjectService pledgeSubjectService;
    private final PledgeSubjectConverterDto pledgeSubjectConverterDto;
    private final PledgeSubjectAutoConverter pledgeSubjectAutoConverter;
    private final PledgeSubjectEquipmentConverter pledgeSubjectEquipmentConverter;
    private final PledgeSubjectBuildingConverter pledgeSubjectBuildingConverter;
    private final PledgeSubjectLandLeaseConverter pledgeSubjectLandLeaseConverter;
    private final PledgeSubjectLandOwnershipConverter pledgeSubjectLandOwnershipConverter;
    private final PledgeSubjectRoomConverter pledgeSubjectRoomConverter;
    private final PledgeSubjectSecuritiesConverter pledgeSubjectSecuritiesConverter;
    private final PledgeSubjectTboConverter pledgeSubjectTboConverter;
    private final PledgeSubjectVesselConverter pledgeSubjectVesselConverter;
    private final LoanAgreementConverterDto loanAgreementConverterDto;
    private final PledgeAgreementConverterDto pledgeAgreementConverterDto;

    public DtoFactory(PledgeSubjectService pledgeSubjectService,
                      PledgeSubjectConverterDto pledgeSubjectConverterDto,
                      PledgeSubjectAutoConverter pledgeSubjectAutoConverter,
                      PledgeSubjectEquipmentConverter pledgeSubjectEquipmentConverter,
                      PledgeSubjectBuildingConverter pledgeSubjectBuildingConverter,
                      PledgeSubjectLandLeaseConverter pledgeSubjectLandLeaseConverter,
                      PledgeSubjectLandOwnershipConverter pledgeSubjectLandOwnershipConverter,
                      PledgeSubjectRoomConverter pledgeSubjectRoomConverter,
                      PledgeSubjectSecuritiesConverter pledgeSubjectSecuritiesConverter,
                      PledgeSubjectTboConverter pledgeSubjectTboConverter,
                      PledgeSubjectVesselConverter pledgeSubjectVesselConverter, LoanAgreementConverterDto loanAgreementConverterDto, PledgeAgreementConverterDto pledgeAgreementConverterDto) {
        this.pledgeSubjectService = pledgeSubjectService;
        this.pledgeSubjectConverterDto = pledgeSubjectConverterDto;
        this.pledgeSubjectAutoConverter = pledgeSubjectAutoConverter;
        this.pledgeSubjectEquipmentConverter = pledgeSubjectEquipmentConverter;
        this.pledgeSubjectBuildingConverter = pledgeSubjectBuildingConverter;
        this.pledgeSubjectLandLeaseConverter = pledgeSubjectLandLeaseConverter;
        this.pledgeSubjectLandOwnershipConverter = pledgeSubjectLandOwnershipConverter;
        this.pledgeSubjectRoomConverter = pledgeSubjectRoomConverter;
        this.pledgeSubjectSecuritiesConverter = pledgeSubjectSecuritiesConverter;
        this.pledgeSubjectTboConverter = pledgeSubjectTboConverter;
        this.pledgeSubjectVesselConverter = pledgeSubjectVesselConverter;
        this.loanAgreementConverterDto = loanAgreementConverterDto;
        this.pledgeAgreementConverterDto = pledgeAgreementConverterDto;
    }

    public Dto getPledgeSubjectDto(PledgeSubject pledgeSubject){
        if(pledgeSubject instanceof PledgeSubjectAuto)
            return pledgeSubjectAutoConverter.toDto((PledgeSubjectAuto) pledgeSubject);
        if(pledgeSubject instanceof PledgeSubjectEquipment)
            return pledgeSubjectEquipmentConverter.toDto((PledgeSubjectEquipment) pledgeSubject);
        if(pledgeSubject instanceof PledgeSubjectBuilding)
            return pledgeSubjectBuildingConverter.toDto((PledgeSubjectBuilding) pledgeSubject);
        if(pledgeSubject instanceof PledgeSubjectLandLease)
            return pledgeSubjectLandLeaseConverter.toDto((PledgeSubjectLandLease) pledgeSubject);
        if(pledgeSubject instanceof PledgeSubjectLandOwnership)
            return pledgeSubjectLandOwnershipConverter.toDto((PledgeSubjectLandOwnership) pledgeSubject);
        if(pledgeSubject instanceof PledgeSubjectRoom)
            return pledgeSubjectRoomConverter.toDto((PledgeSubjectRoom) pledgeSubject);
        if(pledgeSubject instanceof PledgeSubjectSecurities)
            return pledgeSubjectSecuritiesConverter.toDto((PledgeSubjectSecurities) pledgeSubject);
        if(pledgeSubject instanceof PledgeSubjectTBO)
            return pledgeSubjectTboConverter.toDto((PledgeSubjectTBO) pledgeSubject);
        if(pledgeSubject instanceof PledgeSubjectVessel)
            return pledgeSubjectVesselConverter.toDto((PledgeSubjectVessel) pledgeSubject);

        return pledgeSubjectConverterDto.toDto(pledgeSubject);
    }


    public List<Dto> getPledgeSubjectsDto(List<PledgeSubject> pledgeSubjectList){
        List<Dto> dtoList = new ArrayList<>();
        for(PledgeSubject pledgeSubject: pledgeSubjectList)
            dtoList.add(getPledgeSubjectDto(pledgeSubject));

        return dtoList;
    }

    public PledgeSubject getPledgeSubjectEntity(Dto pledgeSubjectDto){
        if(pledgeSubjectDto instanceof PledgeSubjectAutoDto)
            return pledgeSubjectAutoConverter.toEntity((PledgeSubjectAutoDto) pledgeSubjectDto);
        if(pledgeSubjectDto instanceof PledgeSubjectEquipmentDto)
            return pledgeSubjectEquipmentConverter.toEntity((PledgeSubjectEquipmentDto) pledgeSubjectDto);
        if(pledgeSubjectDto instanceof PledgeSubjectBuildingDto)
            return pledgeSubjectBuildingConverter.toEntity((PledgeSubjectBuildingDto) pledgeSubjectDto);
        if(pledgeSubjectDto instanceof PledgeSubjectLandLeaseDto)
            return pledgeSubjectLandLeaseConverter.toEntity((PledgeSubjectLandLeaseDto) pledgeSubjectDto);
        if(pledgeSubjectDto instanceof PledgeSubjectLandOwnershipDto)
            return pledgeSubjectLandOwnershipConverter.toEntity((PledgeSubjectLandOwnershipDto) pledgeSubjectDto);
        if(pledgeSubjectDto instanceof PledgeSubjectRoomDto)
            return pledgeSubjectRoomConverter.toEntity((PledgeSubjectRoomDto) pledgeSubjectDto);
        if(pledgeSubjectDto instanceof PledgeSubjectSecuritiesDto)
            return pledgeSubjectSecuritiesConverter.toEntity((PledgeSubjectSecuritiesDto) pledgeSubjectDto);
        if(pledgeSubjectDto instanceof PledgeSubjectTboDto)
            return pledgeSubjectTboConverter.toEntity((PledgeSubjectTboDto) pledgeSubjectDto);
        if(pledgeSubjectDto instanceof PledgeSubjectVesselDto)
            return pledgeSubjectVesselConverter.toEntity((PledgeSubjectVesselDto) pledgeSubjectDto);

        return pledgeSubjectConverterDto.toEntity((PledgeSubjectDto) pledgeSubjectDto);
    }

    public List<PledgeSubject> getPledgeSubjectsEntity(List<Dto> pledgeSubjectDtoList){
        List<PledgeSubject> pledgeSubjectList = new ArrayList<>();
        for(Dto psDto : pledgeSubjectDtoList)
            pledgeSubjectList.add(getPledgeSubjectEntity(psDto));

        return pledgeSubjectList;
    }

    public Dto getLoanAgreementDto(LoanAgreement loanAgreement){
        return loanAgreementConverterDto.toDto(loanAgreement);
    }

    public List<Dto> getLoanAgreementsDto(List<LoanAgreement> loanAgreementList){
        List<Dto> dtoList = new ArrayList<>(    );
        for(LoanAgreement loanAgreement: loanAgreementList)
            dtoList.add(loanAgreementConverterDto.toDto(loanAgreement));

        return dtoList;
    }

    public Dto getPledgeAgreementDto(PledgeAgreement pledgeAgreement){
        return pledgeAgreementConverterDto.toDto(pledgeAgreement);
    }

    public List<Dto> getPledgeAgreementsDto(List<PledgeAgreement> pledgeAgreementList){
        List<Dto> dtoList = new ArrayList<>();
        for(PledgeAgreement pledgeAgreement: pledgeAgreementList)
            dtoList.add(getPledgeAgreementDto(pledgeAgreement));

        return dtoList;
    }


}
