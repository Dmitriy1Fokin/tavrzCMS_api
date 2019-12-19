package ru.fds.tavrzcms3.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.fds.tavrzcms3.dictionary.TypeOfPledgeAgreement;
import ru.fds.tavrzcms3.domain.PledgeAgreement;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.dto.DtoFactory;
import ru.fds.tavrzcms3.dto.EmployeeDto;
import ru.fds.tavrzcms3.dto.LoanAgreementDto;
import ru.fds.tavrzcms3.dto.PledgeAgreementDto;
import ru.fds.tavrzcms3.dto.PledgeSubjectDto;
import ru.fds.tavrzcms3.service.EmployeeService;
import ru.fds.tavrzcms3.service.LoanAgreementService;
import ru.fds.tavrzcms3.service.PledgeAgreementService;
import ru.fds.tavrzcms3.service.PledgeSubjectService;
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
@RequestMapping("/pledge_agreement")
public class PledgeAgreementController {

    private final PledgeAgreementService pledgeAgreementService;
    private final PledgeSubjectService pledgeSubjectService;
    private final EmployeeService employeeService;
    private final LoanAgreementService loanAgreementService;

    private final DtoFactory dtoFactory;

    private final ValidatorEntity validatorEntity;

    private static final String MSG_WRONG_LINK = "Неверная ссылка";
    private static final String PAGE_CARD = "pledge_agreement/card";
    private static final String ATTR_WHAT_DO = "whatDo";
    private static final String ATTR_PLEDGE_AGREEMENT = "pledgeAgreementDto";

    public PledgeAgreementController(PledgeAgreementService pledgeAgreementService,
                                     PledgeSubjectService pledgeSubjectService,
                                     EmployeeService employeeService,
                                     LoanAgreementService loanAgreementService,
                                     DtoFactory dtoFactory,
                                     ValidatorEntity validatorEntity) {
        this.pledgeAgreementService = pledgeAgreementService;
        this.pledgeSubjectService = pledgeSubjectService;
        this.employeeService = employeeService;
        this.loanAgreementService = loanAgreementService;
        this.dtoFactory = dtoFactory;
        this.validatorEntity = validatorEntity;
    }

