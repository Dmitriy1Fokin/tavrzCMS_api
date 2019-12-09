package ru.fds.tavrzcms3.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fds.tavrzcms3.domain.Insurance;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.repository.RepositoryInsurance;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class InsuranceService {

    private final RepositoryInsurance repositoryInsurance;
    private final PledgeSubjectService pledgeSubjectService;

    public InsuranceService(RepositoryInsurance repositoryInsurance, PledgeSubjectService pledgeSubjectService) {
        this.repositoryInsurance = repositoryInsurance;
        this.pledgeSubjectService = pledgeSubjectService;
    }

    public List<Insurance> getInsurancesByPledgeSubject(PledgeSubject pledgeSubject){
        Sort sortByDateEnd = new Sort(Sort.Direction.DESC, "dateEndInsurance");
        return repositoryInsurance.findAllByPledgeSubject(pledgeSubject, sortByDateEnd);
    }

    public List<Insurance> getInsurancesByIds(Collection<Long> ids){
        return repositoryInsurance.findAllByInsuranceIdIn(ids);
    }

    @Transactional
    public Insurance updateInsertInsurance(Insurance insurance){
        return repositoryInsurance.save(insurance);
    }
}
