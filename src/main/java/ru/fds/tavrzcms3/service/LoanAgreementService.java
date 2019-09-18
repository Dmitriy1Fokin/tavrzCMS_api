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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    public LoanAgreement getLoanAgreementById(long loanAgreementId){
        LoanAgreement loanAgreement = repositoryLoanAgreement.getOne(loanAgreementId);
        return loanAgreement;
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

    public Page<LoanAgreement> getLoanAgreementFromSearch(Map<String, String> searchParam){

        SpecificationBuilder builder = new SpecificationBuilderImpl();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy-MM-dd");

        if(!searchParam.get("numLA").isEmpty())
            builder.with("numLA", ":", searchParam.get("numLA"), false);
        if(!searchParam.get("loaner").isEmpty()) {
            if (searchParam.get("loanerOption").equals("юл")) {
                List<ClientLegalEntity> loaners = repositoryClientLegalEntity.findByNameContainingIgnoreCase(searchParam.get("loaner"));

                if(loaners.isEmpty())
                    builder.with("loaner", ":", null, false);
                else if(loaners.size() == 1)
                    builder.with("loaner", ":", loaners.get(0), false);
                else if(loaners.size() > 1)
                    for(ClientLegalEntity cle : loaners)
                        builder.with("loaner", ":", cle, true);
            }
            else{
                String[] words = searchParam.get("loaner").split("\\s");
                List<ClientIndividual> loaners = new ArrayList<>();

                if(words.length == 1)
                    loaners = repositoryClientIndividual.findBySurnameContainingIgnoreCase(words[0]);
                else if(words.length > 1)
                    loaners = repositoryClientIndividual.findBySurnameContainingIgnoreCaseAndNameContainingIgnoreCase(words[0], words[1]);

                if(loaners.isEmpty())
                    builder.with("loaner", ":", null, false);
                else if(loaners.size() == 1)
                    builder.with("loaner", ":", loaners.get(0), false);
                else if(loaners.size() > 1)
                    for(ClientIndividual ci : loaners)
                        builder.with("loaner", ":", ci, true);
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
        return repositoryLoanAgreement.findByLoanerInAndStatusLAEquals(clientList, "открыт", pageable);

    }

}
