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
    private final UploadUnloadService uploadUnloadService;


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
                           UploadUnloadService uploadUnloadService) {
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
        this.uploadUnloadService = uploadUnloadService;
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
        int currentPage = page.orElse(0);
        int pageSize = size.orElse(50);
        Pageable pageable = PageRequest.of(currentPage, pageSize);

        Page<PledgeAgreement> pledgeAgreementList = null;
        if(pervPosl.isPresent())
            pledgeAgreementList = pledgeAgreementService.getCurrentPledgeAgreementsForEmployee(employeeId, pervPosl.get(), pageable);
        else
            pledgeAgreementList = pledgeAgreementService.getCurrentPledgeAgreementsForEmployee(employeeId, pageable);



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

    @GetMapping("/client")
    public String clientPage(@RequestParam("clientId") long clientId,
                             Model model){
        Client client = clientService.getClientByClientId(clientId);
        model.addAttribute("client", client);

        return "client";
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
        LoanAgreement loanAgreement = loanAgreementService.getLoanAgreementById(loanAgreementId);
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
    public String monitoringPledgeAgreementsPage(@RequestParam("countOfMonitoringNotDone") int countOfMonitoringNotDone,
                                                 @RequestParam("countOfMonitoringIsDone") int countOfMonitoringIsDone,
                                                 @RequestParam("countOfMonitoringOverdue") int countOfMonitoringOverdue,
                                                 @RequestParam("employeeId") long employeeId,
                                                 Model model){

        model.addAttribute("countOfMonitoringNotDone", countOfMonitoringNotDone);
        model.addAttribute("countOfMonitoringIsDone", countOfMonitoringIsDone);
        model.addAttribute("countOfMonitoringOverdue", countOfMonitoringOverdue);
        Employee employee = employeeService.getEmployee(employeeId);
        List<PledgeAgreement> pledgeAgreementListWithMonitoringNotDone = pledgeAgreementService.getPledgeAgreementWithMonitoringNotDone(employee);
        List<PledgeAgreement> pledgeAgreementListWithMonitoringIsDone = pledgeAgreementService.getPledgeAgreementWithMonitoringIsDone(employee);
        List<PledgeAgreement> pledgeAgreementListWithMonitoringOverdue = pledgeAgreementService.getPledgeAgreementWithMonitoringOverDue(employee);
        model.addAttribute("pledgeAgreementListWithMonitoringNotDone", pledgeAgreementListWithMonitoringNotDone);
        model.addAttribute("pledgeAgreementListWithMonitoringIsDone", pledgeAgreementListWithMonitoringIsDone);
        model.addAttribute("pledgeAgreementListWithMonitoringOverdue", pledgeAgreementListWithMonitoringOverdue);

        return "monitoring_pledge_agreements";
    }

    @GetMapping("/conclusion_pledge_agreements")
    public String conclusionPledgeAgreementsPage(@RequestParam("countOfConclusionNotDone") int countOfConclusionNotDone,
                                                 @RequestParam("countOfConclusionIsDone") int countOfConclusionIsDone,
                                                 @RequestParam("countOfConclusionOverdue") int countOfConclusionOverdue,
                                                 @RequestParam("employeeId") long employeeId,
                                                 Model model){

        model.addAttribute("countOfConclusionNotDone", countOfConclusionNotDone);
        model.addAttribute("countOfConclusionIsDone", countOfConclusionIsDone);
        model.addAttribute("countOfConclusionOverdue", countOfConclusionOverdue);
        Employee employee = employeeService.getEmployee(employeeId);
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
                Client pledgor = clientService.getClientByClientId(pledgorId.get());
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
                    model.addAttribute("monitoring",monitoring);
                    break;
                case "ps":
                    PledgeSubject pledgeSubject = pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId.get());
                    model.addAttribute("whereUpdateMonitoring", whereUpdateMonitoring);
                    model.addAttribute("pledgeSubject", pledgeSubject);
                    model.addAttribute("monitoring", monitoring);
                    break;
                case "pledgor":
                    Client pledgor = clientService.getClientByClientId(pledgorId.get());
                    model.addAttribute("whereUpdateMonitoring", whereUpdateMonitoring);
                    model.addAttribute("pledgor", pledgor);
                    model.addAttribute("monitoring", monitoring);
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
                Client pledgor = clientService.getClientByClientId(pledgorId.get());
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
            LoanAgreement loanAgreement = loanAgreementService.getLoanAgreementById(loanAgreementId.get());

            model.addAttribute("loanAgreement", loanAgreement);
            model.addAttribute("whatDo", whatDo);

            return "loan_agreement_card";

        }else if(whatDo.equals("newLA")){
            LoanAgreement loanAgreement = new LoanAgreement();
            Client client = clientService.getClientByClientId(clientId.get());
            loanAgreement.setLoaner(client);

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
            model.addAttribute("loanAgreement", loanAgreement);
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
    public String pledgeAgreementCardGet(@RequestParam("pledgeAgreementId") long pledgeAgreementId,
                                         @RequestParam("clientId") Optional<Long> clientId,
                                         @RequestParam("whatDo") String whatDo,
                                         Model model){
        if(whatDo.equals("changePA")){
            PledgeAgreement pledgeAgreement = pledgeAgreementService.getPledgeAgreement(pledgeAgreementId);

            model.addAttribute("pledgeAgreement", pledgeAgreement);
            model.addAttribute("whatDo", whatDo);

            return "pledge_agreement_card";
        }else {
            PledgeAgreement pledgeAgreement = new PledgeAgreement();
            Client client = clientService.getClientByClientId(clientId.orElse((long)0));
            pledgeAgreement.setPledgor(client);

            model.addAttribute("pledgeAgreement", pledgeAgreement);
            model.addAttribute("whatDo", whatDo);

            return "pledge_agreement_card";
        }

    }

    @PostMapping("/pledge_agreement_card")
    public String pledgeAgreementCardPost(@ModelAttribute PledgeAgreement pledgeAgreement,
                                          @RequestParam("whatDo") String whatDo,
                                          Model model){

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

    @GetMapping("/pledge_subject_card")
    public String pledgeSubjectCard(@RequestParam("pledgeSubjectId") Optional<Long> pledgeSubjectId,
                                    @RequestParam("typeOfCollateral") Optional<String> typeOfCollateral,
                                    @RequestParam("pledgeAgreementId") Optional<Long> pledgeAgreementId,
                                    @RequestParam("whatDo") String whatDo,
                                    Model model){

        switch (whatDo){
            case "changePS":
                PledgeSubject pledgeSubject = pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId.get());

                if(pledgeSubject.getClass() == PledgeSubjectRealtyLandLease.class ||
                        pledgeSubject.getClass() == PledgeSubjectRealtyLandOwnership.class){
                    List<LandCategory> landCategoryList = landCategoryService.getAllLandCategory();
                    model.addAttribute("landCategoryList", landCategoryList);
                }else if(pledgeSubject.getClass() == PledgeSubjectRealtyBuilding.class ||
                        pledgeSubject.getClass() == PledgeSubjectRealtyRoom.class){
                    List<MarketSegment> marketSegmentList = marketSegmentService.getAllMarketSegment();
                    model.addAttribute("marketSegmentList", marketSegmentList);
                }

                model.addAttribute("pledgeSubject", pledgeSubject);
                model.addAttribute("whatDo", whatDo);

                return "pledge_subject_card";

            case "newPS":

                List<PledgeAgreement> pledgeAgreementList= new ArrayList<>();
                pledgeAgreementList.add(pledgeAgreementService.getPledgeAgreement(pledgeAgreementId.get()));

                if(typeOfCollateral.get().equals("Авто/спецтехника")) {
                    PledgeSubjectAuto pledgeSubjectAuto = new PledgeSubjectAuto();
                    pledgeSubjectAuto.setPledgeAgreements(pledgeAgreementList);
                    model.addAttribute("pledgeSubject", pledgeSubjectAuto);
                }
                else if(typeOfCollateral.get().equals("Оборудование")){
                    PledgeSubjectEquipment pledgeSubjectEquipment = new PledgeSubjectEquipment();
                    pledgeSubjectEquipment.setPledgeAgreements(pledgeAgreementList);
                    model.addAttribute("pledgeSubject", pledgeSubjectEquipment);
                }
                else if(typeOfCollateral.get().equals("Ценные бумаги")){
                    PledgeSubjectSecurities pledgeSubjectSecurities = new PledgeSubjectSecurities();
                    pledgeSubjectSecurities.setPledgeAgreements(pledgeAgreementList);
                    model.addAttribute("pledgeSubject", pledgeSubjectSecurities);
                }
                else if(typeOfCollateral.get().equals("Судно")){
                    PledgeSubjectVessel pledgeSubjectVessel = new PledgeSubjectVessel();
                    pledgeSubjectVessel.setPledgeAgreements(pledgeAgreementList);
                    model.addAttribute("pledgeSubject", pledgeSubjectVessel);
                }
                else if(typeOfCollateral.get().equals("ТМЦ")){
                    PledgeSubjectTBO pledgeSubjectTBO = new PledgeSubjectTBO();
                    pledgeSubjectTBO.setPledgeAgreements(pledgeAgreementList);
                    model.addAttribute("pledgeSubject", pledgeSubjectTBO);
                }
                else if(typeOfCollateral.get().equals("Недвижимость - ЗУ - собственность")){
                    PledgeSubjectRealtyLandOwnership pledgeSubjectRealtyLandOwnership = new PledgeSubjectRealtyLandOwnership();
                    pledgeSubjectRealtyLandOwnership.setPledgeAgreements(pledgeAgreementList);
                    List<LandCategory> landCategoryList = landCategoryService.getAllLandCategory();
                    model.addAttribute("pledgeSubject", pledgeSubjectRealtyLandOwnership);
                    model.addAttribute("landCategoryList", landCategoryList);
                }
                else if(typeOfCollateral.get().equals("Недвижимость - ЗУ - право аренды")){
                    PledgeSubjectRealtyLandLease pledgeSubjectRealtyLandLease = new PledgeSubjectRealtyLandLease();
                    pledgeSubjectRealtyLandLease.setPledgeAgreements(pledgeAgreementList);
                    List<LandCategory> landCategoryList = landCategoryService.getAllLandCategory();
                    model.addAttribute("pledgeSubject", pledgeSubjectRealtyLandLease);
                    model.addAttribute("landCategoryList", landCategoryList);
                }
                else if(typeOfCollateral.get().equals("Недвижимость - здание/сооружение")){
                    PledgeSubjectRealtyBuilding pledgeSubjectRealtyBuilding = new PledgeSubjectRealtyBuilding();
                    pledgeSubjectRealtyBuilding.setPledgeAgreements(pledgeAgreementList);
                    List<MarketSegment> marketSegmentList = marketSegmentService.getAllMarketSegment();
                    model.addAttribute("pledgeSubject", pledgeSubjectRealtyBuilding);
                    model.addAttribute("marketSegmentList", marketSegmentList);
                }
                else if(typeOfCollateral.get().equals("Недвижимость - помещение")){
                    PledgeSubjectRealtyRoom pledgeSubjectRealtyRoom = new PledgeSubjectRealtyRoom();
                    pledgeSubjectRealtyRoom.setPledgeAgreements(pledgeAgreementList);
                    List<MarketSegment> marketSegmentList = marketSegmentService.getAllMarketSegment();
                    model.addAttribute("pledgeSubject", pledgeSubjectRealtyRoom);
                    model.addAttribute("marketSegmentList", marketSegmentList);
                }



                CostHistory costHistory = new CostHistory();
                Monitoring monitoring = new Monitoring();
                Encumbrance encumbrance = new Encumbrance();

                model.addAttribute("costHistory", costHistory);
                model.addAttribute("monitoring", monitoring);
                model.addAttribute("encumbrance", encumbrance);
                model.addAttribute("pledgeAgreementId", pledgeAgreementId.get());
                model.addAttribute("whatDo", whatDo);

                return "pledge_subject_card";

            }

        return null;

    }


    @PostMapping("/pledge_subject_detail")
    public String updatePledgeSubject(@ModelAttribute PledgeSubject pledgeSubject,
                                      @ModelAttribute PledgeSubjectAuto pledgeSubjectAuto,
                                      @ModelAttribute PledgeSubjectEquipment pledgeSubjectEquipment,
                                      @ModelAttribute PledgeSubjectSecurities pledgeSubjectSecurities,
                                      @ModelAttribute PledgeSubjectVessel pledgeSubjectVessel,
                                      @ModelAttribute PledgeSubjectTBO pledgeSubjectTBO,
                                      @ModelAttribute PledgeSubjectRealty pledgeSubjectRealty,
                                      @ModelAttribute CostHistory costHistory,
                                      @ModelAttribute Monitoring monitoring,
                                      @ModelAttribute Encumbrance encumbrance,
                                      Model model){

        PledgeSubject pledgeSubjectUpdated = null;

                if(pledgeSubject.getTypeOfCollateral().equals("Авто/спецтехника"))
                    pledgeSubjectUpdated = pledgeSubjectService.updatePledgeSubject(pledgeSubjectAuto);
                else if(pledgeSubject.getTypeOfCollateral().equals("Оборудование"))
                    pledgeSubjectUpdated = pledgeSubjectService.updatePledgeSubject(pledgeSubjectEquipment);
                else if(pledgeSubject.getTypeOfCollateral().equals("Ценные бумаги"))
                    pledgeSubjectUpdated = pledgeSubjectService.updatePledgeSubject(pledgeSubjectSecurities);
                else if(pledgeSubject.getTypeOfCollateral().equals("Судно"))
                    pledgeSubjectUpdated = pledgeSubjectService.updatePledgeSubject(pledgeSubjectVessel);
                else if(pledgeSubject.getTypeOfCollateral().equals("ТМЦ"))
                    pledgeSubjectUpdated = pledgeSubjectService.updatePledgeSubject(pledgeSubjectTBO);
                else if(pledgeSubject.getTypeOfCollateral().equals("Недвижимость - ЗУ - собственность") ||
                        pledgeSubject.getTypeOfCollateral().equals("Недвижимость - ЗУ - право аренды") ||
                        pledgeSubject.getTypeOfCollateral().equals("Недвижимость - здание/сооружение") ||
                        pledgeSubject.getTypeOfCollateral().equals("Недвижимость - помещение"))
                    pledgeSubjectUpdated = pledgeSubjectService.updatePledgeSubject(pledgeSubjectRealty);

                model.addAttribute("pledgeSubject", pledgeSubjectUpdated);

                return "pledge_subject_detail";

    }

    @PostMapping("/pledge_subjects")
    public String insertNewPledgeSubject(@ModelAttribute PledgeSubject pledgeSubject,
                                         @ModelAttribute PledgeSubjectAuto pledgeSubjectAuto,
                                         @ModelAttribute PledgeSubjectEquipment pledgeSubjectEquipment,
                                         @ModelAttribute PledgeSubjectSecurities pledgeSubjectSecurities,
                                         @ModelAttribute PledgeSubjectVessel pledgeSubjectVessel,
                                         @ModelAttribute PledgeSubjectTBO pledgeSubjectTBO,
                                         @ModelAttribute PledgeSubjectRealty pledgeSubjectRealty,
                                         @ModelAttribute PledgeSubjectRealtyLandOwnership pledgeSubjectRealtyLandOwnership,
                                         @ModelAttribute PledgeSubjectRealtyLandLease pledgeSubjectRealtyLandLease,
                                         @ModelAttribute PledgeSubjectRealtyBuilding pledgeSubjectRealtyBuilding,
                                         @ModelAttribute PledgeSubjectRealtyRoom pledgeSubjectRealtyRoom,
                                         @ModelAttribute CostHistory costHistory,
                                         @ModelAttribute Monitoring monitoring,
                                         @ModelAttribute Encumbrance encumbrance,
                                         @RequestParam("pledgeAgreementId") Optional<Long> pledgeAgreementId,
                                         Model model){

        PledgeSubject pledgeSubjectUpdated = null;

        if(pledgeSubject.getTypeOfCollateral().equals("Авто/спецтехника"))
            pledgeSubjectUpdated = pledgeSubjectService.insertPledgeSubject(pledgeSubjectAuto, costHistory, monitoring, encumbrance);
        else if(pledgeSubject.getTypeOfCollateral().equals("Оборудование"))
            pledgeSubjectUpdated = pledgeSubjectService.insertPledgeSubject(pledgeSubjectEquipment, costHistory, monitoring, encumbrance);
        else if(pledgeSubject.getTypeOfCollateral().equals("Ценные бумаги"))
            pledgeSubjectUpdated = pledgeSubjectService.insertPledgeSubject(pledgeSubjectSecurities,costHistory, monitoring, encumbrance);
        else if(pledgeSubject.getTypeOfCollateral().equals("Судно"))
            pledgeSubjectUpdated = pledgeSubjectService.insertPledgeSubject(pledgeSubjectVessel, costHistory, monitoring, encumbrance);
        else if(pledgeSubject.getTypeOfCollateral().equals("ТМЦ"))
            pledgeSubjectUpdated = pledgeSubjectService.insertPledgeSubject(pledgeSubjectTBO, costHistory, monitoring, encumbrance);
        else if(pledgeSubject.getTypeOfCollateral().equals("Недвижимость - ЗУ - собственность"))
            pledgeSubjectUpdated = pledgeSubjectService.insertPledgeSubject(pledgeSubjectRealtyLandOwnership, costHistory, monitoring, encumbrance);
        else if(pledgeSubject.getTypeOfCollateral().equals("Недвижимость - ЗУ - право аренды"))
            pledgeSubjectUpdated = pledgeSubjectService.insertPledgeSubject(pledgeSubjectRealtyLandLease, costHistory, monitoring, encumbrance);
        else if(pledgeSubject.getTypeOfCollateral().equals("Недвижимость - здание/сооружение"))
            pledgeSubjectUpdated = pledgeSubjectService.insertPledgeSubject(pledgeSubjectRealtyBuilding, costHistory, monitoring, encumbrance);
        else if(pledgeSubject.getTypeOfCollateral().equals("Недвижимость - помещение"))
            pledgeSubjectUpdated = pledgeSubjectService.insertPledgeSubject(pledgeSubjectRealtyRoom, costHistory, monitoring, encumbrance);

        PledgeAgreement pledgeAgreement = pledgeAgreementService.getPledgeAgreement(pledgeAgreementId.get());
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

    @GetMapping("/client_card")
    public String clientCardGet(@RequestParam("clientId") Optional<Long> clientId,
                                @RequestParam("whatDo") String whatDo,
                                Model model){

        List<ClientManager> clientManagerList = clientManagerService.getAllClientManager();
        List<Employee> employeeList = employeeService.getAllEmployee();

        if(whatDo.equals("newClientLE")){
            ClientLegalEntity clientLegalEntity = new ClientLegalEntity();

            model.addAttribute("whatDo", whatDo);
            model.addAttribute("clientManagerList", clientManagerList);
            model.addAttribute("employeeList", employeeList);
            model.addAttribute("clientLegalEntity", clientLegalEntity);

            return "client_card";
        }else if(whatDo.equals("newClientInd")){
            ClientIndividual clientIndividual = new ClientIndividual();

            model.addAttribute("whatDo", whatDo);
            model.addAttribute("clientManagerList", clientManagerList);
            model.addAttribute("employeeList", employeeList);
            model.addAttribute("clientIndividual", clientIndividual);

            return "client_card";
        }else {
            Client client = clientService.getClientByClientId(clientId.orElse((long)0));

            model.addAttribute("whatDo", whatDo);
            model.addAttribute("clientManagerList", clientManagerList);
            model.addAttribute("employeeList", employeeList);
            model.addAttribute("client", client);

            return "client_card";
        }
    }

    @PostMapping("/client_card")
    public String clientCardPost(@ModelAttribute Client client,
                                 @ModelAttribute ClientIndividual clientIndividual,
                                 @ModelAttribute ClientLegalEntity clientLegalEntity,
                                 @RequestParam("whatDo") String whatDo,
                                 Model model){

        if(whatDo.equals("newClientLE")){
            Client clientLegalEntityNew = clientService.saveClientLegalEntity(clientLegalEntity);

            model.addAttribute("client", clientLegalEntityNew);
            model.addAttribute("whatDo", "responseSuccess");

            return "client_card";

        }else if(whatDo.equals("newClientInd")){
            Client clientIndividualNew = clientService.saveClientIndividual(clientIndividual);

            model.addAttribute("client", clientIndividualNew);
            model.addAttribute("whatDo", "responseSuccess");

            return "client_card";
        }else {
            if(client.getTypeOfClient().equals("юл")){
                ClientLegalEntity clientLegalEntityUpdated = clientService.updateClientLegalEntity(clientLegalEntity);
                model.addAttribute("client", clientLegalEntityUpdated);
            }else {
                ClientIndividual clientIndividualUpdated = clientService.updateClientIndividual(clientIndividual);
                model.addAttribute("client", clientIndividualUpdated);
            }

            return "client";
        }
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

        LoanAgreement loanAgreement = loanAgreementService.getLoanAgreementById(loanAgreementId);
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
            return pledgeSubjectService.getPlegeSubjectByName(namePS.get());
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
    public String uploadFile(@RequestParam("file") Optional<MultipartFile> file,
                             @RequestParam("whatUpload") Optional<String> whatUpload,
                             Model model){


        if (whatUpload.isPresent()) {
            if (whatUpload.get().equals("legalEntity")) {

                try {
                    File uploadFile =  uploadUnloadService.uploadFile(file.orElseThrow(() -> new IOException("Файл не найден.")), "legal_entity");
                    List<ClientLegalEntity> clientLegalEntityList = clientService.insertClientLegalEntityFromExcel(uploadFile);
                    String ids = "";
                    for(ClientLegalEntity cle : clientLegalEntityList)
                        ids += cle.getClientId() + " ";
                    String message = "Добавлено " + clientLegalEntityList.size() + " клиента(ов) с id " + ids;
                    model.addAttribute("message", message);
                    return "update";

                }catch (IOException ioe){
                    ioe.printStackTrace();
                    model.addAttribute("message", ioe.getMessage());
                    return "update";
                }catch (InvalidFormatException ife){
                    ife.printStackTrace();
                    model.addAttribute("message", ife.getMessage());
                    return "update";
                }


            }else if (whatUpload.get().equals("individual")) {

            }
        } else {
            model.addAttribute("message", "Ошибка импорта.");
            return "update";
        }







        return "update";
    }
}
