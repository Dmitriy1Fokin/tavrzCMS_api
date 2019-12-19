package ru.fds.tavrzcms3.service;


import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fds.tavrzcms3.dictionary.Operations;
import ru.fds.tavrzcms3.dictionary.TypeOfClient;
import ru.fds.tavrzcms3.domain.*;
import ru.fds.tavrzcms3.repository.*;
import ru.fds.tavrzcms3.specification.SearchCriteria;
import ru.fds.tavrzcms3.specification.SearchCriteriaNestedAttribute;
import ru.fds.tavrzcms3.specification.SpecificationBuilder;
import ru.fds.tavrzcms3.specification.SpecificationBuilderImpl;

import java.util.*;

@Service
public class ClientService {

    private final RepositoryClient repositoryClient;

    public ClientService(RepositoryClient repositoryClient) {
        this.repositoryClient = repositoryClient;
    }

    public Optional<Client> getClientById(long clientId){
        return repositoryClient.findById(clientId);
    }

    public List<Client> getClientsByIds(List<Long> ids){
        return repositoryClient.findAllByClientIdIn(ids);
    }

    public List<Client> getClientByEmployee(Employee employee){
        return repositoryClient.findByEmployee(employee);
    }

    public List<Client> getAllClientsByClientManager(ClientManager clientManager){
        return repositoryClient.findAllByClientManager(clientManager);
    }

    public String getFullNameClient(long clientId){
        Optional<Client> clientOptional = repositoryClient.findById(clientId);

        StringBuilder fullName = new StringBuilder();
        if(clientOptional.isPresent()){
            Client client = clientOptional.get();
            if(client.getTypeOfClient() == TypeOfClient.INDIVIDUAL){

                if(Objects.nonNull(client.getClientIndividual().getSurname()) && !client.getClientIndividual().getSurname().isEmpty()){
                    fullName.append(client.getClientIndividual().getSurname());
                }
                if(Objects.nonNull(client.getClientIndividual().getName()) && !client.getClientIndividual().getName().isEmpty()){
                    fullName.append(" ")
                            .append(client.getClientIndividual().getName());
                }
                if(Objects.nonNull(client.getClientIndividual().getPatronymic()) && !client.getClientIndividual().getPatronymic().isEmpty()){
                    fullName.append(" ")
                            .append(client.getClientIndividual().getPatronymic());
                }

                return fullName.toString();

            }else if(client.getTypeOfClient() == TypeOfClient.LEGAL_ENTITY){

                if(Objects.nonNull(client.getClientLegalEntity().getOrganizationalForm())
                        && !client.getClientLegalEntity().getOrganizationalForm().isEmpty()){
                    fullName.append(client.getClientLegalEntity().getOrganizationalForm());
                }
                if(Objects.nonNull(client.getClientLegalEntity().getName()) && !client.getClientLegalEntity().getName().isEmpty()){
                    fullName.append(" \"")
                            .append(client.getClientLegalEntity().getName())
                            .append("\"");
                }

                return fullName.toString();

            }else
                return "";
        }else
            return "";
    }

