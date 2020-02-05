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
import ru.fds.tavrzcms3.wrapper.PledgeAgreementDtoWrapper;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pledge_agreement")
public class PledgeAgreementController {

    private final PledgeAgreementService pledgeAgreementService;
    private final FilesService filesService;
    private final DtoFactory dtoFactory;

    public PledgeAgreementController(PledgeAgreementService pledgeAgreementService,
                                     FilesService filesService,
                                     DtoFactory dtoFactory) {
        this.pledgeAgreementService = pledgeAgreementService;
        this.filesService = filesService;
        this.dtoFactory = dtoFactory;
    }

    @GetMapping("/{pledgeAgreementId}")
    public PledgeAgreementDto getPledgeAgreement(@PathVariable("pledgeAgreementId") Long pledgeAgreementId){
        return pledgeAgreementService.getPledgeAgreementById(pledgeAgreementId).map(dtoFactory::getPledgeAgreementDto)
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

    @GetMapping("/search")
    public List<PledgeAgreementDto> getPledgeAgreementBySearchCriteria(@RequestParam Map<String, String> reqParam) throws ReflectiveOperationException {
        List<PledgeAgreement> pledgeAgreementList = pledgeAgreementService.getPledgeAgreementFromSearch(reqParam);
        return dtoFactory.getPledgeAgreementsDto(pledgeAgreementList);
    }

    @PostMapping("/insert")
    public PledgeAgreementDto insertPledgeAgreement(@Valid @RequestBody PledgeAgreementDtoWrapper pledgeAgreementDtoWrapper){
        PledgeAgreement pledgeAgreement = dtoFactory.getPledgeAgreementEntity(pledgeAgreementDtoWrapper.getPledgeAgreementDto());
        List<Long> loanAgreementsIds = pledgeAgreementDtoWrapper.getLoanAgreementsIds();

        pledgeAgreement = pledgeAgreementService
                .insertPledgeAgreement(pledgeAgreement, loanAgreementsIds);

        return dtoFactory.getPledgeAgreementDto(pledgeAgreement);
    }

    @PutMapping("/update")
    public PledgeAgreementDto updatePledgeAgreement(@Valid @RequestBody PledgeAgreementDtoWrapper pledgeAgreementDtoWrapper){
        PledgeAgreement pledgeAgreement = dtoFactory.getPledgeAgreementEntity(pledgeAgreementDtoWrapper.getPledgeAgreementDto());
        List<Long> loanAgreementsIds = pledgeAgreementDtoWrapper.getLoanAgreementsIds();

        pledgeAgreement = pledgeAgreementService
                .updatePledgeAgreement(pledgeAgreement, loanAgreementsIds);

        return dtoFactory.getPledgeAgreementDto(pledgeAgreement);
    }

    @PostMapping("/insert/file")
    public List<PledgeAgreementDto> insertPledgeAgreementFromFile(@RequestParam("file") MultipartFile file) throws IOException {
        File uploadFile = filesService.uploadFile(file, "pledge_agreement_new");
        List<PledgeAgreement> pledgeAgreementList = pledgeAgreementService.getNewPledgeAgreementsFromFile(uploadFile);

        return dtoFactory.getPledgeAgreementsDto(pledgeAgreementList);
    }

    @PutMapping("/update/file")
    public List<PledgeAgreementDto> updatePledgeAgreementFromFile(@RequestParam("file") MultipartFile file) throws IOException {
        File uploadFile = filesService.uploadFile(file, "pledge_agreement_update");
        List<PledgeAgreement> pledgeAgreementList = pledgeAgreementService.getCurrentPledgeAgreementsFromFile(uploadFile);

        return dtoFactory.getPledgeAgreementsDto(pledgeAgreementList);
    }

    @PutMapping("update/withdraw_pledge_subject")
    public PledgeAgreementDto withdrawPledgeSubjectFromPledgeAgreement(@RequestParam("pledgeSubjectId") Long pledgeSubjectId,
                                                                       @RequestParam("pledgeAgreementId") Long pledgeAgreementId){
        return dtoFactory.getPledgeAgreementDto(pledgeAgreementService
                .withdrawPledgeSubjectFromPledgeAgreement(pledgeAgreementId, pledgeSubjectId));
    }

    @PutMapping("/update/insert_current_pledge_subject")
    public PledgeAgreementDto insertCurrentPledgeSubjectInPledgeAgreement(@RequestParam("pledgeSubjectsIdArray") List<Long> pledgeSubjectsIdArray,
                                                                          @RequestParam("pledgeAgreementId") Long pledgeAgreementId){
        return dtoFactory.getPledgeAgreementDto(pledgeAgreementService.
                insertCurrentPledgeSubjectsInPledgeAgreement(pledgeAgreementId, pledgeSubjectsIdArray));
    }
}
