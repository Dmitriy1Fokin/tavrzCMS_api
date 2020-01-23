package ru.fds.tavrzcms3.service;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    private static final String MSG_WRONG_ID = "Неверный id{";
    private static final String MSG_LINE = "). Строка: ";

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

    public List<LoanAgreement> getCurrentLoanAgreementsByPledgeAgreement(Long pledgeAgreementId){
        Optional<PledgeAgreement> pledgeAgreement = repositoryPledgeAgreement.findById(pledgeAgreementId);
        if(pledgeAgreement.isPresent())
            return repositoryLoanAgreement.findByPledgeAgreementAndStatusLA(pledgeAgreement.get(), StatusOfAgreement.OPEN);
        else
            return Collections.emptyList();
    }

    public List<LoanAgreement> getClosedLoanAgreementsByPledgeAgreement(Long pledgeAgreementId){
        Optional<PledgeAgreement> pledgeAgreement = repositoryPledgeAgreement.findById(pledgeAgreementId);
        if(pledgeAgreement.isPresent())
            return repositoryLoanAgreement.findByPledgeAgreementAndStatusLA(pledgeAgreement.get(), StatusOfAgreement.CLOSED);
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

    public List<LoanAgreement> getNewLoanAgreementsFromFile(File file) throws IOException {
        FileImporter fileImporter = FileImporterFactory.getInstance(file);
        for(int i = 0; i < excelColumnNum.getStartRow(); i++){
            fileImporter.nextLine();
        }
        int countRow = excelColumnNum.getStartRow();

        List<LoanAgreement> loanAgreementList = new ArrayList<>();

        do {
            countRow += 1;

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
                    .client(setClientInNewLoanAgreement(fileImporter, countRow))
                    .build();

            loanAgreementList.add(loanAgreement);

        }while (fileImporter.nextLine());

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

            StatusOfAgreement statusOfAgreement;
            try {
                statusOfAgreement = StatusOfAgreement.valueOf(fileImporter.getString(excelColumnNum.getLoanAgreementUpdate().getStatus()));
            }catch (IllegalArgumentException ex){
                statusOfAgreement = null;
            }

            loanAgreement.setNumLA(fileImporter.getString(excelColumnNum.getLoanAgreementUpdate().getNumLA()));
            loanAgreement.setDateBeginLA(fileImporter.getLocalDate(excelColumnNum.getLoanAgreementUpdate().getDateBegin()));
            loanAgreement.setDateEndLA(fileImporter.getLocalDate(excelColumnNum.getLoanAgreementUpdate().getDateEnd()));
            loanAgreement.setStatusLA(statusOfAgreement);
            loanAgreement.setAmountLA(fileImporter.getBigDecimal(excelColumnNum.getLoanAgreementUpdate().getAmount()));
            loanAgreement.setDebtLA(fileImporter.getBigDecimal(excelColumnNum.getLoanAgreementUpdate().getDebt()));
            loanAgreement.setInterestRateLA(fileImporter.getDouble(excelColumnNum.getLoanAgreementUpdate().getInterestRate()));
            loanAgreement.setPfo(Byte.valueOf(fileImporter.getInteger(excelColumnNum.getLoanAgreementUpdate().getPfo()).toString()));
            loanAgreement.setQualityCategory(Byte.valueOf(fileImporter.getInteger(excelColumnNum.getLoanAgreementUpdate().getQuality()).toString()));
            loanAgreement.setClient(setClientInCurrentLoanAgreement(fileImporter, countRow));

            loanAgreementList.add(loanAgreement);

        }while (fileImporter.nextLine());

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


    @Transactional
    public LoanAgreement updateInsertLoanAgreement(LoanAgreement loanAgreement){
        return repositoryLoanAgreement.save(loanAgreement);
    }

    @Transactional
    public List<LoanAgreement> updateInsertLoanAgreements(List<LoanAgreement> loanAgreementList){
        return repositoryLoanAgreement.saveAll(loanAgreementList);
    }



}
