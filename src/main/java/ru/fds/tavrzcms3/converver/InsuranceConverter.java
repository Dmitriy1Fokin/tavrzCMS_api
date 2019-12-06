package ru.fds.tavrzcms3.converver;

import org.springframework.stereotype.Component;
import ru.fds.tavrzcms3.domain.Insurance;
import ru.fds.tavrzcms3.dto.InsuranceDto;
import ru.fds.tavrzcms3.service.PledgeSubjectService;

@Component
public class InsuranceConverter implements Converter<Insurance, InsuranceDto> {

    private final PledgeSubjectService pledgeSubjectService;

    public InsuranceConverter(PledgeSubjectService pledgeSubjectService) {
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
                .pledgeSubject(pledgeSubjectService.getPledgeSubjectById(dto.getPledgeSubjectId()).orElse(null))
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
