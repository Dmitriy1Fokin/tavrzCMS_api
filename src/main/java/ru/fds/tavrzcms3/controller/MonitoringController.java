package ru.fds.tavrzcms3.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.Monitoring;
import ru.fds.tavrzcms3.domain.PledgeAgreement;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.dto.DtoFactory;
import ru.fds.tavrzcms3.dto.MonitoringDto;
import ru.fds.tavrzcms3.service.ClientService;
import ru.fds.tavrzcms3.service.FilesService;
import ru.fds.tavrzcms3.service.MonitoringService;
import ru.fds.tavrzcms3.service.PledgeAgreementService;
import ru.fds.tavrzcms3.service.PledgeSubjectService;
import ru.fds.tavrzcms3.validate.ValidatorEntity;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/monitoring")
public class MonitoringController {

    private final PledgeSubjectService pledgeSubjectService;
    private final MonitoringService monitoringService;
    private final PledgeAgreementService pledgeAgreementService;
    private final ClientService clientService;
    private final FilesService filesService;
    private final DtoFactory dtoFactory;
    private ValidatorEntity validatorEntity;


    public MonitoringController(PledgeSubjectService pledgeSubjectService,
                                MonitoringService monitoringService,
                                PledgeAgreementService pledgeAgreementService,
                                ClientService clientService,
                                FilesService filesService, DtoFactory dtoFactory,
                                ValidatorEntity validatorEntity) {
        this.pledgeSubjectService = pledgeSubjectService;
        this.monitoringService = monitoringService;
        this.pledgeAgreementService = pledgeAgreementService;
        this.clientService = clientService;
        this.filesService = filesService;
        this.dtoFactory = dtoFactory;
        this.validatorEntity = validatorEntity;
    }

    @GetMapping("/pledge_subject")
    public List<MonitoringDto> getMonitoringsByPledgeSubject(@RequestParam("pledgeSubjectId") Long pledgeSubjectId){
        return dtoFactory.getMonitoringsDto(monitoringService.getMonitoringByPledgeSubject(pledgeSubjectId));
    }

    @PostMapping("insert")
    public MonitoringDto insertMonitoring(@Valid @RequestBody MonitoringDto monitoringDto,
                                          @RequestParam("pledgeSubjectId") Long pledgeSubjectId){

        Monitoring monitoring = dtoFactory.getMonitoringEntity(monitoringDto);

        Optional<PledgeSubject> pledgeSubject = pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId);

        monitoring.setPledgeSubject(pledgeSubject
                .orElseThrow(() -> new NullPointerException("Pledge subject not found")));

        monitoring = monitoringService.insertMonitoringInPledgeSubject(monitoring);

        return dtoFactory.getMonitoringDto(monitoring);
    }

    @PostMapping("/insert/pledge_agreement")
    public List<MonitoringDto> insertMonitoringByPledgeAgreement(@Valid @RequestBody MonitoringDto monitoringDto,
                                                                 @RequestParam("pledgeAgreementId") Long pledgeAgreementId){

        Monitoring monitoring = dtoFactory.getMonitoringEntity(monitoringDto);

        Optional<PledgeAgreement> pledgeAgreement = pledgeAgreementService.getPledgeAgreementById(pledgeAgreementId);

        List<Monitoring> monitoringList = monitoringService
                .insertMonitoringInPledgeAgreement(pledgeAgreement
                        .orElseThrow(() -> new NullPointerException("Pledge agreement not found")), monitoring);

        return dtoFactory.getMonitoringsDto(monitoringList);
    }

    @PostMapping("insert/client")
    public List<MonitoringDto> insertMonitoringByClient(@Valid @RequestBody MonitoringDto monitoringDto,
                                                        @RequestParam("clientId") Long clienttId){
        Monitoring monitoring = dtoFactory.getMonitoringEntity(monitoringDto);

        Optional<Client> client = clientService.getClientById(clienttId);

        List<Monitoring> monitoringList = monitoringService.insertMonitoringInPledgor(client
                .orElseThrow(() -> new NullPointerException("Client not found")), monitoring);

        return dtoFactory.getMonitoringsDto(monitoringList);
    }

    @PostMapping("/insert_from_file")
    public List<MonitoringDto> insertMonitoringFromFile(@RequestParam("file") MultipartFile file) throws IOException {
        File uploadFile = filesService.uploadFile(file, "monitoring_new");
        List<Monitoring> monitoringList = monitoringService.getNewMonitoringsFromFile(uploadFile);

        return getPersistentMonitoringDto(monitoringList);
    }

    @PutMapping("/update_from_file")
    public List<MonitoringDto> updateMonitoringFromFile(@RequestParam("file") MultipartFile file) throws IOException {
        File uploadFile = filesService.uploadFile(file, "monitoring_update");
        List<Monitoring> monitoringList = monitoringService.getCurrentMonitoringsFromFile(uploadFile);

        return getPersistentMonitoringDto(monitoringList);
    }

    private List<MonitoringDto> getPersistentMonitoringDto(List<Monitoring> monitoringList) {
        for(int i = 0; i < monitoringList.size(); i++){
            Set<ConstraintViolation<Monitoring>> violations =  validatorEntity.validateEntity(monitoringList.get(i));
            if(!violations.isEmpty())
                throw new ConstraintViolationException("object " + (i+1), violations);
        }

        monitoringList = monitoringService.insertMonitoringsInPledgeSubject(monitoringList);

        return dtoFactory.getMonitoringsDto(monitoringList);
    }
}