    public List<Client> getClientFromSearch(Map<String, String> searchParam){

        SpecificationBuilder builder = new SpecificationBuilderImpl();

        if(Objects.nonNull(searchParam.get("typeOfClient")) && !searchParam.get("typeOfClient").isEmpty()){

            SearchCriteria searchCriteria = SearchCriteria.builder()
                    .key("typeOfClient")
                    .value(TypeOfClient.valueOf(searchParam.get("typeOfClient")))
                    .operation(Operations.EQUAL_IGNORE_CASE)
                    .predicate(false)
                    .build();
            builder.with(searchCriteria);


            if(searchParam.get("typeOfClient").equals(TypeOfClient.LEGAL_ENTITY.name())){

                if(Objects.nonNull(searchParam.get("clientName")) && !searchParam.get("clientName").isEmpty()){
                    SearchCriteriaNestedAttribute searchCriteriaNestedAttribute = SearchCriteriaNestedAttribute.builder()
                            .nestedObjectName("clientLegalEntity")
                            .key("name")
                            .value(searchParam.get("clientName"))
                            .operation(Operations.EQUAL_IGNORE_CASE)
                            .predicate(false)
                            .build();
                    builder.withNestedAttribute(searchCriteriaNestedAttribute);
                }

                if(Objects.nonNull(searchParam.get("inn")) && !searchParam.get("inn").isEmpty()){
                    SearchCriteriaNestedAttribute searchCriteriaNestedAttribute = SearchCriteriaNestedAttribute.builder()
                            .nestedObjectName("clientLegalEntity")
                            .key("inn")
                            .value(searchParam.get("inn"))
                            .operation(Operations.EQUAL_IGNORE_CASE)
                            .predicate(false)
                            .build();
                    builder.withNestedAttribute(searchCriteriaNestedAttribute);
                }
            }else if(searchParam.get("typeOfClient").equals(TypeOfClient.INDIVIDUAL.name())){

                if(Objects.nonNull(searchParam.get("clientName")) && !searchParam.get("clientName").isEmpty()){
                    String[] words = searchParam.get("clientName").split("\\s");
                    if(words.length == 1) {
                        SearchCriteriaNestedAttribute searchCriteriaNestedAttribute = SearchCriteriaNestedAttribute.builder()
                                .nestedObjectName("clientIndividual")
                                .key("surname")
                                .value(words[0])
                                .operation(Operations.EQUAL_IGNORE_CASE)
                                .predicate(false)
                                .build();
                        builder.withNestedAttribute(searchCriteriaNestedAttribute);

                    }else if(words.length == 2){
                        SearchCriteriaNestedAttribute surname = SearchCriteriaNestedAttribute.builder()
                                .nestedObjectName("clientIndividual")
                                .key("surname")
                                .value(words[0])
                                .operation(Operations.EQUAL_IGNORE_CASE)
                                .predicate(false)
                                .build();
                        builder.withNestedAttribute(surname);

                        SearchCriteriaNestedAttribute name = SearchCriteriaNestedAttribute.builder()
                                .nestedObjectName("clientIndividual")
                                .key("name")
                                .value(words[1])
                                .operation(Operations.EQUAL_IGNORE_CASE)
                                .predicate(false)
                                .build();
                        builder.withNestedAttribute(name);

                    }else if(words.length >= 3){
                        SearchCriteriaNestedAttribute surname = SearchCriteriaNestedAttribute.builder()
                                .nestedObjectName("clientIndividual")
                                .key("surname")
                                .value(words[0])
                                .operation(Operations.EQUAL_IGNORE_CASE)
                                .predicate(false)
                                .build();
                        builder.withNestedAttribute(surname);

                        SearchCriteriaNestedAttribute name = SearchCriteriaNestedAttribute.builder()
                                .nestedObjectName("clientIndividual")
                                .key("name")
                                .value(words[1])
                                .operation(Operations.EQUAL_IGNORE_CASE)
                                .predicate(false)
                                .build();
                        builder.withNestedAttribute(name);

                        SearchCriteriaNestedAttribute patronymic = SearchCriteriaNestedAttribute.builder()
                                .nestedObjectName("clientIndividual")
                                .key("patronymic")
                                .value(words[2])
                                .operation(Operations.EQUAL_IGNORE_CASE)
                                .predicate(false)
                                .build();
                        builder.withNestedAttribute(patronymic);

                    }
                }

                if(Objects.nonNull(searchParam.get("pasportNum")) && !searchParam.get("pasportNum").isEmpty()){
                    SearchCriteriaNestedAttribute searchCriteriaNestedAttribute = SearchCriteriaNestedAttribute.builder()
                            .nestedObjectName("clientIndividual")
                            .key("pasportNum")
                            .value(searchParam.get("pasportNum"))
                            .operation(Operations.EQUAL_IGNORE_CASE)
                            .predicate(false)
                            .build();
                    builder.withNestedAttribute(searchCriteriaNestedAttribute);
                }

            }
        }

        Specification<Client> specification = builder.build();

        return repositoryClient.findAll(specification);
    }

    @Transactional
    public Client updateInsertClient(Client client){
        return repositoryClient.save(client);
    }
}
