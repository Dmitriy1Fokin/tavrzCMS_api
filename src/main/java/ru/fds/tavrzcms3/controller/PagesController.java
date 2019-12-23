package ru.fds.tavrzcms3.controller;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.fds.tavrzcms3.dictionary.TypeOfPledgeAgreement;
import ru.fds.tavrzcms3.domain.embedded.ClientIndividual;
import ru.fds.tavrzcms3.domain.embedded.ClientLegalEntity;
import ru.fds.tavrzcms3.service.*;
import ru.fds.tavrzcms3.domain.*;

import java.io.*;
import java.util.*;


@Controller
public class PagesController {

    private final EmployeeService employeeService;
    private final PledgeAgreementService pledgeAgreementService;
    private final ClientService clientService;
    private final LoanAgreementService loanAgreementService;
    private final FilesService filesService;

    private static final String PAGE_UPDATE = "update";
    private static final String MSG_WRONG_LINK = "Неверная ссылка";
    private static final String ATTR_MESSAGE_ERROR = "messageError";


    public PagesController(EmployeeService employeeService,
                           PledgeAgreementService pledgeAgreementService,
                           ClientService clientService,
                           LoanAgreementService loanAgreementService,
                           FilesService filesService) {
        this.employeeService = employeeService;
        this.pledgeAgreementService = pledgeAgreementService;
        this.clientService = clientService;
        this.loanAgreementService = loanAgreementService;
        this.filesService = filesService;
    }

    @GetMapping("/login")
    public String login() {
        return "/login";
    }

    @GetMapping("/")
    public String homePage(@AuthenticationPrincipal User user, Model model) {
        GrantedAuthority authorityUser = new SimpleGrantedAuthority("ROLE_USER");
        GrantedAuthority authorityChief = new SimpleGrantedAuthority("ROLE_USER_CHIEF");

        Collection<GrantedAuthority> grantedAuthorities = user.getAuthorities();
        if(grantedAuthorities.contains(authorityUser) || grantedAuthorities.contains(authorityChief)){
            Employee employee = employeeService.getEmployeeByUser(user);
            model.addAttribute("employee", employee);

            int countOfPA = pledgeAgreementService.countOfCurrentPledgeAgreementForEmployee(employee);
            model.addAttribute("countOfAllPledgeAgreement", countOfPA);

            int countOfPervPA = pledgeAgreementService.countOfCurrentPledgeAgreementForEmployee(employee, TypeOfPledgeAgreement.PERV);
            model.addAttribute("countOfPervPledgeAgreements", countOfPervPA);

            int countOfPoslPA = pledgeAgreementService.countOfCurrentPledgeAgreementForEmployee(employee, TypeOfPledgeAgreement.POSL);
            model.addAttribute("countOfPoslPledgeAgreements", countOfPoslPA);

            int countOfLoanAgreements = loanAgreementService.countOfCurrentLoanAgreementsByEmployee(employee);
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
        }




        return "home";
    }

    @GetMapping("/update")
    public String updatePage(){
        return PAGE_UPDATE;
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
//                    clientLegalEntityList = clientService.updateInsertClientLegalEntityList(clientLegalEntityList);

                    model.addAttribute("clientLegalEntityList", clientLegalEntityList);

                }else if(whatUpload.get().equals("clientIndividual")){
                    List<ClientIndividual> clientIndividualList = filesService.getClientIndividualFromExcel(uploadFile);
//                    clientIndividualList = clientService.updateInsertClientIndividualList(clientIndividualList);

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

                return PAGE_UPDATE;


            }else {
                model.addAttribute(ATTR_MESSAGE_ERROR, "Ошибка импорта.");
                return PAGE_UPDATE;
            }

        }catch (IOException ioe){
            ioe.printStackTrace();
            model.addAttribute(ATTR_MESSAGE_ERROR, ioe.getMessage());
            return PAGE_UPDATE;

        }catch (InvalidFormatException ife){
            ife.printStackTrace();
            model.addAttribute(ATTR_MESSAGE_ERROR, ife.getMessage());
            return PAGE_UPDATE;
        }
    }
}
