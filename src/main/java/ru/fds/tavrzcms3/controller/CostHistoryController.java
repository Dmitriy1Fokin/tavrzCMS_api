package ru.fds.tavrzcms3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.fds.tavrzcms3.converver.CostHistoryConverterDto;
import ru.fds.tavrzcms3.converver.PledgeAgreementConverterDto;
import ru.fds.tavrzcms3.converver.PledgeSubjectConverterDto;
import ru.fds.tavrzcms3.domain.CostHistory;
import ru.fds.tavrzcms3.domain.Employee;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.dto.CostHistoryDto;
import ru.fds.tavrzcms3.dto.PledgeAgreementDto;
import ru.fds.tavrzcms3.dto.PledgeSubjectDto;
import ru.fds.tavrzcms3.service.CostHistoryService;
import ru.fds.tavrzcms3.service.EmployeeService;
import ru.fds.tavrzcms3.service.PledgeAgreementService;
import ru.fds.tavrzcms3.service.PledgeSubjectService;
import ru.fds.tavrzcms3.validate.ValidatorEntity;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/cost_history")
public class CostHistoryController {

    private final PledgeSubjectService pledgeSubjectService;
    private final CostHistoryService costHistoryService;
    private final EmployeeService employeeService;
    private final PledgeAgreementService pledgeAgreementService;

    private final PledgeAgreementConverterDto pledgeAgreementConverterDto;
    private final PledgeSubjectConverterDto pledgeSubjectConverterDto;
    private final CostHistoryConverterDto costHistoryConverterDto;

    private final ValidatorEntity validatorEntity;

    private static final String MSG_WRONG_LINK = "Неверная ссылка";

    public CostHistoryController(PledgeSubjectService pledgeSubjectService,
                                 CostHistoryService costHistoryService,
                                 EmployeeService employeeService,
                                 PledgeAgreementService pledgeAgreementService,
                                 PledgeAgreementConverterDto pledgeAgreementConverterDto,
                                 PledgeSubjectConverterDto pledgeSubjectConverterDto,
                                 CostHistoryConverterDto costHistoryConverterDto,
                                 ValidatorEntity validatorEntity) {
        this.pledgeSubjectService = pledgeSubjectService;
        this.costHistoryService = costHistoryService;
        this.employeeService = employeeService;
        this.pledgeAgreementService = pledgeAgreementService;
        this.pledgeAgreementConverterDto = pledgeAgreementConverterDto;
        this.pledgeSubjectConverterDto = pledgeSubjectConverterDto;
        this.costHistoryConverterDto = costHistoryConverterDto;
        this.validatorEntity = validatorEntity;
    }

    @GetMapping("/pledge_agreements")
    public String conclusionsPage(@RequestParam("employeeId") long employeeId,
                                                 Model model){

        Employee employee = employeeService.getEmployeeById(employeeId).orElseThrow(() -> new IllegalArgumentException(MSG_WRONG_LINK));

        List<PledgeAgreementDto> pledgeAgreementListWithConclusionNotDone = pledgeAgreementConverterDto
                .toDto(pledgeAgreementService.getPledgeAgreementWithConclusionNotDone(employee));
        List<PledgeAgreementDto> pledgeAgreementListWithConclusionIsDone =  pledgeAgreementConverterDto
                .toDto(pledgeAgreementService.getPledgeAgreementWithConclusionIsDone(employee));
        List<PledgeAgreementDto> pledgeAgreementListWithConclusionOverdue = pledgeAgreementConverterDto
                .toDto(pledgeAgreementService.getPledgeAgreementWithConclusionOverDue(employee));

        model.addAttribute("pledgeAgreementListWithConclusionNotDone", pledgeAgreementListWithConclusionNotDone);
        model.addAttribute("pledgeAgreementListWithConclusionIsDone", pledgeAgreementListWithConclusionIsDone);
        model.addAttribute("pledgeAgreementListWithConclusionOverdue", pledgeAgreementListWithConclusionOverdue);

        return "cost_history/pledge_agreements";
    }

    @GetMapping("/pledge_subject")
    public String costHistoryPage(@RequestParam("pledgeSubjectId") long pledgeSubjectId,
                                  Model model){

        PledgeSubject pledgeSubject = pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId)
                .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK));

        PledgeSubjectDto pledgeSubjectDto = pledgeSubjectConverterDto.toDto(pledgeSubject);

        List<CostHistoryDto> costHistoryList = costHistoryConverterDto
                .toDto(costHistoryService.getCostHistoryPledgeSubject(pledgeSubject));

        model.addAttribute("pledgeSubject", pledgeSubjectDto);
        model.addAttribute("costHistoryList", costHistoryList);

        return "cost_history/pledge_subject";
    }

    @GetMapping("/card")
    public String costHistoryCard(@RequestParam("pledgeSubjectId") long pledgeSubjectId,
                                     Model model){

        PledgeSubject pledgeSubject = pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId)
                .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK));

        CostHistoryDto costHistoryDto = CostHistoryDto.builder()
                .pledgeSubjectId(pledgeSubject.getPledgeSubjectId())
                .rsDz(pledgeSubject.getRsDz())
                .zsDz(pledgeSubject.getZsDz())
                .build();

        model.addAttribute("pledgeSubjectName", pledgeSubject.getName());
        model.addAttribute("costHistoryDto", costHistoryDto);

        return "cost_history/card";
    }

    @PostMapping("/insert")
    public String insertCostHistory(@Valid CostHistoryDto costHistoryDto,
                                      BindingResult bindingResult,
                                      @RequestParam("pledgeSubjectName") String pledgeSubjectName,
                                      Model model){
        if(bindingResult.hasErrors()) {
            model.addAttribute("pledgeSubjectName", pledgeSubjectName);
            return "cost_history/card";
        }


        CostHistory costHistory = costHistoryConverterDto.toEntity(costHistoryDto);
        Set<ConstraintViolation<CostHistory>> violations = validatorEntity.validateEntity(costHistory);
        if(!violations.isEmpty())
            throw new IllegalArgumentException(validatorEntity.getErrorMessage());

        costHistoryService.insertCostHistory(costHistory);

        return costHistoryPage(costHistory.getPledgeSubject().getPledgeSubjectId(), model);
    }
}
