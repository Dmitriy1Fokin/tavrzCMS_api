package ru.fds.tavrzcms3.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fds.tavrzcms3.domain.PledgeSubjectTBO;
import ru.fds.tavrzcms3.repository.RepositoryPledgeSubjectTBO;

@Service
public class PledgeSubjectTBOService {

    @Autowired
    RepositoryPledgeSubjectTBO repositoryPledgeSubjectTBO;

    public PledgeSubjectTBO getPledgeSubjectTBO(long pledgeSubjectId){
        return repositoryPledgeSubjectTBO.getOne(pledgeSubjectId);
    }

    @Transactional
    public PledgeSubjectTBO updatePledgeSubjectTBO(PledgeSubjectTBO pledgeSubjectTBO){
        return repositoryPledgeSubjectTBO.save(pledgeSubjectTBO);
    }
}
