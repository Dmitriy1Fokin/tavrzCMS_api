package ru.fds.tavrzcms3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fds.tavrzcms3.dictionary.StatusOfAgreement;
import ru.fds.tavrzcms3.dictionary.TypeOfPledgeAgreement;
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
    RepositoryClient repositoryClient;
    @Autowired
    ClientService clientService;

    public Optional<LoanAgreement> getLoanAgreementById(long loanAgreementId){
        return repositoryLoanAgreement.findById(loanAgreementId);
    }

    public int countOfCurrentLoanAgreementsByPledgeAgreement(PledgeAgreement pledgeAgreement){
        return repositoryLoanAgreement.countAllByPledgeAgreementsAndStatusLAEquals(pledgeAgreement, StatusOfAgreement.OPEN);
    }

    public List<LoanAgreement> getCurrentLoanAgreementsByPledgeAgreement(PledgeAgreement pledgeAgreement){
        return repositoryLoanAgreement.findByPledgeAgreementsAndStatusLAEquals(pledgeAgreement, StatusOfAgreement.OPEN);
    }

    public int countOfClosedLoanAgreementsByPledgeAgreement(PledgeAgreement pledgeAgreement){
        return repositoryLoanAgreement.countAllByPledgeAgreementsAndStatusLAEquals(pledgeAgreement, StatusOfAgreement.CLOSED);
    }

    public List<LoanAgreement> getClosedLoanAgreementsByPledgeAgreement(PledgeAgreement pledgeAgreement){
        return repositoryLoanAgreement.findByPledgeAgreementsAndStatusLAEquals(pledgeAgreement, StatusOfAgreement.CLOSED);
    }

    public int countOfCurrentLoanAgreementsByEmployee(Employee employee){
        List<Client> loaners = clientService.getClientByEmployee(employee);
        return repositoryLoanAgreement.countAllByClientInAndStatusLAEquals(loaners, StatusOfAgreement.OPEN);
    }

    public Page<LoanAgreement> getCurrentLoanAgreementsByEmployee(Pageable pageable, Employee employee){
        List<Client> clientList = repositoryClient.findByEmployee(employee);
        return repositoryLoanAgreement.findByClientInAndStatusLAEquals(clientList, StatusOfAgreement.OPEN, pageable);
    }

    public int countOfCurrentLoanAgreementsByLoaner(Client client){
        return repositoryLoanAgreement.countAllByClientAndStatusLAEquals(client, StatusOfAgreement.OPEN);
    }

    public List<LoanAgreement> getCurrentLoanAgreementsByLoaner(Client client){
        return repositoryLoanAgreement.findByClientAndStatusLAEquals(client, StatusOfAgreement.OPEN);
    }

    public int countOfClosedLoanAgreementsByLoaner(Client client){
        return repositoryLoanAgreement.countAllByClientAndStatusLAEquals(client, StatusOfAgreement.CLOSED);
    }

    public List<LoanAgreement> getClosedLoanAgreementsByLoaner(Client client){
        return repositoryLoanAgreement.findByClientAndStatusLAEquals(client, StatusOfAgreement.CLOSED);
    }

    public Page<LoanAgreement> getLoanAgreementFromSearch(Map<String, String> searchParam){

        SpecificationBuilder builder = new SpecificationBuilderImpl();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy-MM-dd");

        if(!searchParam.get("numLA").isEmpty())
            builder.with("numLA", ":", searchParam.get("numLA"), false);
        if(!searchParam.get("client").isEmpty()) {
            if (searchParam.get("clientOption").equals("юл")) {
                List<ClientLegalEntity> loaners = clientService.getClientLegalEntityByName(searchParam.get("client"));

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
                List<ClientIndividual> loaners = clientService.getClientIndividualByFio(words);

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
        builder.with("statusLA", ":", StatusOfAgreement.valueOf(searchParam.get("statusLA")), false);

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



}
