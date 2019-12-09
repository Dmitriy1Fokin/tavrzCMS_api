package ru.fds.tavrzcms3.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fds.tavrzcms3.domain.Encumbrance;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.repository.RepositoryEncumbrance;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class EncumbranceService {

    private final RepositoryEncumbrance repositoryEncumbrance;

    public EncumbranceService(RepositoryEncumbrance repositoryEncumbrance) {
        this.repositoryEncumbrance = repositoryEncumbrance;
    }

    public List<Encumbrance> getEncumbranceByPledgeSubject(PledgeSubject pledgeSubject){
        return repositoryEncumbrance.findAllByPledgeSubject(pledgeSubject);
    }

    public List<Encumbrance> getEncumbranceByIds(Collection<Long> ids){
        return repositoryEncumbrance.findAllByEncumbranceIdIn(ids);
    }

    @Transactional
    public Encumbrance updateInsertEncumbrance(Encumbrance encumbrance){
        return repositoryEncumbrance.save(encumbrance);
    }
}
