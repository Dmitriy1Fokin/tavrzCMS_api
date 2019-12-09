package ru.fds.tavrzcms3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.fds.tavrzcms3.domain.*;
import ru.fds.tavrzcms3.service.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/monitoring")
public class MonitoringController {

    private final PledgeSubjectService pledgeSubjectService;
    private final MonitoringService monitoringService;
    private final EmployeeService employeeService;
    private final PledgeAgreementService pledgeAgreementService;
    private final ClientService clientService;

    private static final String MSG_WRONG_LINK = "Неверная ссылка";
    private static final String ATTR_PLEDGE_SUBJECT = "pledgeSubject";
    private static final String ATTR_MONITORING_LIST = "monitoringList";
    private static final String ATTR_WHERE_UPDATE_MONITORING = "whereUpdateMonitoring";
    private static final String ATTR_PLEDGE_AGREEMENT = "pledgeAgreement";
    private static final String ATTR_MONITORING = "monitoring";
    private static final String ATTR_CLIENT = "client";

    public MonitoringController(PledgeSubjectService pledgeSubjectService,
                                MonitoringService monitoringService,
                                EmployeeService employeeService,
                                PledgeAgreementService pledgeAgreementService,
                                ClientService clientService) {
        this.pledgeSubjectService = pledgeSubjectService;
        this.monitoringService = monitoringService;
        this.employeeService = employeeService;
        this.pledgeAgreementService = pledgeAgreementService;
        this.clientService = clientService;
    }

    @GetMapping("pledge_subject")
    public String monitoringPage(@RequestParam("pledgeSubjectId") long pledgeSubjectId,
                                 Model model){
        PledgeSubject pledgeSubject = pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId).orElseThrow(() -> new RuntimeException(MSG_WRONG_LINK));
        Collection<Monitoring> monitoringList = monitoringService.getMonitoringByPledgeSubject(pledgeSubject);
        model.addAttribute(ATTR_PLEDGE_SUBJECT, pledgeSubject);
        model.addAttribute(ATTR_MONITORING_LIST, monitoringList);

