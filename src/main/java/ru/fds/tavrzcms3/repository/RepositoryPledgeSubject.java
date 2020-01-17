package ru.fds.tavrzcms3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.fds.tavrzcms3.domain.PledgeAgreement;
import ru.fds.tavrzcms3.domain.PledgeSubject;

import java.util.List;

public interface RepositoryPledgeSubject extends JpaRepository<PledgeSubject, Long>, JpaSpecificationExecutor<PledgeSubject> {
    List<PledgeSubject> findAllByPledgeSubjectIdIn(List<Long> ids);
    List<PledgeSubject> findByPledgeAgreementsIn(List<PledgeAgreement> pledgeAgreements);
    List<PledgeSubject> findByPledgeAgreements(PledgeAgreement pledgeAgreements);
    List<PledgeSubject> findAllByNameContainingIgnoreCase(String name);
    List<PledgeSubject> findByPledgeSubjectBuildingCadastralNumContainingIgnoreCase(String cadastralNum);
    List<PledgeSubject> findByPledgeSubjectLandLeaseCadastralNumContainingIgnoreCase(String cadastralNum);
    List<PledgeSubject> findByPledgeSubjectLandOwnershipCadastralNumContainingIgnoreCase(String cadastralNum);
    List<PledgeSubject> findByPledgeSubjectRoomCadastralNumContainingIgnoreCase(String cadastralNum);
}
