package ru.fds.tavrzcms3.service;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fds.tavrzcms3.dictionary.Operations;
import ru.fds.tavrzcms3.dictionary.StatusOfAgreement;
import ru.fds.tavrzcms3.dictionary.TypeOfPledgeAgreement;
import ru.fds.tavrzcms3.dictionary.excelproprities.ExcelColumnNum;
import ru.fds.tavrzcms3.domain.*;
import ru.fds.tavrzcms3.fileimport.FileImporter;
import ru.fds.tavrzcms3.fileimport.FileImporterFactory;
import ru.fds.tavrzcms3.repository.*;
import ru.fds.tavrzcms3.specification.SearchCriteria;
import ru.fds.tavrzcms3.specification.SpecificationBuilder;
import ru.fds.tavrzcms3.specification.impl.SpecificationBuilderImpl;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class PledgeAgreementService {

    private final RepositoryPledgeAgreement repositoryPledgeAgreement;
    private final RepositoryLoanAgreement repositoryLoanAgreement;
    private final RepositoryPledgeSubject repositoryPledgeSubject;
    private final ClientService clientService;

    private final ExcelColumnNum excelColumnNum;

    public PledgeAgreementService(RepositoryPledgeAgreement repositoryPledgeAgreement,
                                  RepositoryLoanAgreement repositoryLoanAgreement,
                                  RepositoryPledgeSubject repositoryPledgeSubject,
                                  ClientService clientService,
                                  ExcelColumnNum excelColumnNum) {
        this.repositoryPledgeAgreement = repositoryPledgeAgreement;
        this.repositoryLoanAgreement = repositoryLoanAgreement;
        this.repositoryPledgeSubject = repositoryPledgeSubject;
        this.clientService = clientService;
        this.excelColumnNum = excelColumnNum;
    }

    public Optional<PledgeAgreement> getPledgeAgreementById(long pledgeAgreementId){
        return repositoryPledgeAgreement.findById(pledgeAgreementId);
    }

    public List<PledgeAgreement> getPledgeAgreements(){
        return repositoryPledgeAgreement.findAll();
    }

    public List<PledgeAgreement> getPledgeAgreementsByIds(List<Long> ids){
        return repositoryPledgeAgreement.findAllByPledgeAgreementIdIn(ids);
    }

    public List<PledgeAgreement> getAllCurrentPledgeAgreements(){
        return repositoryPledgeAgreement.findAllByStatusPAEquals(StatusOfAgreement.OPEN);
    }

    public List<PledgeAgreement> getAllCurrentPledgeAgreements(TypeOfPledgeAgreement typeOfPledgeAgreement){
        return repositoryPledgeAgreement.findAllByStatusPAEqualsAndPervPoslEquals(StatusOfAgreement.OPEN, typeOfPledgeAgreement);
    }

    public int countOfAllCurrentPledgeAgreements(){
        return repositoryPledgeAgreement.countAllByStatusPAEquals(StatusOfAgreement.OPEN);
    }

    public int countOfAllCurrentPledgeAgreements(TypeOfPledgeAgreement typeOfPledgeAgreement){
        return repositoryPledgeAgreement.countAllByStatusPAEqualsAndPervPoslEquals(StatusOfAgreement.OPEN, typeOfPledgeAgreement);
    }

    public List<PledgeAgreement> getPledgeAgreementsByNumPA(String numPA){
        return repositoryPledgeAgreement.findAllByNumPAContainingIgnoreCase(numPA);
    }

    public List<LocalDate> getDatesOfConclusion(PledgeAgreement pledgeAgreement){
        List<Date> dateList = repositoryPledgeAgreement.getDatesOfConclusion(pledgeAgreement.getPledgeAgreementId());

        if(Objects.isNull(dateList) || dateList.isEmpty())
            return Collections.emptyList();

        List<LocalDate> localDateList = new ArrayList<>();
        for(Date date : dateList)
            localDateList.add(date.toLocalDate());

        return localDateList;
    }

    public List<LocalDate> getDatesOfMonitoring(PledgeAgreement pledgeAgreement){
        List<Date> dateList = repositoryPledgeAgreement.getDatesOMonitorings(pledgeAgreement.getPledgeAgreementId());

        if(Objects.isNull(dateList) || dateList.isEmpty())
            return Collections.emptyList();

        List<LocalDate> localDateList = new ArrayList<>();
        for(Date date : dateList)
            localDateList.add(date.toLocalDate());

        return localDateList;
    }

    public List<String> getResultsOfMonitoring(PledgeAgreement pledgeAgreement){
        return repositoryPledgeAgreement.getResultsOfMonitoring(pledgeAgreement.getPledgeAgreementId());
    }

    public List<String> getTypeOfCollateral(PledgeAgreement pledgeAgreement){
        return  repositoryPledgeAgreement.getTypeOfCollateral(pledgeAgreement.getPledgeAgreementId());
    }

    public List<PledgeAgreement> getAllPledgeAgreementByPLedgeSubject(Long pledgeSubjectId){
        Optional<PledgeSubject> pledgeSubject = repositoryPledgeSubject.findById(pledgeSubjectId);
        if(pledgeSubject.isPresent())
            return repositoryPledgeAgreement.findAllByPledgeSubjects(pledgeSubject.get());
        else
            return Collections.emptyList();
    }

    public List<PledgeAgreement> getCurrentPledgeAgreementsByEmployee(long employeeId, TypeOfPledgeAgreement pervPosl){
        return repositoryPledgeAgreement.getCurrentPledgeAgreementsForEmployee(employeeId, pervPosl.getTranslate());
    }

    public List<PledgeAgreement> getCurrentPledgeAgreementsByEmployee(long employeeId){
        return repositoryPledgeAgreement.getCurrentPledgeAgreementsForEmployee(employeeId);
    }

    public int countOfCurrentPledgeAgreementForEmployee(Employee employee){
        List<Client> pledgors = clientService.getClientByEmployee(employee);

        return repositoryPledgeAgreement.countAllByClientInAndStatusPAEquals(pledgors, StatusOfAgreement.OPEN);
    }

    public int countOfCurrentPledgeAgreementForEmployee(Employee employee, TypeOfPledgeAgreement pervPosl){
        List<Client> pledgors = clientService.getClientByEmployee(employee);

        return repositoryPledgeAgreement.countAllByClientInAndPervPoslEqualsAndStatusPAEquals(pledgors, pervPosl, StatusOfAgreement.OPEN);
    }

    public int countOfMonitoringNotDone(Employee employee){
        LocalDate now = LocalDate.now();
        LocalDate firstDate = LocalDate.of(now.getYear()-1, now.getMonth(), 1);
        LocalDate secondDate = firstDate.plusMonths(1);

        return repositoryPledgeAgreement.countOfMonitoringBetweenDates(employee.getEmployeeId(), firstDate, secondDate);
    }

    public int countOfMonitoringIsDone(Employee employee){
        LocalDate now = LocalDate.now();
        LocalDate firstDate = LocalDate.of(now.getYear(), now.getMonth(), 1);
        LocalDate secondDate = firstDate.plusMonths(1);

        return repositoryPledgeAgreement.countOfMonitoringBetweenDates(employee.getEmployeeId(), firstDate, secondDate);
    }

    public int countOfMonitoringOverdue(Employee employee){
        LocalDate now = LocalDate.now();
        LocalDate firstDate = LocalDate.of(now.getYear()-1, now.getMonth(), 1);

        return repositoryPledgeAgreement.countOfMonitoringLessDate(employee.getEmployeeId(), firstDate);
    }

    public List<PledgeAgreement> getPledgeAgreementWithMonitoringNotDone(Long employeeId){
        LocalDate now = LocalDate.now();
        LocalDate firstDate = LocalDate.of(now.getYear()-1, now.getMonthValue(), 1);
        LocalDate secondDate = firstDate.plusMonths(1);

        return repositoryPledgeAgreement.getPledgeAgreementWithMonitoringBetweenDates(employeeId, firstDate, secondDate);
    }

    public List<PledgeAgreement> getPledgeAgreementWithMonitoringIsDone(Long employeeId){
        LocalDate now = LocalDate.now();
        LocalDate firstDate = LocalDate.of(now.getYear(), now.getMonthValue(), 1);
        LocalDate secondDate = firstDate.plusMonths(1);

        return repositoryPledgeAgreement.getPledgeAgreementWithMonitoringBetweenDates(employeeId, firstDate, secondDate);
    }

    public List<PledgeAgreement> getPledgeAgreementWithMonitoringOverDue(Long employeeId){
        LocalDate now = LocalDate.now();
        LocalDate firstDate = LocalDate.of(now.getYear()-1, now.getMonth(), 1);

        return repositoryPledgeAgreement.getPledgeAgreementWithMonitoringLessDate(employeeId, firstDate);
    }

    public int countOfConclusionNotDone(Employee employee){
        LocalDate now = LocalDate.now();
        LocalDate firstDate = LocalDate.of(now.getYear()-1, now.getMonthValue(), 1);
        LocalDate secondDate = firstDate.plusMonths(1);

        return repositoryPledgeAgreement.countOfConclusionsBetweenDates(employee.getEmployeeId(), firstDate, secondDate);
    }

    public int countOfConclusionIsDone(Employee employee){
        LocalDate now = LocalDate.now();
        LocalDate firstDate = LocalDate.of(now.getYear(), now.getMonth(), 1);
        LocalDate secondDate = firstDate.plusMonths(1);

        return repositoryPledgeAgreement.countOfConclusionsBetweenDates(employee.getEmployeeId(), firstDate, secondDate);
    }

    public int countOfConclusionOverdue(Employee employee){
        LocalDate now = LocalDate.now();
        LocalDate firstDate = LocalDate.of(now.getYear()-1, now.getMonth(), 1);

        return repositoryPledgeAgreement.countOfConclusionsLessDate(employee.getEmployeeId(), firstDate);
    }

    public List<PledgeAgreement> getPledgeAgreementWithConclusionNotDone(Long employeeId){
        LocalDate now = LocalDate.now();
        LocalDate firstDate = LocalDate.of(now.getYear()-1, now.getMonthValue(), 1);
        LocalDate secondDate = firstDate.plusMonths(1);

        return repositoryPledgeAgreement.getPledgeAgreementWithConclusionsBetweenDates(employeeId, firstDate, secondDate);
    }

    public List<PledgeAgreement> getPledgeAgreementWithConclusionIsDone(Long employeeId){
        LocalDate now = LocalDate.now();
        LocalDate firstDate = LocalDate.of(now.getYear(), now.getMonth(), 1);
        LocalDate secondDate = firstDate.plusMonths(1);

        return repositoryPledgeAgreement.getPledgeAgreementWithConclusionsBetweenDates(employeeId, firstDate, secondDate);
    }

    public List<PledgeAgreement> getPledgeAgreementWithConclusionOverdue(Long employeeId){
        LocalDate now = LocalDate.now();
        LocalDate firstDate = LocalDate.of(now.getYear()-1, now.getMonth(), 1);

        return repositoryPledgeAgreement.getPledgeAgreementWithConclusionsLessDate(employeeId, firstDate);
    }

    public List<PledgeAgreement> getPledgeAgreementFromSearch(Map<String, String> searchParam) throws ReflectiveOperationException{

        SpecificationBuilder builder = new SpecificationBuilderImpl();

        for(Field field : PledgeAgreement.class.getDeclaredFields()){
            if(searchParam.containsKey(field.getName())){
                if((field.getType() == String.class || field.getType() == BigDecimal.class) && !searchParam.get(field.getName()).isEmpty()){
                    SearchCriteria searchCriteria = SearchCriteria.builder()
                            .key(field.getName())
                            .value(searchParam.get(field.getName()))
                            .operation(Operations.valueOf(searchParam.get(field.getName() + "Option")))
                            .predicate(false)
                            .build();
                    builder.with(searchCriteria);
                }else if(field.getType().getSuperclass() == Enum.class && !searchParam.get(field.getName()).isEmpty()){
                    Method method = field.getType().getMethod("valueOf", String.class);
                    Class enumClass = field.getType();
                    SearchCriteria searchCriteria = SearchCriteria.builder()
                            .key(field.getName())
                            .value(method.invoke(enumClass, searchParam.get(field.getName())))
                            .operation(Operations.EQUAL_IGNORE_CASE)
                            .predicate(false)
                            .build();
                    builder.with(searchCriteria);
                }else if(field.getType() == Client.class && !searchParam.get(field.getName()).isEmpty()){
                    Map<String, String> searchParamClient = new HashMap<>();
                    searchParamClient.put("typeOfClient", searchParam.get("typeOfClient"));
                    searchParamClient.put("clientName", searchParam.get(field.getName()));
                    List<Client> clientList = clientService.getClientFromSearch(searchParamClient);
                    if(clientList.isEmpty()){
                        SearchCriteria searchCriteria = SearchCriteria.builder()
                                .key(field.getName())
                                .value(null)
                                .operation(Operations.EQUAL_IGNORE_CASE)
                                .predicate(false)
                                .build();
                        builder.with(searchCriteria);
                    }else if(clientList.size() == 1){
                        SearchCriteria searchCriteria = SearchCriteria.builder()
                                .key(field.getName())
                                .value(clientList.get(0))
                                .operation(Operations.EQUAL_IGNORE_CASE)
                                .predicate(false)
                                .build();
                        builder.with(searchCriteria);

                    }else {
                        SearchCriteria searchCriteriaFirst = SearchCriteria.builder()
                                .key(field.getName())
                                .value(clientList.get(0))
                                .operation(Operations.EQUAL_IGNORE_CASE)
                                .predicate(false)
                                .build();
                        builder.with(searchCriteriaFirst);

                        for(int i = 1; i < clientList.size(); i++){
                            SearchCriteria searchCriteria = SearchCriteria.builder()
                                    .key(field.getName())
                                    .value(clientList.get(i))
                                    .operation(Operations.EQUAL_IGNORE_CASE)
                                    .predicate(true)
                                    .build();
                            builder.with(searchCriteria);
                        }
                    }

                }else if(field.getType() == LocalDate.class && !searchParam.get(field.getName()).isEmpty()){

                        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyy-MM-dd");
                        LocalDate localDate = LocalDate.parse(searchParam.get(field.getName()), dateTimeFormatter);

                        SearchCriteria searchCriteria = SearchCriteria.builder()
                                .key(field.getName())
                                .value(localDate)
                                .operation(Operations.valueOf(searchParam.get(field.getName() + "Option")))
                                .predicate(false)
                                .build();
                        builder.with(searchCriteria);
                }
            }
        }

        Specification specification = builder.build();
        return repositoryPledgeAgreement.findAll(specification);
    }

    public List<PledgeAgreement> getCurrentPledgeAgreementsByPledgor(Long clientId){
        return  repositoryPledgeAgreement.getPledgeAgreementsByClient(clientId, StatusOfAgreement.OPEN.getTranslate());
    }

    public List<PledgeAgreement> getClosedPledgeAgreementsByPledgor(Long clientId){
        return  repositoryPledgeAgreement.getPledgeAgreementsByClient(clientId, StatusOfAgreement.CLOSED.getTranslate());
    }

    public List<PledgeAgreement> getCurrentPledgeAgreementsByLoanAgreement(Long loanAgreementId){
        Optional<LoanAgreement> loanAgreement = repositoryLoanAgreement.findById(loanAgreementId);
        if(loanAgreement.isPresent())
            return repositoryPledgeAgreement.findByLoanAgreementsAndStatusPAEquals(loanAgreement.get(), StatusOfAgreement.OPEN);
        else
            return Collections.emptyList();
    }

    public List<PledgeAgreement> getClosedPledgeAgreementsByLoanAgreement(Long loanAgreementId){
        Optional<LoanAgreement> loanAgreement = repositoryLoanAgreement.findById(loanAgreementId);
        if(loanAgreement.isPresent())
            return repositoryPledgeAgreement.findByLoanAgreementsAndStatusPAEquals(loanAgreement.get(),StatusOfAgreement.CLOSED);
        else
            return Collections.emptyList();
    }

    public List<PledgeAgreement> getAllPledgeAgreementsByLoanAgreement(LoanAgreement loanAgreement){
        return repositoryPledgeAgreement.findByLoanAgreements(loanAgreement);
    }

    public List<PledgeAgreement> getAllPledgeAgreementsByPledgor(Client client){
        return repositoryPledgeAgreement.findAllByClient(client);
    }

    public List<PledgeAgreement> getNewPledgeAgreementsFromFile(File file) throws IOException {
        FileImporter fileImporter = FileImporterFactory.getInstance(file);
        for(int i = 0; i < excelColumnNum.getStartRow(); i++){
            fileImporter.nextLine();
        }
        int countRow = excelColumnNum.getStartRow();

        List<PledgeAgreement> pledgeAgreementList = new ArrayList<>();

        do {
            countRow += 1;

            if(Objects.isNull(fileImporter.getLong(excelColumnNum.getPledgeAgreementNew().getClientId()))){
                throw new IOException("Неверный id{"
                        + fileImporter.getLong(excelColumnNum.getPledgeAgreementNew().getClientId()) + ") клиента. Строка: " + countRow);
            }
            Optional<Client> client = clientService.getClientById(fileImporter
                    .getLong(excelColumnNum.getPledgeAgreementNew().getClientId()));
            if(!client.isPresent()){
                throw new IOException("Клиента с таким id отсутствует ("
                        + fileImporter.getLong(excelColumnNum.getPledgeAgreementNew().getClientId())
                        + "). Строка: " + countRow);
            }

            if(fileImporter.getLongList(excelColumnNum.getPledgeAgreementNew().getLoanAgreementsIds(), excelColumnNum.getDelimiter()).isEmpty()){
                throw new IOException("Неверный id{"
                        + fileImporter.getLongList(excelColumnNum.getPledgeAgreementNew().getLoanAgreementsIds(), excelColumnNum.getDelimiter())
                        + ") кредитного договора.");
            }
            List<LoanAgreement> loanAgreementList = repositoryLoanAgreement.findAllByLoanAgreementIdIn(fileImporter
                    .getLongList(excelColumnNum.getPledgeAgreementNew().getLoanAgreementsIds(), excelColumnNum.getDelimiter()));
            if(loanAgreementList.isEmpty()){
                throw new IOException("Кредитного договора с таким id отсутствует ("
                        + fileImporter.getLongList(excelColumnNum.getPledgeAgreementNew().getLoanAgreementsIds(), excelColumnNum.getDelimiter())
                        + "). Строка: " + countRow);
            }

            TypeOfPledgeAgreement typeOfPledgeAgreement;
            try {
                typeOfPledgeAgreement = TypeOfPledgeAgreement.valueOf(fileImporter.getString(excelColumnNum.getPledgeAgreementNew().getPervPosl()));
            }catch (IllegalArgumentException ex){
                typeOfPledgeAgreement = null;
            }

            StatusOfAgreement statusOfAgreement;
            try {
                statusOfAgreement = StatusOfAgreement.valueOf(fileImporter.getString(excelColumnNum.getPledgeAgreementNew().getStatus()));
            }catch (IllegalArgumentException ex){
                statusOfAgreement = null;
            }

            PledgeAgreement pledgeAgreement = PledgeAgreement.builder()
                    .numPA(fileImporter.getString(excelColumnNum.getPledgeAgreementNew().getNumPA()))
                    .dateBeginPA(fileImporter.getLocalDate(excelColumnNum.getPledgeAgreementNew().getDateBegin()))
                    .dateEndPA(fileImporter.getLocalDate(excelColumnNum.getPledgeAgreementNew().getDateEnd()))
                    .pervPosl(typeOfPledgeAgreement)
                    .statusPA(statusOfAgreement)
                    .noticePA(fileImporter.getString(excelColumnNum.getPledgeAgreementNew().getNotice()))
                    .loanAgreements(loanAgreementList)
                    .client(client.get())
                    .build();

            pledgeAgreementList.add(pledgeAgreement);
        }while (fileImporter.nextLine());

        return pledgeAgreementList;
    }

    public List<PledgeAgreement> getCurrentPledgeAgreementsFromFile(File file) throws IOException {
        FileImporter fileImporter = FileImporterFactory.getInstance(file);
        for(int i = 0; i < excelColumnNum.getStartRow(); i++){
            fileImporter.nextLine();
        }
        int countRow = excelColumnNum.getStartRow();

        List<PledgeAgreement> pledgeAgreementList = new ArrayList<>();

        do {
            countRow += 1;

            if(Objects.isNull(fileImporter.getLong(excelColumnNum.getPledgeAgreementUpdate().getPledgeAgreementId()))){
                throw new IOException("Неверный id{"
                        + fileImporter.getLong(excelColumnNum.getPledgeAgreementUpdate().getPledgeAgreementId()) + ") договора залога. Строка: " + countRow);
            }
            Optional<PledgeAgreement> pledgeAgreement = getPledgeAgreementById(fileImporter
                    .getLong(excelColumnNum.getPledgeAgreementUpdate().getPledgeAgreementId()));
            if(!pledgeAgreement.isPresent()){
                throw new IOException("Договора залога с таким id отсутствует ("
                        + fileImporter.getLong(excelColumnNum.getPledgeAgreementUpdate().getPledgeAgreementId())
                        + "). Строка: " + countRow);
            }

            if(Objects.isNull(fileImporter.getLong(excelColumnNum.getPledgeAgreementUpdate().getClientId()))){
                throw new IOException("Неверный id{"
                        + fileImporter.getLong(excelColumnNum.getPledgeAgreementUpdate().getClientId()) + ") клиента. Строка: " + countRow);
            }
            Optional<Client> client = clientService.getClientById(fileImporter
                    .getLong(excelColumnNum.getPledgeAgreementUpdate().getClientId()));
            if(!client.isPresent()){
                throw new IOException("Клиента с таким id отсутствует ("
                        + fileImporter.getLong(excelColumnNum.getPledgeAgreementUpdate().getClientId())
                        + "). Строка: " + countRow);
            }

            if(fileImporter.getLongList(excelColumnNum.getPledgeAgreementUpdate().getLoanAgreementsIds(), excelColumnNum.getDelimiter()).isEmpty()){
                throw new IOException("Неверный id{"
                        + fileImporter.getLongList(excelColumnNum.getPledgeAgreementUpdate().getLoanAgreementsIds(), excelColumnNum.getDelimiter())
                        + ") кредитного договора.");
            }
            List<LoanAgreement> loanAgreementList = repositoryLoanAgreement.findAllByLoanAgreementIdIn(fileImporter
                    .getLongList(excelColumnNum.getPledgeAgreementUpdate().getLoanAgreementsIds(), excelColumnNum.getDelimiter()));
            if(loanAgreementList.isEmpty()){
                throw new IOException("Кредитного договора с таким id отсутствует ("
                        + fileImporter.getLongList(excelColumnNum.getPledgeAgreementUpdate().getLoanAgreementsIds(), excelColumnNum.getDelimiter())
                        + "). Строка: " + countRow);
            }

            TypeOfPledgeAgreement typeOfPledgeAgreement;
            try {
                typeOfPledgeAgreement = TypeOfPledgeAgreement.valueOf(fileImporter.getString(excelColumnNum.getPledgeAgreementUpdate().getPervPosl()));
            }catch (IllegalArgumentException ex){
                typeOfPledgeAgreement = null;
            }

            StatusOfAgreement statusOfAgreement;
            try {
                statusOfAgreement = StatusOfAgreement.valueOf(fileImporter.getString(excelColumnNum.getPledgeAgreementUpdate().getStatus()));
            }catch (IllegalArgumentException ex){
                statusOfAgreement = null;
            }

            pledgeAgreement.get().setNumPA(fileImporter.getString(excelColumnNum.getPledgeAgreementUpdate().getNumPA()));
            pledgeAgreement.get().setDateBeginPA(fileImporter.getLocalDate(excelColumnNum.getPledgeAgreementUpdate().getDateBegin()));
            pledgeAgreement.get().setDateEndPA(fileImporter.getLocalDate(excelColumnNum.getPledgeAgreementUpdate().getDateEnd()));
            pledgeAgreement.get().setPervPosl(typeOfPledgeAgreement);
            pledgeAgreement.get().setStatusPA(statusOfAgreement);
            pledgeAgreement.get().setNoticePA(fileImporter.getString(excelColumnNum.getPledgeAgreementUpdate().getNotice()));
            pledgeAgreement.get().setClient(client.get());
            pledgeAgreement.get().setLoanAgreements(loanAgreementList);

            pledgeAgreementList.add(pledgeAgreement.get());

        }while (fileImporter.nextLine());

        return pledgeAgreementList;
    }

    @Transactional
    public PledgeAgreement updateInsertPledgeAgreement(PledgeAgreement pledgeAgreement){
        return repositoryPledgeAgreement.save(pledgeAgreement);
    }

    @Transactional
    public List<PledgeAgreement> updateInsertPledgeAgreements(List<PledgeAgreement> pledgeAgreementList){
        return repositoryPledgeAgreement.saveAll(pledgeAgreementList);
    }
}
