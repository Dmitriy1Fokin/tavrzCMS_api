package ru.fds.tavrzcms3.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.fds.tavrzcms3.annotation.LogModificationDB;
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

@RestController
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

    @GetMapping("/{id}")
    public PledgeAgreementDto getPledgeAgreement(@PathVariable Long id){
        Optional<PledgeAgreement> pledgeAgreement = pledgeAgreementService.getPledgeAgreementById(id);
        return pledgeAgreement.map(dtoFactory::getPledgeAgreementDto)
                .orElseThrow(()-> new NullPointerException("Pledge agreement not found"));
    }

    @GetMapping("/current")
    public List<PledgeAgreementDto> getCurrentPledgeAgreements(){
        return dtoFactory.getPledgeAgreementsDto(pledgeAgreementService.getAllCurrentPledgeAgreements());
    }

    @GetMapping("/current/perv")
    public List<PledgeAgreementDto> getCurrentPervPledgeAgreements(){
        return dtoFactory.getPledgeAgreementsDto(pledgeAgreementService
                .getAllCurrentPledgeAgreements(TypeOfPledgeAgreement.PERV));
    }

    @GetMapping("/current/posl")
    public List<PledgeAgreementDto> getCurrentPoslPledgeAgreements(){
        return dtoFactory.getPledgeAgreementsDto(pledgeAgreementService
                .getAllCurrentPledgeAgreements(TypeOfPledgeAgreement.POSL));
    }

    @GetMapping("/current_pa_for_client")
    public List<PledgeAgreementDto> getCurrentPledgeAgreementsByClient(@RequestParam("clientId") Long clientId){
        return dtoFactory.getPledgeAgreementsDto(pledgeAgreementService.getCurrentPledgeAgreementsByPledgor(clientId));
    }

    @GetMapping("/closed_pa_for_client")
    public List<PledgeAgreementDto> getClosedPledgeAgreementsByClient(@RequestParam("clientId") Long clientId){
        return dtoFactory.getPledgeAgreementsDto(pledgeAgreementService.getClosedPledgeAgreementsByPledgor(clientId));
    }

    @GetMapping("/with_conclusion_not_done")
    public List<PledgeAgreementDto> getPledgeAgreementsWithConclusionNotDone(@RequestParam("employeeId") Long employeeId){
        return dtoFactory.getPledgeAgreementsDto(pledgeAgreementService.getPledgeAgreementWithConclusionNotDone(employeeId));
    }

    @GetMapping("/with_conclusion_is_done")
    public List<PledgeAgreementDto> getPledgeAgreementsWithConclusionIsDone(@RequestParam("employeeId") Long employeeId){
        return dtoFactory.getPledgeAgreementsDto(pledgeAgreementService.getPledgeAgreementWithConclusionIsDone(employeeId));
    }

    @GetMapping("/with_conclusion_overdue")
    public List<PledgeAgreementDto> getPledgeAgreementsWithConclusionOverdue(@RequestParam("employeeId") Long employeeId){
        return dtoFactory.getPledgeAgreementsDto(pledgeAgreementService.getPledgeAgreementWithConclusionOverdue(employeeId));
    }

    @GetMapping("/with_monitoring_not_done")
    public List<PledgeAgreementDto> getPledgeAgreementsWithMonitoringNotDone(@RequestParam("employeeId") Long employeeId){
        return dtoFactory.getPledgeAgreementsDto(pledgeAgreementService.getPledgeAgreementWithMonitoringNotDone(employeeId));
    }

    @GetMapping("/with_monitoring_is_done")
    public List<PledgeAgreementDto> getPledgeAgreementsWithMonitoringIsDone(@RequestParam("employeeId") Long employeeId){
        return dtoFactory.getPledgeAgreementsDto(pledgeAgreementService.getPledgeAgreementWithMonitoringIsDone(employeeId));
    }

    @GetMapping("/with_monitoring_overdue")
    public List<PledgeAgreementDto> getPledgeAgreementsWithMonitoringOverdue(@RequestParam("employeeId") Long employeeId){
        return dtoFactory.getPledgeAgreementsDto(pledgeAgreementService.getPledgeAgreementWithMonitoringOverDue(employeeId));
    }

    @GetMapping("/current_pa_for_loan_agreement")
    public List<PledgeAgreementDto> getCurrentPledgeAgreementsByLoanAgreement(@RequestParam("loanAgreementId") Long loanAgreementId){
        return dtoFactory.getPledgeAgreementsDto(pledgeAgreementService.getCurrentPledgeAgreementsByLoanAgreement(loanAgreementId));
    }

    @GetMapping("/closed_pa_for_loan_agreement")
    public List<PledgeAgreementDto> getClosedPledgeAgreementsByLoanAgreement(@RequestParam("loanAgreementId") Long loanAgreementId){
        return dtoFactory.getPledgeAgreementsDto(pledgeAgreementService.getClosedPledgeAgreementsByLoanAgreement(loanAgreementId));
    }

    @GetMapping("/search_by_num")
    public List<PledgeAgreementDto> getPledgeAgreementsByNum(@RequestParam("numPA") String numPA){
        return dtoFactory.getPledgeAgreementsDto(pledgeAgreementService.getPledgeAgreementsByNumPA(numPA));
    }

    @GetMapping("/current_pa_for_employee")
    public List<PledgeAgreementDto> getCurrentPledgeAgreementByEmployee(@RequestParam("employeeId") Long employeeId){
        return dtoFactory.getPledgeAgreementsDto(pledgeAgreementService
                .getCurrentPledgeAgreementsByEmployee(employeeId));
    }

    @GetMapping("/current_pa_for_employee/perv")
    public List<PledgeAgreementDto> getCurrentPervPledgeAgreementByEmployee(@RequestParam("employeeId") Long employeeId){
        return dtoFactory.getPledgeAgreementsDto(pledgeAgreementService
                .getCurrentPledgeAgreementsByEmployee(employeeId, TypeOfPledgeAgreement.PERV));
    }

    @GetMapping("/current_pa_for_employee/posl")
    public List<PledgeAgreementDto> getCurrentPoslPledgeAgreementByEmployee(@RequestParam("employeeId") Long employeeId){
        return dtoFactory.getPledgeAgreementsDto(pledgeAgreementService
                .getCurrentPledgeAgreementsByEmployee(employeeId, TypeOfPledgeAgreement.POSL));
    }



















    @GetMapping("/pledge_agreements")
    public String pledgeAgreementPage(@RequestParam("employeeId") Optional<Long> employeeId,
                                      @RequestParam("pervPosl") Optional<String> pervPosl,
                                      @RequestParam("page") Optional<Integer> page,
                                      @RequestParam("size") Optional<Integer> size,
                                      Model model) {
        int currentPage = page.orElse(0);
        int pageSize = size.orElse(50);
        int startItem = currentPage * pageSize;

        Page<PledgeAgreementDto> pledgeAgreementDtoPage;
        if(employeeId.isPresent()){
            List<PledgeAgreement> pledgeAgreementList;
            if(pervPosl.isPresent()){
                pledgeAgreementList = pledgeAgreementService.getCurrentPledgeAgreementsByEmployee(
                        employeeId.get(),
                        TypeOfPledgeAgreement.valueOf(pervPosl.get()));
            }else{
                pledgeAgreementList = pledgeAgreementService.getCurrentPledgeAgreementsByEmployee(employeeId.get());
            }

            List<PledgeAgreement> pledgeAgreementListForPage;
            if(pledgeAgreementList.size() < startItem){
                pledgeAgreementListForPage = Collections.emptyList();
            }else {
                int toIndex = Math.min(startItem+pageSize, pledgeAgreementList.size());
                pledgeAgreementListForPage = pledgeAgreementList.subList(startItem, toIndex);
            }

            pledgeAgreementDtoPage = new PageImpl<>(
                    dtoFactory.getPledgeAgreementsDto(pledgeAgreementListForPage),
                    PageRequest.of(currentPage, pageSize),
                    pledgeAgreementList.size());
        }else {
            List<PledgeAgreement> pledgeAgreementList;
            if(pervPosl.isPresent()){
                pledgeAgreementList = pledgeAgreementService.getAllCurrentPledgeAgreements(TypeOfPledgeAgreement.valueOf(pervPosl.get()));
            }else{
                pledgeAgreementList = pledgeAgreementService.getAllCurrentPledgeAgreements();
            }

            List<PledgeAgreement> pledgeAgreementListForPage;
            if(pledgeAgreementList.size() < startItem){
                pledgeAgreementListForPage = Collections.emptyList();
            }else {
                int toIndex = Math.min(startItem+pageSize, pledgeAgreementList.size());
                pledgeAgreementListForPage = pledgeAgreementList.subList(startItem, toIndex);
            }

            pledgeAgreementDtoPage = new PageImpl<>(
                    dtoFactory.getPledgeAgreementsDto(pledgeAgreementListForPage),
                    PageRequest.of(currentPage, pageSize),
                    pledgeAgreementList.size());
        }


        model.addAttribute("pledgeAgreementList", pledgeAgreementDtoPage);
        model.addAttribute("pervPosl", pervPosl.orElse(""));
        model.addAttribute("employeeId", employeeId.orElse(null));

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

    @LogModificationDB
    @PostMapping("/update_insert")
    public String updateInsertPledgeAgreement(@AuthenticationPrincipal User user,
                                              @Valid PledgeAgreementDto pledgeAgreementDto,
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

    @LogModificationDB
    @PostMapping("withdrawFromDepositPledgeSubject")
    public @ResponseBody
    int withdrawFromDepositPledgeSubject(@AuthenticationPrincipal User user,
                                         @RequestParam("pledgeSubjectId") long pledgeSubjectId,
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

    @LogModificationDB
    @PostMapping("searchPS")
    public @ResponseBody List<PledgeSubjectDto> searchPS(@AuthenticationPrincipal User user,
                                                         @RequestParam("cadastralNum") Optional<String> cadastralNum,
                                                         @RequestParam("namePS") Optional<String> namePS){

        List<PledgeSubjectDto> pledgeSubjectDtoList = Collections.emptyList();
        if(cadastralNum.isPresent())
            pledgeSubjectDtoList = dtoFactory.getPledgeSubjectsDto(pledgeSubjectService.getPledgeSubjectByCadastralNum(cadastralNum.get()));
        else if(namePS.isPresent())
            pledgeSubjectDtoList = dtoFactory.getPledgeSubjectsDto(pledgeSubjectService.getPledgeSubjectByName(namePS.get()));

            return pledgeSubjectDtoList;
    }

    @LogModificationDB
    @PostMapping("insertPS")
    public @ResponseBody int insertCurrentPledgeSubject(@AuthenticationPrincipal User user,
                                                        @RequestParam("pledgeSubjectsIdArray[]") long[] pledgeSubjectsIdArray,
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
