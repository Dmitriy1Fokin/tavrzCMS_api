package ru.fds.tavrzcms3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.domain.Specification;
import ru.fds.tavrzcms3.Service.InsuranceService;
import ru.fds.tavrzcms3.Service.LoanAgreementService;
import ru.fds.tavrzcms3.Service.PledgeAgreementService;
import ru.fds.tavrzcms3.Service.PledgeSubjectService;
import ru.fds.tavrzcms3.domain.*;
import ru.fds.tavrzcms3.repository.*;
import ru.fds.tavrzcms3.specification.LoanAgreementSpecification;
import ru.fds.tavrzcms3.specification.LoanAgreementSpecificationsBuilder;
import ru.fds.tavrzcms3.specification.SearchCriteria;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@SpringBootApplication
public class Tavrzcms3Application {

    public static void main(String[] args) {
        SpringApplication.run(Tavrzcms3Application.class);
    }


    @PostConstruct
    public void init(){
    }
}
