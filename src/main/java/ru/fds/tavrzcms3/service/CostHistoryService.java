package ru.fds.tavrzcms3.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fds.tavrzcms3.domain.CostHistory;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.repository.RepositoryCostHistory;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CostHistoryService {

    private final RepositoryCostHistory repositoryCostHistory;
    private final PledgeSubjectService pledgeSubjectService;

    public CostHistoryService(RepositoryCostHistory repositoryCostHistory, PledgeSubjectService pledgeSubjectService) {
        this.repositoryCostHistory = repositoryCostHistory;
        this.pledgeSubjectService = pledgeSubjectService;
    }


    public List<CostHistory> getCostHistoryPledgeSubject(PledgeSubject pledgeSubject){
        Sort sortByDateConclusion = new Sort(Sort.Direction.DESC, "dateConclusion");
        return repositoryCostHistory.findByPledgeSubject(pledgeSubject, sortByDateConclusion);
    }

    public List<CostHistory> getCostHistorybyIds(Collection<Long> ids){
        return repositoryCostHistory.findAllByCostHistoryIdIn(ids);
    }

    @Transactional
    public CostHistory insertCostHistory(CostHistory costHistory){
        return repositoryCostHistory.save(costHistory);
    }
}
