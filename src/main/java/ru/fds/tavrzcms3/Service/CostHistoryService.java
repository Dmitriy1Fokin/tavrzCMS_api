package ru.fds.tavrzcms3.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.fds.tavrzcms3.domain.CostHistory;
import ru.fds.tavrzcms3.repository.RepositoryCostHistory;
import ru.fds.tavrzcms3.repository.RepositoryPledgeSubject;

import java.util.List;

@Service
public class CostHistoryService {

    @Autowired
    RepositoryCostHistory repositoryCostHistory;

    @Autowired
    RepositoryPledgeSubject repositoryPledgeSubject;

    public synchronized List<CostHistory> getCostHistoryByPledgeSubjectId(long pledgeSubjectId){
        Sort sortByDateConclusion = new Sort(Sort.Direction.DESC, "dateConclusion");
        return repositoryCostHistory.findByPledgeSubject(repositoryPledgeSubject.findByPledgeSubjectId(pledgeSubjectId), sortByDateConclusion);
    }
}
