package ru.fds.tavrzcms3.Service;

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
import ru.fds.tavrzcms3.specification.PledgeAgreementSpecificationsBuilder;

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
    RepositoryClient repositoryClient;
    @Autowired
    RepositoryClientLegalEntity repositoryClientLegalEntity;
    @Autowired
    RepositoryClientIndividual repositoryClientIndividual;
    @Autowired
    RepositoryEmployee repositoryEmployee;

    public double getRsDz(long pledgeAgreementId){
        PledgeAgreement pledgeAgreement = repositoryPledgeAgreement.getOne(pledgeAgreementId);
        List<PledgeSubject> pledgeSubjects = repositoryPledgeSubject.findByPledgeAgreements(pledgeAgreement);
        double totalSum = 0;
        for (PledgeSubject ps : pledgeSubjects)
            totalSum += ps.getRsDz();

        return  totalSum;
    }

    public double getRsZz(long pledgeAgreementId){
        PledgeAgreement pledgeAgreement = repositoryPledgeAgreement.getOne(pledgeAgreementId);
        List<PledgeSubject> pledgeSubjects = repositoryPledgeSubject.findByPledgeAgreements(pledgeAgreement);
        double totalSum = 0;
        for(PledgeSubject ps : pledgeSubjects)
            totalSum += ps.getRsZz();

        return totalSum;
    }

    public double getZsDz(long pledgeAgreementId){
        PledgeAgreement pledgeAgreement = repositoryPledgeAgreement.getOne(pledgeAgreementId);
        List<PledgeSubject> pledgeSubjects = repositoryPledgeSubject.findByPledgeAgreements(pledgeAgreement);
        double totalSum = 0;
        for(PledgeSubject ps : pledgeSubjects)
            totalSum += ps.getZsDz();

        return totalSum;
    }

    public double getZsZz(long pledgeAgreementId){
        PledgeAgreement pledgeAgreement = repositoryPledgeAgreement.getOne(pledgeAgreementId);
        List<PledgeSubject> pledgeSubjects = repositoryPledgeSubject.findByPledgeAgreements(pledgeAgreement);
        double totalSum = 0;
        for(PledgeSubject ps : pledgeSubjects)
            totalSum += ps.getZsZz();

        return totalSum;
    }

    public double getSs(long pledgeAgreementId){
        PledgeAgreement pledgeAgreement = repositoryPledgeAgreement.getOne(pledgeAgreementId);
        List<PledgeSubject> pledgeSubjects = repositoryPledgeSubject.findByPledgeAgreements(pledgeAgreement);
        double totalSum = 0;
        for(PledgeSubject ps : pledgeSubjects)
            totalSum += ps.getSs();

        return totalSum;
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

    public  List<PledgeAgreement> getCurrentPledgeAgreementsForPledgor(long pledgorId, String pervPosl){
        return repositoryPledgeAgreement.findByPledgorAndPervPoslEqualsAndStatusPAEquals(repositoryClient.getOne(pledgorId), pervPosl, "открыт");
    }

    public List<PledgeAgreement> getCurrentPledgeAgreementsForEmployee(long employeeId, String pervPosl){
        Employee employee = repositoryEmployee.getOne(employeeId);
        List<Client> pledgors = repositoryClient.findByEmployee(employee);
        Sort sort = new Sort(Sort.Direction.ASC,"pledgor");
        if(!pervPosl.isEmpty())
            return repositoryPledgeAgreement.findByPledgorInAndPervPoslEqualsAndStatusPAEquals(pledgors,  pervPosl, "открыт", sort);
        else
            return repositoryPledgeAgreement.findByPledgorInAndStatusPAEquals(pledgors, "открыт", sort);
    }

    public Page<PledgeAgreement> getCurrentPledgeAgreementsForEmployee(long employeeId, String pervPosl, Pageable pageable){
        Employee employee = repositoryEmployee.getOne(employeeId);
        List<Client> pledgors = repositoryClient.findByEmployee(employee);
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.ASC,"pledgor");
        if(!pervPosl.isEmpty())
            return repositoryPledgeAgreement.findByPledgorInAndPervPoslEqualsAndStatusPAEquals(pledgors,  pervPosl, "открыт", pageable);
        else
            return repositoryPledgeAgreement.findByPledgorInAndStatusPAEquals(pledgors, "открыт", pageable);
    }

    public int countOfCurrentPledgeAgreementForEmployee(long employeeId, String pervPosl){
        Employee employee = repositoryEmployee.getOne(employeeId);
        List<Client> pledgors = repositoryClient.findByEmployee(employee);
        if(!pervPosl.isEmpty())
            return repositoryPledgeAgreement.countAllByPledgorInAndPervPoslEqualsAndStatusPAEquals(pledgors, pervPosl, "открыт");
        else
            return repositoryPledgeAgreement.countAllByPledgorInAndStatusPAEquals(pledgors, "открыт");
    }

    public int countOfLoanAgreementsForEmployee(long employeeId){
        Employee employee = repositoryEmployee.getOne(employeeId);
        List<Client> loaners = repositoryClient.findByEmployee(employee);
        return repositoryLoanAgreement.countAllByLoanerInAndStatusLAEquals(loaners, "открыт");
    }

    public int countOfMonitoringNotDone(long employeeId){
        List<PledgeAgreement> pledgeAgreementList = getCurrentPledgeAgreementsForEmployee(employeeId, "перв");
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

    public int countOfMonitoringIsDone(long employeeId){
        List<PledgeAgreement> pledgeAgreementList = getCurrentPledgeAgreementsForEmployee(employeeId, "перв");
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

    public int countOfMonitoringOverdue(long employeeId){
        List<PledgeAgreement> pledgeAgreementList = getCurrentPledgeAgreementsForEmployee(employeeId, "перв");
        Calendar dateNow = new GregorianCalendar();
        Calendar beforeDateCalendar = new GregorianCalendar(dateNow.get(Calendar.YEAR) - 1, dateNow.get(Calendar.MONTH) - 1, dateNow.getMaximum(Calendar.DAY_OF_MONTH));
        Date beforeDate = new Date(beforeDateCalendar.getTimeInMillis());
        int countOfMonitoring = 0;
        for(PledgeAgreement pa : pledgeAgreementList)
            if(repositoryPledgeSubject.existsByPledgeAgreementsAndDateMonitoringBefore(pa, beforeDate))
                countOfMonitoring += 1;

        return countOfMonitoring;
    }

    public List<PledgeAgreement> getPledgeAgreementWithMonitoringNotDone(long employeeId){
        List<PledgeAgreement> pledgeAgreementList = getCurrentPledgeAgreementsForEmployee(employeeId, "перв");
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

    public List<PledgeAgreement> getPledgeAgreementWithMonitoringIsDone(long employeeId){
        List<PledgeAgreement> pledgeAgreementList = getCurrentPledgeAgreementsForEmployee(employeeId, "перв");
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

    public List<PledgeAgreement> getPledgeAgreementWithMonitoringOverDue(long employeeId){
        List<PledgeAgreement> pledgeAgreementList = getCurrentPledgeAgreementsForEmployee(employeeId, "перв");
        Calendar dateNow = new GregorianCalendar();
        Calendar beforeDateCalendar = new GregorianCalendar(dateNow.get(Calendar.YEAR) - 1, dateNow.get(Calendar.MONTH) - 1, dateNow.getMaximum(Calendar.DAY_OF_MONTH));
        Date beforeDate = new Date(beforeDateCalendar.getTimeInMillis());
        List<PledgeAgreement> pledgeAgreementListWithMonitoringOverdue = new ArrayList<>();
        for (PledgeAgreement pa : pledgeAgreementList)
            if(repositoryPledgeSubject.existsByPledgeAgreementsAndDateMonitoringBefore(pa, beforeDate))
                pledgeAgreementListWithMonitoringOverdue.add(pa);

        return pledgeAgreementListWithMonitoringOverdue;
    }

    public int countOfConclusionNotDone(long employeeId){
        List<PledgeAgreement> pledgeAgreementList = getCurrentPledgeAgreementsForEmployee(employeeId, "перв");
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

    public int countOfConclusionIsDone(long employeeId){
        List<PledgeAgreement> pledgeAgreementList = getCurrentPledgeAgreementsForEmployee(employeeId, "перв");
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

    public int countOfConclusionOverdue(long employeeId){
        List<PledgeAgreement> pledgeAgreementList = getCurrentPledgeAgreementsForEmployee(employeeId, "перв");
        Calendar dateNow = new GregorianCalendar();
        Calendar beforeDateCalendar = new GregorianCalendar(dateNow.get(Calendar.YEAR) - 1, dateNow.get(Calendar.MONTH) - 1, dateNow.getMaximum(Calendar.DAY_OF_MONTH));
        Date beforeDate = new Date(beforeDateCalendar.getTimeInMillis());
        int countOfConclusion = 0;
        for(PledgeAgreement pa : pledgeAgreementList)
            if(repositoryPledgeSubject.existsByPledgeAgreementsAndDateConclusionBefore(pa, beforeDate))
                countOfConclusion += 1;

        return countOfConclusion;
    }

    public List<PledgeAgreement> getPledgeAgreementWithConclusionNotDone(long employeeId){
        List<PledgeAgreement> pledgeAgreementList = getCurrentPledgeAgreementsForEmployee(employeeId, "перв");
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

    public List<PledgeAgreement> getPledgeAgreementWithConclusionIsDone(long employeeId){
        List<PledgeAgreement> pledgeAgreementList = getCurrentPledgeAgreementsForEmployee(employeeId, "перв");
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

    public List<PledgeAgreement> getPledgeAgreementWithConclusionOverDue(long employeeId){
        List<PledgeAgreement> pledgeAgreementList = getCurrentPledgeAgreementsForEmployee(employeeId, "перв");
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
        PledgeAgreementSpecificationsBuilder builder = new PledgeAgreementSpecificationsBuilder();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy-MM-dd");

        if(!searchParam.get("numPA").isEmpty())
            builder.with("numPA", ":", searchParam.get("numPA"), false);


        if(!searchParam.get("pledgor").isEmpty()) {
            if (searchParam.get("pledgorOption").equals("юл")) {
                List<ClientLegalEntity> pledgors = repositoryClientLegalEntity.findByNameContainingIgnoreCase(searchParam.get("pledgor"));
                if(pledgors.size() == 1) {
                    builder.with("pledgor", ":", pledgors.get(0), false);
                }
                else if(pledgors.size() > 1){
                    for(ClientLegalEntity cle : pledgors) {
                        builder.with("pledgor", ":", cle, true);
                    }
                }
            }
            else{
                String[] words = searchParam.get("pledgor").split("\\s");
                if(words.length == 1){
                    List<ClientIndividual> pledgors = repositoryClientIndividual.findBySurnameContainingIgnoreCase(words[0]);

                    if(pledgors.size() == 1)
                        builder.with("pledgor", ":", pledgors.get(0), false);
                    else if(pledgors.size() > 1){
                        for(ClientIndividual ci : pledgors) {
                            builder.with("pledgor", ":", ci, true);
                        }
                    }
                }
                else if(words.length == 2){
                    List<ClientIndividual> pledgors = repositoryClientIndividual.findBySurnameContainingIgnoreCaseAndNameContainingIgnoreCase(words[0], words[1]);

                    if(pledgors.size() == 1)
                        builder.with("pledgor", ":", pledgors.get(0), false);
                    else if(pledgors.size() > 1){
                        for(ClientIndividual ci : pledgors) {
                            builder.with("pledgor", ":", ci, true);
                        }
                    }
                }
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

        builder.with("statusPA", ":", searchParam.get("statusPA"), false);


        Specification<PledgeAgreement> spec = builder.build();

        int currentPage = Integer.parseInt(searchParam.get("page"));
        int pageSize = Integer.parseInt(searchParam.get("size"));
        Pageable pageable = PageRequest.of(currentPage, pageSize);

        return repositoryPledgeAgreement.findAll(spec, pageable);

    }

    @Transactional
    public PledgeAgreement updatePledgeAgreement(PledgeAgreement pledgeAgreement){
        return repositoryPledgeAgreement.save(pledgeAgreement);
    }

}
