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
//    @Autowired
//    RepositoryClient repositoryClient;
    @Autowired
    RepositoryClientLegalEntity repositoryClientLegalEntity;
    @Autowired
    RepositoryClientIndividual repositoryClientIndividual;
    @Autowired
    EmployeeService employeeService;
    @Autowired
    ClientService clientService;

    public List<PledgeAgreement> getPledgeAgreementsBynumPA(String numPA){
        return repositoryPledgeAgreement.findAllByNumPAContainingIgnoreCase(numPA);
    }

    public Set<Date> getDatesOfConclusion(long pledgeAgreementId){
        PledgeAgreement pledgeAgreement = repositoryPledgeAgreement.getOne(pledgeAgreementId);
        List<PledgeSubject> pledgeSubjects = repositoryPledgeSubject.findByPledgeAgreements(pledgeAgreement);
        Set<Date> dates = new LinkedHashSet<>();
        for(PledgeSubject ps : pledgeSubjects)
            dates.add(ps.getDateConclusion());

        return  dates;
    }

    public Set<Date> getDatesOfMonitoring(long pledgeAgreementId){
        PledgeAgreement pledgeAgreement = repositoryPledgeAgreement.getOne(pledgeAgreementId);
        List<PledgeSubject> pledgeSubjects = repositoryPledgeSubject.findByPledgeAgreements(pledgeAgreement);
        Set<Date> dates = new LinkedHashSet<>();
        for(PledgeSubject ps : pledgeSubjects)
            dates.add(ps.getDateMonitoring());

        return  dates;
    }

    public Set<String> getResultsOfMonitoring(long pledgeAgreementId){
        PledgeAgreement pledgeAgreement = repositoryPledgeAgreement.getOne(pledgeAgreementId);
        List<PledgeSubject> pledgeSubjects = repositoryPledgeSubject.findByPledgeAgreements(pledgeAgreement);
        Set<String> results = new LinkedHashSet<>();
        for(PledgeSubject ps : pledgeSubjects)
            results.add(ps.getStatusMonitoring());

        return  results;
    }

    public PledgeAgreement getPledgeAgreement(long pledgeAgreementId){
        return repositoryPledgeAgreement.getOne(pledgeAgreementId);
    }

    public Set<String> getTypeOfCollateral(long pledgeAgreementId){
        PledgeAgreement pledgeAgreement = repositoryPledgeAgreement.getOne(pledgeAgreementId);
        List<PledgeSubject> pledgeSubjects = repositoryPledgeSubject.findByPledgeAgreements(pledgeAgreement);
        Set<String> typeOfCollateralSet = new LinkedHashSet<>();
        for(PledgeSubject ps : pledgeSubjects)
            typeOfCollateralSet.add(ps.getTypeOfCollateral());

        return  typeOfCollateralSet;
    }

    public int countOfCurrentLoanAgreementsForPladgeAgreement(long pledgeAgreementId){
        return repositoryLoanAgreement.countAllByPledgeAgreementsAndStatusLAEquals(repositoryPledgeAgreement.findByPledgeAgreementId(pledgeAgreementId), "открыт");
    }

    public List<LoanAgreement> getCurrentLoanAgreementsForPledgeAgreement(long pledgeAgreementId){
        return repositoryLoanAgreement.findByPledgeAgreementsAndStatusLAEquals(repositoryPledgeAgreement.findByPledgeAgreementId(pledgeAgreementId), "открыт");
    }

    public int countOfClosedLoanAgreementsForPledgeAgreement(long pledgeAgreementId){
        return repositoryLoanAgreement.countAllByPledgeAgreementsAndStatusLAEquals(repositoryPledgeAgreement.findByPledgeAgreementId(pledgeAgreementId), "закрыт");
    }

    public List<LoanAgreement> getClosedLoanAgreementsForPledgeAgreement(long pledgeAgreementId){
        return repositoryLoanAgreement.findByPledgeAgreementsAndStatusLAEquals(repositoryPledgeAgreement.findByPledgeAgreementId(pledgeAgreementId), "закрыт");
    }

    public List<PledgeAgreement> getCurrentPledgeAgreementsForEmployee(Employee employee, String pervPosl){
        List<Client> pledgors = clientService.getClientByEmployee(employee);
        Sort sort = new Sort(Sort.Direction.ASC,"pledgor");
        if(!pervPosl.isEmpty())
            return repositoryPledgeAgreement.findByPledgorInAndPervPoslEqualsAndStatusPAEquals(pledgors,  pervPosl, "открыт", sort);
        else
            return repositoryPledgeAgreement.findByPledgorInAndStatusPAEquals(pledgors, "открыт", sort);
    }

    public Page<PledgeAgreement> getCurrentPledgeAgreementsForEmployee(long employeeId, String pervPosl, Pageable pageable){
        Employee employee = employeeService.getEmployee(employeeId);
        List<Client> pledgors = clientService.getClientByEmployee(employee);
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.ASC,"pledgor");
        if(!pervPosl.isEmpty())
            return repositoryPledgeAgreement.findByPledgorInAndPervPoslEqualsAndStatusPAEquals(pledgors,  pervPosl, "открыт", pageable);
        else
            return repositoryPledgeAgreement.findByPledgorInAndStatusPAEquals(pledgors, "открыт", pageable);
    }

    public int countOfCurrentPledgeAgreementForEmployee(Employee employee){
        List<Client> pledgors = clientService.getClientByEmployee(employee);

        return repositoryPledgeAgreement.countAllByPledgorInAndStatusPAEquals(pledgors, "открыт");
    }

    public int countOfCurrentPledgeAgreementForEmployee(Employee employee, String pervPosl){
        List<Client> pledgors = clientService.getClientByEmployee(employee);

        return repositoryPledgeAgreement.countAllByPledgorInAndPervPoslEqualsAndStatusPAEquals(pledgors, pervPosl, "открыт");
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
        if(!searchParam.get("pledgor").isEmpty()) {
            if (searchParam.get("pledgorOption").equals("юл")) {
                List<ClientLegalEntity> pledgors = repositoryClientLegalEntity.findByNameContainingIgnoreCase(searchParam.get("pledgor"));
                if(pledgors.isEmpty())
                    builder.with("pledgor", ":", null, false);
                else if(pledgors.size() == 1)
                    builder.with("pledgor", ":", pledgors.get(0), false);
                else if(pledgors.size() > 1)
                    for(ClientLegalEntity cle : pledgors)
                        builder.with("pledgor", ":", cle, true);
            }
            else{
                String[] words = searchParam.get("pledgor").split("\\s");
                List<ClientIndividual> pledgors = new ArrayList<>();

                if(words.length == 1)
                    pledgors = repositoryClientIndividual.findBySurnameContainingIgnoreCase(words[0]);
                else if(words.length > 1)
                    pledgors = repositoryClientIndividual.findBySurnameContainingIgnoreCaseAndNameContainingIgnoreCase(words[0], words[1]);

                if(pledgors.isEmpty())
                    builder.with("pledgor", ":", null, false);
                else if(pledgors.size() == 1)
                    builder.with("pledgor", ":", pledgors.get(0), false);
                else if(pledgors.size() > 1)
                    for(ClientIndividual ci : pledgors)
                        builder.with("pledgor", ":", ci, true);
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

    @Transactional
    public PledgeAgreement updateInsertPledgeAgreement(PledgeAgreement pledgeAgreement){
        return repositoryPledgeAgreement.save(pledgeAgreement);
    }

}
