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
import ru.fds.tavrzcms3.annotation.LogModificationDB;
import ru.fds.tavrzcms3.dictionary.TypeOfPledgeAgreement;
import ru.fds.tavrzcms3.domain.embedded.ClientIndividual;
import ru.fds.tavrzcms3.domain.embedded.ClientLegalEntity;
import ru.fds.tavrzcms3.dto.DtoFactory;
import ru.fds.tavrzcms3.dto.EmployeeDto;
import ru.fds.tavrzcms3.service.*;
import ru.fds.tavrzcms3.domain.*;

import java.io.*;
import java.util.*;


@Controller
public class MainController {

    private final EmployeeService employeeService;
    private final PledgeAgreementService pledgeAgreementService;
    private final LoanAgreementService loanAgreementService;
    private final FilesService filesService;

    private final DtoFactory dtoFactory;

    private static final String PAGE_UPDATE = "update";
    private static final String MSG_WRONG_LINK = "Неверная ссылка";
    private static final String ATTR_MESSAGE_ERROR = "messageError";


    public MainController(EmployeeService employeeService,
                          PledgeAgreementService pledgeAgreementService,
                          LoanAgreementService loanAgreementService,
                          FilesService filesService,
                          DtoFactory dtoFactory) {
        this.employeeService = employeeService;
        this.pledgeAgreementService = pledgeAgreementService;
        this.loanAgreementService = loanAgreementService;
        this.filesService = filesService;
        this.dtoFactory = dtoFactory;
    }

    @GetMapping("/login")
    public String login() {
        return "/login";
    }

    @GetMapping("/")
    public String homePage(@AuthenticationPrincipal User user, Model model) {
        final GrantedAuthority authorityUser = new SimpleGrantedAuthority("ROLE_USER");
        final GrantedAuthority authorityChief = new SimpleGrantedAuthority("ROLE_USER_CHIEF");
        final GrantedAuthority authorityGuest = new SimpleGrantedAuthority("ROLE_GUEST");
        final GrantedAuthority authorityAdmin = new SimpleGrantedAuthority("ROLE_ADMIN");

        Collection<GrantedAuthority> grantedAuthorities = user.getAuthorities();
        if(grantedAuthorities.contains(authorityUser) || grantedAuthorities.contains(authorityChief)){
            Employee employee = employeeService.getEmployeeByUser(user);
            EmployeeDto employeeDto = dtoFactory.getEmployeeDto(employee);
            model.addAttribute("employeeDto", employeeDto);

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

            if(grantedAuthorities.contains(authorityChief)){
                Map<EmployeeDto, List<Integer>> employeeDtoMapExcludeChief = new HashMap<>();
                List<Employee> employeeListExcludeChief = employeeService.getEmployeesExcludeEmployee(employee.getEmployeeId());
                for(Employee emp : employeeListExcludeChief){
                    List<Integer> integerList = new ArrayList<>();
                    integerList.add(pledgeAgreementService.countOfCurrentPledgeAgreementForEmployee(emp));
                    integerList.add(pledgeAgreementService.countOfCurrentPledgeAgreementForEmployee(emp, TypeOfPledgeAgreement.PERV));
                    integerList.add(pledgeAgreementService.countOfCurrentPledgeAgreementForEmployee(emp, TypeOfPledgeAgreement.POSL));
                    integerList.add(loanAgreementService.countOfCurrentLoanAgreementsByEmployee(emp));
                    integerList.add(pledgeAgreementService.countOfMonitoringNotDone(emp));
                    integerList.add(pledgeAgreementService.countOfMonitoringIsDone(emp));
                    integerList.add(pledgeAgreementService.countOfMonitoringOverdue(emp));
                    integerList.add(pledgeAgreementService.countOfConclusionNotDone(emp));
                    integerList.add(pledgeAgreementService.countOfConclusionIsDone(emp));
                    integerList.add(pledgeAgreementService.countOfConclusionOverdue(emp));

                    EmployeeDto emplDto = dtoFactory.getEmployeeDto(emp);
                    employeeDtoMapExcludeChief.put(emplDto, integerList);
                }

                model.addAttribute("employeeDtoMapExcludeChief", employeeDtoMapExcludeChief);
            }
        }else if(grantedAuthorities.contains(authorityGuest) || grantedAuthorities.contains(authorityAdmin)){

            model.addAttribute("countOfAllPledgeAgreement", pledgeAgreementService.countOfAllCurrentPledgeAgreements());
            model.addAttribute("countOfPervPledgeAgreements", pledgeAgreementService.countOfAllCurrentPledgeAgreements(TypeOfPledgeAgreement.PERV));
            model.addAttribute("countOfPoslPledgeAgreements", pledgeAgreementService.countOfAllCurrentPledgeAgreements(TypeOfPledgeAgreement.POSL));
            model.addAttribute("countOfLoanAgreements", loanAgreementService.countOfAllCurrentLoanAgreements());
        }


        return "home";
    }

