package ru.fds.tavrzcms3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fds.tavrzcms3.domain.*;
import ru.fds.tavrzcms3.repository.*;
import ru.fds.tavrzcms3.specification.SpecificationBuilder;
import ru.fds.tavrzcms3.specification.SpecificationBuilderImpl;

import java.util.List;
import java.util.Map;

@Service
public class ClientService {

    @Autowired
    RepositoryClient repositoryClient;
    @Autowired
    RepositoryClientLegalEntity repositoryClientLegalEntity;
    @Autowired
    RepositoryClientIndividual repositoryClientIndividual;
    @Autowired
    RepositoryPledgeAgreement repositoryPledgeAgreement;
    @Autowired
    RepositoryLoanAgreement repositoryLoanAgreement;

    public Client getClientByClientId(long clientId){
        return repositoryClient.getOne(clientId);
    }

    public int countOfCurrentPledgeAgreementsByPledgorId(long pledgorId){
        return repositoryPledgeAgreement.countAllByPledgorAndStatusPAEquals(repositoryClient.getOne(pledgorId), "открыт");
    }

    public List<PledgeAgreement> getCurrentPledgeAgreementsByPledgorId(long pledgorId){
        return repositoryPledgeAgreement.findByPledgorAndStatusPA(repositoryClient.getOne(pledgorId), "открыт");
    }

    public int countOfClosedPledgeAgreementsByPledgorId(long pledgorId){
        return repositoryPledgeAgreement.countAllByPledgorAndStatusPAEquals(repositoryClient.getOne(pledgorId), "закрыт");
    }

    public List<PledgeAgreement> getClosedPledgeAgreementsByPledgorId(long pledgorId){
        return repositoryPledgeAgreement.findByPledgorAndStatusPA(repositoryClient.getOne(pledgorId), "закрыт");
    }

    public int countOfCurrentLoanEgreementsByLoanerId(long loanerId){
        return repositoryLoanAgreement.countAllByLoanerAndStatusLAEquals(repositoryClient.getOne(loanerId), "открыт");
    }

    public List<LoanAgreement> getCurrentLoanAgreementsByLoanerId(long loanerId){
        return repositoryLoanAgreement.findByLoanerAndStatusLAEquals(repositoryClient.getOne(loanerId), "открыт");
    }

    public int countOfClosedLoanEgreementsByLoanerId(long loanerId){
        return repositoryLoanAgreement.countAllByLoanerAndStatusLAEquals(repositoryClient.getOne(loanerId), "закрыт");
    }

    public List<LoanAgreement> getClosedLoanAgreementsByLoanerId(long loanerId){
        return repositoryLoanAgreement.findByLoanerAndStatusLAEquals(repositoryClient.getOne(loanerId), "закрыт");
    }

    public Page<Client> getClientFromSearch(Map<String, String> searchParam){

        int currentPage = Integer.parseInt(searchParam.get("page"));
        int pageSize = Integer.parseInt(searchParam.get("size"));
        Pageable pageable = PageRequest.of(currentPage, pageSize);

        SpecificationBuilder builder = new SpecificationBuilderImpl();

        if(searchParam.get("clientOption").equals("юл")){
            if(!searchParam.get("clientName").isEmpty())
                builder.with("name", ":", searchParam.get("clientName"), false);
            if(!searchParam.get("inn").isEmpty())
                builder.with("inn", ":", searchParam.get("inn"), false);

            Specification<ClientLegalEntity> specification = builder.build();

            return repositoryClientLegalEntity.findAll(specification, pageable);


        }else{
            if(!searchParam.get("clientName").isEmpty()){
                String[] words = searchParam.get("clientName").split("\\s");

                if(words.length == 1) {
                    builder.with("surname",":", words[0], false);
                }
                else if(words.length == 2){
                    builder.with("surname",":", words[0], false);
                    builder.with("name",":", words[1],false);
                }else if(words.length >= 3){
                    builder.with("surname",":", words[0], false);
                    builder.with("name",":", words[1],false);
                    builder.with("patronymic", ":", words[2],false);
                }
            }
            if(!searchParam.get("pasportNum").isEmpty())
                builder.with("pasportNum", ":", searchParam.get("pasportNum"),false);

            Specification<ClientIndividual> specification = builder.build();

            return repositoryClientIndividual.findAll(specification, pageable);
        }

    }

    @Transactional
    public ClientIndividual updateClientIndividual(ClientIndividual clientIndividual){
        return repositoryClientIndividual.save(clientIndividual);
    }

    @Transactional
    public ClientLegalEntity updateClientLegalEntity(ClientLegalEntity clientLegalEntity){
        return repositoryClientLegalEntity.save(clientLegalEntity);
    }

    @Transactional
    public Client saveClientLegalEntity(ClientLegalEntity clientLegalEntity){
        return repositoryClientLegalEntity.save(clientLegalEntity);
    }

    @Transactional
    public Client saveClientIndividual(ClientIndividual clientIndividual){
        return repositoryClientIndividual.save(clientIndividual);
    }

}