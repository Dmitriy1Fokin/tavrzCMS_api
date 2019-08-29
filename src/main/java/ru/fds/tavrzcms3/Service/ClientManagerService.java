package ru.fds.tavrzcms3.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.fds.tavrzcms3.domain.ClientManager;
import ru.fds.tavrzcms3.repository.RepositoryClientManager;

import java.util.List;

@Service
public class ClientManagerService {

    @Autowired
    RepositoryClientManager repositoryClientManager;

    public List<ClientManager> getAllClientManager(){
        Sort sortByDateSuname = new Sort(Sort.Direction.ASC, "surname");
        return repositoryClientManager.findAll(sortByDateSuname);
    }
}