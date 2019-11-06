package ru.fds.tavrzcms3.controller;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.fds.tavrzcms3.service.*;
import ru.fds.tavrzcms3.domain.*;

import javax.validation.Valid;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Controller
public class PagesController {

    private final EmployeeService employeeService;
    private final PledgeAgreementService pledgeAgreementService;
    private final PledgeSubjectService pledgeSubjectService;
    private final InsuranceService insuranceService;
    private final EncumbranceService encumbranceService;
    private final ClientService clientService;
    private final LoanAgreementService loanAgreementService;
    private final CostHistoryService costHistoryService;
    private final MonitoringService monitoringService;
    private final LandCategoryService landCategoryService;
    private final MarketSegmentService marketSegmentService;
    private final ClientManagerService clientManagerService;
    private final FilesService filesService;


    public PagesController(EmployeeService employeeService,
                           PledgeAgreementService pledgeAgreementService,
                           PledgeSubjectService pledgeSubjectService,
                           InsuranceService insuranceService,
                           EncumbranceService encumbranceService,
                           ClientService clientService,
                           LoanAgreementService loanAgreementService,
                           CostHistoryService costHistoryService,
                           MonitoringService monitoringService,
                           LandCategoryService landCategoryService,
                           MarketSegmentService marketSegmentService,
                           ClientManagerService clientManagerService,
                           FilesService filesService) {
        this.employeeService = employeeService;
        this.pledgeAgreementService = pledgeAgreementService;
        this.pledgeSubjectService = pledgeSubjectService;
        this.insuranceService = insuranceService;
        this.encumbranceService = encumbranceService;
        this.clientService = clientService;
        this.loanAgreementService = loanAgreementService;
        this.costHistoryService = costHistoryService;
        this.monitoringService = monitoringService;
        this.landCategoryService = landCategoryService;
        this.marketSegmentService = marketSegmentService;
        this.clientManagerService = clientManagerService;
        this.filesService = filesService;
    }

    @GetMapping("/login")
    public String login() {
        return "/login";
    }

    @GetMapping("/")
    public String homePage(@AuthenticationPrincipal User user, Model model) {
        Employee employee = employeeService.getEmployee(user);
        model.addAttribute("employee", employee);

        int countOfPA = pledgeAgreementService.countOfCurrentPledgeAgreementForEmployee(employee);
        model.addAttribute("countOfAllPledgeAgreement", countOfPA);

        int countOfPervPA = pledgeAgreementService.countOfCurrentPledgeAgreementForEmployee(employee, "перв");
        model.addAttribute("countOfPervPledgeAgreements", countOfPervPA);

        int countOfPoslPA = pledgeAgreementService.countOfCurrentPledgeAgreementForEmployee(employee, "посл");
        model.addAttribute("countOfPoslPledgeAgreements", countOfPoslPA);

        int countOfLoanAgreements = loanAgreementService.countOfCurrentLoanAgreementsForEmployee(employee);
        model.addAttribute("countOfLoanAgreements", countOfLoanAgreements);

        int countOfMonitoringNotDone = pledgeAgreementService.countOfMonitoringNotDone(employee);
        model.addAttribute("countOfMonitoringNotDone", countOfMonitoringNotDone);

        int countOfMonitoringIsDone = pledgeAgreementService.countOfMonitoringIsDone(employee);
        model.addAttribute("countOfMonitoringIsDone", countOfMonitoringIsDone);

        int countOfMonitoringOverdue = pledgeAgreementService.countOfMonitoringOverdue(employee);
        model.addAttribute("countOfMonitoringOverdue", countOfMonitoringOverdue);

        int countOfConclusionNotDone = pledgeAgreementService.countOfConclusionNotDone(employee);
        model.addAttribute("countOfConclusionNotDone", countOfConclusionNotDone);

        int countOfConclusionIsDone = pledgeAgreementService.countOfConclusionIsDone(employee);
        model.addAttribute("countOfConclusionIsDone", countOfConclusionIsDone);

        int countOfConclusionOverdue = pledgeAgreementService.countOfConclusionOverdue(employee);
        model.addAttribute("countOfConclusionOverdue", countOfConclusionOverdue);

        return "home";
    }

    @GetMapping("/pledge_agreements")
    public String pledgeAgreementPage(@RequestParam("employeeId") long employeeId,
                                      @RequestParam("pervPosl") Optional<String> pervPosl,
                                      @RequestParam("page") Optional<Integer> page,
                                      @RequestParam("size") Optional<Integer> size,
                                      Model model) {
        Employee employee = employeeService.getEmployee(employeeId).orElseThrow(() -> new RuntimeException("Неверная ссылка"));
        int currentPage = page.orElse(0);
        int pageSize = size.orElse(50);
        Pageable pageable = PageRequest.of(currentPage, pageSize);

        Page<PledgeAgreement> pledgeAgreementList = null;
        if(pervPosl.isPresent())
            pledgeAgreementList = pledgeAgreementService.getCurrentPledgeAgreementsForEmployee(employee, pervPosl.get(), pageable);
        else
            pledgeAgreementList = pledgeAgreementService.getCurrentPledgeAgreementsForEmployee(employee, pageable);



        model.addAttribute("pledgeAgreementList", pledgeAgreementList);
        model.addAttribute("pervPosl", pervPosl.orElse(null));
        model.addAttribute("employeeId", employeeId);

        int totalPages = pledgeAgreementList.getTotalPages();
        if(totalPages > 0){
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "pledge_agreements";
    }

    @GetMapping("/pledge_subjects")
    public String pledgeSubjectsPage(@RequestParam("pledgeAgreementId") long pledgeAgreementId,
                                     Model model){
        PledgeAgreement pledgeAgreement = pledgeAgreementService.getPledgeAgreement(pledgeAgreementId);
        model.addAttribute("pledgeAgreement", pledgeAgreement);

        return "pledge_subjects";
    }

    @GetMapping("/insurances")
    public String insurancesPage(@RequestParam("pledgeSubjectId") long pledgeSubjectId,
                                 Model model){
        PledgeSubject pledgeSubject = pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId);
        List<Insurance> insuranceList = insuranceService.getInsurancesByPledgeSubject(pledgeSubject);
        model.addAttribute("pledgeSubject", pledgeSubject);
        model.addAttribute("insuranceList", insuranceList);

        return "insurances";
    }

    @PostMapping("/insurances")
    public String insuranceInsert(@Valid Insurance insurance,
                                  BindingResult bindingResult,
                                  Model model){

        if(bindingResult.hasErrors())
            return "insurance_card";

        Insurance insuranceInserted = insuranceService.updateInsertInsurance(insurance);
        List <Insurance> insuranceList = insuranceService.getInsurancesByPledgeSubject(insuranceInserted.getPledgeSubject());
        model.addAttribute("pledgeSubject", insuranceInserted.getPledgeSubject());
        model.addAttribute("insuranceList", insuranceList);

        return "insurances";
    }

    @GetMapping("/insurance_card")
    public String insuranceCardGet(@RequestParam("pledgeSubjectId") long pledgeSubjectId,
                                   Model model){
        Insurance insurance = new Insurance();
        insurance.setPledgeSubject(pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId));
        model.addAttribute("insurance", insurance);

