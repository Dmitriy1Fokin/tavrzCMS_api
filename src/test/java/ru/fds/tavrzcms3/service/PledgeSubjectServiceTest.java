package ru.fds.tavrzcms3.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.fds.tavrzcms3.dictionary.Liquidity;
import ru.fds.tavrzcms3.dictionary.MarketSegment;
import ru.fds.tavrzcms3.dictionary.StatusOfMonitoring;
import ru.fds.tavrzcms3.dictionary.TypeOfCollateral;
import ru.fds.tavrzcms3.dictionary.TypeOfMonitoring;
import ru.fds.tavrzcms3.dictionary.TypeOfPledge;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.domain.embedded.PledgeSubjectRoom;
import ru.fds.tavrzcms3.dto.DtoFactory;
import ru.fds.tavrzcms3.dto.PledgeSubjectDto;
import ru.fds.tavrzcms3.repository.RepositoryPledgeSubject;
import ru.fds.tavrzcms3.validate.ValidatorEntity;

import javax.validation.ConstraintViolation;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PledgeSubjectServiceTest {

    @Autowired
    PledgeSubjectService pledgeSubjectService;
    @Autowired
    RepositoryPledgeSubject repositoryPledgeSubject;
    @Autowired
    DtoFactory dtoFactory;
    @Autowired
    ValidatorEntity validatorEntity;

    @Test
    public void getNewPledgeSubjectsFromFile(){
        try {
            List<PledgeSubject> pledgeSubjectAutoList = pledgeSubjectService
                    .getNewPledgeSubjectsFromFile(new File("src\\test\\java\\ru\\fds\\tavrzcms3\\testdata\\pledge_subject_auto_new.xlsx"), TypeOfCollateral.AUTO);
            System.out.println("PLEDGE SUBJECT AUTO FROM FILE:");
            pledgeSubjectAutoList.forEach(x->System.out.println(x));

            List<PledgeSubject> pledgeSubjectBuildingList = pledgeSubjectService
                    .getNewPledgeSubjectsFromFile(new File("src\\test\\java\\ru\\fds\\tavrzcms3\\testdata\\pledge_subject_building_new.xlsx"), TypeOfCollateral.BUILDING);
            System.out.println("PLEDGE SUBJECT BUILDING FROM FILE:");
            pledgeSubjectBuildingList.forEach(x->System.out.println(x));

            List<PledgeSubject> pledgeSubjectEquipmentList = pledgeSubjectService
                    .getNewPledgeSubjectsFromFile(new File("src\\test\\java\\ru\\fds\\tavrzcms3\\testdata\\pledge_subject_equip_new.xlsx"), TypeOfCollateral.EQUIPMENT);
            System.out.println("PLEDGE SUBJECT EQUIPMENT FROM FILE:");
            pledgeSubjectEquipmentList.forEach(x->System.out.println(x));

            List<PledgeSubject> pledgeSubjectLandLeaseList = pledgeSubjectService
                    .getNewPledgeSubjectsFromFile(new File("src\\test\\java\\ru\\fds\\tavrzcms3\\testdata\\pledge_subject_land_lease_new.xlsx"), TypeOfCollateral.LAND_LEASE);
            System.out.println("PLEDGE SUBJECT LAND_LEASE FROM FILE:");
            pledgeSubjectLandLeaseList.forEach(x->System.out.println(x));

            List<PledgeSubject> pledgeSubjectLandOwnList = pledgeSubjectService
                    .getNewPledgeSubjectsFromFile(new File("src\\test\\java\\ru\\fds\\tavrzcms3\\testdata\\pledge_subject_land_own_new.xlsx"), TypeOfCollateral.LAND_OWNERSHIP);
            System.out.println("PLEDGE SUBJECT LAND_OWNERSHIP FROM FILE:");
            pledgeSubjectLandOwnList.forEach(x->System.out.println(x));

            List<PledgeSubject> pledgeSubjectRoomList = pledgeSubjectService
                    .getNewPledgeSubjectsFromFile(new File("src\\test\\java\\ru\\fds\\tavrzcms3\\testdata\\pledge_subject_room_new.xlsx"), TypeOfCollateral.PREMISE);
            System.out.println("PLEDGE SUBJECT PREMISE FROM FILE:");
            pledgeSubjectRoomList.forEach(x->System.out.println(x));

            List<PledgeSubject> pledgeSubjectSecuritiesList = pledgeSubjectService
                    .getNewPledgeSubjectsFromFile(new File("src\\test\\java\\ru\\fds\\tavrzcms3\\testdata\\pledge_subject_securities_new.xlsx"), TypeOfCollateral.SECURITIES);
            System.out.println("PLEDGE SUBJECT SECURITIES FROM FILE:");
            pledgeSubjectSecuritiesList.forEach(x->System.out.println(x));

            List<PledgeSubject> pledgeSubjectTboList = pledgeSubjectService
                    .getNewPledgeSubjectsFromFile(new File("src\\test\\java\\ru\\fds\\tavrzcms3\\testdata\\pledge_subject_tbo_new.xlsx"), TypeOfCollateral.TBO);
            System.out.println("PLEDGE SUBJECT TBO FROM FILE:");
            pledgeSubjectTboList.forEach(x->System.out.println(x));

            List<PledgeSubject> pledgeSubjectVesselList = pledgeSubjectService
                    .getNewPledgeSubjectsFromFile(new File("src\\test\\java\\ru\\fds\\tavrzcms3\\testdata\\pledge_subject_vessel_new.xlsx"), TypeOfCollateral.VESSEL);
            System.out.println("PLEDGE SUBJECT VESSEL FROM FILE:");
            pledgeSubjectVesselList.forEach(x->System.out.println(x));



            List<PledgeSubject> pledgeSubjectAutoListValid = new ArrayList<>();
            System.out.println("PLEDGE SUBJECT AUTO VALIDATION ERRORS:");
            pledgeSubjectAutoList.forEach(x->{
                Set<ConstraintViolation<PledgeSubject>> violations = validatorEntity.validateEntity(x);
                if(violations.isEmpty())
                    pledgeSubjectAutoListValid.add(x);
                else
                    System.out.println(validatorEntity.getErrorMessage());
            });

            List<PledgeSubject> pledgeSubjectBuildingListValid = new ArrayList<>();
            System.out.println("PLEDGE SUBJECT BUILDING VALIDATION ERRORS:");
            pledgeSubjectBuildingList.forEach(x->{
                Set<ConstraintViolation<PledgeSubject>> violations = validatorEntity.validateEntity(x);
                if(violations.isEmpty())
                    pledgeSubjectBuildingListValid.add(x);
                else
                    System.out.println(validatorEntity.getErrorMessage());
            });

            List<PledgeSubject> pledgeSubjectEquipmentListValid = new ArrayList<>();
            System.out.println("PLEDGE SUBJECT EQUIPMENT VALIDATION ERRORS:");
            pledgeSubjectEquipmentList.forEach(x->{
                Set<ConstraintViolation<PledgeSubject>> violations = validatorEntity.validateEntity(x);
                if(violations.isEmpty())
                    pledgeSubjectEquipmentListValid.add(x);
                else
                    System.out.println(validatorEntity.getErrorMessage());
            });

            List<PledgeSubject> pledgeSubjectLandLeaseListValid = new ArrayList<>();
            System.out.println("PLEDGE SUBJECT LAND_LEASE VALIDATION ERRORS:");
            pledgeSubjectLandLeaseList.forEach(x->{
                Set<ConstraintViolation<PledgeSubject>> violations = validatorEntity.validateEntity(x);
                if(violations.isEmpty())
                    pledgeSubjectLandLeaseListValid.add(x);
                else
                    System.out.println(validatorEntity.getErrorMessage());
            });

            List<PledgeSubject> pledgeSubjectLandOwnListValid = new ArrayList<>();
            System.out.println("PLEDGE SUBJECT LAND_OWNERSHIP VALIDATION ERRORS:");
            pledgeSubjectLandOwnList.forEach(x->{
                Set<ConstraintViolation<PledgeSubject>> violations = validatorEntity.validateEntity(x);
                if(violations.isEmpty())
                    pledgeSubjectLandOwnListValid.add(x);
                else
                    System.out.println(validatorEntity.getErrorMessage());
            });

            List<PledgeSubject> pledgeSubjectRoomListValid = new ArrayList<>();
            System.out.println("PLEDGE SUBJECT PREMISE VALIDATION ERRORS:");
            pledgeSubjectRoomList.forEach(x->{
                Set<ConstraintViolation<PledgeSubject>> violations = validatorEntity.validateEntity(x);
                if(violations.isEmpty())
                    pledgeSubjectRoomListValid.add(x);
                else
                    System.out.println(validatorEntity.getErrorMessage());
            });

            List<PledgeSubject> pledgeSubjectSecuritiesListValid = new ArrayList<>();
            System.out.println("PLEDGE SUBJECT SECURITIES VALIDATION ERRORS:");
            pledgeSubjectSecuritiesList.forEach(x->{
                Set<ConstraintViolation<PledgeSubject>> violations = validatorEntity.validateEntity(x);
                if(violations.isEmpty())
                    pledgeSubjectSecuritiesListValid.add(x);
                else
                    System.out.println(validatorEntity.getErrorMessage());
            });

            List<PledgeSubject> pledgeSubjectTboListValid = new ArrayList<>();
            System.out.println("PLEDGE SUBJECT TBO VALIDATION ERRORS:");
            pledgeSubjectTboList.forEach(x->{
                Set<ConstraintViolation<PledgeSubject>> violations = validatorEntity.validateEntity(x);
                if(violations.isEmpty())
                    pledgeSubjectTboListValid.add(x);
                else
                    System.out.println(validatorEntity.getErrorMessage());
            });

            List<PledgeSubject> pledgeSubjectVesselListValid = new ArrayList<>();
            System.out.println("PLEDGE SUBJECT VESSEL VALIDATION ERRORS:");
            pledgeSubjectVesselList.forEach(x->{
                Set<ConstraintViolation<PledgeSubject>> violations = validatorEntity.validateEntity(x);
                if(violations.isEmpty())
                    pledgeSubjectVesselListValid.add(x);
                else
                    System.out.println(validatorEntity.getErrorMessage());
            });



            System.out.println("PLEDGE SUBJECT AUTO VALID:");
            pledgeSubjectAutoListValid.forEach(x-> System.out.println(x));

            System.out.println("PLEDGE SUBJECT BUILDING VALID:");
            pledgeSubjectBuildingListValid.forEach(x-> System.out.println(x));

            System.out.println("PLEDGE SUBJECT EQUIPMENT VALID:");
            pledgeSubjectEquipmentListValid.forEach(x-> System.out.println(x));

            System.out.println("PLEDGE SUBJECT LAND_LEASE VALID:");
            pledgeSubjectLandLeaseListValid.forEach(x-> System.out.println(x));

            System.out.println("PLEDGE SUBJECT LAND_OWNERSHIP VALID:");
            pledgeSubjectLandOwnListValid.forEach(x-> System.out.println(x));

            System.out.println("PLEDGE SUBJECT PREMISE VALID:");
            pledgeSubjectRoomListValid.forEach(x-> System.out.println(x));

            System.out.println("PLEDGE SUBJECT SECURITIES VALID:");
            pledgeSubjectSecuritiesListValid.forEach(x-> System.out.println(x));

            System.out.println("PLEDGE SUBJECT TBO VALID:");
            pledgeSubjectTboListValid.forEach(x-> System.out.println(x));

            System.out.println("PLEDGE SUBJECT VESSEL VALID:");
            pledgeSubjectVesselListValid.forEach(x-> System.out.println(x));

            assertEquals(3, pledgeSubjectAutoList.size());
            assertEquals(3, pledgeSubjectBuildingList.size());
            assertEquals(3, pledgeSubjectEquipmentList.size());
            assertEquals(3, pledgeSubjectLandLeaseList.size());
            assertEquals(3, pledgeSubjectLandOwnList.size());
            assertEquals(3, pledgeSubjectRoomList.size());
            assertEquals(3, pledgeSubjectSecuritiesList.size());
            assertEquals(3, pledgeSubjectTboList.size());
            assertEquals(3, pledgeSubjectVesselList.size());

            assertEquals(3, pledgeSubjectAutoListValid.size());
            assertEquals(3, pledgeSubjectBuildingListValid.size());
            assertEquals(3, pledgeSubjectEquipmentListValid.size());
            assertEquals(3, pledgeSubjectLandLeaseListValid.size());
            assertEquals(3, pledgeSubjectLandOwnListValid.size());
            assertEquals(3, pledgeSubjectRoomListValid.size());
            assertEquals(3, pledgeSubjectSecuritiesListValid.size());
            assertEquals(3, pledgeSubjectTboListValid.size());
            assertEquals(3, pledgeSubjectVesselListValid.size());



        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void getCurrentPledgeSubjectsFromFile() {
        try {
            List<PledgeSubject> pledgeSubjectAutoList = pledgeSubjectService
                    .getCurrentPledgeSubjectsFromFile(new File("src\\test\\java\\ru\\fds\\tavrzcms3\\testdata\\pledge_subject_auto_update.xlsx"));
            System.out.println("PLEDGE SUBJECT AUTO FROM FILE:");
            pledgeSubjectAutoList.forEach(x->System.out.println(x));

            List<PledgeSubject> pledgeSubjectBuildingList = pledgeSubjectService
                    .getCurrentPledgeSubjectsFromFile(new File("src\\test\\java\\ru\\fds\\tavrzcms3\\testdata\\pledge_subject_building_update.xlsx"));
            System.out.println("PLEDGE SUBJECT BUILDING FROM FILE:");
            pledgeSubjectBuildingList.forEach(x->System.out.println(x));

            List<PledgeSubject> pledgeSubjectEquipmentList = pledgeSubjectService
                    .getCurrentPledgeSubjectsFromFile(new File("src\\test\\java\\ru\\fds\\tavrzcms3\\testdata\\pledge_subject_equip_update.xlsx"));
            System.out.println("PLEDGE SUBJECT EQUIPMENT FROM FILE:");
            pledgeSubjectEquipmentList.forEach(x->System.out.println(x));

            List<PledgeSubject> pledgeSubjectLandLeaseList = pledgeSubjectService
                    .getCurrentPledgeSubjectsFromFile(new File("src\\test\\java\\ru\\fds\\tavrzcms3\\testdata\\pledge_subject_land_lease_update.xlsx"));
            System.out.println("PLEDGE SUBJECT LAND_LEASE FROM FILE:");
            pledgeSubjectLandLeaseList.forEach(x->System.out.println(x));

            List<PledgeSubject> pledgeSubjectLandOwnList = pledgeSubjectService
                    .getCurrentPledgeSubjectsFromFile(new File("src\\test\\java\\ru\\fds\\tavrzcms3\\testdata\\pledge_subject_land_own_update.xlsx"));
            System.out.println("PLEDGE SUBJECT LAND_OWNERSHIP FROM FILE:");
            pledgeSubjectLandOwnList.forEach(x->System.out.println(x));

            List<PledgeSubject> pledgeSubjectRoomList = pledgeSubjectService
                    .getCurrentPledgeSubjectsFromFile(new File("src\\test\\java\\ru\\fds\\tavrzcms3\\testdata\\pledge_subject_room_update.xlsx"));
            System.out.println("PLEDGE SUBJECT PREMISE FROM FILE:");
            pledgeSubjectRoomList.forEach(x->System.out.println(x));

            List<PledgeSubject> pledgeSubjectSecuritiesList = pledgeSubjectService
                    .getCurrentPledgeSubjectsFromFile(new File("src\\test\\java\\ru\\fds\\tavrzcms3\\testdata\\pledge_subject_securities_update.xlsx"));
            System.out.println("PLEDGE SUBJECT SECURITIES FROM FILE:");
            pledgeSubjectSecuritiesList.forEach(x->System.out.println(x));

            List<PledgeSubject> pledgeSubjectTboList = pledgeSubjectService
                    .getCurrentPledgeSubjectsFromFile(new File("src\\test\\java\\ru\\fds\\tavrzcms3\\testdata\\pledge_subject_tbo_update.xlsx"));
            System.out.println("PLEDGE SUBJECT TBO FROM FILE:");
            pledgeSubjectTboList.forEach(x->System.out.println(x));

            List<PledgeSubject> pledgeSubjectVesselList = pledgeSubjectService
                    .getCurrentPledgeSubjectsFromFile(new File("src\\test\\java\\ru\\fds\\tavrzcms3\\testdata\\pledge_subject_vessel_update.xlsx"));
            System.out.println("PLEDGE SUBJECT VESSEL FROM FILE:");
            pledgeSubjectVesselList.forEach(x->System.out.println(x));



            List<PledgeSubject> pledgeSubjectAutoListValid = new ArrayList<>();
            System.out.println("PLEDGE SUBJECT AUTO VALIDATION ERRORS:");
            pledgeSubjectAutoList.forEach(x->{
                Set<ConstraintViolation<PledgeSubject>> violations = validatorEntity.validateEntity(x);
                if(violations.isEmpty())
                    pledgeSubjectAutoListValid.add(x);
                else
                    System.out.println(validatorEntity.getErrorMessage());
            });

            List<PledgeSubject> pledgeSubjectBuildingListValid = new ArrayList<>();
            System.out.println("PLEDGE SUBJECT BUILDING VALIDATION ERRORS:");
            pledgeSubjectBuildingList.forEach(x->{
                Set<ConstraintViolation<PledgeSubject>> violations = validatorEntity.validateEntity(x);
                if(violations.isEmpty())
                    pledgeSubjectBuildingListValid.add(x);
                else
                    System.out.println(validatorEntity.getErrorMessage());
            });

            List<PledgeSubject> pledgeSubjectEquipmentListValid = new ArrayList<>();
            System.out.println("PLEDGE SUBJECT EQUIPMENT VALIDATION ERRORS:");
            pledgeSubjectEquipmentList.forEach(x->{
                Set<ConstraintViolation<PledgeSubject>> violations = validatorEntity.validateEntity(x);
                if(violations.isEmpty())
                    pledgeSubjectEquipmentListValid.add(x);
                else
                    System.out.println(validatorEntity.getErrorMessage());
            });

            List<PledgeSubject> pledgeSubjectLandLeaseListValid = new ArrayList<>();
            System.out.println("PLEDGE SUBJECT LAND_LEASE VALIDATION ERRORS:");
            pledgeSubjectLandLeaseList.forEach(x->{
                Set<ConstraintViolation<PledgeSubject>> violations = validatorEntity.validateEntity(x);
                if(violations.isEmpty())
                    pledgeSubjectLandLeaseListValid.add(x);
                else
                    System.out.println(validatorEntity.getErrorMessage());
            });

            List<PledgeSubject> pledgeSubjectLandOwnListValid = new ArrayList<>();
            System.out.println("PLEDGE SUBJECT LAND_OWNERSHIP VALIDATION ERRORS:");
            pledgeSubjectLandOwnList.forEach(x->{
                Set<ConstraintViolation<PledgeSubject>> violations = validatorEntity.validateEntity(x);
                if(violations.isEmpty())
                    pledgeSubjectLandOwnListValid.add(x);
                else
                    System.out.println(validatorEntity.getErrorMessage());
            });

            List<PledgeSubject> pledgeSubjectRoomListValid = new ArrayList<>();
            System.out.println("PLEDGE SUBJECT PREMISE VALIDATION ERRORS:");
            pledgeSubjectRoomList.forEach(x->{
                Set<ConstraintViolation<PledgeSubject>> violations = validatorEntity.validateEntity(x);
                if(violations.isEmpty())
                    pledgeSubjectRoomListValid.add(x);
                else
                    System.out.println(validatorEntity.getErrorMessage());
            });

            List<PledgeSubject> pledgeSubjectSecuritiesListValid = new ArrayList<>();
            System.out.println("PLEDGE SUBJECT SECURITIES VALIDATION ERRORS:");
            pledgeSubjectSecuritiesList.forEach(x->{
                Set<ConstraintViolation<PledgeSubject>> violations = validatorEntity.validateEntity(x);
                if(violations.isEmpty())
                    pledgeSubjectSecuritiesListValid.add(x);
                else
                    System.out.println(validatorEntity.getErrorMessage());
            });

            List<PledgeSubject> pledgeSubjectTboListValid = new ArrayList<>();
            System.out.println("PLEDGE SUBJECT TBO VALIDATION ERRORS:");
            pledgeSubjectTboList.forEach(x->{
                Set<ConstraintViolation<PledgeSubject>> violations = validatorEntity.validateEntity(x);
                if(violations.isEmpty())
                    pledgeSubjectTboListValid.add(x);
                else
                    System.out.println(validatorEntity.getErrorMessage());
            });

            List<PledgeSubject> pledgeSubjectVesselListValid = new ArrayList<>();
            System.out.println("PLEDGE SUBJECT VESSEL VALIDATION ERRORS:");
            pledgeSubjectVesselList.forEach(x->{
                Set<ConstraintViolation<PledgeSubject>> violations = validatorEntity.validateEntity(x);
                if(violations.isEmpty())
                    pledgeSubjectVesselListValid.add(x);
                else
                    System.out.println(validatorEntity.getErrorMessage());
            });



            System.out.println("PLEDGE SUBJECT AUTO VALID:");
            pledgeSubjectAutoListValid.forEach(x-> System.out.println(x));

            System.out.println("PLEDGE SUBJECT BUILDING VALID:");
            pledgeSubjectBuildingListValid.forEach(x-> System.out.println(x));

            System.out.println("PLEDGE SUBJECT EQUIPMENT VALID:");
            pledgeSubjectEquipmentListValid.forEach(x-> System.out.println(x));

            System.out.println("PLEDGE SUBJECT LAND_LEASE VALID:");
            pledgeSubjectLandLeaseListValid.forEach(x-> System.out.println(x));

            System.out.println("PLEDGE SUBJECT LAND_OWNERSHIP VALID:");
            pledgeSubjectLandOwnListValid.forEach(x-> System.out.println(x));

            System.out.println("PLEDGE SUBJECT PREMISE VALID:");
            pledgeSubjectRoomListValid.forEach(x-> System.out.println(x));

            System.out.println("PLEDGE SUBJECT SECURITIES VALID:");
            pledgeSubjectSecuritiesListValid.forEach(x-> System.out.println(x));

            System.out.println("PLEDGE SUBJECT TBO VALID:");
            pledgeSubjectTboListValid.forEach(x-> System.out.println(x));

            System.out.println("PLEDGE SUBJECT VESSEL VALID:");
            pledgeSubjectVesselListValid.forEach(x-> System.out.println(x));

            assertEquals(3, pledgeSubjectAutoList.size());
            assertEquals(3, pledgeSubjectBuildingList.size());
            assertEquals(3, pledgeSubjectEquipmentList.size());
            assertEquals(3, pledgeSubjectLandLeaseList.size());
            assertEquals(3, pledgeSubjectLandOwnList.size());
            assertEquals(3, pledgeSubjectRoomList.size());
            assertEquals(3, pledgeSubjectSecuritiesList.size());
            assertEquals(3, pledgeSubjectTboList.size());
            assertEquals(3, pledgeSubjectVesselList.size());

            assertEquals(3, pledgeSubjectAutoListValid.size());
            assertEquals(3, pledgeSubjectBuildingListValid.size());
            assertEquals(3, pledgeSubjectEquipmentListValid.size());
            assertEquals(3, pledgeSubjectLandLeaseListValid.size());
            assertEquals(3, pledgeSubjectLandOwnListValid.size());
            assertEquals(3, pledgeSubjectRoomListValid.size());
            assertEquals(3, pledgeSubjectSecuritiesListValid.size());
            assertEquals(3, pledgeSubjectTboListValid.size());
            assertEquals(3, pledgeSubjectVesselListValid.size());



        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}