package ru.fds.tavrzcms3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.fds.tavrzcms3.dictionary.TypeOfCollateral;
import ru.fds.tavrzcms3.domain.*;
import ru.fds.tavrzcms3.service.LandCategoryService;
import ru.fds.tavrzcms3.service.MarketSegmentService;
import ru.fds.tavrzcms3.service.PledgeAgreementService;
import ru.fds.tavrzcms3.service.PledgeSubjectService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/pledge_subject")
public class PledgeSubjectController {

    private final PledgeSubjectService pledgeSubjectService;
    private final LandCategoryService landCategoryService;
    private final MarketSegmentService marketSegmentService;
    private final PledgeAgreementService pledgeAgreementService;

    private static final String PAGE_DETAIL = "pledge_subject/detail";
    private static final String PAGE_CARD_UPDATE = "pledge_subject/card_update";
    private static final String PAGE_CARD_NEW = "pledge_subject/card_new";
    private static final String PAGE_PLEDGE_SUBJECTS = "pledge_agreement/pledge_subjects";
    private static final String MSG_WRONG_LINK = "Неверная ссылка";
    private static final String ATTR_PLEDGE_AGREEMENT_ID = "pledgeAgreementId";
    private static final String ATTR_PLEDGE_AGREEMENT = "pledgeAgreement";
    private static final String ATTR_PLEDGE_SUBJECT = "pledgeSubject";
    private static final String ATTR_PS_AUTO = "pledgeSubjectAuto";
    private static final String ATTR_PS_EQUIPMENT = "pledgeSubjectEquipment";
    private static final String ATTR_PS_SECURITIES = "pledgeSubjectSecurities";
    private static final String ATTR_PS_VESSEL = "pledgeSubjectVessel";
    private static final String ATTR_PS_TBO = "pledgeSubjectTBO";
    private static final String ATTR_PS_LAND_OWNERSHIP = "pledgeSubjectLandOwnership";
    private static final String ATTR_PS_LAND_LEASE = "pledgeSubjectLandLease";
    private static final String ATTR_PS_BUILDING = "pledgeSubjectBuilding";
    private static final String ATTR_PS_ROOM = "pledgeSubjectRoom";
    private static final String ATTR_PS_REALTY = "pledgeSubjectRealty";
    private static final String ATTR_LAND_CATEGORY_LIST = "landCategoryList";
    private static final String ATTR_TYPE_OF_COLLATERAL = "typeOfCollateral";
    private static final String ATTR_MARKET_SEGMENT_LIST = "marketSegmentList";
    private static final String ATTR_COST_HISTORY = "costHistory";
    private static final String ATTR_MONITORING = "monitoring";

    public PledgeSubjectController(PledgeSubjectService pledgeSubjectService,
                                   LandCategoryService landCategoryService,
                                   MarketSegmentService marketSegmentService,
                                   PledgeAgreementService pledgeAgreementService) {
        this.pledgeSubjectService = pledgeSubjectService;
        this.landCategoryService = landCategoryService;
        this.marketSegmentService = marketSegmentService;
        this.pledgeAgreementService = pledgeAgreementService;
    }

    @GetMapping("/detail")
    public String pledgeSubjectDetailPage(@RequestParam("pledgeSubjectId") long pledgeSubjectId,
                                          Model model){
        PledgeSubject pledgeSubject = pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId)
                .orElseThrow(() -> new IllegalArgumentException(MSG_WRONG_LINK));
        model.addAttribute(ATTR_PLEDGE_SUBJECT, pledgeSubject);

