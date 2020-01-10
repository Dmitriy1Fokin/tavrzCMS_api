package ru.fds.tavrzcms3.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.fds.tavrzcms3.domain.Encumbrance;
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
public class EncumbranceServiceTest {

    @Autowired
    EncumbranceService encumbranceService;
    @Autowired
    ValidatorEntity validatorEntity;

    @Test
    public void getNewEncumbranceFromFile() {
        try {
            List<Encumbrance> encumbranceList = encumbranceService.getNewEncumbranceFromFile(new File("src\\test\\java\\ru\\fds\\tavrzcms3\\testdata\\encumbrance_new.xlsx"));
            System.out.println("ENCUMBRANCE FROM FILE:");
            encumbranceList.forEach(x->System.out.println(x));

            List<Encumbrance> encumbranceListValid = new ArrayList<>();
            System.out.println("ENCUMBRANCE VALIDATION ERRORS:");
            encumbranceList.forEach(x->{
                Set<ConstraintViolation<Encumbrance>> violations = validatorEntity.validateEntity(x);
                if(violations.isEmpty())
                    encumbranceListValid.add(x);
                else
                    System.out.println(validatorEntity.getErrorMessage());
            });

            System.out.println("ENCUMBRANCE VALID:");
            encumbranceListValid.forEach(x-> System.out.println(x));

            assertEquals(3, encumbranceList.size());
            assertEquals(1, encumbranceListValid.size());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getCurrentEncumbranceFromFile() {
        try {
            List<Encumbrance> encumbranceList = encumbranceService.getCurrentEncumbranceFromFile(new File("src\\test\\java\\ru\\fds\\tavrzcms3\\testdata\\encumbrance_update.xlsx"));
            System.out.println("ENCUMBRANCE FROM FILE:");
            encumbranceList.forEach(x->System.out.println(x));

            List<Encumbrance> encumbranceListValid = new ArrayList<>();
            System.out.println("ENCUMBRANCE VALIDATION ERRORS:");
            encumbranceList.forEach(x->{
                Set<ConstraintViolation<Encumbrance>> violations = validatorEntity.validateEntity(x);
                if(violations.isEmpty())
                    encumbranceListValid.add(x);
                else
                    System.out.println(validatorEntity.getErrorMessage());
            });

            System.out.println("ENCUMBRANCE VALID:");
            encumbranceListValid.forEach(x-> System.out.println(x));

            assertEquals(3, encumbranceList.size());
            assertEquals(1, encumbranceListValid.size());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}