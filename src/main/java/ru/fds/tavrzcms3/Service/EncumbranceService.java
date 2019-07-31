package ru.fds.tavrzcms3.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.fds.tavrzcms3.domain.Encumbrance;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.repository.RepositoryEncumbrance;

import java.util.List;

@Service
public class EncumbranceService {

    @Autowired
    RepositoryEncumbrance repositoryEncumbrance;

    public synchronized List<Encumbrance> getEncumbranceByPledgeSubject(PledgeSubject pledgeSubject){
        return repositoryEncumbrance.findAllByPledgeSubject(pledgeSubject);
    }
}
