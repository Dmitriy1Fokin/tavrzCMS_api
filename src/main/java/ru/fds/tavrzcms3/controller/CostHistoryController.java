package ru.fds.tavrzcms3.controller;

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
import ru.fds.tavrzcms3.validate.ValidatorEntity;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/cost_history")
public class CostHistoryController {

    private final CostHistoryService costHistoryService;
    private final ValidatorEntity validatorEntity;
    private final DtoFactory dtoFactory;


    public CostHistoryController(CostHistoryService costHistoryService,
                                 ValidatorEntity validatorEntity,
                                 DtoFactory dtoFactory) {
        this.costHistoryService = costHistoryService;
        this.validatorEntity = validatorEntity;
        this.dtoFactory = dtoFactory;
    }

    @GetMapping("/pledge_subject")
    public List<CostHistoryDto> getCostHistoryByPledgeSubject(@RequestParam("pledgeSubjectId") Long pledgeSubjectId){
        return dtoFactory.getCostHistoriesDto(costHistoryService.getCostHistoryPledgeSubject(pledgeSubjectId));
    }

    @PostMapping("/insert")
    public CostHistoryDto insertCostHistory(@Valid @RequestBody CostHistoryDto costHistoryDto){
        CostHistory costHistory = dtoFactory.getCostHistoryEntity(costHistoryDto);

        Set<ConstraintViolation<CostHistory>> violations =  validatorEntity.validateEntity(costHistory);
        if(!violations.isEmpty())
            throw new ConstraintViolationException(violations);

        costHistory = costHistoryService.insertCostHistory(costHistory);
        return dtoFactory.getCostHistoryDto(costHistory);
    }

    @PutMapping("/update")
    public CostHistoryDto updateCostHistory(@Valid @RequestBody CostHistoryDto costHistoryDto){
        return insertCostHistory(costHistoryDto);
    }
}
