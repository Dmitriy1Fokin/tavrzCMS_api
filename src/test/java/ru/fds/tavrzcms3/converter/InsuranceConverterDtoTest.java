package ru.fds.tavrzcms3.converter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.fds.tavrzcms3.converter.dtoconverter.impl.InsuranceConverterDto;
import ru.fds.tavrzcms3.domain.Insurance;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.dto.InsuranceDto;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InsuranceConverterDtoTest {

    @Autowired
    InsuranceConverterDto insuranceConverter;

    @Test
    public void toEntity() {
        InsuranceDto insuranceDto = InsuranceDto.builder()
                .insuranceId(1L)
                .numInsurance("num1")
                .dateBeginInsurance(LocalDate.now())
                .dateEndInsurance(LocalDate.now())
                .sumInsured(BigDecimal.valueOf(987654))
                .dateInsuranceContract(LocalDate.now())
                .paymentOfInsurancePremium("да")
                .franchiseAmount(BigDecimal.valueOf(123.0))
                .pledgeSubjectId(2L)
                .build();

        Insurance insurance = insuranceConverter.toEntity(insuranceDto);

        assertEquals(insurance.getInsuranceId(), insuranceDto.getInsuranceId());
        assertEquals(insurance.getNumInsurance(), insuranceDto.getNumInsurance());
        assertEquals(insurance.getDateBeginInsurance(), insuranceDto.getDateBeginInsurance());
        assertEquals(insurance.getDateEndInsurance(), insuranceDto.getDateEndInsurance());
        assertEquals(insurance.getSumInsured(), insuranceDto.getSumInsured());
        assertEquals(insurance.getDateInsuranceContract(), insuranceDto.getDateInsuranceContract());
        assertEquals(insurance.getPaymentOfInsurancePremium(), insuranceDto.getPaymentOfInsurancePremium());
        assertEquals(insurance.getFranchiseAmount(), insuranceDto.getFranchiseAmount());
        assertEquals(insurance.getPledgeSubject().getPledgeSubjectId(), insuranceDto.getPledgeSubjectId());
    }

    @Test
    public void toDto() {
        Insurance insurance = Insurance.builder()
                .insuranceId(1L)
                .numInsurance("num1")
                .dateBeginInsurance(LocalDate.now())
                .dateEndInsurance(LocalDate.now())
                .sumInsured(BigDecimal.valueOf(987654))
                .dateInsuranceContract(LocalDate.now())
                .paymentOfInsurancePremium("да")
                .franchiseAmount(BigDecimal.valueOf(123.0))
                .pledgeSubject(new PledgeSubject().builder().pledgeSubjectId(3L).build())
                .build();

        InsuranceDto insuranceDto = insuranceConverter.toDto(insurance);

        assertEquals(insurance.getInsuranceId(), insuranceDto.getInsuranceId());
        assertEquals(insurance.getNumInsurance(), insuranceDto.getNumInsurance());
        assertEquals(insurance.getDateBeginInsurance(), insuranceDto.getDateBeginInsurance());
        assertEquals(insurance.getDateEndInsurance(), insuranceDto.getDateEndInsurance());
        assertEquals(insurance.getSumInsured(), insuranceDto.getSumInsured());
        assertEquals(insurance.getDateInsuranceContract(), insuranceDto.getDateInsuranceContract());
        assertEquals(insurance.getPaymentOfInsurancePremium(), insuranceDto.getPaymentOfInsurancePremium());
        assertEquals(insurance.getFranchiseAmount(), insuranceDto.getFranchiseAmount());
        assertEquals(insurance.getPledgeSubject().getPledgeSubjectId(), insuranceDto.getPledgeSubjectId());

    }
}