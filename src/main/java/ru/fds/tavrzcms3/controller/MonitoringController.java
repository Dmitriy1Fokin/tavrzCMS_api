package ru.fds.tavrzcms3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.fds.tavrzcms3.domain.*;
import ru.fds.tavrzcms3.dto.ClientDto;
import ru.fds.tavrzcms3.dto.DtoFactory;
import ru.fds.tavrzcms3.dto.MonitoringDto;
import ru.fds.tavrzcms3.dto.PledgeAgreementDto;
import ru.fds.tavrzcms3.dto.PledgeSubjectDto;
import ru.fds.tavrzcms3.service.*;
import ru.fds.tavrzcms3.validate.ValidatorEntity;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/monitoring")
public class MonitoringController {

    private final PledgeSubjectService pledgeSubjectService;
    private final MonitoringService monitoringService;
    private final EmployeeService employeeService;
    private final PledgeAgreementService pledgeAgreementService;
    private final ClientService clientService;

    private final DtoFactory dtoFactory;

    private ValidatorEntity validatorEntity;

    private static final String MSG_WRONG_LINK = "Неверная ссылка";
    private static final String PAGE_CARD = "monitoring/card";
    private static final String ATTR_PLEDGE_SUBJECT = "pledgeSubject";
    private static final String ATTR_MONITORING_LIST = "monitoringList";
    private static final String ATTR_WHERE_UPDATE_MONITORING = "whereUpdateMonitoring";
    private static final String ATTR_PLEDGE_AGREEMENT = "pledgeAgreement";
    private static final String ATTR_MONITORING = "monitoringDto";
    private static final String ATTR_CLIENT = "client";
    private static final String ATTR_COUNT_MONITORING = "countMonitoring";

    public MonitoringController(PledgeSubjectService pledgeSubjectService,
                                MonitoringService monitoringService,
                                EmployeeService employeeService,
                                PledgeAgreementService pledgeAgreementService,
                                ClientService clientService,
                                DtoFactory dtoFactory,
                                ValidatorEntity validatorEntity) {
        this.pledgeSubjectService = pledgeSubjectService;
        this.monitoringService = monitoringService;
        this.employeeService = employeeService;
        this.pledgeAgreementService = pledgeAgreementService;
        this.clientService = clientService;
        this.dtoFactory = dtoFactory;
        this.validatorEntity = validatorEntity;
    }

    @GetMapping("/pledge_subject")
    public String monitoringPage(@RequestParam("pledgeSubjectId") long pledgeSubjectId,
                                 Model model){

        PledgeSubject pledgeSubject = pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId)
                .orElseThrow(() -> new IllegalArgumentException(MSG_WRONG_LINK));

        PledgeSubjectDto pledgeSubjectDto = dtoFactory.getPledgeSubjectDto(pledgeSubject);
        List<MonitoringDto> monitoringDtoList = dtoFactory.getMonitoringsDto(monitoringService.getMonitoringByPledgeSubject(pledgeSubject));

        model.addAttribute(ATTR_PLEDGE_SUBJECT, pledgeSubjectDto);
        model.addAttribute(ATTR_MONITORING_LIST, monitoringDtoList);

