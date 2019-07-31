package ru.fds.tavrzcms3.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.fds.tavrzcms3.domain.PledgeEgreement;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.repository.RepositoryPledgeEgreement;
import ru.fds.tavrzcms3.repository.RepositoryPledgeSubject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PledgeEgreementService {

    @Autowired
    RepositoryPledgeEgreement repositoryPledgeEgreement;

    @Autowired
    RepositoryPledgeSubject repositoryPledgeSubject;

    public synchronized double getRsDz(long peId){
        PledgeEgreement pledgeEgreement = repositoryPledgeEgreement.getOne(peId);
        List<PledgeSubject> pledgeSubjects = repositoryPledgeSubject.findByPledgeEgreements(pledgeEgreement);
        double totalSum = 0;
        for (PledgeSubject ps : pledgeSubjects)
            totalSum += ps.getRsDz();

        return  totalSum;
    }

    public synchronized double getRsZz(long id){
        PledgeEgreement pledgeEgreement = repositoryPledgeEgreement.getOne(id);
        List<PledgeSubject> pledgeSubjects = repositoryPledgeSubject.findByPledgeEgreements(pledgeEgreement);
        double totalSum = 0;
        for(PledgeSubject ps : pledgeSubjects)
            totalSum += ps.getRsZz();

        return totalSum;
    }

    public synchronized double getZsDz(long id){
        PledgeEgreement pledgeEgreement = repositoryPledgeEgreement.getOne(id);
        List<PledgeSubject> pledgeSubjects = repositoryPledgeSubject.findByPledgeEgreements(pledgeEgreement);
        double totalSum = 0;
        for(PledgeSubject ps : pledgeSubjects)
            totalSum += ps.getZsDz();

        return totalSum;
    }

    public synchronized double getZsZz(long id){
        PledgeEgreement pledgeEgreement = repositoryPledgeEgreement.getOne(id);
        List<PledgeSubject> pledgeSubjects = repositoryPledgeSubject.findByPledgeEgreements(pledgeEgreement);
        double totalSum = 0;
        for(PledgeSubject ps : pledgeSubjects)
            totalSum += ps.getZsZz();

        return totalSum;
    }

    public synchronized double getSs(long id){
        PledgeEgreement pledgeEgreement = repositoryPledgeEgreement.getOne(id);
        List<PledgeSubject> pledgeSubjects = repositoryPledgeSubject.findByPledgeEgreements(pledgeEgreement);
        double totalSum = 0;
        for(PledgeSubject ps : pledgeSubjects)
            totalSum += ps.getSs();

        return totalSum;
    }

    public synchronized List<Date> getDatesOfConclusion(long id){
        PledgeEgreement pledgeEgreement = repositoryPledgeEgreement.getOne(id);
        List<PledgeSubject> pledgeSubjects = repositoryPledgeSubject.findByPledgeEgreements(pledgeEgreement);
        List<Date> dates = new ArrayList<>();
        for(PledgeSubject ps : pledgeSubjects){
            Date date = ps.getDateConclusion();
            if(!dates.contains(date))
                dates.add(date);
        }

        return  dates;
    }

    public synchronized List<Date> getDatesOfMonitoring(long id){
        PledgeEgreement pledgeEgreement = repositoryPledgeEgreement.getOne(id);
        List<PledgeSubject> pledgeSubjects = repositoryPledgeSubject.findByPledgeEgreements(pledgeEgreement);
        List<Date> dates = new ArrayList<>();
        for(PledgeSubject ps : pledgeSubjects){
            Date date = ps.getDateMonitoring();
            if(!dates.contains(date))
                dates.add(date);
        }

        return  dates;
    }

    public synchronized List<String> getResultsOfMonitoring(long id){
        PledgeEgreement pledgeEgreement = repositoryPledgeEgreement.getOne(id);
        List<PledgeSubject> pledgeSubjects = repositoryPledgeSubject.findByPledgeEgreements(pledgeEgreement);
        List<String> results = new ArrayList<>();
        for(PledgeSubject ps : pledgeSubjects){
            String resultOfMonitoring = ps.getStatusMonitoring();
            if(!results.contains(resultOfMonitoring))
                results.add(resultOfMonitoring);
        }

        return  results;
    }

    public synchronized PledgeEgreement getPledgeEgreementById(long id){
        PledgeEgreement pledgeEgreement = repositoryPledgeEgreement.getOne(id);
        return pledgeEgreement;
    }

}
