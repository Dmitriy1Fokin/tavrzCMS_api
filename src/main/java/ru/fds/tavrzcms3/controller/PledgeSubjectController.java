package ru.fds.tavrzcms3.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.fds.tavrzcms3.annotation.LogModificationDB;
import ru.fds.tavrzcms3.dictionary.TypeOfCollateral;
import ru.fds.tavrzcms3.domain.CostHistory;
import ru.fds.tavrzcms3.domain.Monitoring;
import ru.fds.tavrzcms3.domain.PledgeAgreement;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.dto.CostHistoryDto;
import ru.fds.tavrzcms3.dto.DtoFactory;
import ru.fds.tavrzcms3.dto.MonitoringDto;
import ru.fds.tavrzcms3.dto.PledgeSubjectDto;
import ru.fds.tavrzcms3.service.FilesService;
import ru.fds.tavrzcms3.service.PledgeAgreementService;
import ru.fds.tavrzcms3.service.PledgeSubjectService;
import ru.fds.tavrzcms3.validate.ValidatorEntity;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/pledge_subject")
public class PledgeSubjectController {

    private final PledgeSubjectService pledgeSubjectService;
    private final PledgeAgreementService pledgeAgreementService;
    private final FilesService filesService;
    private final DtoFactory dtoFactory;
    private final ValidatorEntity validatorEntity;

    private static final String PAGE_CARD_NEW = "pledge_subject/card_new";

    private static final String ATTR_PLEDGE_SUBJECT = "pledgeSubjectDto";
    private static final String ATTR_COST_HISTORY = "costHistoryDto";
    private static final String ATTR_MONITORING = "monitoringDto";

    public PledgeSubjectController(PledgeSubjectService pledgeSubjectService,
                                   PledgeAgreementService pledgeAgreementService,
                                   FilesService filesService,
                                   DtoFactory dtoFactory,
                                   ValidatorEntity validatorEntity) {
        this.pledgeSubjectService = pledgeSubjectService;
        this.pledgeAgreementService = pledgeAgreementService;
        this.filesService = filesService;
        this.dtoFactory = dtoFactory;
        this.validatorEntity = validatorEntity;
    }

    @GetMapping("/{id}")
    public PledgeSubjectDto getPledgeSubject(@PathVariable Long id){
        Optional<PledgeSubject> pledgeSubject = pledgeSubjectService.getPledgeSubjectById(id);
        return pledgeSubject.map(dtoFactory::getPledgeSubjectDto)
                .orElseThrow(()-> new NullPointerException("PledgeSubject not found"));
    }

    @GetMapping("/pledge_agreement")
    public List<PledgeSubjectDto> getPledgeSubjectByPledgeAgreement(@RequestParam("pledgeAgreementId") Long pledgeAgreementId){
        return dtoFactory.getPledgeSubjectsDto(pledgeSubjectService.getPledgeSubjectsForPledgeAgreement(pledgeAgreementId));
    }

    @GetMapping("/search_by_name")
    public List<PledgeSubjectDto> getPledgeSubjectsByName(@RequestParam("namePS") @NotBlank String namePS){
        return dtoFactory.getPledgeSubjectsDto(pledgeSubjectService.getPledgeSubjectByName(namePS));
    }

    @GetMapping("/search_by_cadastral_num")
    public List<PledgeSubjectDto> getPledgeSubjectsByCadastralNum(@RequestParam("cadastralNum") @NotBlank String cadastralNum){
        return dtoFactory.getPledgeSubjectsDto(pledgeSubjectService.getPledgeSubjectByCadastralNum(cadastralNum));
    }

    @PutMapping("/update")
    public PledgeSubjectDto updatePledgeSubject(@Valid @RequestBody PledgeSubjectDto pledgeSubjectDto){
        PledgeSubject pledgeSubject = dtoFactory.getPledgeSubjectEntity(pledgeSubjectDto);

        Set<ConstraintViolation<PledgeSubject>> violations =  validatorEntity.validateEntity(pledgeSubject);
        if(!violations.isEmpty())
            throw new ConstraintViolationException(violations);

        pledgeSubject = pledgeSubjectService.updatePledgeSubject(pledgeSubject);

        return dtoFactory.getPledgeSubjectDto(pledgeSubject);
    }

    @PostMapping("/insert_from_file/auto")
    public List<PledgeSubjectDto> insertPledgeSubjectAutoFromFile(@RequestParam("file") MultipartFile file) throws IOException {
        File uploadFile = filesService.uploadFile(file, "pledge_subject_new_auto");
        List<PledgeSubject> pledgeSubjectList = pledgeSubjectService
                .getNewPledgeSubjectsFromFile(uploadFile, TypeOfCollateral.AUTO);
        
        return getPersistentPledgeSubjectsDto(pledgeSubjectList);
    }

    @PostMapping("/insert_from_file/equipment")
    public List<PledgeSubjectDto> insertPledgeSubjectEquipmentFromFile(@RequestParam("file") MultipartFile file) throws IOException {
        File uploadFile = filesService.uploadFile(file, "pledge_subject_new_equipment");
        List<PledgeSubject> pledgeSubjectList = pledgeSubjectService
                .getNewPledgeSubjectsFromFile(uploadFile, TypeOfCollateral.EQUIPMENT);

        return getPersistentPledgeSubjectsDto(pledgeSubjectList);
    }

