package ru.fds.tavrzcms3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fds.tavrzcms3.domain.PledgeSubjectRealtyRoom;
import ru.fds.tavrzcms3.repository.RepositoryPledgeSubjectRealtyRoom;

@Service
public class PledgeSubjectRealtyRoomService {

    @Autowired
    RepositoryPledgeSubjectRealtyRoom repositoryPledgeSubjectRealtyRoom;

    public PledgeSubjectRealtyRoom getPledgeSubjectRealtyRoom(long pledgeSubjectId){
        return repositoryPledgeSubjectRealtyRoom.getOne(pledgeSubjectId);
    }

    @Transactional
    public PledgeSubjectRealtyRoom updatePledgeSubjectRealtyRoom(PledgeSubjectRealtyRoom pledgeSubjectRealtyRoom){
        return repositoryPledgeSubjectRealtyRoom.save(pledgeSubjectRealtyRoom);
    }
}
