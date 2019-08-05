package ru.fds.tavrzcms3;

import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.fds.tavrzcms3.Service.InsuranceService;
import ru.fds.tavrzcms3.Service.LoanAgreementService;
import ru.fds.tavrzcms3.Service.PledgeAgreementService;
import ru.fds.tavrzcms3.Service.PledgeSubjectService;
import ru.fds.tavrzcms3.domain.LoanAgreement;
import ru.fds.tavrzcms3.domain.PledgeAgreement;
import ru.fds.tavrzcms3.repository.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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


        List<LoanAgreement> loanAgreements = pledgeAgreementService.getClosedLoanAgreements(126);
        for(LoanAgreement la : loanAgreements)
            System.out.println(la);

    }
}
