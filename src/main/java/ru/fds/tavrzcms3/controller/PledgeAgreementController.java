package ru.fds.tavrzcms3.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.fds.tavrzcms3.dictionary.TypeOfPledgeAgreement;
import ru.fds.tavrzcms3.domain.PledgeAgreement;
import ru.fds.tavrzcms3.dto.DtoFactory;
import ru.fds.tavrzcms3.dto.PledgeAgreementDto;
import ru.fds.tavrzcms3.service.FilesService;
import ru.fds.tavrzcms3.service.PledgeAgreementService;
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
@RequestMapping("/pledge_agreement")
public class PledgeAgreementController {

    private final PledgeAgreementService pledgeAgreementService;
    private final FilesService filesService;
    private final DtoFactory dtoFactory;

    private final ValidatorEntity validatorEntity;


    public PledgeAgreementController(PledgeAgreementService pledgeAgreementService,
                                     FilesService filesService,
                                     DtoFactory dtoFactory,
                                     ValidatorEntity validatorEntity) {
        this.pledgeAgreementService = pledgeAgreementService;
        this.filesService = filesService;
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

    @GetMapping("/pledge_subject")
    public List<PledgeAgreementDto> getPledgeAgreementsByPledgeSubjects(@RequestParam("pledgeSubjectId") Long pledgeSubjectId){
        return dtoFactory.getPledgeAgreementsDto(pledgeAgreementService.getAllPledgeAgreementByPLedgeSubject(pledgeSubjectId));
    }

    @PostMapping("/insert")
    public PledgeAgreementDto insertPledgeAgreement(@Valid @RequestBody PledgeAgreementDto pledgeAgreementDto){
        PledgeAgreement pledgeAgreement = dtoFactory.getPledgeAgreementEntity(pledgeAgreementDto);

        Set<ConstraintViolation<PledgeAgreement>> violations =  validatorEntity.validateEntity(pledgeAgreement);
        if(!violations.isEmpty())
            throw new ConstraintViolationException(violations);

        pledgeAgreement = pledgeAgreementService.updateInsertPledgeAgreement(pledgeAgreement);

        return dtoFactory.getPledgeAgreementDto(pledgeAgreement);
    }

    @PutMapping("/update")
    public PledgeAgreementDto updatePledgeAgreement(@Valid @RequestBody PledgeAgreementDto pledgeAgreementDto){
        return insertPledgeAgreement(pledgeAgreementDto);
    }

    @PostMapping("/insert_from_file")
    public List<PledgeAgreementDto> insertPledgeAgreementFromFile(@RequestParam("file") MultipartFile file) throws IOException {
        File uploadFile = filesService.uploadFile(file, "pledge_agreement_new");
        List<PledgeAgreement> pledgeAgreementList = pledgeAgreementService.getNewPledgeAgreementsFromFile(uploadFile);

        return getPersistentPledgeAgreementsDto(pledgeAgreementList);
    }

    @PutMapping("/update_from_file")
    public List<PledgeAgreementDto> updatePledgeAgreementFromFile(@RequestParam("file") MultipartFile file) throws IOException {
        File uploadFile = filesService.uploadFile(file, "pledge_agreement_update");
        List<PledgeAgreement> pledgeAgreementList = pledgeAgreementService.getCurrentPledgeAgreementsFromFile(uploadFile);

        return getPersistentPledgeAgreementsDto(pledgeAgreementList);
    }

    private List<PledgeAgreementDto> getPersistentPledgeAgreementsDto(List<PledgeAgreement> pledgeAgreementList) {
        for(int i = 0; i < pledgeAgreementList.size(); i++){
            Set<ConstraintViolation<PledgeAgreement>> violations =  validatorEntity.validateEntity(pledgeAgreementList.get(i));
            if(!violations.isEmpty())
                throw new ConstraintViolationException("object " + (i+1), violations);
        }

        pledgeAgreementList = pledgeAgreementService.updateInsertPledgeAgreements(pledgeAgreementList);

        return dtoFactory.getPledgeAgreementsDto(pledgeAgreementList);
    }
}