        return "insurance_card";
    }

    @GetMapping("/encumbrances")
    public  String encumbrancePage(@RequestParam("pledgeSubjectId") long pledgeSubjectId,
                                   Model model){
        PledgeSubject pledgeSubject = pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId);
        List<Encumbrance> encumbranceList = encumbranceService.getEncumbranceByPledgeSubject(pledgeSubject);
        model.addAttribute("pledgeSubject", pledgeSubject);
        model.addAttribute("encumbranceList", encumbranceList);

        return "encumbrances";
    }

    @GetMapping("/encumbrance_card")
    public String encumbranceCardPageGet(@RequestParam("pledgeSubjectId") long pledgeSubjectId,
                                         Model model){

        Encumbrance encumbrance = new Encumbrance();
        encumbrance.setPledgeSubject(pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId));
        model.addAttribute("encumbrance", encumbrance);

        return "encumbrance_card";
    }

    @PostMapping("/encumbrance_card")
    public String encumbranceCardPagePost(@Valid Encumbrance encumbrance,
                                          BindingResult bindingResult,
                                          Model model){

        if(bindingResult.hasErrors())
            return "encumbrance_card";

        Encumbrance encumbranceInserted = encumbranceService.updateInsertEncumbrance(encumbrance);
        List<Encumbrance> encumbranceList = encumbranceService.getEncumbranceByPledgeSubject(encumbranceInserted.getPledgeSubject());
        model.addAttribute("pledgeSubject", encumbranceInserted.getPledgeSubject());
        model.addAttribute("encumbranceList", encumbranceList);

        return "encumbrances";
    }

    @GetMapping("/pledge_subject_detail")
    public String pledgeSubjectDetailPage(@RequestParam("pledgeSubjectId") long pledgeSubjectId,
                                          Model model){
        PledgeSubject pledgeSubject = pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId);
        model.addAttribute("pledgeSubject", pledgeSubject);

        return "pledge_subject_detail";
    }

    @GetMapping("/pledge_agreement_detail")
    public String pledgeAgreementDetailPage(@RequestParam("pledgeAgreementId") long pledgeAgreementId,
                                            Model model){
        PledgeAgreement pledgeAgreement = pledgeAgreementService.getPledgeAgreement(pledgeAgreementId);
        model.addAttribute("pledgeAgreement", pledgeAgreement);

        return "pledge_agreement_detail";
    }

    @GetMapping("/loan_agreement_detail")
    public String loanAgreementDetailPage(@RequestParam("loanAgreementId") long loanAgreementId,
                                          Model model){
        LoanAgreement loanAgreement = loanAgreementService.getLoanAgreementById(loanAgreementId).orElseThrow(() -> new RuntimeException("Неверная ссылка"));
        model.addAttribute("loanAgreement", loanAgreement);

        return "loan_agreement_detail";
    }

    @GetMapping("cost_history")
    public String costHistoryPage(@RequestParam("pledgeSubjectId") long pledgeSubjectId,
                                  Model model){
        PledgeSubject pledgeSubject = pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId);
        List<CostHistory> costHistoryList = costHistoryService.getCostHistoryPledgeSubject(pledgeSubject);
        model.addAttribute("pledgeSubject", pledgeSubject);
        model.addAttribute("costHistoryList", costHistoryList);

        return "cost_history";
    }

    @GetMapping("monitoring_pledge_subject")
    public String monitoringPage(@RequestParam("pledgeSubjectId") long pledgeSubjectId,
                                 Model model){
        PledgeSubject pledgeSubject = pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId);
        List<Monitoring> monitoringList = monitoringService.getMonitoringByPledgeSubject(pledgeSubject);
        model.addAttribute("pledgeSubject", pledgeSubject);
        model.addAttribute("monitoringList", monitoringList);

        return "monitoring_pledge_subject";
    }

    @GetMapping("/loan_agreements")
    public String loanAgreementsPage(@RequestParam("employeeId") long employeeId,
                                     @RequestParam("page") Optional<Integer> page,
                                     @RequestParam("size") Optional<Integer> size,
                                     Model model) {

        int currentPage = page.orElse(0);
        int pageSize = size.orElse(50);
        Pageable pageable = PageRequest.of(currentPage, pageSize);
        Page<LoanAgreement> loanAgreementList = loanAgreementService.getCurrentLoanAgreementsForEmployee(pageable, employeeId);

        model.addAttribute("loanAgreementList", loanAgreementList);
        model.addAttribute("employeeId", employeeId);

        int totalPages = loanAgreementList.getTotalPages();
        if(totalPages > 0){
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "loan_agreements";
    }

    @GetMapping("/monitoring_pledge_agreements")
    public String monitoringPledgeAgreementsPage(@RequestParam("employeeId") long employeeId,
                                                 Model model){

        Employee employee = employeeService.getEmployee(employeeId).orElseThrow(() -> new RuntimeException("Неверная ссылка"));
        List<PledgeAgreement> pledgeAgreementListWithMonitoringNotDone = pledgeAgreementService.getPledgeAgreementWithMonitoringNotDone(employee);
        List<PledgeAgreement> pledgeAgreementListWithMonitoringIsDone = pledgeAgreementService.getPledgeAgreementWithMonitoringIsDone(employee);
        List<PledgeAgreement> pledgeAgreementListWithMonitoringOverdue = pledgeAgreementService.getPledgeAgreementWithMonitoringOverDue(employee);
        model.addAttribute("pledgeAgreementListWithMonitoringNotDone", pledgeAgreementListWithMonitoringNotDone);
        model.addAttribute("pledgeAgreementListWithMonitoringIsDone", pledgeAgreementListWithMonitoringIsDone);
        model.addAttribute("pledgeAgreementListWithMonitoringOverdue", pledgeAgreementListWithMonitoringOverdue);

        return "monitoring_pledge_agreements";
    }

    @GetMapping("/conclusion_pledge_agreements")
    public String conclusionPledgeAgreementsPage(@RequestParam("employeeId") long employeeId,
                                                 Model model){

        Employee employee = employeeService.getEmployee(employeeId).orElseThrow(() -> new RuntimeException("Неверная ссылка"));
        List<PledgeAgreement> pledgeAgreementListWithConclusionNotDone = pledgeAgreementService.getPledgeAgreementWithConclusionNotDone(employee);
        List<PledgeAgreement> pledgeAgreementListWithConclusionIsDone = pledgeAgreementService.getPledgeAgreementWithConclusionIsDone(employee);
        List<PledgeAgreement> pledgeAgreementListWithConclusionOverdue = pledgeAgreementService.getPledgeAgreementWithConclusionOverDue(employee);
        model.addAttribute("pledgeAgreementListWithConclusionNotDone", pledgeAgreementListWithConclusionNotDone);
        model.addAttribute("pledgeAgreementListWithConclusionIsDone", pledgeAgreementListWithConclusionIsDone);
        model.addAttribute("pledgeAgreementListWithConclusionOverdue", pledgeAgreementListWithConclusionOverdue);

        return "conclusion_pledge_agreements";
    }

    @GetMapping("/search")
    public String searchPage(){
        return "search";
    }

    @GetMapping("/search_results")
    public String searchResultsPage(@RequestParam Map<String, String> reqParam,
                                    Model model){
//        reqParam.forEach((k, v) -> System.out.println(k + " : " + v));
        switch (reqParam.get("typeOfSearch")){

            case "searchLA":
                Page<LoanAgreement> loanAgreementList = loanAgreementService.getLoanAgreementFromSearch(reqParam);
                model.addAttribute("resultList", loanAgreementList);

                int totalPagesLA = loanAgreementList.getTotalPages();
                if(totalPagesLA > 0){
                    List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPagesLA).boxed().collect(Collectors.toList());
                    model.addAttribute("pageNumbers", pageNumbers);
                }

                reqParam.remove("page");
                model.addAttribute("reqParam", reqParam);

                break;

            case "searchPA":
                Page<PledgeAgreement> pledgeAgreementList = pledgeAgreementService.getPledgeAgreementFromSearch(reqParam);
                model.addAttribute("resultList", pledgeAgreementList);

                int totalPagesPA = pledgeAgreementList.getTotalPages();
                if(totalPagesPA > 0){
                    List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPagesPA).boxed().collect(Collectors.toList());
                    model.addAttribute("pageNumbers", pageNumbers);
                }

                reqParam.remove("page");
                model.addAttribute("reqParam", reqParam);

                break;

            case "searchPS":

                Page<PledgeSubject> pledgeSubjectList = pledgeSubjectService.getPledgeSubjectsFromSearch(reqParam);
                model.addAttribute("resultList", pledgeSubjectList);

                int totalPagesPS = pledgeSubjectList.getTotalPages();
                if(totalPagesPS > 0){
                    List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPagesPS).boxed().collect(Collectors.toList());
                    model.addAttribute("pageNumbers", pageNumbers);
                }

                reqParam.remove("page");
                model.addAttribute("reqParam", reqParam);

                break;

            case "searchClient":

                Page<Client> clientPage = clientService.getClientFromSearch(reqParam);
                model.addAttribute("resultList", clientPage);

                int totalPagesClient = clientPage.getTotalPages();
                if(totalPagesClient > 0){
                    List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPagesClient).boxed().collect(Collectors.toList());
                    model.addAttribute("pageNumbers", pageNumbers);
                }

                reqParam.remove("page");
                model.addAttribute("reqParam", reqParam);

                break;
        }

        return "search_results";
    }

    @GetMapping("/monitoring_card")
    public String monitoringCardPageGet(@RequestParam("whereUpdateMonitoring") String whereUpdateMonitoring,
                                        @RequestParam("pledgeAgreementId") Optional<Long> pledgeAgreementId,
                                        @RequestParam("pledgeSubjectId") Optional<Long> pledgeSubjectId,
                                        @RequestParam("pledgorId") Optional<Long> pledgorId,
                                        Model model){
        switch(whereUpdateMonitoring) {
            case "pa":
                PledgeAgreement pledgeAgreement = pledgeAgreementService.getPledgeAgreement(pledgeAgreementId.get());
                model.addAttribute("whereUpdateMonitoring", whereUpdateMonitoring);
                model.addAttribute("pledgeAgreement", pledgeAgreement);
                model.addAttribute("monitoring", new Monitoring());
                break;
            case "ps":
                PledgeSubject pledgeSubject = pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId.get());
                model.addAttribute("whereUpdateMonitoring", whereUpdateMonitoring);
                model.addAttribute("pledgeSubject", pledgeSubject);
                model.addAttribute("monitoring", new Monitoring());
                break;
            case "pledgor":
                Client pledgor = clientService.getClientById(pledgorId.get()).orElseThrow(() -> new RuntimeException("Неверная ссылка"));
                model.addAttribute("whereUpdateMonitoring", whereUpdateMonitoring);
                model.addAttribute("pledgor", pledgor);
                model.addAttribute("monitoring", new Monitoring());
                break;
        }
        return "monitoring_card";
    }

    @PostMapping("/monitoring_card")
    public String monitoringCardPagePost(@Valid Monitoring monitoring,
                                         BindingResult bindingResult,
                                         @RequestParam("whereUpdateMonitoring") String whereUpdateMonitoring,
                                         @RequestParam("pledgeAgreementId") Optional<Long> pledgeAgreementId,
                                         @RequestParam("pledgeSubjectId") Optional<Long> pledgeSubjectId,
                                         @RequestParam("pledgorId") Optional<Long> pledgorId,
                                         Model model){

        if(bindingResult.hasErrors()) {
            switch(whereUpdateMonitoring) {
                case "pa":
                    PledgeAgreement pledgeAgreement = pledgeAgreementService.getPledgeAgreement(pledgeAgreementId.get());
                    model.addAttribute("whereUpdateMonitoring", whereUpdateMonitoring);
                    model.addAttribute("pledgeAgreement", pledgeAgreement);
                    break;
                case "ps":
                    PledgeSubject pledgeSubject = pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId.get());
                    model.addAttribute("whereUpdateMonitoring", whereUpdateMonitoring);
                    model.addAttribute("pledgeSubject", pledgeSubject);
                    break;
                case "pledgor":
                    Client pledgor = clientService.getClientById(pledgorId.get()).orElseThrow(() -> new RuntimeException("Неверная ссылка"));
                    model.addAttribute("whereUpdateMonitoring", whereUpdateMonitoring);
                    model.addAttribute("pledgor", pledgor);
                    break;
            }
            return "monitoring_card";
        }

        switch (whereUpdateMonitoring) {
            case "pa":
                PledgeAgreement pledgeAgreement = pledgeAgreementService.getPledgeAgreement(pledgeAgreementId.get());
                List<Monitoring> monitoringListForPA = monitoringService.insertMonitoringInPledgeAgreement(pledgeAgreement, monitoring);

                model.addAttribute("whereUpdateMonitoring", "responseSuccess");
                model.addAttribute("pledgeAgreement", pledgeAgreement);
                model.addAttribute("monitoringList" , monitoringListForPA);
                return "monitoring_card";

            case "ps":
                PledgeSubject pledgeSubject = pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId.get());
                monitoring.setPledgeSubject(pledgeSubject);
                Monitoring monitoringForPS = monitoringService.insertMonitoringInPledgeSubject(monitoring);
                List<Monitoring> monitoringListForPS = monitoringService.getMonitoringByPledgeSubject(pledgeSubject);

                model.addAttribute("pledgeSubject", pledgeSubject);
                model.addAttribute("monitoringList", monitoringListForPS);
                return "monitoring_pledge_subject";

            case "pledgor":
                Client pledgor = clientService.getClientById(pledgorId.get()).orElseThrow(() -> new RuntimeException("Неверная ссылка"));
                List<Monitoring> monitoringListForPledgor = monitoringService.insertMonitoringInPledgor(pledgor, monitoring);
                model.addAttribute("whereUpdateMonitoring", "responseSuccess");
                model.addAttribute("pledgor", pledgor);
                model.addAttribute("monitoringList" , monitoringListForPledgor);
                return "monitoring_card";

        }

        return "monitoring_card";
    }


    @GetMapping("/cost_history_card")
    public String costHistoryCardGet(@RequestParam("pledgeSubjectId") long pledgeSubjectId,
                                     Model model){
        PledgeSubject pledgeSubject = pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId);
        CostHistory costHistory = new CostHistory();
        costHistory.setPledgeSubject(pledgeSubject);
        costHistory.setRsDz(pledgeSubject.getRsDz());
        costHistory.setZsDz(pledgeSubject.getZsDz());
        model.addAttribute("costHistory", costHistory);

        return "cost_history_card";
    }

    @PostMapping("/cost_history_card")
    public String costHistoryCardPost(@Valid CostHistory costHistory,
                                      BindingResult bindingResult,
                                      Model model){
        if(bindingResult.hasErrors())
            return "cost_history_card";

        CostHistory costHistoryUpdated = costHistoryService.insertCostHistory(costHistory);
        List<CostHistory> costHistoryList = costHistoryService.getCostHistoryPledgeSubject(costHistoryUpdated.getPledgeSubject());
        model.addAttribute("pledgeSubject", costHistoryUpdated.getPledgeSubject());
        model.addAttribute("costHistoryList", costHistoryList);

        return "cost_history";
    }

    @GetMapping("/loan_agreement_card")
    public String loanAgreementCardPageGet(@RequestParam("loanAgreementId") Optional<Long> loanAgreementId,
                                           @RequestParam("clientId") Optional<Long> clientId,
                                           @RequestParam("whatDo") String whatDo,
                                           Model model){
        if(whatDo.equals("changeLA")){
            LoanAgreement loanAgreement = loanAgreementService.getLoanAgreementById(loanAgreementId.get()).orElseThrow(() -> new RuntimeException("Неверная ссылка"));

            model.addAttribute("loanAgreement", loanAgreement);
            model.addAttribute("whatDo", whatDo);

            return "loan_agreement_card";

        }else if(whatDo.equals("newLA")){
            LoanAgreement loanAgreement = new LoanAgreement();
            Client client = clientService.getClientById(clientId.get()).orElseThrow(() -> new RuntimeException("Неверная ссылка"));
            loanAgreement.setClient(client);

            model.addAttribute("loanAgreement", loanAgreement);
            model.addAttribute("whatDo", whatDo);

            return "loan_agreement_card";
        }else
            return null;

    }

    @PostMapping("/loan_agreement_card")
    public String loanAgreementCardPagePost(@Valid LoanAgreement loanAgreement,
                                            BindingResult bindingResult,
                                            @RequestParam("whatDo") String whatDo,
                                            Model model){

        if(bindingResult.hasErrors()){
            model.addAttribute("whatDo", whatDo);
            return "loan_agreement_card";
        }

        if(whatDo.equals("changeLA")){
            LoanAgreement loanAgreementUpdated = loanAgreementService.updateInsertLoanAgreement(loanAgreement);

            model.addAttribute("loanAgreement", loanAgreementUpdated);

            return "loan_agreement_detail";

        }else  if(whatDo.equals("newLA")){
            LoanAgreement loanAgreementUpdated = loanAgreementService.updateInsertLoanAgreement(loanAgreement);

            model.addAttribute("loanAgreement", loanAgreementUpdated);
            model.addAttribute("whatDo", "responseSuccess");

            return "loan_agreement_card";

        }else
            return null;
    }

    @GetMapping("/pledge_agreement_card")
    public String pledgeAgreementCardGet(@RequestParam("pledgeAgreementId") Optional<Long> pledgeAgreementId,
                                         @RequestParam("clientId") Optional<Long> clientId,
                                         @RequestParam("whatDo") String whatDo,
                                         Model model){
        if(whatDo.equals("changePA")){
            PledgeAgreement pledgeAgreement = pledgeAgreementService.getPledgeAgreement(pledgeAgreementId.get());

            model.addAttribute("pledgeAgreement", pledgeAgreement);
            model.addAttribute("whatDo", whatDo);

            return "pledge_agreement_card";

        }else if(whatDo.equals("newPA")){
            PledgeAgreement pledgeAgreement = new PledgeAgreement();
            Client client = clientService.getClientById(clientId.get()).orElseThrow(() -> new RuntimeException("Неверная ссылка"));
            pledgeAgreement.setClient(client);

            model.addAttribute("pledgeAgreement", pledgeAgreement);
            model.addAttribute("whatDo", whatDo);

            return "pledge_agreement_card";

        }else
            return null;

    }

    @PostMapping("/pledge_agreement_card")
    public String pledgeAgreementCardPost(@Valid PledgeAgreement pledgeAgreement,
                                          BindingResult bindingResult,
                                          @RequestParam("whatDo") String whatDo,
                                          Model model){

        if(bindingResult.hasErrors()){
            model.addAttribute("whatDo", whatDo);
            return "pledge_agreement_card";
        }

        if(whatDo.equals("changePA")){
            PledgeAgreement pa = pledgeAgreementService.updateInsertPledgeAgreement(pledgeAgreement);

            model.addAttribute("pledgeAgreement", pa);

            return "pledge_agreement_detail";
        }else {
            PledgeAgreement pa = pledgeAgreementService.updateInsertPledgeAgreement(pledgeAgreement);

            model.addAttribute("pledgeAgreement", pa);
            model.addAttribute("whatDo", "responseSuccess");

            return "pledge_agreement_card";
        }


    }

    @GetMapping("/pledge_subject_card_update")
    public String pledgeSubjectCardUpdate(@RequestParam("pledgeSubjectId") Optional<Long> pledgeSubjectId,
                                          Model model){

        PledgeSubject pledgeSubject = pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId.get());
        if(pledgeSubject.getClass()==PledgeSubjectAuto.class){
            model.addAttribute("pledgeSubjectAuto", pledgeSubject);
        }else if(pledgeSubject.getClass()==PledgeSubjectEquipment.class){
            model.addAttribute("pledgeSubjectEquipment", pledgeSubject);
        }else if(pledgeSubject.getClass()==PledgeSubjectSecurities.class){
            model.addAttribute("pledgeSubjectSecurities", pledgeSubject);
        }else if(pledgeSubject.getClass()==PledgeSubjectVessel.class){
            model.addAttribute("pledgeSubjectVessel", pledgeSubject);
        }else if(pledgeSubject.getClass()==PledgeSubjectTBO.class){
            model.addAttribute("pledgeSubjectTBO", pledgeSubject);
        }else if(pledgeSubject.getClass()==PledgeSubjectRealtyLandOwnership.class ||
                pledgeSubject.getClass()==PledgeSubjectRealtyLandLease.class){
            List<LandCategory> landCategoryList = landCategoryService.getAllLandCategory();
            model.addAttribute("landCategoryList", landCategoryList);
            model.addAttribute("pledgeSubjectRealty", pledgeSubject);
        }else if(pledgeSubject.getClass()==PledgeSubjectRealtyBuilding.class ||
                pledgeSubject.getClass()==PledgeSubjectRealtyRoom.class){
            List<MarketSegment> marketSegmentList = marketSegmentService.getAllMarketSegment();
            model.addAttribute("marketSegmentList", marketSegmentList);
            model.addAttribute("pledgeSubjectRealty", pledgeSubject);
        }

        model.addAttribute("typeOfCollateral", pledgeSubject.getTypeOfCollateral());

        return "pledge_subject_card_update";
    }

    @GetMapping("pledge_subject_card_new")
    public String pledgeSubjectCardNew(@RequestParam("typeOfCollateral") Optional<String> typeOfCollateral,
                                       @RequestParam("pledgeAgreementId") Optional<Long> pledgeAgreementId,
                                       Model model){

        List<PledgeAgreement> pledgeAgreementList= new ArrayList<>();
        pledgeAgreementList.add(pledgeAgreementService.getPledgeAgreement(pledgeAgreementId.get()));

        if(typeOfCollateral.get().equals("Авто/спецтехника")) {
            PledgeSubjectAuto pledgeSubjectAuto = new PledgeSubjectAuto();
            pledgeSubjectAuto.setPledgeAgreements(pledgeAgreementList);
            model.addAttribute("pledgeSubjectAuto", pledgeSubjectAuto);
        }
        else if(typeOfCollateral.get().equals("Оборудование")){
            PledgeSubjectEquipment pledgeSubjectEquipment = new PledgeSubjectEquipment();
            pledgeSubjectEquipment.setPledgeAgreements(pledgeAgreementList);
            model.addAttribute("pledgeSubjectEquipment", pledgeSubjectEquipment);
        }
        else if(typeOfCollateral.get().equals("Ценные бумаги")){
            PledgeSubjectSecurities pledgeSubjectSecurities = new PledgeSubjectSecurities();
            pledgeSubjectSecurities.setPledgeAgreements(pledgeAgreementList);
            model.addAttribute("pledgeSubjectSecurities", pledgeSubjectSecurities);
        }
        else if(typeOfCollateral.get().equals("Судно")){
            PledgeSubjectVessel pledgeSubjectVessel = new PledgeSubjectVessel();
            pledgeSubjectVessel.setPledgeAgreements(pledgeAgreementList);
            model.addAttribute("pledgeSubjectVessel", pledgeSubjectVessel);
        }
        else if(typeOfCollateral.get().equals("ТМЦ")){
            PledgeSubjectTBO pledgeSubjectTBO = new PledgeSubjectTBO();
            pledgeSubjectTBO.setPledgeAgreements(pledgeAgreementList);
            model.addAttribute("pledgeSubjectTBO", pledgeSubjectTBO);
        }
        else if(typeOfCollateral.get().equals("Недвижимость - ЗУ - собственность")){
            PledgeSubjectRealtyLandOwnership pledgeSubjectRealtyLandOwnership = new PledgeSubjectRealtyLandOwnership();
            pledgeSubjectRealtyLandOwnership.setPledgeAgreements(pledgeAgreementList);
            List<LandCategory> landCategoryList = landCategoryService.getAllLandCategory();
            model.addAttribute("pledgeSubjectRealtyLandOwnership", pledgeSubjectRealtyLandOwnership);
            model.addAttribute("landCategoryList", landCategoryList);
        }
        else if(typeOfCollateral.get().equals("Недвижимость - ЗУ - право аренды")){
            PledgeSubjectRealtyLandLease pledgeSubjectRealtyLandLease = new PledgeSubjectRealtyLandLease();
            pledgeSubjectRealtyLandLease.setPledgeAgreements(pledgeAgreementList);
            List<LandCategory> landCategoryList = landCategoryService.getAllLandCategory();
            model.addAttribute("pledgeSubjectRealtyLandLease", pledgeSubjectRealtyLandLease);
            model.addAttribute("landCategoryList", landCategoryList);
        }
        else if(typeOfCollateral.get().equals("Недвижимость - здание/сооружение")){
            PledgeSubjectRealtyBuilding pledgeSubjectRealtyBuilding = new PledgeSubjectRealtyBuilding();
            pledgeSubjectRealtyBuilding.setPledgeAgreements(pledgeAgreementList);
            List<MarketSegment> marketSegmentList = marketSegmentService.getAllMarketSegment();
            model.addAttribute("marketSegmentList", marketSegmentList);
            model.addAttribute("pledgeSubjectRealtyBuilding", pledgeSubjectRealtyBuilding);
        }
        else if(typeOfCollateral.get().equals("Недвижимость - помещение")){
            PledgeSubjectRealtyRoom pledgeSubjectRealtyRoom = new PledgeSubjectRealtyRoom();
            pledgeSubjectRealtyRoom.setPledgeAgreements(pledgeAgreementList);
            List<MarketSegment> marketSegmentList = marketSegmentService.getAllMarketSegment();
            model.addAttribute("marketSegmentList", marketSegmentList);
            model.addAttribute("pledgeSubjectRealtyRoom", pledgeSubjectRealtyRoom);
        }


        CostHistory costHistory = new CostHistory();
        Monitoring monitoring = new Monitoring();


        model.addAttribute("costHistory", costHistory);
        model.addAttribute("monitoring", monitoring);
        model.addAttribute("pledgeAgreementId", pledgeAgreementId.get());
        model.addAttribute("typeOfCollateral", typeOfCollateral.get());

        return "pledge_subject_card_new";

    }

    @PostMapping("/update_pledge_subject_auto")
    public String updatePledgeSubjectAuto(@Valid PledgeSubjectAuto pledgeSubjectAuto,
                                          BindingResult bindingResult,
                                          Model model){

        if(bindingResult.hasErrors()) {
            model.addAttribute("typeOfCollateral", pledgeSubjectAuto.getTypeOfCollateral());

            return "pledge_subject_card_update";
        }

        PledgeSubject pledgeSubjectUpdated = pledgeSubjectService.updatePledgeSubject(pledgeSubjectAuto);

        model.addAttribute("pledgeSubject", pledgeSubjectUpdated);

        return "pledge_subject_detail";
    }

    @PostMapping("update_pledge_subject_equipment")
    public String updatePledgeSubjectEquipment(@Valid PledgeSubjectEquipment pledgeSubjectEquipment,
                                               BindingResult bindingResult,
                                               Model model){
        if(bindingResult.hasErrors()) {
            model.addAttribute("typeOfCollateral", pledgeSubjectEquipment.getTypeOfCollateral());

            return "pledge_subject_card_update";
        }

        PledgeSubject pledgeSubjectUpdated = pledgeSubjectService.updatePledgeSubject(pledgeSubjectEquipment);

        model.addAttribute("pledgeSubject", pledgeSubjectUpdated);

        return "pledge_subject_detail";
    }

    @PostMapping("update_pledge_subject_securities")
    public String updatePledgeSubjectSecurities(@Valid PledgeSubjectSecurities pledgeSubjectSecurities,
                                                BindingResult bindingResult,
                                                Model model){
        if(bindingResult.hasErrors()) {
            model.addAttribute("typeOfCollateral", pledgeSubjectSecurities.getTypeOfCollateral());

            return "pledge_subject_card_update";
        }

        PledgeSubject pledgeSubjectUpdated = pledgeSubjectService.updatePledgeSubject(pledgeSubjectSecurities);

        model.addAttribute("pledgeSubject", pledgeSubjectUpdated);

        return "pledge_subject_detail";
    }

    @PostMapping("update_pledge_subject_vessel")
    public String updatePledgeSubjectVessel(@Valid PledgeSubjectVessel pledgeSubjectVessel,
                                            BindingResult bindingResult,
                                            Model model){
        if(bindingResult.hasErrors()) {
            model.addAttribute("typeOfCollateral", pledgeSubjectVessel.getTypeOfCollateral());

            return "pledge_subject_card_update";
        }

        PledgeSubject pledgeSubjectUpdated = pledgeSubjectService.updatePledgeSubject(pledgeSubjectVessel);

        model.addAttribute("pledgeSubject", pledgeSubjectUpdated);

        return "pledge_subject_detail";
    }

    @PostMapping("update_pledge_subject_tbo")
    public String updatePledgeSubjectTBO(@Valid PledgeSubjectTBO pledgeSubjectTBO,
                                         BindingResult bindingResult,
                                         Model model){
        if(bindingResult.hasErrors()) {
            model.addAttribute("typeOfCollateral", pledgeSubjectTBO.getTypeOfCollateral());

            return "pledge_subject_card_update";
        }

        PledgeSubject pledgeSubjectUpdated = pledgeSubjectService.updatePledgeSubject(pledgeSubjectTBO);

        model.addAttribute("pledgeSubject", pledgeSubjectUpdated);

        return "pledge_subject_detail";
    }

    @PostMapping("update_pledge_subject_landOwn")
    public String updatePledgeSubjectLandOwnership(@Valid PledgeSubjectRealty pledgeSubjectRealtyLandOwnership,
                                                 BindingResult bindingResult,
                                                 Model model){
        if(bindingResult.hasErrors()) {
            List<LandCategory> landCategoryList = landCategoryService.getAllLandCategory();
            model.addAttribute("landCategoryList", landCategoryList);
            model.addAttribute("typeOfCollateral", pledgeSubjectRealtyLandOwnership.getTypeOfCollateral());

            return "pledge_subject_card_update";
        }

        PledgeSubject pledgeSubjectUpdated = pledgeSubjectService.updatePledgeSubject(pledgeSubjectRealtyLandOwnership);

        model.addAttribute("pledgeSubject", pledgeSubjectUpdated);

        return "pledge_subject_detail";
    }

    @PostMapping("update_pledge_subject_landLease")
    public String updatePledgeSubjectLandLease(@Valid PledgeSubjectRealty pledgeSubjectRealtyLandLease,
                                               BindingResult bindingResult,
                                               Model model){
        if(bindingResult.hasErrors()) {
            List<LandCategory> landCategoryList = landCategoryService.getAllLandCategory();
            model.addAttribute("landCategoryList", landCategoryList);
            model.addAttribute("typeOfCollateral", pledgeSubjectRealtyLandLease.getTypeOfCollateral());

            return "pledge_subject_card_update";
        }

        PledgeSubject pledgeSubjectUpdated = pledgeSubjectService.updatePledgeSubject(pledgeSubjectRealtyLandLease);

        model.addAttribute("pledgeSubject", pledgeSubjectUpdated);

        return "pledge_subject_detail";
    }

    @PostMapping("update_pledge_subject_building")
    public String updatePledgeSubjectBuilding(@Valid PledgeSubjectRealty pledgeSubjectRealtyBuilding,
                                              BindingResult bindingResult,
                                              Model model){
        if(bindingResult.hasErrors()) {
            List<MarketSegment> marketSegmentList = marketSegmentService.getAllMarketSegment();
            model.addAttribute("marketSegmentList", marketSegmentList);
            model.addAttribute("typeOfCollateral", pledgeSubjectRealtyBuilding.getTypeOfCollateral());

            return "pledge_subject_card_update";
        }

        PledgeSubject pledgeSubjectUpdated = pledgeSubjectService.updatePledgeSubject(pledgeSubjectRealtyBuilding);

        model.addAttribute("pledgeSubject", pledgeSubjectUpdated);

        return "pledge_subject_detail";
    }

    @PostMapping("update_pledge_subject_room")
    public String updatePledgeSubjectRoom(@Valid PledgeSubjectRealty pledgeSubjectRealtyRoom,
                                          BindingResult bindingResult,
                                          Model model){
        if(bindingResult.hasErrors()) {
            List<MarketSegment> marketSegmentList = marketSegmentService.getAllMarketSegment();
            model.addAttribute("marketSegmentList", marketSegmentList);
            model.addAttribute("typeOfCollateral", pledgeSubjectRealtyRoom.getTypeOfCollateral());

            return "pledge_subject_card_update";
        }

        PledgeSubject pledgeSubjectUpdated = pledgeSubjectService.updatePledgeSubject(pledgeSubjectRealtyRoom);

        model.addAttribute("pledgeSubject", pledgeSubjectUpdated);

        return "pledge_subject_detail";
    }

    @PostMapping("insert_pledge_subject_auto")
    public String insertNewPledgeSubjectAuto(@Valid PledgeSubjectAuto pledgeSubjectAuto,
                                             BindingResult bindingResultAuto,
                                             @Valid CostHistory costHistory,
                                             BindingResult bindingResultCostHistory,
                                             @Valid Monitoring monitoring,
                                             BindingResult bindingResultMonitoring,
                                             @RequestParam("pledgeAgreementId") long pledgeAgreementId,
                                             Model model){

        if(bindingResultAuto.hasErrors()){
            model.addAttribute("costHistory", costHistory);
            model.addAttribute("monitoring", monitoring);
            model.addAttribute("pledgeAgreementId", pledgeAgreementId);
            model.addAttribute("typeOfCollateral", pledgeSubjectAuto.getTypeOfCollateral());

            return "pledge_subject_card_new";

        }else if(bindingResultCostHistory.hasErrors()){
            model.addAttribute("pledgeSubjectAuto", pledgeSubjectAuto);
            model.addAttribute("monitoring", monitoring);
            model.addAttribute("pledgeAgreementId", pledgeAgreementId);
            model.addAttribute("typeOfCollateral", pledgeSubjectAuto.getTypeOfCollateral());

            return "pledge_subject_card_new";

        }else if(bindingResultMonitoring.hasErrors()){
            model.addAttribute("pledgeSubjectAuto", pledgeSubjectAuto);
            model.addAttribute("costHistory", costHistory);
            model.addAttribute("pledgeAgreementId", pledgeAgreementId);
            model.addAttribute("typeOfCollateral", pledgeSubjectAuto.getTypeOfCollateral());

            return "pledge_subject_card_new";
        }

        pledgeSubjectService.insertPledgeSubject(pledgeSubjectAuto, costHistory, monitoring);
        PledgeAgreement pledgeAgreement = pledgeAgreementService.getPledgeAgreement(pledgeAgreementId);
        model.addAttribute("pledgeAgreement", pledgeAgreement);
        return "pledge_subjects";
    }

    @PostMapping("insert_pledge_subject_equipment")
    public String insertNewPledgeSubjectEquipment(@Valid PledgeSubjectEquipment pledgeSubjectEquipment,
                                                  BindingResult bindingResultEquipment,
                                                  @Valid CostHistory costHistory,
                                                  BindingResult bindingResultCostHistory,
                                                  @Valid Monitoring monitoring,
                                                  BindingResult bindingResultMonitoring,
                                                  @RequestParam("pledgeAgreementId") long pledgeAgreementId,
                                                  Model model){

        if(bindingResultEquipment.hasErrors()){
            model.addAttribute("costHistory", costHistory);
            model.addAttribute("monitoring", monitoring);
            model.addAttribute("pledgeAgreementId", pledgeAgreementId);
            model.addAttribute("typeOfCollateral", pledgeSubjectEquipment.getTypeOfCollateral());

            return "pledge_subject_card_new";

        }else if(bindingResultCostHistory.hasErrors()){
            model.addAttribute("pledgeSubjectEquipment", pledgeSubjectEquipment);
            model.addAttribute("monitoring", monitoring);
            model.addAttribute("pledgeAgreementId", pledgeAgreementId);
            model.addAttribute("typeOfCollateral", pledgeSubjectEquipment.getTypeOfCollateral());

            return "pledge_subject_card_new";

        }else if(bindingResultMonitoring.hasErrors()){
            model.addAttribute("pledgeSubjectEquipment", pledgeSubjectEquipment);
            model.addAttribute("costHistory", costHistory);
            model.addAttribute("pledgeAgreementId", pledgeAgreementId);
            model.addAttribute("typeOfCollateral", pledgeSubjectEquipment.getTypeOfCollateral());

            return "pledge_subject_card_new";
        }

        pledgeSubjectService.insertPledgeSubject(pledgeSubjectEquipment, costHistory, monitoring);
        PledgeAgreement pledgeAgreement = pledgeAgreementService.getPledgeAgreement(pledgeAgreementId);
        model.addAttribute("pledgeAgreement", pledgeAgreement);
        return "pledge_subjects";
    }

    @PostMapping("insert_pledge_subject_securities")
    public String insertNewPledgeSubjectSecurities(@Valid PledgeSubjectSecurities pledgeSubjectSecurities,
                                                   BindingResult bindingResultSecurities,
                                                   @Valid CostHistory costHistory,
                                                   BindingResult bindingResultCostHistory,
                                                   @Valid Monitoring monitoring,
                                                   BindingResult bindingResultMonitoring,
                                                   @RequestParam("pledgeAgreementId") long pledgeAgreementId,
                                                   Model model){

        if(bindingResultSecurities.hasErrors()){
            model.addAttribute("costHistory", costHistory);
            model.addAttribute("monitoring", monitoring);
            model.addAttribute("pledgeAgreementId", pledgeAgreementId);
            model.addAttribute("typeOfCollateral", pledgeSubjectSecurities.getTypeOfCollateral());

            return "pledge_subject_card_new";

        }else if(bindingResultCostHistory.hasErrors()){
            model.addAttribute("pledgeSubjectSecurities", pledgeSubjectSecurities);
            model.addAttribute("monitoring", monitoring);
            model.addAttribute("pledgeAgreementId", pledgeAgreementId);
            model.addAttribute("typeOfCollateral", pledgeSubjectSecurities.getTypeOfCollateral());

            return "pledge_subject_card_new";

        }else if(bindingResultMonitoring.hasErrors()){
            model.addAttribute("pledgeSubjectSecurities", pledgeSubjectSecurities);
            model.addAttribute("costHistory", costHistory);
            model.addAttribute("pledgeAgreementId", pledgeAgreementId);
            model.addAttribute("typeOfCollateral", pledgeSubjectSecurities.getTypeOfCollateral());

            return "pledge_subject_card_new";
        }

        pledgeSubjectService.insertPledgeSubject(pledgeSubjectSecurities, costHistory, monitoring);
        PledgeAgreement pledgeAgreement = pledgeAgreementService.getPledgeAgreement(pledgeAgreementId);
        model.addAttribute("pledgeAgreement", pledgeAgreement);
        return "pledge_subjects";
    }

    @PostMapping("insert_pledge_subject_vessel")
    public String insertNewPledgeSubjectVessel(@Valid PledgeSubjectVessel pledgeSubjectVessel,
                                               BindingResult bindingResultVessel,
                                               @Valid CostHistory costHistory,
                                               BindingResult bindingResultCostHistory,
                                               @Valid Monitoring monitoring,
                                               BindingResult bindingResultMonitoring,
                                               @RequestParam("pledgeAgreementId") long pledgeAgreementId,
                                               Model model){

        if(bindingResultVessel.hasErrors()){
            model.addAttribute("costHistory", costHistory);
            model.addAttribute("monitoring", monitoring);
            model.addAttribute("pledgeAgreementId", pledgeAgreementId);
            model.addAttribute("typeOfCollateral", pledgeSubjectVessel.getTypeOfCollateral());

            return "pledge_subject_card_new";

        }else if(bindingResultCostHistory.hasErrors()){
            model.addAttribute("pledgeSubjectVessel", pledgeSubjectVessel);
            model.addAttribute("monitoring", monitoring);
            model.addAttribute("pledgeAgreementId", pledgeAgreementId);
            model.addAttribute("typeOfCollateral", pledgeSubjectVessel.getTypeOfCollateral());

            return "pledge_subject_card_new";

        }else if(bindingResultMonitoring.hasErrors()){
            model.addAttribute("pledgeSubjectVessel", pledgeSubjectVessel);
            model.addAttribute("costHistory", costHistory);
            model.addAttribute("pledgeAgreementId", pledgeAgreementId);
            model.addAttribute("typeOfCollateral", pledgeSubjectVessel.getTypeOfCollateral());

            return "pledge_subject_card_new";
        }

        pledgeSubjectService.insertPledgeSubject(pledgeSubjectVessel, costHistory, monitoring);
        PledgeAgreement pledgeAgreement = pledgeAgreementService.getPledgeAgreement(pledgeAgreementId);
        model.addAttribute("pledgeAgreement", pledgeAgreement);
        return "pledge_subjects";
    }

    @PostMapping("insert_pledge_subject_tbo")
    public String insertNewPledgeSubjectTBO(@Valid PledgeSubjectTBO pledgeSubjectTBO,
                                            BindingResult bindingResultTBO,
                                            @Valid CostHistory costHistory,
                                            BindingResult bindingResultCostHistory,
                                            @Valid Monitoring monitoring,
                                            BindingResult bindingResultMonitoring,
                                            @RequestParam("pledgeAgreementId") long pledgeAgreementId,
                                            Model model){

        if(bindingResultTBO.hasErrors()){
            model.addAttribute("costHistory", costHistory);
            model.addAttribute("monitoring", monitoring);
            model.addAttribute("pledgeAgreementId", pledgeAgreementId);
            model.addAttribute("typeOfCollateral", pledgeSubjectTBO.getTypeOfCollateral());

            return "pledge_subject_card_new";

        }else if(bindingResultCostHistory.hasErrors()){
            model.addAttribute("pledgeSubjectTBO", pledgeSubjectTBO);
            model.addAttribute("monitoring", monitoring);
            model.addAttribute("pledgeAgreementId", pledgeAgreementId);
            model.addAttribute("typeOfCollateral", pledgeSubjectTBO.getTypeOfCollateral());

            return "pledge_subject_card_new";

        }else if(bindingResultMonitoring.hasErrors()){
            model.addAttribute("pledgeSubjectTBO", pledgeSubjectTBO);
            model.addAttribute("costHistory", costHistory);
            model.addAttribute("pledgeAgreementId", pledgeAgreementId);
            model.addAttribute("typeOfCollateral", pledgeSubjectTBO.getTypeOfCollateral());

            return "pledge_subject_card_new";
        }

        pledgeSubjectService.insertPledgeSubject(pledgeSubjectTBO, costHistory, monitoring);
        PledgeAgreement pledgeAgreement = pledgeAgreementService.getPledgeAgreement(pledgeAgreementId);
        model.addAttribute("pledgeAgreement", pledgeAgreement);
        return "pledge_subjects";
    }

    @PostMapping("insert_pledge_subject_landOwn")
    public String insertNewPledgeSubjectLandOwnership(@Valid PledgeSubjectRealtyLandOwnership pledgeSubjectRealtyLandOwnership,
                                                      BindingResult bindingResultLandOwn,
                                                      @Valid CostHistory costHistory,
                                                      BindingResult bindingResultCostHistory,
                                                      @Valid Monitoring monitoring,
                                                      BindingResult bindingResultMonitoring,
                                                      @RequestParam("pledgeAgreementId") long pledgeAgreementId,
                                                      Model model){

        if(bindingResultLandOwn.hasErrors()){
            List<LandCategory> landCategoryList = landCategoryService.getAllLandCategory();
            model.addAttribute("landCategoryList", landCategoryList);
            model.addAttribute("costHistory", costHistory);
            model.addAttribute("monitoring", monitoring);
            model.addAttribute("pledgeAgreementId", pledgeAgreementId);
            model.addAttribute("typeOfCollateral", pledgeSubjectRealtyLandOwnership.getTypeOfCollateral());

            return "pledge_subject_card_new";

        }else if(bindingResultCostHistory.hasErrors()){
            List<LandCategory> landCategoryList = landCategoryService.getAllLandCategory();
            model.addAttribute("landCategoryList", landCategoryList);
            model.addAttribute("pledgeSubjectRealtyLandOwnership", pledgeSubjectRealtyLandOwnership);
            model.addAttribute("monitoring", monitoring);
            model.addAttribute("pledgeAgreementId", pledgeAgreementId);
            model.addAttribute("typeOfCollateral", pledgeSubjectRealtyLandOwnership.getTypeOfCollateral());

            return "pledge_subject_card_new";

        }else if(bindingResultMonitoring.hasErrors()){
            List<LandCategory> landCategoryList = landCategoryService.getAllLandCategory();
            model.addAttribute("landCategoryList", landCategoryList);
            model.addAttribute("pledgeSubjectRealtyLandOwnership", pledgeSubjectRealtyLandOwnership);
            model.addAttribute("costHistory", costHistory);
            model.addAttribute("pledgeAgreementId", pledgeAgreementId);
            model.addAttribute("typeOfCollateral", pledgeSubjectRealtyLandOwnership.getTypeOfCollateral());

            return "pledge_subject_card_new";
        }

        pledgeSubjectService.insertPledgeSubject(pledgeSubjectRealtyLandOwnership, costHistory, monitoring);
        PledgeAgreement pledgeAgreement = pledgeAgreementService.getPledgeAgreement(pledgeAgreementId);
        model.addAttribute("pledgeAgreement", pledgeAgreement);
        return "pledge_subjects";
    }

    @PostMapping("insert_pledge_subject_landLease")
    public String insertNewPledgeSubjectLandLease(@Valid PledgeSubjectRealtyLandLease pledgeSubjectRealtyLandLease,
                                                  BindingResult bindingResultLandLease,
                                                  @Valid CostHistory costHistory,
                                                  BindingResult bindingResultCostHistory,
                                                  @Valid Monitoring monitoring,
                                                  BindingResult bindingResultMonitoring,
                                                  @RequestParam("pledgeAgreementId") long pledgeAgreementId,
                                                  Model model){

        if(bindingResultLandLease.hasErrors()){
            List<LandCategory> landCategoryList = landCategoryService.getAllLandCategory();
            model.addAttribute("landCategoryList", landCategoryList);
            model.addAttribute("costHistory", costHistory);
            model.addAttribute("monitoring", monitoring);
            model.addAttribute("pledgeAgreementId", pledgeAgreementId);
            model.addAttribute("typeOfCollateral", pledgeSubjectRealtyLandLease.getTypeOfCollateral());

            return "pledge_subject_card_new";

        }else if(bindingResultCostHistory.hasErrors()){
            List<LandCategory> landCategoryList = landCategoryService.getAllLandCategory();
            model.addAttribute("landCategoryList", landCategoryList);
            model.addAttribute("pledgeSubjectRealtyLandLease", pledgeSubjectRealtyLandLease);
            model.addAttribute("monitoring", monitoring);
            model.addAttribute("pledgeAgreementId", pledgeAgreementId);
            model.addAttribute("typeOfCollateral", pledgeSubjectRealtyLandLease.getTypeOfCollateral());

            return "pledge_subject_card_new";

        }else if(bindingResultMonitoring.hasErrors()){
            List<LandCategory> landCategoryList = landCategoryService.getAllLandCategory();
            model.addAttribute("landCategoryList", landCategoryList);
            model.addAttribute("pledgeSubjectRealtyLandLease", pledgeSubjectRealtyLandLease);
            model.addAttribute("costHistory", costHistory);
            model.addAttribute("pledgeAgreementId", pledgeAgreementId);
            model.addAttribute("typeOfCollateral", pledgeSubjectRealtyLandLease.getTypeOfCollateral());

            return "pledge_subject_card_new";
        }

        pledgeSubjectService.insertPledgeSubject(pledgeSubjectRealtyLandLease, costHistory, monitoring);
        PledgeAgreement pledgeAgreement = pledgeAgreementService.getPledgeAgreement(pledgeAgreementId);
        model.addAttribute("pledgeAgreement", pledgeAgreement);
        return "pledge_subjects";
    }

    @PostMapping("insert_pledge_subject_building")
    public String insertNewPledgeSubjectBuilding(@Valid PledgeSubjectRealtyBuilding pledgeSubjectRealtyBuilding,
                                                 BindingResult bindingResultBuilding,
                                                 @Valid CostHistory costHistory,
                                                 BindingResult bindingResultCostHistory,
                                                 @Valid Monitoring monitoring,
                                                 BindingResult bindingResultMonitoring,
                                                 @RequestParam("pledgeAgreementId") long pledgeAgreementId,
                                                 Model model){

        if(bindingResultBuilding.hasErrors()){
            List<MarketSegment> marketSegmentList = marketSegmentService.getAllMarketSegment();
            model.addAttribute("marketSegmentList", marketSegmentList);
            model.addAttribute("costHistory", costHistory);
            model.addAttribute("monitoring", monitoring);
            model.addAttribute("pledgeAgreementId", pledgeAgreementId);
            model.addAttribute("typeOfCollateral", pledgeSubjectRealtyBuilding.getTypeOfCollateral());

            return "pledge_subject_card_new";

        }else if(bindingResultCostHistory.hasErrors()){
            List<MarketSegment> marketSegmentList = marketSegmentService.getAllMarketSegment();
            model.addAttribute("marketSegmentList", marketSegmentList);
            model.addAttribute("pledgeSubjectRealtyBuilding", pledgeSubjectRealtyBuilding);
            model.addAttribute("monitoring", monitoring);
            model.addAttribute("pledgeAgreementId", pledgeAgreementId);
            model.addAttribute("typeOfCollateral", pledgeSubjectRealtyBuilding.getTypeOfCollateral());

            return "pledge_subject_card_new";

        }else if(bindingResultMonitoring.hasErrors()){
            List<MarketSegment> marketSegmentList = marketSegmentService.getAllMarketSegment();
            model.addAttribute("marketSegmentList", marketSegmentList);
            model.addAttribute("pledgeSubjectRealtyBuilding", pledgeSubjectRealtyBuilding);
            model.addAttribute("costHistory", costHistory);
            model.addAttribute("pledgeAgreementId", pledgeAgreementId);
            model.addAttribute("typeOfCollateral", pledgeSubjectRealtyBuilding.getTypeOfCollateral());

            return "pledge_subject_card_new";
        }

        pledgeSubjectService.insertPledgeSubject(pledgeSubjectRealtyBuilding, costHistory, monitoring);
        PledgeAgreement pledgeAgreement = pledgeAgreementService.getPledgeAgreement(pledgeAgreementId);
        model.addAttribute("pledgeAgreement", pledgeAgreement);
        return "pledge_subjects";
    }

    @PostMapping("insert_pledge_subject_room")
    public String insertNewPledgeSubjectRoom(@Valid PledgeSubjectRealtyRoom pledgeSubjectRealtyRoom,
                                             BindingResult bindingResultRoom,
                                             @Valid CostHistory costHistory,
                                             BindingResult bindingResultCostHistory,
                                             @Valid Monitoring monitoring,
                                             BindingResult bindingResultMonitoring,
                                             @RequestParam("pledgeAgreementId") long pledgeAgreementId,
                                             Model model){

        if(bindingResultRoom.hasErrors()){
            List<MarketSegment> marketSegmentList = marketSegmentService.getAllMarketSegment();
            model.addAttribute("marketSegmentList", marketSegmentList);
            model.addAttribute("costHistory", costHistory);
            model.addAttribute("monitoring", monitoring);
            model.addAttribute("pledgeAgreementId", pledgeAgreementId);
            model.addAttribute("typeOfCollateral", pledgeSubjectRealtyRoom.getTypeOfCollateral());

            return "pledge_subject_card_new";

        }else if(bindingResultCostHistory.hasErrors()){
            List<MarketSegment> marketSegmentList = marketSegmentService.getAllMarketSegment();
            model.addAttribute("marketSegmentList", marketSegmentList);
            model.addAttribute("pledgeSubjectRealtyRoom", pledgeSubjectRealtyRoom);
            model.addAttribute("monitoring", monitoring);
            model.addAttribute("pledgeAgreementId", pledgeAgreementId);
            model.addAttribute("typeOfCollateral", pledgeSubjectRealtyRoom.getTypeOfCollateral());

            return "pledge_subject_card_new";

        }else if(bindingResultMonitoring.hasErrors()){
            List<MarketSegment> marketSegmentList = marketSegmentService.getAllMarketSegment();
            model.addAttribute("marketSegmentList", marketSegmentList);
            model.addAttribute("pledgeSubjectRealtyRoom", pledgeSubjectRealtyRoom);
            model.addAttribute("costHistory", costHistory);
            model.addAttribute("pledgeAgreementId", pledgeAgreementId);
            model.addAttribute("typeOfCollateral", pledgeSubjectRealtyRoom.getTypeOfCollateral());

            return "pledge_subject_card_new";
        }

        pledgeSubjectService.insertPledgeSubject(pledgeSubjectRealtyRoom, costHistory, monitoring);
        PledgeAgreement pledgeAgreement = pledgeAgreementService.getPledgeAgreement(pledgeAgreementId);
        model.addAttribute("pledgeAgreement", pledgeAgreement);
        return "pledge_subjects";
    }

    @PostMapping("withdrawFromDepositPledgeSubject")
    public @ResponseBody int withdrawFromDepositPledgeSubject(@RequestParam("pledgeSubjectId") long pledgeSubjectId,
                                                   @RequestParam("pledgeAgreementId") long pledgeAgreementId,
                                                   Model model){

        System.out.println("pledgeSubjectId" + pledgeSubjectId);
        System.out.println("pledgeAgreementId" + pledgeAgreementId);
        PledgeAgreement pledgeAgreement = pledgeAgreementService.getPledgeAgreement(pledgeAgreementId);
        List<PledgeSubject> pledgeSubjectList = pledgeSubjectService.getPledgeSubjectsForPledgeAgreement(pledgeAgreementId);
        pledgeSubjectList.remove(pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId));
        pledgeAgreement.setPledgeSubjects(pledgeSubjectList);
        pledgeAgreementService.updateInsertPledgeAgreement(pledgeAgreement);


        return pledgeSubjectList.size();
    }

    @GetMapping("/update")
    public String updatePage(){
        return "update";
    }

    @GetMapping("/client")
    public String clientPage(@RequestParam("clientId") long clientId,
                             Model model){
        Client client = clientService.getClientById(clientId).orElseThrow(() -> new RuntimeException("Неверная ссылка"));
        model.addAttribute("client", client);

        return "client";
    }

    @GetMapping("/client_card_update")
    public String clientCardUpdatePage(@RequestParam("clientId") Optional<Long> clientId,
                                       Model model){

        List<ClientManager> clientManagerList = clientManagerService.getAllClientManager();
        List<Employee> employeeList = employeeService.getAllEmployee();

        Client client = clientService.getClientById(clientId.get()).orElseThrow(() -> new RuntimeException("Неверная ссылка"));
        if(client.getClass()==ClientLegalEntity.class){
            model.addAttribute("clientLegalEntity", client);
        }else if(client.getClass()==ClientIndividual.class){
            model.addAttribute("clientIndividual", client);
        }

        model.addAttribute("clientManagerList", clientManagerList);
        model.addAttribute("employeeList", employeeList);
        model.addAttribute("typeOfClient", client.getTypeOfClient());

        return "client_card_update";
    }

    @GetMapping("/client_card_new")
    public String clientCardNewPage(@RequestParam("typeOfClient") String typeOfClient,
                                    Model model){

        List<ClientManager> clientManagerList = clientManagerService.getAllClientManager();
        List<Employee> employeeList = employeeService.getAllEmployee();

        if(typeOfClient.equals("юл")){
            ClientLegalEntity clientLegalEntity = new ClientLegalEntity();
            model.addAttribute("clientLegalEntity", clientLegalEntity);

        }else if(typeOfClient.equals("фл")){
            ClientIndividual clientIndividual = new ClientIndividual();
            model.addAttribute("clientIndividual", clientIndividual);

        }

        model.addAttribute("typeOfClient", typeOfClient);
        model.addAttribute("clientManagerList", clientManagerList);
        model.addAttribute("employeeList", employeeList);

        return "client_card_new";
    }

    @PostMapping("update_client_le")
    public String updateClientLegalEntity(@Valid ClientLegalEntity clientLegalEntity,
                                          BindingResult bindingResult,
                                          Model model){
        if(bindingResult.hasErrors()){
            List<ClientManager> clientManagerList = clientManagerService.getAllClientManager();
            List<Employee> employeeList = employeeService.getAllEmployee();
            model.addAttribute("clientManagerList", clientManagerList);
            model.addAttribute("employeeList", employeeList);
            model.addAttribute("typeOfClient", clientLegalEntity.getTypeOfClient());

            return "client_card_update";
        }

        Client clientUpdated = clientService.updateInsertClient(clientLegalEntity);

        model.addAttribute("client", clientUpdated);

        return "client";
    }

    @PostMapping("update_client_ind")
    public String updateClientIndividual(@Valid ClientIndividual clientIndividual,
                                          BindingResult bindingResult,
                                          Model model){
        if(bindingResult.hasErrors()){
            List<ClientManager> clientManagerList = clientManagerService.getAllClientManager();
            List<Employee> employeeList = employeeService.getAllEmployee();
            model.addAttribute("clientManagerList", clientManagerList);
            model.addAttribute("employeeList", employeeList);
            model.addAttribute("typeOfClient", clientIndividual.getTypeOfClient());

            return "client_card_update";
        }

        Client clientUpdated = clientService.updateInsertClient(clientIndividual);
        model.addAttribute("client", clientUpdated);

        return "client";
    }

    @PostMapping("insert_client_le")
    public String insertNewClientLegalEntity(@Valid ClientLegalEntity clientLegalEntity,
                                             BindingResult bindingResult,
                                             Model model){
        if(bindingResult.hasErrors()){
            List<ClientManager> clientManagerList = clientManagerService.getAllClientManager();
            List<Employee> employeeList = employeeService.getAllEmployee();
            model.addAttribute("clientManagerList", clientManagerList);
            model.addAttribute("employeeList", employeeList);
            model.addAttribute("typeOfClient", clientLegalEntity.getTypeOfClient());

            return "client_card_new";
        }

        Client clientUpdated = clientService.updateInsertClient(clientLegalEntity);

        model.addAttribute("client", clientUpdated);

        return "client";
    }

    @PostMapping("insert_client_ind")
    public String insertNewClientIndividual(@Valid ClientIndividual clientIndividual,
                                            BindingResult bindingResult,
                                            Model model){
        if(bindingResult.hasErrors()){
            List<ClientManager> clientManagerList = clientManagerService.getAllClientManager();
            List<Employee> employeeList = employeeService.getAllEmployee();
            model.addAttribute("clientManagerList", clientManagerList);
            model.addAttribute("employeeList", employeeList);
            model.addAttribute("typeOfClient", clientIndividual.getTypeOfClient());

            return "client_card_new";
        }

        Client clientUpdated = clientService.updateInsertClient(clientIndividual);
        model.addAttribute("client", clientUpdated);

        return "client";
    }

    @PostMapping("searchPA")
    public @ResponseBody List<PledgeAgreement> searchPA(@RequestParam("numPA") String numPA){
        List<PledgeAgreement> pledgeAgreements = pledgeAgreementService.getPledgeAgreementsBynumPA(numPA);
        if(!pledgeAgreements.isEmpty())
            return pledgeAgreements;
        else
            return null;
    }

    @PostMapping("insertPA")
    public @ResponseBody int insertPA(@RequestParam("pledgeAgreementIdArray[]") long[] pledgeAgreementIdArray,
                                      @RequestParam("loanAgreementId") long loanAgreementId){

        List<PledgeAgreement> pledgeAgreementList = loanAgreementService.getAllPledgeAgreements(loanAgreementId);
        int countPABeforeUpdate = pledgeAgreementList.size();
        for(int i = 0; i < pledgeAgreementIdArray.length; i++){
            pledgeAgreementList.add(pledgeAgreementService.getPledgeAgreement(pledgeAgreementIdArray[i]));
        }

        int countPAAfterUpdate = pledgeAgreementList.size();
        System.out.println(countPAAfterUpdate - countPABeforeUpdate);

        LoanAgreement loanAgreement = loanAgreementService.getLoanAgreementById(loanAgreementId).orElseThrow(() -> new RuntimeException("Неверная ссылка"));
        loanAgreement.setPledgeAgreements(pledgeAgreementList);
        loanAgreementService.updateInsertLoanAgreement(loanAgreement);

        return countPAAfterUpdate - countPABeforeUpdate;
    }

    @PostMapping("searchPS")
    public @ResponseBody List<PledgeSubject> searchPS(@RequestParam("cadastralNum") Optional<String> cadastralNum,
                                                      @RequestParam("namePS") Optional<String> namePS){

        if(cadastralNum.isPresent())
            return pledgeSubjectService.getPledgeSubjectByCadastralNum(cadastralNum.get());
        else if(namePS.isPresent())
            return pledgeSubjectService.getPledgeSubjectByName(namePS.get());
        else
            return null;
    }

    @PostMapping("insertPS")
    public @ResponseBody int insertCurrentPledgeSubject(@RequestParam("pledgeSubjectsIdArray[]") long[] pledgeSubjectsIdArray,
                                                        @RequestParam("pledgeAgreementId") long pledgeAgreementId){

        PledgeAgreement pledgeAgreement = pledgeAgreementService.getPledgeAgreement(pledgeAgreementId);
        List<PledgeSubject> pledgeSubjectList = pledgeSubjectService.getPledgeSubjectsForPledgeAgreement(pledgeAgreementId);
        int countPSBeforeUpdate = pledgeSubjectList.size();
        for(int i = 0; i < pledgeSubjectsIdArray.length; i++)
            pledgeSubjectList.add(pledgeSubjectService.getPledgeSubjectById(pledgeSubjectsIdArray[i]));

        int countPSAfterUpdate = pledgeSubjectList.size();

        pledgeAgreement.setPledgeSubjects(pledgeSubjectList);
        pledgeAgreementService.updateInsertPledgeAgreement(pledgeAgreement);

        return countPSAfterUpdate - countPSBeforeUpdate;
    }

    @PostMapping("/upload")
    public String importEntityFromExcel(@RequestParam("file") Optional<MultipartFile> file,
                                        @RequestParam("whatUpload") Optional<String> whatUpload,
                                        Model model){

        try {
            File uploadFile =  filesService.uploadFile(file.orElseThrow(() -> new IOException("Файл не найден.")), "legal_entity");

            if(whatUpload.isPresent()){
                if(whatUpload.get().equals("clientLegalEntity")){
                    List<ClientLegalEntity> clientLegalEntityList = filesService.getClientLegalEntityFromExcel(uploadFile);
                    clientLegalEntityList = clientService.updateInsertClientLegalEntityList(clientLegalEntityList);

                    model.addAttribute("clientLegalEntityList", clientLegalEntityList);

                }else if(whatUpload.get().equals("clientIndividual")){
                    List<ClientIndividual> clientIndividualList = filesService.getClientIndividualFromExcel(uploadFile);
                    clientIndividualList = clientService.updateInsertClientIndividualList(clientIndividualList);

                    model.addAttribute("clientIndividualList", clientIndividualList);

                }else if(whatUpload.get().equals("pledgeAgreement")){
                    List<PledgeAgreement> pledgeAgreementList = filesService.getPledgeAgreementFromExcel(uploadFile);
                    pledgeAgreementList = pledgeAgreementService.updateInsertPledgeAgreementList(pledgeAgreementList);

                    model.addAttribute("pledgeAgreementList", pledgeAgreementList);

                }else if(whatUpload.get().equals("loanAgreement")){

                }else if(whatUpload.get().equals("psBuilding")){

                }else if(whatUpload.get().equals("psRoom")){

                }else if(whatUpload.get().equals("psLandOwnership")){

                }else if(whatUpload.get().equals("psLandLease")){

                }else if(whatUpload.get().equals("psAuto")){

                }else if(whatUpload.get().equals("psEquipment")){

                }else if(whatUpload.get().equals("psTBO")){

                }else if(whatUpload.get().equals("psSecurities")){

                }else if(whatUpload.get().equals("psVessel")){

                }else if(whatUpload.get().equals("insurance")){

                }else if(whatUpload.get().equals("encumbrance")){

                }else if(whatUpload.get().equals("costHistory")){

                }else if(whatUpload.get().equals("monitoring")){

                }else if(whatUpload.get().equals("clientManager")){

                }

                model.addAttribute("messageSuccess", true);
                model.addAttribute("whatUpload", whatUpload.get());

                return "update";


            }else {
                model.addAttribute("messageError", "Ошибка импорта.");
                return "update";
            }

        }catch (IOException ioe){
            ioe.printStackTrace();
            model.addAttribute("messageError", ioe.getMessage());
            return "update";

        }catch (InvalidFormatException ife){
            ife.printStackTrace();
            model.addAttribute("messageError", ife.getMessage());
            return "update";

        }finally {
            return "update";
        }
    }
}
