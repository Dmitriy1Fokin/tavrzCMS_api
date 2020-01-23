package ru.fds.tavrzcms3.converter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.fds.tavrzcms3.converter.dtoconverter.impl.LoanAgreementConverterDto;
import ru.fds.tavrzcms3.dictionary.StatusOfAgreement;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.LoanAgreement;
import ru.fds.tavrzcms3.domain.PledgeAgreement;
import ru.fds.tavrzcms3.dto.LoanAgreementDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LoanAgreementConverterDtoTest {

    @Autowired
    LoanAgreementConverterDto loanAgreementConverter;

    @Test
    public void toEntity() {
        LoanAgreementDto loanAgreementDto = LoanAgreementDto.builder()
                .loanAgreementId(1L)
                .numLA("000")
                .dateBeginLA(LocalDate.now())
                .dateEndLA(LocalDate.now())
                .statusLA(StatusOfAgreement.OPEN)
                .amountLA(BigDecimal.valueOf(12345))
                .debtLA(BigDecimal.valueOf(1234))
                .interestRateLA(0.12)
                .pfo((byte) 2)
                .qualityCategory((byte)4)
                .clientId(2L)
                .build();

        LoanAgreement loanAgreement = loanAgreementConverter.toEntity(loanAgreementDto);

        assertEquals(loanAgreement.getLoanAgreementId(), loanAgreementDto.getLoanAgreementId());
        assertEquals(loanAgreement.getNumLA(), loanAgreementDto.getNumLA());
        assertEquals(loanAgreement.getDateBeginLA(), loanAgreementDto.getDateBeginLA());
        assertEquals(loanAgreement.getDateEndLA(), loanAgreementDto.getDateEndLA());
        assertEquals(loanAgreement.getStatusLA(), loanAgreementDto.getStatusLA());
        assertEquals(loanAgreement.getAmountLA(), loanAgreementDto.getAmountLA());
        assertEquals(loanAgreement.getDebtLA(), loanAgreementDto.getDebtLA());
        assertEquals(loanAgreement.getInterestRateLA(), loanAgreementDto.getInterestRateLA(),0);
        assertEquals(loanAgreement.getPfo(), loanAgreementDto.getPfo());
        assertEquals(loanAgreement.getQualityCategory(), loanAgreementDto.getQualityCategory());
        assertEquals(loanAgreement.getClient().getClientId(), loanAgreementDto.getClientId());
    }

    @Test
    public void toDto() {
        LoanAgreement loanAgreement = LoanAgreement.builder()
                .loanAgreementId(1L)
                .numLA("000")
                .dateBeginLA(LocalDate.now())
                .dateEndLA(LocalDate.now())
                .statusLA(StatusOfAgreement.OPEN)
                .amountLA(BigDecimal.valueOf(12345))
                .debtLA(BigDecimal.valueOf(1234))
                .interestRateLA(0.12)
                .pfo((byte) 2)
                .qualityCategory((byte)4)
                .client(new Client().builder().clientId(2L).build())
                .build();

        LoanAgreementDto loanAgreementDto = loanAgreementConverter.toDto(loanAgreement);

        assertEquals(loanAgreement.getLoanAgreementId(), loanAgreementDto.getLoanAgreementId());
        assertEquals(loanAgreement.getNumLA(), loanAgreementDto.getNumLA());
        assertEquals(loanAgreement.getDateBeginLA(), loanAgreementDto.getDateBeginLA());
        assertEquals(loanAgreement.getDateEndLA(), loanAgreementDto.getDateEndLA());
        assertEquals(loanAgreement.getStatusLA(), loanAgreementDto.getStatusLA());
        assertEquals(loanAgreement.getAmountLA(), loanAgreementDto.getAmountLA());
        assertEquals(loanAgreement.getDebtLA(), loanAgreementDto.getDebtLA());
        assertEquals(loanAgreement.getInterestRateLA(), loanAgreementDto.getInterestRateLA(),0);
        assertEquals(loanAgreement.getPfo(), loanAgreementDto.getPfo());
        assertEquals(loanAgreement.getQualityCategory(), loanAgreementDto.getQualityCategory());
        assertEquals(loanAgreement.getClient().getClientId(), loanAgreementDto.getClientId());
    }
}