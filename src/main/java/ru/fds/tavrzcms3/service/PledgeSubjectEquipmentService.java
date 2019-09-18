package ru.fds.tavrzcms3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fds.tavrzcms3.domain.PledgeSubjectEquipment;
import ru.fds.tavrzcms3.repository.RepositoryPledgeSubjectEquipment;

@Service
public class PledgeSubjectEquipmentService {

    @Autowired
    RepositoryPledgeSubjectEquipment repositoryPledgeSubjectEquipment;

    public PledgeSubjectEquipment getPledgeSubjectEquipment(long pledgeSubjectId){
        return repositoryPledgeSubjectEquipment.getOne(pledgeSubjectId);
    }

    @Transactional
    public PledgeSubjectEquipment updatePledgeSubjectEquipment(PledgeSubjectEquipment pledgeSubjectEquipment){
        return repositoryPledgeSubjectEquipment.save(pledgeSubjectEquipment);
    }
}
