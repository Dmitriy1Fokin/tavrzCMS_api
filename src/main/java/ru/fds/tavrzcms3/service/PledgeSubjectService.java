package ru.fds.tavrzcms3.service;

import ru.fds.tavrzcms3.dictionary.TypeOfCollateral;
import ru.fds.tavrzcms3.domain.CostHistory;
import ru.fds.tavrzcms3.domain.Monitoring;
import ru.fds.tavrzcms3.domain.PledgeAgreement;
import ru.fds.tavrzcms3.domain.PledgeSubject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PledgeSubjectService {
    Optional<PledgeSubject> getPledgeSubjectById(Long pledgeSubjectId);
    List<PledgeSubject> getPledgeSubjectsByPledgeAgreement(Long pledgeAgreementId);
    List<PledgeSubject> getPledgeSubjectsByPledgeAgreements(List<PledgeAgreement> pledgeAgreementList);
    List<PledgeSubject> getPledgeSubjectsFromSearch(Map<String, String> searchParam) throws ReflectiveOperationException;
    List<PledgeSubject> getPledgeSubjectByCadastralNum(String cadastralNum);
    List<PledgeSubject> getPledgeSubjectByName(String name);
    List<PledgeSubject> getNewPledgeSubjectsFromFile(File file, TypeOfCollateral typeOfCollateral) throws IOException;
    List<PledgeSubject> getCurrentPledgeSubjectsFromFile(File file) throws IOException;
    PledgeSubject updatePledgeSubject(PledgeSubject pledgeSubject, List<Long> pledgeAgreementsIds);
    List<PledgeSubject> updatePledgeSubjects(List<PledgeSubject> pledgeSubjectList,
                                             List<List<PledgeAgreement>> pledgeAgreementList);
    PledgeSubject insertPledgeSubject(PledgeSubject pledgeSubject,
                                      List<Long> pledgeAgreementsIds,
                                      CostHistory costHistory,
                                      Monitoring monitoring);
    List<PledgeSubject> insertPledgeSubjects(List<PledgeSubject> pledgeSubjectList,
                                             List<List<PledgeAgreement>> pledgeAgreementList,
                                             List<CostHistory> costHistoryList,
                                             List<Monitoring> monitoringList);
}
