package ru.fds.tavrzcms3.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.fds.tavrzcms3.dictionary.StatusOfMonitoring;
import ru.fds.tavrzcms3.dictionary.TypeOfMonitoring;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonitoringDto{

    private Long monitoringId;

    @NotNull(message = "Обязательно для заполнения")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateMonitoring;

    private StatusOfMonitoring statusMonitoring;

    @NotBlank(message = "Обязательно для заполнения")
    private String employee;

    private TypeOfMonitoring typeOfMonitoring;

    private String notice;

    private BigDecimal collateralValue;

    private Long pledgeSubjectId;

}
