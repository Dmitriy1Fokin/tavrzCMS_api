package ru.fds.tavrzcms3.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.fds.tavrzcms3.converver.EmployeeConverterDto;
import ru.fds.tavrzcms3.converver.LoanAgreementConverterDto;
import ru.fds.tavrzcms3.converver.PledgeAgreementConverterDto;
import ru.fds.tavrzcms3.domain.Employee;
import ru.fds.tavrzcms3.domain.LoanAgreement;
import ru.fds.tavrzcms3.dto.EmployeeDto;
import ru.fds.tavrzcms3.dto.LoanAgreementDto;
import ru.fds.tavrzcms3.dto.PledgeAgreementDto;
import ru.fds.tavrzcms3.service.EmployeeService;
import ru.fds.tavrzcms3.service.LoanAgreementService;
import ru.fds.tavrzcms3.service.PledgeAgreementService;
import ru.fds.tavrzcms3.validate.ValidatorEntity;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/loan_agreement")
public class LoanAgreementController {

    private final EmployeeService employeeService;
    private final LoanAgreementService loanAgreementService;
    private final PledgeAgreementService pledgeAgreementService;

    private final LoanAgreementConverterDto loanAgreementConverterDto;
    private final EmployeeConverterDto employeeConverterDto;
    private final PledgeAgreementConverterDto pledgeAgreementConverterDto;

    private final ValidatorEntity validatorEntity;

    private static final String MSG_WRONG_LINK = "Неверная ссылка";
    private static final String PAGE_CARD = "loan_agreement/card";
    private static final String ATTR_LOAN_AGREEMENT = "loanAgreementDto";
    private static final String ATTR_WHAT_DO = "whatDo";

    public LoanAgreementController(EmployeeService employeeService,
                                   LoanAgreementService loanAgreementService,
                                   PledgeAgreementService pledgeAgreementService,
                                   LoanAgreementConverterDto loanAgreementConverterDto,
                                   EmployeeConverterDto employeeConverterDto,
                                   PledgeAgreementConverterDto pledgeAgreementConverterDto,
                                   ValidatorEntity validatorEntity) {
        this.employeeService = employeeService;
        this.loanAgreementService = loanAgreementService;
        this.pledgeAgreementService = pledgeAgreementService;
        this.loanAgreementConverterDto = loanAgreementConverterDto;
        this.employeeConverterDto = employeeConverterDto;
        this.pledgeAgreementConverterDto = pledgeAgreementConverterDto;
        this.validatorEntity = validatorEntity;
    }

    @GetMapping("/loan_agreements")
    public String loanAgreementsPage(@RequestParam("employeeId") long employeeId,
                                     @RequestParam("page") Optional<Integer> page,
                                     @RequestParam("size") Optional<Integer> size,
                                     Model model) {

        int currentPage = page.orElse(0);
        int pageSize = size.orElse(50);
        int startItem = currentPage * pageSize;

        Employee employee = employeeService.getEmployeeById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException(MSG_WRONG_LINK));
        List<LoanAgreement> loanAgreementList = loanAgreementService.getCurrentLoanAgreementsByEmployee(employee);

        List<LoanAgreement> loanAgreementListForPage;
        if(loanAgreementList.size() < startItem){
            loanAgreementListForPage = Collections.emptyList();
        }else {
            int toIndex = Math.min(startItem+pageSize, loanAgreementList.size());
            loanAgreementListForPage = loanAgreementList.subList(startItem, toIndex);
        }

        Page<LoanAgreementDto> loanAgreementDtoPage = new PageImpl<>(
                loanAgreementConverterDto.toDto(loanAgreementListForPage),
                PageRequest.of(currentPage, pageSize),
                loanAgreementList.size());

        model.addAttribute("loanAgreementList", loanAgreementDtoPage);
        model.addAttribute("employeeId", employeeId);

        int totalPages = loanAgreementDtoPage.getTotalPages();
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
        LoanAgreementDto loanAgreementDto = loanAgreementConverterDto.toDto(loanAgreement);

        EmployeeDto employeeDto = employeeConverterDto.toDto(employeeService.getEmployeeByLoanAgreement(loanAgreementId));

        List<PledgeAgreementDto> currentPledgeAgreementDtoList = pledgeAgreementConverterDto
                .toDto(pledgeAgreementService.getCurrentPledgeAgreementsByLoanAgreement(loanAgreement));

        List<PledgeAgreementDto> closedPledgeAgreementDtoList = pledgeAgreementConverterDto
                .toDto(pledgeAgreementService.getClosedPledgeAgreementsByLoanAgreement(loanAgreement));

        model.addAttribute(ATTR_LOAN_AGREEMENT, loanAgreementDto);
        model.addAttribute("employeeDto", employeeDto);
        model.addAttribute("currentPledgeAgreementDtoList", currentPledgeAgreementDtoList);
        model.addAttribute("closedPledgeAgreementDtoList", closedPledgeAgreementDtoList);

        return "loan_agreement/detail";
    }

    @GetMapping("/card")
    public String loanAgreementCardPage(@RequestParam("loanAgreementId") Optional<Long> loanAgreementId,
                                        @RequestParam("clientId") Optional<Long> clientId,
                                        @RequestParam("whatDo") String whatDo,
                                           Model model){
        if(whatDo.equals("changeLA")){
            LoanAgreement loanAgreement = loanAgreementService.getLoanAgreementById(loanAgreementId
                    .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK)))
                    .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK));

            LoanAgreementDto loanAgreementDto = loanAgreementConverterDto.toDto(loanAgreement);

            model.addAttribute(ATTR_LOAN_AGREEMENT, loanAgreementDto);
            model.addAttribute(ATTR_WHAT_DO, whatDo);

            return PAGE_CARD;

        }else if(whatDo.equals("newLA")){

            LoanAgreementDto loanAgreementDto = LoanAgreementDto.builder()
                    .clientId(clientId.orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK)))
                    .build();

            model.addAttribute(ATTR_LOAN_AGREEMENT, loanAgreementDto);
            model.addAttribute(ATTR_WHAT_DO, whatDo);

            return PAGE_CARD;

        }else
            throw new IllegalArgumentException(MSG_WRONG_LINK);
    }

    @PostMapping("/update_insert")
    public String updateInsertLoanAgreement(@Valid LoanAgreementDto loanAgreementDto,
                                            BindingResult bindingResult,
                                            @RequestParam("whatDo") String whatDo,
                                            Model model){

        if(bindingResult.hasErrors()){
            model.addAttribute(ATTR_WHAT_DO, whatDo);
            return PAGE_CARD;
        }


        LoanAgreement loanAgreement = loanAgreementConverterDto.toEntity(loanAgreementDto);

        Set<ConstraintViolation<LoanAgreement>> violations =  validatorEntity.validateEntity(loanAgreement);
        if(!violations.isEmpty())
            throw new IllegalArgumentException(validatorEntity.getErrorMessage());

        loanAgreement = loanAgreementService.updateInsertLoanAgreement(loanAgreement);

        return loanAgreementDetailPage(loanAgreement.getLoanAgreementId(), model);
    }
}
