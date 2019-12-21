package ru.fds.tavrzcms3.converver.dtoconverter;

import org.springframework.stereotype.Component;
import ru.fds.tavrzcms3.domain.ClientLegalEntity;
import ru.fds.tavrzcms3.dto.ClientLegalEntityDto;

@Component
public class ClientLegalEntityConverterDto implements ConverterDto<ClientLegalEntity, ClientLegalEntityDto> {

    @Override
    public ClientLegalEntity toEntity(ClientLegalEntityDto dto) {

        return ClientLegalEntity.builder()
                .organizationalForm(dto.getOrganizationalForm())
                .name(dto.getName())
                .inn(dto.getInn())
                .build();
    }

    @Override
    public ClientLegalEntityDto toDto(ClientLegalEntity entity) {

        return ClientLegalEntityDto.builder()
                .organizationalForm(entity.getOrganizationalForm())
                .name(entity.getName())
                .inn(entity.getInn())
                .build();
    }
}
