package ru.fds.tavrzcms3.converver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.fds.tavrzcms3.dictionary.StatusOfAgreement;
import ru.fds.tavrzcms3.dictionary.TypeOfPledgeAgreement;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.LoanAgreement;
import ru.fds.tavrzcms3.domain.PledgeAgreement;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.dto.PledgeAgreementDto;
import ru.fds.tavrzcms3.dto.PledgeSubjectDto;

import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PledgeAgreementConverterTest {

    @Autowired
    PledgeAgreementConverter pledgeAgreementConverter;

    @Test
    public void toEntity() {
        PledgeAgreementDto pledgeAgreementDto = PledgeAgreementDto.builder()
                .pledgeAgreementId(1L)
                .numPA("000")
                .dateBeginPA(new Date(123))
                .dateEndPA(new Date(34))
                .pervPosl(TypeOfPledgeAgreement.PERV)
                .statusPA(StatusOfAgreement.CLOSED)
                .noticePA("QWE")
                .zsDz(1)
                .zsZz(2)
                .rsDz(3)
                .rsZz(4)
                .ss(5)
                .loanAgreementsIds(Arrays.asList(1L, 3L, 5L))
                .clientId(2L)
                .pledgeSubjectsIds(Arrays.asList(2L, 4L))
                .briefInfoAboutCollateral(Arrays.asList("qwe, asd", "wert"))
                .typesOfCollateral(Arrays.asList("gfd", "bnht"))
                .datesOfConclusions(Arrays.asList(new Date(1), new Date(2)))
                .datesOfMonitoring(Arrays.asList(new Date(31), new Date(25)))
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
        assertEquals(pledgeAgreement.getZsDz(), pledgeAgreementDto.getZsDz(), 0);
        assertEquals(pledgeAgreement.getZsZz(), pledgeAgreementDto.getZsZz(), 0);
        assertEquals(pledgeAgreement.getRsDz(), pledgeAgreementDto.getRsDz(), 0);
        assertEquals(pledgeAgreement.getRsZz(), pledgeAgreementDto.getRsZz(), 0);
        assertEquals(pledgeAgreement.getSs(), pledgeAgreementDto.getSs(), 0);
        assertEquals(pledgeAgreement.getLoanAgreements().size(), pledgeAgreementDto.getLoanAgreementsIds().size());
        assertEquals(pledgeAgreement.getClient().getClientId(), pledgeAgreementDto.getClientId());
        assertEquals(pledgeAgreement.getPledgeSubjects().size(), pledgeAgreementDto.getPledgeSubjectsIds().size());
    }

    @Test
    public void toDto() {
        PledgeAgreement pledgeAgreement = PledgeAgreement.builder()
                .pledgeAgreementId(1L)
                .numPA("000")
                .dateBeginPA(new Date(123))
                .dateEndPA(new Date(34))
                .pervPosl(TypeOfPledgeAgreement.PERV)
                .statusPA(StatusOfAgreement.CLOSED)
                .noticePA("QWE")
                .zsDz(1)
                .zsZz(2)
                .rsDz(3)
                .rsZz(4)
                .ss(5)
                .loanAgreement(new LoanAgreement().builder().loanAgreementId(1L).build())
                .client(new Client().builder().clientId(2L).build())
                .pledgeSubject(new PledgeSubject().builder().pledgeSubjectId(4L).build())
                .build();

        PledgeAgreementDto pledgeAgreementDto = pledgeAgreementConverter.toDto(pledgeAgreement);

        assertEquals(pledgeAgreement.getPledgeAgreementId(), pledgeAgreementDto.getPledgeAgreementId());
        assertEquals(pledgeAgreement.getNumPA(), pledgeAgreementDto.getNumPA());
        assertEquals(pledgeAgreement.getDateBeginPA(), pledgeAgreementDto.getDateBeginPA());
        assertEquals(pledgeAgreement.getDateEndPA(), pledgeAgreementDto.getDateEndPA());
        assertEquals(pledgeAgreement.getPervPosl(), pledgeAgreementDto.getPervPosl());
        assertEquals(pledgeAgreement.getStatusPA(), pledgeAgreementDto.getStatusPA());
        assertEquals(pledgeAgreement.getNoticePA(), pledgeAgreementDto.getNoticePA());
        assertEquals(pledgeAgreement.getZsDz(), pledgeAgreementDto.getZsDz(), 0);
        assertEquals(pledgeAgreement.getZsZz(), pledgeAgreementDto.getZsZz(), 0);
        assertEquals(pledgeAgreement.getRsDz(), pledgeAgreementDto.getRsDz(), 0);
        assertEquals(pledgeAgreement.getRsZz(), pledgeAgreementDto.getRsZz(), 0);
        assertEquals(pledgeAgreement.getSs(), pledgeAgreementDto.getSs(), 0);
        assertEquals(pledgeAgreement.getLoanAgreements().size(), pledgeAgreementDto.getLoanAgreementsIds().size());
        assertEquals(pledgeAgreement.getClient().getClientId(), pledgeAgreementDto.getClientId());

    }
}