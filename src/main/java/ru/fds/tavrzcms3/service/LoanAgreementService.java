package ru.fds.tavrzcms3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
public class LoanAgreementService {

    @Autowired
    RepositoryLoanAgreement repositoryLoanAgreement;
    @Autowired
    RepositoryPledgeAgreement repositoryPledgeAgreement;
    @Autowired
    RepositoryClient repositoryClient;
    @Autowired
    RepositoryClientLegalEntity repositoryClientLegalEntity;
    @Autowired
    RepositoryClientIndividual repositoryClientIndividual;
    @Autowired
    RepositoryEmployee repositoryEmployee;
    @Autowired
    EmployeeService employeeService;
    @Autowired
    ClientService clientService;

    public Optional<LoanAgreement> getLoanAgreementById(long loanAgreementId){
        return repositoryLoanAgreement.findById(loanAgreementId);
    }

    public int countOfCurrentLoanAgreementsForPledgeAgreement(PledgeAgreement pledgeAgreement){
        return repositoryLoanAgreement.countAllByPledgeAgreementsAndStatusLAEquals(pledgeAgreement, "открыт");
    }

    public List<LoanAgreement> getCurrentLoanAgreementsForPledgeAgreement(PledgeAgreement pledgeAgreement){
        return repositoryLoanAgreement.findByPledgeAgreementsAndStatusLAEquals(pledgeAgreement, "открыт");
    }

    public int countOfClosedLoanAgreementsForPledgeAgreement(PledgeAgreement pledgeAgreement){
        return repositoryLoanAgreement.countAllByPledgeAgreementsAndStatusLAEquals(pledgeAgreement, "закрыт");
    }

    public List<LoanAgreement> getClosedLoanAgreementsForPledgeAgreement(PledgeAgreement pledgeAgreement){
        return repositoryLoanAgreement.findByPledgeAgreementsAndStatusLAEquals(pledgeAgreement, "закрыт");
    }

    public int countOfCurrentLoanAgreementsForEmployee(Employee employee){
        List<Client> loaners = clientService.getClientByEmployee(employee);
        return repositoryLoanAgreement.countAllByClientInAndStatusLAEquals(loaners, "открыт");
    }

    public int countOfCurrentPledgeAgreements(long loanAgreementId){
        List<PledgeAgreement> pledgeAgreementList = repositoryPledgeAgreement
                .findByLoanAgreementsAndStatusPAEquals(repositoryLoanAgreement.findByLoanAgreementId(loanAgreementId),"открыт");
        return pledgeAgreementList.size();
    }

    public List<PledgeAgreement> getCurrentPledgeAgreements(long loanAgreementId){
        return repositoryPledgeAgreement.findByLoanAgreementsAndStatusPAEquals(repositoryLoanAgreement.findByLoanAgreementId(loanAgreementId),
                                                                                 "открыт");
    }

    public int countOfClosedPledgeAgreements(long loanAgreementId){
        List<PledgeAgreement> pledgeAgreementList = repositoryPledgeAgreement
                .findByLoanAgreementsAndStatusPAEquals(repositoryLoanAgreement.findByLoanAgreementId(loanAgreementId),"закрыт");

        return pledgeAgreementList.size();
    }

    public List<PledgeAgreement> getClosedPledgeAgreements(long loanAgreementId){
        return repositoryPledgeAgreement
                .findByLoanAgreementsAndStatusPAEquals(repositoryLoanAgreement
                                .findByLoanAgreementId(loanAgreementId),"закрыт");
    }

    public List<PledgeAgreement> getAllPledgeAgreements(long loanAgreementId){
        return repositoryPledgeAgreement
                .findByLoanAgreements(repositoryLoanAgreement.findByLoanAgreementId(loanAgreementId));
    }

