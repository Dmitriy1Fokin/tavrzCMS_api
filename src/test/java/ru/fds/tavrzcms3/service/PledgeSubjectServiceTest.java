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
import ru.fds.tavrzcms3.domain.PledgeSubjectRoom;
import ru.fds.tavrzcms3.dto.DtoFactory;
import ru.fds.tavrzcms3.dto.PledgeSubjectDto;
import ru.fds.tavrzcms3.repository.RepositoryPledgeSubject;
import ru.fds.tavrzcms3.validate.ValidatorEntity;

import javax.validation.ConstraintViolation;
import java.util.Date;
import java.util.Set;

import static org.junit.Assert.*;

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
    public void getPledgeSubject(){
        PledgeSubject pledgeSubject = repositoryPledgeSubject.findById(133L).get();
        System.out.println(pledgeSubject);

    }

    @Test
    public void updatePledgeSubject(){
        PledgeSubject pledgeSubject = repositoryPledgeSubject.findById(1592L).get();
        System.out.println(pledgeSubject);
        pledgeSubject.setAdressCity("Тамбов");
        pledgeSubject.getPledgeSubjectBuilding().setConditionalNum("23/456-13");
        pledgeSubject.getPledgeSubjectBuilding().setYearOfConstruction(1959);
        repositoryPledgeSubject.save(pledgeSubject);
        System.out.println(pledgeSubject);

    }

    @Test
    public void insertPledgeSubject(){
        PledgeSubjectRoom pledgeSubjectRoom = PledgeSubjectRoom.builder()
                .floorLocation("1-3")
                .marketSegmentRoom(MarketSegment.OFFICE)
                .marketSegmentBuilding(MarketSegment.TRADING_OFFICE)
                .area(123.3)
                .cadastralNum("55:12:0012349:1265")
                .conditionalNum("25-23/2010")
                .build();

        PledgeSubject pledgeSubject = PledgeSubject.builder()
                .name("qwer")
                .liquidity(Liquidity.HIGH)
                .zsDz(100)
                .zsZz(70)
                .rsDz(150)
                .rsZz(100)
                .ss(70)
                .dateMonitoring(new Date(123456789))
                .dateConclusion(new Date(234567890))
                .statusMonitoring(StatusOfMonitoring.IN_STOCK)
                .typeOfCollateral(TypeOfCollateral.PREMISE)
                .typeOfPledge(TypeOfPledge.RETURN)
                .typeOfMonitoring(TypeOfMonitoring.VISUAL)
                .adressRegion("Омская обл.")
                .adressDistrict("Омский район")
                .adressCity("Омск")
                .adressStreet("ул. Ленина")
                .adressBuilbing("12")
                .adressPemises("12-Н")
                .insuranceObligation("да")
                .pledgeSubjectRoom(pledgeSubjectRoom)
                .build();

        System.out.println(pledgeSubject);
        repositoryPledgeSubject.save(pledgeSubject);
        System.out.println(pledgeSubject);


    }

    @Test
    public void updatePledgeSubjectFromDto(){
        PledgeSubject pledgeSubject = repositoryPledgeSubject.findById(33L).get();
        System.out.println(pledgeSubject);

        PledgeSubjectDto pledgeSubjectDto = dtoFactory.getPledgeSubjectDto(pledgeSubject);
        System.out.println(pledgeSubjectDto);

        pledgeSubjectDto.getPledgeSubjectEquipmentDto().setProductivity(1000D);
        pledgeSubjectDto.getPledgeSubjectEquipmentDto().setTypeOfProductivity("л/час");
        System.out.println(pledgeSubjectDto);

        PledgeSubject pledgeSubjectAfterDto = dtoFactory.getPledgeSubjectEntity(pledgeSubjectDto);
        System.out.println(pledgeSubjectAfterDto);

        Set<ConstraintViolation<PledgeSubject>> violations =  validatorEntity.validateEntity(pledgeSubjectAfterDto);
        if(!violations.isEmpty())
            throw new IllegalArgumentException(validatorEntity.getErrorMessage());

        repositoryPledgeSubject.save(pledgeSubjectAfterDto);
        System.out.println(pledgeSubjectAfterDto);




    }


}