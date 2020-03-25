package ru.fds.tavrzcms3.service;

import ru.fds.tavrzcms3.domain.CostHistory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface CostHistoryService {
    Optional<CostHistory> getCostHistoryById(Long costHistoryId);
    List<CostHistory> getCostHistoryPledgeSubject(Long pledgeSubjectId);
    List<CostHistory> getNewCostHistoriesFromFile(File file) throws IOException;
    List<CostHistory> getCurrentCostHistoriesFromFile(File file) throws IOException;
    CostHistory updateInsertCostHistory(CostHistory costHistory);
    List<CostHistory> updateInsertCostHistories(List<CostHistory> costHistoryList);
}
