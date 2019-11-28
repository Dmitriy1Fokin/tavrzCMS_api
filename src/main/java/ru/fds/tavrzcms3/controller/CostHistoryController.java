package ru.fds.tavrzcms3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.fds.tavrzcms3.domain.CostHistory;
import ru.fds.tavrzcms3.domain.Employee;
import ru.fds.tavrzcms3.domain.PledgeAgreement;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.service.CostHistoryService;
import ru.fds.tavrzcms3.service.EmployeeService;
import ru.fds.tavrzcms3.service.PledgeAgreementService;
import ru.fds.tavrzcms3.service.PledgeSubjectService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/cost_history")
public class CostHistoryController {

    private final PledgeSubjectService pledgeSubjectService;
    private final CostHistoryService costHistoryService;
    private final EmployeeService employeeService;
    private final PledgeAgreementService pledgeAgreementService;

    private static final String MSG_WRONG_LINK = "Неверная ссылка";

    public CostHistoryController(PledgeSubjectService pledgeSubjectService,
                                 CostHistoryService costHistoryService,
                                 EmployeeService employeeService,
                                 PledgeAgreementService pledgeAgreementService) {
        this.pledgeSubjectService = pledgeSubjectService;
        this.costHistoryService = costHistoryService;
        this.employeeService = employeeService;
        this.pledgeAgreementService = pledgeAgreementService;
    }

    @GetMapping("/pledge_agreements")
    public String conclusionsPage(@RequestParam("employeeId") long employeeId,
                                                 Model model){

        Employee employee = employeeService.getEmployeeById(employeeId).orElseThrow(() -> new RuntimeException(MSG_WRONG_LINK));
        List<PledgeAgreement> pledgeAgreementListWithConclusionNotDone = pledgeAgreementService.getPledgeAgreementWithConclusionNotDone(employee);
        List<PledgeAgreement> pledgeAgreementListWithConclusionIsDone = pledgeAgreementService.getPledgeAgreementWithConclusionIsDone(employee);
        List<PledgeAgreement> pledgeAgreementListWithConclusionOverdue = pledgeAgreementService.getPledgeAgreementWithConclusionOverDue(employee);
        model.addAttribute("pledgeAgreementListWithConclusionNotDone", pledgeAgreementListWithConclusionNotDone);
        model.addAttribute("pledgeAgreementListWithConclusionIsDone", pledgeAgreementListWithConclusionIsDone);
        model.addAttribute("pledgeAgreementListWithConclusionOverdue", pledgeAgreementListWithConclusionOverdue);

        return "cost_history/pledge_agreements";
    }

    @GetMapping("pledge_subject")
    public String costHistoryPage(@RequestParam("pledgeSubjectId") long pledgeSubjectId,
                                  Model model){
        PledgeSubject pledgeSubject = pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId)
                .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK));
        List<CostHistory> costHistoryList = costHistoryService.getCostHistoryPledgeSubject(pledgeSubject);
        model.addAttribute("pledgeSubject", pledgeSubject);
        model.addAttribute("costHistoryList", costHistoryList);

        return "cost_history/pledge_subject";
    }

    @GetMapping("/card")
    public String costHistoryCard(@RequestParam("pledgeSubjectId") long pledgeSubjectId,
                                     Model model){
        PledgeSubject pledgeSubject = pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId)
                .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK));
        CostHistory costHistory = new CostHistory();
        costHistory.setPledgeSubject(pledgeSubject);
        costHistory.setRsDz(pledgeSubject.getRsDz());
        costHistory.setZsDz(pledgeSubject.getZsDz());
        model.addAttribute("costHistory", costHistory);

        return "cost_history/card";
    }

    @PostMapping("/insert")
    public String insertCostHistory(@Valid CostHistory costHistory,
                                      BindingResult bindingResult,
                                      Model model){
        if(bindingResult.hasErrors())
            return "cost_history/card";

        CostHistory costHistoryUpdated = costHistoryService.insertCostHistory(costHistory);
        List<CostHistory> costHistoryList = costHistoryService.getCostHistoryPledgeSubject(costHistoryUpdated.getPledgeSubject());
        model.addAttribute("pledgeSubject", costHistoryUpdated.getPledgeSubject());
        model.addAttribute("costHistoryList", costHistoryList);

        return "cost_history/pledge_subject";
    }
}
