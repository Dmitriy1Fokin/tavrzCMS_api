package ru.fds.tavrzcms3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.domain.Specification;
import ru.fds.tavrzcms3.Service.InsuranceService;
import ru.fds.tavrzcms3.Service.LoanAgreementService;
import ru.fds.tavrzcms3.Service.PledgeAgreementService;
import ru.fds.tavrzcms3.Service.PledgeSubjectService;
import ru.fds.tavrzcms3.domain.*;
import ru.fds.tavrzcms3.repository.*;
import ru.fds.tavrzcms3.specification.LoanAgreementSpecification;
import ru.fds.tavrzcms3.specification.LoanAgreementSpecificationsBuilder;
import ru.fds.tavrzcms3.specification.SearchCriteria;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@SpringBootApplication
public class Tavrzcms3Application {

    public static void main(String[] args) {
        SpringApplication.run(Tavrzcms3Application.class);
    }

    @Autowired
    private RepositoryEmployee repositoryEmployee;

    @Autowired
    private RepositoryClientIndividual repositoryClientIndividual;

    @Autowired
    private RepositoryClient repositoryClient;

    @Autowired
    private RepositoryPledgeAgreement repositoryPledgeAgreement;

    @Autowired
    private RepositoryPledgeSubject repositoryPledgeSubject;

    @Autowired
    private InsuranceService insuranceService;

    @Autowired
    private PledgeSubjectService pledgeSubjectService;

    @Autowired
    private RepositoryLoanAgreement repositoryLoanAgreement;

    @Autowired
    private LoanAgreementService loanAgreementService;

    @Autowired
    private PledgeAgreementService pledgeAgreementService;

    @Autowired
    private RepositoryClientLegalEntity repositoryClientLegalEntity;





