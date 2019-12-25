package ru.fds.tavrzcms3.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
import ru.fds.tavrzcms3.service.LoanAgreementService;
import ru.fds.tavrzcms3.service.PledgeAgreementService;
import ru.fds.tavrzcms3.service.PledgeSubjectService;
import ru.fds.tavrzcms3.validate.ValidatorEntity;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/pledge_subject")
public class PledgeSubjectController {

    private final PledgeSubjectService pledgeSubjectService;
    private final PledgeAgreementService pledgeAgreementService;
    private final LoanAgreementService loanAgreementService;

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
                                   DtoFactory dtoFactory,
                                   ValidatorEntity validatorEntity) {
        this.pledgeSubjectService = pledgeSubjectService;
        this.pledgeAgreementService = pledgeAgreementService;
        this.loanAgreementService = loanAgreementService;
        this.dtoFactory = dtoFactory;
        this.validatorEntity = validatorEntity;
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
