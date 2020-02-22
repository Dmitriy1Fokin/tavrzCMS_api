package ru.fds.tavrzcms3.service;

import ru.fds.tavrzcms3.domain.Insurance;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface InsuranceService {
    Optional<Insurance> getInsuranceById(Long insuranceId);
    List<Insurance> getInsurancesByPledgeSubject(Long pledgeSubjectId);
    List<Insurance> getNewInsurancesFromFile(File file) throws IOException;
    List<Insurance> getCurrentInsurancesFromFile(File file) throws IOException;
    Insurance updateInsertInsurance(Insurance insurance);
    List<Insurance> updateInsertInsurances(List<Insurance> insuranceList);
}
