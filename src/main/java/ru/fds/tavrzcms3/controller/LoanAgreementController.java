package ru.fds.tavrzcms3.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.Employee;
import ru.fds.tavrzcms3.domain.LoanAgreement;
import ru.fds.tavrzcms3.service.ClientService;
import ru.fds.tavrzcms3.service.EmployeeService;
import ru.fds.tavrzcms3.service.LoanAgreementService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/loan_agreement")
public class LoanAgreementController {

    private final EmployeeService employeeService;
    private final LoanAgreementService loanAgreementService;
    private final ClientService clientService;

    private static final String MSG_WRONG_LINK = "Неверная ссылка";
    private static final String ATTR_LOAN_AGREEMENT = "loanAgreement";
    private static final String ATTR_WHAT_DO = "whatDo";

    public LoanAgreementController(EmployeeService employeeService,
                                   LoanAgreementService loanAgreementService,
                                   ClientService clientService) {
        this.employeeService = employeeService;
        this.loanAgreementService = loanAgreementService;
        this.clientService = clientService;
    }

    @GetMapping("/loan_agreements")
    public String loanAgreementsPage(@RequestParam("employeeId") long employeeId,
                                     @RequestParam("page") Optional<Integer> page,
                                     @RequestParam("size") Optional<Integer> size,
                                     Model model) {

        int currentPage = page.orElse(0);
        int pageSize = size.orElse(50);
        Pageable pageable = PageRequest.of(currentPage, pageSize);
        Employee employee = employeeService.getEmployeeById(employeeId).orElseThrow(() -> new RuntimeException(MSG_WRONG_LINK));
        Page<LoanAgreement> loanAgreementList = loanAgreementService.getCurrentLoanAgreementsByEmployee(pageable, employee);

        model.addAttribute("loanAgreementList", loanAgreementList);
        model.addAttribute("employeeId", employee.getEmployeeId());

        int totalPages = loanAgreementList.getTotalPages();
        if(totalPages > 0){
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "loan_agreement/loan_agreements";
    }

    @GetMapping("/detail")
    public String loanAgreementDetailPage(@RequestParam("loanAgreementId") long loanAgreementId,
                                          Model model){
        LoanAgreement loanAgreement = loanAgreementService.getLoanAgreementById(loanAgreementId)
                .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK));
        model.addAttribute(ATTR_LOAN_AGREEMENT, loanAgreement);

        return "loan_agreement/detail";
    }

    @GetMapping("/card")
    public String loanAgreementCardPageGet(@RequestParam("loanAgreementId") Optional<Long> loanAgreementId,
                                           @RequestParam("clientId") Optional<Long> clientId,
                                           @RequestParam("whatDo") String whatDo,
                                           Model model){
        if(whatDo.equals("changeLA")){
            LoanAgreement loanAgreement = loanAgreementService.getLoanAgreementById(loanAgreementId
                    .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK)))
                    .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK));

            model.addAttribute(ATTR_LOAN_AGREEMENT, loanAgreement);
            model.addAttribute(ATTR_WHAT_DO, whatDo);

            return "loan_agreement/card";

        }else if(whatDo.equals("newLA")){
            Client client = clientService.getClientById(clientId
                    .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK)))
                    .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK));

            LoanAgreement loanAgreement = new LoanAgreement();
            loanAgreement.setClient(client);

            model.addAttribute(ATTR_LOAN_AGREEMENT, loanAgreement);
            model.addAttribute(ATTR_WHAT_DO, whatDo);

            return "loan_agreement/card";
        }else
            throw new IllegalArgumentException(MSG_WRONG_LINK);

    }

    @PostMapping("/update_insert")
    public String loanAgreementCardPagePost(@Valid LoanAgreement loanAgreement,
                                            BindingResult bindingResult,
                                            @RequestParam("whatDo") String whatDo,
                                            Model model){

        if(bindingResult.hasErrors()){
            model.addAttribute(ATTR_WHAT_DO, whatDo);
            return "loan_agreement/card";
        }

        if(whatDo.equals("changeLA")){
            LoanAgreement loanAgreementUpdated = loanAgreementService.updateInsertLoanAgreement(loanAgreement);

            model.addAttribute(ATTR_LOAN_AGREEMENT, loanAgreementUpdated);

            return "loan_agreement/detail";

        }else  if(whatDo.equals("newLA")){
            LoanAgreement loanAgreementUpdated = loanAgreementService.updateInsertLoanAgreement(loanAgreement);

            model.addAttribute(ATTR_LOAN_AGREEMENT, loanAgreementUpdated);
            model.addAttribute(ATTR_WHAT_DO, "responseSuccess");

            return "loan_agreement/card";

        }else
            throw new IllegalArgumentException(MSG_WRONG_LINK);
    }


}