    @PostMapping("/insert_from_file/building")
    public List<PledgeSubjectDto> insertPledgeSubjectBuildingFromFile(@RequestParam("file") MultipartFile file) throws IOException {
        File uploadFile = filesService.uploadFile(file, "pledge_subject_new_building");
        List<PledgeSubject> pledgeSubjectList = pledgeSubjectService
                .getNewPledgeSubjectsFromFile(uploadFile, TypeOfCollateral.BUILDING);

        return getPersistentPledgeSubjectsDto(pledgeSubjectList);
    }

    @PostMapping("/insert_from_file/land_lease")
    public List<PledgeSubjectDto> insertPledgeSubjectLandLeaseFromFile(@RequestParam("file") MultipartFile file) throws IOException {
        File uploadFile = filesService.uploadFile(file, "pledge_subject_new_land_lease");
        List<PledgeSubject> pledgeSubjectList = pledgeSubjectService
                .getNewPledgeSubjectsFromFile(uploadFile, TypeOfCollateral.LAND_LEASE);

        return getPersistentPledgeSubjectsDto(pledgeSubjectList);
    }

    @PostMapping("/insert_from_file/land_ownership")
    public List<PledgeSubjectDto> insertPledgeSubjectLandOwnershipFromFile(@RequestParam("file") MultipartFile file) throws IOException {
        File uploadFile = filesService.uploadFile(file, "pledge_subject_new_land_ownership");
        List<PledgeSubject> pledgeSubjectList = pledgeSubjectService
                .getNewPledgeSubjectsFromFile(uploadFile, TypeOfCollateral.LAND_OWNERSHIP);

        return getPersistentPledgeSubjectsDto(pledgeSubjectList);
    }

    @PostMapping("/insert_from_file/premise")
    public List<PledgeSubjectDto> insertPledgeSubjectPremiseFromFile(@RequestParam("file") MultipartFile file) throws IOException {
        File uploadFile = filesService.uploadFile(file, "pledge_subject_new_premise");
        List<PledgeSubject> pledgeSubjectList = pledgeSubjectService
                .getNewPledgeSubjectsFromFile(uploadFile, TypeOfCollateral.PREMISE);

        return getPersistentPledgeSubjectsDto(pledgeSubjectList);
    }

    @PostMapping("/insert_from_file/securities")
    public List<PledgeSubjectDto> insertPledgeSubjectSecuritiesFromFile(@RequestParam("file") MultipartFile file) throws IOException {
        File uploadFile = filesService.uploadFile(file, "pledge_subject_new_securities");
        List<PledgeSubject> pledgeSubjectList = pledgeSubjectService
                .getNewPledgeSubjectsFromFile(uploadFile, TypeOfCollateral.SECURITIES);

        return getPersistentPledgeSubjectsDto(pledgeSubjectList);
    }

    @PostMapping("/insert_from_file/tbo")
    public List<PledgeSubjectDto> insertPledgeSubjectTboFromFile(@RequestParam("file") MultipartFile file) throws IOException {
        File uploadFile = filesService.uploadFile(file, "pledge_subject_new_tbo");
        List<PledgeSubject> pledgeSubjectList = pledgeSubjectService
                .getNewPledgeSubjectsFromFile(uploadFile, TypeOfCollateral.TBO);

        return getPersistentPledgeSubjectsDto(pledgeSubjectList);
    }

    @PostMapping("/insert_from_file/vessel")
    public List<PledgeSubjectDto> insertPledgeSubjectVesselFromFile(@RequestParam("file") MultipartFile file) throws IOException {
        File uploadFile = filesService.uploadFile(file, "pledge_subject_new_vessel");
        List<PledgeSubject> pledgeSubjectList = pledgeSubjectService
                .getNewPledgeSubjectsFromFile(uploadFile, TypeOfCollateral.VESSEL);

        return getPersistentPledgeSubjectsDto(pledgeSubjectList);
    }
    
    @PutMapping("/update_from_file")
    public List<PledgeSubjectDto> updatePledgeSubjectFromFile(@RequestParam("file") MultipartFile file) throws IOException {
        File uploadFile = filesService.uploadFile(file, "pledge_subject_update");
        List<PledgeSubject> pledgeSubjectList = pledgeSubjectService
                .getCurrentPledgeSubjectsFromFile(uploadFile);

        return getPersistentPledgeSubjectsDto(pledgeSubjectList);
    }

    private List<PledgeSubjectDto> getPersistentPledgeSubjectsDto(List<PledgeSubject> pledgeSubjectList) {
        for(int i = 0; i < pledgeSubjectList.size(); i++){
            Set<ConstraintViolation<PledgeSubject>> violations =  validatorEntity.validateEntity(pledgeSubjectList.get(i));
            if(!violations.isEmpty())
                throw new ConstraintViolationException("object " + (i+1), violations);
        }

        pledgeSubjectList = pledgeSubjectService.insertPledgeSubjects(pledgeSubjectList);

        return dtoFactory.getPledgeSubjectsDto(pledgeSubjectList);
    }

