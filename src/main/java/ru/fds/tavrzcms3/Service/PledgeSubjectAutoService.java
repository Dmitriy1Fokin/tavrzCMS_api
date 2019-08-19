package ru.fds.tavrzcms3.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fds.tavrzcms3.domain.PledgeSubjectAuto;
import ru.fds.tavrzcms3.repository.RepositoryPledgeSubjectAuto;

@Service
public class PledgeSubjectAutoService {

    @Autowired
    RepositoryPledgeSubjectAuto repositoryPledgeSubjectAuto;

    public PledgeSubjectAuto getPledgeSubjectAuto(long pledgeSubjectId){
        return repositoryPledgeSubjectAuto.getOne(pledgeSubjectId);
    }

    @Transactional
    public PledgeSubjectAuto updatePledgeSubjectAuto(PledgeSubjectAuto pledgeSubjectAuto){
        return repositoryPledgeSubjectAuto.save(pledgeSubjectAuto);
    }
}
