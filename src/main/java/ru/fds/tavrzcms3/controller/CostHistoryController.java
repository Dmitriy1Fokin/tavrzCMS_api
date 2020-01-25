package ru.fds.tavrzcms3.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.fds.tavrzcms3.domain.CostHistory;
import ru.fds.tavrzcms3.dto.CostHistoryDto;
import ru.fds.tavrzcms3.dto.DtoFactory;
import ru.fds.tavrzcms3.service.CostHistoryService;
import ru.fds.tavrzcms3.service.FilesService;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/cost_history")
public class CostHistoryController {

    private final CostHistoryService costHistoryService;
    private final FilesService filesService;
    private final DtoFactory dtoFactory;


    public CostHistoryController(CostHistoryService costHistoryService,
                                 FilesService filesService,
                                 DtoFactory dtoFactory) {
        this.costHistoryService = costHistoryService;
        this.filesService = filesService;
        this.dtoFactory = dtoFactory;
    }

    @GetMapping("/pledge_subject")
    public List<CostHistoryDto> getCostHistoryByPledgeSubject(@RequestParam("pledgeSubjectId") Long pledgeSubjectId){
        return dtoFactory.getCostHistoriesDto(costHistoryService.getCostHistoryPledgeSubject(pledgeSubjectId));
    }

    @PostMapping("/insert")
    public CostHistoryDto insertCostHistory(@Valid @RequestBody CostHistoryDto costHistoryDto){
        CostHistory costHistory = costHistoryService
                .updateInsertCostHistory( dtoFactory.getCostHistoryEntity(costHistoryDto));

        return dtoFactory.getCostHistoryDto(costHistory);
    }

    @PutMapping("/update")
    public CostHistoryDto updateCostHistory(@Valid @RequestBody CostHistoryDto costHistoryDto){
        return insertCostHistory(costHistoryDto);
    }

    @PostMapping("/insert_from_file")
    public List<CostHistoryDto> insertCostHistoryFromFile(@RequestParam("file") MultipartFile file) throws IOException {
        File uploadFile = filesService.uploadFile(file, "cost_history_new");
        List<CostHistory> costHistoryList = costHistoryService.getNewCostHistoriesFromFile(uploadFile);

        return dtoFactory.getCostHistoriesDto(costHistoryList);
    }

    @PutMapping("/update_from_file")
    public List<CostHistoryDto> updateCostHistoryFromFile(@RequestParam("file") MultipartFile file) throws IOException {
        File uploadFile = filesService.uploadFile(file, "cost_history_update");
        List<CostHistory> costHistoryList = costHistoryService.getCurrentCostHistoriesFromFile(uploadFile);

        return dtoFactory.getCostHistoriesDto(costHistoryList);
    }
}
