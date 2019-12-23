package ru.fds.tavrzcms3.converver.dtoconverter.impl;

import org.springframework.stereotype.Component;
import ru.fds.tavrzcms3.converver.dtoconverter.ConverterDto;
import ru.fds.tavrzcms3.domain.CostHistory;
import ru.fds.tavrzcms3.dto.CostHistoryDto;
import ru.fds.tavrzcms3.service.PledgeSubjectService;

@Component
public class CostHistoryConverterDto implements ConverterDto<CostHistory, CostHistoryDto> {

    private final PledgeSubjectService pledgeSubjectService;

    public CostHistoryConverterDto(PledgeSubjectService pledgeSubjectService) {
        this.pledgeSubjectService = pledgeSubjectService;
    }

    @Override
    public CostHistory toEntity(CostHistoryDto dto) {
        return CostHistory.builder()
                .costHistoryId(dto.getCostHistoryId())
                .dateConclusion(dto.getDateConclusion())
                .zsDz(dto.getZsDz())
                .zsZz(dto.getZsZz())
                .rsDz(dto.getRsDz())
                .rsZz(dto.getRsZz())
                .ss(dto.getSs())
                .appraiser(dto.getAppraiser())
                .appraisalReportNum(dto.getAppraisalReportNum())
                .appraisalReportDate(dto.getAppraisalReportDate())
                .pledgeSubject(pledgeSubjectService.getPledgeSubjectById(dto.getPledgeSubjectId()).orElse(null))
                .build();
    }

    @Override
    public CostHistoryDto toDto(CostHistory entity) {
        return CostHistoryDto.builder()
                .costHistoryId(entity.getCostHistoryId())
                .dateConclusion(entity.getDateConclusion())
                .zsDz(entity.getZsDz())
                .zsZz(entity.getZsZz())
                .rsDz(entity.getRsDz())
                .rsZz(entity.getRsZz())
                .ss(entity.getSs())
                .appraiser(entity.getAppraiser())
                .appraisalReportNum(entity.getAppraisalReportNum())
                .appraisalReportDate(entity.getAppraisalReportDate())
                .pledgeSubjectId(entity.getPledgeSubject().getPledgeSubjectId())
                .build();
    }
}
