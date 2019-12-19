package ru.fds.tavrzcms3.converver;

import org.springframework.stereotype.Component;
import ru.fds.tavrzcms3.domain.ClientIndividual;
import ru.fds.tavrzcms3.dto.ClientIndividualDto;

@Component
public class ClientIndividualConverterDto implements ConverterDto<ClientIndividual, ClientIndividualDto> {

    @Override
    public ClientIndividual toEntity(ClientIndividualDto dto) {

        return ClientIndividual.builder()
                .surname(dto.getSurname())
                .name(dto.getName())
                .patronymic(dto.getPatronymic())
                .pasportNum(dto.getPasportNum())
                .build();
    }

    @Override
    public ClientIndividualDto toDto(ClientIndividual entity) {

        return ClientIndividualDto.builder()
                .surname(entity.getSurname())
                .name(entity.getName())
                .patronymic(entity.getPatronymic())
                .pasportNum(entity.getPasportNum())
                .build();
    }
}
