package ru.fds.tavrzcms3.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.fds.tavrzcms3.domain.LoanAgreement;
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
public class LoanAgreementServiceTest {

    @Autowired
    LoanAgreementService loanAgreementService;
    @Autowired
    ValidatorEntity validatorEntity;

    @Test
    public void getNewLoanAgreementsFromFile() {
        try {
            List<LoanAgreement> loanAgreementList = loanAgreementService.getNewLoanAgreementsFromFile(new File("src\\test\\java\\ru\\fds\\tavrzcms3\\testdata\\loan_agreement_new.xlsx"));
            System.out.println("LOAN AGREEMENT FROM FILE:");
            loanAgreementList.forEach(x->System.out.println(x));

            List<LoanAgreement> loanAgreementListValid = new ArrayList<>();
            System.out.println("LOAN AGREEMENT VALIDATION ERRORS:");
            loanAgreementList.forEach(x->{
                Set<ConstraintViolation<LoanAgreement>> violations = validatorEntity.validateEntity(x);
                if(violations.isEmpty())
                    loanAgreementListValid.add(x);
                else
                    System.out.println(validatorEntity.getErrorMessage());
            });

            System.out.println("LOAN AGREEMENT VALID:");
            loanAgreementListValid.forEach(x-> System.out.println(x));

            assertEquals(2, loanAgreementList.size());
            assertEquals(1, loanAgreementListValid.size());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getCurrentLoanAgreementsFromFile() {
        try {
            List<LoanAgreement> loanAgreementList = loanAgreementService.getCurrentLoanAgreementsFromFile(new File("src\\test\\java\\ru\\fds\\tavrzcms3\\testdata\\loan_agreement_update.xlsx"));
            System.out.println("LOAN AGREEMENT FROM FILE:");
            loanAgreementList.forEach(x->System.out.println(x));

            List<LoanAgreement> loanAgreementListValid = new ArrayList<>();
            System.out.println("LOAN AGREEMENT VALIDATION ERRORS:");
            loanAgreementList.forEach(x->{
                Set<ConstraintViolation<LoanAgreement>> violations = validatorEntity.validateEntity(x);
                if(violations.isEmpty())
                    loanAgreementListValid.add(x);
                else
                    System.out.println(validatorEntity.getErrorMessage());
            });

            System.out.println("LOAN AGREEMENT VALID:");
            loanAgreementListValid.forEach(x-> System.out.println(x));

            assertEquals(2, loanAgreementList.size());
            assertEquals(1, loanAgreementListValid.size());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}