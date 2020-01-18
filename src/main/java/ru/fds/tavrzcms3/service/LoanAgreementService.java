package ru.fds.tavrzcms3.service;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fds.tavrzcms3.dictionary.Operations;
import ru.fds.tavrzcms3.dictionary.StatusOfAgreement;
import ru.fds.tavrzcms3.dictionary.excelproprities.ExcelColumnNum;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.LoanAgreement;
import ru.fds.tavrzcms3.domain.PledgeAgreement;
import ru.fds.tavrzcms3.domain.embedded.ClientIndividual;
import ru.fds.tavrzcms3.domain.embedded.ClientLegalEntity;
import ru.fds.tavrzcms3.fileimport.FileImporter;
import ru.fds.tavrzcms3.fileimport.FileImporterFactory;
import ru.fds.tavrzcms3.repository.RepositoryLoanAgreement;
import ru.fds.tavrzcms3.repository.RepositoryPledgeAgreement;
import ru.fds.tavrzcms3.specification.Search;
import ru.fds.tavrzcms3.specification.SearchCriteria;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class LoanAgreementService {

    private final RepositoryLoanAgreement repositoryLoanAgreement;
    private final RepositoryPledgeAgreement repositoryPledgeAgreement;
    private final ClientService clientService;

    private final ExcelColumnNum excelColumnNum;

    public LoanAgreementService(RepositoryLoanAgreement repositoryLoanAgreement,
                                RepositoryPledgeAgreement repositoryPledgeAgreement,
                                ClientService clientService,
                                ExcelColumnNum excelColumnNum) {
        this.repositoryLoanAgreement = repositoryLoanAgreement;
        this.repositoryPledgeAgreement = repositoryPledgeAgreement;
        this.clientService = clientService;
        this.excelColumnNum = excelColumnNum;
    }


    public Optional<LoanAgreement> getLoanAgreementById(long loanAgreementId){
        return repositoryLoanAgreement.findById(loanAgreementId);
    }

    public List<LoanAgreement> getLoanAgreementsByIds(List<Long> ids){
        return repositoryLoanAgreement.findAllByLoanAgreementIdIn(ids);
    }

    public List<LoanAgreement> getAllLoanAgreementsByLoaner(Client client){
        return repositoryLoanAgreement.findAllByClient(client);
    }

    public List<LoanAgreement> getAllLoanAgreementByPledgeAgreement(PledgeAgreement pledgeAgreement){
        return repositoryLoanAgreement.findAllByPledgeAgreements(pledgeAgreement);
    }

    public List<LoanAgreement> getCurrentLoanAgreementsByPledgeAgreement(Long pledgeAgreementId){
        Optional<PledgeAgreement> pledgeAgreement = repositoryPledgeAgreement.findById(pledgeAgreementId);
        if(pledgeAgreement.isPresent())
            return repositoryLoanAgreement.findByPledgeAgreementsAndStatusLAEquals(pledgeAgreement.get(), StatusOfAgreement.OPEN);
        else
            return Collections.emptyList();
    }

    public List<LoanAgreement> getClosedLoanAgreementsByPledgeAgreement(Long pledgeAgreementId){
        Optional<PledgeAgreement> pledgeAgreement = repositoryPledgeAgreement.findById(pledgeAgreementId);
        if(pledgeAgreement.isPresent())
            return repositoryLoanAgreement.findByPledgeAgreementsAndStatusLAEquals(pledgeAgreement.get(), StatusOfAgreement.CLOSED);
        else
            return Collections.emptyList();
    }

    public List<LoanAgreement> getAllCurrentLoanAgreements(){
        return repositoryLoanAgreement.findAllByStatusLAEquals(StatusOfAgreement.OPEN);
    }

    public List<LoanAgreement> getCurrentLoanAgreementsByEmployee(Long employeeId){
        return repositoryLoanAgreement.getLoanAgreementByEmployee(employeeId, StatusOfAgreement.OPEN.getTranslate());
    }

    public List<LoanAgreement> getCurrentLoanAgreementsByLoaner(Long clientId){
        return repositoryLoanAgreement.getLoanAgreementsByClient(clientId, StatusOfAgreement.OPEN.getTranslate());
    }

    public List<LoanAgreement> getClosedLoanAgreementsByLoaner(Long clientId){
        return repositoryLoanAgreement.getLoanAgreementsByClient(clientId, StatusOfAgreement.CLOSED.getTranslate());
    }

    public List<LoanAgreement> getLoanAgreementFromSearch(Map<String, String> searchParam) throws ReflectiveOperationException {
        final String CLIENT = "client";

        Search<LoanAgreement> loanAgreementSearch = new Search<>(LoanAgreement.class);

        Set<String> clientAttributes = new HashSet<>();
        Arrays.stream(Client.class.getDeclaredFields()).forEach(field -> clientAttributes.add(field.getName()));
        Arrays.stream(ClientIndividual.class.getDeclaredFields()).forEach(field -> clientAttributes.add(field.getName()));
        Arrays.stream(ClientLegalEntity.class.getDeclaredFields()).forEach(field -> clientAttributes.add(field.getName()));
        clientAttributes.retainAll(searchParam.keySet());

        if(!clientAttributes.isEmpty() && clientAttributes.contains("typeOfClient")){
            List<Client> clientList = clientService.getClientFromSearch(searchParam);
            if(clientList.isEmpty()){
                SearchCriteria searchCriteria = SearchCriteria.builder()
                        .key(CLIENT)
                        .value(null)
                        .operation(Operations.EQUAL_IGNORE_CASE)
                        .predicate(false)
                        .build();
                loanAgreementSearch.withCriteria(searchCriteria);
            }else {
                SearchCriteria searchCriteriaFirst = SearchCriteria.builder()
                        .key(CLIENT)
                        .value(clientList.get(0))
                        .operation(Operations.EQUAL_IGNORE_CASE)
                        .predicate(false)
                        .build();
                loanAgreementSearch.withCriteria(searchCriteriaFirst);

                for(int i = 1; i < clientList.size(); i++){
                    SearchCriteria searchCriteria = SearchCriteria.builder()
                            .key(CLIENT)
                            .value(clientList.get(i))
                            .operation(Operations.EQUAL_IGNORE_CASE)
                            .predicate(true)
                            .build();
                    loanAgreementSearch.withCriteria(searchCriteria);
                }
            }
        }

        loanAgreementSearch.withParam(searchParam);

        Specification<LoanAgreement> specification = loanAgreementSearch.getSpecification();

        return repositoryLoanAgreement.findAll(specification);
    }

    public List<LoanAgreement> getNewLoanAgreementsFromFile(File file) throws IOException {
        FileImporter fileImporter = FileImporterFactory.getInstance(file);
        for(int i = 0; i < excelColumnNum.getStartRow(); i++){
            fileImporter.nextLine();
        }
        int countRow = excelColumnNum.getStartRow();

        List<LoanAgreement> loanAgreementList = new ArrayList<>();

        do {
            countRow += 1;

            if(Objects.isNull(fileImporter.getLong(excelColumnNum.getLoanAgreementNew().getClientId()))){
                throw new IOException("Неверный id{"
                        + fileImporter.getLong(excelColumnNum.getLoanAgreementNew().getClientId()) + ") клиента.");
            }

            Optional<Client> client = clientService.getClientById(fileImporter
                    .getLong(excelColumnNum.getLoanAgreementNew().getClientId()));
            if(!client.isPresent()){
                throw new IOException("Клиента с таким id отсутствует ("
                        + fileImporter.getLong(excelColumnNum.getLoanAgreementNew().getClientId())
                        + "). Строка: " + countRow);
            }

            StatusOfAgreement statusOfAgreement;
            try {
                statusOfAgreement = StatusOfAgreement.valueOf(fileImporter.getString(excelColumnNum.getLoanAgreementNew().getStatus()));
            }catch (IllegalArgumentException ex){
                statusOfAgreement = null;
            }

            LoanAgreement loanAgreement = LoanAgreement.builder()
                    .numLA(fileImporter.getString(excelColumnNum.getLoanAgreementNew().getNumLA()))
                    .dateBeginLA(fileImporter.getLocalDate(excelColumnNum.getLoanAgreementNew().getDateBegin()))
                    .dateEndLA(fileImporter.getLocalDate(excelColumnNum.getLoanAgreementNew().getDateEnd()))
                    .statusLA(statusOfAgreement)
                    .amountLA(fileImporter.getBigDecimal(excelColumnNum.getLoanAgreementNew().getAmount()))
                    .debtLA(fileImporter.getBigDecimal(excelColumnNum.getLoanAgreementNew().getDebt()))
                    .interestRateLA(fileImporter.getDouble(excelColumnNum.getLoanAgreementNew().getInterestRate()))
                    .pfo(Byte.valueOf(fileImporter.getInteger(excelColumnNum.getLoanAgreementNew().getPfo()).toString()))
                    .qualityCategory(Byte.valueOf(fileImporter.getInteger(excelColumnNum.getLoanAgreementNew().getQuality()).toString()))
                    .client(client.get())
                    .build();

            loanAgreementList.add(loanAgreement);

        }while (fileImporter.nextLine());

        return loanAgreementList;
    }

    public List<LoanAgreement> getCurrentLoanAgreementsFromFile(File file) throws IOException {
        FileImporter fileImporter = FileImporterFactory.getInstance(file);
        for(int i = 0; i < excelColumnNum.getStartRow(); i++){
            fileImporter.nextLine();
        }
        int countRow = excelColumnNum.getStartRow();

        List<LoanAgreement> loanAgreementList = new ArrayList<>();

        do {
            countRow += 1;

            if(Objects.isNull(fileImporter.getLong(excelColumnNum.getLoanAgreementUpdate().getLoanAgreementId()))){
                throw new IOException("Неверный id{"
                        + fileImporter.getLong(excelColumnNum.getLoanAgreementUpdate().getLoanAgreementId()) + ") кредитного догоаора.");
            }
            Optional<LoanAgreement> loanAgreement = getLoanAgreementById(fileImporter
                    .getLong(excelColumnNum.getLoanAgreementUpdate().getLoanAgreementId()));
            if(!loanAgreement.isPresent()){
                throw new IOException("Кредитного договора с таким id отсутствует ("
                        + fileImporter.getLong(excelColumnNum.getLoanAgreementUpdate().getLoanAgreementId())
                        + "). Строка: " + countRow);
            }

            if(Objects.isNull(fileImporter.getLong(excelColumnNum.getLoanAgreementUpdate().getClientId()))){
                throw new IOException("Неверный id{"
                        + fileImporter.getLong(excelColumnNum.getLoanAgreementUpdate().getClientId()) + ") клиента.");
            }
            Optional<Client> client = clientService.getClientById(fileImporter
                    .getLong(excelColumnNum.getLoanAgreementUpdate().getClientId()));
            if(!client.isPresent()){
                throw new IOException("Клиента с таким id отсутствует ("
                        + fileImporter.getLong(excelColumnNum.getLoanAgreementUpdate().getClientId())
                        + "). Строка: " + countRow);
            }

            StatusOfAgreement statusOfAgreement;
            try {
                statusOfAgreement = StatusOfAgreement.valueOf(fileImporter.getString(excelColumnNum.getLoanAgreementUpdate().getStatus()));
            }catch (IllegalArgumentException ex){
                statusOfAgreement = null;
            }

            loanAgreement.get().setNumLA(fileImporter.getString(excelColumnNum.getLoanAgreementUpdate().getNumLA()));
            loanAgreement.get().setDateBeginLA(fileImporter.getLocalDate(excelColumnNum.getLoanAgreementUpdate().getDateBegin()));
            loanAgreement.get().setDateEndLA(fileImporter.getLocalDate(excelColumnNum.getLoanAgreementUpdate().getDateEnd()));
            loanAgreement.get().setStatusLA(statusOfAgreement);
            loanAgreement.get().setAmountLA(fileImporter.getBigDecimal(excelColumnNum.getLoanAgreementUpdate().getAmount()));
            loanAgreement.get().setDebtLA(fileImporter.getBigDecimal(excelColumnNum.getLoanAgreementUpdate().getDebt()));
            loanAgreement.get().setInterestRateLA(fileImporter.getDouble(excelColumnNum.getLoanAgreementUpdate().getInterestRate()));
            loanAgreement.get().setPfo(Byte.valueOf(fileImporter.getInteger(excelColumnNum.getLoanAgreementUpdate().getPfo()).toString()));
            loanAgreement.get().setQualityCategory(Byte.valueOf(fileImporter.getInteger(excelColumnNum.getLoanAgreementUpdate().getQuality()).toString()));
            loanAgreement.get().setClient(client.get());

            loanAgreementList.add(loanAgreement.get());

        }while (fileImporter.nextLine());

        return loanAgreementList;
    }

    @Transactional
    public LoanAgreement updateInsertLoanAgreement(LoanAgreement loanAgreement){
        return repositoryLoanAgreement.save(loanAgreement);
    }

    @Transactional
    public List<LoanAgreement> updateInsertLoanAgreements(List<LoanAgreement> loanAgreementList){
        return repositoryLoanAgreement.saveAll(loanAgreementList);
    }



}
