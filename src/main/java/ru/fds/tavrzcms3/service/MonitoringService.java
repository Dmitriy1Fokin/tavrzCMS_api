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
import ru.fds.tavrzcms3.validate.ValidatorEntity;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class MonitoringService {


    private final RepositoryMonitoring repositoryMonitoring;
    private final PledgeSubjectService pledgeSubjectService;
    private final PledgeAgreementService pledgeAgreementService;
    private final ValidatorEntity validatorEntity;
    private final ExcelColumnNum excelColumnNum;

    private static final String MSG_WRONG_ID = "Неверный id{";
    private static final String MSG_LINE = "). Строка: ";

    public MonitoringService(RepositoryMonitoring repositoryMonitoring,
                             PledgeSubjectService pledgeSubjectService,
                             PledgeAgreementService pledgeAgreementService,
                             ValidatorEntity validatorEntity,
                             ExcelColumnNum excelColumnNum) {
        this.repositoryMonitoring = repositoryMonitoring;
        this.pledgeSubjectService = pledgeSubjectService;
        this.pledgeAgreementService = pledgeAgreementService;
        this.validatorEntity = validatorEntity;
        this.excelColumnNum = excelColumnNum;
    }

    private Optional<Monitoring> getMonitoringById(Long monitoringId){
        return repositoryMonitoring.findById(monitoringId);
    }

    public List<Monitoring> getMonitoringByPledgeSubject(Long pledgeSubjectId){
        Sort sortByDateMonitoring = new Sort(Sort.Direction.DESC, "dateMonitoring");
        return repositoryMonitoring.findByPledgeSubject(pledgeSubjectId, sortByDateMonitoring);
    }

    @Transactional
    public List<Monitoring> getNewMonitoringsFromFile(File file) throws IOException{
        FileImporter fileImporter = FileImporterFactory.getInstance(file);
        for(int i = 0; i < excelColumnNum.getStartRow(); i++){
            fileImporter.nextLine();
        }
        int countRow = excelColumnNum.getStartRow();

        List<Monitoring> monitoringList = new ArrayList<>();

        do{
            countRow += 1;

            Monitoring monitoring = Monitoring.builder()
                    .dateMonitoring(fileImporter.getLocalDate(excelColumnNum.getMonitoringNew().getDate()))
                    .statusMonitoring(StatusOfMonitoring.valueOfString(fileImporter
                            .getString(excelColumnNum.getMonitoringNew().getStatus())).orElse(null))
                    .employee(fileImporter.getString(excelColumnNum.getMonitoringNew().getEmployee()))
                    .typeOfMonitoring(TypeOfMonitoring.valueOfString(fileImporter
                            .getString(excelColumnNum.getMonitoringNew().getTypeOfMonitoring())).orElse(null))
                    .notice(fileImporter.getString(excelColumnNum.getMonitoringNew().getNotice()))
                    .collateralValue(fileImporter.getBigDecimal(excelColumnNum.getMonitoringNew().getCollateralValue()))
                    .pledgeSubject(setPledgeSubjectInNewMonitoring(fileImporter, countRow))
                    .build();

            Set<ConstraintViolation<Monitoring>> violations =  validatorEntity.validateEntity(monitoring);
            if(!violations.isEmpty())
                throw new ConstraintViolationException("object " + countRow, violations);

            monitoringList.add(monitoring);

        }while (fileImporter.nextLine());

        monitoringList = insertMonitoringsInPledgeSubject(monitoringList);

        return monitoringList;
    }

    private PledgeSubject setPledgeSubjectInNewMonitoring(FileImporter fileImporter, int countRow) throws IOException {
        if(Objects.isNull(fileImporter.getLong(excelColumnNum.getMonitoringNew().getPledgeSubjectId()))){
            throw new IOException(MSG_WRONG_ID
                    + fileImporter.getLong(excelColumnNum.getMonitoringNew().getPledgeSubjectId())
                    + ") предмета залога. Строка: " + countRow);
        }

        return pledgeSubjectService
                .getPledgeSubjectById(fileImporter.getLong(excelColumnNum.getMonitoringNew().getPledgeSubjectId()))
                .orElseThrow(() -> new IOException("Предмет залога с таким id отсутствует ("
                        + fileImporter.getLong(excelColumnNum.getMonitoringNew().getPledgeSubjectId())
                        + MSG_LINE + countRow));
    }

    @Transactional
    public List<Monitoring> getCurrentMonitoringsFromFile(File file) throws IOException{
        FileImporter fileImporter = FileImporterFactory.getInstance(file);
        for(int i = 0; i < excelColumnNum.getStartRow(); i++){
            fileImporter.nextLine();
        }
        int countRow = excelColumnNum.getStartRow();

        List<Monitoring> monitoringList = new ArrayList<>();

        do{
            countRow += 1;

            Monitoring monitoring = setCurrentMonitoring(fileImporter, countRow);

            monitoring.setDateMonitoring(fileImporter.getLocalDate(excelColumnNum.getMonitoringUpdate().getDate()));
            monitoring.setStatusMonitoring(StatusOfMonitoring.valueOfString(fileImporter
                    .getString(excelColumnNum.getMonitoringUpdate().getStatus())).orElse(null));
            monitoring.setEmployee(fileImporter.getString(excelColumnNum.getMonitoringUpdate().getEmployee()));
            monitoring.setTypeOfMonitoring(TypeOfMonitoring.valueOfString(fileImporter
                    .getString(excelColumnNum.getMonitoringUpdate().getTypeOfMonitoring())).orElse(null));
            monitoring.setNotice(fileImporter.getString(excelColumnNum.getMonitoringUpdate().getNotice()));
            monitoring.setCollateralValue(fileImporter.getBigDecimal(excelColumnNum.getMonitoringUpdate().getCollateralValue()));
            monitoring.setPledgeSubject(setPledgeSubjectInCurrentMonitoring(fileImporter, countRow));

            Set<ConstraintViolation<Monitoring>> violations =  validatorEntity.validateEntity(monitoring);
            if(!violations.isEmpty())
                throw new ConstraintViolationException("object " + countRow, violations);

            monitoringList.add(monitoring);

        }while (fileImporter.nextLine());

        monitoringList = insertMonitoringsInPledgeSubject(monitoringList);

        return monitoringList;
    }

    private Monitoring setCurrentMonitoring(FileImporter fileImporter, int countRow) throws IOException {
        if(Objects.isNull(fileImporter.getLong(excelColumnNum.getMonitoringUpdate().getMonitoringId()))){
            throw new IOException(MSG_WRONG_ID
                    + fileImporter.getLong(excelColumnNum.getMonitoringUpdate().getMonitoringId())
                    + ") мониторинга. Строка: " + countRow);
        }

        return getMonitoringById(fileImporter.getLong(excelColumnNum.getMonitoringUpdate().getMonitoringId()))
                .orElseThrow(() -> new IOException("Мониторинг с таким id отсутствует ("
                        + fileImporter.getLong(excelColumnNum.getMonitoringUpdate().getMonitoringId())
                        + MSG_LINE + countRow));
    }

    private PledgeSubject setPledgeSubjectInCurrentMonitoring(FileImporter fileImporter, int countRow) throws IOException {
        if(Objects.isNull(fileImporter.getLong(excelColumnNum.getMonitoringUpdate().getPledgeSubjectId()))){
            throw new IOException(MSG_WRONG_ID
                    + fileImporter.getLong(excelColumnNum.getMonitoringUpdate().getPledgeSubjectId())
                    + ") предмета залога. Строка: " + countRow);
        }

        return pledgeSubjectService.getPledgeSubjectById(fileImporter.getLong(excelColumnNum.getMonitoringUpdate().getPledgeSubjectId()))
                .orElseThrow(() -> new IOException("Предмет залога с таким id отсутствует ("
                        + fileImporter.getLong(excelColumnNum.getMonitoringUpdate().getPledgeSubjectId())
                        + MSG_LINE + countRow));
    }

    @Transactional
    public List<Monitoring> insertMonitoringInPledgeAgreement(PledgeAgreement pledgeAgreement, Monitoring monitoring){
        List<PledgeSubject> pledgeSubjectList = pledgeSubjectService.getPledgeSubjectsByPledgeAgreement(pledgeAgreement.getPledgeAgreementId());
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
        List<PledgeAgreement> pledgeAgreementList = pledgeAgreementService.getCurrentPledgeAgreementsByPledgor(pledgor.getClientId());
        List<PledgeSubject> pledgeSubjectList = pledgeSubjectService.getPledgeSubjectsByPledgeAgreements(pledgeAgreementList);
        List<Monitoring> monitoringList = new ArrayList<>();
        for(PledgeSubject ps : pledgeSubjectList){
            monitoring.setPledgeSubject(ps);
            monitoringList.add(new Monitoring(monitoring));
        }

        monitoringList = repositoryMonitoring.saveAll(monitoringList);

        return monitoringList;
    }
}
