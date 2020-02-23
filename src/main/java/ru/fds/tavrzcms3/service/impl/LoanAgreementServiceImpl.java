package ru.fds.tavrzcms3.service.impl;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fds.tavrzcms3.dictionary.StatusOfAgreement;
import ru.fds.tavrzcms3.dictionary.excelproprities.ExcelColumnNum;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.LoanAgreement;
import ru.fds.tavrzcms3.domain.embedded.ClientIndividual;
import ru.fds.tavrzcms3.domain.embedded.ClientLegalEntity;
import ru.fds.tavrzcms3.fileimport.FileImporter;
import ru.fds.tavrzcms3.fileimport.FileImporterFactory;
import ru.fds.tavrzcms3.repository.RepositoryLoanAgreement;
import ru.fds.tavrzcms3.repository.RepositoryPledgeAgreement;
import ru.fds.tavrzcms3.service.ClientService;
import ru.fds.tavrzcms3.service.LoanAgreementService;
import ru.fds.tavrzcms3.service.MessageService;
import ru.fds.tavrzcms3.specification.Search;
import ru.fds.tavrzcms3.validate.ValidatorEntity;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class LoanAgreementServiceImpl implements LoanAgreementService {

    private final RepositoryLoanAgreement repositoryLoanAgreement;
    private final RepositoryPledgeAgreement repositoryPledgeAgreement;
    private final ClientService clientService;
    private final ValidatorEntity validatorEntity;
    private final ExcelColumnNum excelColumnNum;
    private final MessageService messageService;

    private static final String MSG_WRONG_ID = "Неверный id{";
    private static final String MSG_LINE = "). Строка: ";

    public LoanAgreementServiceImpl(RepositoryLoanAgreement repositoryLoanAgreement,
                                    RepositoryPledgeAgreement repositoryPledgeAgreement,
                                    ClientService clientService,
                                    ValidatorEntity validatorEntity,
                                    ExcelColumnNum excelColumnNum,
                                    MessageService messageService) {
        this.repositoryLoanAgreement = repositoryLoanAgreement;
        this.repositoryPledgeAgreement = repositoryPledgeAgreement;
        this.clientService = clientService;
        this.validatorEntity = validatorEntity;
        this.excelColumnNum = excelColumnNum;
        this.messageService = messageService;
    }


    @Override
    public Optional<LoanAgreement> getLoanAgreementById(Long loanAgreementId){
        return repositoryLoanAgreement.findById(loanAgreementId);
    }

    @Override
    public List<LoanAgreement> getCurrentLoanAgreementsByPledgeAgreement(Long pledgeAgreementId){
        return repositoryLoanAgreement.findByPledgeAgreementAndStatusLA(pledgeAgreementId, StatusOfAgreement.OPEN);
    }

    @Override
    public List<LoanAgreement> getClosedLoanAgreementsByPledgeAgreement(Long pledgeAgreementId){
        return repositoryLoanAgreement.findByPledgeAgreementAndStatusLA(pledgeAgreementId, StatusOfAgreement.CLOSED);
    }

    @Override
    public List<LoanAgreement> getAllCurrentLoanAgreements(){
        return repositoryLoanAgreement.findAllByStatusLAEquals(StatusOfAgreement.OPEN);
    }

    @Override
    public Integer getCountOfCurrentLoanAgreements(){
        return repositoryLoanAgreement.countAllByStatusLAEquals(StatusOfAgreement.OPEN);
    }

    @Override
    public List<LoanAgreement> getCurrentLoanAgreementsByEmployee(Long employeeId){
        return repositoryLoanAgreement.getLoanAgreementByEmployee(employeeId, StatusOfAgreement.OPEN.getTranslate());
    }

    @Override
    public Integer getCountOfCurrentLoanAgreementsByEmployee(Long employeeId){
        return repositoryLoanAgreement.getCountOfCurrentLoanAgreementByEmployee(employeeId);
    }

    @Override
    public List<LoanAgreement> getCurrentLoanAgreementsByLoaner(Long clientId){
        return repositoryLoanAgreement.getLoanAgreementsByClient(clientId, StatusOfAgreement.OPEN.getTranslate());
    }

    @Override
    public List<LoanAgreement> getClosedLoanAgreementsByLoaner(Long clientId){
        return repositoryLoanAgreement.getLoanAgreementsByClient(clientId, StatusOfAgreement.CLOSED.getTranslate());
    }

    @Override
    public List<LoanAgreement> getLoanAgreementFromSearch(Map<String, String> searchParam) throws ReflectiveOperationException {
        Search<LoanAgreement> loanAgreementSearch = new Search<>(LoanAgreement.class);

        Set<String> clientAttributes = new HashSet<>();
        Arrays.stream(Client.class.getDeclaredFields()).forEach(field -> clientAttributes.add(field.getName()));
        Arrays.stream(ClientIndividual.class.getDeclaredFields()).forEach(field -> clientAttributes.add(field.getName()));
        Arrays.stream(ClientLegalEntity.class.getDeclaredFields()).forEach(field -> clientAttributes.add(field.getName()));
        clientAttributes.retainAll(searchParam.keySet());

        if(!clientAttributes.isEmpty() && clientAttributes.contains("typeOfClient")){
            List<Client> clientList = clientService.getClientFromSearch(searchParam);
            loanAgreementSearch.withParamClient(clientList);
        }

        loanAgreementSearch.withParam(searchParam);

        Specification<LoanAgreement> specification = loanAgreementSearch.getSpecification();

        return repositoryLoanAgreement.findAll(specification);
    }

    @Override
    @Transactional
    public List<LoanAgreement> getNewLoanAgreementsFromFile(File file) throws IOException {
        FileImporter fileImporter = FileImporterFactory.getInstance(file);
        for(int i = 0; i < excelColumnNum.getStartRow(); i++){
            fileImporter.nextLine();
        }
        int countRow = excelColumnNum.getStartRow();

        List<LoanAgreement> loanAgreementList = new ArrayList<>();

        do {
            countRow += 1;

            LoanAgreement loanAgreement = LoanAgreement.builder()
                    .numLA(fileImporter.getString(excelColumnNum.getLoanAgreementNew().getNumLA()))
                    .dateBeginLA(fileImporter.getLocalDate(excelColumnNum.getLoanAgreementNew().getDateBegin()))
                    .dateEndLA(fileImporter.getLocalDate(excelColumnNum.getLoanAgreementNew().getDateEnd()))
                    .statusLA(StatusOfAgreement.valueOfString(fileImporter
                            .getString(excelColumnNum.getLoanAgreementNew().getStatus())).orElse(null))
                    .amountLA(fileImporter.getBigDecimal(excelColumnNum.getLoanAgreementNew().getAmount()))
                    .debtLA(fileImporter.getBigDecimal(excelColumnNum.getLoanAgreementNew().getDebt()))
                    .interestRateLA(fileImporter.getDouble(excelColumnNum.getLoanAgreementNew().getInterestRate()))
                    .pfo(Byte.valueOf(fileImporter.getInteger(excelColumnNum.getLoanAgreementNew().getPfo()).toString()))
                    .qualityCategory(Byte.valueOf(fileImporter.getInteger(excelColumnNum.getLoanAgreementNew().getQuality()).toString()))
                    .client(setClientInNewLoanAgreement(fileImporter, countRow))
                    .build();

            Set<ConstraintViolation<LoanAgreement>> violations =  validatorEntity.validateEntity(loanAgreement);
            if(!violations.isEmpty())
                throw new ConstraintViolationException("object " + countRow, violations);

            loanAgreementList.add(loanAgreement);

        }while (fileImporter.nextLine());

        loanAgreementList = insertLoanAgreements(loanAgreementList);

        return loanAgreementList;
    }

    private Client setClientInNewLoanAgreement(FileImporter fileImporter, int countRow) throws IOException {
        if(Objects.isNull(fileImporter.getLong(excelColumnNum.getLoanAgreementNew().getClientId()))){
            throw new IOException(MSG_WRONG_ID
                    + fileImporter.getLong(excelColumnNum.getLoanAgreementNew().getClientId())
                    + ") клиента. Строка: " + countRow);
        }

        return clientService.getClientById(fileImporter.getLong(excelColumnNum.getLoanAgreementNew().getClientId()))
                .orElseThrow(() -> new IOException("Клиента с таким id отсутствует ("
                        + fileImporter.getLong(excelColumnNum.getLoanAgreementNew().getClientId())
                        + MSG_LINE + countRow));
    }

    @Override
    @Transactional
    public List<LoanAgreement> getCurrentLoanAgreementsFromFile(File file) throws IOException {
        FileImporter fileImporter = FileImporterFactory.getInstance(file);
        for(int i = 0; i < excelColumnNum.getStartRow(); i++){
            fileImporter.nextLine();
        }
        int countRow = excelColumnNum.getStartRow();

        List<LoanAgreement> loanAgreementList = new ArrayList<>();

        do {
            countRow += 1;

            LoanAgreement loanAgreement = setCurrentLoanAgreement(fileImporter, countRow);

            loanAgreement.setNumLA(fileImporter.getString(excelColumnNum.getLoanAgreementUpdate().getNumLA()));
            loanAgreement.setDateBeginLA(fileImporter.getLocalDate(excelColumnNum.getLoanAgreementUpdate().getDateBegin()));
            loanAgreement.setDateEndLA(fileImporter.getLocalDate(excelColumnNum.getLoanAgreementUpdate().getDateEnd()));
            loanAgreement.setStatusLA(StatusOfAgreement.valueOfString(fileImporter
                    .getString(excelColumnNum.getLoanAgreementUpdate().getStatus())).orElse(null));
            loanAgreement.setAmountLA(fileImporter.getBigDecimal(excelColumnNum.getLoanAgreementUpdate().getAmount()));
            loanAgreement.setDebtLA(fileImporter.getBigDecimal(excelColumnNum.getLoanAgreementUpdate().getDebt()));
            loanAgreement.setInterestRateLA(fileImporter.getDouble(excelColumnNum.getLoanAgreementUpdate().getInterestRate()));
            loanAgreement.setPfo(Byte.valueOf(fileImporter.getInteger(excelColumnNum.getLoanAgreementUpdate().getPfo()).toString()));
            loanAgreement.setQualityCategory(Byte.valueOf(fileImporter.getInteger(excelColumnNum.getLoanAgreementUpdate().getQuality()).toString()));
            loanAgreement.setClient(setClientInCurrentLoanAgreement(fileImporter, countRow));

            Set<ConstraintViolation<LoanAgreement>> violations =  validatorEntity.validateEntity(loanAgreement);
            if(!violations.isEmpty())
                throw new ConstraintViolationException("object " + countRow, violations);

            loanAgreementList.add(loanAgreement);

        }while (fileImporter.nextLine());

        loanAgreementList = updateLoanAgreements(loanAgreementList);

        return loanAgreementList;
    }

    private LoanAgreement setCurrentLoanAgreement(FileImporter fileImporter, int countRow) throws IOException {
        if(Objects.isNull(fileImporter.getLong(excelColumnNum.getLoanAgreementUpdate().getLoanAgreementId()))){
            throw new IOException(MSG_WRONG_ID
                    + fileImporter.getLong(excelColumnNum.getLoanAgreementUpdate().getLoanAgreementId())
                    + ") кредитного догоаора. Строка: " + countRow);
        }

        return getLoanAgreementById(fileImporter.getLong(excelColumnNum.getLoanAgreementUpdate().getLoanAgreementId()))
                .orElseThrow(() -> new IOException("Кредитного договора с таким id отсутствует ("
                        + fileImporter.getLong(excelColumnNum.getLoanAgreementUpdate().getLoanAgreementId())
                        + MSG_LINE + countRow));
    }

    private Client setClientInCurrentLoanAgreement(FileImporter fileImporter, int countRow) throws IOException {
        if(Objects.isNull(fileImporter.getLong(excelColumnNum.getLoanAgreementUpdate().getClientId()))){
            throw new IOException(MSG_WRONG_ID
                    + fileImporter.getLong(excelColumnNum.getLoanAgreementUpdate().getClientId())
                    + ") клиента. Строка: " + countRow);
        }

        return clientService.getClientById(fileImporter.getLong(excelColumnNum.getLoanAgreementUpdate().getClientId()))
                .orElseThrow(() -> new IOException("Клиента с таким id отсутствует ("
                        + fileImporter.getLong(excelColumnNum.getLoanAgreementUpdate().getClientId())
                        + MSG_LINE + countRow));
    }


    @Override
    @Transactional
    public LoanAgreement insertLoanAgreement(LoanAgreement loanAgreement){
        loanAgreement = repositoryLoanAgreement.saveAndFlush(loanAgreement);
        messageService.sendNewLoanAgreement(loanAgreement.getLoanAgreementId());
        return loanAgreement;
    }

    @Override
    @Transactional
    public LoanAgreement updateLoanAgreement(LoanAgreement loanAgreement){
        loanAgreement = repositoryLoanAgreement.saveAndFlush(loanAgreement);
        messageService.sendExistLoanAgreement(loanAgreement.getLoanAgreementId());
        return loanAgreement;
    }

    @Override
    @Transactional
    public List<LoanAgreement> insertLoanAgreements(List<LoanAgreement> loanAgreementList){
        loanAgreementList = repositoryLoanAgreement.saveAll(loanAgreementList);
        loanAgreementList.forEach(loanAgreement ->  messageService.sendNewLoanAgreement(loanAgreement.getLoanAgreementId()));
        return loanAgreementList;
    }

    @Override
    @Transactional
    public List<LoanAgreement> updateLoanAgreements(List<LoanAgreement> loanAgreementList){
        loanAgreementList = repositoryLoanAgreement.saveAll(loanAgreementList);
        loanAgreementList.forEach(loanAgreement -> messageService.sendExistLoanAgreement(loanAgreement.getLoanAgreementId()));
        return loanAgreementList;
    }



}
