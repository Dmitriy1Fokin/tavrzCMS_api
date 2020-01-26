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
import ru.fds.tavrzcms3.dictionary.TypeOfCollateral;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.dto.CostHistoryDto;
import ru.fds.tavrzcms3.dto.DtoFactory;
import ru.fds.tavrzcms3.dto.MonitoringDto;
import ru.fds.tavrzcms3.dto.PledgeSubjectDto;
import ru.fds.tavrzcms3.exception.NotFoundException;
import ru.fds.tavrzcms3.service.FilesService;
import ru.fds.tavrzcms3.service.PledgeSubjectService;
import ru.fds.tavrzcms3.wrapper.PledgeSubjectDtoNewWrapper;
import ru.fds.tavrzcms3.wrapper.PledgeSubjectUpdateDtoWrapper;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pledge_subject")
public class PledgeSubjectController {

    private final PledgeSubjectService pledgeSubjectService;
    private final FilesService filesService;
    private final DtoFactory dtoFactory;

    public PledgeSubjectController(PledgeSubjectService pledgeSubjectService,
                                   FilesService filesService,
                                   DtoFactory dtoFactory) {
        this.pledgeSubjectService = pledgeSubjectService;
        this.filesService = filesService;
        this.dtoFactory = dtoFactory;
    }

