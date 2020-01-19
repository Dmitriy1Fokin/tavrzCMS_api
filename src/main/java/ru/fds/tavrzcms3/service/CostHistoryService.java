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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CostHistoryService {

    private final RepositoryCostHistory repositoryCostHistory;
    private final RepositoryPledgeSubject repositoryPledgeSubject;
    private final ExcelColumnNum excelColumnNum;

    private static final String MSG_WRONG_ID = "Неверный id{";
    private static final String MSG_LINE = "). Строка: ";

    public CostHistoryService(RepositoryCostHistory repositoryCostHistory,
                              RepositoryPledgeSubject repositoryPledgeSubject,
                              ExcelColumnNum excelColumnNum) {
        this.repositoryCostHistory = repositoryCostHistory;
        this.repositoryPledgeSubject = repositoryPledgeSubject;
        this.excelColumnNum = excelColumnNum;
    }

    private Optional<CostHistory> getCostHistoryById(Long costHistoryId){
        return repositoryCostHistory.findById(costHistoryId);
    }

    public List<CostHistory> getCostHistoryPledgeSubject(Long pledgeSubjectId){
        Optional<PledgeSubject> pledgeSubject = repositoryPledgeSubject.findById(pledgeSubjectId);
        if(pledgeSubject.isPresent()){
            Sort sortByDateConclusion = new Sort(Sort.Direction.DESC, "dateConclusion");
            return repositoryCostHistory.findByPledgeSubject(pledgeSubject.get(), sortByDateConclusion);
        }
        else
            return Collections.emptyList();
    }

    public List<CostHistory> getCostHistoryByIds(Collection<Long> ids){
        return repositoryCostHistory.findAllByCostHistoryIdIn(ids);
    }

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
                    .pledgeSubject(setPledgeSubject(fileImporter, countRow))
                    .build();

            costHistoryList.add(costHistory);

        }while (fileImporter.nextLine());

        return costHistoryList;
    }

    private PledgeSubject setPledgeSubject(FileImporter fileImporter, int countRow) throws IOException {
        if(Objects.isNull(fileImporter.getLong(excelColumnNum.getCostHistoryNew().getPledgeSubjectId()))){
            throw new IOException(MSG_WRONG_ID
                    + fileImporter.getLong(excelColumnNum.getCostHistoryNew().getPledgeSubjectId()) + ") предмета залога.");
        }

        return repositoryPledgeSubject
                .findById(fileImporter.getLong(excelColumnNum.getCostHistoryNew().getPledgeSubjectId()))
                .orElseThrow(() -> new IOException("Предмет залога с таким id отсутствует ("
                        + fileImporter.getLong(excelColumnNum.getCostHistoryNew().getPledgeSubjectId())
                        + MSG_LINE + countRow));
    }

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

            costHistoryList.add(costHistory);

        }while (fileImporter.nextLine());

        return costHistoryList;
    }

    private CostHistory setCurrentCostHistory(FileImporter fileImporter, int countRow) throws IOException {
        if(Objects.isNull(fileImporter.getLong(excelColumnNum.getCostHistoryUpdate().getCostHistoryId()))){
            throw new IOException(MSG_WRONG_ID
                    + fileImporter.getLong(excelColumnNum.getCostHistoryUpdate().getCostHistoryId()) + ") истории стоимотей.");
        }

        return getCostHistoryById(fileImporter.getLong(excelColumnNum.getCostHistoryUpdate().getCostHistoryId()))
                .orElseThrow(() -> new IOException("История стоимостей с таким id отсутствует ("
                        + fileImporter.getLong(excelColumnNum.getCostHistoryUpdate().getCostHistoryId())
                        + MSG_LINE + countRow));
    }

    private PledgeSubject setPledgeSubjectInCurrentCostHistory(FileImporter fileImporter, int countRow) throws IOException{
        if(Objects.isNull(fileImporter.getLong(excelColumnNum.getCostHistoryUpdate().getPledgeSubjectId()))){
            throw new IOException(MSG_WRONG_ID
                    + fileImporter.getLong(excelColumnNum.getCostHistoryUpdate().getPledgeSubjectId()) + ") предмета залога.");
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