    @GetMapping("/pledge_agreements")
    public String pledgeAgreementPage(@RequestParam("employeeId") long employeeId,
                                      @RequestParam("pervPosl") Optional<String> pervPosl,
                                      @RequestParam("page") Optional<Integer> page,
                                      @RequestParam("size") Optional<Integer> size,
                                      Model model) {
        int currentPage = page.orElse(0);
        int pageSize = size.orElse(50);
        int startItem = currentPage * pageSize;

        List<PledgeAgreement> pledgeAgreementList;
        if(pervPosl.isPresent())
            pledgeAgreementList = pledgeAgreementService.getCurrentPledgeAgreementsByEmployee(
                    employeeId,
                    TypeOfPledgeAgreement.valueOf(pervPosl.get()));
        else
            pledgeAgreementList = pledgeAgreementService.getCurrentPledgeAgreementsByEmployee(employeeId);

        List<PledgeAgreement> pledgeAgreementListForPage;
        if(pledgeAgreementList.size() < startItem){
            pledgeAgreementListForPage = Collections.emptyList();
        }else {
            int toIndex = Math.min(startItem+pageSize, pledgeAgreementList.size());
            pledgeAgreementListForPage = pledgeAgreementList.subList(startItem, toIndex);
        }

        Page<PledgeAgreementDto> pledgeAgreementDtoPage = new PageImpl<>(
                dtoFactory.getPledgeAgreementsDto(pledgeAgreementListForPage),
                PageRequest.of(currentPage, pageSize),
                pledgeAgreementList.size()
        );

        model.addAttribute("pledgeAgreementList", pledgeAgreementDtoPage);
        model.addAttribute("pervPosl", pervPosl.orElse(""));
        model.addAttribute("employeeId", employeeId);

        int totalPages = pledgeAgreementDtoPage.getTotalPages();
        if(totalPages > 0){
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "pledge_agreement/pledge_agreements";
    }

    @GetMapping("/pledge_subjects")
    public String pledgeSubjectsPage(@RequestParam("pledgeAgreementId") long pledgeAgreementId,
                                     Model model){

        PledgeAgreement pledgeAgreement = pledgeAgreementService.getPledgeAgreementById(pledgeAgreementId)
                .orElseThrow(() -> new IllegalArgumentException(MSG_WRONG_LINK));

        PledgeAgreementDto pledgeAgreementDto = dtoFactory.getPledgeAgreementDto(pledgeAgreement);

        List<PledgeSubjectDto> pledgeSubjectDtoList = dtoFactory
                .getPledgeSubjectsDto(pledgeSubjectService.getPledgeSubjectsForPledgeAgreement(pledgeAgreementId));

        model.addAttribute(ATTR_PLEDGE_AGREEMENT, pledgeAgreementDto);
        model.addAttribute("pledgeSubjectDtoList", pledgeSubjectDtoList);

        return "pledge_agreement/pledge_subjects";
    }

    @GetMapping("/detail")
    public String pledgeAgreementDetailPage(@RequestParam("pledgeAgreementId") long pledgeAgreementId,
                                            Model model){

        PledgeAgreement pledgeAgreement = pledgeAgreementService.getPledgeAgreementById(pledgeAgreementId)
                .orElseThrow(() -> new IllegalArgumentException(MSG_WRONG_LINK));

        PledgeAgreementDto pledgeAgreementDto = dtoFactory.getPledgeAgreementDto(pledgeAgreement);

        EmployeeDto employeeDto = dtoFactory
                .getEmployeeDto(employeeService.getEmployeeByPledgeAgreement(pledgeAgreementId));

        List<LoanAgreementDto> currentLoanAgreementDtoList = dtoFactory
                .getLoanAgreementsDto(loanAgreementService.getCurrentLoanAgreementsByPledgeAgreement(pledgeAgreement));

        List<LoanAgreementDto> closedLoanAgreementDtoList = dtoFactory
                .getLoanAgreementsDto(loanAgreementService.getClosedLoanAgreementsByPledgeAgreement(pledgeAgreement));


        model.addAttribute(ATTR_PLEDGE_AGREEMENT, pledgeAgreementDto);
        model.addAttribute("employeeDto", employeeDto);
        model.addAttribute("currentLoanAgreementDtoList",currentLoanAgreementDtoList);
        model.addAttribute("closedLoanAgreementDtoList", closedLoanAgreementDtoList);

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

            PledgeAgreementDto pledgeAgreementDto = dtoFactory.getPledgeAgreementDto(pledgeAgreement);

            model.addAttribute(ATTR_PLEDGE_AGREEMENT, pledgeAgreementDto);
            model.addAttribute(ATTR_WHAT_DO, whatDo);

            return PAGE_CARD;

        }else if(whatDo.equals("newPA")){

            PledgeAgreementDto pledgeAgreementDto = PledgeAgreementDto.builder()
                    .clientId(clientId.orElseThrow(() -> new IllegalArgumentException(MSG_WRONG_LINK)))
                    .build();

            model.addAttribute(ATTR_PLEDGE_AGREEMENT, pledgeAgreementDto);
            model.addAttribute(ATTR_WHAT_DO, whatDo);

            return PAGE_CARD;

        }else
            throw new IllegalArgumentException(MSG_WRONG_LINK);

    }

