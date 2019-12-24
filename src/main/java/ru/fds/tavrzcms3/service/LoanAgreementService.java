package ru.fds.tavrzcms3.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fds.tavrzcms3.dictionary.Operations;
import ru.fds.tavrzcms3.dictionary.StatusOfAgreement;
import ru.fds.tavrzcms3.domain.*;
import ru.fds.tavrzcms3.repository.*;
import ru.fds.tavrzcms3.specification.SearchCriteria;
import ru.fds.tavrzcms3.specification.SpecificationBuilder;
import ru.fds.tavrzcms3.specification.impl.SpecificationBuilderImpl;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class LoanAgreementService {

    private final RepositoryLoanAgreement repositoryLoanAgreement;
    private final RepositoryClient repositoryClient;
    private final ClientService clientService;

    public LoanAgreementService(RepositoryLoanAgreement repositoryLoanAgreement,
                                RepositoryClient repositoryClient,
                                ClientService clientService) {
        this.repositoryLoanAgreement = repositoryLoanAgreement;
        this.repositoryClient = repositoryClient;
        this.clientService = clientService;
    }


    public Optional<LoanAgreement> getLoanAgreementById(long loanAgreementId){
        return repositoryLoanAgreement.findById(loanAgreementId);
    }

    public List<LoanAgreement> getLoanAgreementsByIds(List<Long> ids){
        return repositoryLoanAgreement.findAllByLoanAgreementIdIn(ids);
    }

    public List<LoanAgreement> getAllLoanAgreementsByLoaner(Client client){
        return repositoryLoanAgreement.findAllByClient(client);
    }

    public List<LoanAgreement> getAllLoanAgreementByPledgeAgreement(PledgeAgreement pledgeAgreement){
        return repositoryLoanAgreement.findAllByPledgeAgreements(pledgeAgreement);
    }

    public List<LoanAgreement> getAllLoanAgreementByPledgeAgreements(List<PledgeAgreement> pledgeAgreementList){
        return repositoryLoanAgreement.findAllByPledgeAgreementsIn(pledgeAgreementList);
    }

    public List<LoanAgreement> getCurrentLoanAgreementsByPledgeAgreement(PledgeAgreement pledgeAgreement){
        return repositoryLoanAgreement.findByPledgeAgreementsAndStatusLAEquals(pledgeAgreement, StatusOfAgreement.OPEN);
    }

    public List<LoanAgreement> getClosedLoanAgreementsByPledgeAgreement(PledgeAgreement pledgeAgreement){
        return repositoryLoanAgreement.findByPledgeAgreementsAndStatusLAEquals(pledgeAgreement, StatusOfAgreement.CLOSED);
    }

    public int countOfCurrentLoanAgreementsByEmployee(Employee employee){
        List<Client> loaners = clientService.getClientByEmployee(employee);
        return repositoryLoanAgreement.countAllByClientInAndStatusLAEquals(loaners, StatusOfAgreement.OPEN);
    }

    public int countOfAllCurrentLoanAgreements(){
        return repositoryLoanAgreement.countAllByStatusLAEquals(StatusOfAgreement.OPEN);
    }

    public List<LoanAgreement> getAllCurrentLoanAgreements(){
        return repositoryLoanAgreement.findAllByStatusLAEquals(StatusOfAgreement.OPEN);
    }

    public List<LoanAgreement> getCurrentLoanAgreementsByEmployee(Employee employee){
        List<Client> clientList = repositoryClient.findByEmployee(employee);
        return repositoryLoanAgreement.findByClientInAndStatusLAEquals(clientList, StatusOfAgreement.OPEN);
    }

    public List<LoanAgreement> getCurrentLoanAgreementsByLoaner(Client client){
        return repositoryLoanAgreement.findByClientAndStatusLAEquals(client, StatusOfAgreement.OPEN);
    }

    public List<LoanAgreement> getClosedLoanAgreementsByLoaner(Client client){
        return repositoryLoanAgreement.findByClientAndStatusLAEquals(client, StatusOfAgreement.CLOSED);
    }

    public List<LoanAgreement> getLoanAgreementFromSearch(Map<String, String> searchParam){

        SpecificationBuilder builder = new SpecificationBuilderImpl();

        for(Field field : LoanAgreement.class.getDeclaredFields()){
            if(searchParam.containsKey(field.getName())){
                if((field.getType() == String.class || field.getType() == double.class || field.getType() == byte.class)
                        && !searchParam.get(field.getName()).isEmpty()){
                    SearchCriteria searchCriteria = SearchCriteria.builder()
                            .key(field.getName())
                            .value(searchParam.get(field.getName()))
                            .operation(Operations.valueOf(searchParam.get(field.getName() + "Option")))
                            .predicate(false)
                            .build();
                    builder.with(searchCriteria);

                }else if(field.getType() == StatusOfAgreement.class && !searchParam.get(field.getName()).isEmpty()){
                    SearchCriteria searchCriteria = SearchCriteria.builder()
                            .key(field.getName())
                            .value(StatusOfAgreement.valueOf(searchParam.get(field.getName())))
                            .operation(Operations.EQUAL_IGNORE_CASE)
                            .predicate(false)
                            .build();
                    builder.with(searchCriteria);
                }else if(field.getType() == Client.class && !searchParam.get(field.getName()).isEmpty()){
                    Map<String, String> searchParamClient = new HashMap<>();
                    searchParamClient.put("typeOfClient", searchParam.get("typeOfClient"));
                    searchParamClient.put("clientName", searchParam.get(field.getName()));
                    List<Client> clientList = clientService.getClientFromSearch(searchParamClient);
                    if(clientList.isEmpty()){
                        SearchCriteria searchCriteria = SearchCriteria.builder()
                                .key(field.getName())
                                .value(null)
                                .operation(Operations.EQUAL_IGNORE_CASE)
                                .predicate(false)
                                .build();
                        builder.with(searchCriteria);
                    }else if(clientList.size() == 1){
                            SearchCriteria searchCriteria = SearchCriteria.builder()
                                    .key(field.getName())
                                    .value(clientList.get(0))
                                    .operation(Operations.EQUAL_IGNORE_CASE)
                                    .predicate(false)
                                    .build();
                            builder.with(searchCriteria);

                    }else {
                        SearchCriteria searchCriteriaFirst = SearchCriteria.builder()
                                .key(field.getName())
                                .value(clientList.get(0))
                                .operation(Operations.EQUAL_IGNORE_CASE)
                                .predicate(false)
                                .build();
                        builder.with(searchCriteriaFirst);

                        for(int i = 1; i < clientList.size(); i++){
                            SearchCriteria searchCriteria = SearchCriteria.builder()
                                    .key(field.getName())
                                    .value(clientList.get(i))
                                    .operation(Operations.EQUAL_IGNORE_CASE)
                                    .predicate(true)
                                    .build();
                            builder.with(searchCriteria);
                        }
                    }
                }else if(field.getType() == LocalDate.class && !searchParam.get(field.getName()).isEmpty()){

                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyy-MM-dd");
                    LocalDate localDate = LocalDate.parse(searchParam.get(field.getName()), dateTimeFormatter);

                    SearchCriteria searchCriteria = SearchCriteria.builder()
                            .key(field.getName())
                            .value(localDate)
                            .operation(Operations.valueOf(searchParam.get(field.getName() + "Option")))
                            .predicate(false)
                            .build();
                    builder.with(searchCriteria);
                }
            }
        }

        Specification<LoanAgreement> spec = builder.build();

        return repositoryLoanAgreement.findAll(spec);
    }

    @Transactional
    public LoanAgreement updateInsertLoanAgreement(LoanAgreement loanAgreement){
        return repositoryLoanAgreement.save(loanAgreement);
    }



}
