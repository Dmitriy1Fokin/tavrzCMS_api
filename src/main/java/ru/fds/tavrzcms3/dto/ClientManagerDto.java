package ru.fds.tavrzcms3.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.fds.tavrzcms3.validate.validationgroup.Exist;
import ru.fds.tavrzcms3.validate.validationgroup.New;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientManagerDto{

    @NotNull(groups = Exist.class)
    @Null(groups = New.class)
    private Long clientManagerId;

    @NotBlank(message = "Обязательно для заполнения")
    private String surname;

    @NotBlank(message = "Обязательно для заполнения")
    private String name;

    private String patronymic;

    @Null(groups = New.class)
    private List<Long> clientsIds;

    private String fullName;
}
