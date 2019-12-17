package ru.fds.tavrzcms3.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fds.tavrzcms3.dictionary.Liquidity;
import ru.fds.tavrzcms3.dictionary.StatusOfMonitoring;
import ru.fds.tavrzcms3.dictionary.TypeOfAuto;
import ru.fds.tavrzcms3.dictionary.TypeOfCollateral;
import ru.fds.tavrzcms3.dictionary.TypeOfEquip;
import ru.fds.tavrzcms3.dictionary.TypeOfPledge;
import ru.fds.tavrzcms3.dictionary.TypeOfSecurities;
import ru.fds.tavrzcms3.dictionary.TypeOfTBO;
import ru.fds.tavrzcms3.dictionary.TypeOfVessel;
import ru.fds.tavrzcms3.domain.CostHistory;
import ru.fds.tavrzcms3.domain.Monitoring;
import ru.fds.tavrzcms3.domain.PledgeAgreement;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.domain.PledgeSubjectAuto;
import ru.fds.tavrzcms3.domain.PledgeSubjectBuilding;
import ru.fds.tavrzcms3.domain.PledgeSubjectEquipment;
import ru.fds.tavrzcms3.domain.PledgeSubjectLandLease;
import ru.fds.tavrzcms3.domain.PledgeSubjectLandOwnership;
import ru.fds.tavrzcms3.domain.PledgeSubjectRoom;
import ru.fds.tavrzcms3.domain.PledgeSubjectSecurities;
import ru.fds.tavrzcms3.domain.PledgeSubjectTBO;
import ru.fds.tavrzcms3.domain.PledgeSubjectVessel;
import ru.fds.tavrzcms3.repository.RepositoryCostHistory;
import ru.fds.tavrzcms3.repository.RepositoryEncumbrance;
import ru.fds.tavrzcms3.repository.RepositoryInsurance;
import ru.fds.tavrzcms3.repository.RepositoryMonitoring;
import ru.fds.tavrzcms3.repository.RepositoryPledgeAgreement;
import ru.fds.tavrzcms3.repository.RepositoryPledgeSubject;
import ru.fds.tavrzcms3.specification.SpecificationBuilder;
import ru.fds.tavrzcms3.specification.SpecificationBuilderImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class PledgeSubjectService {

    private final RepositoryPledgeSubject repositoryPledgeSubject;
    private final RepositoryPledgeAgreement repositoryPledgeAgreement;
    private final RepositoryCostHistory repositoryCostHistory;
    private final RepositoryMonitoring repositoryMonitoring;
    private final RepositoryEncumbrance repositoryEncumbrance;
    private final RepositoryInsurance repositoryInsurance;

    public PledgeSubjectService(RepositoryPledgeSubject repositoryPledgeSubject,
                                RepositoryPledgeAgreement repositoryPledgeAgreement,
                                RepositoryCostHistory repositoryCostHistory,
                                RepositoryMonitoring repositoryMonitoring,
                                RepositoryEncumbrance repositoryEncumbrance,
                                RepositoryInsurance repositoryInsurance) {
        this.repositoryPledgeSubject = repositoryPledgeSubject;
        this.repositoryPledgeAgreement = repositoryPledgeAgreement;
        this.repositoryCostHistory = repositoryCostHistory;
        this.repositoryMonitoring = repositoryMonitoring;
        this.repositoryEncumbrance = repositoryEncumbrance;
        this.repositoryInsurance = repositoryInsurance;
    }

    public Optional<PledgeSubject> getPledgeSubjectById(Long pledgeSubjectId){
        if(pledgeSubjectId == null)
            return Optional.empty();
        
        return repositoryPledgeSubject.findById(pledgeSubjectId);
    }

    public List<PledgeSubject> getPledgeSubjectByIds(List<Long> ids){
        return repositoryPledgeSubject.findAllByPledgeSubjectIdIn(ids);
    }

    public List<PledgeSubject> getPledgeSubjectsForPledgeAgreement(long pledgeAgreementId){
        PledgeAgreement pledgeAgreement = repositoryPledgeAgreement.getOne(pledgeAgreementId);
        return repositoryPledgeSubject.findByPledgeAgreements(pledgeAgreement);
    }

    public List<PledgeSubject> getPledgeSubjectByPledgeAgreement(PledgeAgreement pledgeAgreement){
        return repositoryPledgeSubject.findByPledgeAgreements(pledgeAgreement);
    }

    public List<PledgeSubject> getPledgeSubjectsForPledgeAgreements(List<PledgeAgreement> pledgeAgreementList){
        return repositoryPledgeSubject.findByPledgeAgreementsIn(pledgeAgreementList);
    }

    public Optional<PledgeSubject> getPledgeSubjectByCostHistory(CostHistory costHistory){
        return repositoryPledgeSubject.findByCostHistories(costHistory);
    }

    public Page<PledgeSubject> getPledgeSubjectsFromSearch(Map<String, String> searchParam){

//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy-MM-dd");
//
//        int currentPage = Integer.parseInt(searchParam.get("page"));
//        int pageSize = Integer.parseInt(searchParam.get("size"));
//        Pageable pageable = PageRequest.of(currentPage, pageSize);
//
//        SpecificationBuilder builder = new SpecificationBuilderImpl();
//
//        if(!searchParam.get("namePS").isEmpty())
//            builder.with("name", ":", searchParam.get("namePS"), false);
//        if(!searchParam.get("liquidity").isEmpty())
//            builder.with("liquidity", ":", Liquidity.valueOf(searchParam.get("liquidity")), false);
//        if(!searchParam.get("rsDZ").isEmpty())
//            builder.with("rsDz", searchParam.get("rsDZOption"), searchParam.get("rsDZ"), false);
//        if(!searchParam.get("zsDZ").isEmpty())
//            builder.with("zsDz", searchParam.get("zsDZOption"), searchParam.get("zsDZ"), false);
//        if(!searchParam.get("rsZZ").isEmpty())
//            builder.with("rsZz", searchParam.get("rsZZOption"), searchParam.get("rsZZ"), false);
//        if(!searchParam.get("zsZZ").isEmpty())
//            builder.with("zsZz", searchParam.get("zsZZOption"), searchParam.get("zsZZ"), false);
//        if(!searchParam.get("ss").isEmpty())
//            builder.with("ss", searchParam.get("ssOption"), searchParam.get("ss"), false);
//        if(!searchParam.get("dateMonitoring").isEmpty()){
//            try {
//                Date date = simpleDateFormat.parse(searchParam.get("dateMonitoring"));
//                builder.with("dateMonitoring", searchParam.get("dateMonitoringOption"), date, false);
//            }catch (ParseException e){
//                System.out.println("Не верный фортат dateEndPA");
//                e.printStackTrace();
//            }
//        }
//        if(!searchParam.get("dateConclusion").isEmpty()){
//            try {
//                Date date = simpleDateFormat.parse(searchParam.get("dateConclusion"));
//                builder.with("dateConclusion", searchParam.get("dateConclusionOption"), date, false);
//            }catch (ParseException e){
//                System.out.println("Не верный фортат dateEndPA");
//                e.printStackTrace();
//            }
//        }
//        if(!searchParam.get("resultOfMonitoring").isEmpty())
//            builder.with("statusMonitoring", ":", StatusOfMonitoring.valueOf(searchParam.get("resultOfMonitoring")), false);
//        if(!searchParam.get("typeOfPledge").isEmpty())
//            builder.with("typeOfPledge", ":", TypeOfPledge.valueOf(searchParam.get("typeOfPledge")), false);
//        if(!searchParam.get("adressRegion").isEmpty())
//            builder.with("adressRegion", ":", searchParam.get("adressRegion"), false);
//        if(!searchParam.get("adressDistrict").isEmpty())
//            builder.with("adressDistrict", ":", searchParam.get("adressDistrict"), false);
//        if(!searchParam.get("adressCity").isEmpty())
//            builder.with("adressCity", ":", searchParam.get("adressCity"), false);
//        if(!searchParam.get("adressStreet").isEmpty())
//            builder.with("adressStreet", ":", searchParam.get("adressStreet"), false);
//        if(!searchParam.get("insuranceObligation").isEmpty())
//            builder.with("insuranceObligation", ":", searchParam.get("insuranceObligation"), false);
//
//
//        if(searchParam.get("typeOfCollateral").isEmpty()){
//
//            Specification<PledgeSubject> spec = builder.build();
//
//            return repositoryPledgeSubject.findAll(spec, pageable);
//
//        }else if(searchParam.get("typeOfCollateral").equals(TypeOfCollateral.AUTO.name())){
//
//            if(!searchParam.get("brandAuto").isEmpty())
//                builder.with("brandAuto", ":", searchParam.get("brandAuto"), false);
//            if(!searchParam.get("modelAuto").isEmpty())
//                builder.with("modelAuto", ":", searchParam.get("modelAuto"), false);
//            if(!searchParam.get("vin").isEmpty())
//                builder.with("vin", ":", searchParam.get("vin"), false);
//            if(!searchParam.get("numOfEngine").isEmpty())
//                builder.with("numOfEngine", ":", searchParam.get("numOfEngine"), false);
//            if(!searchParam.get("numOfPts").isEmpty())
//                builder.with("numOfPTS", ":", searchParam.get("numOfPts"), false);
//            if(!searchParam.get("yearOfManufactureAuto").isEmpty())
//                builder.with("yearOfManufactureAuto", searchParam.get("yearOfManufactureAutoOption"), searchParam.get("yearOfManufactureAuto"), false);
//            if(!searchParam.get("inventoryNumAuto").isEmpty())
//                builder.with("inventoryNumAuto", ":", searchParam.get("inventoryNumAuto"), false);
//            if(!searchParam.get("typeOfAuto").isEmpty())
//                builder.with("typeOfAuto", ":", TypeOfAuto.valueOf(searchParam.get("typeOfAuto")), false);
//
//            Specification<PledgeSubjectAuto> spec = builder.build();
//
//            return repositoryPledgeSubjectAuto.findAll(spec, pageable);
//
//        }else if(searchParam.get("typeOfCollateral").equals(TypeOfCollateral.EQUIPMENT.name())){
//
//            if(!searchParam.get("brandEquip").isEmpty())
//                builder.with("brandAuto", ":", searchParam.get("brandEquip"), false);
//            if(!searchParam.get("modelEquip").isEmpty())
//                builder.with("modelAuto", ":", searchParam.get("modelEquip"), false);
//            if(!searchParam.get("SerialNum").isEmpty())
//                builder.with("serialNum", ":", searchParam.get("SerialNum"), false);
//            if(!searchParam.get("yearOfManufactureEquip").isEmpty())
//                builder.with("yearOfManufactureAuto", searchParam.get("yearOfManufactureEquipOption"), searchParam.get("yearOfManufactureEquip"), false);
//            if(!searchParam.get("inventoryNumEquip").isEmpty())
//                builder.with("inventoryNumAuto", ":", searchParam.get("inventoryNumEquip"), false);
//            if(!searchParam.get("typeOfEquip").isEmpty())
//                builder.with("typeOfEquipment", ":", TypeOfEquip.valueOf(searchParam.get("typeOfEquip")), false);
//
//            Specification<PledgeSubjectEquipment> spec = builder.build();
//
//            return repositoryPledgeSubjectEquipment.findAll(spec, pageable);
//
//        }else if(searchParam.get("typeOfCollateral").equals(TypeOfCollateral.TBO.name())){
//
//            if(!searchParam.get("carryingAmount").isEmpty())
//                builder.with("carryingAmount", searchParam.get("carryingAmountOption"), searchParam.get("carryingAmount"), false);
//            if(!searchParam.get("typeOfTbo").isEmpty())
//                builder.with("typeOfTBO", ":", TypeOfTBO.valueOf(searchParam.get("typeOfTbo")), false);
//
//            Specification<PledgeSubjectTBO> spec = builder.build();
//
//            return repositoryPledgeSubjectTBO.findAll(spec, pageable);
//
//        }else if(searchParam.get("typeOfCollateral").equals(TypeOfCollateral.SECURITIES.name())){
//
//            if(!searchParam.get("nominalValue").isEmpty())
//                builder.with("nominalValue", searchParam.get("nominalValueOption"), searchParam.get("nominalValue"), false);
//            if(!searchParam.get("actuaValue").isEmpty())
//                builder.with("actualValue", searchParam.get("actuaValueOption"), searchParam.get("actuaValue"), false);
//            if(!searchParam.get("typeOfSecurities").isEmpty()) {
//                builder.with("typeOfSecurities", ":", TypeOfSecurities.valueOf(searchParam.get("typeOfSecurities")), false);
//            }
//
//            Specification<PledgeSubjectSecurities> spec = builder.build();
//
//            return repositoryPledgeSubjectSecurities.findAll(spec, pageable);
//
//        }else if(searchParam.get("typeOfCollateral").equals(TypeOfCollateral.LAND_OWNERSHIP.name())){
//
//            if(!searchParam.get("CadastralNum").isEmpty())
//                builder.with("cadastralNum", ":", searchParam.get("CadastralNum"), false);
//            if(!searchParam.get("area").isEmpty())
//                builder.with("area", searchParam.get("areaOption"), searchParam.get("area"), false);
//            if(!searchParam.get("ConditionalNum").isEmpty())
//                builder.with("conditionalNum", ":", searchParam.get("ConditionalNum"), false);
//            if(!searchParam.get("landCategory").isEmpty()) {
//                LandCategory landCategory = repositoryLandCategory.getOne(Integer.parseInt(searchParam.get("landCategory")));
//                builder.with("landCategory", ":", landCategory, false);
//            }
//            if(!searchParam.get("permittedUse").isEmpty())
//                builder.with("permittedUse", ":", searchParam.get("permittedUse"), false);
//            if(!searchParam.get("builtUp").isEmpty())
//                builder.with("builtUp", ":", searchParam.get("builtUp"), false);
//            if(!searchParam.get("cadastralNumOfBuilding").isEmpty())
//                builder.with("cadastralNumOfBuilding", ":", searchParam.get("cadastralNumOfBuilding"), false);
//
//            Specification<PledgeSubjectLandOwnership> spec = builder.build();
//
//            return repositoryPledgeSubjectRealtyLandOwnership.findAll(spec, pageable);
//
//        }else if(searchParam.get("typeOfCollateral").equals(TypeOfCollateral.LAND_LEASE.name())){
//
//            if(!searchParam.get("CadastralNum").isEmpty())
//                builder.with("cadastralNum", ":", searchParam.get("CadastralNum"), false);
//            if(!searchParam.get("area").isEmpty())
//                builder.with("area", searchParam.get("areaOption"), searchParam.get("area"), false);
//            if(!searchParam.get("ConditionalNum").isEmpty())
//                builder.with("conditionalNum", ":", searchParam.get("ConditionalNum"), false);
//            if(!searchParam.get("landCategory").isEmpty()) {
//                LandCategory landCategory = repositoryLandCategory.getOne(Integer.parseInt(searchParam.get("landCategory")));
//                builder.with("landCategory", ":", landCategory, false);
//                }
//            if(!searchParam.get("permittedUse").isEmpty())
//                builder.with("permittedUse", ":", searchParam.get("permittedUse"), false);
//            if(!searchParam.get("builtUp").isEmpty())
//                builder.with("builtUp", ":", searchParam.get("builtUp"), false);
//            if(!searchParam.get("cadastralNumOfBuilding").isEmpty())
//                builder.with("cadastralNumOfBuilding", ":", searchParam.get("cadastralNumOfBuilding"), false);
//            if(!searchParam.get("beginDateLease").isEmpty()){
//                try {
//                    Date date = simpleDateFormat.parse(searchParam.get("beginDateLease"));
//                    builder.with("dateBeginLease", searchParam.get("beginDateLeaseOption"), date, false);
//                }catch (ParseException e){
//                        System.out.println("Не верный фортат dateEndPA");
//                        e.printStackTrace();
//                }
//            }
//            if(!searchParam.get("endDateLease").isEmpty()){
//                try {
//                    Date date = simpleDateFormat.parse(searchParam.get("endDateLease"));
//                    builder.with("dateEndLease", searchParam.get("endDateLeaseOption"), date, false);
//                }catch (ParseException e){
//                    System.out.println("Не верный фортат dateEndPA");
//                    e.printStackTrace();
//                }
//            }
//
//            Specification<PledgeSubjectLandLease> spec = builder.build();
//
//            return repositoryPledgeSubjectRealtyLandLease.findAll(spec, pageable);
//
//
//        }else if(searchParam.get("typeOfCollateral").equals(TypeOfCollateral.BUILDING.name())){
//
//            if(!searchParam.get("CadastralNum").isEmpty())
//                builder.with("cadastralNum", ":", searchParam.get("CadastralNum"), false);
//            if(!searchParam.get("area").isEmpty())
//                builder.with("area", searchParam.get("areaOption"), searchParam.get("area"), false);
//            if(!searchParam.get("ConditionalNum").isEmpty())
//                builder.with("conditionalNum", ":", searchParam.get("ConditionalNum"), false);
//            if(!searchParam.get("marketSegmentBuilding").isEmpty()) {
//                MarketSegment marketSegment  = repositoryMarketSegment.findByName(searchParam.get("marketSegmentBuilding"));
//                builder.with("marketSegment", ":", marketSegment, false);
//            }
//            if(!searchParam.get("readinessВegree").isEmpty())
//                builder.with("readinessDegree", searchParam.get("readinessВegreeOption"), searchParam.get("readinessВegree"), false);
//            if(!searchParam.get("yearOfConstructionBuilding").isEmpty())
//                builder.with("yearOfConstruction", searchParam.get("yearOfConstructionBuildingOption"), searchParam.get("yearOfConstructionBuilding"), false);
//
//            Specification<PledgeSubjectBuilding> spec= builder.build();
//
//            return repositoryPledgeSubjectRealtyBuilding.findAll(spec, pageable);
//
//        }else if(searchParam.get("typeOfCollateral").equals(TypeOfCollateral.PREMISE.name())){
//
//            if(!searchParam.get("CadastralNum").isEmpty())
//                builder.with("cadastralNum", ":", searchParam.get("CadastralNum"), false);
//            if(!searchParam.get("area").isEmpty())
//                builder.with("area", searchParam.get("areaOption"), searchParam.get("area"), false);
//            if(!searchParam.get("ConditionalNum").isEmpty())
//                builder.with("conditionalNum", ":", searchParam.get("ConditionalNum"), false);
//            if(!searchParam.get("marketSegmentRoom").isEmpty()) {
//                MarketSegment marketSegment  = repositoryMarketSegment.findByName(searchParam.get("marketSegmentRoom"));
//                builder.with("marketSegmentRoom", ":", marketSegment, false);
//            }
//            if(!searchParam.get("marketSegmentBuilding").isEmpty()) {
//                MarketSegment marketSegment  = repositoryMarketSegment.findByName(searchParam.get("marketSegmentBuilding"));
//                builder.with("marketSegmentBuilding", ":", marketSegment, false);
//            }
//            if(!searchParam.get("floorLocation").isEmpty())
//                builder.with("floorLocation", searchParam.get("floorLocationOption"), searchParam.get("floorLocation"), false);
//
//            Specification<PledgeSubjectRoom> spec= builder.build();
//
//            return repositoryPledgeSubjectRealtyRoom.findAll(spec, pageable);
//
//        }else if(searchParam.get("typeOfCollateral").equals(TypeOfCollateral.VESSEL.name())){
//
//            if(!searchParam.get("imo").isEmpty())
//                builder.with("imo", ":", searchParam.get("imo"), false);
//            if(!searchParam.get("typeOfVessel").isEmpty())
//                builder.with("vesselType", ":", TypeOfVessel.valueOf(searchParam.get("typeOfVessel")), false);
//            if(!searchParam.get("grossTonnage").isEmpty())
//                builder.with("grossTonnage", searchParam.get("grossTonnageOption"), searchParam.get("grossTonnage"), false);
//            if(!searchParam.get("deadweight").isEmpty())
//                builder.with("deadweight", searchParam.get("deadweightOption"), searchParam.get("deadweight"), false);
//            if(!searchParam.get("yearBuiltVessel").isEmpty())
//                builder.with("yearBuilt", searchParam.get("yearBuiltVesselOption"), searchParam.get("yearBuiltVessel"), false);
//
//            Specification<PledgeSubjectVessel> spec = builder.build();
//
//            return repositoryPledgeSubjectVessel.findAll(spec, pageable);
//
//        }else{
//
//            return new PageImpl<PledgeSubject>(new ArrayList<>());
//        }

        return null;
    }

    @Transactional
    public PledgeSubject updatePledgeSubject(PledgeSubject pledgeSubject) {
        return repositoryPledgeSubject.save(pledgeSubject);
    }

    @Transactional
    public PledgeSubject insertPledgeSubject(PledgeSubject pledgeSubject, CostHistory costHistory, Monitoring monitoring){

        pledgeSubject = repositoryPledgeSubject.save(pledgeSubject);

        costHistory.setPledgeSubject(pledgeSubject);
        repositoryCostHistory.save(costHistory);

        monitoring.setPledgeSubject(pledgeSubject);
        repositoryMonitoring.save(monitoring);

        return pledgeSubject;
    }


    public List<PledgeSubject> getPledgeSubjectByCadastralNum(String cadastralNum){
        List<PledgeSubject> pledgeSubjectList = new ArrayList<>();
        pledgeSubjectList.addAll(repositoryPledgeSubject.findByPledgeSubjectBuildingCadastralNumContainingIgnoreCase(cadastralNum));
        pledgeSubjectList.addAll(repositoryPledgeSubject.findByPledgeSubjectLandLeaseCadastralNumContainingIgnoreCase(cadastralNum));
        pledgeSubjectList.addAll(repositoryPledgeSubject.findByPledgeSubjectLandOwnershipCadastralNumContainingIgnoreCase(cadastralNum));
        pledgeSubjectList.addAll(repositoryPledgeSubject.findByPledgeSubjectRoomCadastralNumContainingIgnoreCase(cadastralNum));

        return pledgeSubjectList;
    }

    public List<PledgeSubject> getPledgeSubjectByName(String name){
        return repositoryPledgeSubject.findAllByNameContainingIgnoreCase(name);
    }
}
