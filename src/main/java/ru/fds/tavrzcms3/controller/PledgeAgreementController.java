package ru.fds.tavrzcms3.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.fds.tavrzcms3.dictionary.TypeOfPledgeAgreement;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.Employee;
import ru.fds.tavrzcms3.domain.PledgeAgreement;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.service.ClientService;
import ru.fds.tavrzcms3.service.EmployeeService;
import ru.fds.tavrzcms3.service.PledgeAgreementService;
import ru.fds.tavrzcms3.service.PledgeSubjectService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/pledge_agreement")
public class PledgeAgreementController {

    private final EmployeeService employeeService;
    private final PledgeAgreementService pledgeAgreementService;
    private final ClientService clientService;
    private final PledgeSubjectService pledgeSubjectService;

    private static final String MSG_WRONG_LINK = "Неверная ссылка";
    private static final String ATTR_WHAT_DO = "whatDo";
    private static final String ATTR_PLEDGE_AGREEMENT = "pledgeAgreement";

    public PledgeAgreementController(EmployeeService employeeService,
                                     PledgeAgreementService pledgeAgreementService,
                                     ClientService clientService,
                                     PledgeSubjectService pledgeSubjectService) {
        this.employeeService = employeeService;
        this.pledgeAgreementService = pledgeAgreementService;
        this.clientService = clientService;
        this.pledgeSubjectService = pledgeSubjectService;
    }

    @GetMapping("/pledge_agreements")
    public String pledgeAgreementPage(@RequestParam("employeeId") long employeeId,
                                      @RequestParam("pervPosl") Optional<String> pervPosl,
                                      @RequestParam("page") Optional<Integer> page,
                                      @RequestParam("size") Optional<Integer> size,
                                      Model model) {
        Employee employee = employeeService.getEmployeeById(employeeId).orElseThrow(() -> new IllegalArgumentException(MSG_WRONG_LINK));
        int currentPage = page.orElse(0);
        int pageSize = size.orElse(50);
        Pageable pageable = PageRequest.of(currentPage, pageSize);

        Page<PledgeAgreement> pledgeAgreementList = null;
        if(pervPosl.isPresent())
            pledgeAgreementList = pledgeAgreementService.getCurrentPledgeAgreementsByEmployee(
                    employee,
                    TypeOfPledgeAgreement.valueOf(pervPosl.get()),
                    pageable);
        else
            pledgeAgreementList = pledgeAgreementService.getCurrentPledgeAgreementsByEmployee(employee, pageable);



        model.addAttribute("pledgeAgreementList", pledgeAgreementList);
        model.addAttribute("pervPosl", pervPosl.orElse(""));
        model.addAttribute("employeeId", employeeId);

        int totalPages = pledgeAgreementList.getTotalPages();
        if(totalPages > 0){
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "pledge_agreement/pledge_agreements";
    }

    @GetMapping("/pledge_subjects")
    public String pledgeSubjectsPage(@RequestParam("pledgeAgreementId") long pledgeAgreementId,
                                     Model model){
        PledgeAgreement pledgeAgreement = pledgeAgreementService.getPledgeAgreementById(pledgeAgreementId).orElseThrow(() -> new IllegalArgumentException(MSG_WRONG_LINK));
        model.addAttribute(ATTR_PLEDGE_AGREEMENT, pledgeAgreement);

        return "pledge_agreement/pledge_subjects";
    }

    @GetMapping("/detail")
    public String pledgeAgreementDetailPage(@RequestParam("pledgeAgreementId") long pledgeAgreementId,
                                            Model model){
        PledgeAgreement pledgeAgreement = pledgeAgreementService.getPledgeAgreementById(pledgeAgreementId).orElseThrow(() -> new IllegalArgumentException(MSG_WRONG_LINK));
        model.addAttribute(ATTR_PLEDGE_AGREEMENT, pledgeAgreement);

        return "pledge_agreement/detail";
    }

    @GetMapping("/card")
    public String pledgeAgreementCard(@RequestParam("pledgeAgreementId") Optional<Long> pledgeAgreementId,
                                         @RequestParam("clientId") Optional<Long> clientId,
                                         @RequestParam("whatDo") String whatDo,
                                         Model model){
        if(whatDo.equals("changePA")){
            PledgeAgreement pledgeAgreement = pledgeAgreementService.getPledgeAgreementById(pledgeAgreementId
                    .orElseThrow(() -> new IllegalArgumentException(MSG_WRONG_LINK)))
                    .orElseThrow(() -> new IllegalArgumentException(MSG_WRONG_LINK));

            model.addAttribute(ATTR_PLEDGE_AGREEMENT, pledgeAgreement);
            model.addAttribute(ATTR_WHAT_DO, whatDo);

            return "pledge_agreement/card";

        }else if(whatDo.equals("newPA")){
            PledgeAgreement pledgeAgreement = new PledgeAgreement();
            Client client = clientService.getClientById(clientId
                    .orElseThrow(() -> new IllegalArgumentException(MSG_WRONG_LINK)))
                    .orElseThrow(() -> new IllegalArgumentException(MSG_WRONG_LINK));
            pledgeAgreement.setClient(client);

            model.addAttribute(ATTR_PLEDGE_AGREEMENT, pledgeAgreement);
            model.addAttribute(ATTR_WHAT_DO, whatDo);

            return "pledge_agreement/card";

        }else
            throw new IllegalArgumentException(MSG_WRONG_LINK);

    }

    @PostMapping("/update_insert")
    public String updateInsertPledgeAgreement(@Valid PledgeAgreement pledgeAgreement,
                                          BindingResult bindingResult,
                                          @RequestParam("whatDo") String whatDo,
                                          Model model){

        if(bindingResult.hasErrors()){
            model.addAttribute(ATTR_WHAT_DO, whatDo);
            return "pledge_agreement/card";
        }

        if(whatDo.equals("changePA")){
            PledgeAgreement pa = pledgeAgreementService.updateInsertPledgeAgreement(pledgeAgreement);

            model.addAttribute(ATTR_PLEDGE_AGREEMENT, pa);

            return "pledge_agreement/detail";
        }else {
            PledgeAgreement pa = pledgeAgreementService.updateInsertPledgeAgreement(pledgeAgreement);

            model.addAttribute(ATTR_PLEDGE_AGREEMENT, pa);
            model.addAttribute(ATTR_WHAT_DO, "responseSuccess");

            return "pledge_agreement/card";
        }


    }

    @PostMapping("withdrawFromDepositPledgeSubject")
    public @ResponseBody
    int withdrawFromDepositPledgeSubject(@RequestParam("pledgeSubjectId") long pledgeSubjectId,
                                         @RequestParam("pledgeAgreementId") long pledgeAgreementId,
                                         Model model){

        PledgeAgreement pledgeAgreement = pledgeAgreementService.getPledgeAgreementById(pledgeAgreementId).orElseThrow(() -> new IllegalArgumentException(MSG_WRONG_LINK));
        List<PledgeSubject> pledgeSubjectList = pledgeSubjectService.getPledgeSubjectsForPledgeAgreement(pledgeAgreementId);
        pledgeSubjectList.remove(pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId));
        pledgeAgreement.setPledgeSubjects(pledgeSubjectList);
        pledgeAgreementService.updateInsertPledgeAgreement(pledgeAgreement);

        return pledgeSubjectList.size();
    }
}