    @PutMapping("withdraw_from_deposit_pledgeSubject")
    public List<PledgeSubjectDto> withdrawFromDepositPledgeSubject(@RequestParam("pledgeSubjectId") long pledgeSubjectId,
                                                                   @RequestParam("pledgeAgreementId") long pledgeAgreementId){
        PledgeAgreement pledgeAgreement = pledgeAgreementService.getPledgeAgreementById(pledgeAgreementId)
                .orElseThrow(() -> new NullPointerException("Pledge agreement not found"));

        List<PledgeSubject> pledgeSubjectList = pledgeSubjectService.getPledgeSubjectsForPledgeAgreement(pledgeAgreementId);

        Optional<PledgeSubject> pledgeSubjectToRemove = pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId);
        if(pledgeSubjectToRemove.isPresent()){
            pledgeSubjectList.remove(pledgeSubjectToRemove.get());
            pledgeAgreement.setPledgeSubjects(pledgeSubjectList);
            pledgeAgreementService.updateInsertPledgeAgreement(pledgeAgreement);
        }else
            throw new NullPointerException("Pledge subject not found");

        return dtoFactory.getPledgeSubjectsDto(pledgeSubjectList);
    }

    @PutMapping("/insert/current_in_pledge_agreement")
    public List<PledgeSubjectDto> insertCurrentPledgeSubjectInPledgeAgreement(@RequestParam("pledgeSubjectsIdArray") long[] pledgeSubjectsIdArray,
                                                                              @RequestParam("pledgeAgreementId") long pledgeAgreementId){
        PledgeAgreement pledgeAgreement = pledgeAgreementService.getPledgeAgreementById(pledgeAgreementId)
                .orElseThrow(() -> new NullPointerException("Pledge agreement not found"));

        List<PledgeSubject> pledgeSubjectList = pledgeSubjectService.getPledgeSubjectsForPledgeAgreement(pledgeAgreementId);
        for (long i : pledgeSubjectsIdArray) {
            pledgeSubjectList.add(pledgeSubjectService.getPledgeSubjectById(i)
                    .orElseThrow(() -> new NullPointerException("Pledge subject not found")));
        }

        pledgeAgreement.setPledgeSubjects(pledgeSubjectList);
        pledgeAgreementService.updateInsertPledgeAgreement(pledgeAgreement);

        return dtoFactory.getPledgeSubjectsDto(pledgeSubjectList);
    }






































    @LogModificationDB
    @PostMapping("/insert_pledge_subject")
    public String insertNewPledgeSubject(@AuthenticationPrincipal User user,
                                         @Valid PledgeSubjectDto pledgeSubjectDto,
                                         BindingResult bindingResult,
                                         @Valid CostHistoryDto costHistoryDto,
                                         BindingResult bindingResultCostHistory,
                                         @Valid MonitoringDto monitoringDto,
                                         BindingResult bindingResultMonitoring,
                                         Model model){

        if(bindingResult.hasErrors()){
            model.addAttribute(ATTR_COST_HISTORY, costHistoryDto);
            model.addAttribute(ATTR_MONITORING, monitoringDto);
            return PAGE_CARD_NEW;

        }else if(bindingResultCostHistory.hasErrors()){
            model.addAttribute(ATTR_PLEDGE_SUBJECT, pledgeSubjectDto);
            model.addAttribute(ATTR_MONITORING, monitoringDto);

            return PAGE_CARD_NEW;

        }else if(bindingResultMonitoring.hasErrors()){
            model.addAttribute(ATTR_PLEDGE_SUBJECT, pledgeSubjectDto);
            model.addAttribute(ATTR_COST_HISTORY, costHistoryDto);

            return PAGE_CARD_NEW;
        }

        PledgeSubject pledgeSubject = dtoFactory.getPledgeSubjectEntity(pledgeSubjectDto);
        Set<ConstraintViolation<PledgeSubject>> violations =  validatorEntity.validateEntity(pledgeSubject);
        if(!violations.isEmpty())
            throw new IllegalArgumentException(validatorEntity.getErrorMessage());

        CostHistory costHistory = dtoFactory.getCostHistoryEntity(costHistoryDto);
        Set<ConstraintViolation<CostHistory>> violationsCostHistory =  validatorEntity.validateEntity(costHistory);
        if(!violationsCostHistory.isEmpty())
            throw new IllegalArgumentException(validatorEntity.getErrorMessage());

        Monitoring monitoring = dtoFactory.getMonitoringEntity(monitoringDto);
        Set<ConstraintViolation<Monitoring>> violationsMonitoring =  validatorEntity.validateEntity(monitoring);
        if(!violationsMonitoring.isEmpty())
            throw new IllegalArgumentException(validatorEntity.getErrorMessage());

        pledgeSubjectService.insertPledgeSubject(pledgeSubject, costHistory, monitoring);

        return null;
    }
}