        return "monitoring/pledge_subject";
    }

    @GetMapping("/pledge_agreements")
    public String monitoringPledgeAgreementsPage(@RequestParam("employeeId") long employeeId,
                                                 Model model){

        Employee employee = employeeService.getEmployeeById(employeeId).orElseThrow(() -> new RuntimeException(MSG_WRONG_LINK));
        List<PledgeAgreement> pledgeAgreementListWithMonitoringNotDone = pledgeAgreementService.getPledgeAgreementWithMonitoringNotDone(employee);
        List<PledgeAgreement> pledgeAgreementListWithMonitoringIsDone = pledgeAgreementService.getPledgeAgreementWithMonitoringIsDone(employee);
        List<PledgeAgreement> pledgeAgreementListWithMonitoringOverdue = pledgeAgreementService.getPledgeAgreementWithMonitoringOverDue(employee);
        model.addAttribute("pledgeAgreementListWithMonitoringNotDone", pledgeAgreementListWithMonitoringNotDone);
        model.addAttribute("pledgeAgreementListWithMonitoringIsDone", pledgeAgreementListWithMonitoringIsDone);
        model.addAttribute("pledgeAgreementListWithMonitoringOverdue", pledgeAgreementListWithMonitoringOverdue);

        return "monitoring/pledge_agreements";
    }

    @GetMapping("/card")
    public String monitoringCardPageGet(@RequestParam("whereUpdateMonitoring") String whereUpdateMonitoring,
                                        @RequestParam("pledgeAgreementId") Optional<Long> pledgeAgreementId,
                                        @RequestParam("pledgeSubjectId") Optional<Long> pledgeSubjectId,
                                        @RequestParam("pledgorId") Optional<Long> pledgorId,
                                        Model model){
        if(whereUpdateMonitoring.equals("pa")){
            Optional<PledgeAgreement> pledgeAgreementOptional = pledgeAgreementService.getPledgeAgreementById(pledgeAgreementId
                    .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK)));

            model.addAttribute(ATTR_PLEDGE_AGREEMENT, pledgeAgreementOptional
                    .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK)));
            model.addAttribute(ATTR_WHERE_UPDATE_MONITORING, whereUpdateMonitoring);
            model.addAttribute(ATTR_MONITORING, new Monitoring());

        }else if(whereUpdateMonitoring.equals("ps")){
            Optional<PledgeSubject> pledgeSubjectOptional = pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId
                    .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK)));

            model.addAttribute(ATTR_PLEDGE_SUBJECT, pledgeSubjectOptional
                    .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK)));
            model.addAttribute(ATTR_WHERE_UPDATE_MONITORING, whereUpdateMonitoring);
            model.addAttribute(ATTR_MONITORING, new Monitoring());

        }else if(whereUpdateMonitoring.equals("pledgor")){
            Optional<Client> clientOptional = clientService.getClientById(pledgorId
                    .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK)));

            model.addAttribute(ATTR_CLIENT, clientOptional
                    .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK)));
            model.addAttribute(ATTR_WHERE_UPDATE_MONITORING, whereUpdateMonitoring);
            model.addAttribute(ATTR_MONITORING, new Monitoring());

        }else{
            throw new IllegalArgumentException(MSG_WRONG_LINK);
        }

        return "monitoring/card";
    }

    @PostMapping("/insert")
    public String monitoringCardPagePost(@Valid Monitoring monitoring,
                                         BindingResult bindingResult,
                                         @RequestParam("whereUpdateMonitoring") String whereUpdateMonitoring,
                                         @RequestParam("pledgeAgreementId") Optional<Long> pledgeAgreementId,
                                         @RequestParam("pledgeSubjectId") Optional<Long> pledgeSubjectId,
                                         @RequestParam("pledgorId") Optional<Long> pledgorId,
                                         Model model){

        if(bindingResult.hasErrors()) {
            if(whereUpdateMonitoring.equals("pa")){
                PledgeAgreement pledgeAgreement = pledgeAgreementService.getPledgeAgreementById(pledgeAgreementId
                        .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK)))
                        .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK));
                model.addAttribute(ATTR_WHERE_UPDATE_MONITORING, whereUpdateMonitoring);
                model.addAttribute(ATTR_PLEDGE_AGREEMENT, pledgeAgreement);
            }else if(whereUpdateMonitoring.equals("ps")){
                PledgeSubject pledgeSubject = pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId
                        .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK)))
                        .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK));
                model.addAttribute(ATTR_WHERE_UPDATE_MONITORING, whereUpdateMonitoring);
                model.addAttribute(ATTR_PLEDGE_SUBJECT, pledgeSubject);
            }else if(whereUpdateMonitoring.equals("pledgor")){
                Client client = clientService.getClientById(pledgorId
                        .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK)))
                        .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK));
                model.addAttribute(ATTR_WHERE_UPDATE_MONITORING, whereUpdateMonitoring);
                model.addAttribute(ATTR_CLIENT, client);
            }else {
                throw new IllegalArgumentException(MSG_WRONG_LINK);
            }
            return "monitoring/card";
        }


        if(whereUpdateMonitoring.equals("pa")){
            PledgeAgreement pledgeAgreement = pledgeAgreementService.getPledgeAgreementById(pledgeAgreementId
                    .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK)))
                    .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK));

            List<Monitoring> monitoringListForPA = monitoringService.insertMonitoringInPledgeAgreement(pledgeAgreement, monitoring);

            model.addAttribute(ATTR_WHERE_UPDATE_MONITORING, "responseSuccess");
            model.addAttribute(ATTR_PLEDGE_AGREEMENT, pledgeAgreement);
            model.addAttribute(ATTR_MONITORING_LIST , monitoringListForPA);

            return "monitoring/card";

        }else if(whereUpdateMonitoring.equals("ps")){
            PledgeSubject pledgeSubject = pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId
                    .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK)))
                    .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK));

            monitoring.setPledgeSubject(pledgeSubject);
            monitoringService.insertMonitoringInPledgeSubject(monitoring);
            Collection<Monitoring> monitoringListForPS = monitoringService.getMonitoringByPledgeSubject(pledgeSubject);

            model.addAttribute(ATTR_PLEDGE_SUBJECT, pledgeSubject);
            model.addAttribute(ATTR_MONITORING_LIST, monitoringListForPS);

            return "monitoring/pledge_subject";

        }else if(whereUpdateMonitoring.equals("pledgor")){
            Client client = clientService.getClientById(pledgorId
                    .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK)))
                    .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK));

            List<Monitoring> monitoringListForPledgor = monitoringService.insertMonitoringInPledgor(client, monitoring);

            model.addAttribute(ATTR_WHERE_UPDATE_MONITORING, "responseSuccess");
            model.addAttribute(ATTR_CLIENT, client);
            model.addAttribute(ATTR_MONITORING_LIST , monitoringListForPledgor);

            return "monitoring/card";

        }else{
            throw new IllegalArgumentException(MSG_WRONG_LINK);
        }
    }
}
