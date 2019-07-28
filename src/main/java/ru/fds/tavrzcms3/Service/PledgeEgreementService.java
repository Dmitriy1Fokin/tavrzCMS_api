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

    public double getRsDz(long peId){
        PledgeEgreement pledgeEgreement = repositoryPledgeEgreement.getOne(peId);
        List<PledgeSubject> pledgeSubjects = repositoryPledgeSubject.findByPledgeEgreements(pledgeEgreement);
        double totalSum = 0;
        for (PledgeSubject ps : pledgeSubjects)
            totalSum += ps.getRsDz();

        return  totalSum;
    }

    public double getRsZz(long id){
        PledgeEgreement pledgeEgreement = repositoryPledgeEgreement.getOne(id);
        List<PledgeSubject> pledgeSubjects = repositoryPledgeSubject.findByPledgeEgreements(pledgeEgreement);
        double totalSum = 0;
        for(PledgeSubject ps : pledgeSubjects)
            totalSum += ps.getRsZz();

        return totalSum;
    }

    public double getZsDz(long id){
        PledgeEgreement pledgeEgreement = repositoryPledgeEgreement.getOne(id);
        List<PledgeSubject> pledgeSubjects = repositoryPledgeSubject.findByPledgeEgreements(pledgeEgreement);
        double totalSum = 0;
        for(PledgeSubject ps : pledgeSubjects)
            totalSum += ps.getZsDz();

        return totalSum;
    }

    public double getZsZz(long id){
        PledgeEgreement pledgeEgreement = repositoryPledgeEgreement.getOne(id);
        List<PledgeSubject> pledgeSubjects = repositoryPledgeSubject.findByPledgeEgreements(pledgeEgreement);
        double totalSum = 0;
        for(PledgeSubject ps : pledgeSubjects)
            totalSum += ps.getZsZz();

        return totalSum;
    }

    public double getSs(long id){
        PledgeEgreement pledgeEgreement = repositoryPledgeEgreement.getOne(id);
        List<PledgeSubject> pledgeSubjects = repositoryPledgeSubject.findByPledgeEgreements(pledgeEgreement);
        double totalSum = 0;
        for(PledgeSubject ps : pledgeSubjects)
            totalSum += ps.getSs();

        return totalSum;
    }

    public List<Date> getDatesOfConclusion(long id){
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

    public List<Date> getDatesOfMonitoring(long id){
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

    public List<String> getResultsOfMonitoring(long id){
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

}
