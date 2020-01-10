package ru.fds.tavrzcms3.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.fds.tavrzcms3.domain.Monitoring;
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
public class MonitoringServiceTest {

    @Autowired
    MonitoringService monitoringService;
    @Autowired
    ValidatorEntity validatorEntity;

    @Test
    public void getNewMonitoringsFromFile() {
        try {
            List<Monitoring> monitoringList = monitoringService.getNewMonitoringsFromFile(new File("src\\test\\java\\ru\\fds\\tavrzcms3\\testdata\\monitoring_new.xlsx"));
            System.out.println("MONITORING FROM FILE:");
            monitoringList.forEach(x->System.out.println(x));

            List<Monitoring> monitoringListValid = new ArrayList<>();
            System.out.println("MONITORING VALIDATION ERRORS:");
            monitoringList.forEach(x->{
                Set<ConstraintViolation<Monitoring>> violations = validatorEntity.validateEntity(x);
                if(violations.isEmpty())
                    monitoringListValid.add(x);
                else
                    System.out.println(validatorEntity.getErrorMessage());
            });

            System.out.println("MONITORING VALID:");
            monitoringListValid.forEach(x-> System.out.println(x));

            assertEquals(3, monitoringList.size());
            assertEquals(1, monitoringListValid.size());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getCurrentMonitoringsFromFile() {
        try {
            List<Monitoring> monitoringList = monitoringService.getCurrentMonitoringsFromFile(new File("src\\test\\java\\ru\\fds\\tavrzcms3\\testdata\\monitoring_update.xlsx"));
            System.out.println("MONITORING FROM FILE:");
            monitoringList.forEach(x->System.out.println(x));

            List<Monitoring> monitoringListValid = new ArrayList<>();
            System.out.println("MONITORING VALIDATION ERRORS:");
            monitoringList.forEach(x->{
                Set<ConstraintViolation<Monitoring>> violations = validatorEntity.validateEntity(x);
                if(violations.isEmpty())
                    monitoringListValid.add(x);
                else
                    System.out.println(validatorEntity.getErrorMessage());
            });

            System.out.println("MONITORING VALID:");
            monitoringListValid.forEach(x-> System.out.println(x));

            assertEquals(3, monitoringList.size());
            assertEquals(1, monitoringListValid.size());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}