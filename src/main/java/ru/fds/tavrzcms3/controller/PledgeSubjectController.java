package ru.fds.tavrzcms3.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
import ru.fds.tavrzcms3.dto.LoanAgreementDto;
import ru.fds.tavrzcms3.dto.MonitoringDto;
import ru.fds.tavrzcms3.dto.PledgeAgreementDto;
import ru.fds.tavrzcms3.dto.PledgeSubjectAutoDto;
import ru.fds.tavrzcms3.dto.PledgeSubjectBuildingDto;
import ru.fds.tavrzcms3.dto.PledgeSubjectDto;
import ru.fds.tavrzcms3.dto.PledgeSubjectEquipmentDto;
import ru.fds.tavrzcms3.dto.PledgeSubjectLandLeaseDto;
import ru.fds.tavrzcms3.dto.PledgeSubjectLandOwnershipDto;
import ru.fds.tavrzcms3.dto.PledgeSubjectRoomDto;
import ru.fds.tavrzcms3.dto.PledgeSubjectSecuritiesDto;
import ru.fds.tavrzcms3.dto.PledgeSubjectTboDto;
import ru.fds.tavrzcms3.dto.PledgeSubjectVesselDto;
import ru.fds.tavrzcms3.service.FilesService;
import ru.fds.tavrzcms3.service.LoanAgreementService;
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
    private final LoanAgreementService loanAgreementService;
    private final FilesService filesService;
    private final DtoFactory dtoFactory;
    private final ValidatorEntity validatorEntity;

    private static final String PAGE_DETAIL = "pledge_subject/detail";
    private static final String PAGE_CARD_UPDATE = "pledge_subject/card_update";
    private static final String PAGE_CARD_NEW = "pledge_subject/card_new";

    private static final String MSG_WRONG_LINK = "Неверная ссылка";

    private static final String ATTR_PLEDGE_AGREEMENT_DTO_LIST = "pledgeAgreementDtoList";
    private static final String ATTR_LOAN_AGREEMENT_DTO_LIST = "loanAgreementDtoList";
    private static final String ATTR_PLEDGE_SUBJECT = "pledgeSubjectDto";
    private static final String ATTR_COST_HISTORY = "costHistoryDto";
    private static final String ATTR_MONITORING = "monitoringDto";

    public PledgeSubjectController(PledgeSubjectService pledgeSubjectService,
                                   PledgeAgreementService pledgeAgreementService,
                                   LoanAgreementService loanAgreementService,
                                   FilesService filesService, 
                                   DtoFactory dtoFactory,
                                   ValidatorEntity validatorEntity) {
        this.pledgeSubjectService = pledgeSubjectService;
        this.pledgeAgreementService = pledgeAgreementService;
        this.loanAgreementService = loanAgreementService;
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

































    @GetMapping("/detail")
    public String pledgeSubjectDetailPage(@RequestParam("pledgeSubjectId") Long pledgeSubjectId,
                                          Model model){

        PledgeSubject pledgeSubject = pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId)
                .orElseThrow(() -> new IllegalArgumentException(MSG_WRONG_LINK));

        PledgeSubjectDto pledgeSubjectDto = dtoFactory.getPledgeSubjectDto(pledgeSubject);

        List<PledgeAgreement> pledgeAgreementList = pledgeAgreementService.getAllPledgeAgreementByPLedgeSubject(pledgeSubject);
        List<PledgeAgreementDto> pledgeAgreementDtoList = dtoFactory.getPledgeAgreementsDto(pledgeAgreementList);

        List<LoanAgreementDto> loanAgreementDtoList = dtoFactory
                .getLoanAgreementsDto(loanAgreementService.getAllLoanAgreementByPledgeAgreements(pledgeAgreementList));

        model.addAttribute(ATTR_PLEDGE_SUBJECT, pledgeSubjectDto);
        model.addAttribute(ATTR_PLEDGE_AGREEMENT_DTO_LIST, pledgeAgreementDtoList);
        model.addAttribute(ATTR_LOAN_AGREEMENT_DTO_LIST, loanAgreementDtoList);

        return PAGE_DETAIL;
    }

    @GetMapping("/card_update")
    public String pledgeSubjectCardUpdate(@RequestParam("pledgeSubjectId") Optional<Long> pledgeSubjectId,
                                          Model model){

        PledgeSubject pledgeSubject = pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId
                .orElseThrow(() -> new IllegalArgumentException(MSG_WRONG_LINK)))
                .orElseThrow(() -> new IllegalArgumentException(MSG_WRONG_LINK));

        PledgeSubjectDto pledgeSubjectDto = dtoFactory.getPledgeSubjectDto(pledgeSubject);

        model.addAttribute(ATTR_PLEDGE_SUBJECT, pledgeSubjectDto);

        return PAGE_CARD_UPDATE;
    }


    @LogModificationDB
    @PostMapping("/update_pledge_subject")
    public String updatePledgeSubject(@AuthenticationPrincipal User user,
                                      @Valid PledgeSubjectDto pledgeSubjectDto,
                                      BindingResult bindingResult,
                                      Model model){

        if(bindingResult.hasErrors()) {
            return PAGE_CARD_UPDATE;
        }

        PledgeSubject pledgeSubject = dtoFactory.getPledgeSubjectEntity(pledgeSubjectDto);

        Set<ConstraintViolation<PledgeSubject>> violations =  validatorEntity.validateEntity(pledgeSubject);
        if(!violations.isEmpty())
            throw new IllegalArgumentException(validatorEntity.getErrorMessage());

        pledgeSubject = pledgeSubjectService.updatePledgeSubject(pledgeSubject);

        return pledgeSubjectDetailPage(pledgeSubject.getPledgeSubjectId(), model);
    }

    @GetMapping("/card_new")
    public String pledgeSubjectCardNew(@RequestParam("typeOfCollateral") Optional<String> typeOfCollateral,
                                       @RequestParam("pledgeAgreementId") Optional<Long> pledgeAgreementId,
                                       Model model){

        if(!typeOfCollateral.isPresent() || !pledgeAgreementId.isPresent())
            throw new IllegalArgumentException(MSG_WRONG_LINK);

        PledgeSubjectDto pledgeSubjectDto = PledgeSubjectDto.builder()
                .typeOfCollateral(TypeOfCollateral.valueOf(typeOfCollateral.get()))
                .pledgeAgreementsId(pledgeAgreementId.get())
                .build();

        if(typeOfCollateral.get().equals(TypeOfCollateral.AUTO.name()))
            pledgeSubjectDto.setPledgeSubjectAutoDto(new PledgeSubjectAutoDto());
        else if (typeOfCollateral.get().equals(TypeOfCollateral.EQUIPMENT.name()))
            pledgeSubjectDto.setPledgeSubjectEquipmentDto(new PledgeSubjectEquipmentDto());
        else if (typeOfCollateral.get().equals(TypeOfCollateral.BUILDING.name()))
            pledgeSubjectDto.setPledgeSubjectBuildingDto(new PledgeSubjectBuildingDto());
        else if (typeOfCollateral.get().equals(TypeOfCollateral.LAND_LEASE.name()))
            pledgeSubjectDto.setPledgeSubjectLandLeaseDto(new PledgeSubjectLandLeaseDto());
        else if (typeOfCollateral.get().equals(TypeOfCollateral.LAND_OWNERSHIP.name()))
            pledgeSubjectDto.setPledgeSubjectLandOwnershipDto(new PledgeSubjectLandOwnershipDto());
        else if (typeOfCollateral.get().equals(TypeOfCollateral.PREMISE.name()))
            pledgeSubjectDto.setPledgeSubjectRoomDto(new PledgeSubjectRoomDto());
        else if (typeOfCollateral.get().equals(TypeOfCollateral.SECURITIES.name()))
            pledgeSubjectDto.setPledgeSubjectSecuritiesDto(new PledgeSubjectSecuritiesDto());
        else if (typeOfCollateral.get().equals(TypeOfCollateral.TBO.name()))
            pledgeSubjectDto.setPledgeSubjectTboDto(new PledgeSubjectTboDto());
        else if (typeOfCollateral.get().equals(TypeOfCollateral.VESSEL.name()))
            pledgeSubjectDto.setPledgeSubjectVesselDto(new PledgeSubjectVesselDto());

        CostHistoryDto costHistoryDto = new CostHistoryDto();
        MonitoringDto monitoringDto = new MonitoringDto();

        model.addAttribute(ATTR_PLEDGE_SUBJECT, pledgeSubjectDto);
        model.addAttribute(ATTR_COST_HISTORY, costHistoryDto);
        model.addAttribute(ATTR_MONITORING, monitoringDto);

        return PAGE_CARD_NEW;
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

        return pledgeSubjectDetailPage(pledgeSubject.getPledgeSubjectId(), model);
    }
}
