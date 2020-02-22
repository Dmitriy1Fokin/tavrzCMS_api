package ru.fds.tavrzcms3.service;

import ru.fds.tavrzcms3.domain.ClientManager;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ClientManagerService {
    List<ClientManager> getAllClientManager();
    Optional<ClientManager> getClientManagerById(Long clientManagerId);
    Optional<ClientManager> getClientManagerByClient(Long clientId);
    List<ClientManager> getNewClientManagersFromFile(File file) throws IOException;
    List<ClientManager>  getCurrentClientManagersFromFile(File file) throws IOException;
    ClientManager insertUpdateClientManager(ClientManager clientManager);
    List<ClientManager> updateInsertClientManagers(List<ClientManager> clientManagerList);
}
