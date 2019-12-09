package ru.fds.tavrzcms3.converver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.fds.tavrzcms3.dictionary.StatusOfAgreement;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.LoanAgreement;
import ru.fds.tavrzcms3.domain.PledgeAgreement;
import ru.fds.tavrzcms3.dto.LoanAgreementDto;

import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.*;

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
                .dateBeginLA(new Date(123))
                .dateEndLA(new Date(456))
                .statusLA(StatusOfAgreement.OPEN)
                .amountLA(12345)
                .debtLA(1234)
                .interestRateLA(0.12)
                .pfo((byte) 2)
                .qualityCategory((byte)4)
                .clientId(2L)
                .pledgeAgreementsIds(Arrays.asList(6L, 7L))
                .build();

        LoanAgreement loanAgreement = loanAgreementConverter.toEntity(loanAgreementDto);

        assertEquals(loanAgreement.getLoanAgreementId(), loanAgreementDto.getLoanAgreementId());
        assertEquals(loanAgreement.getNumLA(), loanAgreementDto.getNumLA());
        assertEquals(loanAgreement.getDateBeginLA(), loanAgreementDto.getDateBeginLA());
        assertEquals(loanAgreement.getDateEndLA(), loanAgreementDto.getDateEndLA());
        assertEquals(loanAgreement.getStatusLA(), loanAgreementDto.getStatusLA());
        assertEquals(loanAgreement.getAmountLA(), loanAgreementDto.getAmountLA(), 0);
        assertEquals(loanAgreement.getDebtLA(), loanAgreementDto.getDebtLA(), 0);
        assertEquals(loanAgreement.getInterestRateLA(), loanAgreementDto.getInterestRateLA(),0);
        assertEquals(loanAgreement.getPfo(), loanAgreementDto.getPfo());
        assertEquals(loanAgreement.getQualityCategory(), loanAgreementDto.getQualityCategory());
        assertEquals(loanAgreement.getClient().getClientId(), loanAgreementDto.getClientId());
        assertEquals(loanAgreement.getPledgeAgreements().size(), loanAgreementDto.getPledgeAgreementsIds().size());
    }

    @Test
    public void toDto() {
        LoanAgreement loanAgreement = LoanAgreement.builder()
                .loanAgreementId(1L)
                .numLA("000")
                .dateBeginLA(new Date(123))
                .dateEndLA(new Date(456))
                .statusLA(StatusOfAgreement.OPEN)
                .amountLA(12345)
                .debtLA(1234)
                .interestRateLA(0.12)
                .pfo((byte) 2)
                .qualityCategory((byte)4)
                .client(new Client().builder().clientId(2L).build())
                .pledgeAgreement(new PledgeAgreement().builder().pledgeAgreementId(3L).build())
                .build();

        LoanAgreementDto loanAgreementDto = loanAgreementConverter.toDto(loanAgreement);

        assertEquals(loanAgreement.getLoanAgreementId(), loanAgreementDto.getLoanAgreementId());
        assertEquals(loanAgreement.getNumLA(), loanAgreementDto.getNumLA());
        assertEquals(loanAgreement.getDateBeginLA(), loanAgreementDto.getDateBeginLA());
        assertEquals(loanAgreement.getDateEndLA(), loanAgreementDto.getDateEndLA());
        assertEquals(loanAgreement.getStatusLA(), loanAgreementDto.getStatusLA());
        assertEquals(loanAgreement.getAmountLA(), loanAgreementDto.getAmountLA(), 0);
        assertEquals(loanAgreement.getDebtLA(), loanAgreementDto.getDebtLA(), 0);
        assertEquals(loanAgreement.getInterestRateLA(), loanAgreementDto.getInterestRateLA(),0);
        assertEquals(loanAgreement.getPfo(), loanAgreementDto.getPfo());
        assertEquals(loanAgreement.getQualityCategory(), loanAgreementDto.getQualityCategory());
        assertEquals(loanAgreement.getClient().getClientId(), loanAgreementDto.getClientId());
        assertEquals(loanAgreement.getPledgeAgreements().size(), loanAgreementDto.getPledgeAgreementsIds().size());
    }
}