package ru.fds.tavrzcms3.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fds.tavrzcms3.dictionary.excelproprities.ExcelColumnNum;
import ru.fds.tavrzcms3.domain.CostHistory;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.fileimport.FileImporter;
import ru.fds.tavrzcms3.fileimport.FileImporterFactory;
import ru.fds.tavrzcms3.repository.RepositoryCostHistory;
import ru.fds.tavrzcms3.repository.RepositoryPledgeSubject;
import ru.fds.tavrzcms3.validate.ValidatorEntity;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class CostHistoryService {

    private final RepositoryCostHistory repositoryCostHistory;
    private final RepositoryPledgeSubject repositoryPledgeSubject;
    private final ValidatorEntity validatorEntity;
    private final ExcelColumnNum excelColumnNum;

    private static final String MSG_WRONG_ID = "Неверный id{";
    private static final String MSG_LINE = "). Строка: ";

    public CostHistoryService(RepositoryCostHistory repositoryCostHistory,
                              RepositoryPledgeSubject repositoryPledgeSubject,
                              ValidatorEntity validatorEntity,
                              ExcelColumnNum excelColumnNum) {
        this.repositoryCostHistory = repositoryCostHistory;
        this.repositoryPledgeSubject = repositoryPledgeSubject;
        this.validatorEntity = validatorEntity;
        this.excelColumnNum = excelColumnNum;
    }

    private Optional<CostHistory> getCostHistoryById(Long costHistoryId){
        return repositoryCostHistory.findById(costHistoryId);
    }

    public List<CostHistory> getCostHistoryPledgeSubject(Long pledgeSubjectId){
        Sort sortByDateConclusion = new Sort(Sort.Direction.DESC, "dateConclusion");
        return repositoryCostHistory.findByPledgeSubject(pledgeSubjectId, sortByDateConclusion);
    }

    @Transactional
    public List<CostHistory> getNewCostHistoriesFromFile(File file) throws IOException{
        FileImporter fileImporter = FileImporterFactory.getInstance(file);
        for(int i = 0; i < excelColumnNum.getStartRow(); i++){
            fileImporter.nextLine();
        }
        int countRow = excelColumnNum.getStartRow();

        List<CostHistory> costHistoryList = new ArrayList<>();

        do{
            countRow += 1;

            CostHistory costHistory = CostHistory.builder()
                    .dateConclusion(fileImporter.getLocalDate(excelColumnNum.getCostHistoryNew().getDate()))
                    .zsDz(fileImporter.getBigDecimal(excelColumnNum.getCostHistoryNew().getZsDz()))
                    .zsZz(fileImporter.getBigDecimal(excelColumnNum.getCostHistoryNew().getZsZz()))
                    .rsDz(fileImporter.getBigDecimal(excelColumnNum.getCostHistoryNew().getRsDz()))
                    .rsZz(fileImporter.getBigDecimal(excelColumnNum.getCostHistoryNew().getRsZZ()))
                    .ss(fileImporter.getBigDecimal(excelColumnNum.getCostHistoryNew().getSs()))
                    .appraiser(fileImporter.getString(excelColumnNum.getCostHistoryNew().getAppraiser()))
                    .appraisalReportNum(fileImporter.getString(excelColumnNum.getCostHistoryNew().getNumAppraisalReport()))
                    .appraisalReportDate(fileImporter.getLocalDate(excelColumnNum.getCostHistoryNew().getDateAppraisalReport()))
                    .pledgeSubject(setPledgeSubjectInNewCostHistory(fileImporter, countRow))
                    .build();

            Set<ConstraintViolation<CostHistory>> violations =  validatorEntity.validateEntity(costHistory);
            if(!violations.isEmpty())
                throw new ConstraintViolationException("object " + countRow, violations);

            costHistoryList.add(costHistory);

        }while (fileImporter.nextLine());

        costHistoryList = updateInsertCostHistories(costHistoryList);

        return costHistoryList;
    }

    private PledgeSubject setPledgeSubjectInNewCostHistory(FileImporter fileImporter, int countRow) throws IOException {
        if(Objects.isNull(fileImporter.getLong(excelColumnNum.getCostHistoryNew().getPledgeSubjectId()))){
            throw new IOException(MSG_WRONG_ID
                    + fileImporter.getLong(excelColumnNum.getCostHistoryNew().getPledgeSubjectId())
                    + ") предмета залога. Строка: " + countRow);
        }

        return repositoryPledgeSubject
                .findById(fileImporter.getLong(excelColumnNum.getCostHistoryNew().getPledgeSubjectId()))
                .orElseThrow(() -> new IOException("Предмет залога с таким id отсутствует ("
                        + fileImporter.getLong(excelColumnNum.getCostHistoryNew().getPledgeSubjectId())
                        + MSG_LINE + countRow));
    }

    @Transactional
    public List<CostHistory> getCurrentCostHistoriesFromFile(File file) throws IOException{
        FileImporter fileImporter = FileImporterFactory.getInstance(file);
        for(int i = 0; i < excelColumnNum.getStartRow(); i++){
            fileImporter.nextLine();
        }
        int countRow = excelColumnNum.getStartRow();

        List<CostHistory> costHistoryList = new ArrayList<>();

        do{
            countRow += 1;

            CostHistory costHistory = setCurrentCostHistory(fileImporter, countRow);

            costHistory.setDateConclusion(fileImporter.getLocalDate(excelColumnNum.getCostHistoryUpdate().getDate()));
            costHistory.setZsDz(fileImporter.getBigDecimal(excelColumnNum.getCostHistoryUpdate().getZsDz()));
            costHistory.setZsZz(fileImporter.getBigDecimal(excelColumnNum.getCostHistoryUpdate().getZsZz()));
            costHistory.setRsDz(fileImporter.getBigDecimal(excelColumnNum.getCostHistoryUpdate().getRsDz()));
            costHistory.setRsZz(fileImporter.getBigDecimal(excelColumnNum.getCostHistoryUpdate().getRsZZ()));
            costHistory.setSs(fileImporter.getBigDecimal(excelColumnNum.getCostHistoryUpdate().getSs()));
            costHistory.setAppraiser(fileImporter.getString(excelColumnNum.getCostHistoryUpdate().getAppraiser()));
            costHistory.setAppraisalReportNum(fileImporter.getString(excelColumnNum.getCostHistoryUpdate().getNumAppraisalReport()));
            costHistory.setAppraisalReportDate(fileImporter.getLocalDate(excelColumnNum.getCostHistoryUpdate().getDateAppraisalReport()));
            costHistory.setPledgeSubject(setPledgeSubjectInCurrentCostHistory(fileImporter, countRow));

            Set<ConstraintViolation<CostHistory>> violations =  validatorEntity.validateEntity(costHistory);
            if(!violations.isEmpty())
                throw new ConstraintViolationException("object " + countRow, violations);

            costHistoryList.add(costHistory);

        }while (fileImporter.nextLine());

        costHistoryList = updateInsertCostHistories(costHistoryList);

        return costHistoryList;
    }

    private CostHistory setCurrentCostHistory(FileImporter fileImporter, int countRow) throws IOException {
        if(Objects.isNull(fileImporter.getLong(excelColumnNum.getCostHistoryUpdate().getCostHistoryId()))){
            throw new IOException(MSG_WRONG_ID
                    + fileImporter.getLong(excelColumnNum.getCostHistoryUpdate().getCostHistoryId())
                    + ") истории стоимотей. Строка: " + countRow);
        }

        return getCostHistoryById(fileImporter.getLong(excelColumnNum.getCostHistoryUpdate().getCostHistoryId()))
                .orElseThrow(() -> new IOException("История стоимостей с таким id отсутствует ("
                        + fileImporter.getLong(excelColumnNum.getCostHistoryUpdate().getCostHistoryId())
                        + MSG_LINE + countRow));
    }

    private PledgeSubject setPledgeSubjectInCurrentCostHistory(FileImporter fileImporter, int countRow) throws IOException{
        if(Objects.isNull(fileImporter.getLong(excelColumnNum.getCostHistoryUpdate().getPledgeSubjectId()))){
            throw new IOException(MSG_WRONG_ID
                    + fileImporter.getLong(excelColumnNum.getCostHistoryUpdate().getPledgeSubjectId())
                    + ") предмета залога. Строка: " + countRow);
        }

        return  repositoryPledgeSubject.findById(fileImporter.getLong(excelColumnNum.getCostHistoryUpdate().getPledgeSubjectId()))
                .orElseThrow(() -> new IOException("Предмет залога с таким id отсутствует ("
                        + fileImporter.getLong(excelColumnNum.getCostHistoryUpdate().getPledgeSubjectId())
                        + MSG_LINE + countRow));
    }

    @Transactional
    public CostHistory updateInsertCostHistory(CostHistory costHistory){
        return repositoryCostHistory.save(costHistory);
    }

    @Transactional
    public List<CostHistory> updateInsertCostHistories(List<CostHistory> costHistoryList){
        return repositoryCostHistory.saveAll(costHistoryList);
    }
}