    @GetMapping("/employee")
    public String homeEmployeePage(@RequestParam("id") Long employeeId, Model model) {

        Optional<Employee> employeeOptional = employeeService.getEmployeeById(employeeId);

        if(employeeOptional.isPresent()){
            EmployeeDto employeeDto = dtoFactory.getEmployeeDto(employeeOptional.get());
            model.addAttribute("employeeDto", employeeDto);

            int countOfPA = pledgeAgreementService.countOfCurrentPledgeAgreementForEmployee(employeeOptional.get());
            model.addAttribute("countOfAllPledgeAgreement", countOfPA);

            int countOfPervPA = pledgeAgreementService.countOfCurrentPledgeAgreementForEmployee(employeeOptional.get(), TypeOfPledgeAgreement.PERV);
            model.addAttribute("countOfPervPledgeAgreements", countOfPervPA);

            int countOfPoslPA = pledgeAgreementService.countOfCurrentPledgeAgreementForEmployee(employeeOptional.get(), TypeOfPledgeAgreement.POSL);
            model.addAttribute("countOfPoslPledgeAgreements", countOfPoslPA);

            int countOfLoanAgreements = loanAgreementService.countOfCurrentLoanAgreementsByEmployee(employeeOptional.get());
            model.addAttribute("countOfLoanAgreements", countOfLoanAgreements);

            int countOfMonitoringNotDone = pledgeAgreementService.countOfMonitoringNotDone(employeeOptional.get());
            model.addAttribute("countOfMonitoringNotDone", countOfMonitoringNotDone);

            int countOfMonitoringIsDone = pledgeAgreementService.countOfMonitoringIsDone(employeeOptional.get());
            model.addAttribute("countOfMonitoringIsDone", countOfMonitoringIsDone);

            int countOfMonitoringOverdue = pledgeAgreementService.countOfMonitoringOverdue(employeeOptional.get());
            model.addAttribute("countOfMonitoringOverdue", countOfMonitoringOverdue);

            int countOfConclusionNotDone = pledgeAgreementService.countOfConclusionNotDone(employeeOptional.get());
            model.addAttribute("countOfConclusionNotDone", countOfConclusionNotDone);

            int countOfConclusionIsDone = pledgeAgreementService.countOfConclusionIsDone(employeeOptional.get());
            model.addAttribute("countOfConclusionIsDone", countOfConclusionIsDone);

            int countOfConclusionOverdue = pledgeAgreementService.countOfConclusionOverdue(employeeOptional.get());
            model.addAttribute("countOfConclusionOverdue", countOfConclusionOverdue);

        }else
            throw new IllegalArgumentException(MSG_WRONG_LINK);

        return "home";
    }

    @GetMapping("/update")
    public String updatePage(){
        return PAGE_UPDATE;
    }

    @LogModificationDB
    @PostMapping("/upload")
    public String importEntityFromExcel(@AuthenticationPrincipal User user,
                                        @RequestParam("file") Optional<MultipartFile> file,
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
