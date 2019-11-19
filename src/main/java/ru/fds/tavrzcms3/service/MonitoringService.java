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

import java.util.ArrayList;
import java.util.List;

@Service
public class MonitoringService {

    @Autowired
    RepositoryMonitoring repositoryMonitoring;
    @Autowired
    PledgeSubjectService pledgeSubjectService;
    @Autowired
    PledgeAgreementService pledgeAgreementService;


    public List<Monitoring> getMonitoringByPledgeSubject(PledgeSubject pledgeSubject){
        Sort sortByDateMonitoring = new Sort(Sort.Direction.DESC, "dateMonitoring");
        return repositoryMonitoring.findByPledgeSubject(pledgeSubject, sortByDateMonitoring);
    }

    @Transactional
    public List<Monitoring> insertMonitoringInPledgeAgreement(PledgeAgreement pledgeAgreement, Monitoring monitoring){
        List<PledgeSubject> pledgeSubjectList = pledgeSubjectService.getPledgeSubjectsForPledgeAgreement(pledgeAgreement.getPledgeAgreementId());
        List<Monitoring> monitoringList = new ArrayList<>();
        for (PledgeSubject ps : pledgeSubjectList){
            monitoring.setPledgeSubject(ps);
            monitoringList.add(new Monitoring(monitoring));
        }

        monitoringList = repositoryMonitoring.saveAll(monitoringList);

        return monitoringList;
    }

    @Transactional
    public Monitoring insertMonitoringInPledgeSubject(Monitoring monitoring){
        return repositoryMonitoring.save(monitoring);
    }

    @Transactional
    public List<Monitoring> insertMonitoringInPledgor(Client pledgor, Monitoring monitoring){
        List<PledgeAgreement> pledgeAgreementList = pledgeAgreementService.getCurrentPledgeAgreementsByPledgor(pledgor);
        List<PledgeSubject> pledgeSubjectList = pledgeSubjectService.getPledgeSubjectsForPledgeAgreements(pledgeAgreementList);
        List<Monitoring> monitoringList = new ArrayList<>();
        for(PledgeSubject ps : pledgeSubjectList){
            monitoring.setPledgeSubject(ps);
            monitoringList.add(new Monitoring(monitoring));
        }

        monitoringList = repositoryMonitoring.saveAll(monitoringList);

        return monitoringList;
    }
}
