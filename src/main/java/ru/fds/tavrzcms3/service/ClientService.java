package ru.fds.tavrzcms3.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fds.tavrzcms3.dictionary.TypeOfClient;
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

    public List<ClientLegalEntity> getClientLegalEntityByName(String name){
        return repositoryClientLegalEntity.findByNameContainingIgnoreCase(name);
    }

    public ClientIndividual getClientIndividualByClient(Client client){
        return repositoryClientIndividual.findByClient(client);
    }

    public ClientLegalEntity getClientLegalEntityByClient(Client client){
        return repositoryClientLegalEntity.findByClient(client);
    }

    public List<ClientIndividual> getClientIndividualByFio(String[] fio){
        if(fio.length == 1)
            return repositoryClientIndividual.findBySurnameContainingIgnoreCase(fio[0]);
        else if(fio.length == 2)
            return repositoryClientIndividual.findBySurnameContainingIgnoreCaseAndNameContainingIgnoreCase(fio[0], fio[1]);
        else if(fio.length > 2)
            return repositoryClientIndividual.findBySurnameContainingIgnoreCaseAndNameContainingIgnoreCaseAndPatronymicContainingIgnoreCase(fio[0], fio[1], fio[2]);
        else
            return new ArrayList<>();
    }

    public String getFullNameClient(long clientId){
        Optional<Client> client = repositoryClient.findById(clientId);

        if(client.isPresent()){
            if(client.get() instanceof ClientLegalEntity){

                ClientLegalEntity clientLegalEntity = repositoryClientLegalEntity.findByClient(client.get());

                return clientLegalEntity.getOrganizationalForm() + " " + clientLegalEntity.getName();

            }else if(client.get() instanceof ClientIndividual){

                ClientIndividual clientIndividual = repositoryClientIndividual.findByClient(client.get());

                StringBuilder fullName = new StringBuilder();

                if(Objects.nonNull(clientIndividual.getSurname()) && !clientIndividual.getSurname().isEmpty())
                    fullName.append(clientIndividual.getSurname());
                if(Objects.nonNull(clientIndividual.getName()) && !clientIndividual.getName().isEmpty())
                    fullName.append(" ").append(clientIndividual.getName());
                if(Objects.nonNull(clientIndividual.getPatronymic()) && !clientIndividual.getPatronymic().isEmpty())
                    fullName.append(" ").append(clientIndividual.getPatronymic());

                return fullName.toString();

            }else {
                return "";
            }

        }else {
            return "";
        }
    }

    public List<Client> getClientFromSearch(Map<String, String> searchParam){

//        SpecificationBuilder builder = new SpecificationBuilderImpl();
//
//        if(searchParam.get("clientOption").equals(TypeOfClient.LEGAL_ENTITY.name())){
//            if(!searchParam.get("clientName").isEmpty())
//                builder.with("name", ":", searchParam.get("clientName"), false);
//            if(!searchParam.get("inn").isEmpty())
//                builder.with("inn", ":", searchParam.get("inn"), false);
//
//            Specification<ClientLegalEntity> specification = builder.build();
//
//            return repositoryClientLegalEntity.findAll(specification);
//
//
//        }else{
//            if(!searchParam.get("clientName").isEmpty()){
//                String[] words = searchParam.get("clientName").split("\\s");
//
//                if(words.length == 1) {
//                    builder.with("surname",":", words[0], false);
//                }
//                else if(words.length == 2){
//                    builder.with("surname",":", words[0], false);
//                    builder.with("name",":", words[1],false);
//                }else if(words.length >= 3){
//                    builder.with("surname",":", words[0], false);
//                    builder.with("name",":", words[1],false);
//                    builder.with("patronymic", ":", words[2],false);
//                }
//            }
//            if(!searchParam.get("pasportNum").isEmpty())
//                builder.with("pasportNum", ":", searchParam.get("pasportNum"),false);
//
//            Specification<ClientIndividual> specification = builder.build();
//
//            return repositoryClientIndividual.findAll(specification);
//        }

        return null;
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

}
