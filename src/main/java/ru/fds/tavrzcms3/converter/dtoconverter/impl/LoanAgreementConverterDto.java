package ru.fds.tavrzcms3.converter.dtoconverter.impl;

import org.springframework.stereotype.Component;
import ru.fds.tavrzcms3.converter.dtoconverter.ConverterDto;
import ru.fds.tavrzcms3.domain.LoanAgreement;
import ru.fds.tavrzcms3.domain.PledgeAgreement;
import ru.fds.tavrzcms3.dto.LoanAgreementDto;
import ru.fds.tavrzcms3.service.ClientService;
import ru.fds.tavrzcms3.service.PledgeAgreementService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class LoanAgreementConverterDto implements ConverterDto<LoanAgreement, LoanAgreementDto> {

    private final ClientService clientService;
    private final PledgeAgreementService pledgeAgreementService;

    public LoanAgreementConverterDto(ClientService clientService,
                                     PledgeAgreementService pledgeAgreementService) {
        this.clientService = clientService;
        this.pledgeAgreementService = pledgeAgreementService;
    }

    @Override
    public LoanAgreement toEntity(LoanAgreementDto dto) {
        List<PledgeAgreement> pledgeAgreementList;
        if(dto.getPledgeAgreementsIds() != null)
            pledgeAgreementList = pledgeAgreementService.getPledgeAgreementsByIds(dto.getPledgeAgreementsIds());
        else
            pledgeAgreementList = Collections.emptyList();

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
                .pledgeAgreements(pledgeAgreementList)
                .build();
    }

    @Override
    public LoanAgreementDto toDto(LoanAgreement entity) {
        List<Long> pledgeAgreementDtoList = new ArrayList<>();
        for(PledgeAgreement pa : pledgeAgreementService.getAllPledgeAgreementsByLoanAgreement(entity))
            pledgeAgreementDtoList.add(pa.getPledgeAgreementId());

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
                .pledgeAgreementsIds(pledgeAgreementDtoList)
                .clientName(clientService.getFullNameClient(entity.getClient().getClientId()))
                .build();
    }
}
