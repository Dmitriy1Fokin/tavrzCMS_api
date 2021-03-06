package ru.fds.tavrzcms3.service.impl;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fds.tavrzcms3.dictionary.StatusOfAgreement;
import ru.fds.tavrzcms3.dictionary.TypeOfPledgeAgreement;
import ru.fds.tavrzcms3.dictionary.excelproprities.ExcelColumnNum;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.jointable.LaJoinPa;
import ru.fds.tavrzcms3.domain.LoanAgreement;
import ru.fds.tavrzcms3.domain.jointable.PaJoinPs;
import ru.fds.tavrzcms3.domain.PledgeAgreement;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.domain.embedded.ClientIndividual;
import ru.fds.tavrzcms3.domain.embedded.ClientLegalEntity;
import ru.fds.tavrzcms3.exception.NotFoundException;
import ru.fds.tavrzcms3.exception.NotFullResultException;
import ru.fds.tavrzcms3.fileimport.FileImporter;
import ru.fds.tavrzcms3.fileimport.FileImporterFactory;
import ru.fds.tavrzcms3.repository.RepositoryLaJoinPa;
import ru.fds.tavrzcms3.repository.RepositoryLoanAgreement;
import ru.fds.tavrzcms3.repository.RepositoryPaJoinPs;
import ru.fds.tavrzcms3.repository.RepositoryPledgeAgreement;
import ru.fds.tavrzcms3.repository.RepositoryPledgeSubject;
import ru.fds.tavrzcms3.service.ClientService;
import ru.fds.tavrzcms3.service.MessageService;
import ru.fds.tavrzcms3.service.PledgeAgreementService;
import ru.fds.tavrzcms3.specification.Search;
import ru.fds.tavrzcms3.utils.DateUtils;
import ru.fds.tavrzcms3.validate.ValidatorEntity;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class PledgeAgreementServiceImpl implements PledgeAgreementService {


    private final RepositoryPledgeAgreement repositoryPledgeAgreement;
    private final RepositoryLoanAgreement repositoryLoanAgreement;
    private final RepositoryPledgeSubject repositoryPledgeSubject;
    private final RepositoryPaJoinPs repositoryPaJoinPs;
    private final RepositoryLaJoinPa repositoryLaJoinPa;
    private final ClientService clientService;
    private final ExcelColumnNum excelColumnNum;
    private final ValidatorEntity validatorEntity;
    private final MessageService messageService;
    private final DateUtils dateUtils;

    private static final String MSG_WRONG_ID = "Неверный id{";
    private static final String MSG_LINE = "). Строка: ";
    private static final String NOT_ALL_LOAN_AGREEMENTS_WERE_FOUND = "Not all loan agreements were found";

    public PledgeAgreementServiceImpl(RepositoryPledgeAgreement repositoryPledgeAgreement,
                                      RepositoryLoanAgreement repositoryLoanAgreement,
                                      RepositoryPledgeSubject repositoryPledgeSubject,
                                      RepositoryPaJoinPs repositoryPaJoinPs,
                                      RepositoryLaJoinPa repositoryLaJoinPa,
                                      ClientService clientService,
                                      ExcelColumnNum excelColumnNum,
                                      ValidatorEntity validatorEntity,
                                      MessageService messageService,
                                      DateUtils dateUtils) {
        this.repositoryPledgeAgreement = repositoryPledgeAgreement;
        this.repositoryLoanAgreement = repositoryLoanAgreement;
        this.repositoryPledgeSubject = repositoryPledgeSubject;
        this.repositoryPaJoinPs = repositoryPaJoinPs;
        this.repositoryLaJoinPa = repositoryLaJoinPa;
        this.clientService = clientService;
        this.excelColumnNum = excelColumnNum;
        this.validatorEntity = validatorEntity;
        this.messageService = messageService;
        this.dateUtils = dateUtils;
    }

    @Override
    public Optional<PledgeAgreement> getPledgeAgreementById(Long pledgeAgreementId){
        return repositoryPledgeAgreement.findById(pledgeAgreementId);
    }

    @Override
    public List<PledgeAgreement> getPledgeAgreementsByIds(List<Long> ids){
        return repositoryPledgeAgreement.findAllByPledgeAgreementIdIn(ids);
    }

    @Override
    public List<PledgeAgreement> getAllCurrentPledgeAgreements(Pageable pageable){
        return repositoryPledgeAgreement.findAllByStatusPAEquals(pageable, StatusOfAgreement.OPEN).getContent();
    }

    @Override
    public Integer getCountCurrentPledgeAgreements(){
        return repositoryPledgeAgreement.countAllByStatusPAEquals(StatusOfAgreement.OPEN);
    }

    @Override
    public List<PledgeAgreement> getAllCurrentPledgeAgreements(Pageable pageable, TypeOfPledgeAgreement typeOfPledgeAgreement){
        return repositoryPledgeAgreement.findAllByStatusPAEqualsAndPervPoslEquals(pageable,
                StatusOfAgreement.OPEN,
                typeOfPledgeAgreement).getContent();
    }

    @Override
    public Integer getCountCurrentPledgeAgreements(TypeOfPledgeAgreement typeOfPledgeAgreement){
        return repositoryPledgeAgreement.countAllByStatusPAEqualsAndPervPoslEquals(StatusOfAgreement.OPEN, typeOfPledgeAgreement);
    }

    @Override
    public List<PledgeAgreement> getPledgeAgreementsByNumPA(Pageable pageable, String numPA){
        return repositoryPledgeAgreement.findAllByNumPAContainingIgnoreCase(pageable, numPA).getContent();
    }

    @Override
    public List<LocalDate> getDatesOfConclusion(PledgeAgreement pledgeAgreement){
        List<Date> dateList = repositoryPledgeAgreement.getDatesOfConclusion(pledgeAgreement.getPledgeAgreementId());

        if(dateList.isEmpty()){
            return Collections.emptyList();
        }

        List<LocalDate> localDateList = new ArrayList<>(dateList.size());
        for(Date date : dateList) {
            localDateList.add(date.toLocalDate());
        }

        return localDateList;
    }

    @Override
    public List<LocalDate> getDatesOfMonitoring(PledgeAgreement pledgeAgreement){
        List<Date> dateList = repositoryPledgeAgreement.getDatesOMonitorings(pledgeAgreement.getPledgeAgreementId());

        if(dateList.isEmpty()) {
            return Collections.emptyList();
        }

        List<LocalDate> localDateList = new ArrayList<>(dateList.size());
        for(Date date : dateList) {
            localDateList.add(date.toLocalDate());
        }

        return localDateList;
    }

    @Override
    public List<String> getResultsOfMonitoring(PledgeAgreement pledgeAgreement){
        return repositoryPledgeAgreement.getResultsOfMonitoring(pledgeAgreement.getPledgeAgreementId());
    }

    @Override
    public List<String> getTypeOfCollateral(PledgeAgreement pledgeAgreement){
        return  repositoryPledgeAgreement.getTypeOfCollateral(pledgeAgreement.getPledgeAgreementId());
    }

    @Override
    public List<String> getBriefInfoAboutCollateral(PledgeAgreement pledgeAgreement){
        return repositoryPledgeAgreement.getBriefInfoAboutCollateral(pledgeAgreement.getPledgeAgreementId());
    }

    @Override
    public List<PledgeAgreement> getAllPledgeAgreementByPLedgeSubject(Long pledgeSubjectId){
        return repositoryPledgeAgreement.getPledgeAgreementByPLedgeSubject(pledgeSubjectId);
    }

    @Override
    public List<PledgeAgreement> getCurrentPledgeAgreementsByEmployee(Long employeeId, TypeOfPledgeAgreement pervPosl, Pageable pageable){
        return repositoryPledgeAgreement.getCurrentPledgeAgreementsForEmployee(pageable, employeeId, pervPosl.getTranslate()).getContent();
    }

    @Override
    public Integer getCountOfCurrentPledgeAgreementsByEmployee(Long employeeId, TypeOfPledgeAgreement pervPosl){
        return repositoryPledgeAgreement.getCountOfCurrentPledgeAgreementsForEmployee(employeeId, pervPosl.getTranslate());
    }

    @Override
    public List<PledgeAgreement> getCurrentPledgeAgreementsByEmployee(Long employeeId, Pageable pageable){
        return repositoryPledgeAgreement.getCurrentPledgeAgreementsForEmployee(pageable, employeeId).getContent();
    }

    @Override
    public Integer getCountOfCurrentPledgeAgreementsByEmployee(Long employeeId){
        return repositoryPledgeAgreement.getCountOfCurrentPledgeAgreementsForEmployee(employeeId);
    }

    @Override
    public List<PledgeAgreement> getPledgeAgreementWithMonitoringNotDone(Long employeeId){
        return repositoryPledgeAgreement.getPledgeAgreementWithMonitoringBetweenDates(employeeId,
                dateUtils.getFirstDateInThisMonthInLastYear(),
                dateUtils.getLastDateInThisMonthInLastYear());
    }

    @Override
    public Integer getCountPledgeAgreementWithMonitoringNotDone(Long employeeId){
        return repositoryPledgeAgreement.getCountPledgeAgreementWithMonitoringBetweenDates(employeeId,
                dateUtils.getFirstDateInThisMonthInLastYear(),
                dateUtils.getLastDateInThisMonthInLastYear());
    }

    @Override
    public List<PledgeAgreement> getPledgeAgreementWithMonitoringIsDone(Long employeeId){
        return repositoryPledgeAgreement.getPledgeAgreementWithMonitoringBetweenDates(employeeId,
                dateUtils.getFirstDateInThisMonth(),
                dateUtils.getLastDateInThisMonth());
    }

    @Override
    public Integer getCountPledgeAgreementWithMonitoringIsDone(Long employeeId){
        return repositoryPledgeAgreement.getCountPledgeAgreementWithMonitoringBetweenDates(employeeId,
                dateUtils.getFirstDateInThisMonth(),
                dateUtils.getLastDateInThisMonth());
    }

    @Override
    public List<PledgeAgreement> getPledgeAgreementWithMonitoringOverDue(Long employeeId){
        return repositoryPledgeAgreement.getPledgeAgreementWithMonitoringLessDate(employeeId,
                dateUtils.getFirstDateInThisMonthInLastYear());
    }

    @Override
    public Integer getCountPledgeAgreementWithMonitoringOverdue(Long employeeId){
        return repositoryPledgeAgreement.getCountPledgeAgreementWithMonitoringLessDate(employeeId,
                dateUtils.getFirstDateInThisMonthInLastYear());
    }

    @Override
    public List<PledgeAgreement> getPledgeAgreementWithConclusionNotDone(Long employeeId){
        return repositoryPledgeAgreement.getPledgeAgreementWithConclusionsBetweenDates(employeeId,
                dateUtils.getFirstDateInThisMonthInLastYear(),
                dateUtils.getLastDateInThisMonthInLastYear());
    }

    @Override
    public Integer getCountPledgeAgreementWithConclusionNotDone(Long employeeId){
        return repositoryPledgeAgreement.getCountPledgeAgreementWithConclusionsBetweenDates(employeeId,
                dateUtils.getFirstDateInThisMonthInLastYear(),
                dateUtils.getLastDateInThisMonthInLastYear());
    }

    @Override
    public List<PledgeAgreement> getPledgeAgreementWithConclusionIsDone(Long employeeId){
        return repositoryPledgeAgreement.getPledgeAgreementWithConclusionsBetweenDates(employeeId,
                dateUtils.getFirstDateInThisMonth(),
                dateUtils.getLastDateInThisMonth());
    }

    @Override
    public Integer getCountPledgeAgreementWithConclusionIsDone(Long employeeId){
        return repositoryPledgeAgreement.getCountPledgeAgreementWithConclusionsBetweenDates(employeeId,
                dateUtils.getFirstDateInThisMonth(),
                dateUtils.getLastDateInThisMonth());
    }

    @Override
    public List<PledgeAgreement> getPledgeAgreementWithConclusionOverdue(Long employeeId){
        return repositoryPledgeAgreement.getPledgeAgreementWithConclusionsLessDate(employeeId,
                dateUtils.getFirstDateInThisMonthInLastYear());
    }

    @Override
    public Integer getCountPledgeAgreementWithConclusionOverdue(Long employeeId){
        return repositoryPledgeAgreement.getCountPledgeAgreementWithConclusionsLessDate(employeeId,
                dateUtils.getFirstDateInThisMonthInLastYear());
    }

    @Override
    public List<PledgeAgreement> getPledgeAgreementFromSearch(Map<String, String> searchParam, Pageable pageable) throws ReflectiveOperationException{
        Search<PledgeAgreement> pledgeAgreementSearch = new Search<>(PledgeAgreement.class);

        Set<String> clientAttributes = new HashSet<>();
        Arrays.stream(Client.class.getDeclaredFields()).forEach(field -> clientAttributes.add(field.getName()));
        Arrays.stream(ClientIndividual.class.getDeclaredFields()).forEach(field -> clientAttributes.add(field.getName()));
        Arrays.stream(ClientLegalEntity.class.getDeclaredFields()).forEach(field -> clientAttributes.add(field.getName()));
        clientAttributes.retainAll(searchParam.keySet());

        if(!clientAttributes.isEmpty() && clientAttributes.contains("typeOfClient")){
            List<Client> clientList = clientService.getClientFromSearch(searchParam);
            pledgeAgreementSearch.withParamClient(clientList);
        }

        pledgeAgreementSearch.withParam(searchParam);

        Specification<PledgeAgreement> specification = pledgeAgreementSearch.getSpecification();

        return repositoryPledgeAgreement.findAll(specification, pageable).getContent();
    }

    @Override
    public List<PledgeAgreement> getCurrentPledgeAgreementsByPledgor(Long clientId){
        return  repositoryPledgeAgreement.getPledgeAgreementsByClient(clientId, StatusOfAgreement.OPEN.getTranslate());
    }

    @Override
    public List<PledgeAgreement> getClosedPledgeAgreementsByPledgor(Long clientId){
        return  repositoryPledgeAgreement.getPledgeAgreementsByClient(clientId, StatusOfAgreement.CLOSED.getTranslate());
    }

    @Override
    public List<PledgeAgreement> getCurrentPledgeAgreementsByLoanAgreement(Long loanAgreementId){
        return repositoryPledgeAgreement.findByLoanAgreementAndStatusPA(loanAgreementId, StatusOfAgreement.OPEN);
    }

    @Override
    public List<PledgeAgreement> getClosedPledgeAgreementsByLoanAgreement(Long loanAgreementId){
        return repositoryPledgeAgreement.findByLoanAgreementAndStatusPA(loanAgreementId,StatusOfAgreement.CLOSED);
    }

    @Override
    @Transactional
    public List<PledgeAgreement> getNewPledgeAgreementsFromFile(File file) throws IOException {
        FileImporter fileImporter = FileImporterFactory.getInstance(file);
        for(int i = 0; i < excelColumnNum.getStartRow(); i++){
            fileImporter.nextLine();
        }
        int countRow = excelColumnNum.getStartRow();

        List<PledgeAgreement> pledgeAgreementList = new ArrayList<>();
        List<List<LoanAgreement>> loanAgreementsList = new ArrayList<>();

        do {
            countRow += 1;

            PledgeAgreement pledgeAgreement = PledgeAgreement.builder()
                    .numPA(fileImporter.getString(excelColumnNum.getPledgeAgreementNew().getNumPA()))
                    .dateBeginPA(fileImporter.getLocalDate(excelColumnNum.getPledgeAgreementNew().getDateBegin()))
                    .dateEndPA(fileImporter.getLocalDate(excelColumnNum.getPledgeAgreementNew().getDateEnd()))
                    .pervPosl(TypeOfPledgeAgreement.valueOfString(fileImporter.getString(excelColumnNum.getPledgeAgreementNew().getPervPosl())).orElse(null))
                    .statusPA(StatusOfAgreement.valueOfString(fileImporter.getString(excelColumnNum.getPledgeAgreementNew().getStatus())).orElse(null))
                    .noticePA(fileImporter.getString(excelColumnNum.getPledgeAgreementNew().getNotice()))
                    .client(setClientInNewPledgeAgreement(fileImporter, countRow))
                    .rsDz(BigDecimal.ZERO)
                    .rsZz(BigDecimal.ZERO)
                    .zsDz(BigDecimal.ZERO)
                    .zsZz(BigDecimal.ZERO)
                    .ss(BigDecimal.ZERO)
                    .build();

            Set<ConstraintViolation<PledgeAgreement>> violations =  validatorEntity.validateEntity(pledgeAgreement);
            if(!violations.isEmpty())
                throw new ConstraintViolationException("Object " + countRow, violations);

            pledgeAgreementList.add(pledgeAgreement);
            loanAgreementsList.add(setLoanAgreementsInNewPledgeAgreement(fileImporter, countRow));

        }while (fileImporter.nextLine());

        pledgeAgreementList = insertPledgeAgreements(pledgeAgreementList, loanAgreementsList);

        return pledgeAgreementList;
    }

    private Client setClientInNewPledgeAgreement(FileImporter fileImporter, int countRow) throws IOException {
        if(Objects.isNull(fileImporter.getLong(excelColumnNum.getPledgeAgreementNew().getClientId()))){
            throw new IOException(MSG_WRONG_ID
                    + fileImporter.getLong(excelColumnNum.getPledgeAgreementNew().getClientId())
                    + ") клиента. Строка: " + countRow);
        }

        return clientService.getClientById(fileImporter.getLong(excelColumnNum.getPledgeAgreementNew().getClientId()))
                .orElseThrow(() -> new IOException("Клиента с таким id отсутствует ("
                        + fileImporter.getLong(excelColumnNum.getPledgeAgreementNew().getClientId())
                        + MSG_LINE + countRow));
    }

    private List<LoanAgreement> setLoanAgreementsInNewPledgeAgreement(FileImporter fileImporter, int countRow) throws IOException {
        if(fileImporter.getLongList(excelColumnNum.getPledgeAgreementNew().getLoanAgreementsIds(), excelColumnNum.getDelimiter()).isEmpty()){
            throw new IOException(MSG_WRONG_ID
                    + fileImporter.getLongList(excelColumnNum.getPledgeAgreementNew().getLoanAgreementsIds(), excelColumnNum.getDelimiter())
                    + ") кредитного договора. Строка: " + countRow);
        }

        List<Long> ids = fileImporter
                .getLongList(excelColumnNum.getPledgeAgreementNew().getLoanAgreementsIds(), excelColumnNum.getDelimiter());
        List<LoanAgreement> loanAgreementList = repositoryLoanAgreement.findAllByLoanAgreementIdIn(ids);

        if(loanAgreementList.size() < ids.size()){
            throw new IOException(NOT_ALL_LOAN_AGREEMENTS_WERE_FOUND);
        }

        if(loanAgreementList.isEmpty()){
            throw new IOException("Кредитного договора с таким id отсутствует ("
                    + fileImporter.getLongList(excelColumnNum.getPledgeAgreementNew().getLoanAgreementsIds(), excelColumnNum.getDelimiter())
                    + MSG_LINE + countRow);
        }

        return loanAgreementList;
    }

    @Override
    @Transactional
    public List<PledgeAgreement> getCurrentPledgeAgreementsFromFile(File file) throws IOException {
        FileImporter fileImporter = FileImporterFactory.getInstance(file);
        for(int i = 0; i < excelColumnNum.getStartRow(); i++){
            fileImporter.nextLine();
        }
        int countRow = excelColumnNum.getStartRow();

        List<PledgeAgreement> pledgeAgreementList = new ArrayList<>();
        List<List<LoanAgreement>> loanAgreementsList = new ArrayList<>();

        do {
            countRow += 1;

            PledgeAgreement pledgeAgreement = setCurrentPledgeAgreement(fileImporter, countRow);

            pledgeAgreement.setNumPA(fileImporter.getString(excelColumnNum.getPledgeAgreementUpdate().getNumPA()));
            pledgeAgreement.setDateBeginPA(fileImporter.getLocalDate(excelColumnNum.getPledgeAgreementUpdate().getDateBegin()));
            pledgeAgreement.setDateEndPA(fileImporter.getLocalDate(excelColumnNum.getPledgeAgreementUpdate().getDateEnd()));
            pledgeAgreement.setPervPosl(TypeOfPledgeAgreement.valueOfString(fileImporter.getString(excelColumnNum.getPledgeAgreementUpdate().getPervPosl())).orElse(null));
            pledgeAgreement.setStatusPA(StatusOfAgreement.valueOfString(fileImporter.getString(excelColumnNum.getPledgeAgreementUpdate().getStatus())).orElse(null));
            pledgeAgreement.setNoticePA(fileImporter.getString(excelColumnNum.getPledgeAgreementUpdate().getNotice()));
            pledgeAgreement.setClient(setClientInCurrentPledgeAgreement(fileImporter, countRow));

            Set<ConstraintViolation<PledgeAgreement>> violations =  validatorEntity.validateEntity(pledgeAgreement);
            if(!violations.isEmpty())
                throw new ConstraintViolationException("Object " + countRow, violations);

            pledgeAgreementList.add(pledgeAgreement);
            loanAgreementsList.add(setLoanAgreementsInCurrentPledgeAgreement(fileImporter, countRow));

        }while (fileImporter.nextLine());

        pledgeAgreementList = updatePledgeAgreements(pledgeAgreementList, loanAgreementsList);

        return pledgeAgreementList;
    }

    private PledgeAgreement setCurrentPledgeAgreement(FileImporter fileImporter, int countRow) throws IOException {
        if(Objects.isNull(fileImporter.getLong(excelColumnNum.getPledgeAgreementUpdate().getPledgeAgreementId()))){
            throw new IOException(MSG_WRONG_ID
                    + fileImporter.getLong(excelColumnNum.getPledgeAgreementUpdate().getPledgeAgreementId())
                    + ") договора залога. Строка: " + countRow);
        }

        return getPledgeAgreementById(fileImporter.getLong(excelColumnNum.getPledgeAgreementUpdate().getPledgeAgreementId()))
                .orElseThrow(() -> new IOException("Договора залога с таким id отсутствует ("
                        + fileImporter.getLong(excelColumnNum.getPledgeAgreementUpdate().getPledgeAgreementId())
                        + MSG_LINE + countRow));
    }

    private Client setClientInCurrentPledgeAgreement(FileImporter fileImporter, int countRow) throws IOException {
        if(Objects.isNull(fileImporter.getLong(excelColumnNum.getPledgeAgreementUpdate().getClientId()))){
            throw new IOException(MSG_WRONG_ID
                    + fileImporter.getLong(excelColumnNum.getPledgeAgreementUpdate().getClientId())
                    + ") клиента. Строка: " + countRow);
        }

        return clientService.getClientById(fileImporter.getLong(excelColumnNum.getPledgeAgreementUpdate().getClientId()))
                .orElseThrow(() -> new IOException("Клиента с таким id отсутствует ("
                        + fileImporter.getLong(excelColumnNum.getPledgeAgreementUpdate().getClientId())
                        + MSG_LINE + countRow));
    }

    private List<LoanAgreement> setLoanAgreementsInCurrentPledgeAgreement(FileImporter fileImporter, int countRow) throws IOException {
        if(fileImporter.getLongList(excelColumnNum.getPledgeAgreementUpdate().getLoanAgreementsIds(), excelColumnNum.getDelimiter()).isEmpty()){
            throw new IOException(MSG_WRONG_ID
                    + fileImporter.getLongList(excelColumnNum.getPledgeAgreementUpdate().getLoanAgreementsIds(), excelColumnNum.getDelimiter())
                    + ") кредитного договора. Строка: " + countRow);
        }

        List<Long> ids = fileImporter
                .getLongList(excelColumnNum.getPledgeAgreementUpdate().getLoanAgreementsIds(), excelColumnNum.getDelimiter());
        List<LoanAgreement> loanAgreementList = repositoryLoanAgreement.findAllByLoanAgreementIdIn(ids);

        if(loanAgreementList.size() < ids.size()){
            throw new IOException(NOT_ALL_LOAN_AGREEMENTS_WERE_FOUND);
        }

        if(loanAgreementList.isEmpty()){
            throw new IOException("Кредитного договора с таким id отсутствует ("
                    + fileImporter.getLongList(excelColumnNum.getPledgeAgreementUpdate().getLoanAgreementsIds(), excelColumnNum.getDelimiter())
                    + MSG_LINE + countRow);
        }

        return loanAgreementList;
    }

    @Override
    @Transactional
    public PledgeAgreement insertPledgeAgreement(PledgeAgreement pledgeAgreement, List<Long> loanAgreementsIds){
        pledgeAgreement = repositoryPledgeAgreement.save(pledgeAgreement);

        List<LoanAgreement> loanAgreementList = repositoryLoanAgreement.findAllByLoanAgreementIdIn(loanAgreementsIds);
        if(loanAgreementList.size() < loanAgreementsIds.size()){
            throw new NotFullResultException(NOT_ALL_LOAN_AGREEMENTS_WERE_FOUND);
        }

        List<LaJoinPa> laJoinPaList = new ArrayList<>();
        for (LoanAgreement agreement : loanAgreementList) {
            laJoinPaList.add(new LaJoinPa(agreement, pledgeAgreement));
        }

        repositoryLaJoinPa.saveAll(laJoinPaList);

        messageService.sendNewPledgeAgreement(pledgeAgreement.getPledgeAgreementId());

        return pledgeAgreement;
    }

    @Override
    @Transactional
    public PledgeAgreement updatePledgeAgreement(PledgeAgreement pledgeAgreement, List<Long> loanAgreementsIds){
        pledgeAgreement = repositoryPledgeAgreement.save(pledgeAgreement);

        List<LoanAgreement> loanAgreementListFromDB = repositoryLoanAgreement.findByPledgeAgreement(pledgeAgreement);
        List<LoanAgreement> loanAgreementListFromRequest = repositoryLoanAgreement.findAllByLoanAgreementIdIn(loanAgreementsIds);
        if(loanAgreementListFromRequest.size() < loanAgreementsIds.size()){
            throw new NotFullResultException(NOT_ALL_LOAN_AGREEMENTS_WERE_FOUND);
        }

        List<LoanAgreement> loanAgreementListToDelete = new ArrayList<>(loanAgreementListFromDB);
        loanAgreementListToDelete.removeAll(loanAgreementListFromRequest);
        repositoryLaJoinPa.deleteByLoanAgreementInAndPledgeAgreement(loanAgreementListToDelete, pledgeAgreement);

        List<LoanAgreement> loanAgreementListToInsert = new ArrayList<>(loanAgreementListFromRequest);
        loanAgreementListToInsert.removeAll(loanAgreementListFromDB);
        List<LaJoinPa> laJoinPaList = new ArrayList<>();
        for(LoanAgreement loanAgreement : loanAgreementListToInsert){
            laJoinPaList.add(new LaJoinPa(loanAgreement, pledgeAgreement));
        }
        repositoryLaJoinPa.saveAll(laJoinPaList);

        messageService.sendExistPledgeAgreement(pledgeAgreement.getPledgeAgreementId());

        return pledgeAgreement;
    }

    @Override
    @Transactional
    public List<PledgeAgreement> insertPledgeAgreements(List<PledgeAgreement> pledgeAgreementList,
                                                        List<List<LoanAgreement>> loanAgreementsList){

        pledgeAgreementList = repositoryPledgeAgreement.saveAll(pledgeAgreementList);
        List<LaJoinPa> laJoinPaList = new ArrayList<>();
        for(int i = 0; i < pledgeAgreementList.size(); i++){
            List<LoanAgreement> loanAgreements = loanAgreementsList.get(i);
            for(LoanAgreement la : loanAgreements){
                laJoinPaList.add(new LaJoinPa(la, pledgeAgreementList.get(i)));
            }
        }

        repositoryLaJoinPa.saveAll(laJoinPaList);

        pledgeAgreementList.forEach(pledgeAgreement -> messageService.sendNewPledgeAgreement(pledgeAgreement.getPledgeAgreementId()));

        return pledgeAgreementList;
    }


    @Override
    @Transactional
    public List<PledgeAgreement> updatePledgeAgreements(List<PledgeAgreement> pledgeAgreementList,
                                                        List<List<LoanAgreement>> loanAgreementsList){

        pledgeAgreementList = repositoryPledgeAgreement.saveAll(pledgeAgreementList);
        List<LaJoinPa> laJoinPaList = new ArrayList<>();
        for(int i = 0; i < pledgeAgreementList.size(); i++){
            List<LoanAgreement> loanAgreementListFromDB = repositoryLoanAgreement.findByPledgeAgreement(pledgeAgreementList.get(i));
            List<LoanAgreement> loanAgreementListToDelete = new ArrayList<>(loanAgreementListFromDB);
            loanAgreementListToDelete.removeAll(loanAgreementsList.get(i));
            repositoryLaJoinPa.deleteByLoanAgreementInAndPledgeAgreement(loanAgreementListToDelete, pledgeAgreementList.get(i));

            List<LoanAgreement> loanAgreementListToInsert = new ArrayList<>(loanAgreementsList.get(i));
            loanAgreementListToInsert.removeAll(loanAgreementListFromDB);
            for(LoanAgreement loanAgreement : loanAgreementListToInsert){
                laJoinPaList.add(new LaJoinPa(loanAgreement, pledgeAgreementList.get(i)));
            }
        }

        repositoryLaJoinPa.saveAll(laJoinPaList);

        pledgeAgreementList.forEach(pledgeAgreement -> messageService.sendExistPledgeAgreement(pledgeAgreement.getPledgeAgreementId()));

        return pledgeAgreementList;
    }

    @Override
    @Transactional
    public PledgeAgreement withdrawPledgeSubjectFromPledgeAgreement(Long pledgeAgreementId ,Long pledgeSubjectId){
        PledgeAgreement pledgeAgreement = getPledgeAgreementById(pledgeAgreementId)
                .orElseThrow(() -> new NotFoundException("Pledge agreement not found"));

        PledgeSubject pledgeSubject = repositoryPledgeSubject.findById(pledgeSubjectId)
                .orElseThrow(() -> new NotFoundException("Pledge subject not found"));

        repositoryPaJoinPs.deleteByPledgeAgreementAndPledgeSubject(pledgeAgreement, pledgeSubject);

        return pledgeAgreement;
    }

    @Override
    @Transactional
    public PledgeAgreement insertCurrentPledgeSubjectsInPledgeAgreement(Long pledgeAgreementId ,List<Long> pledgeSubjectsIds){
        PledgeAgreement pledgeAgreement = getPledgeAgreementById(pledgeAgreementId)
                .orElseThrow(() -> new NotFoundException("Pledge agreement not found"));

        List<PledgeSubject> pledgeSubjectListNew = repositoryPledgeSubject.findAllByPledgeSubjectIdIn(pledgeSubjectsIds);
        if(pledgeSubjectListNew.size() < pledgeSubjectsIds.size()){
            throw new NotFullResultException("Not all pledge subjects were found");
        }

        List<PaJoinPs> paJoinPsList = new ArrayList<>(pledgeSubjectListNew.size());
        pledgeSubjectListNew.forEach(pledgeSubject -> {
            if(!repositoryPaJoinPs.existsByPledgeAgreementAndPledgeSubject(pledgeAgreement, pledgeSubject)){
                paJoinPsList.add(new PaJoinPs(pledgeAgreement, pledgeSubject));
            }
        });

        repositoryPaJoinPs.saveAll(paJoinPsList);

        return pledgeAgreement;
    }
}
