package ru.fds.tavrzcms3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.Monitoring;
import ru.fds.tavrzcms3.domain.PledgeAgreement;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.repository.RepositoryMonitoring;
import ru.fds.tavrzcms3.repository.RepositoryPledgeAgreement;
import ru.fds.tavrzcms3.repository.RepositoryPledgeSubject;

import java.util.ArrayList;
import java.util.List;

@Service
public class MonitoringService {

    @Autowired
    RepositoryMonitoring repositoryMonitoring;
    @Autowired
    RepositoryPledgeSubject repositoryPledgeSubject;
    @Autowired
    RepositoryPledgeAgreement repositoryPledgeAgreement;


    public List<Monitoring> getMonitoringByPledgeSubjectId(long pledgeSubjectId){
        Sort sortByDateMonitoring = new Sort(Sort.Direction.DESC, "dateMonitoring");
        return repositoryMonitoring.findByPledgeSubject(repositoryPledgeSubject.findByPledgeSubjectId(pledgeSubjectId), sortByDateMonitoring);
    }

    @Transactional
    public List<Monitoring> insertMonitoringInPledgeAgreement(PledgeAgreement pledgeAgreement, Monitoring monitoring){
        List<PledgeSubject> pledgeSubjectList = repositoryPledgeSubject.findByPledgeAgreements(pledgeAgreement);
        List<Monitoring> monitoringList = new ArrayList<>();
        for (PledgeSubject ps : pledgeSubjectList){
            monitoring.setPledgeSubject(ps);
            monitoringList.add(repositoryMonitoring.save(new Monitoring(monitoring)));
        }

        return monitoringList;
    }

    @Transactional
    public Monitoring insertMonitoringInPledgeSubject(PledgeSubject pledgeSubject, Monitoring monitoring){
        monitoring.setPledgeSubject(pledgeSubject);
        return repositoryMonitoring.save(monitoring);
    }

    @Transactional
    public List<Monitoring> insertMonitoringInPledgor(Client pledgor, Monitoring monitoring){
        List<PledgeAgreement> pledgeAgreementList = repositoryPledgeAgreement.findByPledgor(pledgor);
        List<PledgeSubject> pledgeSubjectList = repositoryPledgeSubject.findByPledgeAgreementsIn(pledgeAgreementList);
        List<Monitoring> monitoringList = new ArrayList<>();
        for(PledgeSubject ps : pledgeSubjectList){
            monitoring.setPledgeSubject(ps);
            monitoringList.add(repositoryMonitoring.save(new Monitoring(monitoring)));
        }

        return monitoringList;
    }
}
