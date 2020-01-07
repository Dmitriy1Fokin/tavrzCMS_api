package ru.fds.tavrzcms3.service;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fds.tavrzcms3.dictionary.Operations;
import ru.fds.tavrzcms3.dictionary.StatusOfAgreement;
import ru.fds.tavrzcms3.dictionary.excelproprities.ExcelColumnNum;
import ru.fds.tavrzcms3.domain.*;
import ru.fds.tavrzcms3.fileimport.FileImporter;
import ru.fds.tavrzcms3.fileimport.FileImporterFactory;
import ru.fds.tavrzcms3.repository.*;
import ru.fds.tavrzcms3.specification.SearchCriteria;
import ru.fds.tavrzcms3.specification.SpecificationBuilder;
import ru.fds.tavrzcms3.specification.impl.SpecificationBuilderImpl;
import ru.fds.tavrzcms3.validate.ValidatorEntity;

import javax.validation.ConstraintViolation;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class LoanAgreementService {

    private final RepositoryLoanAgreement repositoryLoanAgreement;
    private final RepositoryPledgeAgreement repositoryPledgeAgreement;
    private final RepositoryClient repositoryClient;
    private final ClientService clientService;

    private final ValidatorEntity validatorEntity;
    private final ExcelColumnNum excelColumnNum;

    public LoanAgreementService(RepositoryLoanAgreement repositoryLoanAgreement,
                                RepositoryPledgeAgreement repositoryPledgeAgreement,
                                RepositoryClient repositoryClient,
                                ClientService clientService,
                                ValidatorEntity validatorEntity,
                                ExcelColumnNum excelColumnNum) {
        this.repositoryLoanAgreement = repositoryLoanAgreement;
        this.repositoryPledgeAgreement = repositoryPledgeAgreement;
        this.repositoryClient = repositoryClient;
        this.clientService = clientService;
        this.validatorEntity = validatorEntity;
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

    public List<LoanAgreement> getAllLoanAgreementByPledgeAgreements(List<PledgeAgreement> pledgeAgreementList){
        return repositoryLoanAgreement.findAllByPledgeAgreementsIn(pledgeAgreementList);
    }

    public List<LoanAgreement> getCurrentLoanAgreementsByPledgeAgreement(PledgeAgreement pledgeAgreement){
        return repositoryLoanAgreement.findByPledgeAgreementsAndStatusLAEquals(pledgeAgreement, StatusOfAgreement.OPEN);
    }

    public List<LoanAgreement> getClosedLoanAgreementsByPledgeAgreement(PledgeAgreement pledgeAgreement){
        return repositoryLoanAgreement.findByPledgeAgreementsAndStatusLAEquals(pledgeAgreement, StatusOfAgreement.CLOSED);
    }

    public int countOfCurrentLoanAgreementsByEmployee(Employee employee){
        List<Client> loaners = clientService.getClientByEmployee(employee);
        return repositoryLoanAgreement.countAllByClientInAndStatusLAEquals(loaners, StatusOfAgreement.OPEN);
    }

    public int countOfAllCurrentLoanAgreements(){
        return repositoryLoanAgreement.countAllByStatusLAEquals(StatusOfAgreement.OPEN);
    }

    public List<LoanAgreement> getAllCurrentLoanAgreements(){
        return repositoryLoanAgreement.findAllByStatusLAEquals(StatusOfAgreement.OPEN);
    }

    public List<LoanAgreement> getCurrentLoanAgreementsByEmployee(Employee employee){
        List<Client> clientList = repositoryClient.findByEmployee(employee);
        return repositoryLoanAgreement.findByClientInAndStatusLAEquals(clientList, StatusOfAgreement.OPEN);
    }

    public List<LoanAgreement> getCurrentLoanAgreementsByLoaner(Client client){
        return repositoryLoanAgreement.findByClientAndStatusLAEquals(client, StatusOfAgreement.OPEN);
    }

    public List<LoanAgreement> getClosedLoanAgreementsByLoaner(Client client){
        return repositoryLoanAgreement.findByClientAndStatusLAEquals(client, StatusOfAgreement.CLOSED);
    }

    public List<LoanAgreement> getLoanAgreementFromSearch(Map<String, String> searchParam){

        SpecificationBuilder builder = new SpecificationBuilderImpl();

        for(Field field : LoanAgreement.class.getDeclaredFields()){
            if(searchParam.containsKey(field.getName())){
                if((field.getType()==String.class || field.getType()==double.class || field.getType()==byte.class || field.getType()==BigDecimal.class)
                        && !searchParam.get(field.getName()).isEmpty()){
                    SearchCriteria searchCriteria = SearchCriteria.builder()
                            .key(field.getName())
                            .value(searchParam.get(field.getName()))
                            .operation(Operations.valueOf(searchParam.get(field.getName() + "Option")))
                            .predicate(false)
                            .build();
                    builder.with(searchCriteria);

                }else if(field.getType() == StatusOfAgreement.class && !searchParam.get(field.getName()).isEmpty()){
                    SearchCriteria searchCriteria = SearchCriteria.builder()
                            .key(field.getName())
                            .value(StatusOfAgreement.valueOf(searchParam.get(field.getName())))
                            .operation(Operations.EQUAL_IGNORE_CASE)
                            .predicate(false)
                            .build();
                    builder.with(searchCriteria);
                }else if(field.getType() == Client.class && !searchParam.get(field.getName()).isEmpty()){
                    Map<String, String> searchParamClient = new HashMap<>();
                    searchParamClient.put("typeOfClient", searchParam.get("typeOfClient"));
                    searchParamClient.put("clientName", searchParam.get(field.getName()));
                    List<Client> clientList = clientService.getClientFromSearch(searchParamClient);
                    if(clientList.isEmpty()){
                        SearchCriteria searchCriteria = SearchCriteria.builder()
                                .key(field.getName())
                                .value(null)
                                .operation(Operations.EQUAL_IGNORE_CASE)
                                .predicate(false)
                                .build();
                        builder.with(searchCriteria);
                    }else if(clientList.size() == 1){
                            SearchCriteria searchCriteria = SearchCriteria.builder()
                                    .key(field.getName())
                                    .value(clientList.get(0))
                                    .operation(Operations.EQUAL_IGNORE_CASE)
                                    .predicate(false)
                                    .build();
                            builder.with(searchCriteria);

                    }else {
                        SearchCriteria searchCriteriaFirst = SearchCriteria.builder()
                                .key(field.getName())
                                .value(clientList.get(0))
                                .operation(Operations.EQUAL_IGNORE_CASE)
                                .predicate(false)
                                .build();
                        builder.with(searchCriteriaFirst);

                        for(int i = 1; i < clientList.size(); i++){
                            SearchCriteria searchCriteria = SearchCriteria.builder()
                                    .key(field.getName())
                                    .value(clientList.get(i))
                                    .operation(Operations.EQUAL_IGNORE_CASE)
                                    .predicate(true)
                                    .build();
                            builder.with(searchCriteria);
                        }
                    }
                }else if(field.getType() == LocalDate.class && !searchParam.get(field.getName()).isEmpty()){

                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyy-MM-dd");
                    LocalDate localDate = LocalDate.parse(searchParam.get(field.getName()), dateTimeFormatter);

                    SearchCriteria searchCriteria = SearchCriteria.builder()
                            .key(field.getName())
                            .value(localDate)
                            .operation(Operations.valueOf(searchParam.get(field.getName() + "Option")))
                            .predicate(false)
                            .build();
                    builder.with(searchCriteria);
                }
            }
        }

        Specification<LoanAgreement> spec = builder.build();

        return repositoryLoanAgreement.findAll(spec);
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

            LoanAgreement loanAgreement = LoanAgreement.builder()
                    .numLA(fileImporter.getString(excelColumnNum.getLoanAgreementNew().getNumLA()))
                    .dateBeginLA(fileImporter.getLocalDate(excelColumnNum.getLoanAgreementNew().getDateBegin()))
                    .dateEndLA(fileImporter.getLocalDate(excelColumnNum.getLoanAgreementNew().getDateEnd()))
                    .statusLA(StatusOfAgreement.valueOf(fileImporter.getString(excelColumnNum.getLoanAgreementNew().getStatus())))
                    .amountLA(fileImporter.getBigDecimal(excelColumnNum.getLoanAgreementNew().getAmount()))
                    .debtLA(fileImporter.getBigDecimal(excelColumnNum.getLoanAgreementNew().getDebt()))
                    .interestRateLA(fileImporter.getDouble(excelColumnNum.getLoanAgreementNew().getInterestRate()))
                    .pfo(Byte.valueOf(fileImporter.getString(excelColumnNum.getLoanAgreementNew().getPfo())))
                    .qualityCategory(Byte.valueOf(fileImporter.getString(excelColumnNum.getLoanAgreementNew().getQuality())))
                    .client(client.get())
                    .build();

            Set<ConstraintViolation<LoanAgreement>> violations = validatorEntity.validateEntity(loanAgreement);
            if(!violations.isEmpty())
                throw new IOException("В строке:" + countRow + ". " + validatorEntity.getErrorMessage());

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

            loanAgreement.get().setNumLA(fileImporter.getString(excelColumnNum.getLoanAgreementUpdate().getNumLA()));
            loanAgreement.get().setDateBeginLA(fileImporter.getLocalDate(excelColumnNum.getLoanAgreementUpdate().getDateBegin()));
            loanAgreement.get().setDateEndLA(fileImporter.getLocalDate(excelColumnNum.getLoanAgreementUpdate().getDateEnd()));
            loanAgreement.get().setStatusLA(StatusOfAgreement.valueOf(fileImporter.getString(excelColumnNum.getLoanAgreementUpdate().getStatus())));
            loanAgreement.get().setAmountLA(fileImporter.getBigDecimal(excelColumnNum.getLoanAgreementUpdate().getAmount()));
            loanAgreement.get().setDebtLA(fileImporter.getBigDecimal(excelColumnNum.getLoanAgreementUpdate().getDebt()));
            loanAgreement.get().setInterestRateLA(fileImporter.getDouble(excelColumnNum.getLoanAgreementUpdate().getInterestRate()));
            loanAgreement.get().setPfo(Byte.valueOf(fileImporter.getString(excelColumnNum.getLoanAgreementUpdate().getPfo())));
            loanAgreement.get().setQualityCategory(Byte.valueOf(fileImporter.getString(excelColumnNum.getLoanAgreementUpdate().getQuality())));
            loanAgreement.get().setClient(client.get());

            Set<ConstraintViolation<LoanAgreement>> violations = validatorEntity.validateEntity(loanAgreement.get());
            if(!violations.isEmpty())
                throw new IOException("В строке:" + countRow + ". " + validatorEntity.getErrorMessage());

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
