package ru.fds.tavrzcms3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fds.tavrzcms3.domain.Insurance;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.repository.RepositoryInsurance;

import java.util.List;

@Service
public class InsuranceService {

    @Autowired
    RepositoryInsurance repositoryInsurance;

    public List<Insurance> getInsurancesByPledgeSubject(PledgeSubject pledgeSubject){
        Sort sortByDateEnd = new Sort(Sort.Direction.DESC, "dateEndInsurance");
        List<Insurance> insuranceList = repositoryInsurance.findAllByPledgeSubject(pledgeSubject, sortByDateEnd);
        return insuranceList;
    }

    @Transactional
    public Insurance insertInsuranceInPledgeSubject(PledgeSubject pledgeSubject, Insurance insurance){
        insurance.setPledgeSubject(pledgeSubject);
        return repositoryInsurance.save(insurance);
    }
}
