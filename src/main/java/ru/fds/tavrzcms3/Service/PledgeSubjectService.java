package ru.fds.tavrzcms3.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.fds.tavrzcms3.domain.*;
import ru.fds.tavrzcms3.repository.*;
import ru.fds.tavrzcms3.specification.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    public synchronized PledgeSubject getPledgeSubjectById(long id){
        PledgeSubject pledgeSubject = repositoryPledgeSubject.findByPledgeSubjectId(id);
        return  pledgeSubject;
    }

    public synchronized List<PledgeSubject> getPledgeSubjectsForPledgeAgreement(long pledgeAgreementId){
        PledgeAgreement pledgeAgreement = repositoryPledgeAgreement.getOne(pledgeAgreementId);
        return repositoryPledgeSubject.findByPledgeAgreements(pledgeAgreement);
    }

    public synchronized List<PledgeSubject> getPledgeSubjectsFromSearch(Map<String, String> searchParam){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy-MM-dd");

        if(searchParam.get("typeOfCollateral").isEmpty()){
            PledgeSubjectSpecificationsBuilder builder = new PledgeSubjectSpecificationsBuilder();
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

            Specification<PledgeSubject> spec = builder.build();

            return repositoryPledgeSubject.findAll(spec);

        }else {
            switch (searchParam.get("typeOfCollateral")){
                case "auto":
                    PledgeSubjectAutoSpecificationBuilder builderAuto = new PledgeSubjectAutoSpecificationBuilder();
                    if(!searchParam.get("namePS").isEmpty())
                        builderAuto.with("name", ":", searchParam.get("namePS"), false);
                    if(!searchParam.get("liquidity").isEmpty())
                        builderAuto.with("liquidity", ":", searchParam.get("liquidity"), false);
                    if(!searchParam.get("rsDZ").isEmpty())
                        builderAuto.with("rsDz", searchParam.get("rsDZOption"), searchParam.get("rsDZ"), false);
                    if(!searchParam.get("zsDZ").isEmpty())
                        builderAuto.with("zsDz", searchParam.get("zsDZOption"), searchParam.get("zsDZ"), false);
                    if(!searchParam.get("rsZZ").isEmpty())
                        builderAuto.with("rsZz", searchParam.get("rsZZOption"), searchParam.get("rsZZ"), false);
                    if(!searchParam.get("zsZZ").isEmpty())
                        builderAuto.with("zsZz", searchParam.get("zsZZOption"), searchParam.get("zsZZ"), false);
                    if(!searchParam.get("ss").isEmpty())
                        builderAuto.with("ss", searchParam.get("ssOption"), searchParam.get("ss"), false);
                    if(!searchParam.get("dateMonitoring").isEmpty()){
                        try {
                            Date date = simpleDateFormat.parse(searchParam.get("dateMonitoring"));
                            builderAuto.with("dateMonitoring", searchParam.get("dateMonitoringOption"), date, false);
                        }catch (ParseException e){
                            System.out.println("Не верный фортат dateEndPA");
                            e.printStackTrace();
                        }
                    }
                    if(!searchParam.get("dateConclusion").isEmpty()){
                        try {
                            Date date = simpleDateFormat.parse(searchParam.get("dateConclusion"));
                            builderAuto.with("dateConclusion", searchParam.get("dateConclusionOption"), date, false);
                        }catch (ParseException e){
                            System.out.println("Не верный фортат dateEndPA");
                            e.printStackTrace();
                        }
                    }
                    if(!searchParam.get("resultOfMonitoring").isEmpty())
                        builderAuto.with("statusMonitoring", ":", searchParam.get("resultOfMonitoring"), false);
                    if(!searchParam.get("typeOfPledge").isEmpty())
                        builderAuto.with("typeOfPledge", ":", searchParam.get("typeOfPledge"), false);
                    if(!searchParam.get("adressRegion").isEmpty())
                        builderAuto.with("adressRegion", ":", searchParam.get("adressRegion"), false);
                    if(!searchParam.get("adressDistrict").isEmpty())
                        builderAuto.with("adressDistrict", ":", searchParam.get("adressDistrict"), false);
                    if(!searchParam.get("adressCity").isEmpty())
                        builderAuto.with("adressCity", ":", searchParam.get("adressCity"), false);
                    if(!searchParam.get("adressStreet").isEmpty())
                        builderAuto.with("adressStreet", ":", searchParam.get("adressStreet"), false);
                    if(!searchParam.get("insuranceObligation").isEmpty())
                        builderAuto.with("insuranceObligation", ":", searchParam.get("insuranceObligation"), false);
                    if(!searchParam.get("brandAuto").isEmpty())
                        builderAuto.with("brand", ":", searchParam.get("brandAuto"), false);
                    if(!searchParam.get("modelAuto").isEmpty())
                        builderAuto.with("model", ":", searchParam.get("modelAuto"), false);
                    if(!searchParam.get("vin").isEmpty())
                        builderAuto.with("vin", ":", searchParam.get("vin"), false);
                    if(!searchParam.get("numOfEngine").isEmpty())
                        builderAuto.with("numOfEngine", ":", searchParam.get("numOfEngine"), false);
                    if(!searchParam.get("numOfPts").isEmpty())
                        builderAuto.with("numOfPTS", ":", searchParam.get("numOfPts"), false);
                    if(!searchParam.get("yearOfManufactureAuto").isEmpty())
                        builderAuto.with("yearOfManufacture", searchParam.get("yearOfManufactureAutoOption"), searchParam.get("yearOfManufactureAuto"), false);
                    if(!searchParam.get("inventoryNumAuto").isEmpty())
                        builderAuto.with("inventoryNum", ":", searchParam.get("inventoryNumAuto"), false);
                    if(!searchParam.get("typeOfAuto").isEmpty())
                        builderAuto.with("typeOfAuto", ":", searchParam.get("typeOfAuto"), false);


                    Specification<PledgeSubjectAuto> spec = builderAuto.build();


                    return new ArrayList<>(repositoryPledgeSubjectAuto.findAll(spec));
                case "equip":
                    PledgeSubjectEquipmentSpecificationBuilder builderEquip = new PledgeSubjectEquipmentSpecificationBuilder();
                    if(!searchParam.get("namePS").isEmpty())
                        builderEquip.with("name", ":", searchParam.get("namePS"), false);
                    if(!searchParam.get("liquidity").isEmpty())
                        builderEquip.with("liquidity", ":", searchParam.get("liquidity"), false);
                    if(!searchParam.get("rsDZ").isEmpty())
                        builderEquip.with("rsDz", searchParam.get("rsDZOption"), searchParam.get("rsDZ"), false);
                    if(!searchParam.get("zsDZ").isEmpty())
                        builderEquip.with("zsDz", searchParam.get("zsDZOption"), searchParam.get("zsDZ"), false);
                    if(!searchParam.get("rsZZ").isEmpty())
                        builderEquip.with("rsZz", searchParam.get("rsZZOption"), searchParam.get("rsZZ"), false);
                    if(!searchParam.get("zsZZ").isEmpty())
                        builderEquip.with("zsZz", searchParam.get("zsZZOption"), searchParam.get("zsZZ"), false);
                    if(!searchParam.get("ss").isEmpty())
                        builderEquip.with("ss", searchParam.get("ssOption"), searchParam.get("ss"), false);
                    if(!searchParam.get("dateMonitoring").isEmpty()){
                        try {
                            Date date = simpleDateFormat.parse(searchParam.get("dateMonitoring"));
                            builderEquip.with("dateMonitoring", searchParam.get("dateMonitoringOption"), date, false);
                        }catch (ParseException e){
                            System.out.println("Не верный фортат dateEndPA");
                            e.printStackTrace();
                        }
                    }
                    if(!searchParam.get("dateConclusion").isEmpty()){
                        try {
                            Date date = simpleDateFormat.parse(searchParam.get("dateConclusion"));
                            builderEquip.with("dateConclusion", searchParam.get("dateConclusionOption"), date, false);
                        }catch (ParseException e){
                            System.out.println("Не верный фортат dateEndPA");
                            e.printStackTrace();
                        }
                    }
                    if(!searchParam.get("resultOfMonitoring").isEmpty())
                        builderEquip.with("statusMonitoring", ":", searchParam.get("resultOfMonitoring"), false);
                    if(!searchParam.get("typeOfPledge").isEmpty())
                        builderEquip.with("typeOfPledge", ":", searchParam.get("typeOfPledge"), false);
                    if(!searchParam.get("adressRegion").isEmpty())
                        builderEquip.with("adressRegion", ":", searchParam.get("adressRegion"), false);
                    if(!searchParam.get("adressDistrict").isEmpty())
                        builderEquip.with("adressDistrict", ":", searchParam.get("adressDistrict"), false);
                    if(!searchParam.get("adressCity").isEmpty())
                        builderEquip.with("adressCity", ":", searchParam.get("adressCity"), false);
                    if(!searchParam.get("adressStreet").isEmpty())
                        builderEquip.with("adressStreet", ":", searchParam.get("adressStreet"), false);
                    if(!searchParam.get("insuranceObligation").isEmpty())
                        builderEquip.with("insuranceObligation", ":", searchParam.get("insuranceObligation"), false);
                    if(!searchParam.get("brandEquip").isEmpty())
                        builderEquip.with("brand", ":", searchParam.get("brandEquip"), false);
                    if(!searchParam.get("modelEquip").isEmpty())
                        builderEquip.with("model", ":", searchParam.get("modelEquip"), false);
                    if(!searchParam.get("SerialNum").isEmpty())
                        builderEquip.with("serialNum", ":", searchParam.get("SerialNum"), false);
                    if(!searchParam.get("yearOfManufactureEquip").isEmpty())
                        builderEquip.with("yearOfManufacture", searchParam.get("yearOfManufactureEquipOption"), searchParam.get("yearOfManufactureEquip"), false);
                    if(!searchParam.get("inventoryNumEquip").isEmpty())
                        builderEquip.with("inventoryNum", ":", searchParam.get("inventoryNumEquip"), false);
                    if(!searchParam.get("typeOfEquip").isEmpty())
                        builderEquip.with("typeOfquipment", ":", searchParam.get("typeOfEquip"), false);

                    Specification<PledgeSubjectEquipment> specEquip = builderEquip.build();


                    return new ArrayList<>(repositoryPledgeSubjectEquipment.findAll(specEquip));

                case "tbo":
                    PledgeSubjectTBOSpecificationBuilder builderTbo = new PledgeSubjectTBOSpecificationBuilder();
                    if(!searchParam.get("namePS").isEmpty())
                        builderTbo.with("name", ":", searchParam.get("namePS"), false);
                    if(!searchParam.get("liquidity").isEmpty())
                        builderTbo.with("liquidity", ":", searchParam.get("liquidity"), false);
                    if(!searchParam.get("rsDZ").isEmpty())
                        builderTbo.with("rsDz", searchParam.get("rsDZOption"), searchParam.get("rsDZ"), false);
                    if(!searchParam.get("zsDZ").isEmpty())
                        builderTbo.with("zsDz", searchParam.get("zsDZOption"), searchParam.get("zsDZ"), false);
                    if(!searchParam.get("rsZZ").isEmpty())
                        builderTbo.with("rsZz", searchParam.get("rsZZOption"), searchParam.get("rsZZ"), false);
                    if(!searchParam.get("zsZZ").isEmpty())
                        builderTbo.with("zsZz", searchParam.get("zsZZOption"), searchParam.get("zsZZ"), false);
                    if(!searchParam.get("ss").isEmpty())
                        builderTbo.with("ss", searchParam.get("ssOption"), searchParam.get("ss"), false);
                    if(!searchParam.get("dateMonitoring").isEmpty()){
                        try {
                            Date date = simpleDateFormat.parse(searchParam.get("dateMonitoring"));
                            builderTbo.with("dateMonitoring", searchParam.get("dateMonitoringOption"), date, false);
                        }catch (ParseException e){
                            System.out.println("Не верный фортат dateEndPA");
                            e.printStackTrace();
                        }
                    }
                    if(!searchParam.get("dateConclusion").isEmpty()){
                        try {
                            Date date = simpleDateFormat.parse(searchParam.get("dateConclusion"));
                            builderTbo.with("dateConclusion", searchParam.get("dateConclusionOption"), date, false);
                        }catch (ParseException e){
                            System.out.println("Не верный фортат dateEndPA");
                            e.printStackTrace();
                        }
                    }
                    if(!searchParam.get("resultOfMonitoring").isEmpty())
                        builderTbo.with("statusMonitoring", ":", searchParam.get("resultOfMonitoring"), false);
                    if(!searchParam.get("typeOfPledge").isEmpty())
                        builderTbo.with("typeOfPledge", ":", searchParam.get("typeOfPledge"), false);
                    if(!searchParam.get("adressRegion").isEmpty())
                        builderTbo.with("adressRegion", ":", searchParam.get("adressRegion"), false);
                    if(!searchParam.get("adressDistrict").isEmpty())
                        builderTbo.with("adressDistrict", ":", searchParam.get("adressDistrict"), false);
                    if(!searchParam.get("adressCity").isEmpty())
                        builderTbo.with("adressCity", ":", searchParam.get("adressCity"), false);
                    if(!searchParam.get("adressStreet").isEmpty())
                        builderTbo.with("adressStreet", ":", searchParam.get("adressStreet"), false);
                    if(!searchParam.get("insuranceObligation").isEmpty())
                        builderTbo.with("insuranceObligation", ":", searchParam.get("insuranceObligation"), false);
                    if(!searchParam.get("carryingAmount").isEmpty())
                        builderTbo.with("carryingAmount", searchParam.get("carryingAmountOption"), searchParam.get("carryingAmount"), false);
                    if(!searchParam.get("typeOfTbo").isEmpty())
                        builderTbo.with("typeOfTBO", ":", searchParam.get("typeOfTbo"), false);


                    Specification<PledgeSubjectTBO> specTbo = builderTbo.build();

                    return new ArrayList<>(repositoryPledgeSubjectTBO.findAll(specTbo));

                case "securities":
                    PledgeSubjectSecuritiesSpecificationBuilder builderSecurities = new PledgeSubjectSecuritiesSpecificationBuilder();
                    if(!searchParam.get("namePS").isEmpty())
                        builderSecurities.with("name", ":", searchParam.get("namePS"), false);
                    if(!searchParam.get("liquidity").isEmpty())
                        builderSecurities.with("liquidity", ":", searchParam.get("liquidity"), false);
                    if(!searchParam.get("rsDZ").isEmpty())
                        builderSecurities.with("rsDz", searchParam.get("rsDZOption"), searchParam.get("rsDZ"), false);
                    if(!searchParam.get("zsDZ").isEmpty())
                        builderSecurities.with("zsDz", searchParam.get("zsDZOption"), searchParam.get("zsDZ"), false);
                    if(!searchParam.get("rsZZ").isEmpty())
                        builderSecurities.with("rsZz", searchParam.get("rsZZOption"), searchParam.get("rsZZ"), false);
                    if(!searchParam.get("zsZZ").isEmpty())
                        builderSecurities.with("zsZz", searchParam.get("zsZZOption"), searchParam.get("zsZZ"), false);
                    if(!searchParam.get("ss").isEmpty())
                        builderSecurities.with("ss", searchParam.get("ssOption"), searchParam.get("ss"), false);
                    if(!searchParam.get("dateMonitoring").isEmpty()){
                        try {
                            Date date = simpleDateFormat.parse(searchParam.get("dateMonitoring"));
                            builderSecurities.with("dateMonitoring", searchParam.get("dateMonitoringOption"), date, false);
                        }catch (ParseException e){
                            System.out.println("Не верный фортат dateEndPA");
                            e.printStackTrace();
                        }
                    }
                    if(!searchParam.get("dateConclusion").isEmpty()){
                        try {
                            Date date = simpleDateFormat.parse(searchParam.get("dateConclusion"));
                            builderSecurities.with("dateConclusion", searchParam.get("dateConclusionOption"), date, false);
                        }catch (ParseException e){
                            System.out.println("Не верный фортат dateEndPA");
                            e.printStackTrace();
                        }
                    }
                    if(!searchParam.get("resultOfMonitoring").isEmpty())
                        builderSecurities.with("statusMonitoring", ":", searchParam.get("resultOfMonitoring"), false);
                    if(!searchParam.get("typeOfPledge").isEmpty())
                        builderSecurities.with("typeOfPledge", ":", searchParam.get("typeOfPledge"), false);
                    if(!searchParam.get("adressRegion").isEmpty())
                        builderSecurities.with("adressRegion", ":", searchParam.get("adressRegion"), false);
                    if(!searchParam.get("adressDistrict").isEmpty())
                        builderSecurities.with("adressDistrict", ":", searchParam.get("adressDistrict"), false);
                    if(!searchParam.get("adressCity").isEmpty())
                        builderSecurities.with("adressCity", ":", searchParam.get("adressCity"), false);
                    if(!searchParam.get("adressStreet").isEmpty())
                        builderSecurities.with("adressStreet", ":", searchParam.get("adressStreet"), false);
                    if(!searchParam.get("insuranceObligation").isEmpty())
                        builderSecurities.with("insuranceObligation", ":", searchParam.get("insuranceObligation"), false);
                    if(!searchParam.get("nominalValue").isEmpty())
                        builderSecurities.with("nominalValue", searchParam.get("nominalValueOption"), searchParam.get("nominalValue"), false);
                    if(!searchParam.get("actuaValue").isEmpty())
                        builderSecurities.with("actualValue", searchParam.get("actuaValueOption"), searchParam.get("actuaValue"), false);
                    if(!searchParam.get("typeOfSecurities").isEmpty())
                        builderSecurities.with("typeOfSecurities", ":", searchParam.get("typeOfSecurities"), false);

                    Specification<PledgeSubjectSecurities> securitiesSpecification = builderSecurities.build();

                    return new ArrayList<>(repositoryPledgeSubjectSecurities.findAll(securitiesSpecification));
                case "landOwn":
                    PledgeSubjectRealtyLandOwnershipSpecificationBuilder builderLandOwn = new PledgeSubjectRealtyLandOwnershipSpecificationBuilder();
                    if(!searchParam.get("namePS").isEmpty())
                        builderLandOwn.with("name", ":", searchParam.get("namePS"), false);
                    if(!searchParam.get("liquidity").isEmpty())
                        builderLandOwn.with("liquidity", ":", searchParam.get("liquidity"), false);
                    if(!searchParam.get("rsDZ").isEmpty())
                        builderLandOwn.with("rsDz", searchParam.get("rsDZOption"), searchParam.get("rsDZ"), false);
                    if(!searchParam.get("zsDZ").isEmpty())
                        builderLandOwn.with("zsDz", searchParam.get("zsDZOption"), searchParam.get("zsDZ"), false);
                    if(!searchParam.get("rsZZ").isEmpty())
                        builderLandOwn.with("rsZz", searchParam.get("rsZZOption"), searchParam.get("rsZZ"), false);
                    if(!searchParam.get("zsZZ").isEmpty())
                        builderLandOwn.with("zsZz", searchParam.get("zsZZOption"), searchParam.get("zsZZ"), false);
                    if(!searchParam.get("ss").isEmpty())
                        builderLandOwn.with("ss", searchParam.get("ssOption"), searchParam.get("ss"), false);
                    if(!searchParam.get("dateMonitoring").isEmpty()){
                        try {
                            Date date = simpleDateFormat.parse(searchParam.get("dateMonitoring"));
                            builderLandOwn.with("dateMonitoring", searchParam.get("dateMonitoringOption"), date, false);
                        }catch (ParseException e){
                            System.out.println("Не верный фортат dateEndPA");
                            e.printStackTrace();
                        }
                    }
                    if(!searchParam.get("dateConclusion").isEmpty()){
                        try {
                            Date date = simpleDateFormat.parse(searchParam.get("dateConclusion"));
                            builderLandOwn.with("dateConclusion", searchParam.get("dateConclusionOption"), date, false);
                        }catch (ParseException e){
                            System.out.println("Не верный фортат dateEndPA");
                            e.printStackTrace();
                        }
                    }
                    if(!searchParam.get("resultOfMonitoring").isEmpty())
                        builderLandOwn.with("statusMonitoring", ":", searchParam.get("resultOfMonitoring"), false);
                    if(!searchParam.get("typeOfPledge").isEmpty())
                        builderLandOwn.with("typeOfPledge", ":", searchParam.get("typeOfPledge"), false);
                    if(!searchParam.get("adressRegion").isEmpty())
                        builderLandOwn.with("adressRegion", ":", searchParam.get("adressRegion"), false);
                    if(!searchParam.get("adressDistrict").isEmpty())
                        builderLandOwn.with("adressDistrict", ":", searchParam.get("adressDistrict"), false);
                    if(!searchParam.get("adressCity").isEmpty())
                        builderLandOwn.with("adressCity", ":", searchParam.get("adressCity"), false);
                    if(!searchParam.get("adressStreet").isEmpty())
                        builderLandOwn.with("adressStreet", ":", searchParam.get("adressStreet"), false);
                    if(!searchParam.get("insuranceObligation").isEmpty())
                        builderLandOwn.with("insuranceObligation", ":", searchParam.get("insuranceObligation"), false);
                    if(!searchParam.get("CadastralNum").isEmpty())
                        builderLandOwn.with("cadastralNum", ":", searchParam.get("CadastralNum"), false);
                    if(!searchParam.get("area").isEmpty())
                        builderLandOwn.with("area", searchParam.get("areaOption"), searchParam.get("area"), false);
                    if(!searchParam.get("ConditionalNum").isEmpty())
                        builderLandOwn.with("conditionalNum", ":", searchParam.get("ConditionalNum"), false);
                    if(!searchParam.get("landCategory").isEmpty()) {
                        LandCategory landCategory = repositoryLandCategory.getOne(Integer.parseInt(searchParam.get("landCategory")));
                        builderLandOwn.with("landCategory", ":", landCategory, false);
                    }
                    if(!searchParam.get("permittedUse").isEmpty())
                        builderLandOwn.with("permittedUse", ":", searchParam.get("permittedUse"), false);
                    if(!searchParam.get("builtUp").isEmpty())
                        builderLandOwn.with("builtUp", ":", searchParam.get("builtUp"), false);
                    if(!searchParam.get("cadastralNumOfBuilding").isEmpty())
                        builderLandOwn.with("cadastralNumOfBuilding", ":", searchParam.get("cadastralNumOfBuilding"), false);

                    Specification<PledgeSubjectRealtyLandOwnership> landOwnershipSpecification = builderLandOwn.build();

                    return new ArrayList<>(repositoryPledgeSubjectRealtyLandOwnership.findAll(landOwnershipSpecification));
                case "landLease":
                    PledgeSubjectRealtyLandLeaseSpecificationBuilder builderLandLease = new PledgeSubjectRealtyLandLeaseSpecificationBuilder();
                    if(!searchParam.get("namePS").isEmpty())
                        builderLandLease.with("name", ":", searchParam.get("namePS"), false);
                    if(!searchParam.get("liquidity").isEmpty())
                        builderLandLease.with("liquidity", ":", searchParam.get("liquidity"), false);
                    if(!searchParam.get("rsDZ").isEmpty())
                        builderLandLease.with("rsDz", searchParam.get("rsDZOption"), searchParam.get("rsDZ"), false);
                    if(!searchParam.get("zsDZ").isEmpty())
                        builderLandLease.with("zsDz", searchParam.get("zsDZOption"), searchParam.get("zsDZ"), false);
                    if(!searchParam.get("rsZZ").isEmpty())
                        builderLandLease.with("rsZz", searchParam.get("rsZZOption"), searchParam.get("rsZZ"), false);
                    if(!searchParam.get("zsZZ").isEmpty())
                        builderLandLease.with("zsZz", searchParam.get("zsZZOption"), searchParam.get("zsZZ"), false);
                    if(!searchParam.get("ss").isEmpty())
                        builderLandLease.with("ss", searchParam.get("ssOption"), searchParam.get("ss"), false);
                    if(!searchParam.get("dateMonitoring").isEmpty()){
                        try {
                            Date date = simpleDateFormat.parse(searchParam.get("dateMonitoring"));
                            builderLandLease.with("dateMonitoring", searchParam.get("dateMonitoringOption"), date, false);
                        }catch (ParseException e){
                            System.out.println("Не верный фортат dateEndPA");
                            e.printStackTrace();
                        }
                    }
                    if(!searchParam.get("dateConclusion").isEmpty()){
                        try {
                            Date date = simpleDateFormat.parse(searchParam.get("dateConclusion"));
                            builderLandLease.with("dateConclusion", searchParam.get("dateConclusionOption"), date, false);
                        }catch (ParseException e){
                            System.out.println("Не верный фортат dateEndPA");
                            e.printStackTrace();
                        }
                    }
                    if(!searchParam.get("resultOfMonitoring").isEmpty())
                        builderLandLease.with("statusMonitoring", ":", searchParam.get("resultOfMonitoring"), false);
                    if(!searchParam.get("typeOfPledge").isEmpty())
                        builderLandLease.with("typeOfPledge", ":", searchParam.get("typeOfPledge"), false);
                    if(!searchParam.get("adressRegion").isEmpty())
                        builderLandLease.with("adressRegion", ":", searchParam.get("adressRegion"), false);
                    if(!searchParam.get("adressDistrict").isEmpty())
                        builderLandLease.with("adressDistrict", ":", searchParam.get("adressDistrict"), false);
                    if(!searchParam.get("adressCity").isEmpty())
                        builderLandLease.with("adressCity", ":", searchParam.get("adressCity"), false);
                    if(!searchParam.get("adressStreet").isEmpty())
                        builderLandLease.with("adressStreet", ":", searchParam.get("adressStreet"), false);
                    if(!searchParam.get("insuranceObligation").isEmpty())
                        builderLandLease.with("insuranceObligation", ":", searchParam.get("insuranceObligation"), false);
                    if(!searchParam.get("CadastralNum").isEmpty())
                        builderLandLease.with("cadastralNum", ":", searchParam.get("CadastralNum"), false);
                    if(!searchParam.get("area").isEmpty())
                        builderLandLease.with("area", searchParam.get("areaOption"), searchParam.get("area"), false);
                    if(!searchParam.get("ConditionalNum").isEmpty())
                        builderLandLease.with("conditionalNum", ":", searchParam.get("ConditionalNum"), false);
                    if(!searchParam.get("landCategory").isEmpty()) {
                        LandCategory landCategory = repositoryLandCategory.getOne(Integer.parseInt(searchParam.get("landCategory")));
                        builderLandLease.with("landCategory", ":", landCategory, false);
                    }
                    if(!searchParam.get("permittedUse").isEmpty())
                        builderLandLease.with("permittedUse", ":", searchParam.get("permittedUse"), false);
                    if(!searchParam.get("builtUp").isEmpty())
                        builderLandLease.with("builtUp", ":", searchParam.get("builtUp"), false);
                    if(!searchParam.get("cadastralNumOfBuilding").isEmpty())
                        builderLandLease.with("cadastralNumOfBuilding", ":", searchParam.get("cadastralNumOfBuilding"), false);
                    if(!searchParam.get("beginDateLease").isEmpty()){
                        try {
                            Date date = simpleDateFormat.parse(searchParam.get("beginDateLease"));
                            builderLandLease.with("dateBeginLease", searchParam.get("beginDateLeaseOption"), date, false);
                        }catch (ParseException e){
                            System.out.println("Не верный фортат dateEndPA");
                            e.printStackTrace();
                        }
                    }
                    if(!searchParam.get("endDateLease").isEmpty()){
                        try {
                            Date date = simpleDateFormat.parse(searchParam.get("endDateLease"));
                            builderLandLease.with("dateEndLease", searchParam.get("endDateLeaseOption"), date, false);
                        }catch (ParseException e){
                            System.out.println("Не верный фортат dateEndPA");
                            e.printStackTrace();
                        }
                    }


                    Specification<PledgeSubjectRealtyLandLease> landLeaseSpecification = builderLandLease.build();

                    return new ArrayList<>(repositoryPledgeSubjectRealtyLandLease.findAll(landLeaseSpecification));
                case "build":
                    PledgeSubjectRealtyBuildingSpecificationBuilder builderBuild = new PledgeSubjectRealtyBuildingSpecificationBuilder();
                    if(!searchParam.get("namePS").isEmpty())
                        builderBuild.with("name", ":", searchParam.get("namePS"), false);
                    if(!searchParam.get("liquidity").isEmpty())
                        builderBuild.with("liquidity", ":", searchParam.get("liquidity"), false);
                    if(!searchParam.get("rsDZ").isEmpty())
                        builderBuild.with("rsDz", searchParam.get("rsDZOption"), searchParam.get("rsDZ"), false);
                    if(!searchParam.get("zsDZ").isEmpty())
                        builderBuild.with("zsDz", searchParam.get("zsDZOption"), searchParam.get("zsDZ"), false);
                    if(!searchParam.get("rsZZ").isEmpty())
                        builderBuild.with("rsZz", searchParam.get("rsZZOption"), searchParam.get("rsZZ"), false);
                    if(!searchParam.get("zsZZ").isEmpty())
                        builderBuild.with("zsZz", searchParam.get("zsZZOption"), searchParam.get("zsZZ"), false);
                    if(!searchParam.get("ss").isEmpty())
                        builderBuild.with("ss", searchParam.get("ssOption"), searchParam.get("ss"), false);
                    if(!searchParam.get("dateMonitoring").isEmpty()){
                        try {
                            Date date = simpleDateFormat.parse(searchParam.get("dateMonitoring"));
                            builderBuild.with("dateMonitoring", searchParam.get("dateMonitoringOption"), date, false);
                        }catch (ParseException e){
                            System.out.println("Не верный фортат dateEndPA");
                            e.printStackTrace();
                        }
                    }
                    if(!searchParam.get("dateConclusion").isEmpty()){
                        try {
                            Date date = simpleDateFormat.parse(searchParam.get("dateConclusion"));
                            builderBuild.with("dateConclusion", searchParam.get("dateConclusionOption"), date, false);
                        }catch (ParseException e){
                            System.out.println("Не верный фортат dateEndPA");
                            e.printStackTrace();
                        }
                    }
                    if(!searchParam.get("resultOfMonitoring").isEmpty())
                        builderBuild.with("statusMonitoring", ":", searchParam.get("resultOfMonitoring"), false);
                    if(!searchParam.get("typeOfPledge").isEmpty())
                        builderBuild.with("typeOfPledge", ":", searchParam.get("typeOfPledge"), false);
                    if(!searchParam.get("adressRegion").isEmpty())
                        builderBuild.with("adressRegion", ":", searchParam.get("adressRegion"), false);
                    if(!searchParam.get("adressDistrict").isEmpty())
                        builderBuild.with("adressDistrict", ":", searchParam.get("adressDistrict"), false);
                    if(!searchParam.get("adressCity").isEmpty())
                        builderBuild.with("adressCity", ":", searchParam.get("adressCity"), false);
                    if(!searchParam.get("adressStreet").isEmpty())
                        builderBuild.with("adressStreet", ":", searchParam.get("adressStreet"), false);
                    if(!searchParam.get("insuranceObligation").isEmpty())
                        builderBuild.with("insuranceObligation", ":", searchParam.get("insuranceObligation"), false);
                    if(!searchParam.get("CadastralNum").isEmpty())
                        builderBuild.with("cadastralNum", ":", searchParam.get("CadastralNum"), false);
                    if(!searchParam.get("area").isEmpty())
                        builderBuild.with("area", searchParam.get("areaOption"), searchParam.get("area"), false);
                    if(!searchParam.get("ConditionalNum").isEmpty())
                        builderBuild.with("conditionalNum", ":", searchParam.get("ConditionalNum"), false);
                    if(!searchParam.get("marketSegmentBuilding").isEmpty()) {
                        MarketSegment marketSegment  = repositoryMarketSegment.findByName(searchParam.get("marketSegmentBuilding"));
                        builderBuild.with("marketSegment", ":", marketSegment, false);
                    }
                    if(!searchParam.get("readinessВegree").isEmpty())
                        builderBuild.with("readinessDegree", searchParam.get("readinessВegreeOption"), searchParam.get("readinessВegree"), false);
                    if(!searchParam.get("yearOfConstructionBuilding").isEmpty())
                        builderBuild.with("yearOfConstruction", searchParam.get("yearOfConstructionBuildingOption"), searchParam.get("yearOfConstructionBuilding"), false);


                    Specification<PledgeSubjectRealtyBuilding> buildingSpecification = builderBuild.build();

                    return new ArrayList<>(repositoryPledgeSubjectRealtyBuilding.findAll(buildingSpecification));
                case "room":
                    PledgeSubjectRealtyRoomSpecificationBuilder builderRoom = new PledgeSubjectRealtyRoomSpecificationBuilder();
                    if(!searchParam.get("namePS").isEmpty())
                        builderRoom.with("name", ":", searchParam.get("namePS"), false);
                    if(!searchParam.get("liquidity").isEmpty())
                        builderRoom.with("liquidity", ":", searchParam.get("liquidity"), false);
                    if(!searchParam.get("rsDZ").isEmpty())
                        builderRoom.with("rsDz", searchParam.get("rsDZOption"), searchParam.get("rsDZ"), false);
                    if(!searchParam.get("zsDZ").isEmpty())
                        builderRoom.with("zsDz", searchParam.get("zsDZOption"), searchParam.get("zsDZ"), false);
                    if(!searchParam.get("rsZZ").isEmpty())
                        builderRoom.with("rsZz", searchParam.get("rsZZOption"), searchParam.get("rsZZ"), false);
                    if(!searchParam.get("zsZZ").isEmpty())
                        builderRoom.with("zsZz", searchParam.get("zsZZOption"), searchParam.get("zsZZ"), false);
                    if(!searchParam.get("ss").isEmpty())
                        builderRoom.with("ss", searchParam.get("ssOption"), searchParam.get("ss"), false);
                    if(!searchParam.get("dateMonitoring").isEmpty()){
                        try {
                            Date date = simpleDateFormat.parse(searchParam.get("dateMonitoring"));
                            builderRoom.with("dateMonitoring", searchParam.get("dateMonitoringOption"), date, false);
                        }catch (ParseException e){
                            System.out.println("Не верный фортат dateEndPA");
                            e.printStackTrace();
                        }
                    }
                    if(!searchParam.get("dateConclusion").isEmpty()){
                        try {
                            Date date = simpleDateFormat.parse(searchParam.get("dateConclusion"));
                            builderRoom.with("dateConclusion", searchParam.get("dateConclusionOption"), date, false);
                        }catch (ParseException e){
                            System.out.println("Не верный фортат dateEndPA");
                            e.printStackTrace();
                        }
                    }
                    if(!searchParam.get("resultOfMonitoring").isEmpty())
                        builderRoom.with("statusMonitoring", ":", searchParam.get("resultOfMonitoring"), false);
                    if(!searchParam.get("typeOfPledge").isEmpty())
                        builderRoom.with("typeOfPledge", ":", searchParam.get("typeOfPledge"), false);
                    if(!searchParam.get("adressRegion").isEmpty())
                        builderRoom.with("adressRegion", ":", searchParam.get("adressRegion"), false);
                    if(!searchParam.get("adressDistrict").isEmpty())
                        builderRoom.with("adressDistrict", ":", searchParam.get("adressDistrict"), false);
                    if(!searchParam.get("adressCity").isEmpty())
                        builderRoom.with("adressCity", ":", searchParam.get("adressCity"), false);
                    if(!searchParam.get("adressStreet").isEmpty())
                        builderRoom.with("adressStreet", ":", searchParam.get("adressStreet"), false);
                    if(!searchParam.get("insuranceObligation").isEmpty())
                        builderRoom.with("insuranceObligation", ":", searchParam.get("insuranceObligation"), false);
                    if(!searchParam.get("CadastralNum").isEmpty())
                        builderRoom.with("cadastralNum", ":", searchParam.get("CadastralNum"), false);
                    if(!searchParam.get("area").isEmpty())
                        builderRoom.with("area", searchParam.get("areaOption"), searchParam.get("area"), false);
                    if(!searchParam.get("ConditionalNum").isEmpty())
                        builderRoom.with("conditionalNum", ":", searchParam.get("ConditionalNum"), false);
                    if(!searchParam.get("marketSegmentRoom").isEmpty()) {
                        MarketSegment marketSegment  = repositoryMarketSegment.findByName(searchParam.get("marketSegmentRoom"));
                        builderRoom.with("marketSegmentRoom", ":", marketSegment, false);
                    }
                    if(!searchParam.get("marketSegmentBuilding").isEmpty()) {
                        MarketSegment marketSegment  = repositoryMarketSegment.findByName(searchParam.get("marketSegmentBuilding"));
                        builderRoom.with("marketSegmentBuilding", ":", marketSegment, false);
                    }
                    if(!searchParam.get("floorLocation").isEmpty())
                        builderRoom.with("floorLocation", searchParam.get("floorLocationOption"), searchParam.get("floorLocation"), false);


                    Specification<PledgeSubjectRealtyRoom> roomSpecification = builderRoom.build();

                    return new ArrayList<>(repositoryPledgeSubjectRealtyRoom.findAll(roomSpecification));
                case "vessel":
                    PledgeSubjectVesselSpecificationBuilder builderVessel = new PledgeSubjectVesselSpecificationBuilder();
                    if(!searchParam.get("namePS").isEmpty())
                        builderVessel.with("name", ":", searchParam.get("namePS"), false);
                    if(!searchParam.get("liquidity").isEmpty())
                        builderVessel.with("liquidity", ":", searchParam.get("liquidity"), false);
                    if(!searchParam.get("rsDZ").isEmpty())
                        builderVessel.with("rsDz", searchParam.get("rsDZOption"), searchParam.get("rsDZ"), false);
                    if(!searchParam.get("zsDZ").isEmpty())
                        builderVessel.with("zsDz", searchParam.get("zsDZOption"), searchParam.get("zsDZ"), false);
                    if(!searchParam.get("rsZZ").isEmpty())
                        builderVessel.with("rsZz", searchParam.get("rsZZOption"), searchParam.get("rsZZ"), false);
                    if(!searchParam.get("zsZZ").isEmpty())
                        builderVessel.with("zsZz", searchParam.get("zsZZOption"), searchParam.get("zsZZ"), false);
                    if(!searchParam.get("ss").isEmpty())
                        builderVessel.with("ss", searchParam.get("ssOption"), searchParam.get("ss"), false);
                    if(!searchParam.get("dateMonitoring").isEmpty()){
                        try {
                            Date date = simpleDateFormat.parse(searchParam.get("dateMonitoring"));
                            builderVessel.with("dateMonitoring", searchParam.get("dateMonitoringOption"), date, false);
                        }catch (ParseException e){
                            System.out.println("Не верный фортат dateEndPA");
                            e.printStackTrace();
                        }
                    }
                    if(!searchParam.get("dateConclusion").isEmpty()){
                        try {
                            Date date = simpleDateFormat.parse(searchParam.get("dateConclusion"));
                            builderVessel.with("dateConclusion", searchParam.get("dateConclusionOption"), date, false);
                        }catch (ParseException e){
                            System.out.println("Не верный фортат dateEndPA");
                            e.printStackTrace();
                        }
                    }
                    if(!searchParam.get("resultOfMonitoring").isEmpty())
                        builderVessel.with("statusMonitoring", ":", searchParam.get("resultOfMonitoring"), false);
                    if(!searchParam.get("typeOfPledge").isEmpty())
                        builderVessel.with("typeOfPledge", ":", searchParam.get("typeOfPledge"), false);
                    if(!searchParam.get("adressRegion").isEmpty())
                        builderVessel.with("adressRegion", ":", searchParam.get("adressRegion"), false);
                    if(!searchParam.get("adressDistrict").isEmpty())
                        builderVessel.with("adressDistrict", ":", searchParam.get("adressDistrict"), false);
                    if(!searchParam.get("adressCity").isEmpty())
                        builderVessel.with("adressCity", ":", searchParam.get("adressCity"), false);
                    if(!searchParam.get("adressStreet").isEmpty())
                        builderVessel.with("adressStreet", ":", searchParam.get("adressStreet"), false);
                    if(!searchParam.get("insuranceObligation").isEmpty())
                        builderVessel.with("insuranceObligation", ":", searchParam.get("insuranceObligation"), false);


                    if(!searchParam.get("imo").isEmpty())
                        builderVessel.with("imo", ":", searchParam.get("imo"), false);
                    if(!searchParam.get("typeOfVessel").isEmpty())
                        builderVessel.with("vesselType", ":", searchParam.get("typeOfVessel"), false);
                    if(!searchParam.get("grossTonnage").isEmpty())
                        builderVessel.with("grossTonnage", searchParam.get("grossTonnageOption"), searchParam.get("grossTonnage"), false);
                    if(!searchParam.get("deadweight").isEmpty())
                        builderVessel.with("deadweight", searchParam.get("deadweightOption"), searchParam.get("deadweight"), false);
                    if(!searchParam.get("yearBuiltVessel").isEmpty())
                        builderVessel.with("yearBuilt", searchParam.get("yearBuiltVesselOption"), searchParam.get("yearBuiltVessel"), false);


                    Specification<PledgeSubjectVessel> vesselSpecification = builderVessel.build();

                    return new ArrayList<>(repositoryPledgeSubjectVessel.findAll(vesselSpecification));

                default:
                    return null;
            }
        }
    }
}
