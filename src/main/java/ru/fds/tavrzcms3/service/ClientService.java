package ru.fds.tavrzcms3.service;


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
import ru.fds.tavrzcms3.specification.Search;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class ClientService {

    private final RepositoryClient repositoryClient;
    private final ClientManagerService clientManagerService;
    private final EmployeeService employeeService;

    private final ExcelColumnNum excelColumnNum;

    public ClientService(RepositoryClient repositoryClient,
                         ClientManagerService clientManagerService,
                         EmployeeService employeeService,
                         ExcelColumnNum excelColumnNum) {
        this.repositoryClient = repositoryClient;
        this.clientManagerService = clientManagerService;
        this.employeeService = employeeService;
        this.excelColumnNum = excelColumnNum;
    }

    public Optional<Client> getClientById(long clientId){
        return repositoryClient.findById(clientId);
    }

    public List<Client> getAllClients(){
        return repositoryClient.findAll();
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

            }else if(typeOfClient == TypeOfClient.INDIVIDUAL){
                ClientIndividual clientIndividual = ClientIndividual.builder()
                        .surname(fileImporter.getString(excelColumnNum.getClientNew().getIndividual().getSurname()))
                        .name(fileImporter.getString(excelColumnNum.getClientNew().getIndividual().getName()))
                        .patronymic(fileImporter.getString(excelColumnNum.getClientNew().getIndividual().getPatronymic()))
                        .pasportNum(fileImporter.getString(excelColumnNum.getClientNew().getIndividual().getPasport()))
                        .build();
                client.setClientIndividual(clientIndividual);

            }

            if(Objects.isNull(fileImporter.getLong(excelColumnNum.getClientNew().getClientManager()))){
                throw new IOException("Неверный id{"
                        + fileImporter.getLong(excelColumnNum.getClientNew().getClientManager()) + ") клиентского менеджера. Строка: " + countRow);
            }
            Optional<ClientManager> clientManager = clientManagerService.getClientManagerById(fileImporter.getLong(excelColumnNum.getClientNew().getClientManager()));
            if(clientManager.isPresent()){
                client.setClientManager(clientManager.get());
            }else{
                throw new IOException("Клиентский менеджер с таким id(" + fileImporter.getLong(excelColumnNum.getClientNew().getClientManager()) + ") отсутствует. Строка: " + countRow);
            }

            if(Objects.isNull(fileImporter.getLong(excelColumnNum.getClientNew().getEmployee()))){
                throw new IOException("Неверный id{"
                        + fileImporter.getLong(excelColumnNum.getClientNew().getEmployee()) + ") ответственного сотрудника. Строка: " + countRow);
            }
            Optional<Employee> employee= employeeService.getEmployeeById(fileImporter.getLong(excelColumnNum.getClientNew().getEmployee()));
            if(employee.isPresent()){
                client.setEmployee(employee.get());
            }else{
                throw new IOException("Сотрудник с таким id(" + fileImporter.getLong(excelColumnNum.getClientNew().getEmployee()) + ") отсутствует. Строка: " + countRow);
            }

            clientList.add(client);

        }while (fileImporter.nextLine());

        return clientList;
    }

    public List<Client> getCurrentClientsFromFile(File file) throws IOException{
        FileImporter fileImporter = FileImporterFactory.getInstance(file);
        for(int i = 0; i < excelColumnNum.getStartRow(); i++){
            fileImporter.nextLine();
        }
        int countRow = excelColumnNum.getStartRow();

        List<Client> clientList = new ArrayList<>();

        do{
            countRow += 1;

            if(Objects.isNull(fileImporter.getLong(excelColumnNum.getClientUpdate().getClientId()))){
                throw new IOException("Неверный id{"
                        + fileImporter.getLong(excelColumnNum.getClientUpdate().getClientId()) + ") клиента.");
            }

            Optional<Client> client = getClientById(fileImporter.getLong(excelColumnNum.getClientUpdate().getClientId()));
            if(!client.isPresent()){
                throw new IOException("Клиент с таким id отсутствует ("
                        + fileImporter.getLong(excelColumnNum.getClientUpdate().getClientId())
                        + "). Строка: " + countRow);
            }

            if(Objects.isNull(fileImporter.getLong(excelColumnNum.getClientUpdate().getClientManager()))){
                throw new IOException("Неверный id{"
                        + fileImporter.getLong(excelColumnNum.getClientUpdate().getClientManager()) + ") клиентского менеджера. Строка: " + countRow);
            }
            Optional<ClientManager> clientManager = clientManagerService.getClientManagerById(fileImporter.getLong(excelColumnNum.getClientUpdate().getClientManager()));
            if(clientManager.isPresent()){
                client.get().setClientManager(clientManager.get());
            }else{
                throw new IOException("Клиентский менеджер с таким id(" + fileImporter.getLong(excelColumnNum.getClientUpdate().getClientManager()) + ") отсутствует. Строка: " + countRow);
            }

            if(Objects.isNull(fileImporter.getLong(excelColumnNum.getClientUpdate().getEmployee()))){
                throw new IOException("Неверный id{"
                        + fileImporter.getLong(excelColumnNum.getClientUpdate().getEmployee()) + ") ответственного сотрудника. Строка: " + countRow);
            }
            Optional<Employee> employee= employeeService.getEmployeeById(fileImporter.getLong(excelColumnNum.getClientUpdate().getEmployee()));
            if(employee.isPresent()){
                client.get().setEmployee(employee.get());
            }else{
                throw new IOException("Сотрудник с таким id(" + fileImporter.getLong(excelColumnNum.getClientUpdate().getEmployee()) + ") отсутствует. Строка: " + countRow);
            }

            if(Objects.nonNull(client.get().getClientLegalEntity())){

                String inn = "";
                if(Objects.nonNull(fileImporter.getInteger(excelColumnNum.getClientUpdate().getLegalEntity().getInn()))){
                    inn = fileImporter.getInteger(excelColumnNum.getClientUpdate().getLegalEntity().getInn()).toString();
                }
                client.get().getClientLegalEntity().setOrganizationalForm(fileImporter.getString(excelColumnNum.getClientUpdate().getLegalEntity().getOrganizationForm()));
                client.get().getClientLegalEntity().setName(fileImporter.getString(excelColumnNum.getClientUpdate().getLegalEntity().getName()));
                client.get().getClientLegalEntity().setInn(inn);
            }else if(Objects.nonNull(client.get().getClientIndividual())){

                client.get().getClientIndividual().setSurname(fileImporter.getString(excelColumnNum.getClientUpdate().getIndividual().getSurname()));
                client.get().getClientIndividual().setName(fileImporter.getString(excelColumnNum.getClientUpdate().getIndividual().getName()));
                client.get().getClientIndividual().setPatronymic(fileImporter.getString(excelColumnNum.getClientUpdate().getIndividual().getPatronymic()));
                client.get().getClientIndividual().setPasportNum(fileImporter.getString(excelColumnNum.getClientUpdate().getIndividual().getPasport()));
            }

            clientList.add(client.get());

        }while (fileImporter.nextLine());

        return clientList;
    }

    @Transactional
    public Client updateInsertClient(Client client){
        return repositoryClient.save(client);
    }

    @Transactional
    public List<Client> updateInsertClients(List<Client> clientList){
        return repositoryClient.saveAll(clientList);
    }
}