    public Page<LoanAgreement> getLoanAgreementFromSearch(Map<String, String> searchParam){

        SpecificationBuilder builder = new SpecificationBuilderImpl();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy-MM-dd");

        if(!searchParam.get("numLA").isEmpty())
            builder.with("numLA", ":", searchParam.get("numLA"), false);
        if(!searchParam.get("client").isEmpty()) {
            if (searchParam.get("clientOption").equals("юл")) {
                List<ClientLegalEntity> loaners = repositoryClientLegalEntity.findByNameContainingIgnoreCase(searchParam.get("client"));

                if(loaners.isEmpty())
                    builder.with("client", ":", null, false);
                else if(loaners.size() == 1)
                    builder.with("client", ":", loaners.get(0), false);
                else if(loaners.size() > 1)
                    for(ClientLegalEntity cle : loaners)
                        builder.with("client", ":", cle, true);
            }
            else{
                String[] words = searchParam.get("client").split("\\s");
                List<ClientIndividual> loaners = new ArrayList<>();

                if(words.length == 1)
                    loaners = repositoryClientIndividual.findBySurnameContainingIgnoreCase(words[0]);
                else if(words.length > 1)
                    loaners = repositoryClientIndividual.findBySurnameContainingIgnoreCaseAndNameContainingIgnoreCase(words[0], words[1]);

                if(loaners.isEmpty())
                    builder.with("client", ":", null, false);
                else if(loaners.size() == 1)
                    builder.with("client", ":", loaners.get(0), false);
                else if(loaners.size() > 1)
                    for(ClientIndividual ci : loaners)
                        builder.with("client", ":", ci, true);
            }
        }
        if(!searchParam.get("dateBeginLA").isEmpty()){
            try {
                Date date = simpleDateFormat.parse(searchParam.get("dateBeginLA"));
                builder.with("dateBeginLA", searchParam.get("dateBeginLAOption"), date, false);
            }catch (ParseException e){
                System.out.println("Не верный фортат dateBeginLA");
            }
        }
        if(!searchParam.get("dateEndLA").isEmpty()){
            try {
                Date date = simpleDateFormat.parse(searchParam.get("dateEndLA"));
                builder.with("dateEndLA", searchParam.get("dateEndLAOption"), date, false);
            }catch (ParseException e){
                System.out.println("Не верный фортат dateEndLA");
            }
        }
        if(!searchParam.get("pfo").isEmpty())
            builder.with("pfo", searchParam.get("pfoOption"), searchParam.get("pfo"), false);
        if(!searchParam.get("quality").isEmpty())
            builder.with("qualityCategory", searchParam.get("qualityOption"), searchParam.get("quality"), false);
        if(!searchParam.get("amaunt").isEmpty())
            builder.with("amountLA", searchParam.get("amauntOption"), searchParam.get("amaunt"), false);
        if(!searchParam.get("debt").isEmpty())
            builder.with("debtLA", searchParam.get("debtOption"), searchParam.get("debt"), false);
        if(!searchParam.get("interestRate").isEmpty())
            builder.with("interestRateLA", searchParam.get("interestRateOption"), searchParam.get("interestRate"), false);
        builder.with("statusLA", ":", searchParam.get("statusLA"), false);

        Specification<LoanAgreement> spec = builder.build();

        int currentPage = Integer.parseInt(searchParam.get("page"));
        int pageSize = Integer.parseInt(searchParam.get("size"));
        Pageable pageable = PageRequest.of(currentPage, pageSize);

        return repositoryLoanAgreement.findAll(spec, pageable);
    }

    @Transactional
    public LoanAgreement updateInsertLoanAgreement(LoanAgreement loanAgreement){
        return repositoryLoanAgreement.save(loanAgreement);
    }

    public Page<LoanAgreement> getCurrentLoanAgreementsForEmployee(Pageable pageable, long employeeId){
        Employee employee = repositoryEmployee.getOne(employeeId);
        List<Client> clientList = repositoryClient.findByEmployee(employee);
        return repositoryLoanAgreement.findByClientInAndStatusLAEquals(clientList, "открыт", pageable);

    }

}
