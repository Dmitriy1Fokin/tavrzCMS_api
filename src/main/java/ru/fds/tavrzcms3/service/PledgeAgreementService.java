package ru.fds.tavrzcms3.service;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fds.tavrzcms3.dictionary.Operations;
import ru.fds.tavrzcms3.dictionary.StatusOfAgreement;
import ru.fds.tavrzcms3.dictionary.TypeOfPledgeAgreement;
import ru.fds.tavrzcms3.domain.*;
import ru.fds.tavrzcms3.repository.*;
import ru.fds.tavrzcms3.specification.SearchCriteria;
import ru.fds.tavrzcms3.specification.SpecificationBuilder;
import ru.fds.tavrzcms3.specification.impl.SpecificationBuilderImpl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
    private final ClientService clientService;

    public PledgeAgreementService(RepositoryPledgeAgreement repositoryPledgeAgreement,
                                  ClientService clientService) {
        this.repositoryPledgeAgreement = repositoryPledgeAgreement;
        this.clientService = clientService;
    }

    public List<PledgeAgreement> getPledgeAgreementsByIds(List<Long> ids){
        return repositoryPledgeAgreement.findAllByPledgeAgreementIdIn(ids);
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

    public Optional<PledgeAgreement> getPledgeAgreementById(long pledgeAgreementId){
        return repositoryPledgeAgreement.findById(pledgeAgreementId);
    }

    public List<PledgeAgreement> getAllPledgeAgreementByPLedgeSubject(PledgeSubject pledgeSubject){
        return repositoryPledgeAgreement.findAllByPledgeSubjects(pledgeSubject);
    }

    public List<String> getTypeOfCollateral(PledgeAgreement pledgeAgreement){
        return  repositoryPledgeAgreement.getTypeOfCollateral(pledgeAgreement.getPledgeAgreementId());
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

    public List<PledgeAgreement> getPledgeAgreementWithMonitoringNotDone(Employee employee){
        LocalDate now = LocalDate.now();
        LocalDate firstDate = LocalDate.of(now.getYear()-1, now.getMonthValue(), 1);
        LocalDate secondDate = firstDate.plusMonths(1);

        return repositoryPledgeAgreement.getPledgeAgreementWithMonitoringBetweenDates(employee.getEmployeeId(), firstDate, secondDate);
    }

    public List<PledgeAgreement> getPledgeAgreementWithMonitoringIsDone(Employee employee){
        LocalDate now = LocalDate.now();
        LocalDate firstDate = LocalDate.of(now.getYear(), now.getMonthValue(), 1);
        LocalDate secondDate = firstDate.plusMonths(1);

        return repositoryPledgeAgreement.getPledgeAgreementWithMonitoringBetweenDates(employee.getEmployeeId(), firstDate, secondDate);
    }

    public List<PledgeAgreement> getPledgeAgreementWithMonitoringOverDue(Employee employee){
        LocalDate now = LocalDate.now();
        LocalDate firstDate = LocalDate.of(now.getYear()-1, now.getMonth(), 1);

        return repositoryPledgeAgreement.getPledgeAgreementWithMonitoringLessDate(employee.getEmployeeId(), firstDate);
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

    public List<PledgeAgreement> getPledgeAgreementWithConclusionNotDone(Employee employee){
        LocalDate now = LocalDate.now();
        LocalDate firstDate = LocalDate.of(now.getYear()-1, now.getMonthValue(), 1);
        LocalDate secondDate = firstDate.plusMonths(1);

        return repositoryPledgeAgreement.getPledgeAgreementWithConclusionsBetweenDates(employee.getEmployeeId(), firstDate, secondDate);
    }

    public List<PledgeAgreement> getPledgeAgreementWithConclusionIsDone(Employee employee){
        LocalDate now = LocalDate.now();
        LocalDate firstDate = LocalDate.of(now.getYear(), now.getMonth(), 1);
        LocalDate secondDate = firstDate.plusMonths(1);

        return repositoryPledgeAgreement.getPledgeAgreementWithConclusionsBetweenDates(employee.getEmployeeId(), firstDate, secondDate);
    }

    public List<PledgeAgreement> getPledgeAgreementWithConclusionOverDue(Employee employee){
        LocalDate now = LocalDate.now();
        LocalDate firstDate = LocalDate.of(now.getYear()-1, now.getMonth(), 1);

        return repositoryPledgeAgreement.getPledgeAgreementWithConclusionsLessDate(employee.getEmployeeId(), firstDate);
    }

    public List<PledgeAgreement> getPledgeAgreementFromSearch(Map<String, String> searchParam) throws ReflectiveOperationException{

        SpecificationBuilder builder = new SpecificationBuilderImpl();

        for(Field field : PledgeAgreement.class.getDeclaredFields()){
            if(searchParam.containsKey(field.getName())){
                if((field.getType() == String.class || field.getType() == double.class) && !searchParam.get(field.getName()).isEmpty()){
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

    public List<PledgeAgreement> getCurrentPledgeAgreementsByPledgor(Client client){
        return repositoryPledgeAgreement.findByClientAndStatusPA(client, StatusOfAgreement.OPEN);
    }

    public List<PledgeAgreement> getClosedPledgeAgreementsByPledgor(Client client){
        return repositoryPledgeAgreement.findByClientAndStatusPA(client, StatusOfAgreement.CLOSED);
    }

    public List<PledgeAgreement> getCurrentPledgeAgreementsByLoanAgreement(LoanAgreement loanAgreement){
        return repositoryPledgeAgreement.findByLoanAgreementsAndStatusPAEquals(loanAgreement, StatusOfAgreement.OPEN);
    }

    public List<PledgeAgreement> getClosedPledgeAgreementsByLoanAgreement(LoanAgreement loanAgreement){
        return repositoryPledgeAgreement.findByLoanAgreementsAndStatusPAEquals(loanAgreement,StatusOfAgreement.CLOSED);
    }

    public List<PledgeAgreement> getAllPledgeAgreementsByLoanAgreement(LoanAgreement loanAgreement){
        return repositoryPledgeAgreement.findByLoanAgreements(loanAgreement);
    }

    public List<PledgeAgreement> getAllPledgeAgreementsByPledgor(Client client){
        return repositoryPledgeAgreement.findAllByClient(client);
    }

    @Transactional
    public PledgeAgreement updateInsertPledgeAgreement(PledgeAgreement pledgeAgreement){
        return repositoryPledgeAgreement.save(pledgeAgreement);
    }

    @Transactional
    public List<PledgeAgreement> updateInsertPledgeAgreementList(List<PledgeAgreement> pledgeAgreementList){
        return repositoryPledgeAgreement.saveAll(pledgeAgreementList);
    }
}
