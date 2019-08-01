package ru.fds.tavrzcms3.rest;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.fds.tavrzcms3.Service.*;
import ru.fds.tavrzcms3.domain.*;

import java.util.List;


@Controller
public class PagesController {

    private final EmployeeService employeeService;
    private final PledgeEgreementService pledgeEgreementService;
    private final PledgeSubjectService pledgeSubjectService;
    private final InsuranceService insuranceService;
    private final EncumbranceService encumbranceService;

    public PagesController(EmployeeService employeeService,
                           PledgeEgreementService pledgeEgreementService,
                           PledgeSubjectService pledgeSubjectService,
                           InsuranceService insuranceService,
                           EncumbranceService encumbranceService) {
        this.employeeService = employeeService;
        this.pledgeEgreementService = pledgeEgreementService;
        this.pledgeSubjectService = pledgeSubjectService;
        this.insuranceService = insuranceService;
        this.encumbranceService = encumbranceService;
    }

    @GetMapping("/")
    public String homePage(@AuthenticationPrincipal User user, Model model) {
        Employee employee = employeeService.getEmployeeByAppUser(user);
        model.addAttribute("employee", employee);

        int countOfPE = employeeService.getCountOfAllPledgeEgreements(user);
        model.addAttribute("countOfAllPledgeEgreement", countOfPE);

        int countOfPervPE = employeeService.getCountOfPervPledgeEgreements(user);
        model.addAttribute("countOfPervPledgeEgreements", countOfPervPE);

        int countOfPoslPE = employeeService.getCountOfPoslPledgeEgreements(user);
        model.addAttribute("countOfPoslPledgeEgreements", countOfPoslPE);

        return "home";
    }

    @GetMapping("/pledge_egreements")
    public String pledgeEgreementPage(@RequestParam("employeeId") long employeeId, @RequestParam("pervPosl") String pervPosl, Model model) {
        Employee employee = employeeService.getEmployeeById(employeeId);
        model.addAttribute("employee", employee);
        model.addAttribute("pervPosl", pervPosl);
        return "pledge_egreements";
    }

    @GetMapping("/pledge_subjects")
    public String pledgeSubjectsPage(@RequestParam("pledgeEgreementId") long pledgeEgreementId, Model model){
        PledgeAgreement pledgeAgreement = pledgeEgreementService.getPledgeEgreementById(pledgeEgreementId);
        model.addAttribute("pledgeEgreement", pledgeAgreement);
        return "pledge_subjects";
    }

    @GetMapping("/insurances")
    public String insurancesPage(@RequestParam("pledgeSubjectId") long pledgeSubjectId, Model model){
        PledgeSubject pledgeSubject = pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId);
        List<Insurance> insuranceList = insuranceService.getInsurancesByPledgeSubject(pledgeSubject);
        model.addAttribute("pledgeSubjectName", pledgeSubject.getName());
        model.addAttribute("insuranceList", insuranceList);
        return "insurances";
    }

    @GetMapping("/encumbrances")
    public  String encumbrancePage(@RequestParam("pledgeSubjectId") long pledgeSubjectId, Model model){
        PledgeSubject pledgeSubject = pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId);
        List<Encumbrance> encumbranceList = encumbranceService.getEncumbranceByPledgeSubject(pledgeSubject);
        model.addAttribute("pledgeSubjectName", pledgeSubject.getName());
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
        return  "pledgor";
    }

    @GetMapping("/pledge_egreement_detail")
    public String pledgeEgreementsDetailPage(@RequestParam("pledgeEgreementId") long pledgeEgreementId, Model model){
        return "pledge_egreement_detail";
    }

    @GetMapping("/loan_egreement_detail")
    public String loanEgreementDetailPage(@RequestParam("loanEgreementId") long loanEgreementId, Model model){
        return "loan_egreement_detail";
    }

    @GetMapping("/loaner")
    public String loanerPage(@RequestParam("loanerId") long loanerId, Model model){
        return "loaner";
    }





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
