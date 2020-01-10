package ru.fds.tavrzcms3.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fds.tavrzcms3.dictionary.StatusOfMonitoring;
import ru.fds.tavrzcms3.dictionary.TypeOfMonitoring;
import ru.fds.tavrzcms3.dictionary.excelproprities.ExcelColumnNum;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.Monitoring;
import ru.fds.tavrzcms3.domain.PledgeAgreement;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.fileimport.FileImporter;
import ru.fds.tavrzcms3.fileimport.FileImporterFactory;
import ru.fds.tavrzcms3.repository.RepositoryMonitoring;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class MonitoringService {

    private final RepositoryMonitoring repositoryMonitoring;
    private final PledgeSubjectService pledgeSubjectService;
    private final PledgeAgreementService pledgeAgreementService;

    private final ExcelColumnNum excelColumnNum;

    public MonitoringService(RepositoryMonitoring repositoryMonitoring,
                             PledgeSubjectService pledgeSubjectService,
                             PledgeAgreementService pledgeAgreementService,
                             ExcelColumnNum excelColumnNum) {
        this.repositoryMonitoring = repositoryMonitoring;
        this.pledgeSubjectService = pledgeSubjectService;
        this.pledgeAgreementService = pledgeAgreementService;
        this.excelColumnNum = excelColumnNum;
    }

    public Optional<Monitoring> getMonitoringById(Long monitoringId){
        return repositoryMonitoring.findById(monitoringId);
    }

    public List<Monitoring> getMonitoringByPledgeSubject(PledgeSubject pledgeSubject){
        Sort sortByDateMonitoring = new Sort(Sort.Direction.DESC, "dateMonitoring");
        return repositoryMonitoring.findByPledgeSubject(pledgeSubject, sortByDateMonitoring);
    }

    public List<Monitoring> getMonitoringByIds(Collection<Long> ids){
        return repositoryMonitoring.findAllByMonitoringIdIn(ids);
    }

    public List<Monitoring> getNewMonitoringsFromFile(File file) throws IOException{
        FileImporter fileImporter = FileImporterFactory.getInstance(file);
        for(int i = 0; i < excelColumnNum.getStartRow(); i++){
            fileImporter.nextLine();
        }
        int countRow = excelColumnNum.getStartRow();

        List<Monitoring> monitoringList = new ArrayList<>();

        do{
            countRow += 1;

            if(Objects.isNull(fileImporter.getLong(excelColumnNum.getMonitoringNew().getPledgeSubjectId()))){
                throw new IOException("Неверный id{"
                        + fileImporter.getLong(excelColumnNum.getMonitoringNew().getPledgeSubjectId()) + ") предмета залога.");
            }
            Optional<PledgeSubject> pledgeSubject = pledgeSubjectService
                    .getPledgeSubjectById(fileImporter.getLong(excelColumnNum.getMonitoringNew().getPledgeSubjectId()));
            if(!pledgeSubject.isPresent()){
                throw new IOException("Предмет залога с таким id отсутствует ("
                        + fileImporter.getLong(excelColumnNum.getMonitoringNew().getPledgeSubjectId())
                        + "). Строка: " + countRow);
            }

            StatusOfMonitoring statusOfMonitoring;
            try {
                statusOfMonitoring = StatusOfMonitoring.valueOf(fileImporter.getString(excelColumnNum.getMonitoringNew().getStatus()));
            }catch (IllegalArgumentException ex){
                statusOfMonitoring = null;
            }

            TypeOfMonitoring typeOfMonitoring;
            try {
                typeOfMonitoring = TypeOfMonitoring.valueOf(fileImporter.getString(excelColumnNum.getMonitoringNew().getTypeOfMonitoring()));
            }catch (IllegalArgumentException ex){
                typeOfMonitoring = null;
            }

            Monitoring monitoring = Monitoring.builder()
                    .dateMonitoring(fileImporter.getLocalDate(excelColumnNum.getMonitoringNew().getDate()))
                    .statusMonitoring(statusOfMonitoring)
                    .employee(fileImporter.getString(excelColumnNum.getMonitoringNew().getEmployee()))
                    .typeOfMonitoring(typeOfMonitoring)
                    .notice(fileImporter.getString(excelColumnNum.getMonitoringNew().getNotice()))
                    .collateralValue(fileImporter.getBigDecimal(excelColumnNum.getMonitoringNew().getCollateralValue()))
                    .pledgeSubject(pledgeSubject.get())
                    .build();

            monitoringList.add(monitoring);

        }while (fileImporter.nextLine());

        return monitoringList;
    }

    public List<Monitoring> getCurrentMonitoringsFromFile(File file) throws IOException{
        FileImporter fileImporter = FileImporterFactory.getInstance(file);
        for(int i = 0; i < excelColumnNum.getStartRow(); i++){
            fileImporter.nextLine();
        }
        int countRow = excelColumnNum.getStartRow();

        List<Monitoring> monitoringList = new ArrayList<>();

        do{
            countRow += 1;

            if(Objects.isNull(fileImporter.getLong(excelColumnNum.getMonitoringUpdate().getMonitoringId()))){
                throw new IOException("Неверный id{"
                        + fileImporter.getLong(excelColumnNum.getMonitoringUpdate().getMonitoringId()) + ") мониторинга.");
            }
            Optional<Monitoring> monitoring = getMonitoringById(fileImporter
                    .getLong(excelColumnNum.getMonitoringUpdate().getMonitoringId()));
            if(!monitoring.isPresent()){
                throw new IOException("Мониторинг с таким id отсутствует ("
                        + fileImporter.getLong(excelColumnNum.getMonitoringUpdate().getMonitoringId())
                        + "). Строка: " + countRow);
            }

            if(Objects.isNull(fileImporter.getLong(excelColumnNum.getMonitoringUpdate().getPledgeSubjectId()))){
                throw new IOException("Неверный id{"
                        + fileImporter.getLong(excelColumnNum.getMonitoringUpdate().getPledgeSubjectId()) + ") предмета залога.");
            }
            Optional<PledgeSubject> pledgeSubject = pledgeSubjectService
                    .getPledgeSubjectById(fileImporter.getLong(excelColumnNum.getMonitoringUpdate().getPledgeSubjectId()));
            if(!pledgeSubject.isPresent()){
                throw new IOException("Предмет залога с таким id отсутствует ("
                        + fileImporter.getLong(excelColumnNum.getMonitoringUpdate().getPledgeSubjectId())
                        + "). Строка: " + countRow);
            }

            StatusOfMonitoring statusOfMonitoring;
            try {
                statusOfMonitoring = StatusOfMonitoring.valueOf(fileImporter.getString(excelColumnNum.getMonitoringUpdate().getStatus()));
            }catch (IllegalArgumentException ex){
                statusOfMonitoring = null;
            }

            TypeOfMonitoring typeOfMonitoring;
            try {
                typeOfMonitoring = TypeOfMonitoring.valueOf(fileImporter.getString(excelColumnNum.getMonitoringUpdate().getTypeOfMonitoring()));
            }catch (IllegalArgumentException ex){
                typeOfMonitoring = null;
            }

            monitoring.get().setDateMonitoring(fileImporter.getLocalDate(excelColumnNum.getMonitoringUpdate().getDate()));
            monitoring.get().setStatusMonitoring(statusOfMonitoring);
            monitoring.get().setEmployee(fileImporter.getString(excelColumnNum.getMonitoringUpdate().getEmployee()));
            monitoring.get().setTypeOfMonitoring(typeOfMonitoring);
            monitoring.get().setNotice(fileImporter.getString(excelColumnNum.getMonitoringUpdate().getNotice()));
            monitoring.get().setCollateralValue(fileImporter.getBigDecimal(excelColumnNum.getMonitoringUpdate().getCollateralValue()));
            monitoring.get().setPledgeSubject(pledgeSubject.get());

            monitoringList.add(monitoring.get());

        }while (fileImporter.nextLine());

        return monitoringList;
    }

    @Transactional
    public List<Monitoring> insertMonitoringInPledgeAgreement(PledgeAgreement pledgeAgreement, Monitoring monitoring){
        List<PledgeSubject> pledgeSubjectList = pledgeSubjectService.getPledgeSubjectsForPledgeAgreement(pledgeAgreement.getPledgeAgreementId());
        List<Monitoring> monitoringList = new ArrayList<>();
        for (PledgeSubject ps : pledgeSubjectList){
            monitoring.setPledgeSubject(ps);
            monitoringList.add(new Monitoring(monitoring));
        }

        monitoringList = repositoryMonitoring.saveAll(monitoringList);

        return monitoringList;
    }

    @Transactional
    public Monitoring insertMonitoringInPledgeSubject(Monitoring monitoring){
        return repositoryMonitoring.save(monitoring);
    }

    @Transactional
    public List<Monitoring> insertMonitoringsInPledgeSubject(List<Monitoring> monitoringList){
        return repositoryMonitoring.saveAll(monitoringList);
    }

    @Transactional
    public List<Monitoring> insertMonitoringInPledgor(Client pledgor, Monitoring monitoring){
        List<PledgeAgreement> pledgeAgreementList = pledgeAgreementService.getCurrentPledgeAgreementsByPledgor(pledgor);
        List<PledgeSubject> pledgeSubjectList = pledgeSubjectService.getPledgeSubjectsForPledgeAgreements(pledgeAgreementList);
        List<Monitoring> monitoringList = new ArrayList<>();
        for(PledgeSubject ps : pledgeSubjectList){
            monitoring.setPledgeSubject(ps);
            monitoringList.add(new Monitoring(monitoring));
        }

        monitoringList = repositoryMonitoring.saveAll(monitoringList);

        return monitoringList;
    }
}
