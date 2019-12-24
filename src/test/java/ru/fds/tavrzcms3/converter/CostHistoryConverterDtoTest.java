package ru.fds.tavrzcms3.converter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.fds.tavrzcms3.converter.dtoconverter.impl.CostHistoryConverterDto;
import ru.fds.tavrzcms3.domain.CostHistory;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.dto.CostHistoryDto;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CostHistoryConverterDtoTest {

    @Autowired
    CostHistoryConverterDto costHistoryConverter;

    @Test
    public void toEntity() {
        CostHistoryDto costHistoryDto = CostHistoryDto.builder()
                .costHistoryId(1L)
                .dateConclusion(LocalDate.now())
                .zsDz(BigDecimal.valueOf(1))
                .zsZz(BigDecimal.valueOf(2))
                .rsDz(BigDecimal.valueOf(3))
                .rsZz(BigDecimal.valueOf(4))
                .ss(BigDecimal.valueOf(5))
                .appraiser("appraiser name")
                .appraisalReportNum("report num")
                .appraisalReportDate(LocalDate.now())
                .pledgeSubjectId(1L)
                .build();

        CostHistory costHistory = costHistoryConverter.toEntity(costHistoryDto);

        assertEquals(costHistoryDto.getCostHistoryId(), costHistory.getCostHistoryId());
        assertEquals(costHistoryDto.getDateConclusion(), costHistory.getDateConclusion());
        assertEquals(costHistoryDto.getZsDz(), costHistory.getZsDz());
        assertEquals(costHistoryDto.getZsZz(), costHistory.getZsZz());
        assertEquals(costHistoryDto.getRsDz(), costHistory.getRsDz());
        assertEquals(costHistoryDto.getRsZz(), costHistory.getRsZz());
        assertEquals(costHistoryDto.getSs(), costHistory.getSs());
        assertEquals(costHistoryDto.getAppraiser(), costHistory.getAppraiser());
        assertEquals(costHistoryDto.getAppraisalReportNum(), costHistory.getAppraisalReportNum());
        assertEquals(costHistoryDto.getAppraisalReportDate(), costHistory.getAppraisalReportDate());
        assertEquals(costHistoryDto.getPledgeSubjectId(), costHistory.getPledgeSubject().getPledgeSubjectId());
    }

    @Test
    public void toDto() {
        CostHistory costHistory = CostHistory.builder()
                .costHistoryId(1L)
                .dateConclusion(LocalDate.now())
                .zsDz(BigDecimal.valueOf(1))
                .zsZz(BigDecimal.valueOf(2))
                .rsDz(BigDecimal.valueOf(3))
                .rsZz(BigDecimal.valueOf(4))
                .ss(BigDecimal.valueOf(5))
                .appraiser("appraiser name")
                .appraisalReportNum("report num")
                .appraisalReportDate(LocalDate.now())
                .pledgeSubject(new PledgeSubject().builder().pledgeSubjectId(1L).build())
                .build();

        CostHistoryDto costHistoryDto = costHistoryConverter.toDto(costHistory);

        assertEquals(costHistoryDto.getCostHistoryId(), costHistory.getCostHistoryId());
        assertEquals(costHistoryDto.getDateConclusion(), costHistory.getDateConclusion());
        assertEquals(costHistoryDto.getZsDz(), costHistory.getZsDz());
        assertEquals(costHistoryDto.getZsZz(), costHistory.getZsZz());
        assertEquals(costHistoryDto.getRsDz(), costHistory.getRsDz());
        assertEquals(costHistoryDto.getRsZz(), costHistory.getRsZz());
        assertEquals(costHistoryDto.getSs(), costHistory.getSs());
        assertEquals(costHistoryDto.getAppraiser(), costHistory.getAppraiser());
        assertEquals(costHistoryDto.getAppraisalReportNum(), costHistory.getAppraisalReportNum());
        assertEquals(costHistoryDto.getAppraisalReportDate(), costHistory.getAppraisalReportDate());
        assertEquals(costHistoryDto.getPledgeSubjectId(), costHistory.getPledgeSubject().getPledgeSubjectId());


    }
}