package ru.fds.tavrzcms3.service;

import org.springframework.data.domain.Pageable;
import ru.fds.tavrzcms3.dictionary.TypeOfClient;
import ru.fds.tavrzcms3.domain.Client;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ClientService {
    Optional<Client> getClientById(Long clientId);
    List<Client> getAllClients(Pageable pageable);
    List<Client> getClientsByEmployee(Long employeeId);
    List<Client> getClientsByClientManager(Long clientManagerId);
    Integer getCountClientByClientManager(Long clientManagerId);
    String getFullNameClient(Long clientId);
    List<Client> getClientFromSearch(Map<String, String> searchParam) throws ReflectiveOperationException;
    List<Client> getNewClientsFromFile(File file, TypeOfClient typeOfClient) throws IOException;
    List<Client> getCurrentClientsFromFile(File file) throws IOException;
    Client updateInsertClient(Client client);
    List<Client> updateInsertClients(List<Client> clientList);
}