    @PostConstruct
    public void init(){
//        ClientIndividual clientList = repositoryClientIndividual.findByPasportNum("5532 123765");
//        System.out.println(clientList);
//
//        System.out.println("-------------FIND ALL EMPLOYEES------------");
//        List<Employee> employees = repositoryEmployee.findAll();
//        for(Employee emp : employees)
//            System.out.println(emp);
//
//        System.out.println("-------------FIND ALL PledgeAgreement------------");
//        System.out.println(employees.get(1));
//        List<Client> clients = repositoryClient.findByEmployee(employees.get(1));
//        for(Client c : clients)
//            System.out.println(c);
//
//        List<PledgeAgreement> pledgeEgreements = repositoryPledgeAgreement.findByPledgorIn(clients);
//        for(PledgeAgreement pe : pledgeEgreements)
//            System.out.println(pe);
//
//
//        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + repositoryPledgeSubject.findById((long)234));


//        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + repositoryPledgeSubject.findByPledgeSubjectId(147));

//        System.out.println("!!!!!!!!!!!!!!!!!!" + loanAgreementService.countOfCurrentPledgeAgreementsByPledgorId(4));

//        PledgeAgreement pledgeAgreement = repositoryPledgeAgreement.findByPledgeAgreementId(6);
//        System.out.println("!!!!!!!!!!!!!!!" + pledgeAgreement);
//
//        Set<String> stringSet =  pledgeAgreementService.getTypeOfCollateral(pledgeAgreement.getPledgeAgreementId());
//        for(String str : stringSet)
//            System.out.println(str);

//
//        List<LoanAgreement> loanAgreements = pledgeAgreementService.getClosedLoanAgreementsForPledgeAgreement(126);
//        for(LoanAgreement la : loanAgreements)
//            System.out.println(la);


//        System.out.println(pledgeAgreementService.countOfMonitoringNotDone(1));
//        System.out.println(pledgeAgreementService.countOfMonitoringIsDone(1));
//        System.out.println(pledgeAgreementService.countOfMonitoringOverdue(1));
//
//        System.out.println("PledgeAgreementWithMonitoringNotDone:");
//        List<PledgeAgreement> pledgeAgreementList = pledgeAgreementService.getPledgeAgreementWithMonitoringNotDone(1);
//        for(int i = 0; i < pledgeAgreementList.size(); i++) {
//            System.out.println(pledgeAgreementList.get(i).getPledgeAgreementId());
////            System.out.println(i + 1 + ". \t" + pledgeAgreementList.get(i).getPledgeAgreementId());
//            System.out.println("Plgedge Subjects of " + pledgeAgreementList.get(i).getPledgeAgreementId());
//            List<PledgeSubject> pledgeSubjects = pledgeSubjectService.getPledgeSubjectsForPledgeAgreement(pledgeAgreementList.get(i).getPledgeAgreementId());
//            for(PledgeSubject ps : pledgeSubjects)
//                System.out.println(ps.getDateMonitoring());
//        }
//
//        System.out.println("!!!!!!!!");
//        List<PledgeAgreement> pledgeAgreementList1 = repositoryPledgeAgreement.findByNumPE("415/1");
//        List<LoanAgreement> loanAgreements = repositoryLoanAgreement.findByPledgeAgreementsAndStatusLAEquals(pledgeAgreementList1.get(0), "открыт");
//        for(LoanAgreement la : loanAgreements)
//            System.out.println(la.getNumLA());
//
//        System.out.println("!!!!!!!!");
//        Client client = repositoryClient.findByLoanAgreements(loanAgreements.get(0));
//        System.out.println(client);
//
//        System.out.println("!!!!!!!!");
//        Employee employee = repositoryEmployee.getOne((long)1);
//        List<Client> clients = repositoryClient.findByEmployee(employee);
//        for(Client c : clients)
//            System.out.println(c.getClientId());

//        LoanAgreementSpecification cpec1 = new LoanAgreementSpecification(new SearchCriteria("pfo", ">=", "4"));
//        List<LoanAgreement> loanAgreements = repositoryLoanAgreement.findAll(cpec1);
//        for(LoanAgreement la : loanAgreements)
//            System.out.println(la.getNumLA());

//        LoanAgreementSpecificationsBuilder builder = new LoanAgreementSpecificationsBuilder();
//        builder.with("pfo", ">", "4");
//        builder.with("amountLA", ">", "100000000");
//        builder.with("debtLA", ">", "66000000");
//        Calendar qwer = new GregorianCalendar(2017,8, 25);
//        Date date = new Date(qwer.getTimeInMillis());
//        builder.with("dateBeginLA", ">=", date);
//        Specification<LoanAgreement> cpec = builder.build();
//        List<LoanAgreement> loanAgreements = repositoryLoanAgreement.findAll(cpec);
//        for(LoanAgreement la : loanAgreements)
//            System.out.println(la.getNumLA() + " - " + la.getAmountLA() + " - " + la.getPfo() + " - " + la.getDebtLA() + " - " + la.getDateBeginLA());
//            System.out.println(la.getNumLA());

//        List<ClientLegalEntity> clientLegalEntities = repositoryClientLegalEntity.findByNameContainingIgnoreCase("ДИАБ");
//        for(ClientLegalEntity c : clientLegalEntities)
//            System.out.println(c);

        List<PledgeAgreement> pledgeAgreementList = pledgeAgreementService.getPervCurrentPledgeAgreementsForEmployeee(1);
        Calendar dateNow = new GregorianCalendar();
        Calendar firstDateCalendar = new GregorianCalendar(dateNow.get(Calendar.YEAR) - 1, dateNow.get(Calendar.MONTH), 1);
        Calendar secondDateCalendar = new GregorianCalendar(dateNow.get(Calendar.YEAR) - 1, dateNow.get(Calendar.MONTH), dateNow.getMaximum(Calendar.DAY_OF_MONTH));
        Date firstDate = new Date(firstDateCalendar.getTimeInMillis());
        Date secondDate = new Date(secondDateCalendar.getTimeInMillis());
        int countOfMonitoring = 0;
        for(PledgeAgreement pa : pledgeAgreementList){
//            int count = repositoryPledgeSubject.existsByPledgeAgreementsAndDateMonitoringBetween(pa, firstDate, secondDate);
            if(repositoryPledgeSubject.existsByPledgeAgreementsAndDateMonitoringBetween(pa, firstDate, secondDate))
                countOfMonitoring += 1;
        }

        System.out.println("countOfMonitoringNotDone:" + countOfMonitoring);

    }
}
