package ru.fds.tavrzcms3.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.fds.tavrzcms3.domain.*;
import ru.fds.tavrzcms3.repository.*;
import ru.fds.tavrzcms3.specification.LoanAgreementSpecificationsBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    public synchronized LoanAgreement getLoanAgreementById(long loanAgreementId){
        LoanAgreement loanAgreement = repositoryLoanAgreement.getOne(loanAgreementId);
        return loanAgreement;
    }

    public synchronized int countOfCurrentPledgeAgreements(long loanAgreementId){
        List<PledgeAgreement> pledgeAgreementList = repositoryPledgeAgreement.findByLoanAgreementsAndStatusPAEquals(repositoryLoanAgreement.findByLoanAgreementId(loanAgreementId),
                                                                                                                    "открыт");
        return pledgeAgreementList.size();
    }

    public synchronized List<PledgeAgreement> getCurrentPledgeAgreements(long loanAgreementId){
        return repositoryPledgeAgreement.findByLoanAgreementsAndStatusPAEquals(repositoryLoanAgreement.findByLoanAgreementId(loanAgreementId),
                                                                                 "открыт");
    }

    public synchronized int countOfClosedPledgeAgreements(long loanAgreementId){
        List<PledgeAgreement> pledgeAgreementList = repositoryPledgeAgreement.findByLoanAgreementsAndStatusPAEquals(repositoryLoanAgreement.findByLoanAgreementId(loanAgreementId),
                                                                                                                      "закрыт");

        return pledgeAgreementList.size();
    }

    public synchronized List<PledgeAgreement> getClosedPledgeAgreements(long loanAgreementId){
        return repositoryPledgeAgreement.findByLoanAgreementsAndStatusPAEquals(repositoryLoanAgreement.findByLoanAgreementId(loanAgreementId),
                                                                                  "закрыт");
    }

    public synchronized List<LoanAgreement> getCurrentLoanAgreement(long loanerId){
        return repositoryLoanAgreement.findByLoanerAndStatusLAEquals(repositoryClient.getOne(loanerId), "открыт");
    }

    public synchronized List<LoanAgreement> getCurrentLoanAgreementsForEmployee(long employeeId){
        Employee employee = repositoryEmployee.getOne(employeeId);
        List<Client> clientList = repositoryClient.findByEmployee(employee);
        return repositoryLoanAgreement.findByLoanerInAndStatusLAEquals(clientList, "открыт");
    }

    public synchronized List<LoanAgreement> getLoanAgreementFromSearch(Map<String, String> searchParam){
        LoanAgreementSpecificationsBuilder builder = new LoanAgreementSpecificationsBuilder();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy-MM-dd");
        if(!searchParam.get("numLA").isEmpty())
            builder.with("numLA", ":", searchParam.get("numLA"), false);


        if(!searchParam.get("loaner").isEmpty()) {
            if (searchParam.get("loanerOption").equals("юл")) {
                List<ClientLegalEntity> clientLegalEntityList = repositoryClientLegalEntity.findByNameContainingIgnoreCase(searchParam.get("loaner"));
                if(clientLegalEntityList.size() == 1) {
                    builder.with("loaner", ":", clientLegalEntityList.get(0), false);
                }
                else if(clientLegalEntityList.size() > 1){
                    for(ClientLegalEntity cle : clientLegalEntityList) {
                        builder.with("loaner", ":", cle, true);
                    }
                }
            }
            else{
                String[] words = searchParam.get("loaner").split("\\s");
                for(String str : words)
                    System.out.println("WORDS:" + str + ", lenght:" + words.length);
                if(words.length == 1){
                    List<ClientIndividual> loaners = repositoryClientIndividual.findBySurnameContainingIgnoreCase(words[0]);
                    for (ClientIndividual qwe : loaners){
                        System.out.println("Loaner: " + qwe.getSurname() + ", id:" + qwe.getClientId());
                    }
                    if(loaners.size() == 1)
                        builder.with("loaner", ":", loaners.get(0), false);
                    else if(loaners.size() > 1){
                        for(ClientIndividual ci : loaners) {
                            builder.with("loaner", ":", ci, true);
                        }
                    }
                }
                else if(words.length == 2){
                    List<ClientIndividual> loaners = repositoryClientIndividual.findBySurnameContainingIgnoreCaseAndNameContainingIgnoreCase(words[0], words[1]);
                    for (ClientIndividual qwe : loaners) {
                        System.out.println("Loaner: " + qwe.getSurname() + ", id:" + qwe.getClientId());
                    }
                    if(loaners.size() == 1)
                        builder.with("loaner", ":", loaners.get(0), false);
                    else if(loaners.size() > 1){
                        for(ClientIndividual ci : loaners) {
                            builder.with("loaner", ":", ci, true);
                        }
                    }
                }
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

        if(!searchParam.get("pfo").isEmpty()){
            builder.with("pfo", searchParam.get("pfoOption"), searchParam.get("pfo"), false);
        }

        if(!searchParam.get("quality").isEmpty()){
            builder.with("quality", searchParam.get("qualityOption"), searchParam.get("quality"), false);
        }

        if(!searchParam.get("amaunt").isEmpty()){
            builder.with("amaunt", searchParam.get("amauntOption"), searchParam.get("amaunt"), false);
        }

        if(!searchParam.get("debt").isEmpty()){
            builder.with("debt", searchParam.get("debtOption"), searchParam.get("debt"), false);
        }

        if(!searchParam.get("interestRate").isEmpty()){
            builder.with("interestRate", searchParam.get("interestRateOption"), searchParam.get("interestRate"), false);
        }

        builder.with("statusLA", ":", searchParam.get("statusLA"), false);


        Specification<LoanAgreement> cpec = builder.build();

        return repositoryLoanAgreement.findAll(cpec);
    }

}
