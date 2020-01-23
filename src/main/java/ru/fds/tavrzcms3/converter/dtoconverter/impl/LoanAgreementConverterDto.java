package ru.fds.tavrzcms3.converter.dtoconverter.impl;

import org.springframework.stereotype.Component;
import ru.fds.tavrzcms3.converter.dtoconverter.ConverterDto;
import ru.fds.tavrzcms3.domain.LoanAgreement;
import ru.fds.tavrzcms3.dto.LoanAgreementDto;
import ru.fds.tavrzcms3.service.ClientService;

@Component
public class LoanAgreementConverterDto implements ConverterDto<LoanAgreement, LoanAgreementDto> {

    private final ClientService clientService;

    public LoanAgreementConverterDto(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public LoanAgreement toEntity(LoanAgreementDto dto) {

        return LoanAgreement.builder()
                .loanAgreementId(dto.getLoanAgreementId())
                .numLA(dto.getNumLA())
                .dateBeginLA(dto.getDateBeginLA())
                .dateEndLA(dto.getDateEndLA())
                .statusLA(dto.getStatusLA())
                .amountLA(dto.getAmountLA())
                .debtLA(dto.getDebtLA())
                .interestRateLA(dto.getInterestRateLA())
                .pfo(dto.getPfo())
                .qualityCategory(dto.getQualityCategory())
                .client(clientService.getClientById(dto.getClientId()).orElse(null))
                .build();
    }

    @Override
    public LoanAgreementDto toDto(LoanAgreement entity) {
        return LoanAgreementDto.builder()
                .loanAgreementId(entity.getLoanAgreementId())
                .numLA(entity.getNumLA())
                .dateBeginLA(entity.getDateBeginLA())
                .dateEndLA(entity.getDateEndLA())
                .statusLA(entity.getStatusLA())
                .amountLA(entity.getAmountLA())
                .debtLA(entity.getDebtLA())
                .interestRateLA(entity.getInterestRateLA())
                .pfo(entity.getPfo())
                .qualityCategory(entity.getQualityCategory())
                .clientId(entity.getClient().getClientId())
                .clientName(clientService.getFullNameClient(entity.getClient().getClientId()))
                .build();
    }
}