    @GetMapping("/{pledgeSubjectId}")
    public PledgeSubjectDto getPledgeSubject(@PathVariable("pledgeSubjectId") Long pledgeSubjectId){
        return pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId).map(dtoFactory::getPledgeSubjectDto)
                .orElseThrow(()-> new NotFoundException("Pledge subject not found"));
    }

    @GetMapping("/pledge_agreement")
    public List<PledgeSubjectDto> getPledgeSubjectByPledgeAgreement(@RequestParam("pledgeAgreementId") Long pledgeAgreementId){
        return dtoFactory.getPledgeSubjectsDto(pledgeSubjectService.getPledgeSubjectsByPledgeAgreement(pledgeAgreementId));
    }

    @GetMapping("/search_by_name")
    public List<PledgeSubjectDto> getPledgeSubjectsByName(@RequestParam("namePS") @NotBlank String namePS){
        return dtoFactory.getPledgeSubjectsDto(pledgeSubjectService.getPledgeSubjectByName(namePS));
    }

    @GetMapping("/search_by_cadastral_num")
    public List<PledgeSubjectDto> getPledgeSubjectsByCadastralNum(@RequestParam("cadastralNum") @NotBlank String cadastralNum){
        return dtoFactory.getPledgeSubjectsDto(pledgeSubjectService.getPledgeSubjectByCadastralNum(cadastralNum));
    }

    @GetMapping("/search")
    public List<PledgeSubjectDto> getPledgeSubjectBySearchCriteria(@RequestParam Map<String, String> reqParam) throws ReflectiveOperationException {
        List<PledgeSubject> pledgeSubjectList = pledgeSubjectService.getPledgeSubjectsFromSearch(reqParam);
        return dtoFactory.getPledgeSubjectsDto(pledgeSubjectList);
    }

    @PostMapping("/insert")
    public PledgeSubjectDto insertPledgeSubject(@Valid @RequestBody PledgeSubjectDtoNewWrapper pledgeSubjectDtoNewWrapper){
        PledgeSubjectDto pledgeSubjectDto = pledgeSubjectDtoNewWrapper.getPledgeSubjectDto();
        CostHistoryDto costHistoryDto = pledgeSubjectDtoNewWrapper.getCostHistoryDto();
        MonitoringDto monitoringDto = pledgeSubjectDtoNewWrapper.getMonitoringDto();
        List<Long> pledgeAgreementsIds = pledgeSubjectDtoNewWrapper.getPledgeAgreementsIds();

        PledgeSubject pledgeSubject = pledgeSubjectService
                .insertPledgeSubject(dtoFactory.getPledgeSubjectEntity(pledgeSubjectDto),
                        pledgeAgreementsIds,
                        dtoFactory.getCostHistoryEntity(costHistoryDto),
                        dtoFactory.getMonitoringEntity(monitoringDto));

        return dtoFactory.getPledgeSubjectDto(pledgeSubject);
    }

    @PutMapping("/update")
    public PledgeSubjectDto updatePledgeSubject(@Valid @RequestBody PledgeSubjectUpdateDtoWrapper pledgeSubjectUpdateDtoWrapper){
        PledgeSubject pledgeSubject = pledgeSubjectService
                .updatePledgeSubject(dtoFactory.
                        getPledgeSubjectEntity(pledgeSubjectUpdateDtoWrapper.getPledgeSubjectDto()),
                        pledgeSubjectUpdateDtoWrapper.getPledgeAgreementsIds());

        return dtoFactory.getPledgeSubjectDto(pledgeSubject);
    }

    @PostMapping("/insert_from_file/auto")
    public List<PledgeSubjectDto> insertPledgeSubjectAutoFromFile(@RequestParam("file") MultipartFile file) throws IOException {
        File uploadFile = filesService.uploadFile(file, "pledge_subject_new_auto");
        List<PledgeSubject> pledgeSubjectList = pledgeSubjectService
                .getNewPledgeSubjectsFromFile(uploadFile, TypeOfCollateral.AUTO);
        
        return dtoFactory.getPledgeSubjectsDto(pledgeSubjectList);
    }

    @PostMapping("/insert_from_file/equipment")
    public List<PledgeSubjectDto> insertPledgeSubjectEquipmentFromFile(@RequestParam("file") MultipartFile file) throws IOException {
        File uploadFile = filesService.uploadFile(file, "pledge_subject_new_equipment");
        List<PledgeSubject> pledgeSubjectList = pledgeSubjectService
                .getNewPledgeSubjectsFromFile(uploadFile, TypeOfCollateral.EQUIPMENT);

        return dtoFactory.getPledgeSubjectsDto(pledgeSubjectList);
    }

    @PostMapping("/insert_from_file/building")
    public List<PledgeSubjectDto> insertPledgeSubjectBuildingFromFile(@RequestParam("file") MultipartFile file) throws IOException {
        File uploadFile = filesService.uploadFile(file, "pledge_subject_new_building");
        List<PledgeSubject> pledgeSubjectList = pledgeSubjectService
                .getNewPledgeSubjectsFromFile(uploadFile, TypeOfCollateral.BUILDING);

        return dtoFactory.getPledgeSubjectsDto(pledgeSubjectList);
    }

    @PostMapping("/insert_from_file/land_lease")
    public List<PledgeSubjectDto> insertPledgeSubjectLandLeaseFromFile(@RequestParam("file") MultipartFile file) throws IOException {
        File uploadFile = filesService.uploadFile(file, "pledge_subject_new_land_lease");
        List<PledgeSubject> pledgeSubjectList = pledgeSubjectService
                .getNewPledgeSubjectsFromFile(uploadFile, TypeOfCollateral.LAND_LEASE);

        return dtoFactory.getPledgeSubjectsDto(pledgeSubjectList);
    }

    @PostMapping("/insert_from_file/land_ownership")
    public List<PledgeSubjectDto> insertPledgeSubjectLandOwnershipFromFile(@RequestParam("file") MultipartFile file) throws IOException {
        File uploadFile = filesService.uploadFile(file, "pledge_subject_new_land_ownership");
        List<PledgeSubject> pledgeSubjectList = pledgeSubjectService
                .getNewPledgeSubjectsFromFile(uploadFile, TypeOfCollateral.LAND_OWNERSHIP);

        return dtoFactory.getPledgeSubjectsDto(pledgeSubjectList);
    }

    @PostMapping("/insert_from_file/premise")
    public List<PledgeSubjectDto> insertPledgeSubjectPremiseFromFile(@RequestParam("file") MultipartFile file) throws IOException {
        File uploadFile = filesService.uploadFile(file, "pledge_subject_new_premise");
        List<PledgeSubject> pledgeSubjectList = pledgeSubjectService
                .getNewPledgeSubjectsFromFile(uploadFile, TypeOfCollateral.PREMISE);

        return dtoFactory.getPledgeSubjectsDto(pledgeSubjectList);
    }

    @PostMapping("/insert_from_file/securities")
    public List<PledgeSubjectDto> insertPledgeSubjectSecuritiesFromFile(@RequestParam("file") MultipartFile file) throws IOException {
        File uploadFile = filesService.uploadFile(file, "pledge_subject_new_securities");
        List<PledgeSubject> pledgeSubjectList = pledgeSubjectService
                .getNewPledgeSubjectsFromFile(uploadFile, TypeOfCollateral.SECURITIES);

        return dtoFactory.getPledgeSubjectsDto(pledgeSubjectList);
    }

    @PostMapping("/insert_from_file/tbo")
    public List<PledgeSubjectDto> insertPledgeSubjectTboFromFile(@RequestParam("file") MultipartFile file) throws IOException {
        File uploadFile = filesService.uploadFile(file, "pledge_subject_new_tbo");
        List<PledgeSubject> pledgeSubjectList = pledgeSubjectService
                .getNewPledgeSubjectsFromFile(uploadFile, TypeOfCollateral.TBO);

        return dtoFactory.getPledgeSubjectsDto(pledgeSubjectList);
    }

    @PostMapping("/insert_from_file/vessel")
    public List<PledgeSubjectDto> insertPledgeSubjectVesselFromFile(@RequestParam("file") MultipartFile file) throws IOException {
        File uploadFile = filesService.uploadFile(file, "pledge_subject_new_vessel");
        List<PledgeSubject> pledgeSubjectList = pledgeSubjectService
                .getNewPledgeSubjectsFromFile(uploadFile, TypeOfCollateral.VESSEL);

        return dtoFactory.getPledgeSubjectsDto(pledgeSubjectList);
    }
    
    @PutMapping("/update_from_file")
    public List<PledgeSubjectDto> updatePledgeSubjectFromFile(@RequestParam("file") MultipartFile file) throws IOException {
        File uploadFile = filesService.uploadFile(file, "pledge_subject_update");
        List<PledgeSubject> pledgeSubjectList = pledgeSubjectService
                .getCurrentPledgeSubjectsFromFile(uploadFile);

        return dtoFactory.getPledgeSubjectsDto(pledgeSubjectList);
    }

}
