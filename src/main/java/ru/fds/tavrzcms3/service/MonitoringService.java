package ru.fds.tavrzcms3.service;

import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.Monitoring;
import ru.fds.tavrzcms3.domain.PledgeAgreement;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface MonitoringService {
    List<Monitoring> getMonitoringByPledgeSubject(Long pledgeSubjectId);
    List<Monitoring> getNewMonitoringsFromFile(File file) throws IOException;
    List<Monitoring> getCurrentMonitoringsFromFile(File file) throws IOException;
    List<Monitoring> insertMonitoringInPledgeAgreement(PledgeAgreement pledgeAgreement, Monitoring monitoring);
    Monitoring insertMonitoringInPledgeSubject(Monitoring monitoring);
    List<Monitoring> insertMonitoringsInPledgeSubject(List<Monitoring> monitoringList);
    List<Monitoring> insertMonitoringInPledgor(Client pledgor, Monitoring monitoring);
    Monitoring updateMonitoring(Monitoring monitoring);
}
