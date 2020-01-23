package ru.fds.tavrzcms3.converter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.fds.tavrzcms3.converter.dtoconverter.impl.PledgeAgreementConverterDto;
import ru.fds.tavrzcms3.dictionary.StatusOfAgreement;
import ru.fds.tavrzcms3.dictionary.TypeOfPledgeAgreement;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.LoanAgreement;
import ru.fds.tavrzcms3.domain.PledgeAgreement;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.dto.PledgeAgreementDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PledgeAgreementConverterDtoTest {

    @Autowired
    PledgeAgreementConverterDto pledgeAgreementConverter;

    @Test
    public void toEntity() {
        PledgeAgreementDto pledgeAgreementDto = PledgeAgreementDto.builder()
                .pledgeAgreementId(1L)
                .numPA("000")
                .dateBeginPA(LocalDate.now())
                .dateEndPA(LocalDate.now())
                .pervPosl(TypeOfPledgeAgreement.PERV)
                .statusPA(StatusOfAgreement.CLOSED)
                .noticePA("QWE")
                .zsDz(BigDecimal.valueOf(1))
                .zsZz(BigDecimal.valueOf(2))
                .rsDz(BigDecimal.valueOf(3))
                .rsZz(BigDecimal.valueOf(4))
                .ss(BigDecimal.valueOf(5))
                .clientId(2L)
                .briefInfoAboutCollateral(Arrays.asList("qwe, asd", "wert"))
                .typesOfCollateral(Arrays.asList("gfd", "bnht"))
                .datesOfConclusions(Arrays.asList(LocalDate.now(), LocalDate.now()))
                .datesOfMonitoring(Arrays.asList(LocalDate.now(), LocalDate.now()))
                .resultsOfMonitoring(Arrays.asList("yhgb", "wedfc"))
                .build();

        PledgeAgreement pledgeAgreement = pledgeAgreementConverter.toEntity(pledgeAgreementDto);

        assertEquals(pledgeAgreement.getPledgeAgreementId(), pledgeAgreementDto.getPledgeAgreementId());
        assertEquals(pledgeAgreement.getNumPA(), pledgeAgreementDto.getNumPA());
        assertEquals(pledgeAgreement.getDateBeginPA(), pledgeAgreementDto.getDateBeginPA());
        assertEquals(pledgeAgreement.getDateEndPA(), pledgeAgreementDto.getDateEndPA());
        assertEquals(pledgeAgreement.getPervPosl(), pledgeAgreementDto.getPervPosl());
        assertEquals(pledgeAgreement.getStatusPA(), pledgeAgreementDto.getStatusPA());
        assertEquals(pledgeAgreement.getNoticePA(), pledgeAgreementDto.getNoticePA());
        assertEquals(pledgeAgreement.getZsDz(), pledgeAgreementDto.getZsDz());
        assertEquals(pledgeAgreement.getZsZz(), pledgeAgreementDto.getZsZz());
        assertEquals(pledgeAgreement.getRsDz(), pledgeAgreementDto.getRsDz());
        assertEquals(pledgeAgreement.getRsZz(), pledgeAgreementDto.getRsZz());
        assertEquals(pledgeAgreement.getSs(), pledgeAgreementDto.getSs());
        assertEquals(pledgeAgreement.getClient().getClientId(), pledgeAgreementDto.getClientId());
    }

    @Test
    public void toDto() {
        PledgeAgreement pledgeAgreement = PledgeAgreement.builder()
                .pledgeAgreementId(1L)
                .numPA("000")
                .dateBeginPA(LocalDate.now())
                .dateEndPA(LocalDate.now())
                .pervPosl(TypeOfPledgeAgreement.PERV)
                .statusPA(StatusOfAgreement.CLOSED)
                .noticePA("QWE")
                .zsDz(BigDecimal.valueOf(1))
                .zsZz(BigDecimal.valueOf(2))
                .rsDz(BigDecimal.valueOf(3))
                .rsZz(BigDecimal.valueOf(4))
                .ss(BigDecimal.valueOf(5))
                .client(new Client().builder().clientId(2L).build())
                .build();

        PledgeAgreementDto pledgeAgreementDto = pledgeAgreementConverter.toDto(pledgeAgreement);

        assertEquals(pledgeAgreement.getPledgeAgreementId(), pledgeAgreementDto.getPledgeAgreementId());
        assertEquals(pledgeAgreement.getNumPA(), pledgeAgreementDto.getNumPA());
        assertEquals(pledgeAgreement.getDateBeginPA(), pledgeAgreementDto.getDateBeginPA());
        assertEquals(pledgeAgreement.getDateEndPA(), pledgeAgreementDto.getDateEndPA());
        assertEquals(pledgeAgreement.getPervPosl(), pledgeAgreementDto.getPervPosl());
        assertEquals(pledgeAgreement.getStatusPA(), pledgeAgreementDto.getStatusPA());
        assertEquals(pledgeAgreement.getNoticePA(), pledgeAgreementDto.getNoticePA());
        assertEquals(pledgeAgreement.getZsDz(), pledgeAgreementDto.getZsDz());
        assertEquals(pledgeAgreement.getZsZz(), pledgeAgreementDto.getZsZz());
        assertEquals(pledgeAgreement.getRsDz(), pledgeAgreementDto.getRsDz());
        assertEquals(pledgeAgreement.getRsZz(), pledgeAgreementDto.getRsZz());
        assertEquals(pledgeAgreement.getSs(), pledgeAgreementDto.getSs());
        assertEquals(pledgeAgreement.getClient().getClientId(), pledgeAgreementDto.getClientId());

    }
}