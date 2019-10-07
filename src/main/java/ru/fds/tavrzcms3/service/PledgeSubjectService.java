package ru.fds.tavrzcms3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fds.tavrzcms3.domain.*;
import ru.fds.tavrzcms3.repository.*;
import ru.fds.tavrzcms3.specification.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class PledgeSubjectService {

    @Autowired
    RepositoryPledgeSubject repositoryPledgeSubject;
    @Autowired
    RepositoryPledgeSubjectAuto repositoryPledgeSubjectAuto;
    @Autowired
    RepositoryPledgeSubjectEquipment repositoryPledgeSubjectEquipment;
    @Autowired
    RepositoryPledgeSubjectRealty repositoryPledgeSubjectRealty;
    @Autowired
    RepositoryPledgeSubjectRealtyBuilding repositoryPledgeSubjectRealtyBuilding;
    @Autowired
    RepositoryPledgeSubjectRealtyLandLease repositoryPledgeSubjectRealtyLandLease;
    @Autowired
    RepositoryPledgeSubjectRealtyLandOwnership repositoryPledgeSubjectRealtyLandOwnership;
    @Autowired
    RepositoryPledgeSubjectRealtyRoom repositoryPledgeSubjectRealtyRoom;
    @Autowired
    RepositoryPledgeSubjectSecurities repositoryPledgeSubjectSecurities;
    @Autowired
    RepositoryPledgeSubjectTBO repositoryPledgeSubjectTBO;
    @Autowired
    RepositoryPledgeSubjectVessel repositoryPledgeSubjectVessel;
    @Autowired
    RepositoryLandCategory repositoryLandCategory;
    @Autowired
    RepositoryMarketSegment repositoryMarketSegment;
    @Autowired
    RepositoryPledgeAgreement repositoryPledgeAgreement;
    @Autowired
    RepositoryCostHistory repositoryCostHistory;
    @Autowired
    RepositoryMonitoring repositoryMonitoring;
    @Autowired
    RepositoryEncumbrance repositoryEncumbrance;
    @Autowired
    RepositoryInsurance repositoryInsurance;

    public PledgeSubject getPledgeSubjectById(long id){
        PledgeSubject pledgeSubject = repositoryPledgeSubject.findByPledgeSubjectId(id);
        return  pledgeSubject;
    }

    public List<PledgeSubject> getPledgeSubjectsForPledgeAgreement(long pledgeAgreementId){
        PledgeAgreement pledgeAgreement = repositoryPledgeAgreement.getOne(pledgeAgreementId);
        return repositoryPledgeSubject.findByPledgeAgreements(pledgeAgreement);
    }

    public Page<PledgeSubject> getPledgeSubjectsFromSearch(Map<String, String> searchParam){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy-MM-dd");

        int currentPage = Integer.parseInt(searchParam.get("page"));
        int pageSize = Integer.parseInt(searchParam.get("size"));
        Pageable pageable = PageRequest.of(currentPage, pageSize);

        SpecificationBuilder builder = new SpecificationBuilderImpl();

        if(!searchParam.get("namePS").isEmpty())
            builder.with("name", ":", searchParam.get("namePS"), false);
        if(!searchParam.get("liquidity").isEmpty())
            builder.with("liquidity", ":", searchParam.get("liquidity"), false);
        if(!searchParam.get("rsDZ").isEmpty())
            builder.with("rsDz", searchParam.get("rsDZOption"), searchParam.get("rsDZ"), false);
        if(!searchParam.get("zsDZ").isEmpty())
            builder.with("zsDz", searchParam.get("zsDZOption"), searchParam.get("zsDZ"), false);
        if(!searchParam.get("rsZZ").isEmpty())
            builder.with("rsZz", searchParam.get("rsZZOption"), searchParam.get("rsZZ"), false);
        if(!searchParam.get("zsZZ").isEmpty())
            builder.with("zsZz", searchParam.get("zsZZOption"), searchParam.get("zsZZ"), false);
        if(!searchParam.get("ss").isEmpty())
            builder.with("ss", searchParam.get("ssOption"), searchParam.get("ss"), false);
        if(!searchParam.get("dateMonitoring").isEmpty()){
            try {
                Date date = simpleDateFormat.parse(searchParam.get("dateMonitoring"));
                builder.with("dateMonitoring", searchParam.get("dateMonitoringOption"), date, false);
            }catch (ParseException e){
                System.out.println("Не верный фортат dateEndPA");
                e.printStackTrace();
            }
        }
        if(!searchParam.get("dateConclusion").isEmpty()){
            try {
                Date date = simpleDateFormat.parse(searchParam.get("dateConclusion"));
                builder.with("dateConclusion", searchParam.get("dateConclusionOption"), date, false);
            }catch (ParseException e){
                System.out.println("Не верный фортат dateEndPA");
                e.printStackTrace();
            }
        }
        if(!searchParam.get("resultOfMonitoring").isEmpty())
            builder.with("statusMonitoring", ":", searchParam.get("resultOfMonitoring"), false);
        if(!searchParam.get("typeOfPledge").isEmpty())
            builder.with("typeOfPledge", ":", searchParam.get("typeOfPledge"), false);
        if(!searchParam.get("adressRegion").isEmpty())
            builder.with("adressRegion", ":", searchParam.get("adressRegion"), false);
        if(!searchParam.get("adressDistrict").isEmpty())
            builder.with("adressDistrict", ":", searchParam.get("adressDistrict"), false);
        if(!searchParam.get("adressCity").isEmpty())
            builder.with("adressCity", ":", searchParam.get("adressCity"), false);
        if(!searchParam.get("adressStreet").isEmpty())
            builder.with("adressStreet", ":", searchParam.get("adressStreet"), false);
        if(!searchParam.get("insuranceObligation").isEmpty())
            builder.with("insuranceObligation", ":", searchParam.get("insuranceObligation"), false);


        if(searchParam.get("typeOfCollateral").isEmpty()){

            Specification<PledgeSubject> spec = builder.build();

            return repositoryPledgeSubject.findAll(spec, pageable);

        }else if(searchParam.get("typeOfCollateral").equals("auto")){

            if(!searchParam.get("brandAuto").isEmpty())
                builder.with("brand", ":", searchParam.get("brandAuto"), false);
            if(!searchParam.get("modelAuto").isEmpty())
                builder.with("model", ":", searchParam.get("modelAuto"), false);
            if(!searchParam.get("vin").isEmpty())
                builder.with("vin", ":", searchParam.get("vin"), false);
            if(!searchParam.get("numOfEngine").isEmpty())
                builder.with("numOfEngine", ":", searchParam.get("numOfEngine"), false);
            if(!searchParam.get("numOfPts").isEmpty())
                builder.with("numOfPTS", ":", searchParam.get("numOfPts"), false);
            if(!searchParam.get("yearOfManufactureAuto").isEmpty())
                builder.with("yearOfManufacture", searchParam.get("yearOfManufactureAutoOption"), searchParam.get("yearOfManufactureAuto"), false);
            if(!searchParam.get("inventoryNumAuto").isEmpty())
                builder.with("inventoryNum", ":", searchParam.get("inventoryNumAuto"), false);
            if(!searchParam.get("typeOfAuto").isEmpty())
                builder.with("typeOfAuto", ":", searchParam.get("typeOfAuto"), false);

            Specification<PledgeSubjectAuto> spec = builder.build();

            return repositoryPledgeSubjectAuto.findAll(spec, pageable);

        }else if(searchParam.get("typeOfCollateral").equals("equip")){

            if(!searchParam.get("brandEquip").isEmpty())
                builder.with("brand", ":", searchParam.get("brandEquip"), false);
            if(!searchParam.get("modelEquip").isEmpty())
                builder.with("model", ":", searchParam.get("modelEquip"), false);
            if(!searchParam.get("SerialNum").isEmpty())
                builder.with("serialNum", ":", searchParam.get("SerialNum"), false);
            if(!searchParam.get("yearOfManufactureEquip").isEmpty())
                builder.with("yearOfManufacture", searchParam.get("yearOfManufactureEquipOption"), searchParam.get("yearOfManufactureEquip"), false);
            if(!searchParam.get("inventoryNumEquip").isEmpty())
                builder.with("inventoryNum", ":", searchParam.get("inventoryNumEquip"), false);
            if(!searchParam.get("typeOfEquip").isEmpty())
                builder.with("typeOfquipment", ":", searchParam.get("typeOfEquip"), false);

            Specification<PledgeSubjectEquipment> spec = builder.build();

            return repositoryPledgeSubjectEquipment.findAll(spec, pageable);

        }else if(searchParam.get("typeOfCollateral").equals("tbo")){

            if(!searchParam.get("carryingAmount").isEmpty())
                builder.with("carryingAmount", searchParam.get("carryingAmountOption"), searchParam.get("carryingAmount"), false);
            if(!searchParam.get("typeOfTbo").isEmpty())
                builder.with("typeOfTBO", ":", searchParam.get("typeOfTbo"), false);

            Specification<PledgeSubjectTBO> spec = builder.build();

            return repositoryPledgeSubjectTBO.findAll(spec, pageable);

        }else if(searchParam.get("typeOfCollateral").equals("securities")){

            if(!searchParam.get("nominalValue").isEmpty())
                builder.with("nominalValue", searchParam.get("nominalValueOption"), searchParam.get("nominalValue"), false);
            if(!searchParam.get("actuaValue").isEmpty())
                builder.with("actualValue", searchParam.get("actuaValueOption"), searchParam.get("actuaValue"), false);
            if(!searchParam.get("typeOfSecurities").isEmpty())
                builder.with("typeOfSecurities", ":", searchParam.get("typeOfSecurities"), false);

            Specification<PledgeSubjectSecurities> spec = builder.build();

            return repositoryPledgeSubjectSecurities.findAll(spec, pageable);

        }else if(searchParam.get("typeOfCollateral").equals("landOwn")){

            if(!searchParam.get("CadastralNum").isEmpty())
                builder.with("cadastralNum", ":", searchParam.get("CadastralNum"), false);
            if(!searchParam.get("area").isEmpty())
                builder.with("area", searchParam.get("areaOption"), searchParam.get("area"), false);
            if(!searchParam.get("ConditionalNum").isEmpty())
                builder.with("conditionalNum", ":", searchParam.get("ConditionalNum"), false);
            if(!searchParam.get("landCategory").isEmpty()) {
                LandCategory landCategory = repositoryLandCategory.getOne(Integer.parseInt(searchParam.get("landCategory")));
                builder.with("landCategory", ":", landCategory, false);
            }
            if(!searchParam.get("permittedUse").isEmpty())
                builder.with("permittedUse", ":", searchParam.get("permittedUse"), false);
            if(!searchParam.get("builtUp").isEmpty())
                builder.with("builtUp", ":", searchParam.get("builtUp"), false);
            if(!searchParam.get("cadastralNumOfBuilding").isEmpty())
                builder.with("cadastralNumOfBuilding", ":", searchParam.get("cadastralNumOfBuilding"), false);

            Specification<PledgeSubjectRealtyLandOwnership> spec = builder.build();

            return repositoryPledgeSubjectRealtyLandOwnership.findAll(spec, pageable);

        }else if(searchParam.get("typeOfCollateral").equals("landLease")){

            if(!searchParam.get("CadastralNum").isEmpty())
                builder.with("cadastralNum", ":", searchParam.get("CadastralNum"), false);
            if(!searchParam.get("area").isEmpty())
                builder.with("area", searchParam.get("areaOption"), searchParam.get("area"), false);
            if(!searchParam.get("ConditionalNum").isEmpty())
                builder.with("conditionalNum", ":", searchParam.get("ConditionalNum"), false);
            if(!searchParam.get("landCategory").isEmpty()) {
                LandCategory landCategory = repositoryLandCategory.getOne(Integer.parseInt(searchParam.get("landCategory")));
                builder.with("landCategory", ":", landCategory, false);
                }
            if(!searchParam.get("permittedUse").isEmpty())
                builder.with("permittedUse", ":", searchParam.get("permittedUse"), false);
            if(!searchParam.get("builtUp").isEmpty())
                builder.with("builtUp", ":", searchParam.get("builtUp"), false);
            if(!searchParam.get("cadastralNumOfBuilding").isEmpty())
                builder.with("cadastralNumOfBuilding", ":", searchParam.get("cadastralNumOfBuilding"), false);
            if(!searchParam.get("beginDateLease").isEmpty()){
                try {
                    Date date = simpleDateFormat.parse(searchParam.get("beginDateLease"));
                    builder.with("dateBeginLease", searchParam.get("beginDateLeaseOption"), date, false);
                }catch (ParseException e){
                        System.out.println("Не верный фортат dateEndPA");
                        e.printStackTrace();
                }
            }
            if(!searchParam.get("endDateLease").isEmpty()){
                try {
                    Date date = simpleDateFormat.parse(searchParam.get("endDateLease"));
                    builder.with("dateEndLease", searchParam.get("endDateLeaseOption"), date, false);
                }catch (ParseException e){
                    System.out.println("Не верный фортат dateEndPA");
                    e.printStackTrace();
                }
            }

            Specification<PledgeSubjectRealtyLandLease> spec = builder.build();

            return repositoryPledgeSubjectRealtyLandLease.findAll(spec, pageable);


        }else if(searchParam.get("typeOfCollateral").equals("build")){

            if(!searchParam.get("CadastralNum").isEmpty())
                builder.with("cadastralNum", ":", searchParam.get("CadastralNum"), false);
            if(!searchParam.get("area").isEmpty())
                builder.with("area", searchParam.get("areaOption"), searchParam.get("area"), false);
            if(!searchParam.get("ConditionalNum").isEmpty())
                builder.with("conditionalNum", ":", searchParam.get("ConditionalNum"), false);
            if(!searchParam.get("marketSegmentBuilding").isEmpty()) {
                MarketSegment marketSegment  = repositoryMarketSegment.findByName(searchParam.get("marketSegmentBuilding"));
                builder.with("marketSegment", ":", marketSegment, false);
            }
            if(!searchParam.get("readinessВegree").isEmpty())
                builder.with("readinessDegree", searchParam.get("readinessВegreeOption"), searchParam.get("readinessВegree"), false);
            if(!searchParam.get("yearOfConstructionBuilding").isEmpty())
                builder.with("yearOfConstruction", searchParam.get("yearOfConstructionBuildingOption"), searchParam.get("yearOfConstructionBuilding"), false);

            Specification<PledgeSubjectRealtyBuilding> spec= builder.build();

            return repositoryPledgeSubjectRealtyBuilding.findAll(spec, pageable);

        }else if(searchParam.get("typeOfCollateral").equals("room")){

            if(!searchParam.get("CadastralNum").isEmpty())
                builder.with("cadastralNum", ":", searchParam.get("CadastralNum"), false);
            if(!searchParam.get("area").isEmpty())
                builder.with("area", searchParam.get("areaOption"), searchParam.get("area"), false);
            if(!searchParam.get("ConditionalNum").isEmpty())
                builder.with("conditionalNum", ":", searchParam.get("ConditionalNum"), false);
            if(!searchParam.get("marketSegmentRoom").isEmpty()) {
                MarketSegment marketSegment  = repositoryMarketSegment.findByName(searchParam.get("marketSegmentRoom"));
                builder.with("marketSegmentRoom", ":", marketSegment, false);
            }
            if(!searchParam.get("marketSegmentBuilding").isEmpty()) {
                MarketSegment marketSegment  = repositoryMarketSegment.findByName(searchParam.get("marketSegmentBuilding"));
                builder.with("marketSegmentBuilding", ":", marketSegment, false);
            }
            if(!searchParam.get("floorLocation").isEmpty())
                builder.with("floorLocation", searchParam.get("floorLocationOption"), searchParam.get("floorLocation"), false);

            Specification<PledgeSubjectRealtyRoom> spec= builder.build();

            return repositoryPledgeSubjectRealtyRoom.findAll(spec, pageable);

        }else if(searchParam.get("typeOfCollateral").equals("vessel")){

            if(!searchParam.get("imo").isEmpty())
                builder.with("imo", ":", searchParam.get("imo"), false);
            if(!searchParam.get("typeOfVessel").isEmpty())
                builder.with("vesselType", ":", searchParam.get("typeOfVessel"), false);
            if(!searchParam.get("grossTonnage").isEmpty())
                builder.with("grossTonnage", searchParam.get("grossTonnageOption"), searchParam.get("grossTonnage"), false);
            if(!searchParam.get("deadweight").isEmpty())
                builder.with("deadweight", searchParam.get("deadweightOption"), searchParam.get("deadweight"), false);
            if(!searchParam.get("yearBuiltVessel").isEmpty())
                builder.with("yearBuilt", searchParam.get("yearBuiltVesselOption"), searchParam.get("yearBuiltVessel"), false);

            Specification<PledgeSubjectVessel> spec = builder.build();

            return repositoryPledgeSubjectVessel.findAll(spec, pageable);

        }else{

            return  null;
        }
    }

    @Transactional
    public PledgeSubject updatePledgeSubject(PledgeSubject pledgeSubject) {

        if (pledgeSubject.getClass() == PledgeSubjectAuto.class)
            return repositoryPledgeSubjectAuto.save((PledgeSubjectAuto) pledgeSubject);
        else if (pledgeSubject.getClass() == PledgeSubjectEquipment.class)
            return repositoryPledgeSubjectEquipment.save((PledgeSubjectEquipment) pledgeSubject);
        else if (pledgeSubject.getClass() == PledgeSubjectRealtyBuilding.class)
            return repositoryPledgeSubjectRealtyBuilding.save((PledgeSubjectRealtyBuilding) pledgeSubject);
        else if (pledgeSubject.getClass() == PledgeSubjectRealtyLandLease.class)
            return repositoryPledgeSubjectRealtyLandLease.save((PledgeSubjectRealtyLandLease) pledgeSubject);
        else if (pledgeSubject.getClass() == PledgeSubjectRealtyLandOwnership.class)
            return repositoryPledgeSubjectRealtyLandOwnership.save((PledgeSubjectRealtyLandOwnership) pledgeSubject);
        else if (pledgeSubject.getClass() == PledgeSubjectRealtyRoom.class)
            return repositoryPledgeSubjectRealtyRoom.save((PledgeSubjectRealtyRoom) pledgeSubject);
        else if (pledgeSubject.getClass() == PledgeSubjectSecurities.class)
            return repositoryPledgeSubjectSecurities.save((PledgeSubjectSecurities) pledgeSubject);
        else if (pledgeSubject.getClass() == PledgeSubjectTBO.class)
            return repositoryPledgeSubjectTBO.save((PledgeSubjectTBO) pledgeSubject);
        else if (pledgeSubject.getClass() == PledgeSubjectVessel.class)
            return repositoryPledgeSubjectVessel.save((PledgeSubjectVessel) pledgeSubject);
        else
            return null;
    }

    @Transactional
    public PledgeSubject insertPledgeSubject(PledgeSubject pledgeSubject, CostHistory costHistory, Monitoring monitoring, Encumbrance encumbrance){
        pledgeSubject.setZsDz(costHistory.getZsDz());
        pledgeSubject.setZsZz(costHistory.getZsZz());
        pledgeSubject.setRsDz(costHistory.getRsDz());
        pledgeSubject.setRsZz(costHistory.getRsZz());
        pledgeSubject.setSs(costHistory.getSs());
        pledgeSubject.setDateConclusion(costHistory.getDateConclusion());
        pledgeSubject.setDateMonitoring(monitoring.getDateMonitoring());
        pledgeSubject.setStatusMonitoring(monitoring.getStatusMonitoring());
        pledgeSubject.setTypeOfMonitoring(monitoring.getTypeOfMonitoring());

        if (pledgeSubject.getClass() == PledgeSubjectAuto.class)
            pledgeSubject = repositoryPledgeSubjectAuto.save((PledgeSubjectAuto) pledgeSubject);
        else if (pledgeSubject.getClass() == PledgeSubjectEquipment.class)
            pledgeSubject = repositoryPledgeSubjectEquipment.save((PledgeSubjectEquipment) pledgeSubject);
        else if (pledgeSubject.getClass() == PledgeSubjectRealtyBuilding.class)
            pledgeSubject = repositoryPledgeSubjectRealtyBuilding.save((PledgeSubjectRealtyBuilding) pledgeSubject);
        else if (pledgeSubject.getClass() == PledgeSubjectRealtyLandLease.class)
            pledgeSubject = repositoryPledgeSubjectRealtyLandLease.save((PledgeSubjectRealtyLandLease) pledgeSubject);
        else if (pledgeSubject.getClass() == PledgeSubjectRealtyLandOwnership.class)
            pledgeSubject = repositoryPledgeSubjectRealtyLandOwnership.save((PledgeSubjectRealtyLandOwnership) pledgeSubject);
        else if (pledgeSubject.getClass() == PledgeSubjectRealtyRoom.class)
            pledgeSubject = repositoryPledgeSubjectRealtyRoom.save((PledgeSubjectRealtyRoom) pledgeSubject);
        else if (pledgeSubject.getClass() == PledgeSubjectSecurities.class)
            pledgeSubject = repositoryPledgeSubjectSecurities.save((PledgeSubjectSecurities) pledgeSubject);
        else if (pledgeSubject.getClass() == PledgeSubjectTBO.class)
            pledgeSubject = repositoryPledgeSubjectTBO.save((PledgeSubjectTBO) pledgeSubject);
        else if (pledgeSubject.getClass() == PledgeSubjectVessel.class)
            pledgeSubject = repositoryPledgeSubjectVessel.save((PledgeSubjectVessel) pledgeSubject);

        costHistory.setPledgeSubject(pledgeSubject);
        repositoryCostHistory.save(costHistory);
        monitoring.setPledgeSubject(pledgeSubject);
        repositoryMonitoring.save(monitoring);
        encumbrance.setPledgeSubject(pledgeSubject);
        repositoryEncumbrance.save(encumbrance);

        return pledgeSubject;
    }

    public List<PledgeSubject> getPledgeSubjectByCadastralNum(String cadastralNum){
        return repositoryPledgeSubjectRealty.findAllByCadastralNumContainingIgnoreCase(cadastralNum);
    }

    public List<PledgeSubject> getPlegeSubjectByName(String name){
        return repositoryPledgeSubject.findAllByNameContainingIgnoreCase(name);
    }
}
