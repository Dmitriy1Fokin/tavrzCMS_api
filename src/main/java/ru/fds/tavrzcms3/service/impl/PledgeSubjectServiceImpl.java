package ru.fds.tavrzcms3.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fds.tavrzcms3.dictionary.TypeOfEquipment;
import ru.fds.tavrzcms3.dictionary.excelproprities.ExcelColumnNum;
import ru.fds.tavrzcms3.dictionary.LandCategory;
import ru.fds.tavrzcms3.dictionary.Liquidity;
import ru.fds.tavrzcms3.dictionary.MarketSegment;
import ru.fds.tavrzcms3.dictionary.StatusOfMonitoring;
import ru.fds.tavrzcms3.dictionary.TypeOfAuto;
import ru.fds.tavrzcms3.dictionary.TypeOfCollateral;
import ru.fds.tavrzcms3.dictionary.TypeOfMonitoring;
import ru.fds.tavrzcms3.dictionary.TypeOfPledge;
import ru.fds.tavrzcms3.dictionary.TypeOfSecurities;
import ru.fds.tavrzcms3.dictionary.TypeOfTBO;
import ru.fds.tavrzcms3.dictionary.TypeOfVessel;
import ru.fds.tavrzcms3.domain.CostHistory;
import ru.fds.tavrzcms3.domain.Monitoring;
import ru.fds.tavrzcms3.domain.jointable.PaJoinPs;
import ru.fds.tavrzcms3.domain.PledgeAgreement;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.domain.embedded.PledgeSubjectAuto;
import ru.fds.tavrzcms3.domain.embedded.PledgeSubjectBuilding;
import ru.fds.tavrzcms3.domain.embedded.PledgeSubjectEquipment;
import ru.fds.tavrzcms3.domain.embedded.PledgeSubjectLandLease;
import ru.fds.tavrzcms3.domain.embedded.PledgeSubjectLandOwnership;
import ru.fds.tavrzcms3.domain.embedded.PledgeSubjectRoom;
import ru.fds.tavrzcms3.domain.embedded.PledgeSubjectSecurities;
import ru.fds.tavrzcms3.domain.embedded.PledgeSubjectTBO;
import ru.fds.tavrzcms3.domain.embedded.PledgeSubjectVessel;
import ru.fds.tavrzcms3.exception.NotFoundException;
import ru.fds.tavrzcms3.exception.NotFullResultException;
import ru.fds.tavrzcms3.fileimport.FileImporter;
import ru.fds.tavrzcms3.fileimport.FileImporterFactory;
import ru.fds.tavrzcms3.repository.RepositoryCostHistory;
import ru.fds.tavrzcms3.repository.RepositoryMonitoring;
import ru.fds.tavrzcms3.repository.RepositoryPaJoinPs;
import ru.fds.tavrzcms3.repository.RepositoryPledgeAgreement;
import ru.fds.tavrzcms3.repository.RepositoryPledgeSubject;
import ru.fds.tavrzcms3.service.MessageService;
import ru.fds.tavrzcms3.service.PledgeSubjectService;
import ru.fds.tavrzcms3.specification.Search;
import ru.fds.tavrzcms3.validate.ValidatorEntity;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class PledgeSubjectServiceImpl implements PledgeSubjectService {

    private final RepositoryPledgeSubject repositoryPledgeSubject;
    private final RepositoryPledgeAgreement repositoryPledgeAgreement;
    private final RepositoryCostHistory repositoryCostHistory;
    private final RepositoryMonitoring repositoryMonitoring;
    private final RepositoryPaJoinPs repositoryPaJoinPs;
    private final ValidatorEntity validatorEntity;
    private final ExcelColumnNum excelColumnNum;
    private final MessageService messageService;

    private static final String MSG_OBJECT = "object ";
    private static final String MSG_WRONG_ID = "Неверный id{";
    private static final String MSG_LINE = "). Строка: ";
    private static final String NOT_ALL_PLEDGE_AGREEMENTS_WERE_FOUND = "Not all pledge agreements were found";

    public PledgeSubjectServiceImpl(RepositoryPledgeSubject repositoryPledgeSubject,
                                    RepositoryPledgeAgreement repositoryPledgeAgreement,
                                    RepositoryCostHistory repositoryCostHistory,
                                    RepositoryMonitoring repositoryMonitoring,
                                    RepositoryPaJoinPs repositoryPaJoinPs,
                                    ValidatorEntity validatorEntity,
                                    ExcelColumnNum excelColumnNum,
                                    MessageService messageService) {
        this.repositoryPledgeSubject = repositoryPledgeSubject;
        this.repositoryPledgeAgreement = repositoryPledgeAgreement;
        this.repositoryCostHistory = repositoryCostHistory;
        this.repositoryMonitoring = repositoryMonitoring;
        this.repositoryPaJoinPs = repositoryPaJoinPs;
        this.validatorEntity = validatorEntity;
        this.excelColumnNum = excelColumnNum;
        this.messageService = messageService;
    }

    @Override
    public Optional<PledgeSubject> getPledgeSubjectById(Long pledgeSubjectId){
        if(pledgeSubjectId == null)
            return Optional.empty();
        
        return repositoryPledgeSubject.findById(pledgeSubjectId);
    }

    @Override
    public List<PledgeSubject> getPledgeSubjectsByPledgeAgreement(long pledgeAgreementId){
        Optional<PledgeAgreement> pledgeAgreement = repositoryPledgeAgreement.findById(pledgeAgreementId);
        if(pledgeAgreement.isPresent()){
            return repositoryPledgeSubject.findPledgeSubjectByPledgeAgreement(pledgeAgreement.get());
        }else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<PledgeSubject> getPledgeSubjectsByPledgeAgreements(List<PledgeAgreement> pledgeAgreementList){
        return repositoryPledgeSubject.findPledgeSubjectByPledgeAgreements(pledgeAgreementList);
    }

    @Override
    public List<PledgeSubject> getPledgeSubjectsFromSearch(Map<String, String> searchParam) throws ReflectiveOperationException{
        final String SEARCH_PARAM_TYPE_OF_COLLATERAL = "typeOfCollateral";

        Search<PledgeSubject> pledgeSubjectSearch = new Search<>(PledgeSubject.class);
        pledgeSubjectSearch.withParam(searchParam);
        if(searchParam.containsKey(SEARCH_PARAM_TYPE_OF_COLLATERAL) && !searchParam.get(SEARCH_PARAM_TYPE_OF_COLLATERAL).isEmpty()){
            if(searchParam.get(SEARCH_PARAM_TYPE_OF_COLLATERAL).equals(TypeOfCollateral.AUTO.name())){
                pledgeSubjectSearch.withNestedAttributeParam(searchParam, "pledgeSubjectAuto");
            }else if(searchParam.get(SEARCH_PARAM_TYPE_OF_COLLATERAL).equals(TypeOfCollateral.EQUIPMENT.name())){
                pledgeSubjectSearch.withNestedAttributeParam(searchParam, "pledgeSubjectEquipment");
            }else if(searchParam.get(SEARCH_PARAM_TYPE_OF_COLLATERAL).equals(TypeOfCollateral.BUILDING.name())){
                pledgeSubjectSearch.withNestedAttributeParam(searchParam, "pledgeSubjectBuilding");
            }else if(searchParam.get(SEARCH_PARAM_TYPE_OF_COLLATERAL).equals(TypeOfCollateral.LAND_LEASE.name())){
                pledgeSubjectSearch.withNestedAttributeParam(searchParam, "pledgeSubjectLandLease");
            }else if(searchParam.get(SEARCH_PARAM_TYPE_OF_COLLATERAL).equals(TypeOfCollateral.LAND_OWNERSHIP.name())){
                pledgeSubjectSearch.withNestedAttributeParam(searchParam, "pledgeSubjectLandOwnership");
            }else if(searchParam.get(SEARCH_PARAM_TYPE_OF_COLLATERAL).equals(TypeOfCollateral.PREMISE.name())){
                pledgeSubjectSearch.withNestedAttributeParam(searchParam, "pledgeSubjectRoom");
            }else if(searchParam.get(SEARCH_PARAM_TYPE_OF_COLLATERAL).equals(TypeOfCollateral.SECURITIES.name())){
                pledgeSubjectSearch.withNestedAttributeParam(searchParam, "pledgeSubjectSecurities");
            }else if(searchParam.get(SEARCH_PARAM_TYPE_OF_COLLATERAL).equals(TypeOfCollateral.TBO.name())){
                pledgeSubjectSearch.withNestedAttributeParam(searchParam, "pledgeSubjectTBO");
            }else if(searchParam.get(SEARCH_PARAM_TYPE_OF_COLLATERAL).equals(TypeOfCollateral.VESSEL.name())){
                pledgeSubjectSearch.withNestedAttributeParam(searchParam, "pledgeSubjectVessel");
            }
        }

        return repositoryPledgeSubject.findAll(pledgeSubjectSearch.getSpecification());
    }

    @Override
    public List<PledgeSubject> getPledgeSubjectByCadastralNum(String cadastralNum){
        List<PledgeSubject> pledgeSubjectList = new ArrayList<>();
        pledgeSubjectList.addAll(repositoryPledgeSubject.findByPledgeSubjectBuildingCadastralNumContainingIgnoreCase(cadastralNum));
        pledgeSubjectList.addAll(repositoryPledgeSubject.findByPledgeSubjectLandLeaseCadastralNumContainingIgnoreCase(cadastralNum));
        pledgeSubjectList.addAll(repositoryPledgeSubject.findByPledgeSubjectLandOwnershipCadastralNumContainingIgnoreCase(cadastralNum));
        pledgeSubjectList.addAll(repositoryPledgeSubject.findByPledgeSubjectRoomCadastralNumContainingIgnoreCase(cadastralNum));

        return pledgeSubjectList;
    }

    @Override
    public List<PledgeSubject> getPledgeSubjectByName(String name){
        return repositoryPledgeSubject.findAllByNameContainingIgnoreCase(name);
    }

    @Override
    @Transactional
    public List<PledgeSubject> getNewPledgeSubjectsFromFile(File file, TypeOfCollateral typeOfCollateral) throws IOException {
        FileImporter fileImporter = FileImporterFactory.getInstance(file);
        for(int i = 0; i < excelColumnNum.getStartRow(); i++){
            fileImporter.nextLine();
        }
        int countRow = excelColumnNum.getStartRow();

        List<PledgeSubject> pledgeSubjectList = new ArrayList<>();
        List<List<PledgeAgreement>> pledgeAgreementsList = new ArrayList<>();
        List<CostHistory> costHistoryList = new ArrayList<>();
        List<Monitoring> monitoringList = new ArrayList<>();

        do {
            countRow += 1;

            PledgeSubject pledgeSubject = PledgeSubject.builder()
                    .name(fileImporter
                            .getString(excelColumnNum.getPledgeSubjectNew().getName()))
                    .zsDz(fileImporter
                            .getBigDecimal(excelColumnNum.getPledgeSubjectNew().getZsDz()))
                    .zsZz(fileImporter
                            .getBigDecimal(excelColumnNum.getPledgeSubjectNew().getZsZz()))
                    .rsDz(fileImporter
                            .getBigDecimal(excelColumnNum.getPledgeSubjectNew().getRsDz()))
                    .rsZz(fileImporter
                            .getBigDecimal(excelColumnNum.getPledgeSubjectNew().getRsZz()))
                    .ss(fileImporter
                            .getBigDecimal(excelColumnNum.getPledgeSubjectNew().getSs()))
                    .dateMonitoring(fileImporter
                            .getLocalDate(excelColumnNum.getPledgeSubjectNew().getDateMonitoring()))
                    .dateConclusion(fileImporter
                            .getLocalDate(excelColumnNum.getPledgeSubjectNew().getDateConclusion()))
                    .adressRegion(fileImporter
                            .getString(excelColumnNum.getPledgeSubjectNew().getAddressRegion()))
                    .adressDistrict(fileImporter
                            .getString(excelColumnNum.getPledgeSubjectNew().getAddressDistrict()))
                    .adressCity(fileImporter
                            .getString(excelColumnNum.getPledgeSubjectNew().getAddressCity()))
                    .adressStreet(fileImporter
                            .getString(excelColumnNum.getPledgeSubjectNew().getAddressStreet()))
                    .adressBuilbing(fileImporter
                            .getString(excelColumnNum.getPledgeSubjectNew().getAddressBuilding()))
                    .adressPemises(fileImporter
                            .getString(excelColumnNum.getPledgeSubjectNew().getAddressPremises()))
                    .insuranceObligation(fileImporter
                            .getString(excelColumnNum.getPledgeSubjectNew().getInsuranceObligation()))
                    .liquidity(Liquidity.valueOfString(fileImporter
                            .getString(excelColumnNum.getPledgeSubjectNew().getLiquidity())).orElse(null))
                    .statusMonitoring(StatusOfMonitoring.valueOfString(fileImporter
                            .getString(excelColumnNum.getPledgeSubjectNew().getStatusMonitoring())).orElse(null))
                    .typeOfCollateral(typeOfCollateral)
                    .typeOfPledge(TypeOfPledge.valueOfString(fileImporter
                            .getString(excelColumnNum.getPledgeSubjectNew().getTypeOfPledge())).orElse(null))
                    .typeOfMonitoring(TypeOfMonitoring.valueOfString(fileImporter
                            .getString(excelColumnNum.getPledgeSubjectNew().getTypeOfMonitoring())).orElse(null))
                    .build();

            if(typeOfCollateral == TypeOfCollateral.AUTO){
                pledgeSubject.setPledgeSubjectAuto(setAutoInNewPledgeSubject(fileImporter));
            }else if(typeOfCollateral == TypeOfCollateral.EQUIPMENT){
                pledgeSubject.setPledgeSubjectEquipment(setEquipmentInNewPledgeSubject(fileImporter));
            }else if(typeOfCollateral == TypeOfCollateral.BUILDING){
                pledgeSubject.setPledgeSubjectBuilding(setBuildingInNewPledgeSubject(fileImporter));
            }else if(typeOfCollateral == TypeOfCollateral.LAND_LEASE){
                pledgeSubject.setPledgeSubjectLandLease(setLandLeaseInNewPledgeSubject(fileImporter));
            }else if(typeOfCollateral == TypeOfCollateral.LAND_OWNERSHIP){
                pledgeSubject.setPledgeSubjectLandOwnership(setLandOwnershipInNewPledgeSubject(fileImporter));
            }else if(typeOfCollateral == TypeOfCollateral.PREMISE){
                pledgeSubject.setPledgeSubjectRoom(setPremiseInNewPledgeSubject(fileImporter));
            }else if(typeOfCollateral == TypeOfCollateral.SECURITIES){
                pledgeSubject.setPledgeSubjectSecurities(setSecuritiesInNewPledgeSubject(fileImporter));
            }else if(typeOfCollateral == TypeOfCollateral.TBO){
                pledgeSubject.setPledgeSubjectTBO(setTboInNewPledgeSubject(fileImporter));
            }else if(typeOfCollateral == TypeOfCollateral.VESSEL){
                pledgeSubject.setPledgeSubjectVessel(setVesselInNewPledgeSubject(fileImporter));
            }

            Set<ConstraintViolation<PledgeSubject>> violations =  validatorEntity.validateEntity(pledgeSubject);
            if(!violations.isEmpty())
                throw new ConstraintViolationException(MSG_OBJECT + countRow, violations);

            pledgeSubjectList.add(pledgeSubject);
            pledgeAgreementsList.add(setPledgeAgreementsInNewPledgeSubject(fileImporter, countRow));
            costHistoryList.add(setCostHistoryInNewPledgeSubject(fileImporter, countRow));
            monitoringList.add(setMonitoringInNewPledgeSubject(fileImporter, countRow));

        }while (fileImporter.nextLine());

        pledgeSubjectList = insertPledgeSubjects(pledgeSubjectList, pledgeAgreementsList, costHistoryList, monitoringList);

        return pledgeSubjectList;
    }

    private List<PledgeAgreement> setPledgeAgreementsInNewPledgeSubject(FileImporter fileImporter, int countRow) throws IOException {
        if(fileImporter.getLongList(excelColumnNum.getPledgeSubjectNew().getPledgeAgreementsIds(), excelColumnNum.getDelimiter()).isEmpty()){
            throw new IOException(MSG_WRONG_ID
                    + fileImporter.getLongList(excelColumnNum.getPledgeSubjectNew().getPledgeAgreementsIds(), excelColumnNum.getDelimiter())
                    + ") договора залога. Строка: " + countRow);
        }

        List<Long> ids = fileImporter
                .getLongList(excelColumnNum.getPledgeSubjectNew().getPledgeAgreementsIds(), excelColumnNum.getDelimiter());
        List<PledgeAgreement> pledgeAgreementList = repositoryPledgeAgreement.findAllByPledgeAgreementIdIn(ids);

        if(pledgeAgreementList.size() < ids.size()){
            throw new IOException(NOT_ALL_PLEDGE_AGREEMENTS_WERE_FOUND);
        }

        if(pledgeAgreementList.isEmpty()){
            throw new IOException("Договор залога с таким id отсутствует ("
                    + fileImporter.getLongList(excelColumnNum.getPledgeSubjectNew().getPledgeAgreementsIds(), excelColumnNum.getDelimiter())
                    + MSG_LINE + countRow);
        }

        return pledgeAgreementList;
    }
    
    private PledgeSubjectAuto setAutoInNewPledgeSubject(FileImporter fileImporter){
        return PledgeSubjectAuto.builder()
                .brandAuto(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectNew().getAuto().getBrand()))
                .modelAuto(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectNew().getAuto().getModel()))
                .vin(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectNew().getAuto().getVin()))
                .numOfEngine(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectNew().getAuto().getNumOfEngine()))
                .numOfPTS(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectNew().getAuto().getNumOfPts()))
                .yearOfManufactureAuto(fileImporter
                        .getInteger(excelColumnNum.getPledgeSubjectNew().getAuto().getYearOfManufacture()))
                .inventoryNumAuto(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectNew().getAuto().getInventoryNum()))
                .typeOfAuto(TypeOfAuto.valueOfString(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectNew().getAuto().getTypeOfAuto())).orElse(null))
                .horsepower(fileImporter
                        .getDouble(excelColumnNum.getPledgeSubjectNew().getAuto().getHorsepower()))
                .build();
    }
    
    private PledgeSubjectEquipment setEquipmentInNewPledgeSubject(FileImporter fileImporter){
        return PledgeSubjectEquipment.builder()
                .brandEquip(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectNew().getEquipment().getBrand()))
                .modelEquip(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectNew().getEquipment().getModel()))
                .serialNum(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectNew().getEquipment().getSerialNumber()))
                .yearOfManufactureEquip(fileImporter
                        .getInteger(excelColumnNum.getPledgeSubjectNew().getEquipment().getYearOfManufacture()))
                .inventoryNumEquip(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectNew().getEquipment().getInventoryNum()))
                .typeOfEquipment(TypeOfEquipment.valueOfString(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectNew().getEquipment().getTypeOfEquipment())).orElse(null))
                .productivity(fileImporter
                        .getDouble(excelColumnNum.getPledgeSubjectNew().getEquipment().getProductivity()))
                .typeOfProductivity(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectNew().getEquipment().getTypeOfProductivity()))
                .build();
    }
    
    private PledgeSubjectBuilding setBuildingInNewPledgeSubject(FileImporter fileImporter){
        return PledgeSubjectBuilding.builder()
                .area(fileImporter
                        .getDouble(excelColumnNum.getPledgeSubjectNew().getBuilding().getArea()))
                .cadastralNum(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectNew().getBuilding().getCadastralNum()))
                .conditionalNum(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectNew().getBuilding().getConditionalNum()))
                .readinessDegree(fileImporter
                        .getInteger(excelColumnNum.getPledgeSubjectNew().getBuilding().getReadinessDegree()))
                .yearOfConstruction(fileImporter
                        .getInteger(excelColumnNum.getPledgeSubjectNew().getBuilding().getYearOfConstruction()))
                .marketSegment(MarketSegment.valueOfString(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectNew().getBuilding().getMarketSegment())).orElse(null))
                .build();
    }
    
    private PledgeSubjectLandLease setLandLeaseInNewPledgeSubject(FileImporter fileImporter){
        return PledgeSubjectLandLease.builder()
                .area(fileImporter
                        .getDouble(excelColumnNum.getPledgeSubjectNew().getLandLease().getArea()))
                .cadastralNum(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectNew().getLandLease().getCadastralNum()))
                .conditionalNum(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectNew().getLandLease().getConditionalNum()))
                .permittedUse(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectNew().getLandLease().getPermittedUse()))
                .builtUp(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectNew().getLandLease().getBuiltUp()))
                .cadastralNumOfBuilding(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectNew().getLandLease().getCadastralNumOfBuilding()))
                .dateBeginLease(fileImporter
                        .getLocalDate(excelColumnNum.getPledgeSubjectNew().getLandLease().getDateBegin()))
                .dateEndLease(fileImporter
                        .getLocalDate(excelColumnNum.getPledgeSubjectNew().getLandLease().getDateEnd()))
                .landCategory(LandCategory.valueOfString(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectNew().getLandLease().getLandCategory())).orElse(null))
                .build();
    }
    
    private PledgeSubjectLandOwnership setLandOwnershipInNewPledgeSubject(FileImporter fileImporter){
        return PledgeSubjectLandOwnership.builder()
                .area(fileImporter
                        .getDouble(excelColumnNum.getPledgeSubjectNew().getLandOwnership().getArea()))
                .cadastralNum(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectNew().getLandOwnership().getCadastralNum()))
                .conditionalNum(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectNew().getLandOwnership().getConditionalNum()))
                .permittedUse(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectNew().getLandOwnership().getPermittedUse()))
                .builtUp(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectNew().getLandOwnership().getBuiltUp()))
                .cadastralNumOfBuilding(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectNew().getLandOwnership().getCadastralNumOfBuilding()))
                .landCategory(LandCategory.valueOfString(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectNew().getLandOwnership().getLandCategory())).orElse(null))
                .build();
    }
    
    private PledgeSubjectRoom setPremiseInNewPledgeSubject(FileImporter fileImporter){
        return PledgeSubjectRoom.builder()
                .area(fileImporter
                        .getDouble(excelColumnNum.getPledgeSubjectNew().getPremise().getArea()))
                .cadastralNum(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectNew().getPremise().getCadastralNum()))
                .conditionalNum(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectNew().getPremise().getConditionalNum()))
                .floorLocation(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectNew().getPremise().getFloorLocation()))
                .marketSegmentRoom(MarketSegment.valueOfString(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectNew().getPremise().getMarketSegmentRoom())).orElse(null))
                .marketSegmentBuilding(MarketSegment.valueOfString(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectNew().getPremise().getMarketSegmentBuilding())).orElse(null))
                .build();
    }
    
    private PledgeSubjectSecurities setSecuritiesInNewPledgeSubject(FileImporter fileImporter){
        return PledgeSubjectSecurities.builder()
                .nominalValue(fileImporter
                        .getDouble(excelColumnNum.getPledgeSubjectNew().getSecurities().getNominalValue()))
                .actualValue(fileImporter
                        .getDouble(excelColumnNum.getPledgeSubjectNew().getSecurities().getActualValue()))
                .typeOfSecurities(TypeOfSecurities.valueOfString(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectNew().getSecurities().getTypeOfSecurities())).orElse(null))
                .build();
    }
    
    private PledgeSubjectTBO setTboInNewPledgeSubject(FileImporter fileImporter){
        return PledgeSubjectTBO.builder()
                .countOfTBO(fileImporter
                        .getInteger(excelColumnNum.getPledgeSubjectNew().getTbo().getCount()))
                .carryingAmount(fileImporter
                        .getDouble(excelColumnNum.getPledgeSubjectNew().getTbo().getCarryingAmount()))
                .typeOfTBO(TypeOfTBO.valueOfString(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectNew().getTbo().getTypeOfTbo())).orElse(null))
                .build();
    }
    
    private PledgeSubjectVessel setVesselInNewPledgeSubject(FileImporter fileImporter){
        String imo = "";
        if(Objects.nonNull(fileImporter.getInteger(excelColumnNum.getPledgeSubjectNew().getVessel().getImo()))){
            imo = fileImporter.getInteger(excelColumnNum.getPledgeSubjectNew().getVessel().getImo()).toString();
        }

        String mmsi = "";
        if(Objects.nonNull(fileImporter.getInteger(excelColumnNum.getPledgeSubjectNew().getVessel().getMmsi()))){
            mmsi = fileImporter.getInteger(excelColumnNum.getPledgeSubjectNew().getVessel().getMmsi()).toString();
        }
        
        return PledgeSubjectVessel.builder()
                .imo(imo)
                .mmsi(mmsi)
                .flag(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectNew().getVessel().getFlag()))
                .vesselType(TypeOfVessel.valueOfString(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectNew().getVessel().getVesselType())).orElse(null))
                .grossTonnage(fileImporter
                        .getInteger(excelColumnNum.getPledgeSubjectNew().getVessel().getGrossTonnage()))
                .deadweight(fileImporter
                        .getInteger(excelColumnNum.getPledgeSubjectNew().getVessel().getDeadweight()))
                .yearBuilt(fileImporter
                        .getInteger(excelColumnNum.getPledgeSubjectNew().getVessel().getYearOfBuilt()))
                .statusVessel(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectNew().getVessel().getStatus()))
                .build();
    }
    
    private CostHistory setCostHistoryInNewPledgeSubject(FileImporter fileImporter, int countRow){
        CostHistory costHistory =  CostHistory.builder()
                .dateConclusion(fileImporter.getLocalDate(excelColumnNum.getPledgeSubjectNew().getDateConclusion()))
                .zsDz(fileImporter.getBigDecimal(excelColumnNum.getPledgeSubjectNew().getZsDz()))
                .zsZz(fileImporter.getBigDecimal(excelColumnNum.getPledgeSubjectNew().getZsZz()))
                .rsDz(fileImporter.getBigDecimal(excelColumnNum.getPledgeSubjectNew().getRsDz()))
                .rsZz(fileImporter.getBigDecimal(excelColumnNum.getPledgeSubjectNew().getRsZz()))
                .ss(fileImporter.getBigDecimal(excelColumnNum.getPledgeSubjectNew().getSs()))
                .appraiser(fileImporter.getString(excelColumnNum.getPledgeSubjectNew().getAppraiser()))
                .appraisalReportNum(fileImporter.getString(excelColumnNum.getPledgeSubjectNew().getNumAppraisalReport()))
                .appraisalReportDate(fileImporter.getLocalDate(excelColumnNum.getPledgeSubjectNew().getDateAppraisalReport()))
                .build();

        Set<ConstraintViolation<CostHistory>> violations =  validatorEntity.validateEntity(costHistory);
        if(!violations.isEmpty())
            throw new ConstraintViolationException(MSG_OBJECT + countRow, violations);

        return costHistory;
    }
    
    private Monitoring setMonitoringInNewPledgeSubject(FileImporter fileImporter, int countRow){
        Monitoring monitoring =  Monitoring.builder()
                .dateMonitoring(fileImporter.getLocalDate(excelColumnNum.getPledgeSubjectNew().getDateMonitoring()))
                .statusMonitoring(StatusOfMonitoring.valueOfString(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectNew().getStatusMonitoring())).orElse(null))
                .employee(fileImporter.getString(excelColumnNum.getPledgeSubjectNew().getEmployee()))
                .typeOfMonitoring(TypeOfMonitoring.valueOfString(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectNew().getTypeOfMonitoring())).orElse(null))
                .notice(fileImporter.getString(excelColumnNum.getPledgeSubjectNew().getNotice()))
                .collateralValue(fileImporter.getBigDecimal(excelColumnNum.getPledgeSubjectNew().getCollateralValue()))
                .build();

        Set<ConstraintViolation<Monitoring>> violations =  validatorEntity.validateEntity(monitoring);
        if(!violations.isEmpty())
            throw new ConstraintViolationException(MSG_OBJECT + countRow, violations);

        return monitoring;
    }

    @Override
    @Transactional
    public List<PledgeSubject> getCurrentPledgeSubjectsFromFile(File file) throws IOException{
        FileImporter fileImporter = FileImporterFactory.getInstance(file);
        for(int i = 0; i < excelColumnNum.getStartRow(); i++){
            fileImporter.nextLine();
        }
        int countRow = excelColumnNum.getStartRow();

        List<PledgeSubject> pledgeSubjectList = new ArrayList<>();
        List<List<PledgeAgreement>> pledgeAgreementsList = new ArrayList<>();

        do{
            countRow += 1;

            PledgeSubject pledgeSubject = setCurrentPledgeSubject(fileImporter, countRow);

            pledgeSubject.setName(fileImporter
                    .getString(excelColumnNum.getPledgeSubjectUpdate().getName()));
            pledgeSubject.setLiquidity(Liquidity.valueOfString(fileImporter
                    .getString(excelColumnNum.getPledgeSubjectUpdate().getLiquidity())).orElse(null));
            pledgeSubject.setTypeOfPledge(TypeOfPledge.valueOfString(fileImporter
                    .getString(excelColumnNum.getPledgeSubjectUpdate().getTypeOfPledge())).orElse(null));
            pledgeSubject.setAdressRegion(fileImporter
                    .getString(excelColumnNum.getPledgeSubjectUpdate().getAddressRegion()));
            pledgeSubject.setAdressDistrict(fileImporter
                    .getString(excelColumnNum.getPledgeSubjectUpdate().getAddressDistrict()));
            pledgeSubject.setAdressCity(fileImporter
                    .getString(excelColumnNum.getPledgeSubjectUpdate().getAddressCity()));
            pledgeSubject.setAdressStreet(fileImporter
                    .getString(excelColumnNum.getPledgeSubjectUpdate().getAddressStreet()));
            pledgeSubject.setAdressBuilbing(fileImporter
                    .getString(excelColumnNum.getPledgeSubjectUpdate().getAddressBuilding()));
            pledgeSubject.setAdressPemises(fileImporter
                    .getString(excelColumnNum.getPledgeSubjectUpdate().getAddressPremises()));
            pledgeSubject.setInsuranceObligation(fileImporter.getString(excelColumnNum.getPledgeSubjectUpdate().getInsuranceObligation()));

            if(Objects.nonNull(pledgeSubject.getPledgeSubjectAuto())){
                setCurrentAuto(fileImporter, pledgeSubject);
            }else if(Objects.nonNull(pledgeSubject.getPledgeSubjectEquipment())){
                setCurrentEquipment(fileImporter, pledgeSubject);
            }else if(Objects.nonNull(pledgeSubject.getPledgeSubjectBuilding())){
                setCurrentBuilding(fileImporter, pledgeSubject);
            }else if(Objects.nonNull(pledgeSubject.getPledgeSubjectLandLease())){
                setCurrentLandLease(fileImporter, pledgeSubject);
            }else if(Objects.nonNull(pledgeSubject.getPledgeSubjectLandOwnership())){
                setCurrentLandOwnership(fileImporter, pledgeSubject);
            }else if(Objects.nonNull(pledgeSubject.getPledgeSubjectRoom())){
                setCurrentPremise(fileImporter, pledgeSubject);
            }else if(Objects.nonNull(pledgeSubject.getPledgeSubjectSecurities())){
                setCurrentSecurities(fileImporter, pledgeSubject);
            }else if(Objects.nonNull(pledgeSubject.getPledgeSubjectTBO())){
                setCurrentTbo(fileImporter, pledgeSubject);
            }else if(Objects.nonNull(pledgeSubject.getPledgeSubjectVessel())){
                setCurrentVessel(fileImporter, pledgeSubject);
            }

            pledgeSubjectList.add(pledgeSubject);
            pledgeAgreementsList.add(setPledgeAgreementsInCurrentPledgeSubject(fileImporter, countRow));

        }while (fileImporter.nextLine());

        pledgeSubjectList = updatePledgeSubjects(pledgeSubjectList, pledgeAgreementsList);

        return pledgeSubjectList;
    }
    
    private PledgeSubject setCurrentPledgeSubject(FileImporter fileImporter, int countRow) throws IOException {
        if(Objects.isNull(fileImporter.getLong(excelColumnNum.getPledgeSubjectUpdate().getPledgeSubjectId()))){
            throw new IOException(MSG_WRONG_ID
                    + fileImporter.getLong(excelColumnNum.getPledgeSubjectUpdate().getPledgeSubjectId()) 
                    + ") предмета залога. Строка: " + countRow);
        }
        
        return getPledgeSubjectById(fileImporter.getLong(excelColumnNum.getPledgeSubjectUpdate().getPledgeSubjectId()))
                .orElseThrow(() -> new IOException("Предмет залога с таким id отсутствует ("
                        + fileImporter.getLong(excelColumnNum.getPledgeSubjectUpdate().getPledgeSubjectId())
                        + MSG_LINE + countRow));
    }
    
    private void setCurrentAuto(FileImporter fileImporter, PledgeSubject pledgeSubject){
        pledgeSubject.getPledgeSubjectAuto().setBrandAuto(fileImporter
                .getString(excelColumnNum.getPledgeSubjectUpdate().getAuto().getBrand()));
        pledgeSubject.getPledgeSubjectAuto().setModelAuto(fileImporter
                .getString(excelColumnNum.getPledgeSubjectUpdate().getAuto().getModel()));
        pledgeSubject.getPledgeSubjectAuto().setVin(fileImporter
                .getString(excelColumnNum.getPledgeSubjectUpdate().getAuto().getVin()));
        pledgeSubject.getPledgeSubjectAuto().setNumOfEngine(fileImporter
                .getString(excelColumnNum.getPledgeSubjectUpdate().getAuto().getNumOfEngine()));
        pledgeSubject.getPledgeSubjectAuto().setNumOfPTS(fileImporter
                .getString(excelColumnNum.getPledgeSubjectUpdate().getAuto().getNumOfPts()));
        pledgeSubject.getPledgeSubjectAuto().setYearOfManufactureAuto(fileImporter
                .getInteger(excelColumnNum.getPledgeSubjectUpdate().getAuto().getYearOfManufacture()));
        pledgeSubject.getPledgeSubjectAuto().setInventoryNumAuto(fileImporter
                .getString(excelColumnNum.getPledgeSubjectUpdate().getAuto().getInventoryNum()));
        pledgeSubject.getPledgeSubjectAuto().setTypeOfAuto(TypeOfAuto.valueOfString(fileImporter
                .getString(excelColumnNum.getPledgeSubjectUpdate().getAuto().getTypeOfAuto())).orElse(null));
        pledgeSubject.getPledgeSubjectAuto().setHorsepower(fileImporter
                .getDouble(excelColumnNum.getPledgeSubjectUpdate().getAuto().getHorsepower()));
    }
    
    private void setCurrentEquipment(FileImporter fileImporter, PledgeSubject pledgeSubject){
        pledgeSubject.getPledgeSubjectEquipment().setBrandEquip(fileImporter
                .getString(excelColumnNum.getPledgeSubjectUpdate().getEquipment().getBrand()));
        pledgeSubject.getPledgeSubjectEquipment().setModelEquip(fileImporter
                .getString(excelColumnNum.getPledgeSubjectUpdate().getEquipment().getModel()));
        pledgeSubject.getPledgeSubjectEquipment().setSerialNum(fileImporter
                .getString(excelColumnNum.getPledgeSubjectUpdate().getEquipment().getSerialNumber()));
        pledgeSubject.getPledgeSubjectEquipment().setYearOfManufactureEquip(fileImporter
                .getInteger(excelColumnNum.getPledgeSubjectUpdate().getEquipment().getYearOfManufacture()));
        pledgeSubject.getPledgeSubjectEquipment().setInventoryNumEquip(fileImporter
                .getString(excelColumnNum.getPledgeSubjectUpdate().getEquipment().getInventoryNum()));
        pledgeSubject.getPledgeSubjectEquipment().setTypeOfEquipment(TypeOfEquipment.valueOfString(fileImporter
                .getString(excelColumnNum.getPledgeSubjectUpdate().getEquipment().getTypeOfEquipment())).orElse(null));
        pledgeSubject.getPledgeSubjectEquipment().setProductivity(fileImporter
                .getDouble(excelColumnNum.getPledgeSubjectUpdate().getEquipment().getProductivity()));
        pledgeSubject.getPledgeSubjectEquipment().setTypeOfProductivity(fileImporter
                .getString(excelColumnNum.getPledgeSubjectUpdate().getEquipment().getTypeOfProductivity()));
    }
    
    private void setCurrentBuilding(FileImporter fileImporter, PledgeSubject pledgeSubject){
        pledgeSubject.getPledgeSubjectBuilding().setArea(fileImporter
                .getDouble(excelColumnNum.getPledgeSubjectUpdate().getBuilding().getArea()));
        pledgeSubject.getPledgeSubjectBuilding().setCadastralNum(fileImporter
                .getString(excelColumnNum.getPledgeSubjectUpdate().getBuilding().getCadastralNum()));
        pledgeSubject.getPledgeSubjectBuilding().setConditionalNum(fileImporter
                .getString(excelColumnNum.getPledgeSubjectUpdate().getBuilding().getConditionalNum()));
        pledgeSubject.getPledgeSubjectBuilding().setReadinessDegree(fileImporter
                .getInteger(excelColumnNum.getPledgeSubjectUpdate().getBuilding().getReadinessDegree()));
        pledgeSubject.getPledgeSubjectBuilding().setYearOfConstruction(fileImporter
                .getInteger(excelColumnNum.getPledgeSubjectUpdate().getBuilding().getYearOfConstruction()));
        pledgeSubject.getPledgeSubjectBuilding().setMarketSegment(MarketSegment.valueOfString(fileImporter
                .getString(excelColumnNum.getPledgeSubjectUpdate().getBuilding().getMarketSegment())).orElse(null));
    }
    
    private void setCurrentLandLease(FileImporter fileImporter, PledgeSubject pledgeSubject){
        pledgeSubject.getPledgeSubjectLandLease().setArea(fileImporter
                .getDouble(excelColumnNum.getPledgeSubjectUpdate().getLandLease().getArea()));
        pledgeSubject.getPledgeSubjectLandLease().setCadastralNum(fileImporter
                .getString(excelColumnNum.getPledgeSubjectUpdate().getLandLease().getCadastralNum()));
        pledgeSubject.getPledgeSubjectLandLease().setConditionalNum(fileImporter
                .getString(excelColumnNum.getPledgeSubjectUpdate().getLandLease().getConditionalNum()));
        pledgeSubject.getPledgeSubjectLandLease().setPermittedUse(fileImporter
                .getString(excelColumnNum.getPledgeSubjectUpdate().getLandLease().getPermittedUse()));
        pledgeSubject.getPledgeSubjectLandLease().setBuiltUp(fileImporter
                .getString(excelColumnNum.getPledgeSubjectUpdate().getLandLease().getBuiltUp()));
        pledgeSubject.getPledgeSubjectLandLease().setCadastralNumOfBuilding(fileImporter
                .getString(excelColumnNum.getPledgeSubjectUpdate().getLandLease().getCadastralNumOfBuilding()));
        pledgeSubject.getPledgeSubjectLandLease().setDateBeginLease(fileImporter
                .getLocalDate(excelColumnNum.getPledgeSubjectUpdate().getLandLease().getDateBegin()));
        pledgeSubject.getPledgeSubjectLandLease().setDateEndLease(fileImporter
                .getLocalDate(excelColumnNum.getPledgeSubjectUpdate().getLandLease().getDateEnd()));
        pledgeSubject.getPledgeSubjectLandLease().setLandCategory(LandCategory.valueOfString(fileImporter
                .getString(excelColumnNum.getPledgeSubjectUpdate().getLandLease().getLandCategory())).orElse(null));
    }
    
    private void setCurrentLandOwnership(FileImporter fileImporter, PledgeSubject pledgeSubject){
        pledgeSubject.getPledgeSubjectLandOwnership().setArea(fileImporter
                .getDouble(excelColumnNum.getPledgeSubjectUpdate().getLandOwnership().getArea()));
        pledgeSubject.getPledgeSubjectLandOwnership().setCadastralNum(fileImporter
                .getString(excelColumnNum.getPledgeSubjectUpdate().getLandOwnership().getCadastralNum()));
        pledgeSubject.getPledgeSubjectLandOwnership().setConditionalNum(fileImporter
                .getString(excelColumnNum.getPledgeSubjectUpdate().getLandOwnership().getConditionalNum()));
        pledgeSubject.getPledgeSubjectLandOwnership().setPermittedUse(fileImporter
                .getString(excelColumnNum.getPledgeSubjectUpdate().getLandOwnership().getPermittedUse()));
        pledgeSubject.getPledgeSubjectLandOwnership().setBuiltUp(fileImporter
                .getString(excelColumnNum.getPledgeSubjectUpdate().getLandOwnership().getBuiltUp()));
        pledgeSubject.getPledgeSubjectLandOwnership().setCadastralNumOfBuilding(fileImporter
                .getString(excelColumnNum.getPledgeSubjectUpdate().getLandOwnership().getCadastralNumOfBuilding()));
        pledgeSubject.getPledgeSubjectLandOwnership().setLandCategory(LandCategory.valueOfString(fileImporter
                .getString(excelColumnNum.getPledgeSubjectUpdate().getLandOwnership().getLandCategory())).orElse(null));
    }
    
    private void setCurrentPremise(FileImporter fileImporter, PledgeSubject pledgeSubject){
        pledgeSubject.getPledgeSubjectRoom().setArea(fileImporter
                .getDouble(excelColumnNum.getPledgeSubjectUpdate().getPremise().getArea()));
        pledgeSubject.getPledgeSubjectRoom().setCadastralNum(fileImporter
                .getString(excelColumnNum.getPledgeSubjectUpdate().getPremise().getCadastralNum()));
        pledgeSubject.getPledgeSubjectRoom().setConditionalNum(fileImporter
                .getString(excelColumnNum.getPledgeSubjectUpdate().getPremise().getConditionalNum()));
        pledgeSubject.getPledgeSubjectRoom().setFloorLocation(fileImporter
                .getString(excelColumnNum.getPledgeSubjectUpdate().getPremise().getFloorLocation()));
        pledgeSubject.getPledgeSubjectRoom().setMarketSegmentRoom(MarketSegment.valueOfString(fileImporter
                .getString(excelColumnNum.getPledgeSubjectUpdate().getPremise().getMarketSegmentRoom())).orElse(null));
        pledgeSubject.getPledgeSubjectRoom().setMarketSegmentBuilding(MarketSegment.valueOfString(fileImporter
                .getString(excelColumnNum.getPledgeSubjectUpdate().getPremise().getMarketSegmentBuilding())).orElse(null));
    }

    private void setCurrentSecurities(FileImporter fileImporter, PledgeSubject pledgeSubject){
        pledgeSubject.getPledgeSubjectSecurities().setNominalValue(fileImporter
                .getDouble(excelColumnNum.getPledgeSubjectUpdate().getSecurities().getNominalValue()));
        pledgeSubject.getPledgeSubjectSecurities().setActualValue(fileImporter
                .getDouble(excelColumnNum.getPledgeSubjectUpdate().getSecurities().getActualValue()));
        pledgeSubject.getPledgeSubjectSecurities().setTypeOfSecurities(TypeOfSecurities.valueOfString(fileImporter
                .getString(excelColumnNum.getPledgeSubjectUpdate().getSecurities().getTypeOfSecurities())).orElse(null));
    }

    private void setCurrentTbo(FileImporter fileImporter, PledgeSubject pledgeSubject){
        pledgeSubject.getPledgeSubjectTBO().setCountOfTBO(fileImporter
                .getInteger(excelColumnNum.getPledgeSubjectUpdate().getTbo().getCount()));
        pledgeSubject.getPledgeSubjectTBO().setCarryingAmount(fileImporter
                .getDouble(excelColumnNum.getPledgeSubjectUpdate().getTbo().getCarryingAmount()));
        pledgeSubject.getPledgeSubjectTBO().setTypeOfTBO(TypeOfTBO.valueOfString(fileImporter
                .getString(excelColumnNum.getPledgeSubjectUpdate().getTbo().getTypeOfTbo())).orElse(null));
    }

    private void setCurrentVessel(FileImporter fileImporter, PledgeSubject pledgeSubject){
        String imo = "";
        if(Objects.nonNull(fileImporter.getInteger(excelColumnNum.getPledgeSubjectUpdate().getVessel().getImo()))){
            imo = fileImporter.getInteger(excelColumnNum.getPledgeSubjectUpdate().getVessel().getImo()).toString();
        }

        String mmsi = "";
        if(Objects.nonNull(fileImporter.getInteger(excelColumnNum.getPledgeSubjectUpdate().getVessel().getMmsi()))){
            mmsi = fileImporter.getInteger(excelColumnNum.getPledgeSubjectUpdate().getVessel().getMmsi()).toString();
        }

        pledgeSubject.getPledgeSubjectVessel().setImo(imo);
        pledgeSubject.getPledgeSubjectVessel().setMmsi(mmsi);
        pledgeSubject.getPledgeSubjectVessel().setFlag(fileImporter
                .getString(excelColumnNum.getPledgeSubjectUpdate().getVessel().getFlag()));
        pledgeSubject.getPledgeSubjectVessel().setVesselType(TypeOfVessel.valueOfString(fileImporter
                .getString(excelColumnNum.getPledgeSubjectUpdate().getVessel().getVesselType())).orElse(null));
        pledgeSubject.getPledgeSubjectVessel().setGrossTonnage(fileImporter
                .getInteger(excelColumnNum.getPledgeSubjectUpdate().getVessel().getGrossTonnage()));
        pledgeSubject.getPledgeSubjectVessel().setDeadweight(fileImporter
                .getInteger(excelColumnNum.getPledgeSubjectUpdate().getVessel().getDeadweight()));
        pledgeSubject.getPledgeSubjectVessel().setYearBuilt(fileImporter
                .getInteger(excelColumnNum.getPledgeSubjectUpdate().getVessel().getYearOfBuilt()));
        pledgeSubject.getPledgeSubjectVessel().setStatusVessel(fileImporter
                .getString(excelColumnNum.getPledgeSubjectUpdate().getVessel().getStatus()));
    }

    private List<PledgeAgreement> setPledgeAgreementsInCurrentPledgeSubject(FileImporter fileImporter, int countRow) throws IOException {
        if(fileImporter.getLongList(excelColumnNum.getPledgeSubjectUpdate().getPledgeAgreementsIds(), excelColumnNum.getDelimiter()).isEmpty()){
            throw new IOException(MSG_WRONG_ID
                    + fileImporter.getLongList(excelColumnNum.getPledgeSubjectUpdate().getPledgeAgreementsIds(), excelColumnNum.getDelimiter())
                    + ") договора залога. Строка: " + countRow);
        }

        List<Long> ids = fileImporter
                .getLongList(excelColumnNum.getPledgeSubjectUpdate().getPledgeAgreementsIds(), excelColumnNum.getDelimiter());
        List<PledgeAgreement> pledgeAgreementList = repositoryPledgeAgreement
                .findAllByPledgeAgreementIdIn(ids);

        if(pledgeAgreementList.size() < ids.size()){
            throw new IOException(NOT_ALL_PLEDGE_AGREEMENTS_WERE_FOUND);
        }

        if(pledgeAgreementList.isEmpty()){
            throw new IOException("Договор залога с таким id отсутствует ("
                    + fileImporter.getLongList(excelColumnNum.getPledgeSubjectUpdate().getPledgeAgreementsIds(), excelColumnNum.getDelimiter())
                    + MSG_LINE + countRow);
        }

        return pledgeAgreementList;
    }

    @Override
    @Transactional
    public PledgeSubject updatePledgeSubject(PledgeSubject pledgeSubject, List<Long> pledgeAgreementsIds) {
        pledgeSubject = repositoryPledgeSubject.save(pledgeSubject);

        List<PledgeAgreement> pledgeAgreementListFromRequest = repositoryPledgeAgreement.findAllByPledgeAgreementIdIn(pledgeAgreementsIds);
        if(pledgeAgreementListFromRequest.size() < pledgeAgreementsIds.size()){
            throw new NotFullResultException(NOT_ALL_PLEDGE_AGREEMENTS_WERE_FOUND);
        }

        List<PledgeAgreement> pledgeAgreementListFromDB = repositoryPledgeAgreement.findByPledgeSubject(pledgeSubject);
        List<PledgeAgreement> pledgeAgreementListToDelete = new ArrayList<>(pledgeAgreementListFromDB);
        pledgeAgreementListToDelete.removeAll(pledgeAgreementListFromRequest);
        repositoryPaJoinPs.deleteByPledgeAgreementInAndPledgeSubject(pledgeAgreementListToDelete, pledgeSubject);

        List<PledgeAgreement> pledgeAgreementListToInsert = new ArrayList<>(pledgeAgreementListFromRequest);
        pledgeAgreementListToInsert.removeAll(pledgeAgreementListFromDB);
        List<PaJoinPs> paJoinPsList = new ArrayList<>(pledgeAgreementListToInsert.size());
        for(PledgeAgreement pledgeAgreement : pledgeAgreementListToInsert){
            paJoinPsList.add(new PaJoinPs(pledgeAgreement, pledgeSubject));
        }

        repositoryPaJoinPs.saveAll(paJoinPsList);

        messageService.sendNewPledgeSubject(pledgeSubject.getPledgeSubjectId());

        return pledgeSubject;
    }

    @Override
    @Transactional
    public List<PledgeSubject> updatePledgeSubjects(List<PledgeSubject> pledgeSubjectList,
                                                    List<List<PledgeAgreement>> pledgeAgreementList){
        pledgeSubjectList = repositoryPledgeSubject.saveAll(pledgeSubjectList);

        List<PaJoinPs> paJoinPsList = new ArrayList<>();
        for(int i = 0; i < pledgeSubjectList.size(); i++){
            List<PledgeAgreement> pledgeAgreementListFromDB = repositoryPledgeAgreement.findByPledgeSubject(pledgeSubjectList.get(i));
            List<PledgeAgreement> pledgeAgreementListToDelete = new ArrayList<>(pledgeAgreementListFromDB);
            pledgeAgreementListToDelete.removeAll(pledgeAgreementList.get(i));
            repositoryPaJoinPs.deleteByPledgeAgreementInAndPledgeSubject(pledgeAgreementListToDelete, pledgeSubjectList.get(i));

            List<PledgeAgreement> pledgeAgreementListToInsert = new ArrayList<>(pledgeAgreementList.get(i));
            pledgeAgreementListToInsert.removeAll(pledgeAgreementListFromDB);
            for(PledgeAgreement pledgeAgreement : pledgeAgreementListToInsert){
                paJoinPsList.add(new PaJoinPs(pledgeAgreement, pledgeSubjectList.get(i)));
            }
        }

        repositoryPaJoinPs.saveAll(paJoinPsList);

        pledgeSubjectList.forEach(pledgeSubject -> messageService.sendExistPledgeSubject(pledgeSubject.getPledgeSubjectId()));

        return pledgeSubjectList;
    }

    @Override
    @Transactional
    public PledgeSubject insertPledgeSubject(PledgeSubject pledgeSubject,
                                             List<Long> pledgeAgreementsIds,
                                             CostHistory costHistory,
                                             Monitoring monitoring){

        pledgeSubject = repositoryPledgeSubject.save(pledgeSubject);

        costHistory.setPledgeSubject(pledgeSubject);
        repositoryCostHistory.save(costHistory);

        monitoring.setPledgeSubject(pledgeSubject);
        repositoryMonitoring.save(monitoring);

        List<PledgeAgreement> pledgeAgreementList = repositoryPledgeAgreement.findAllByPledgeAgreementIdIn(pledgeAgreementsIds);
        if(pledgeAgreementList.size() < pledgeAgreementsIds.size()){
            throw new NotFoundException(NOT_ALL_PLEDGE_AGREEMENTS_WERE_FOUND);
        }

        List<PaJoinPs> paJoinPsList = new ArrayList<>(pledgeAgreementList.size());
        for(PledgeAgreement pledgeAgreement : pledgeAgreementList) {
            paJoinPsList.add(new PaJoinPs(pledgeAgreement, pledgeSubject));
        }
        repositoryPaJoinPs.saveAll(paJoinPsList);

        messageService.sendNewPledgeSubject(pledgeSubject.getPledgeSubjectId());

        return pledgeSubject;
    }

    @Override
    @Transactional
    public List<PledgeSubject> insertPledgeSubjects(List<PledgeSubject> pledgeSubjectList,
                                                    List<List<PledgeAgreement>> pledgeAgreementList,
                                                    List<CostHistory> costHistoryList,
                                                    List<Monitoring> monitoringList){

        pledgeSubjectList = repositoryPledgeSubject.saveAll(pledgeSubjectList);

        List<PaJoinPs> paJoinPsList = new ArrayList<>(pledgeSubjectList.size());
        for (int i = 0; i < pledgeSubjectList.size(); i++){
            costHistoryList.get(i).setPledgeSubject(pledgeSubjectList.get(i));
            monitoringList.get(i).setPledgeSubject(pledgeSubjectList.get(i));
            for(PledgeAgreement pledgeAgreement : pledgeAgreementList.get(i)){
                paJoinPsList.add(new PaJoinPs(pledgeAgreement, pledgeSubjectList.get(i)));
            }
        }

        repositoryCostHistory.saveAll(costHistoryList);
        repositoryMonitoring.saveAll(monitoringList);
        repositoryPaJoinPs.saveAll(paJoinPsList);

        pledgeSubjectList.forEach(pledgeSubject -> messageService.sendNewPledgeSubject(pledgeSubject.getPledgeSubjectId()));

        return pledgeSubjectList;
    }
}
