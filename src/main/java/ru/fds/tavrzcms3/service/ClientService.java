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
import java.util.*;

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





    public Optional<Client> getClientById(long clientId){
        return repositoryClient.findById(clientId);
    }

    public List<Client> getClientByEmployee(Employee employee){
        return repositoryClient.findByEmployee(employee);
    }

    public int countOfCurrentPledgeAgreementsByPledgorId(long pledgorId){
        return repositoryPledgeAgreement.countAllByClientAndStatusPAEquals(repositoryClient.findByClientId(pledgorId), "открыт");
    }

    public List<PledgeAgreement> getCurrentPledgeAgreementsByPledgorId(long pledgorId){
        return repositoryPledgeAgreement.findByClientAndStatusPA(repositoryClient.findByClientId(pledgorId), "открыт");
    }

    public int countOfClosedPledgeAgreementsByPledgorId(long pledgorId){
        return repositoryPledgeAgreement.countAllByClientAndStatusPAEquals(repositoryClient.findByClientId(pledgorId), "закрыт");
    }

    public List<PledgeAgreement> getClosedPledgeAgreementsByPledgorId(long pledgorId){
        return repositoryPledgeAgreement.findByClientAndStatusPA(repositoryClient.findByClientId(pledgorId), "закрыт");
    }

    public int countOfCurrentLoanEgreementsByLoanerId(long loanerId){
        return repositoryLoanAgreement.countAllByClientAndStatusLAEquals(repositoryClient.findByClientId(loanerId), "открыт");
    }

    public List<LoanAgreement> getCurrentLoanAgreementsByLoanerId(long loanerId){
        return repositoryLoanAgreement.findByClientAndStatusLAEquals(repositoryClient.findByClientId(loanerId), "открыт");
    }

    public int countOfClosedLoanEgreementsByLoanerId(long loanerId){
        return repositoryLoanAgreement.countAllByClientAndStatusLAEquals(repositoryClient.findByClientId(loanerId), "закрыт");
    }

    public List<LoanAgreement> getClosedLoanAgreementsByLoanerId(long loanerId){
        return repositoryLoanAgreement.findByClientAndStatusLAEquals(repositoryClient.findByClientId(loanerId), "закрыт");
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
    public Client updateInsertClient(Client client){
        if(client.getClass()==ClientLegalEntity.class)
            client = repositoryClientLegalEntity.save((ClientLegalEntity) client);
        else if(client.getClass()==ClientIndividual.class)
            client = repositoryClientIndividual.save((ClientIndividual) client);

        return client;
    }

    @Transactional
    public List<ClientLegalEntity> updateInsertClientLegalEntityList(List<ClientLegalEntity> clientLegalEntityList){
        return repositoryClientLegalEntity.saveAll(clientLegalEntityList);
    }

    @Transactional
    public List<ClientIndividual> updateInsertClientIndividualList(List<ClientIndividual> clientIndividualList){
        return repositoryClientIndividual.saveAll(clientIndividualList);
    }

    public String getFullNameClient(long clientId){
        Optional<Client> client = repositoryClient.findById(clientId);

        if(client.isPresent()){
            if(client.get().getTypeOfClient().equals("фл")){
                ClientIndividual clientIndividual = repositoryClientIndividual.findByClient(client.get());

                return clientIndividual.getSurname() + " " + clientIndividual.getName() + " " + clientIndividual.getPatronymic();

            }else if(client.get().getTypeOfClient().equals("юл")){
                ClientLegalEntity clientLegalEntity = repositoryClientLegalEntity.findByClient(client.get());

                return clientLegalEntity.getOrganizationalForm() + " " + clientLegalEntity.getName();
            }else
                return null;

        }else
            return null;
    }

}
