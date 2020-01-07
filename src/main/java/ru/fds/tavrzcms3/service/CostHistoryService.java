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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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

    public CostHistoryService(RepositoryCostHistory repositoryCostHistory,
                              RepositoryPledgeSubject repositoryPledgeSubject,
                              ValidatorEntity validatorEntity,
                              ExcelColumnNum excelColumnNum) {
        this.repositoryCostHistory = repositoryCostHistory;
        this.repositoryPledgeSubject = repositoryPledgeSubject;
        this.validatorEntity = validatorEntity;
        this.excelColumnNum = excelColumnNum;
    }

    public Optional<CostHistory> getCostHistoryById(Long costHistoryId){
        return repositoryCostHistory.findById(costHistoryId);
    }

    public List<CostHistory> getCostHistoryPledgeSubject(PledgeSubject pledgeSubject){
        Sort sortByDateConclusion = new Sort(Sort.Direction.DESC, "dateConclusion");
        return repositoryCostHistory.findByPledgeSubject(pledgeSubject, sortByDateConclusion);
    }

    public List<CostHistory> getCostHistoryByIds(Collection<Long> ids){
        return repositoryCostHistory.findAllByCostHistoryIdIn(ids);
    }

    public List<CostHistory> getNewCostHistorysFromFile(File file) throws IOException{
        FileImporter fileImporter = FileImporterFactory.getInstance(file);
        for(int i = 0; i < excelColumnNum.getStartRow(); i++){
            fileImporter.nextLine();
        }
        int countRow = excelColumnNum.getStartRow();

        List<CostHistory> costHistoryList = new ArrayList<>();

        do{
            countRow += 1;

            if(Objects.isNull(fileImporter.getLong(excelColumnNum.getCostHistoryNew().getPledgeSubjectId()))){
                throw new IOException("Неверный id{"
                        + fileImporter.getLong(excelColumnNum.getCostHistoryNew().getPledgeSubjectId()) + ") предмета залога.");
            }

            Optional<PledgeSubject> pledgeSubject = repositoryPledgeSubject
                    .findById(fileImporter.getLong(excelColumnNum.getCostHistoryNew().getPledgeSubjectId()));
            if(!pledgeSubject.isPresent()){
                throw new IOException("Предмет залога с таким id отсутствует ("
                        + fileImporter.getLong(excelColumnNum.getCostHistoryNew().getPledgeSubjectId())
                        + "). Строка: " + countRow);
            }

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
                    .pledgeSubject(pledgeSubject.get())
                    .build();

            Set<ConstraintViolation<CostHistory>> violations = validatorEntity.validateEntity(costHistory);
            if(!violations.isEmpty())
                throw new IOException("В строке:" + countRow + ". " + validatorEntity.getErrorMessage());

            costHistoryList.add(costHistory);

        }while (fileImporter.nextLine());

        return costHistoryList;
    }

    public List<CostHistory> getCurrentCostHistorysFromFile(File file) throws IOException{
        FileImporter fileImporter = FileImporterFactory.getInstance(file);
        for(int i = 0; i < excelColumnNum.getStartRow(); i++){
            fileImporter.nextLine();
        }
        int countRow = excelColumnNum.getStartRow();

        List<CostHistory> costHistoryList = new ArrayList<>();

        do{
            countRow += 1;

            if(Objects.isNull(fileImporter.getLong(excelColumnNum.getCostHistoryUpdate().getCostHistoryId()))){
                throw new IOException("Неверный id{"
                        + fileImporter.getLong(excelColumnNum.getCostHistoryUpdate().getCostHistoryId()) + ") истории стоимотей.");
            }

            Optional<CostHistory> costHistory = getCostHistoryById(fileImporter
                    .getLong(excelColumnNum.getCostHistoryUpdate().getCostHistoryId()));
            if(!costHistory.isPresent()){
                throw new IOException("История стоимостей с таким id отсутствует ("
                        + fileImporter.getLong(excelColumnNum.getCostHistoryUpdate().getCostHistoryId())
                        + "). Строка: " + countRow);
            }

            if(Objects.isNull(fileImporter.getLong(excelColumnNum.getCostHistoryUpdate().getPledgeSubjectId()))){
                throw new IOException("Неверный id{"
                        + fileImporter.getLong(excelColumnNum.getCostHistoryUpdate().getPledgeSubjectId()) + ") предмета залога.");
            }

            Optional<PledgeSubject> pledgeSubject = repositoryPledgeSubject
                    .findById(fileImporter.getLong(excelColumnNum.getCostHistoryUpdate().getPledgeSubjectId()));
            if(!pledgeSubject.isPresent()){
                throw new IOException("Предмет залога с таким id отсутствует ("
                        + fileImporter.getLong(excelColumnNum.getCostHistoryUpdate().getPledgeSubjectId())
                        + "). Строка: " + countRow);
            }

            costHistory.get().setDateConclusion(fileImporter.getLocalDate(excelColumnNum.getCostHistoryUpdate().getDate()));
            costHistory.get().setZsDz(fileImporter.getBigDecimal(excelColumnNum.getCostHistoryUpdate().getZsDz()));
            costHistory.get().setZsZz(fileImporter.getBigDecimal(excelColumnNum.getCostHistoryUpdate().getZsZz()));
            costHistory.get().setRsDz(fileImporter.getBigDecimal(excelColumnNum.getCostHistoryUpdate().getRsDz()));
            costHistory.get().setRsZz(fileImporter.getBigDecimal(excelColumnNum.getCostHistoryUpdate().getRsZZ()));
            costHistory.get().setSs(fileImporter.getBigDecimal(excelColumnNum.getCostHistoryUpdate().getSs()));
            costHistory.get().setAppraiser(fileImporter.getString(excelColumnNum.getCostHistoryUpdate().getAppraiser()));
            costHistory.get().setAppraisalReportNum(fileImporter.getString(excelColumnNum.getCostHistoryUpdate().getNumAppraisalReport()));
            costHistory.get().setAppraisalReportDate(fileImporter.getLocalDate(excelColumnNum.getCostHistoryUpdate().getDateAppraisalReport()));
            costHistory.get().setPledgeSubject(pledgeSubject.get());



            Set<ConstraintViolation<CostHistory>> violations = validatorEntity.validateEntity(costHistory.get());
            if(!violations.isEmpty())
                throw new IOException("В строке:" + countRow + ". " + validatorEntity.getErrorMessage());

            costHistoryList.add(costHistory.get());

        }while (fileImporter.nextLine());

        return costHistoryList;
    }

    @Transactional
    public CostHistory insertCostHistory(CostHistory costHistory){
        return repositoryCostHistory.save(costHistory);
    }

    @Transactional
    public List<CostHistory> insertCostHistories(List<CostHistory> costHistoryList){
        return repositoryCostHistory.saveAll(costHistoryList);
    }
}
