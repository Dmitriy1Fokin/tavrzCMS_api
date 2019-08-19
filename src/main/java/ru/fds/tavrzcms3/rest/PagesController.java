package ru.fds.tavrzcms3.rest;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.fds.tavrzcms3.Service.*;
import ru.fds.tavrzcms3.domain.*;

import java.util.List;
import java.util.Map;


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
    private final PledgeSubjectAutoService pledgeSubjectAutoService;
    private final PledgeSubjectEquipmentService pledgeSubjectEquipmentService;
    private final PledgeSubjectRealtyBuildingService pledgeSubjectRealtyBuildingService;
    private final PledgeSubjectRealtyLandLeaseService pledgeSubjectRealtyLandLeaseService;
    private final PledgeSubjectRealtyLandOwnershipService pledgeSubjectRealtyLandOwnershipService;
    private final PledgeSubjectRealtyRoomService pledgeSubjectRealtyRoomService;
    private final PledgeSubjectSecuritiesService pledgeSubjectSecuritiesService;
    private final PledgeSubjectTBOService pledgeSubjectTBOService;
    private final PledgeSubjectVesselService pledgeSubjectVesselService;
    private final LandCategoryService landCategoryService;
    private final MarketSegmentService marketSegmentService;

    public PagesController(EmployeeService employeeService,
                           PledgeAgreementService pledgeAgreementService,
                           PledgeSubjectService pledgeSubjectService,
                           InsuranceService insuranceService,
                           EncumbranceService encumbranceService,
                           ClientService clientService,
                           LoanAgreementService loanAgreementService,
                           CostHistoryService costHistoryService,
                           MonitoringService monitoringService,
                           PledgeSubjectAutoService pledgeSubjectAutoService,
                           PledgeSubjectEquipmentService pledgeSubjectEquipmentService,
                           PledgeSubjectRealtyBuildingService pledgeSubjectRealtyBuildingService,
                           PledgeSubjectRealtyLandLeaseService pledgeSubjectRealtyLandLeaseService,
                           PledgeSubjectRealtyLandOwnershipService pledgeSubjectRealtyLandOwnershipService,
                           PledgeSubjectRealtyRoomService pledgeSubjectRealtyRoomService,
                           PledgeSubjectSecuritiesService pledgeSubjectSecuritiesService,
                           PledgeSubjectTBOService pledgeSubjectTBOService,
                           PledgeSubjectVesselService pledgeSubjectVesselService,
                           LandCategoryService landCategoryService,
                           MarketSegmentService marketSegmentService) {
        this.employeeService = employeeService;
        this.pledgeAgreementService = pledgeAgreementService;
        this.pledgeSubjectService = pledgeSubjectService;
        this.insuranceService = insuranceService;
        this.encumbranceService = encumbranceService;
        this.clientService = clientService;
        this.loanAgreementService = loanAgreementService;
        this.costHistoryService = costHistoryService;
        this.monitoringService = monitoringService;
        this.pledgeSubjectAutoService = pledgeSubjectAutoService;
        this.pledgeSubjectEquipmentService = pledgeSubjectEquipmentService;
        this.pledgeSubjectRealtyBuildingService = pledgeSubjectRealtyBuildingService;
        this.pledgeSubjectRealtyLandLeaseService = pledgeSubjectRealtyLandLeaseService;
        this.pledgeSubjectRealtyLandOwnershipService = pledgeSubjectRealtyLandOwnershipService;
        this.pledgeSubjectRealtyRoomService = pledgeSubjectRealtyRoomService;
        this.pledgeSubjectSecuritiesService = pledgeSubjectSecuritiesService;
        this.pledgeSubjectTBOService = pledgeSubjectTBOService;
        this.pledgeSubjectVesselService = pledgeSubjectVesselService;
        this.landCategoryService = landCategoryService;
        this.marketSegmentService = marketSegmentService;
    }

    @GetMapping("/")
    public String homePage(@AuthenticationPrincipal User user, Model model) {
        Employee employee = employeeService.getEmployee(user);
        model.addAttribute("employee", employee);

        int countOfPA = pledgeAgreementService.countOfCurrentPledgeAgreementsForEmployee(employee.getEmployeeId());
        model.addAttribute("countOfAllPledgeAgreement", countOfPA);

        int countOfPervPA = pledgeAgreementService.countOfPervCurrentPledgeAgreementsForEmployee(employee.getEmployeeId());
        model.addAttribute("countOfPervPledgeAgreements", countOfPervPA);

        int countOfPoslPA = pledgeAgreementService.countOfPoslCurrentPledgeAgreementsForEmployee(employee.getEmployeeId());
        model.addAttribute("countOfPoslPledgeAgreements", countOfPoslPA);

        int countOfLoanAgreements = pledgeAgreementService.countOfLoanAgreementsForEmployee(employee.getEmployeeId());
        model.addAttribute("countOfLoanAgreements", countOfLoanAgreements);

        int countOfMonitoringNotDone = pledgeAgreementService.countOfMonitoringNotDone(employee.getEmployeeId());
        model.addAttribute("countOfMonitoringNotDone", countOfMonitoringNotDone);

        int countOfMonitoringIsDone = pledgeAgreementService.countOfMonitoringIsDone(employee.getEmployeeId());
        model.addAttribute("countOfMonitoringIsDone", countOfMonitoringIsDone);

        int countOfMonitoringOverdue = pledgeAgreementService.countOfMonitoringOverdue(employee.getEmployeeId());
        model.addAttribute("countOfMonitoringOverdue", countOfMonitoringOverdue);

        int countOfConclusionNotDone = pledgeAgreementService.countOfConclusionNotDone(employee.getEmployeeId());
        model.addAttribute("countOfConclusionNotDone", countOfConclusionNotDone);

        int countOfConclusionIsDone = pledgeAgreementService.countOfConclusionIsDone(employee.getEmployeeId());
        model.addAttribute("countOfConclusionIsDone", countOfConclusionIsDone);

        int countOfConclusionOverdue = pledgeAgreementService.countOfConclusionOverdue(employee.getEmployeeId());
        model.addAttribute("countOfConclusionOverdue", countOfConclusionOverdue);

        return "home";
    }

    @GetMapping("/pledge_agreements")
    public String pledgeEgreementPage(@RequestParam("employeeId") long employeeId, @RequestParam("pervPosl") String pervPosl, Model model) {
        Employee employee = employeeService.getEmployee(employeeId);
        model.addAttribute("employee", employee);
        model.addAttribute("pervPosl", pervPosl);
        return "pledge_agreements";
    }

    @GetMapping("/pledge_subjects")
    public String pledgeSubjectsPage(@RequestParam("pledgeAgreementId") long pledgeAgreementId, Model model){
        PledgeAgreement pledgeAgreement = pledgeAgreementService.getPledgeAgreement(pledgeAgreementId);
        model.addAttribute("pledgeAgreement", pledgeAgreement);
        return "pledge_subjects";
    }

    @GetMapping("/insurances")
    public String insurancesPage(@RequestParam("pledgeSubjectId") long pledgeSubjectId, Model model){
        PledgeSubject pledgeSubject = pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId);
        List<Insurance> insuranceList = insuranceService.getInsurancesByPledgeSubject(pledgeSubject);
        model.addAttribute("pledgeSubject", pledgeSubject);
        model.addAttribute("insuranceList", insuranceList);
        return "insurances";
    }

    @GetMapping("/encumbrances")
    public  String encumbrancePage(@RequestParam("pledgeSubjectId") long pledgeSubjectId, Model model){
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
        model.addAttribute("encumbrance", encumbrance);
        model.addAttribute("pledgeSubjectId", pledgeSubjectId);
        return "encumbrance_card";
    }

    @PostMapping("/encumbrance_card")
    public String encumbranceCardPagePost(@ModelAttribute Encumbrance encumbrance,
                                          @RequestParam("pledgeSubjectId") long pledgeSubjectId,
                                          Model model){

        PledgeSubject pledgeSubject = pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId);
        Encumbrance encumbranceInserted = encumbranceService.insertEncumbranceInPledgeSubject(pledgeSubject, encumbrance);
        List<Encumbrance> encumbranceList = encumbranceService.getEncumbranceByPledgeSubject(encumbranceInserted.getPledgeSubject());
        model.addAttribute("pledgeSubject", encumbranceInserted.getPledgeSubject());
        model.addAttribute("encumbranceList", encumbranceList);
        return "encumbrances";
    }

    @GetMapping("/pledge_subject_detail")
    public String pledgeSubjectDetailPage(@RequestParam("pledgeSubjectId") long pledgeSubjectId, Model model){
        PledgeSubject pledgeSubject = pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId);
        model.addAttribute("pledgeSubject", pledgeSubject);
        return "pledge_subject_detail";
    }

    @GetMapping("/pledgor")
    public String pledgorPage(@RequestParam("pledgorId") long pledgorId, Model model){
        Client pledgor = clientService.getClientByClientId(pledgorId);
        model.addAttribute("pledgor", pledgor);
        return  "pledgor";
    }

    @GetMapping("/pledge_agreement_detail")
    public String pledgeAgreementsDetailPage(@RequestParam("pledgeAgreementId") long pledgeAgreementId, Model model){
        PledgeAgreement pledgeAgreement = pledgeAgreementService.getPledgeAgreement(pledgeAgreementId);
        model.addAttribute("pledgeAgreement", pledgeAgreement);
        return "pledge_agreement_detail";
    }

    @GetMapping("/loan_agreement_detail")
    public String loanAgreementDetailPage(@RequestParam("loanAgreementId") long loanAgreementId, Model model){
        LoanAgreement loanAgreement = loanAgreementService.getLoanAgreementById(loanAgreementId);
        model.addAttribute("loanAgreement", loanAgreement);
        return "loan_agreement_detail";
    }

    @GetMapping("/loaner")
    public String loanerPage(@RequestParam("loanerId") long loanerId, Model model){
        Client loaner = clientService.getClientByClientId(loanerId);
        model.addAttribute("loaner", loaner);
        return "loaner";
    }

    @GetMapping("cost_history")
    public String costHistoryPage(@RequestParam("pledgeSubjectId") long pledgeSubjectId, Model model){
        PledgeSubject pledgeSubject = pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId);
        List<CostHistory> costHistoryList = costHistoryService.getCostHistoryByPledgeSubjectId(pledgeSubjectId);
        model.addAttribute("pledgeSubject", pledgeSubject);
        model.addAttribute("costHistoryList", costHistoryList);
        return "cost_history";
    }

    @GetMapping("monitoring_pledge_subject")
    public String monitoringPage(@RequestParam("pledgeSubjectId") long pledgeSubjectId, Model model){
        PledgeSubject pledgeSubject = pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId);
        List<Monitoring> monitoringList = monitoringService.getMonitoringByPledgeSubjectId(pledgeSubjectId);
        model.addAttribute("pledgeSubject", pledgeSubject);
        model.addAttribute("monitoringList", monitoringList);
        return "monitoring_pledge_subject";
    }

    @GetMapping("/loan_agreements")
    public String loanAgreementsPage(@RequestParam("employeeId") long employeeId, Model model) {
        List<LoanAgreement> loanAgreementList = loanAgreementService.getCurrentLoanAgreementsForEmployee(employeeId);
        model.addAttribute("loanAgreementList", loanAgreementList);
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
        List<PledgeAgreement> pledgeAgreementListWithMonitoringNotDone = pledgeAgreementService.getPledgeAgreementWithMonitoringNotDone(employeeId);
        List<PledgeAgreement> pledgeAgreementListWithMonitoringIsDone = pledgeAgreementService.getPledgeAgreementWithMonitoringIsDone(employeeId);
        List<PledgeAgreement> pledgeAgreementListWithMonitoringOverdue = pledgeAgreementService.getPledgeAgreementWithMonitoringOverDue(employeeId);
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
        List<PledgeAgreement> pledgeAgreementListWithConclusionNotDone = pledgeAgreementService.getPledgeAgreementWithConclusionNotDone(employeeId);
        List<PledgeAgreement> pledgeAgreementListWithConclusionIsDone = pledgeAgreementService.getPledgeAgreementWithConclusionIsDone(employeeId);
        List<PledgeAgreement> pledgeAgreementListWithConclusionOverdue = pledgeAgreementService.getPledgeAgreementWithConclusionOverDue(employeeId);
        model.addAttribute("pledgeAgreementListWithConclusionNotDone", pledgeAgreementListWithConclusionNotDone);
        model.addAttribute("pledgeAgreementListWithConclusionIsDone", pledgeAgreementListWithConclusionIsDone);
        model.addAttribute("pledgeAgreementListWithConclusionOverdue", pledgeAgreementListWithConclusionOverdue);

        return "conclusion_pledge_agreements";
    }

    @GetMapping("/search")
    public String searchPage(){
        System.out.println("@GetMapping(\"/search\")!!!!!!!!!!!!!!!!!!!");
        return "search";
    }

    @GetMapping("/search_results")
    public String searchResultsPage(@RequestParam Map<String, String> reqParam, Model model){
        switch (reqParam.get("typeOfSearch")){
            case "searchLA":
                List<LoanAgreement> loanAgreements = loanAgreementService.getLoanAgreementFromSearch(reqParam);
                model.addAttribute("loanAgreements", loanAgreements);
                model.addAttribute("typeOfSearch", "loanAgreements");
                return "search_results";
            case "searchPA":
                List<PledgeAgreement> pledgeAgreements = pledgeAgreementService.getPledgeAgreementFromSearch(reqParam);
                model.addAttribute("pledgeAgreements", pledgeAgreements);
                model.addAttribute("typeOfSearch", "pledgeAreements");
                return "search_results";
            case "searchPS":
//                reqParam.forEach((k, v) -> System.out.println(k + " : " + v));
                List<PledgeSubject> pledgeSubjects = pledgeSubjectService.getPledgeSubjectsFromSearch(reqParam);
                model.addAttribute("pledgeSubjects", pledgeSubjects);
                model.addAttribute("typeOfSearch", "pledgeSubjects");
                return "search_results";

                default:
                    return "search_results";

        }
    }

    @GetMapping("/monitoring_card")
    public String monitoringCardPageGet(@RequestParam Map<String, String> reqParam, Model model){
        switch (reqParam.get("whereUpdateMonitoring")) {
            case "pa":
                PledgeAgreement pledgeAgreement = pledgeAgreementService.getPledgeAgreement(Long.parseLong(reqParam.get("pledgeAgreementId")));
                model.addAttribute("whereUpdateMonitoring", reqParam.get("whereUpdateMonitoring"));
                model.addAttribute("pledgeAgreement", pledgeAgreement);
                model.addAttribute("monitoring", new Monitoring());
                return "monitoring_card";
            case "ps":
                PledgeSubject pledgeSubject = pledgeSubjectService.getPledgeSubjectById(Long.parseLong(reqParam.get("pledgeSubjectId")));
                model.addAttribute("whereUpdateMonitoring", reqParam.get("whereUpdateMonitoring"));
                model.addAttribute("pledgeSubject", pledgeSubject);
                model.addAttribute("monitoring", new Monitoring());
                return "monitoring_card";
            case "pledgor":
                Client pledgor = clientService.getClientByClientId(Long.parseLong(reqParam.get("pledgorId")));
                model.addAttribute("whereUpdateMonitoring", reqParam.get("whereUpdateMonitoring"));
                model.addAttribute("pledgor", pledgor);
                model.addAttribute("monitoring", new Monitoring());
                return "monitoring_card";

                default:
                    return "monitoring_card";
        }
    }

    @PostMapping("/monitoring_card")
    public String monitoringCardPagePost(@ModelAttribute Monitoring monitoring,
                                         @RequestParam Map<String, String> reqParam,
                                         Model model){

        switch (reqParam.get("whereUpdateMonitoring")) {
            case "pa":
                PledgeAgreement pledgeAgreement = pledgeAgreementService.getPledgeAgreement(Long.parseLong(reqParam.get("pledgeAgreementId")));
                List<Monitoring> monitoringListForPA = monitoringService.insertMonitoringInPledgeAgreement(pledgeAgreement, monitoring);

                model.addAttribute("whereUpdateMonitoring", "responseSuccess");
                model.addAttribute("pledgeAgreement", pledgeAgreement);
                model.addAttribute("monitoringList" , monitoringListForPA);
                return "monitoring_card";

            case "ps":
                PledgeSubject pledgeSubject = pledgeSubjectService.getPledgeSubjectById(Long.parseLong(reqParam.get("pledgeSubjectId")));
                Monitoring monitoringForPS = monitoringService.insertMonitoringInPedgeSubject(pledgeSubject, monitoring);
                List<Monitoring> monitoringListForPS = monitoringService.getMonitoringByPledgeSubjectId(pledgeSubject.getPledgeSubjectId());

                model.addAttribute("pledgeSubject", pledgeSubject);
                model.addAttribute("monitoringList", monitoringListForPS);
                return "monitoring_pledge_subject";
            case "pledgor":
                Client pledgor = clientService.getClientByClientId(Long.parseLong(reqParam.get("pledgorId")));
                List<Monitoring> monitoringListForPledgor = monitoringService.insertMonitoringInPledgor(pledgor, monitoring);
                model.addAttribute("whereUpdateMonitoring", "responseSuccess");
                model.addAttribute("pledgor", pledgor);
                model.addAttribute("monitoringList" , monitoringListForPledgor);
                return "monitoring_card";

            default:
                return "monitoring_card";
        }
    }

    @GetMapping("/cost_history_card")
    public String costHistryCardGet(@RequestParam("pledgeSubjectId") long pledgeSubjectId, Model model){
        PledgeSubject pledgeSubject = pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId);
        model.addAttribute("pledgeSubject", pledgeSubject);
        model.addAttribute("costHistory", new CostHistory());
        return "cost_history_card";
    }

    @PostMapping("/cost_history_card")
    public String costHistryCardPost(@ModelAttribute CostHistory costHistory,
                                     @RequestParam("pledgeSubjectId") long pledgeSubjectId,
                                     Model model){

        PledgeSubject pledgeSubject = pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId);
        CostHistory costHistoryForPS = costHistoryService.insertCostHistoryInPledgeSubject(pledgeSubject, costHistory);
        List<CostHistory> costHistoryList = costHistoryService.getCostHistoryByPledgeSubjectId(pledgeSubject.getPledgeSubjectId());
        model.addAttribute("pledgeSubject", pledgeSubject);
        model.addAttribute("costHistoryList", costHistoryList);
        return "cost_history";
    }

    @GetMapping("/loan_agreement_card")
    public String loanAgreementCardPageGet(@RequestParam("loanAgreementId") long loanAgreementId,
                                        @RequestParam("whatDo") String whatDo,
                                        Model model){
        LoanAgreement loanAgreement = loanAgreementService.getLoanAgreementById(loanAgreementId);
        model.addAttribute("loanAgreement", loanAgreement);
        model.addAttribute("whatDo", whatDo);
        return "loan_agreement_card";
    }

    @PostMapping("/loan_agreement_card")
    public String loanAgreementCardPagePost(@ModelAttribute LoanAgreement loanAgreement,
                                        @RequestParam("whatDo") String whatDo,
                                        Model model){

        switch (whatDo){
            case "changeLA":
                LoanAgreement la = loanAgreementService.updateLoanAgreement(loanAgreement);
                model.addAttribute("loanAgreement", la);
                return "loan_agreement_detail";
            case "newLA":
        }

        return null;
    }

    @GetMapping("/pledge_agreement_card")
    public String pledgeAgreementCardGet(@RequestParam("pledgeAgreementId") long pledgeAgreementId,
                                         @RequestParam("whatDo") String whatDo,
                                         Model model){
        PledgeAgreement pledgeAgreement = pledgeAgreementService.getPledgeAgreement(pledgeAgreementId);
        model.addAttribute("pledgeAgreement", pledgeAgreement);
        model.addAttribute("whatDo", whatDo);
        return "pledge_agreement_card";
    }

    @PostMapping("/pledge_agreement_card")
    public String pledgeAgreementCardPost(@ModelAttribute PledgeAgreement pledgeAgreement,
                                          @RequestParam("whatDo") String whatDo,
                                          Model model){

        switch (whatDo){
            case "changePA":
                PledgeAgreement pa = pledgeAgreementService.updatePledgeAgreement(pledgeAgreement);
                model.addAttribute("pledgeAgreement", pa);
                return "pledge_agreement_detail";
            case "newPA":
        }
        return null;
    }

    @GetMapping("/pledge_subject_card")
    public String pledgeSubjectCardGet(@RequestParam("pledgeSubjectId") long pledgeSubjectId,
                                       @RequestParam("whatDo") String whatDo,
                                       Model model){

        PledgeSubject pledgeSubject = pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId);
        switch (whatDo){
            case "changePS":
//                PledgeSubject pledgeSubject = pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId);
//                model.addAttribute("pledgeSubject", pledgeSubject);
//                model.addAttribute("whatDo", whatDo);
//                return "pledge_subject_card";


                switch (pledgeSubject.getTypeOfCollateral()){
                    case "Авто/спецтехника":
                        PledgeSubjectAuto pledgeSubjectAuto = pledgeSubjectAutoService.getPledgeSubjectAuto(pledgeSubjectId);
                        model.addAttribute("pledgeSubject", pledgeSubjectAuto);
                        model.addAttribute("whatDo", whatDo);
                        return "pledge_subject_card";
                    case "Оборудование":
                        PledgeSubjectEquipment pledgeSubjectEquipment = pledgeSubjectEquipmentService.getPledgeSubjectEquipment(pledgeSubjectId);
                        model.addAttribute("pledgeSubject", pledgeSubjectEquipment);
                        model.addAttribute("whatDo", whatDo);
                        return "pledge_subject_card";
                    case "Ценные бумаги":
                        PledgeSubjectSecurities pledgeSubjectSecurities = pledgeSubjectSecuritiesService.getPledgeSubjectSecurities(pledgeSubjectId);
                        model.addAttribute("pledgeSubject", pledgeSubjectSecurities);
                        model.addAttribute("whatDo", whatDo);
                        return "pledge_subject_card";
                    case "Судно":
                        PledgeSubjectVessel pledgeSubjectVessel = pledgeSubjectVesselService.getPledgeSubjectVessel(pledgeSubjectId);
                        model.addAttribute("pledgeSubject", pledgeSubjectVessel);
                        model.addAttribute("whatDo", whatDo);
                        return "pledge_subject_card";
                    case "ТМЦ":
                        PledgeSubjectTBO pledgeSubjectTBO = pledgeSubjectTBOService.getPledgeSubjectTBO(pledgeSubjectId);
                        model.addAttribute("pledgeSubject", pledgeSubjectTBO);
                        model.addAttribute("whatDo", whatDo);
                        return "pledge_subject_card";
                    case "Недвижимость - ЗУ - собственность":
                        PledgeSubjectRealtyLandOwnership pledgeSubjectRealtyLandOwnership = pledgeSubjectRealtyLandOwnershipService.getPledgeSubjectRealtyLandOwnership(pledgeSubjectId);
                        List<LandCategory> landCategoryListForOwn = landCategoryService.getAllLandCategory();
                        model.addAttribute("pledgeSubject", pledgeSubjectRealtyLandOwnership);
                        model.addAttribute("landCategoryList", landCategoryListForOwn);
                        model.addAttribute("whatDo", whatDo);
                        return "pledge_subject_card";
                    case "Недвижимость - ЗУ - право аренды":
                        PledgeSubjectRealtyLandLease pledgeSubjectRealtyLandLease = pledgeSubjectRealtyLandLeaseService.getPledgeSubjectRealtyLandLease(pledgeSubjectId);
                        List<LandCategory> landCategoryListForLease = landCategoryService.getAllLandCategory();
                        model.addAttribute("pledgeSubject", pledgeSubjectRealtyLandLease);
                        model.addAttribute("landCategoryList", landCategoryListForLease);
                        model.addAttribute("whatDo", whatDo);
                        return "pledge_subject_card";
                    case "Недвижимость - здание/сооружение":
                        PledgeSubjectRealtyBuilding pledgeSubjectRealtyBuilding = pledgeSubjectRealtyBuildingService.getPledgeSubjectRealtyBuilding(pledgeSubjectId);
                        List<MarketSegment> marketSegmentListForBuild = marketSegmentService.getAllMarketSegment();
                        model.addAttribute("pledgeSubject", pledgeSubjectRealtyBuilding);
                        model.addAttribute("marketSegmentList", marketSegmentListForBuild);
                        model.addAttribute("whatDo", whatDo);
                        return "pledge_subject_card";
                    case "Недвижимость - помещение":
                        PledgeSubjectRealtyRoom pledgeSubjectRealtyRoom = pledgeSubjectRealtyRoomService.getPledgeSubjectRealtyRoom(pledgeSubjectId);
                        List<MarketSegment> marketSegmentListForRoom = marketSegmentService.getAllMarketSegment();
                        model.addAttribute("pledgeSubject", pledgeSubjectRealtyRoom);
                        model.addAttribute("marketSegmentList", marketSegmentListForRoom);
                        model.addAttribute("whatDo", whatDo);
                        return "pledge_subject_card";

                }
            case "newPS":
                switch (pledgeSubject.getTypeOfCollateral()){
                case "Авто/спецтехника":
                case "Оборудование":
                case "Ценные бумаги":
                case "Судно":
                case "ТМЦ":
                case "Недвижимость - ЗУ - собственность":
                case "Недвижимость - ЗУ - право аренды":
                case "Недвижимость - здание/сооружение":
                case "Недвижимость - помещение":

            }
        }
        return null;
    }

    @PostMapping("/pledge_subject_card")
    public String pledgeSubjectCardPagePost(@ModelAttribute PledgeSubject pledgeSubject,
                                            @ModelAttribute PledgeSubjectAuto pledgeSubjectAuto,
                                            @ModelAttribute PledgeSubjectEquipment pledgeSubjectEquipment,
                                            @ModelAttribute PledgeSubjectSecurities pledgeSubjectSecurities,
                                            @ModelAttribute PledgeSubjectVessel pledgeSubjectVessel,
                                            @ModelAttribute PledgeSubjectTBO pledgeSubjectTBO,
                                            @ModelAttribute PledgeSubjectRealty pledgeSubjectRealty,
                                            @RequestParam("whatDo") String whatDo,
                                            Model model){



        switch (whatDo){
            case "changePS":
                switch (pledgeSubject.getTypeOfCollateral()){
                    case "Авто/спецтехника":
                        PledgeSubjectAuto pledgeSubjectAutoUpdated = pledgeSubjectAutoService
                                .updatePledgeSubjectAuto(pledgeSubjectAuto);
                        model.addAttribute("pledgeSubject", pledgeSubjectAutoUpdated);
                        break;
                    case "Оборудование":
                        PledgeSubjectEquipment pledgeSubjectEquipmentUpdated = pledgeSubjectEquipmentService
                                .updatePledgeSubjectEquipment(pledgeSubjectEquipment);
                        model.addAttribute("pledgeSubject", pledgeSubjectEquipmentUpdated);
                        break;
                    case "Ценные бумаги":
                        PledgeSubjectSecurities pledgeSubjectSecuritiesUpdated = pledgeSubjectSecuritiesService
                                .updatePledgeSubjectSecurities(pledgeSubjectSecurities);
                        model.addAttribute("pledgeSubject", pledgeSubjectSecuritiesUpdated);
                        break;
                    case "Судно":
                        PledgeSubjectVessel pledgeSubjectVesselUpdated = pledgeSubjectVesselService
                                .updatePledgeSubjectVessel(pledgeSubjectVessel);
                        model.addAttribute("pledgeSubject", pledgeSubjectVesselUpdated);
                        break;
                    case "ТМЦ":
                        PledgeSubjectTBO pledgeSubjectTBOUpdated = pledgeSubjectTBOService
                                .updatePledgeSubjectTBO(pledgeSubjectTBO);
                        model.addAttribute("pledgeSubject", pledgeSubjectTBOUpdated);
                        break;
                    case "Недвижимость - ЗУ - собственность":
                        PledgeSubjectRealtyLandOwnership landOwnershipUpdated = pledgeSubjectRealtyLandOwnershipService
                                .updatePledgeSubjectRealtyLandOwnership((PledgeSubjectRealtyLandOwnership) pledgeSubjectRealty);
                        model.addAttribute("pledgeSubject", landOwnershipUpdated);
                        break;
                    case "Недвижимость - ЗУ - право аренды":
                        PledgeSubjectRealtyLandLease pledgeSubjectRealtyLandLeaseUpdated = pledgeSubjectRealtyLandLeaseService
                                .updatePledgeSubjectRealtyLandLease((PledgeSubjectRealtyLandLease) pledgeSubjectRealty);
                        model.addAttribute("pledgeSubject", pledgeSubjectRealtyLandLeaseUpdated);
                        break;
                    case "Недвижимость - здание/сооружение":
                        PledgeSubjectRealtyBuilding pledgeSubjectRealtyBuildingUpdated = pledgeSubjectRealtyBuildingService
                                .updatePledgeSubjectRealtyBuilding((PledgeSubjectRealtyBuilding) pledgeSubjectRealty);
                        model.addAttribute("pledgeSubject", pledgeSubjectRealtyBuildingUpdated);
                        break;
                    case "Недвижимость - помещение":
                        PledgeSubjectRealtyRoom pledgeSubjectRealtyRoomUpdated = pledgeSubjectRealtyRoomService
                                .updatePledgeSubjectRealtyRoom((PledgeSubjectRealtyRoom) pledgeSubjectRealty);
                        model.addAttribute("pledgeSubject", pledgeSubjectRealtyRoomUpdated);
                        break;
                        default:
                            return null;
                }
                return "pledge_subject_detail";
            case "newPS":
                switch (pledgeSubject.getTypeOfCollateral()){
                    case "Авто/спецтехника":
                    case "Оборудование":
                    case "Ценные бумаги":
                    case "Судно":
                    case "ТМЦ":
                    case "Недвижимость - ЗУ - собственность":
                    case "Недвижимость - ЗУ - право аренды":
                    case "Недвижимость - здание/сооружение":
                    case "Недвижимость - помещение":

                }
        }

        return null;
    }



