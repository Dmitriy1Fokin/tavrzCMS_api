package ru.fds.tavrzcms3;

import ru.fds.tavrzcms3.dictionary.TypeOfClient;
import ru.fds.tavrzcms3.domain.AppUser;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.ClientManager;
import ru.fds.tavrzcms3.domain.embedded.ClientLegalEntity;

import java.util.List;

public final class TestUtils {

    public static AppUser getAppUser(){
        return AppUser.builder()
                .userId(1L)
                .name("name")
                .password("pass")
                .build();
    }

    public static ClientManager getClientManager(){
        return ClientManager.builder()
                .clientManagerId(1L)
                .surname("surname1")
                .name("name1")
                .build();
    }

    public static List<ClientManager> getClientManagerList(){
        ClientManager clientManager = ClientManager.builder()
                .clientManagerId(2L)
                .surname("surname2")
                .name("name2")
                .build();
        return List.of(getClientManager(), clientManager);
    }

    public static Client getClient(){
        return Client.builder()
                .clientId(1L)
                .typeOfClient(TypeOfClient.LEGAL_ENTITY)
                .clientLegalEntity(ClientLegalEntity.builder()
                        .organizationalForm("OOO")
                        .name("name2")
                        .build())
                .build();
    }

    public static List<Client> getClientList(){
        Client client = Client.builder()
                .clientId(2L)
                .typeOfClient(TypeOfClient.LEGAL_ENTITY)
                .clientLegalEntity(ClientLegalEntity.builder()
                        .organizationalForm("OOO")
                        .name("name2")
                        .build())
                .build();
        return List.of(getClient(), client);
    }

}
