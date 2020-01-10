package ru.fds.tavrzcms3.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.fds.tavrzcms3.domain.Insurance;
import ru.fds.tavrzcms3.validate.ValidatorEntity;

import javax.validation.ConstraintViolation;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class InsuranceServiceTest {

    @Autowired
    InsuranceService insuranceService;
    @Autowired
    ValidatorEntity validatorEntity;

    @Test
    public void getNewInsurancesFromFile() {
        try {
            List<Insurance> insuranceList = insuranceService.getNewInsurancesFromFile(new File("src\\test\\java\\ru\\fds\\tavrzcms3\\testdata\\insurance_new.xlsx"));
            System.out.println("INSURANCE FROM FILE:");
            insuranceList.forEach(x->System.out.println(x));

            List<Insurance> insuranceListValid = new ArrayList<>();
            System.out.println("INSURANCE VALIDATION ERRORS:");
            insuranceList.forEach(x->{
                Set<ConstraintViolation<Insurance>> violations = validatorEntity.validateEntity(x);
                if(violations.isEmpty())
                    insuranceListValid.add(x);
                else
                    System.out.println(validatorEntity.getErrorMessage());
            });

            System.out.println("ENCUMBRANCE VALID:");
            insuranceListValid.forEach(x-> System.out.println(x));

            assertEquals(3, insuranceList.size());
            assertEquals(1, insuranceListValid.size());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getCurrentInsurancesFromFile() {
        try {
            List<Insurance> insuranceList = insuranceService.getCurrentInsurancesFromFile(new File("src\\test\\java\\ru\\fds\\tavrzcms3\\testdata\\insurance_update.xlsx"));
            System.out.println("INSURANCE FROM FILE:");
            insuranceList.forEach(x->System.out.println(x));

            List<Insurance> insuranceListValid = new ArrayList<>();
            System.out.println("INSURANCE VALIDATION ERRORS:");
            insuranceList.forEach(x->{
                Set<ConstraintViolation<Insurance>> violations = validatorEntity.validateEntity(x);
                if(violations.isEmpty())
                    insuranceListValid.add(x);
                else
                    System.out.println(validatorEntity.getErrorMessage());
            });

            System.out.println("ENCUMBRANCE VALID:");
            insuranceListValid.forEach(x-> System.out.println(x));

            assertEquals(3, insuranceList.size());
            assertEquals(1, insuranceListValid.size());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}