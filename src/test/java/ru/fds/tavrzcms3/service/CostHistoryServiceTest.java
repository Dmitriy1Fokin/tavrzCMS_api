package ru.fds.tavrzcms3.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.fds.tavrzcms3.domain.CostHistory;
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
public class CostHistoryServiceTest {
    @Autowired
    CostHistoryService costHistoryService;
    @Autowired
    ValidatorEntity validatorEntity;

    @Test
    public void getNewCostHistoriesFromFile() {
        try {
            List<CostHistory> costHistoryList = costHistoryService.getNewCostHistoriesFromFile(new File("src\\test\\java\\ru\\fds\\tavrzcms3\\testdata\\cost_history_new.xlsx"));
            System.out.println("COST HISTORY FROM FILE:");
            costHistoryList.forEach(x->System.out.println(x));

            List<CostHistory> costHistoryListValid = new ArrayList<>();
            System.out.println("COST HISTORY VALIDATION ERRORS:");
            costHistoryList.forEach(x->{
                Set<ConstraintViolation<CostHistory>> violations = validatorEntity.validateEntity(x);
                if(violations.isEmpty())
                    costHistoryListValid.add(x);
                else
                    System.out.println(validatorEntity.getErrorMessage());
            });

            System.out.println("COST HISTORY VALID:");
            costHistoryListValid.forEach(x-> System.out.println(x));

            assertEquals(3, costHistoryList.size());
            assertEquals(1, costHistoryListValid.size());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getCurrentCostHistoriesFromFile() {
        try {
            List<CostHistory> costHistoryList = costHistoryService.getCurrentCostHistoriesFromFile(new File("src\\test\\java\\ru\\fds\\tavrzcms3\\testdata\\cost_history_update.xlsx"));
            System.out.println("COST HISTORY FROM FILE:");
            costHistoryList.forEach(x->System.out.println(x));

            List<CostHistory> costHistoryListValid = new ArrayList<>();
            System.out.println("COST HISTORY VALIDATION ERRORS:");
            costHistoryList.forEach(x->{
                Set<ConstraintViolation<CostHistory>> violations = validatorEntity.validateEntity(x);
                if(violations.isEmpty())
                    costHistoryListValid.add(x);
                else
                    System.out.println(validatorEntity.getErrorMessage());
            });

            System.out.println("COST HISTORY VALID:");
            costHistoryListValid.forEach(x-> System.out.println(x));

            assertEquals(3, costHistoryList.size());
            assertEquals(1, costHistoryListValid.size());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}