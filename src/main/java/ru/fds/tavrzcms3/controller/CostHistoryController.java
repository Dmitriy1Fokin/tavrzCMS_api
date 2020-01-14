package ru.fds.tavrzcms3.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.fds.tavrzcms3.domain.CostHistory;
import ru.fds.tavrzcms3.dto.CostHistoryDto;
import ru.fds.tavrzcms3.dto.DtoFactory;
import ru.fds.tavrzcms3.service.CostHistoryService;
import ru.fds.tavrzcms3.validate.validationgroup.Exist;
import ru.fds.tavrzcms3.validate.validationgroup.New;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/cost_history")
public class CostHistoryController {

    private final CostHistoryService costHistoryService;
    private final DtoFactory dtoFactory;


    public CostHistoryController(CostHistoryService costHistoryService,
                                 DtoFactory dtoFactory) {
        this.costHistoryService = costHistoryService;
        this.dtoFactory = dtoFactory;
    }

    @GetMapping("/pledge_subject")
    public List<CostHistoryDto> getCostHistoryByPledgeSubject(@RequestParam("pledgeSubjectId") long pledgeSubjectId){
        return dtoFactory.getCostHistoriesDto(costHistoryService.getCostHistoryPledgeSubject(pledgeSubjectId));
    }

    @PostMapping("/insert")
    public CostHistoryDto insertCostHistory(@Validated(New.class) @RequestBody CostHistoryDto costHistoryDto){
        CostHistory costHistory = dtoFactory.getCostHistoryEntity(costHistoryDto);
        costHistory = costHistoryService.insertCostHistory(costHistory);
        return dtoFactory.getCostHistoryDto(costHistory);
    }

    @PutMapping("/update")
    public CostHistoryDto updateCostHistory(@Validated(Exist.class) @RequestBody CostHistoryDto costHistoryDto){
        CostHistory costHistory = dtoFactory.getCostHistoryEntity(costHistoryDto);
        costHistory = costHistoryService.insertCostHistory(costHistory);
        return dtoFactory.getCostHistoryDto(costHistory);
    }
}
