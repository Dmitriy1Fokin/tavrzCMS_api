package ru.fds.tavrzcms3.converver;

import ru.fds.tavrzcms3.domain.CostHistory;
import ru.fds.tavrzcms3.dto.CostHistoryDto;
import ru.fds.tavrzcms3.service.PledgeSubjectService;

public class CostHistoryConverter implements Converter<CostHistory, CostHistoryDto> {

    private final PledgeSubjectService pledgeSubjectService;

    public CostHistoryConverter(PledgeSubjectService pledgeSubjectService) {
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
