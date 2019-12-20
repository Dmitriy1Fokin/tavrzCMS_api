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
import ru.fds.tavrzcms3.specification.SpecificationBuilderImpl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PledgeAgreementService {

    private final RepositoryPledgeAgreement repositoryPledgeAgreement;
    private final RepositoryPledgeSubject repositoryPledgeSubject;
    private final ClientService clientService;

    public PledgeAgreementService(RepositoryPledgeAgreement repositoryPledgeAgreement,
                                  RepositoryPledgeSubject repositoryPledgeSubject,
                                  ClientService clientService) {
        this.repositoryPledgeAgreement = repositoryPledgeAgreement;
        this.repositoryPledgeSubject = repositoryPledgeSubject;
        this.clientService = clientService;
    }

    public List<PledgeAgreement> getPledgeAgreementsByIds(List<Long> ids){
        return repositoryPledgeAgreement.findAllByPledgeAgreementIdIn(ids);
    }

    public List<PledgeAgreement> getPledgeAgreementsByNumPA(String numPA){
        return repositoryPledgeAgreement.findAllByNumPAContainingIgnoreCase(numPA);
    }

    public List<Date> getDatesOfConclusion(PledgeAgreement pledgeAgreement){
        return repositoryPledgeAgreement.getDatesOfConclusion(pledgeAgreement.getPledgeAgreementId());
    }

    public List<Date> getDatesOfConclusion(long pledgeAgreementId){
        return repositoryPledgeAgreement.getDatesOfConclusion(pledgeAgreementId);
    }

    public List<Date> getDatesOfMonitoring(PledgeAgreement pledgeAgreement){
        return repositoryPledgeAgreement.getDatesOMonitorings(pledgeAgreement.getPledgeAgreementId());
    }

    public List<Date> getDatesOfMonitoring(long pledgeAgreementId){
        return repositoryPledgeAgreement.getDatesOMonitorings(pledgeAgreementId);
    }

    public List<String> getResultsOfMonitoring(PledgeAgreement pledgeAgreement){
        return repositoryPledgeAgreement.getResultsOfMonitoring(pledgeAgreement.getPledgeAgreementId());
    }

    public List<String> getResultsOfMonitoring(long pledgeAgreementId){
        return repositoryPledgeAgreement.getResultsOfMonitoring(pledgeAgreementId);
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

    public List<String> getTypeOfCollateral(long pledgeAgreementId){
        return  repositoryPledgeAgreement.getTypeOfCollateral(pledgeAgreementId);
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
        List<PledgeAgreement> pledgeAgreementList = getCurrentPledgeAgreementsByEmployee(employee.getEmployeeId(), TypeOfPledgeAgreement.PERV);
        Calendar dateNow = new GregorianCalendar();
        Calendar firstDateCalendar = new GregorianCalendar(dateNow.get(Calendar.YEAR) - 1, dateNow.get(Calendar.MONTH), 1);
        Calendar secondDateCalendar = new GregorianCalendar(dateNow.get(Calendar.YEAR) - 1, dateNow.get(Calendar.MONTH), dateNow.getMaximum(Calendar.DAY_OF_MONTH));
        Date firstDate = new Date(firstDateCalendar.getTimeInMillis());
        Date secondDate = new Date(secondDateCalendar.getTimeInMillis());
        int countOfMonitoring = 0;
        for(PledgeAgreement pa : pledgeAgreementList)
            if(repositoryPledgeSubject.existsByPledgeAgreementsAndDateMonitoringBetween(pa, firstDate,secondDate))
                countOfMonitoring += 1;

        return countOfMonitoring;
    }

    public int countOfMonitoringIsDone(Employee employee){
        List<PledgeAgreement> pledgeAgreementList = getCurrentPledgeAgreementsByEmployee(employee.getEmployeeId(), TypeOfPledgeAgreement.PERV);
        Calendar dateNow = new GregorianCalendar();
        Calendar firstDateCalendar = new GregorianCalendar(dateNow.get(Calendar.YEAR), dateNow.get(Calendar.MONTH), 1);
        Calendar secondDateCalendar = new GregorianCalendar(dateNow.get(Calendar.YEAR), dateNow.get(Calendar.MONTH), dateNow.getMaximum(Calendar.DAY_OF_MONTH));
        Date firstDate = new Date(firstDateCalendar.getTimeInMillis());
        Date secondDate = new Date(secondDateCalendar.getTimeInMillis());
        int countOfMonitoring = 0;
        for(PledgeAgreement pa : pledgeAgreementList)
            if(repositoryPledgeSubject.existsByPledgeAgreementsAndDateMonitoringBetween(pa, firstDate,secondDate))
                countOfMonitoring += 1;

        return countOfMonitoring;
    }

    public int countOfMonitoringOverdue(Employee employee){
        List<PledgeAgreement> pledgeAgreementList = getCurrentPledgeAgreementsByEmployee(employee.getEmployeeId(), TypeOfPledgeAgreement.PERV);
        Calendar dateNow = new GregorianCalendar();
        Calendar beforeDateCalendar = new GregorianCalendar(dateNow.get(Calendar.YEAR) - 1, dateNow.get(Calendar.MONTH) - 1, dateNow.getMaximum(Calendar.DAY_OF_MONTH));
        Date beforeDate = new Date(beforeDateCalendar.getTimeInMillis());
        int countOfMonitoring = 0;
        for(PledgeAgreement pa : pledgeAgreementList)
            if(repositoryPledgeSubject.existsByPledgeAgreementsAndDateMonitoringBefore(pa, beforeDate))
                countOfMonitoring += 1;

        return countOfMonitoring;
    }

    public List<PledgeAgreement> getPledgeAgreementWithMonitoringNotDone(Employee employee){
        List<PledgeAgreement> pledgeAgreementList = getCurrentPledgeAgreementsByEmployee(employee.getEmployeeId(), TypeOfPledgeAgreement.PERV);
        Calendar dateNow = new GregorianCalendar();
        Calendar firstDateCalendar = new GregorianCalendar(dateNow.get(Calendar.YEAR) - 1, dateNow.get(Calendar.MONTH), 1);
        Calendar secondDateCalendar = new GregorianCalendar(dateNow.get(Calendar.YEAR) - 1, dateNow.get(Calendar.MONTH), dateNow.getMaximum(Calendar.DAY_OF_MONTH));
        Date firstDate = new Date(firstDateCalendar.getTimeInMillis());
        Date secondDate = new Date(secondDateCalendar.getTimeInMillis());
        List<PledgeAgreement> pledgeAgreementListWithMonitoringNotDone = new ArrayList<>();
        for (PledgeAgreement pa : pledgeAgreementList)
            if(repositoryPledgeSubject.existsByPledgeAgreementsAndDateMonitoringBetween(pa, firstDate,secondDate))
                pledgeAgreementListWithMonitoringNotDone.add(pa);

        return pledgeAgreementListWithMonitoringNotDone;
    }

    public List<PledgeAgreement> getPledgeAgreementWithMonitoringIsDone(Employee employee){
        List<PledgeAgreement> pledgeAgreementList = getCurrentPledgeAgreementsByEmployee(employee.getEmployeeId(), TypeOfPledgeAgreement.PERV);
        Calendar dateNow = new GregorianCalendar();
        Calendar firstDateCalendar = new GregorianCalendar(dateNow.get(Calendar.YEAR), dateNow.get(Calendar.MONTH), 1);
        Calendar secondDateCalendar = new GregorianCalendar(dateNow.get(Calendar.YEAR), dateNow.get(Calendar.MONTH), dateNow.getMaximum(Calendar.DAY_OF_MONTH));
        Date firstDate = new Date(firstDateCalendar.getTimeInMillis());
        Date secondDate = new Date(secondDateCalendar.getTimeInMillis());
        List<PledgeAgreement> pledgeAgreementListWithMonitoringIsDone = new ArrayList<>();
        for (PledgeAgreement pa : pledgeAgreementList)
            if(repositoryPledgeSubject.existsByPledgeAgreementsAndDateMonitoringBetween(pa, firstDate,secondDate))
                pledgeAgreementListWithMonitoringIsDone.add(pa);

        return pledgeAgreementListWithMonitoringIsDone;
    }

    public List<PledgeAgreement> getPledgeAgreementWithMonitoringOverDue(Employee employee){
        List<PledgeAgreement> pledgeAgreementList = getCurrentPledgeAgreementsByEmployee(employee.getEmployeeId(), TypeOfPledgeAgreement.PERV);
        Calendar dateNow = new GregorianCalendar();
        Calendar beforeDateCalendar = new GregorianCalendar(dateNow.get(Calendar.YEAR) - 1, dateNow.get(Calendar.MONTH) - 1, dateNow.getMaximum(Calendar.DAY_OF_MONTH));
        Date beforeDate = new Date(beforeDateCalendar.getTimeInMillis());
        List<PledgeAgreement> pledgeAgreementListWithMonitoringOverdue = new ArrayList<>();
        for (PledgeAgreement pa : pledgeAgreementList)
            if(repositoryPledgeSubject.existsByPledgeAgreementsAndDateMonitoringBefore(pa, beforeDate))
                pledgeAgreementListWithMonitoringOverdue.add(pa);

        return pledgeAgreementListWithMonitoringOverdue;
    }

    public int countOfConclusionNotDone(Employee employee){
        List<PledgeAgreement> pledgeAgreementList = getCurrentPledgeAgreementsByEmployee(employee.getEmployeeId(), TypeOfPledgeAgreement.PERV);
        Calendar dateNow = new GregorianCalendar();
        Calendar firstDateCalendar = new GregorianCalendar(dateNow.get(Calendar.YEAR) - 1, dateNow.get(Calendar.MONTH), 1);
        Calendar secondDateCalendar = new GregorianCalendar(dateNow.get(Calendar.YEAR) - 1, dateNow.get(Calendar.MONTH), dateNow.getMaximum(Calendar.DAY_OF_MONTH));
        Date firstDate = new Date(firstDateCalendar.getTimeInMillis());
        Date secondDate = new Date(secondDateCalendar.getTimeInMillis());
        int countOfConclusion = 0;
        for(PledgeAgreement pa : pledgeAgreementList)
            if(repositoryPledgeSubject.existsByPledgeAgreementsAndDateConclusionBetween(pa, firstDate, secondDate))
                countOfConclusion += 1;

        return countOfConclusion;
    }

    public int countOfConclusionIsDone(Employee employee){
        List<PledgeAgreement> pledgeAgreementList = getCurrentPledgeAgreementsByEmployee(employee.getEmployeeId(), TypeOfPledgeAgreement.PERV);
        Calendar dateNow = new GregorianCalendar();
        Calendar firstDateCalendar = new GregorianCalendar(dateNow.get(Calendar.YEAR), dateNow.get(Calendar.MONTH), 1);
        Calendar secondDateCalendar = new GregorianCalendar(dateNow.get(Calendar.YEAR), dateNow.get(Calendar.MONTH), dateNow.getMaximum(Calendar.DAY_OF_MONTH));
        Date firstDate = new Date(firstDateCalendar.getTimeInMillis());
        Date secondDate = new Date(secondDateCalendar.getTimeInMillis());
        int countOfConclusion = 0;
        for(PledgeAgreement pa : pledgeAgreementList)
            if(repositoryPledgeSubject.existsByPledgeAgreementsAndDateConclusionBetween(pa, firstDate, secondDate))
                countOfConclusion += 1;

        return countOfConclusion;
    }

    public int countOfConclusionOverdue(Employee employee){
        List<PledgeAgreement> pledgeAgreementList = getCurrentPledgeAgreementsByEmployee(employee.getEmployeeId(), TypeOfPledgeAgreement.PERV);
        Calendar dateNow = new GregorianCalendar();
        Calendar beforeDateCalendar = new GregorianCalendar(dateNow.get(Calendar.YEAR) - 1, dateNow.get(Calendar.MONTH) - 1, dateNow.getMaximum(Calendar.DAY_OF_MONTH));
        Date beforeDate = new Date(beforeDateCalendar.getTimeInMillis());
        int countOfConclusion = 0;
        for(PledgeAgreement pa : pledgeAgreementList)
            if(repositoryPledgeSubject.existsByPledgeAgreementsAndDateConclusionBefore(pa, beforeDate))
                countOfConclusion += 1;

        return countOfConclusion;
    }

    public List<PledgeAgreement> getPledgeAgreementWithConclusionNotDone(Employee employee){
        List<PledgeAgreement> pledgeAgreementList = getCurrentPledgeAgreementsByEmployee(employee.getEmployeeId(), TypeOfPledgeAgreement.PERV);
        Calendar dateNow = new GregorianCalendar();
        Calendar firstDateCalendar = new GregorianCalendar(dateNow.get(Calendar.YEAR) - 1, dateNow.get(Calendar.MONTH), 1);
        Calendar secondDateCalendar = new GregorianCalendar(dateNow.get(Calendar.YEAR) - 1, dateNow.get(Calendar.MONTH), dateNow.getMaximum(Calendar.DAY_OF_MONTH));
        Date firstDate = new Date(firstDateCalendar.getTimeInMillis());
        Date secondDate = new Date(secondDateCalendar.getTimeInMillis());
        List<PledgeAgreement> pledgeAgreementListWithConclusionNotDone = new ArrayList<>();
        for (PledgeAgreement pa : pledgeAgreementList)
            if(repositoryPledgeSubject.existsByPledgeAgreementsAndDateConclusionBetween(pa, firstDate, secondDate))
                pledgeAgreementListWithConclusionNotDone.add(pa);

        return pledgeAgreementListWithConclusionNotDone;
    }

    public List<PledgeAgreement> getPledgeAgreementWithConclusionIsDone(Employee employee){
        List<PledgeAgreement> pledgeAgreementList = getCurrentPledgeAgreementsByEmployee(employee.getEmployeeId(), TypeOfPledgeAgreement.PERV);
        Calendar dateNow = new GregorianCalendar();
        Calendar firstDateCalendar = new GregorianCalendar(dateNow.get(Calendar.YEAR), dateNow.get(Calendar.MONTH), 1);
        Calendar secondDateCalendar = new GregorianCalendar(dateNow.get(Calendar.YEAR), dateNow.get(Calendar.MONTH), dateNow.getMaximum(Calendar.DAY_OF_MONTH));
        Date firstDate = new Date(firstDateCalendar.getTimeInMillis());
        Date secondDate = new Date(secondDateCalendar.getTimeInMillis());
        List<PledgeAgreement> pledgeAgreementListWithConclusionIsDone = new ArrayList<>();
        for (PledgeAgreement pa : pledgeAgreementList)
            if(repositoryPledgeSubject.existsByPledgeAgreementsAndDateConclusionBetween(pa, firstDate, secondDate))
                pledgeAgreementListWithConclusionIsDone.add(pa);

        return pledgeAgreementListWithConclusionIsDone;
    }

    public List<PledgeAgreement> getPledgeAgreementWithConclusionOverDue(Employee employee){
        List<PledgeAgreement> pledgeAgreementList = getCurrentPledgeAgreementsByEmployee(employee.getEmployeeId(), TypeOfPledgeAgreement.PERV);
        Calendar dateNow = new GregorianCalendar();
        Calendar beforeDateCalendar = new GregorianCalendar(dateNow.get(Calendar.YEAR) - 1, dateNow.get(Calendar.MONTH) - 1, dateNow.getMaximum(Calendar.DAY_OF_MONTH));
        Date beforeDate = new Date(beforeDateCalendar.getTimeInMillis());
        List<PledgeAgreement> pledgeAgreementListWithConclusionOverdue = new ArrayList<>();
        for (PledgeAgreement pa : pledgeAgreementList)
            if(repositoryPledgeSubject.existsByPledgeAgreementsAndDateConclusionBefore(pa, beforeDate))
                pledgeAgreementListWithConclusionOverdue.add(pa);

        return pledgeAgreementListWithConclusionOverdue;
    }

    public List<PledgeAgreement> getPledgeAgreementFromSearch(Map<String, String> searchParam) throws ReflectiveOperationException{

        SpecificationBuilder builder = new SpecificationBuilderImpl();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy-MM-dd");

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

                }else if(field.getType() == Date.class && !searchParam.get(field.getName()).isEmpty()){
                    try {
                        Date date = simpleDateFormat.parse(searchParam.get(field.getName()));
                        SearchCriteria searchCriteria = SearchCriteria.builder()
                                .key(field.getName())
                                .value(date)
                                .operation(Operations.valueOf(searchParam.get(field.getName() + "Option")))
                                .predicate(false)
                                .build();
                        builder.with(searchCriteria);
                    } catch (ParseException e) {
                        return Collections.emptyList();
                    }
                }
            }
        }

        Specification specification = builder.build();
        return repositoryPledgeAgreement.findAll(specification);
    }

    public int countOfCurrentPledgeAgreementsByPledgor(Client client){
        return repositoryPledgeAgreement.countAllByClientAndStatusPAEquals(client, StatusOfAgreement.OPEN);
    }

    public List<PledgeAgreement> getCurrentPledgeAgreementsByPledgor(Client client){
        return repositoryPledgeAgreement.findByClientAndStatusPA(client, StatusOfAgreement.OPEN);
    }

    public int countOfClosedPledgeAgreementsByPledgor(Client client){
        return repositoryPledgeAgreement.countAllByClientAndStatusPAEquals(client, StatusOfAgreement.CLOSED);
    }

    public List<PledgeAgreement> getClosedPledgeAgreementsByPledgor(Client client){
        return repositoryPledgeAgreement.findByClientAndStatusPA(client, StatusOfAgreement.CLOSED);
    }

    public int countOfCurrentPledgeAgreementsByLoanAgreement(LoanAgreement loanAgreement){
        return repositoryPledgeAgreement.countAllByLoanAgreementsAndStatusPAEquals(loanAgreement,StatusOfAgreement.OPEN);
    }

    public List<PledgeAgreement> getCurrentPledgeAgreementsByLoanAgreement(LoanAgreement loanAgreement){
        return repositoryPledgeAgreement.findByLoanAgreementsAndStatusPAEquals(loanAgreement, StatusOfAgreement.OPEN);
    }

    public int countOfClosedPledgeAgreementsByLoanAgreement(LoanAgreement loanAgreement){
        return repositoryPledgeAgreement.countAllByLoanAgreementsAndStatusPAEquals(loanAgreement, StatusOfAgreement.CLOSED);
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
