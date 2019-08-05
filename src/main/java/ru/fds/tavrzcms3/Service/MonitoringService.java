package ru.fds.tavrzcms3.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.fds.tavrzcms3.domain.Monitoring;
import ru.fds.tavrzcms3.repository.RepositoryMonitoring;
import ru.fds.tavrzcms3.repository.RepositoryPledgeSubject;

import java.util.List;

@Service
public class MonitoringService {

    @Autowired
    RepositoryMonitoring repositoryMonitoring;

    @Autowired
    RepositoryPledgeSubject repositoryPledgeSubject;

    public synchronized List<Monitoring> getMonitoringByPledgeSubjectId(long pledgeSubjectId){
        Sort sortByDateMonitoring = new Sort(Sort.Direction.DESC, "dateMonitoring");
        return repositoryMonitoring.findByPledgeSubject(repositoryPledgeSubject.findByPledgeSubjectId(pledgeSubjectId), sortByDateMonitoring);
    }
}