        return "monitoring/pledge_subject";
    }

    @GetMapping("/pledge_agreements")
    public String monitoringPledgeAgreementsPage(@RequestParam("employeeId") long employeeId,
                                                 Model model){

        Employee employee = employeeService.getEmployeeById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException(MSG_WRONG_LINK));
        List<PledgeAgreementDto> pledgeAgreementListWithMonitoringNotDone = dtoFactory
                .getPledgeAgreementsDto(pledgeAgreementService.getPledgeAgreementWithMonitoringNotDone(employee));
        List<PledgeAgreementDto> pledgeAgreementListWithMonitoringIsDone = dtoFactory
                .getPledgeAgreementsDto(pledgeAgreementService.getPledgeAgreementWithMonitoringIsDone(employee));
        List<PledgeAgreementDto> pledgeAgreementListWithMonitoringOverdue = dtoFactory
                .getPledgeAgreementsDto(pledgeAgreementService.getPledgeAgreementWithMonitoringOverDue(employee));
        model.addAttribute("pledgeAgreementListWithMonitoringNotDone", pledgeAgreementListWithMonitoringNotDone);
        model.addAttribute("pledgeAgreementListWithMonitoringIsDone", pledgeAgreementListWithMonitoringIsDone);
        model.addAttribute("pledgeAgreementListWithMonitoringOverdue", pledgeAgreementListWithMonitoringOverdue);

        return "monitoring/pledge_agreements";
    }

    @GetMapping("/card")
    public String monitoringCardPage(@RequestParam("whereUpdateMonitoring") String whereUpdateMonitoring,
                                        @RequestParam("pledgeAgreementId") Optional<Long> pledgeAgreementId,
                                        @RequestParam("pledgeSubjectId") Optional<Long> pledgeSubjectId,
                                        @RequestParam("pledgorId") Optional<Long> pledgorId,
                                        Model model){

        if(whereUpdateMonitoring.equals("pa")){

            PledgeAgreement pledgeAgreement = pledgeAgreementService.getPledgeAgreementById(pledgeAgreementId
                    .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK)))
                    .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK));

            PledgeAgreementDto pledgeAgreementDto = dtoFactory.getPledgeAgreementDto(pledgeAgreement);

            model.addAttribute(ATTR_PLEDGE_AGREEMENT, pledgeAgreementDto);
            model.addAttribute(ATTR_WHERE_UPDATE_MONITORING, whereUpdateMonitoring);
            model.addAttribute(ATTR_MONITORING, new MonitoringDto());

        }else if(whereUpdateMonitoring.equals("ps")){

            PledgeSubject pledgeSubject = pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId
                    .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK)))
                    .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK));

            PledgeSubjectDto pledgeSubjectDto = dtoFactory.getPledgeSubjectDto(pledgeSubject);

            model.addAttribute(ATTR_PLEDGE_SUBJECT, pledgeSubjectDto);
            model.addAttribute(ATTR_WHERE_UPDATE_MONITORING, whereUpdateMonitoring);
            model.addAttribute(ATTR_MONITORING, new MonitoringDto());

        }else if(whereUpdateMonitoring.equals("pledgor")){

            Client client = clientService.getClientById(pledgorId
                    .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK)))
                    .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK));

            ClientDto clientDto = dtoFactory.getClientDto(client);

            model.addAttribute(ATTR_CLIENT, clientDto);
            model.addAttribute(ATTR_WHERE_UPDATE_MONITORING, whereUpdateMonitoring);
            model.addAttribute(ATTR_MONITORING, new MonitoringDto());

        }else{
            throw new IllegalArgumentException(MSG_WRONG_LINK);
        }

        return PAGE_CARD;
    }

    @PostMapping("/insert")
    public String insertMonitoring(@Valid MonitoringDto monitoringDto,
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

                PledgeAgreementDto pledgeAgreementDto = dtoFactory.getPledgeAgreementDto(pledgeAgreement);

                model.addAttribute(ATTR_PLEDGE_AGREEMENT, pledgeAgreementDto);
                model.addAttribute(ATTR_WHERE_UPDATE_MONITORING, whereUpdateMonitoring);

            }else if(whereUpdateMonitoring.equals("ps")){

                PledgeSubject pledgeSubject = pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId
                        .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK)))
                        .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK));

                PledgeSubjectDto pledgeSubjectDto = dtoFactory.getPledgeSubjectDto(pledgeSubject);

                model.addAttribute(ATTR_PLEDGE_SUBJECT, pledgeSubjectDto);
                model.addAttribute(ATTR_WHERE_UPDATE_MONITORING, whereUpdateMonitoring);

            }else if(whereUpdateMonitoring.equals("pledgor")){

                Client client = clientService.getClientById(pledgorId
                        .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK)))
                        .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK));

                ClientDto clientDto = dtoFactory.getClientDto(client);

                model.addAttribute(ATTR_CLIENT, clientDto);
                model.addAttribute(ATTR_WHERE_UPDATE_MONITORING, whereUpdateMonitoring);

            }else {
                throw new IllegalArgumentException(MSG_WRONG_LINK);
            }
            return PAGE_CARD;
        }


        Monitoring monitoring = dtoFactory.getMonitoringEntity(monitoringDto);
        Set<ConstraintViolation<Monitoring>> violations =  validatorEntity.validateEntity(monitoring);
        if(!violations.isEmpty())
            throw new IllegalArgumentException(validatorEntity.getErrorMessage());


        if(whereUpdateMonitoring.equals("pa")){

            PledgeAgreement pledgeAgreement = pledgeAgreementService.getPledgeAgreementById(pledgeAgreementId
                    .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK)))
                    .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK));

            PledgeAgreementDto pledgeAgreementDto = dtoFactory.getPledgeAgreementDto(pledgeAgreement);
            List<Monitoring> monitoringList = monitoringService.insertMonitoringInPledgeAgreement(pledgeAgreement, monitoring);


            model.addAttribute(ATTR_WHERE_UPDATE_MONITORING, "responseSuccessPledgeAgreement");
            model.addAttribute(ATTR_COUNT_MONITORING , monitoringList.size());
            model.addAttribute(ATTR_PLEDGE_AGREEMENT, pledgeAgreementDto);
            return PAGE_CARD;

        }else if(whereUpdateMonitoring.equals("ps")){

            PledgeSubject pledgeSubject = pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId
                    .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK)))
                    .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK));

            monitoring.setPledgeSubject(pledgeSubject);
            monitoringService.insertMonitoringInPledgeSubject(monitoring);

            return monitoringPage(pledgeSubject.getPledgeSubjectId(), model);

        }else if(whereUpdateMonitoring.equals("pledgor")){
            Client client = clientService.getClientById(pledgorId
                    .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK)))
                    .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK));

            ClientDto clientDto = dtoFactory.getClientDto(client);

            List<Monitoring> monitoringList = monitoringService.insertMonitoringInPledgor(client, monitoring);

            model.addAttribute(ATTR_COUNT_MONITORING , monitoringList.size());
            model.addAttribute(ATTR_WHERE_UPDATE_MONITORING, "responseSuccessClient");
            model.addAttribute(ATTR_CLIENT, clientDto);

            return PAGE_CARD;

        }else{
            throw new IllegalArgumentException(MSG_WRONG_LINK);
        }
    }
}
