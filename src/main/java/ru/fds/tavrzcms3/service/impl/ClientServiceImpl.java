package ru.fds.tavrzcms3.service.impl;


import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fds.tavrzcms3.dictionary.TypeOfClient;
import ru.fds.tavrzcms3.dictionary.excelproprities.ExcelColumnNum;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.ClientManager;
import ru.fds.tavrzcms3.domain.Employee;
import ru.fds.tavrzcms3.domain.embedded.ClientIndividual;
import ru.fds.tavrzcms3.domain.embedded.ClientLegalEntity;
import ru.fds.tavrzcms3.fileimport.FileImporter;
import ru.fds.tavrzcms3.fileimport.FileImporterFactory;
import ru.fds.tavrzcms3.repository.RepositoryClient;
import ru.fds.tavrzcms3.repository.RepositoryClientManager;
import ru.fds.tavrzcms3.repository.RepositoryEmployee;
import ru.fds.tavrzcms3.service.ClientService;
import ru.fds.tavrzcms3.specification.Search;
import ru.fds.tavrzcms3.validate.ValidatorEntity;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class ClientServiceImpl implements ClientService {

    private final RepositoryClient repositoryClient;
    private final RepositoryEmployee repositoryEmployee;
    private final RepositoryClientManager repositoryClientManager;
    private final ValidatorEntity validatorEntity;
    private final ExcelColumnNum excelColumnNum;

    private static final String MSG_WRONG_ID = "Неверный id{";
    private static final String MSG_LINE = ") отсутствует. Строка: ";

    public ClientServiceImpl(RepositoryClient repositoryClient,
                         RepositoryEmployee repositoryEmployee,
                         RepositoryClientManager repositoryClientManager,
                         ValidatorEntity validatorEntity,
                         ExcelColumnNum excelColumnNum) {
        this.repositoryClient = repositoryClient;
        this.repositoryEmployee = repositoryEmployee;
        this.repositoryClientManager = repositoryClientManager;
        this.validatorEntity = validatorEntity;
        this.excelColumnNum = excelColumnNum;
    }

    @Override
    public Optional<Client> getClientById(long clientId){
        return repositoryClient.findById(clientId);
    }

    @Override
    public List<Client> getAllClients(){
        return repositoryClient.findAll();
    }

    @Override
    public List<Client> getClientsByEmployee(Long employeeId){
        return repositoryClient.findByEmployee(employeeId);
    }

    @Override
    public List<Client> getClientsByClientManager(Long clientManagerId){
        return repositoryClient.findAllByClientManager(clientManagerId);
    }

    @Override
    public String getFullNameClient(long clientId){
        Client client = repositoryClient.findById(clientId)
                .orElseThrow(() -> new NullPointerException("Client not found"));

        StringBuilder fullName = new StringBuilder();

        if(client.getTypeOfClient() == TypeOfClient.INDIVIDUAL){
            fullName.append(client.getClientIndividual().getSurname())
                    .append(" ")
                    .append(client.getClientIndividual().getName());

            if(Objects.nonNull(client.getClientIndividual().getPatronymic()) && !client.getClientIndividual().getPatronymic().isEmpty()){
                fullName.append(" ")
                        .append(client.getClientIndividual().getPatronymic());
            }

        }else if(client.getTypeOfClient() == TypeOfClient.LEGAL_ENTITY){
            fullName.append(client.getClientLegalEntity().getOrganizationalForm())
                    .append(" \"")
                    .append(client.getClientLegalEntity().getName())
                    .append("\"");
        }

        return fullName.toString();

    }

    @Override
    public List<Client> getClientFromSearch(Map<String, String> searchParam) throws ReflectiveOperationException {
        final String SEARCH_PARAM_TYPE_OF_CLIENT = "typeOfClient";

        Search<Client> clientSearch = new Search<>(Client.class);

        clientSearch.withParam(searchParam);

        if(Objects.nonNull(searchParam.get(SEARCH_PARAM_TYPE_OF_CLIENT)) && !searchParam.get(SEARCH_PARAM_TYPE_OF_CLIENT).isEmpty()){
            if(searchParam.get(SEARCH_PARAM_TYPE_OF_CLIENT).equals(TypeOfClient.LEGAL_ENTITY.name())){
                clientSearch.withNestedAttributeParam(searchParam, "clientLegalEntity");
            }else if(searchParam.get(SEARCH_PARAM_TYPE_OF_CLIENT).equals(TypeOfClient.INDIVIDUAL.name())){
                clientSearch.withNestedAttributeParam(searchParam, "clientIndividual");
            }
        }else {
            return Collections.emptyList();
        }

        Specification<Client> specification = clientSearch.getSpecification();

        return repositoryClient.findAll(specification);
    }

    @Override
    @Transactional
    public List<Client> getNewClientsFromFile(File file, TypeOfClient typeOfClient) throws IOException {
        FileImporter fileImporter = FileImporterFactory.getInstance(file);
        for(int i = 0; i < excelColumnNum.getStartRow(); i++){
            fileImporter.nextLine();
        }
        int countRow = excelColumnNum.getStartRow();
        List<Client> clientList = new ArrayList<>();

        do{
            countRow += 1;

            Client client = new Client();
            client.setTypeOfClient(typeOfClient);
            if(typeOfClient == TypeOfClient.LEGAL_ENTITY){
                setNewClientLegalEntity(fileImporter, client);
            }else if(typeOfClient == TypeOfClient.INDIVIDUAL){
               setNewClientIndividual(fileImporter, client);
            }
            setNewClientManager(fileImporter, client, countRow);
            setNewEmployee(fileImporter, client, countRow);

            Set<ConstraintViolation<Client>> violations =  validatorEntity.validateEntity(client);
            if(!violations.isEmpty())
                throw new ConstraintViolationException("object " + countRow, violations);

            clientList.add(client);

        }while (fileImporter.nextLine());

        clientList = updateInsertClients(clientList);

        return clientList;
    }

    private void setNewClientLegalEntity(FileImporter fileImporter, Client client){
        String inn = "";
        if(Objects.nonNull(fileImporter.getInteger(excelColumnNum.getClientNew().getLegalEntity().getInn()))){
            inn = fileImporter.getInteger(excelColumnNum.getClientNew().getLegalEntity().getInn()).toString();
        }

        ClientLegalEntity clientLegalEntity = ClientLegalEntity.builder()
                .organizationalForm(fileImporter.getString(excelColumnNum.getClientNew().getLegalEntity().getOrganizationForm()))
                .name(fileImporter.getString(excelColumnNum.getClientNew().getLegalEntity().getName()))
                .inn(inn)
                .build();
        client.setClientLegalEntity(clientLegalEntity);
    }

    private void setNewClientIndividual(FileImporter fileImporter, Client client){
        ClientIndividual clientIndividual = ClientIndividual.builder()
                .surname(fileImporter.getString(excelColumnNum.getClientNew().getIndividual().getSurname()))
                .name(fileImporter.getString(excelColumnNum.getClientNew().getIndividual().getName()))
                .patronymic(fileImporter.getString(excelColumnNum.getClientNew().getIndividual().getPatronymic()))
                .pasportNum(fileImporter.getString(excelColumnNum.getClientNew().getIndividual().getPasport()))
                .build();
        client.setClientIndividual(clientIndividual);
    }

    private void setNewClientManager(FileImporter fileImporter, Client client, int countRow) throws IOException {
        if(Objects.isNull(fileImporter.getLong(excelColumnNum.getClientNew().getClientManager()))){
            throw new IOException(MSG_WRONG_ID
                    + fileImporter.getLong(excelColumnNum.getClientNew().getClientManager()) + ") клиентского менеджера. Строка: " + countRow);
        }

        ClientManager clientManager = repositoryClientManager
                .findById(fileImporter.getLong(excelColumnNum.getClientNew().getClientManager()))
                .orElseThrow(() -> new IOException("Клиентский менеджер с таким id(" +
                        fileImporter.getLong(excelColumnNum.getClientNew().getClientManager()) +
                        MSG_LINE +
                        countRow));

        client.setClientManager(clientManager);
    }

    private void setNewEmployee(FileImporter fileImporter, Client client, int countRow) throws IOException {
        if(Objects.isNull(fileImporter.getLong(excelColumnNum.getClientNew().getEmployee()))){
            throw new IOException(MSG_WRONG_ID
                    + fileImporter.getLong(excelColumnNum.getClientNew().getEmployee()) + ") ответственного сотрудника. Строка: " + countRow);
        }

        Employee employee = repositoryEmployee
                .findById(fileImporter.getLong(excelColumnNum.getClientNew().getEmployee()))
                .orElseThrow(() -> new IOException("Сотрудник с таким id(" +
                        fileImporter.getLong(excelColumnNum.getClientNew().getEmployee()) +
                        MSG_LINE + countRow));

        client.setEmployee(employee);
    }


    @Override
    @Transactional
    public List<Client> getCurrentClientsFromFile(File file) throws IOException{
        FileImporter fileImporter = FileImporterFactory.getInstance(file);
        for(int i = 0; i < excelColumnNum.getStartRow(); i++){
            fileImporter.nextLine();
        }
        int countRow = excelColumnNum.getStartRow();

        List<Client> clientList = new ArrayList<>();

        do{
            countRow += 1;

            Client client = setCurrentClient(fileImporter, countRow);
            setCurrentClientManager(fileImporter, client, countRow);
            setCurrentEmployee(fileImporter, client, countRow);

            if(Objects.nonNull(client.getClientLegalEntity())){
                setCurrentClientLegalEntity(fileImporter, client);
            }else if(Objects.nonNull(client.getClientIndividual())){
                setCurrentClientIndividual(fileImporter, client);
            }

            Set<ConstraintViolation<Client>> violations =  validatorEntity.validateEntity(client);
            if(!violations.isEmpty())
                throw new ConstraintViolationException("object " + countRow, violations);

            clientList.add(client);

        }while (fileImporter.nextLine());

        clientList = updateInsertClients(clientList);

        return clientList;
    }

    private Client setCurrentClient(FileImporter fileImporter, int countRow) throws IOException {
        if(Objects.isNull(fileImporter.getLong(excelColumnNum.getClientUpdate().getClientId()))){
            throw new IOException(MSG_WRONG_ID
                    + fileImporter.getLong(excelColumnNum.getClientUpdate().getClientId()) + ") клиента. Строка: " + countRow);
        }

        return getClientById(fileImporter.getLong(excelColumnNum.getClientUpdate().getClientId()))
                .orElseThrow(() -> new IOException("Клиент с таким id отсутствует ("
                        + fileImporter.getLong(excelColumnNum.getClientUpdate().getClientId())
                        + "). Строка: " + countRow));
    }

    private void setCurrentClientManager(FileImporter fileImporter, Client client, int countRow) throws IOException {
        if(Objects.isNull(fileImporter.getLong(excelColumnNum.getClientUpdate().getClientManager()))){
            throw new IOException(MSG_WRONG_ID
                    + fileImporter.getLong(excelColumnNum.getClientUpdate().getClientManager()) +
                    ") клиентского менеджера. Строка: " + countRow);
        }

        ClientManager clientManager = repositoryClientManager
                .findById(fileImporter.getLong(excelColumnNum.getClientUpdate().getClientManager()))
                .orElseThrow(() -> new IOException("Клиентский менеджер с таким id(" +
                        fileImporter.getLong(excelColumnNum.getClientUpdate().getClientManager()) +
                        MSG_LINE + countRow));

        client.setClientManager(clientManager);
    }

    private void setCurrentEmployee(FileImporter fileImporter, Client client, int countRow) throws IOException {
        if(Objects.isNull(fileImporter.getLong(excelColumnNum.getClientUpdate().getEmployee()))){
            throw new IOException(MSG_WRONG_ID
                    + fileImporter.getLong(excelColumnNum.getClientUpdate().getEmployee()) +
                    ") ответственного сотрудника. Строка: " + countRow);
        }

        Employee employee = repositoryEmployee.findById(fileImporter.getLong(excelColumnNum.getClientUpdate().getEmployee()))
                .orElseThrow(() -> new IOException("Сотрудник с таким id(" +
                        fileImporter.getLong(excelColumnNum.getClientUpdate().getEmployee()) +
                        MSG_LINE + countRow));

        client.setEmployee(employee);
    }

    private void setCurrentClientLegalEntity(FileImporter fileImporter, Client client){
        String inn = "";
        if(Objects.nonNull(fileImporter.getInteger(excelColumnNum.getClientUpdate().getLegalEntity().getInn()))){
            inn = fileImporter.getInteger(excelColumnNum.getClientUpdate().getLegalEntity().getInn()).toString();
        }
        client.getClientLegalEntity().setOrganizationalForm(fileImporter.getString(excelColumnNum.getClientUpdate().getLegalEntity().getOrganizationForm()));
        client.getClientLegalEntity().setName(fileImporter.getString(excelColumnNum.getClientUpdate().getLegalEntity().getName()));
        client.getClientLegalEntity().setInn(inn);
    }

    private void setCurrentClientIndividual(FileImporter fileImporter, Client client){
        client.getClientIndividual().setSurname(fileImporter.getString(excelColumnNum.getClientUpdate().getIndividual().getSurname()));
        client.getClientIndividual().setName(fileImporter.getString(excelColumnNum.getClientUpdate().getIndividual().getName()));
        client.getClientIndividual().setPatronymic(fileImporter.getString(excelColumnNum.getClientUpdate().getIndividual().getPatronymic()));
        client.getClientIndividual().setPasportNum(fileImporter.getString(excelColumnNum.getClientUpdate().getIndividual().getPasport()));
    }


    @Override
    @Transactional
    public Client updateInsertClient(Client client){
        return repositoryClient.save(client);
    }

    @Override
    @Transactional
    public List<Client> updateInsertClients(List<Client> clientList){
        return repositoryClient.saveAll(clientList);
    }
}
