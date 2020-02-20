package ru.fds.tavrzcms3.dto.external;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Collection;

@Getter
@ToString
public class AuditResultDto {

    private String id;

    private LocalDate date;

    private String typeOfObject;

    private Long objectId;

    private String nameOfObject;

    private String typeOfAudit;

    private String valueInField;

    private String auditLevel;

    private String descriptionResult;

    private Collection<String> advice;

    private String auditStatus;
}