        return PAGE_DETAIL;
    }

    @GetMapping("/card_update")
    public String pledgeSubjectCardUpdate(@RequestParam("pledgeSubjectId") Optional<Long> pledgeSubjectId,
                                          Model model){

        PledgeSubject pledgeSubject = pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId
                .orElseThrow(() -> new IllegalArgumentException(MSG_WRONG_LINK)))
                .orElseThrow(() -> new IllegalArgumentException(MSG_WRONG_LINK));
        if(pledgeSubject.getClass()==PledgeSubjectAuto.class){
            model.addAttribute(ATTR_PS_AUTO, pledgeSubject);
        }else if(pledgeSubject.getClass()==PledgeSubjectEquipment.class){
            model.addAttribute(ATTR_PS_EQUIPMENT, pledgeSubject);
        }else if(pledgeSubject.getClass()==PledgeSubjectSecurities.class){
            model.addAttribute(ATTR_PS_SECURITIES, pledgeSubject);
        }else if(pledgeSubject.getClass()==PledgeSubjectVessel.class){
            model.addAttribute(ATTR_PS_VESSEL, pledgeSubject);
        }else if(pledgeSubject.getClass()==PledgeSubjectTBO.class){
            model.addAttribute(ATTR_PS_TBO, pledgeSubject);
        }else if(pledgeSubject.getClass()==PledgeSubjectLandOwnership.class ||
                pledgeSubject.getClass()==PledgeSubjectLandLease.class){
            List<LandCategory> landCategoryList = landCategoryService.getAllLandCategory();
            model.addAttribute(ATTR_LAND_CATEGORY_LIST, landCategoryList);
            model.addAttribute(ATTR_PS_REALTY, pledgeSubject);
        }else if(pledgeSubject.getClass()==PledgeSubjectBuilding.class ||
                pledgeSubject.getClass()==PledgeSubjectRoom.class){
            List<MarketSegment> marketSegmentList = marketSegmentService.getAllMarketSegment();
            model.addAttribute(ATTR_MARKET_SEGMENT_LIST, marketSegmentList);
            model.addAttribute(ATTR_PS_REALTY, pledgeSubject);
        }

        model.addAttribute(ATTR_TYPE_OF_COLLATERAL, pledgeSubject.getTypeOfCollateral());

        return PAGE_CARD_UPDATE;
    }

    @GetMapping("card_new")
    public String pledgeSubjectCardNew(@RequestParam("typeOfCollateral") Optional<String> typeOfCollateral,
                                       @RequestParam("pledgeAgreementId") Optional<Long> pledgeAgreementId,
                                       Model model){

        if(!typeOfCollateral.isPresent() || !pledgeAgreementId.isPresent())
            throw new IllegalArgumentException(MSG_WRONG_LINK);

        PledgeAgreement pledgeAgreement = pledgeAgreementService.getPledgeAgreementById(pledgeAgreementId
                .orElseThrow(() -> new IllegalArgumentException(MSG_WRONG_LINK)))
                .orElseThrow(() -> new IllegalArgumentException(MSG_WRONG_LINK));

        if(typeOfCollateral.get().equals(TypeOfCollateral.AUTO.name())) {
            PledgeSubjectAuto pledgeSubjectAuto = PledgeSubjectAuto.builder().pledgeAgreement(pledgeAgreement).build();
            model.addAttribute(ATTR_PS_AUTO, pledgeSubjectAuto);
        }
        else if(typeOfCollateral.get().equals(TypeOfCollateral.EQUIPMENT.name())){
            PledgeSubjectEquipment pledgeSubjectEquipment = PledgeSubjectEquipment.builder().pledgeAgreement(pledgeAgreement).build();
            model.addAttribute(ATTR_PS_EQUIPMENT, pledgeSubjectEquipment);
        }
        else if(typeOfCollateral.get().equals(TypeOfCollateral.SECURITIES.name())){
            PledgeSubjectSecurities pledgeSubjectSecurities = PledgeSubjectSecurities.builder().pledgeAgreement(pledgeAgreement).build();
            model.addAttribute(ATTR_PS_SECURITIES, pledgeSubjectSecurities);
        }
        else if(typeOfCollateral.get().equals(TypeOfCollateral.VESSEL.name())){
            PledgeSubjectVessel pledgeSubjectVessel = PledgeSubjectVessel.builder().pledgeAgreement(pledgeAgreement).build();
            model.addAttribute(ATTR_PS_VESSEL, pledgeSubjectVessel);
        }
        else if(typeOfCollateral.get().equals(TypeOfCollateral.TBO.name())){
            PledgeSubjectTBO pledgeSubjectTBO = PledgeSubjectTBO.builder().pledgeAgreement(pledgeAgreement).build();
            model.addAttribute(ATTR_PS_TBO, pledgeSubjectTBO);
        }
        else if(typeOfCollateral.get().equals(TypeOfCollateral.LAND_OWNERSHIP.name())){
            PledgeSubjectLandOwnership pledgeSubjectLandOwnership = PledgeSubjectLandOwnership.builder()
                    .pledgeAgreement(pledgeAgreement)
                    .build();
            List<LandCategory> landCategoryList = landCategoryService.getAllLandCategory();
            model.addAttribute(ATTR_PS_LAND_OWNERSHIP, pledgeSubjectLandOwnership);
            model.addAttribute(ATTR_LAND_CATEGORY_LIST, landCategoryList);
        }
        else if(typeOfCollateral.get().equals(TypeOfCollateral.LAND_LEASE.name())){
            PledgeSubjectLandLease pledgeSubjectLandLease = PledgeSubjectLandLease.builder()
                    .pledgeAgreement(pledgeAgreement)
                    .build();
            List<LandCategory> landCategoryList = landCategoryService.getAllLandCategory();
            model.addAttribute(ATTR_PS_LAND_LEASE, pledgeSubjectLandLease);
            model.addAttribute(ATTR_LAND_CATEGORY_LIST, landCategoryList);
        }
        else if(typeOfCollateral.get().equals(TypeOfCollateral.BUILDING.name())){
            PledgeSubjectBuilding pledgeSubjectBuilding = PledgeSubjectBuilding.builder()
                    .pledgeAgreement(pledgeAgreement)
                    .build();
            List<MarketSegment> marketSegmentList = marketSegmentService.getAllMarketSegment();
            model.addAttribute(ATTR_MARKET_SEGMENT_LIST, marketSegmentList);
            model.addAttribute(ATTR_PS_BUILDING, pledgeSubjectBuilding);
        }
        else if(typeOfCollateral.get().equals(TypeOfCollateral.PREMISE.name())){
            PledgeSubjectRoom pledgeSubjectRoom = PledgeSubjectRoom.builder()
                    .pledgeAgreement(pledgeAgreement)
                    .build();
            List<MarketSegment> marketSegmentList = marketSegmentService.getAllMarketSegment();
            model.addAttribute(ATTR_MARKET_SEGMENT_LIST, marketSegmentList);
            model.addAttribute(ATTR_PS_ROOM, pledgeSubjectRoom);
        }

        CostHistory costHistory = new CostHistory();
        Monitoring monitoring = new Monitoring();

        model.addAttribute(ATTR_COST_HISTORY, costHistory);
        model.addAttribute(ATTR_MONITORING, monitoring);
        model.addAttribute(ATTR_PLEDGE_AGREEMENT_ID, pledgeAgreementId.get());
        model.addAttribute(ATTR_TYPE_OF_COLLATERAL, typeOfCollateral.get());

        return PAGE_CARD_NEW;

    }

    @PostMapping("/update_pledge_subject_auto")
    public String updatePledgeSubjectAuto(@Valid PledgeSubjectAuto pledgeSubjectAuto,
                                          BindingResult bindingResult,
                                          Model model){

        if(bindingResult.hasErrors()) {
            model.addAttribute(ATTR_TYPE_OF_COLLATERAL, pledgeSubjectAuto.getTypeOfCollateral());

            return PAGE_CARD_UPDATE;
        }

        PledgeSubject pledgeSubjectUpdated = pledgeSubjectService.updatePledgeSubject(pledgeSubjectAuto);

        model.addAttribute(ATTR_PLEDGE_SUBJECT, pledgeSubjectUpdated);

        return PAGE_DETAIL;
    }

    @PostMapping("update_pledge_subject_equipment")
    public String updatePledgeSubjectEquipment(@Valid PledgeSubjectEquipment pledgeSubjectEquipment,
                                               BindingResult bindingResult,
                                               Model model){
        if(bindingResult.hasErrors()) {
            model.addAttribute(ATTR_TYPE_OF_COLLATERAL, pledgeSubjectEquipment.getTypeOfCollateral());

            return PAGE_CARD_UPDATE;
        }

        PledgeSubject pledgeSubjectUpdated = pledgeSubjectService.updatePledgeSubject(pledgeSubjectEquipment);

        model.addAttribute(ATTR_PLEDGE_SUBJECT, pledgeSubjectUpdated);

        return PAGE_DETAIL;
    }

    @PostMapping("update_pledge_subject_securities")
    public String updatePledgeSubjectSecurities(@Valid PledgeSubjectSecurities pledgeSubjectSecurities,
                                                BindingResult bindingResult,
                                                Model model){
        if(bindingResult.hasErrors()) {
            model.addAttribute(ATTR_TYPE_OF_COLLATERAL, pledgeSubjectSecurities.getTypeOfCollateral());

            return PAGE_CARD_UPDATE;
        }

        PledgeSubject pledgeSubjectUpdated = pledgeSubjectService.updatePledgeSubject(pledgeSubjectSecurities);

        model.addAttribute(ATTR_PLEDGE_SUBJECT, pledgeSubjectUpdated);

        return PAGE_DETAIL;
    }

    @PostMapping("update_pledge_subject_vessel")
    public String updatePledgeSubjectVessel(@Valid PledgeSubjectVessel pledgeSubjectVessel,
                                            BindingResult bindingResult,
                                            Model model){
        if(bindingResult.hasErrors()) {
            model.addAttribute(ATTR_TYPE_OF_COLLATERAL, pledgeSubjectVessel.getTypeOfCollateral());

            return PAGE_CARD_UPDATE;
        }

        PledgeSubject pledgeSubjectUpdated = pledgeSubjectService.updatePledgeSubject(pledgeSubjectVessel);

        model.addAttribute(ATTR_PLEDGE_SUBJECT, pledgeSubjectUpdated);

        return PAGE_DETAIL;
    }

    @PostMapping("update_pledge_subject_tbo")
    public String updatePledgeSubjectTBO(@Valid PledgeSubjectTBO pledgeSubjectTBO,
                                         BindingResult bindingResult,
                                         Model model){
        if(bindingResult.hasErrors()) {
            model.addAttribute(ATTR_TYPE_OF_COLLATERAL, pledgeSubjectTBO.getTypeOfCollateral());

            return PAGE_CARD_UPDATE;
        }

        PledgeSubject pledgeSubjectUpdated = pledgeSubjectService.updatePledgeSubject(pledgeSubjectTBO);

        model.addAttribute(ATTR_PLEDGE_SUBJECT, pledgeSubjectUpdated);

        return PAGE_DETAIL;
    }

    @PostMapping("update_pledge_subject_landOwn")
    public String updatePledgeSubjectLandOwnership(@Valid PledgeSubjectLandOwnership pledgeSubjectLandOwnership,
                                                   BindingResult bindingResult,
                                                   Model model){
        if(bindingResult.hasErrors()) {
            List<LandCategory> landCategoryList = landCategoryService.getAllLandCategory();
            model.addAttribute(ATTR_LAND_CATEGORY_LIST, landCategoryList);
            model.addAttribute(ATTR_TYPE_OF_COLLATERAL, pledgeSubjectLandOwnership.getTypeOfCollateral());

            return PAGE_CARD_UPDATE;
        }

        PledgeSubject pledgeSubjectUpdated = pledgeSubjectService.updatePledgeSubject(pledgeSubjectLandOwnership);

        model.addAttribute(ATTR_PLEDGE_SUBJECT, pledgeSubjectUpdated);

        return PAGE_DETAIL;
    }

    @PostMapping("update_pledge_subject_landLease")
    public String updatePledgeSubjectLandLease(@Valid PledgeSubjectLandLease pledgeSubjectLandLease,
                                               BindingResult bindingResult,
                                               Model model){
        if(bindingResult.hasErrors()) {
            List<LandCategory> landCategoryList = landCategoryService.getAllLandCategory();
            model.addAttribute(ATTR_LAND_CATEGORY_LIST, landCategoryList);
            model.addAttribute(ATTR_TYPE_OF_COLLATERAL, pledgeSubjectLandLease.getTypeOfCollateral());

            return PAGE_CARD_UPDATE;
        }

        PledgeSubject pledgeSubjectUpdated = pledgeSubjectService.updatePledgeSubject(pledgeSubjectLandLease);

        model.addAttribute(ATTR_PLEDGE_SUBJECT, pledgeSubjectUpdated);

        return PAGE_DETAIL;
    }

    @PostMapping("update_pledge_subject_building")
    public String updatePledgeSubjectBuilding(@Valid PledgeSubjectBuilding pledgeSubjectBuilding,
                                              BindingResult bindingResult,
                                              Model model){
        if(bindingResult.hasErrors()) {
            List<MarketSegment> marketSegmentList = marketSegmentService.getAllMarketSegment();
            model.addAttribute(ATTR_MARKET_SEGMENT_LIST, marketSegmentList);
            model.addAttribute(ATTR_TYPE_OF_COLLATERAL, pledgeSubjectBuilding.getTypeOfCollateral());

            return PAGE_CARD_UPDATE;
        }

        PledgeSubject pledgeSubjectUpdated = pledgeSubjectService.updatePledgeSubject(pledgeSubjectBuilding);

        model.addAttribute(ATTR_PLEDGE_SUBJECT, pledgeSubjectUpdated);

        return PAGE_DETAIL;
    }

    @PostMapping("update_pledge_subject_room")
    public String updatePledgeSubjectRoom(@Valid PledgeSubjectRoom pledgeSubjectRoom,
                                          BindingResult bindingResult,
                                          Model model){
        if(bindingResult.hasErrors()) {
            List<MarketSegment> marketSegmentList = marketSegmentService.getAllMarketSegment();
            model.addAttribute(ATTR_MARKET_SEGMENT_LIST, marketSegmentList);
            model.addAttribute(ATTR_TYPE_OF_COLLATERAL, pledgeSubjectRoom.getTypeOfCollateral());

            return PAGE_CARD_UPDATE;
        }

        PledgeSubject pledgeSubjectUpdated = pledgeSubjectService.updatePledgeSubject(pledgeSubjectRoom);

        model.addAttribute(ATTR_PLEDGE_SUBJECT, pledgeSubjectUpdated);

        return PAGE_DETAIL;
    }

    @PostMapping("insert_pledge_subject_auto")
    public String insertNewPledgeSubjectAuto(@Valid PledgeSubjectAuto pledgeSubjectAuto,
                                             BindingResult bindingResultAuto,
                                             @Valid CostHistory costHistory,
                                             BindingResult bindingResultCostHistory,
                                             @Valid Monitoring monitoring,
                                             BindingResult bindingResultMonitoring,
                                             @RequestParam("pledgeAgreementId") long pledgeAgreementId,
                                             Model model){

        if(bindingResultAuto.hasErrors()){
            model.addAttribute(ATTR_COST_HISTORY, costHistory);
            model.addAttribute(ATTR_MONITORING, monitoring);
            model.addAttribute(ATTR_PLEDGE_AGREEMENT_ID, pledgeAgreementId);
            model.addAttribute(ATTR_TYPE_OF_COLLATERAL, pledgeSubjectAuto.getTypeOfCollateral());

            return PAGE_CARD_NEW;

        }else if(bindingResultCostHistory.hasErrors()){
            model.addAttribute(ATTR_PS_AUTO, pledgeSubjectAuto);
            model.addAttribute(ATTR_MONITORING, monitoring);
            model.addAttribute(ATTR_PLEDGE_AGREEMENT_ID, pledgeAgreementId);
            model.addAttribute(ATTR_TYPE_OF_COLLATERAL, pledgeSubjectAuto.getTypeOfCollateral());

            return PAGE_CARD_NEW;

        }else if(bindingResultMonitoring.hasErrors()){
            model.addAttribute(ATTR_PS_AUTO, pledgeSubjectAuto);
            model.addAttribute(ATTR_COST_HISTORY, costHistory);
            model.addAttribute(ATTR_PLEDGE_AGREEMENT_ID, pledgeAgreementId);
            model.addAttribute(ATTR_TYPE_OF_COLLATERAL, pledgeSubjectAuto.getTypeOfCollateral());

            return PAGE_CARD_NEW;
        }

        pledgeSubjectService.insertPledgeSubject(pledgeSubjectAuto, costHistory, monitoring);
        PledgeAgreement pledgeAgreement = pledgeAgreementService.getPledgeAgreementById(pledgeAgreementId)
                .orElseThrow(() -> new IllegalArgumentException(MSG_WRONG_LINK));
        model.addAttribute(ATTR_PLEDGE_AGREEMENT, pledgeAgreement);

        return PAGE_PLEDGE_SUBJECTS;
    }

    @PostMapping("insert_pledge_subject_equipment")
    public String insertNewPledgeSubjectEquipment(@Valid PledgeSubjectEquipment pledgeSubjectEquipment,
                                                  BindingResult bindingResultEquipment,
                                                  @Valid CostHistory costHistory,
                                                  BindingResult bindingResultCostHistory,
                                                  @Valid Monitoring monitoring,
                                                  BindingResult bindingResultMonitoring,
                                                  @RequestParam("pledgeAgreementId") long pledgeAgreementId,
                                                  Model model){

        if(bindingResultEquipment.hasErrors()){
            model.addAttribute(ATTR_COST_HISTORY, costHistory);
            model.addAttribute(ATTR_MONITORING, monitoring);
            model.addAttribute(ATTR_PLEDGE_AGREEMENT_ID, pledgeAgreementId);
            model.addAttribute(ATTR_TYPE_OF_COLLATERAL, pledgeSubjectEquipment.getTypeOfCollateral());

            return PAGE_CARD_NEW;

        }else if(bindingResultCostHistory.hasErrors()){
            model.addAttribute(ATTR_PS_EQUIPMENT, pledgeSubjectEquipment);
            model.addAttribute(ATTR_MONITORING, monitoring);
            model.addAttribute(ATTR_PLEDGE_AGREEMENT_ID, pledgeAgreementId);
            model.addAttribute(ATTR_TYPE_OF_COLLATERAL, pledgeSubjectEquipment.getTypeOfCollateral());

            return PAGE_CARD_NEW;

        }else if(bindingResultMonitoring.hasErrors()){
            model.addAttribute(ATTR_PS_EQUIPMENT, pledgeSubjectEquipment);
            model.addAttribute(ATTR_COST_HISTORY, costHistory);
            model.addAttribute(ATTR_PLEDGE_AGREEMENT_ID, pledgeAgreementId);
            model.addAttribute(ATTR_TYPE_OF_COLLATERAL, pledgeSubjectEquipment.getTypeOfCollateral());

            return PAGE_CARD_NEW;
        }

        pledgeSubjectService.insertPledgeSubject(pledgeSubjectEquipment, costHistory, monitoring);
        PledgeAgreement pledgeAgreement = pledgeAgreementService.getPledgeAgreementById(pledgeAgreementId)
                .orElseThrow(() -> new IllegalArgumentException(MSG_WRONG_LINK));
        model.addAttribute(ATTR_PLEDGE_AGREEMENT, pledgeAgreement);

        return PAGE_PLEDGE_SUBJECTS;
    }

    @PostMapping("insert_pledge_subject_securities")
    public String insertNewPledgeSubjectSecurities(@Valid PledgeSubjectSecurities pledgeSubjectSecurities,
                                                   BindingResult bindingResultSecurities,
                                                   @Valid CostHistory costHistory,
                                                   BindingResult bindingResultCostHistory,
                                                   @Valid Monitoring monitoring,
                                                   BindingResult bindingResultMonitoring,
                                                   @RequestParam("pledgeAgreementId") long pledgeAgreementId,
                                                   Model model){

        if(bindingResultSecurities.hasErrors()){
            model.addAttribute(ATTR_COST_HISTORY, costHistory);
            model.addAttribute(ATTR_MONITORING, monitoring);
            model.addAttribute(ATTR_PLEDGE_AGREEMENT_ID, pledgeAgreementId);
            model.addAttribute(ATTR_TYPE_OF_COLLATERAL, pledgeSubjectSecurities.getTypeOfCollateral());

            return PAGE_CARD_NEW;

        }else if(bindingResultCostHistory.hasErrors()){
            model.addAttribute(ATTR_PS_SECURITIES, pledgeSubjectSecurities);
            model.addAttribute(ATTR_MONITORING, monitoring);
            model.addAttribute(ATTR_PLEDGE_AGREEMENT_ID, pledgeAgreementId);
            model.addAttribute(ATTR_TYPE_OF_COLLATERAL, pledgeSubjectSecurities.getTypeOfCollateral());

            return PAGE_CARD_NEW;

        }else if(bindingResultMonitoring.hasErrors()){
            model.addAttribute(ATTR_PS_SECURITIES, pledgeSubjectSecurities);
            model.addAttribute(ATTR_COST_HISTORY, costHistory);
            model.addAttribute(ATTR_PLEDGE_AGREEMENT_ID, pledgeAgreementId);
            model.addAttribute(ATTR_TYPE_OF_COLLATERAL, pledgeSubjectSecurities.getTypeOfCollateral());

            return PAGE_CARD_NEW;
        }

        pledgeSubjectService.insertPledgeSubject(pledgeSubjectSecurities, costHistory, monitoring);
        PledgeAgreement pledgeAgreement = pledgeAgreementService.getPledgeAgreementById(pledgeAgreementId)
                .orElseThrow(() -> new IllegalArgumentException(MSG_WRONG_LINK));
        model.addAttribute(ATTR_PLEDGE_AGREEMENT, pledgeAgreement);

        return PAGE_PLEDGE_SUBJECTS;
    }

    @PostMapping("insert_pledge_subject_vessel")
    public String insertNewPledgeSubjectVessel(@Valid PledgeSubjectVessel pledgeSubjectVessel,
                                               BindingResult bindingResultVessel,
                                               @Valid CostHistory costHistory,
                                               BindingResult bindingResultCostHistory,
                                               @Valid Monitoring monitoring,
                                               BindingResult bindingResultMonitoring,
                                               @RequestParam("pledgeAgreementId") long pledgeAgreementId,
                                               Model model){

        if(bindingResultVessel.hasErrors()){
            model.addAttribute(ATTR_COST_HISTORY, costHistory);
            model.addAttribute(ATTR_MONITORING, monitoring);
            model.addAttribute(ATTR_PLEDGE_AGREEMENT_ID, pledgeAgreementId);
            model.addAttribute(ATTR_TYPE_OF_COLLATERAL, pledgeSubjectVessel.getTypeOfCollateral());

            return PAGE_CARD_NEW;

        }else if(bindingResultCostHistory.hasErrors()){
            model.addAttribute(ATTR_PS_VESSEL, pledgeSubjectVessel);
            model.addAttribute(ATTR_MONITORING, monitoring);
            model.addAttribute(ATTR_PLEDGE_AGREEMENT_ID, pledgeAgreementId);
            model.addAttribute(ATTR_TYPE_OF_COLLATERAL, pledgeSubjectVessel.getTypeOfCollateral());

            return PAGE_CARD_NEW;

        }else if(bindingResultMonitoring.hasErrors()){
            model.addAttribute(ATTR_PS_VESSEL, pledgeSubjectVessel);
            model.addAttribute(ATTR_COST_HISTORY, costHistory);
            model.addAttribute(ATTR_PLEDGE_AGREEMENT_ID, pledgeAgreementId);
            model.addAttribute(ATTR_TYPE_OF_COLLATERAL, pledgeSubjectVessel.getTypeOfCollateral());

            return PAGE_CARD_NEW;
        }

        pledgeSubjectService.insertPledgeSubject(pledgeSubjectVessel, costHistory, monitoring);
        PledgeAgreement pledgeAgreement = pledgeAgreementService.getPledgeAgreementById(pledgeAgreementId)
                .orElseThrow(() -> new IllegalArgumentException(MSG_WRONG_LINK));
        model.addAttribute(ATTR_PLEDGE_AGREEMENT, pledgeAgreement);

        return PAGE_PLEDGE_SUBJECTS;
    }

    @PostMapping("insert_pledge_subject_tbo")
    public String insertNewPledgeSubjectTBO(@Valid PledgeSubjectTBO pledgeSubjectTBO,
                                            BindingResult bindingResultTBO,
                                            @Valid CostHistory costHistory,
                                            BindingResult bindingResultCostHistory,
                                            @Valid Monitoring monitoring,
                                            BindingResult bindingResultMonitoring,
                                            @RequestParam("pledgeAgreementId") long pledgeAgreementId,
                                            Model model){

        if(bindingResultTBO.hasErrors()){
            model.addAttribute(ATTR_COST_HISTORY, costHistory);
            model.addAttribute(ATTR_MONITORING, monitoring);
            model.addAttribute(ATTR_PLEDGE_AGREEMENT_ID, pledgeAgreementId);
            model.addAttribute(ATTR_TYPE_OF_COLLATERAL, pledgeSubjectTBO.getTypeOfCollateral());

            return PAGE_CARD_NEW;

        }else if(bindingResultCostHistory.hasErrors()){
            model.addAttribute(ATTR_PS_TBO, pledgeSubjectTBO);
            model.addAttribute(ATTR_MONITORING, monitoring);
            model.addAttribute(ATTR_PLEDGE_AGREEMENT_ID, pledgeAgreementId);
            model.addAttribute(ATTR_TYPE_OF_COLLATERAL, pledgeSubjectTBO.getTypeOfCollateral());

            return PAGE_CARD_NEW;

        }else if(bindingResultMonitoring.hasErrors()){
            model.addAttribute(ATTR_PS_TBO, pledgeSubjectTBO);
            model.addAttribute(ATTR_COST_HISTORY, costHistory);
            model.addAttribute(ATTR_PLEDGE_AGREEMENT_ID, pledgeAgreementId);
            model.addAttribute(ATTR_TYPE_OF_COLLATERAL, pledgeSubjectTBO.getTypeOfCollateral());

            return PAGE_CARD_NEW;
        }

        pledgeSubjectService.insertPledgeSubject(pledgeSubjectTBO, costHistory, monitoring);
        PledgeAgreement pledgeAgreement = pledgeAgreementService.getPledgeAgreementById(pledgeAgreementId)
                .orElseThrow(() -> new IllegalArgumentException(MSG_WRONG_LINK));
        model.addAttribute(ATTR_PLEDGE_AGREEMENT, pledgeAgreement);

        return PAGE_PLEDGE_SUBJECTS;
    }

    @PostMapping("insert_pledge_subject_landOwn")
    public String insertNewPledgeSubjectLandOwnership(@Valid PledgeSubjectLandOwnership pledgeSubjectLandOwnership,
                                                      BindingResult bindingResultLandOwn,
                                                      @Valid CostHistory costHistory,
                                                      BindingResult bindingResultCostHistory,
                                                      @Valid Monitoring monitoring,
                                                      BindingResult bindingResultMonitoring,
                                                      @RequestParam("pledgeAgreementId") long pledgeAgreementId,
                                                      Model model){

        if(bindingResultLandOwn.hasErrors()){
            List<LandCategory> landCategoryList = landCategoryService.getAllLandCategory();
            model.addAttribute(ATTR_LAND_CATEGORY_LIST, landCategoryList);
            model.addAttribute(ATTR_COST_HISTORY, costHistory);
            model.addAttribute(ATTR_MONITORING, monitoring);
            model.addAttribute(ATTR_PLEDGE_AGREEMENT_ID, pledgeAgreementId);
            model.addAttribute(ATTR_TYPE_OF_COLLATERAL, pledgeSubjectLandOwnership.getTypeOfCollateral());

            return PAGE_CARD_NEW;

        }else if(bindingResultCostHistory.hasErrors()){
            List<LandCategory> landCategoryList = landCategoryService.getAllLandCategory();
            model.addAttribute(ATTR_LAND_CATEGORY_LIST, landCategoryList);
            model.addAttribute(ATTR_PS_LAND_OWNERSHIP, pledgeSubjectLandOwnership);
            model.addAttribute(ATTR_MONITORING, monitoring);
            model.addAttribute(ATTR_PLEDGE_AGREEMENT_ID, pledgeAgreementId);
            model.addAttribute(ATTR_TYPE_OF_COLLATERAL, pledgeSubjectLandOwnership.getTypeOfCollateral());

            return PAGE_CARD_NEW;

        }else if(bindingResultMonitoring.hasErrors()){
            List<LandCategory> landCategoryList = landCategoryService.getAllLandCategory();
            model.addAttribute(ATTR_LAND_CATEGORY_LIST, landCategoryList);
            model.addAttribute(ATTR_PS_LAND_OWNERSHIP, pledgeSubjectLandOwnership);
            model.addAttribute(ATTR_COST_HISTORY, costHistory);
            model.addAttribute(ATTR_PLEDGE_AGREEMENT_ID, pledgeAgreementId);
            model.addAttribute(ATTR_TYPE_OF_COLLATERAL, pledgeSubjectLandOwnership.getTypeOfCollateral());

            return PAGE_CARD_NEW;
        }

        pledgeSubjectService.insertPledgeSubject(pledgeSubjectLandOwnership, costHistory, monitoring);
        PledgeAgreement pledgeAgreement = pledgeAgreementService.getPledgeAgreementById(pledgeAgreementId)
                .orElseThrow(() -> new IllegalArgumentException(MSG_WRONG_LINK));
        model.addAttribute(ATTR_PLEDGE_AGREEMENT, pledgeAgreement);

        return PAGE_PLEDGE_SUBJECTS;
    }

    @PostMapping("insert_pledge_subject_landLease")
    public String insertNewPledgeSubjectLandLease(@Valid PledgeSubjectLandLease pledgeSubjectLandLease,
                                                  BindingResult bindingResultLandLease,
                                                  @Valid CostHistory costHistory,
                                                  BindingResult bindingResultCostHistory,
                                                  @Valid Monitoring monitoring,
                                                  BindingResult bindingResultMonitoring,
                                                  @RequestParam("pledgeAgreementId") long pledgeAgreementId,
                                                  Model model){

        if(bindingResultLandLease.hasErrors()){
            List<LandCategory> landCategoryList = landCategoryService.getAllLandCategory();
            model.addAttribute(ATTR_LAND_CATEGORY_LIST, landCategoryList);
            model.addAttribute(ATTR_COST_HISTORY, costHistory);
            model.addAttribute(ATTR_MONITORING, monitoring);
            model.addAttribute(ATTR_PLEDGE_AGREEMENT_ID, pledgeAgreementId);
            model.addAttribute(ATTR_TYPE_OF_COLLATERAL, pledgeSubjectLandLease.getTypeOfCollateral());

            return PAGE_CARD_NEW;

        }else if(bindingResultCostHistory.hasErrors()){
            List<LandCategory> landCategoryList = landCategoryService.getAllLandCategory();
            model.addAttribute(ATTR_LAND_CATEGORY_LIST, landCategoryList);
            model.addAttribute(ATTR_PS_LAND_LEASE, pledgeSubjectLandLease);
            model.addAttribute(ATTR_MONITORING, monitoring);
            model.addAttribute(ATTR_PLEDGE_AGREEMENT_ID, pledgeAgreementId);
            model.addAttribute(ATTR_TYPE_OF_COLLATERAL, pledgeSubjectLandLease.getTypeOfCollateral());

            return PAGE_CARD_NEW;

        }else if(bindingResultMonitoring.hasErrors()){
            List<LandCategory> landCategoryList = landCategoryService.getAllLandCategory();
            model.addAttribute(ATTR_LAND_CATEGORY_LIST, landCategoryList);
            model.addAttribute(ATTR_PS_LAND_LEASE, pledgeSubjectLandLease);
            model.addAttribute(ATTR_COST_HISTORY, costHistory);
            model.addAttribute(ATTR_PLEDGE_AGREEMENT_ID, pledgeAgreementId);
            model.addAttribute(ATTR_TYPE_OF_COLLATERAL, pledgeSubjectLandLease.getTypeOfCollateral());

            return PAGE_CARD_NEW;
        }

        pledgeSubjectService.insertPledgeSubject(pledgeSubjectLandLease, costHistory, monitoring);
        PledgeAgreement pledgeAgreement = pledgeAgreementService.getPledgeAgreementById(pledgeAgreementId)
                .orElseThrow(() -> new IllegalArgumentException(MSG_WRONG_LINK));
        model.addAttribute(ATTR_PLEDGE_AGREEMENT, pledgeAgreement);

        return PAGE_PLEDGE_SUBJECTS;
    }

    @PostMapping("insert_pledge_subject_building")
    public String insertNewPledgeSubjectBuilding(@Valid PledgeSubjectBuilding pledgeSubjectBuilding,
                                                 BindingResult bindingResultBuilding,
                                                 @Valid CostHistory costHistory,
                                                 BindingResult bindingResultCostHistory,
                                                 @Valid Monitoring monitoring,
                                                 BindingResult bindingResultMonitoring,
                                                 @RequestParam("pledgeAgreementId") long pledgeAgreementId,
                                                 Model model){

        if(bindingResultBuilding.hasErrors()){
            List<MarketSegment> marketSegmentList = marketSegmentService.getAllMarketSegment();
            model.addAttribute(ATTR_MARKET_SEGMENT_LIST, marketSegmentList);
            model.addAttribute(ATTR_COST_HISTORY, costHistory);
            model.addAttribute(ATTR_MONITORING, monitoring);
            model.addAttribute(ATTR_PLEDGE_AGREEMENT_ID, pledgeAgreementId);
            model.addAttribute(ATTR_TYPE_OF_COLLATERAL, pledgeSubjectBuilding.getTypeOfCollateral());

            return PAGE_CARD_NEW;

        }else if(bindingResultCostHistory.hasErrors()){
            List<MarketSegment> marketSegmentList = marketSegmentService.getAllMarketSegment();
            model.addAttribute(ATTR_MARKET_SEGMENT_LIST, marketSegmentList);
            model.addAttribute(ATTR_PS_BUILDING, pledgeSubjectBuilding);
            model.addAttribute(ATTR_MONITORING, monitoring);
            model.addAttribute(ATTR_PLEDGE_AGREEMENT_ID, pledgeAgreementId);
            model.addAttribute(ATTR_TYPE_OF_COLLATERAL, pledgeSubjectBuilding.getTypeOfCollateral());

            return PAGE_CARD_NEW;

        }else if(bindingResultMonitoring.hasErrors()){
            List<MarketSegment> marketSegmentList = marketSegmentService.getAllMarketSegment();
            model.addAttribute(ATTR_MARKET_SEGMENT_LIST, marketSegmentList);
            model.addAttribute(ATTR_PS_BUILDING, pledgeSubjectBuilding);
            model.addAttribute(ATTR_COST_HISTORY, costHistory);
            model.addAttribute(ATTR_PLEDGE_AGREEMENT_ID, pledgeAgreementId);
            model.addAttribute(ATTR_TYPE_OF_COLLATERAL, pledgeSubjectBuilding.getTypeOfCollateral());

            return PAGE_CARD_NEW;
        }

        pledgeSubjectService.insertPledgeSubject(pledgeSubjectBuilding, costHistory, monitoring);
        PledgeAgreement pledgeAgreement = pledgeAgreementService.getPledgeAgreementById(pledgeAgreementId)
                .orElseThrow(() -> new IllegalArgumentException(MSG_WRONG_LINK));
        model.addAttribute(ATTR_PLEDGE_AGREEMENT, pledgeAgreement);

        return PAGE_PLEDGE_SUBJECTS;
    }

    @PostMapping("insert_pledge_subject_room")
    public String insertNewPledgeSubjectRoom(@Valid PledgeSubjectRoom pledgeSubjectRoom,
                                             BindingResult bindingResultRoom,
                                             @Valid CostHistory costHistory,
                                             BindingResult bindingResultCostHistory,
                                             @Valid Monitoring monitoring,
                                             BindingResult bindingResultMonitoring,
                                             @RequestParam("pledgeAgreementId") long pledgeAgreementId,
                                             Model model){

        if(bindingResultRoom.hasErrors()){
            List<MarketSegment> marketSegmentList = marketSegmentService.getAllMarketSegment();
            model.addAttribute(ATTR_MARKET_SEGMENT_LIST, marketSegmentList);
            model.addAttribute(ATTR_COST_HISTORY, costHistory);
            model.addAttribute(ATTR_MONITORING, monitoring);
            model.addAttribute(ATTR_PLEDGE_AGREEMENT_ID, pledgeAgreementId);
            model.addAttribute(ATTR_TYPE_OF_COLLATERAL, pledgeSubjectRoom.getTypeOfCollateral());

            return PAGE_CARD_NEW;

        }else if(bindingResultCostHistory.hasErrors()){
            List<MarketSegment> marketSegmentList = marketSegmentService.getAllMarketSegment();
            model.addAttribute(ATTR_MARKET_SEGMENT_LIST, marketSegmentList);
            model.addAttribute(ATTR_PS_ROOM, pledgeSubjectRoom);
            model.addAttribute(ATTR_MONITORING, monitoring);
            model.addAttribute(ATTR_PLEDGE_AGREEMENT_ID, pledgeAgreementId);
            model.addAttribute(ATTR_TYPE_OF_COLLATERAL, pledgeSubjectRoom.getTypeOfCollateral());

            return PAGE_CARD_NEW;

        }else if(bindingResultMonitoring.hasErrors()){
            List<MarketSegment> marketSegmentList = marketSegmentService.getAllMarketSegment();
            model.addAttribute(ATTR_MARKET_SEGMENT_LIST, marketSegmentList);
            model.addAttribute(ATTR_PS_ROOM, pledgeSubjectRoom);
            model.addAttribute(ATTR_COST_HISTORY, costHistory);
            model.addAttribute(ATTR_PLEDGE_AGREEMENT_ID, pledgeAgreementId);
            model.addAttribute(ATTR_TYPE_OF_COLLATERAL, pledgeSubjectRoom.getTypeOfCollateral());

            return PAGE_CARD_NEW;
        }

        pledgeSubjectService.insertPledgeSubject(pledgeSubjectRoom, costHistory, monitoring);
        PledgeAgreement pledgeAgreement = pledgeAgreementService.getPledgeAgreementById(pledgeAgreementId)
                .orElseThrow(() -> new IllegalArgumentException(MSG_WRONG_LINK));
        model.addAttribute(ATTR_PLEDGE_AGREEMENT, pledgeAgreement);

        return PAGE_PLEDGE_SUBJECTS;
    }
}
