package ru.fds.tavrzcms3.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class CostHistoryDto {

    private Long costHistoryId;

    private Date dateConclusion;

    private double zsDz;

    private double zsZz;

    private double rsDz;

    private double rsZz;

    private double ss;

    private String appraiser;

    private String appraisalReportNum;

    private Date appraisalReportDate;

    private Long pledgeSubjectId;
}