//    @PostMapping("/search")
//    public String searchActionPage(@RequestParam("numLA") String numLA, @RequestParam("pfo") Byte pfo, @RequestParam("loaner") String loaner, Model model){
//        System.out.println("@PostMapping(\"/search\")!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//        System.out.println(numLA + pfo);
//        Client client = clientService.getClientByClientId(2);
//        List<LoanAgreement> loanAgreements = loanAgreementService.getLasdasd(numLA, pfo, client);
//        model.addAttribute("loanAgreements", loanAgreements);
//        return "search";
//    }

//    @GetMapping("/edit")
//    public String editPage(@RequestParam("id") long id, Model model) {
//        Employee employee = repositoryEmployee.findById(id).orElseThrow(NotFoundException::new);
//        model.addAttribute("employee", employee);
//        return "edit";
//    }
//
//    @PostMapping("/edit")
//    public String changeEmployee(@RequestParam("id") long id, @RequestParam("surname") String suname, @RequestParam("name") String name, Model model){
//        Employee emp = repositoryEmployee.findById(id).get();
//        emp.setSurname(suname);
//        emp.setName(name);
//        repositoryEmployee.save(emp);
//        System.out.println("Cool!!!!!!!!!!!!!!!!!!!!!!");
//        model.addAttribute("employee", emp);
//        return "edit";
//    }
//
//    @GetMapping("/user")
//    public String userPage() {
//        //myService.onlyUser();
//        return "user";
//    }
//
//    @GetMapping("/admin")
//    public String adminPage() {
//        //myService.onlyAdmin();
//        return "admin";
//    }
//
//    @GetMapping("/authenticated")
//    public String authenticatedPage() {
//        UserDetails userDetails = (UserDetails) SecurityContextHolder
//                .getContext().getAuthentication().getPrincipal();
//        System.out.println(userDetails.getUsername());
//        return "authenticated";
//    }
//
//    @GetMapping("/success")
//    public String successPage() {
//        return "success";
//    }


}
