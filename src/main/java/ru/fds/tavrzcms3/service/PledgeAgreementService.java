package ru.fds.tavrzcms3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fds.tavrzcms3.domain.*;
import ru.fds.tavrzcms3.repository.*;
import ru.fds.tavrzcms3.specification.SpecificationBuilder;
import ru.fds.tavrzcms3.specification.SpecificationBuilderImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PledgeAgreementService {

    @Autowired
    RepositoryPledgeAgreement repositoryPledgeAgreement;
    @Autowired
    RepositoryPledgeSubject repositoryPledgeSubject;
    @Autowired
    RepositoryLoanAgreement repositoryLoanAgreement;
    @Autowired
    RepositoryClientLegalEntity repositoryClientLegalEntity;
    @Autowired
    RepositoryClientIndividual repositoryClientIndividual;
    @Autowired
    EmployeeService employeeService;
    @Autowired
    ClientService clientService;

    public List<PledgeAgreement> getPledgeAgreementsByNumPA(String numPA){
        return repositoryPledgeAgreement.findAllByNumPAContainingIgnoreCase(numPA);
    }

    public List<Date> getDatesOfConclusion(PledgeAgreement pledgeAgreement){
        return repositoryPledgeAgreement.getDatesOfConclusion(pledgeAgreement.getPledgeAgreementId());
    }

    public List<Date> getDatesOfMonitoring(PledgeAgreement pledgeAgreement){
        return repositoryPledgeAgreement.getDatesOMonitorings(pledgeAgreement.getPledgeAgreementId());
    }

    public List<String> getResultsOfMonitoring(PledgeAgreement pledgeAgreement){
        return repositoryPledgeAgreement.getResultsOfMonitoring(pledgeAgreement.getPledgeAgreementId());
    }

    public Optional<PledgeAgreement> getPledgeAgreement(long pledgeAgreementId){
        return repositoryPledgeAgreement.findById(pledgeAgreementId);
    }

    public List<String> getTypeOfCollateral(PledgeAgreement pledgeAgreement){
        return  repositoryPledgeAgreement.getTypeOfCollateral(pledgeAgreement.getPledgeAgreementId());
    }

    public List<PledgeAgreement> getCurrentPledgeAgreementsForEmployee(Employee employee, String pervPosl){
        return repositoryPledgeAgreement.getCurrentPledgeAgreementsForEmployee(employee.getEmployeeId(), pervPosl);
    }

    public Page<PledgeAgreement> getCurrentPledgeAgreementsForEmployee(Employee employee, Pageable pageable){
        List<Client> pledgors = clientService.getClientByEmployee(employee);
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.ASC,"client");

        return repositoryPledgeAgreement.findByClientInAndStatusPAEquals(pledgors, "открыт", pageable);
    }

    public Page<PledgeAgreement> getCurrentPledgeAgreementsForEmployee(Employee employee, String pervPosl, Pageable pageable){
        List<Client> pledgors = clientService.getClientByEmployee(employee);
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.ASC,"client");

        return repositoryPledgeAgreement.findByClientInAndPervPoslEqualsAndStatusPAEquals(pledgors,  pervPosl, "открыт", pageable);
    }

    public int countOfCurrentPledgeAgreementForEmployee(Employee employee){
        List<Client> pledgors = clientService.getClientByEmployee(employee);

        return repositoryPledgeAgreement.countAllByClientInAndStatusPAEquals(pledgors, "открыт");
    }

    public int countOfCurrentPledgeAgreementForEmployee(Employee employee, String pervPosl){
        List<Client> pledgors = clientService.getClientByEmployee(employee);

        return repositoryPledgeAgreement.countAllByClientInAndPervPoslEqualsAndStatusPAEquals(pledgors, pervPosl, "открыт");
    }

    public int countOfMonitoringNotDone(Employee employee){
        List<PledgeAgreement> pledgeAgreementList = getCurrentPledgeAgreementsForEmployee(employee, "перв");
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
        List<PledgeAgreement> pledgeAgreementList = getCurrentPledgeAgreementsForEmployee(employee, "перв");
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
        List<PledgeAgreement> pledgeAgreementList = getCurrentPledgeAgreementsForEmployee(employee, "перв");
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
        List<PledgeAgreement> pledgeAgreementList = getCurrentPledgeAgreementsForEmployee(employee, "перв");
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
        List<PledgeAgreement> pledgeAgreementList = getCurrentPledgeAgreementsForEmployee(employee, "перв");
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
        List<PledgeAgreement> pledgeAgreementList = getCurrentPledgeAgreementsForEmployee(employee, "перв");
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
        List<PledgeAgreement> pledgeAgreementList = getCurrentPledgeAgreementsForEmployee(employee, "перв");
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
        List<PledgeAgreement> pledgeAgreementList = getCurrentPledgeAgreementsForEmployee(employee, "перв");
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
        List<PledgeAgreement> pledgeAgreementList = getCurrentPledgeAgreementsForEmployee(employee, "перв");
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
        List<PledgeAgreement> pledgeAgreementList = getCurrentPledgeAgreementsForEmployee(employee, "перв");
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
        List<PledgeAgreement> pledgeAgreementList = getCurrentPledgeAgreementsForEmployee(employee, "перв");
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
        List<PledgeAgreement> pledgeAgreementList = getCurrentPledgeAgreementsForEmployee(employee, "перв");
        Calendar dateNow = new GregorianCalendar();
        Calendar beforeDateCalendar = new GregorianCalendar(dateNow.get(Calendar.YEAR) - 1, dateNow.get(Calendar.MONTH) - 1, dateNow.getMaximum(Calendar.DAY_OF_MONTH));
        Date beforeDate = new Date(beforeDateCalendar.getTimeInMillis());
        List<PledgeAgreement> pledgeAgreementListWithConclusionOverdue = new ArrayList<>();
        for (PledgeAgreement pa : pledgeAgreementList)
            if(repositoryPledgeSubject.existsByPledgeAgreementsAndDateConclusionBefore(pa, beforeDate))
                pledgeAgreementListWithConclusionOverdue.add(pa);

        return pledgeAgreementListWithConclusionOverdue;
    }

    public Page<PledgeAgreement> getPledgeAgreementFromSearch(Map<String, String> searchParam){
        SpecificationBuilder builder = new SpecificationBuilderImpl();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy-MM-dd");

        if(!searchParam.get("numPA").isEmpty())
            builder.with("numPA", ":", searchParam.get("numPA"), false);
        if(!searchParam.get("client").isEmpty()) {
            if (searchParam.get("clientOption").equals("юл")) {
                List<ClientLegalEntity> pledgors = repositoryClientLegalEntity.findByNameContainingIgnoreCase(searchParam.get("client"));
                if(pledgors.isEmpty())
                    builder.with("client", ":", null, false);
                else if(pledgors.size() == 1)
                    builder.with("client", ":", pledgors.get(0), false);
                else if(pledgors.size() > 1)
                    for(ClientLegalEntity cle : pledgors)
                        builder.with("client", ":", cle, true);
            }
            else{
                String[] words = searchParam.get("client").split("\\s");
                List<ClientIndividual> pledgors = new ArrayList<>();

                if(words.length == 1)
                    pledgors = repositoryClientIndividual.findBySurnameContainingIgnoreCase(words[0]);
                else if(words.length > 1)
                    pledgors = repositoryClientIndividual.findBySurnameContainingIgnoreCaseAndNameContainingIgnoreCase(words[0], words[1]);

                if(pledgors.isEmpty())
                    builder.with("client", ":", null, false);
                else if(pledgors.size() == 1)
                    builder.with("client", ":", pledgors.get(0), false);
                else if(pledgors.size() > 1)
                    for(ClientIndividual ci : pledgors)
                        builder.with("client", ":", ci, true);
            }
        }
        if(!searchParam.get("dateBeginPA").isEmpty()){
            try {
                Date date = simpleDateFormat.parse(searchParam.get("dateBeginPA"));
                builder.with("dateBeginPA", searchParam.get("dateBeginPAOption"), date, false);
            }catch (ParseException e){
                System.out.println("Не верный фортат dateBeginPA");
            }
        }
        if(!searchParam.get("dateEndPA").isEmpty()){
            try {
                Date date = simpleDateFormat.parse(searchParam.get("dateEndPA"));
                builder.with("dateEndPA", searchParam.get("dateEndPAOption"), date, false);
            }catch (ParseException e){
                System.out.println("Не верный фортат dateEndPA");
            }
        }
        if(!searchParam.get("pervPslOption").equals("all"))
            builder.with("pervPosl", ":", searchParam.get("pervPslOption"), false);



        if(!searchParam.get("rsDZ").isEmpty())
            builder.with("rsDz", searchParam.get("rsDZOption"), searchParam.get("rsDZ"), false);



        if(!searchParam.get("rsZZ").isEmpty())
            builder.with("rsZz", searchParam.get("rsZZOption"), searchParam.get("rsZZ"), false);



        if(!searchParam.get("zsDZ").isEmpty())
            builder.with("zsDz", searchParam.get("zsDZOption"), searchParam.get("zsDZ"), false);



        if(!searchParam.get("zsZZ").isEmpty())
            builder.with("zsZz", searchParam.get("zsZZOption"), searchParam.get("zsZZ"), false);



        if(!searchParam.get("ss").isEmpty())
            builder.with("ss", searchParam.get("ssOption"), searchParam.get("ss"), false);



        builder.with("statusPA", ":", searchParam.get("statusPA"), false);

        Specification<PledgeAgreement> spec = builder.build();

        int currentPage = Integer.parseInt(searchParam.get("page"));
        int pageSize = Integer.parseInt(searchParam.get("size"));
        Pageable pageable = PageRequest.of(currentPage, pageSize);

        return repositoryPledgeAgreement.findAll(spec, pageable);

    }

    public int countOfCurrentPledgeAgreementsByPledgor(Client client){
        return repositoryPledgeAgreement.countAllByClientAndStatusPAEquals(client, "открыт");
    }

    public List<PledgeAgreement> getCurrentPledgeAgreementsByPledgor(Client client){
        return repositoryPledgeAgreement.findByClientAndStatusPA(client, "открыт");
    }

    public int countOfClosedPledgeAgreementsByPledgor(Client client){
        return repositoryPledgeAgreement.countAllByClientAndStatusPAEquals(client, "закрыт");
    }

    public List<PledgeAgreement> getClosedPledgeAgreementsByPledgor(Client client){
        return repositoryPledgeAgreement.findByClientAndStatusPA(client, "закрыт");
    }

    public int countOfCurrentPledgeAgreementsByLoanAgreement(LoanAgreement loanAgreement){
        return repositoryPledgeAgreement.countAllByLoanAgreementsAndStatusPAEquals(loanAgreement,"открыт");
    }

    public List<PledgeAgreement> getCurrentPledgeAgreementsByLoanAgreement(LoanAgreement loanAgreement){
        return repositoryPledgeAgreement.findByLoanAgreementsAndStatusPAEquals(loanAgreement, "открыт");
    }

    public int countOfClosedPledgeAgreementsByLoanAgreement(LoanAgreement loanAgreement){
        return repositoryPledgeAgreement.countAllByLoanAgreementsAndStatusPAEquals(loanAgreement, "закрыт");
    }

    public List<PledgeAgreement> getClosedPledgeAgreementsByLoanAgreement(LoanAgreement loanAgreement){
        return repositoryPledgeAgreement.findByLoanAgreementsAndStatusPAEquals(loanAgreement,"закрыт");
    }

    public List<PledgeAgreement> getAllPledgeAgreementsByLoanAgreement(LoanAgreement loanAgreement){
        return repositoryPledgeAgreement.findByLoanAgreements(loanAgreement);
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
