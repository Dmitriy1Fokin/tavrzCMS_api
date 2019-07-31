package ru.fds.tavrzcms3.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.repository.RepositoryPledgeSubject;

@Service
public class PledgeSubjectService {

    @Autowired
    RepositoryPledgeSubject repositoryPledgeSubject;

    public synchronized PledgeSubject getPledgeSubjectById(long id){
        PledgeSubject pledgeSubject = repositoryPledgeSubject.findByPledgeSubjectId(id);
        return  pledgeSubject;
    }
}
