package ru.fds.tavrzcms3.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
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
}
