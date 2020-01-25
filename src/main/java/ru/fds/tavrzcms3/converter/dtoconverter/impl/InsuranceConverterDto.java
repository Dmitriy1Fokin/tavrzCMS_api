package ru.fds.tavrzcms3.converter.dtoconverter.impl;

import org.springframework.stereotype.Component;
import ru.fds.tavrzcms3.converter.dtoconverter.ConverterDto;
import ru.fds.tavrzcms3.domain.Insurance;
import ru.fds.tavrzcms3.dto.InsuranceDto;
import ru.fds.tavrzcms3.exception.NotFoundException;
import ru.fds.tavrzcms3.service.PledgeSubjectService;

@Component
public class InsuranceConverterDto implements ConverterDto<Insurance, InsuranceDto> {

    private final PledgeSubjectService pledgeSubjectService;

    public InsuranceConverterDto(PledgeSubjectService pledgeSubjectService) {
        this.pledgeSubjectService = pledgeSubjectService;
    }

    @Override
    public Insurance toEntity(InsuranceDto dto) {
        return Insurance.builder()
                .insuranceId(dto.getInsuranceId())
                .numInsurance(dto.getNumInsurance())
                .dateBeginInsurance(dto.getDateBeginInsurance())
                .dateEndInsurance(dto.getDateEndInsurance())
                .sumInsured(dto.getSumInsured())
                .dateInsuranceContract(dto.getDateInsuranceContract())
                .paymentOfInsurancePremium(dto.getPaymentOfInsurancePremium())
                .franchiseAmount(dto.getFranchiseAmount())
                .pledgeSubject(pledgeSubjectService.getPledgeSubjectById(dto.getPledgeSubjectId()).orElseThrow(() -> new NotFoundException("Pledge subject not found")))
                .build();
    }

    @Override
    public InsuranceDto toDto(Insurance entity) {
        return InsuranceDto.builder()
                .insuranceId(entity.getInsuranceId())
                .numInsurance(entity.getNumInsurance())
                .dateBeginInsurance(entity.getDateBeginInsurance())
                .dateEndInsurance(entity.getDateEndInsurance())
                .sumInsured(entity.getSumInsured())
                .dateInsuranceContract(entity.getDateInsuranceContract())
                .paymentOfInsurancePremium(entity.getPaymentOfInsurancePremium())
                .franchiseAmount(entity.getFranchiseAmount())
                .pledgeSubjectId(entity.getPledgeSubject().getPledgeSubjectId())
                .build();
    }
}