    @PostMapping("/update_insert")
    public String updateInsertPledgeAgreement(@Valid PledgeAgreementDto pledgeAgreementDto,
                                              BindingResult bindingResult,
                                              @RequestParam("whatDo") String whatDo,
                                              Model model){

        if(bindingResult.hasErrors()){
            model.addAttribute(ATTR_WHAT_DO, whatDo);
            return PAGE_CARD;
        }

        PledgeAgreement pledgeAgreement = dtoFactory.getPledgeAgreementEntity(pledgeAgreementDto);

        Set<ConstraintViolation<PledgeAgreement>> violations =  validatorEntity.validateEntity(pledgeAgreement);
        if(!violations.isEmpty())
            throw new IllegalArgumentException(validatorEntity.getErrorMessage());

        pledgeAgreement = pledgeAgreementService.updateInsertPledgeAgreement(pledgeAgreement);

        return pledgeAgreementDetailPage(pledgeAgreement.getPledgeAgreementId(), model);
    }

    @PostMapping("withdrawFromDepositPledgeSubject")
    public @ResponseBody
    int withdrawFromDepositPledgeSubject(@RequestParam("pledgeSubjectId") long pledgeSubjectId,
                                         @RequestParam("pledgeAgreementId") long pledgeAgreementId,
                                         Model model){

        PledgeAgreement pledgeAgreement = pledgeAgreementService.getPledgeAgreementById(pledgeAgreementId)
                .orElseThrow(() -> new IllegalArgumentException(MSG_WRONG_LINK));

        List<PledgeSubject> pledgeSubjectList = pledgeSubjectService.getPledgeSubjectsForPledgeAgreement(pledgeAgreementId);

        Optional<PledgeSubject> pledgeSubjectToRemove = pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId);
        if(pledgeSubjectToRemove.isPresent()){
            pledgeSubjectList.remove(pledgeSubjectToRemove.get());
            pledgeAgreement.setPledgeSubjects(pledgeSubjectList);
            pledgeAgreementService.updateInsertPledgeAgreement(pledgeAgreement);
        }

        return pledgeSubjectList.size();
    }

    @PostMapping("searchPS")
    public @ResponseBody List<PledgeSubjectDto> searchPS(@RequestParam("cadastralNum") Optional<String> cadastralNum,
                                                      @RequestParam("namePS") Optional<String> namePS){

        List<PledgeSubjectDto> pledgeSubjectDtoList = Collections.emptyList();
        if(cadastralNum.isPresent())
            pledgeSubjectDtoList = dtoFactory.getPledgeSubjectsDto(pledgeSubjectService.getPledgeSubjectByCadastralNum(cadastralNum.get()));
        else if(namePS.isPresent())
            pledgeSubjectDtoList = dtoFactory.getPledgeSubjectsDto(pledgeSubjectService.getPledgeSubjectByName(namePS.get()));

            return pledgeSubjectDtoList;
    }

    @PostMapping("insertPS")
    public @ResponseBody int insertCurrentPledgeSubject(@RequestParam("pledgeSubjectsIdArray[]") long[] pledgeSubjectsIdArray,
                                                        @RequestParam("pledgeAgreementId") long pledgeAgreementId){

        PledgeAgreement pledgeAgreement = pledgeAgreementService.getPledgeAgreementById(pledgeAgreementId)
                .orElseThrow(() -> new IllegalArgumentException(MSG_WRONG_LINK));

        List<PledgeSubject> pledgeSubjectList = pledgeSubjectService.getPledgeSubjectsForPledgeAgreement(pledgeAgreementId);
        int countPSBeforeUpdate = pledgeSubjectList.size();
        for(int i = 0; i < pledgeSubjectsIdArray.length; i++){
            pledgeSubjectList.add(pledgeSubjectService.getPledgeSubjectById(pledgeSubjectsIdArray[i])
                    .orElseThrow(() -> new IllegalArgumentException(MSG_WRONG_LINK)));
        }

        int countPSAfterUpdate = pledgeSubjectList.size();

        pledgeAgreement.setPledgeSubjects(pledgeSubjectList);
        pledgeAgreementService.updateInsertPledgeAgreement(pledgeAgreement);

        return countPSAfterUpdate - countPSBeforeUpdate;
    }
}
