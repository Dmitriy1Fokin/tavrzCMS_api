package ru.fds.tavrzcms3.converver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.fds.tavrzcms3.dictionary.*;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.domain.PledgeSubjectAuto;
import ru.fds.tavrzcms3.dto.MainCharacteristic;
import ru.fds.tavrzcms3.dto.PledgeSubjectAutoDto;
import ru.fds.tavrzcms3.dto.PledgeSubjectDto;
import ru.fds.tavrzcms3.repository.RepositoryPledgeSubject;

import java.util.Date;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class PledgeSubjectConverterDtoTest {

    @Autowired
    PledgeSubjectConverterDto pledgeSubjectConverterDto;
    @Autowired
    PledgeSubjectAutoConverter pledgeSubjectAutoConverter;
    @Autowired
    RepositoryPledgeSubject repositoryPledgeSubject;

    @Test
    public void toEntity() {
        PledgeSubjectDto pledgeSubjectDto = PledgeSubjectDto.builder()
                .pledgeSubjectId(2L)
                .name("имя")
                .liquidity(Liquidity.AVERAGE)
                .rsDz(100)
                .rsZz(50)
                .zsDz(70)
                .zsZz(40)
                .ss(0)
                .dateMonitoring(new Date(1))
                .dateConclusion(new Date(123456))
                .statusMonitoring(StatusOfMonitoring.IN_STOCK)
                .typeOfCollateral(TypeOfCollateral.VESSEL)
                .typeOfPledge(TypeOfPledge.RETURN)
                .typeOfMonitoring(TypeOfMonitoring.VISUAL)
                .adressRegion("SPB")
                .adressDistrict("Prim")
                .adressCity("SPB2")
                .adressStreet("Nev")
                .adressBuilbing("2")
                .adressPemises("23-Y")
                .insuranceObligation("да")
                .pledgeAgreementsId(12L)
                .costHistoriesId(23L)
                .monitoringId(34L)
                .encumbrancesId(76L)
                .insurancesId(99L)
                .fullAddress("SPB, Prim, SPB2, Nev, 2, 23-Y")
                .mainCharacteristic(new MainCharacteristic("123", "qwe"))
                .build();

        PledgeSubject pledgeSubject = pledgeSubjectConverterDto.toEntity(pledgeSubjectDto);



    }

    @Test
    public void toDto() {
        PledgeSubject pledgeSubjectAuto = repositoryPledgeSubject.findById(133L).get();
        PledgeSubject pledgeSubjectEquip = repositoryPledgeSubject.findById(15L).get();
        PledgeSubject pledgeSubjectBuild = repositoryPledgeSubject.findById(2L).get();
        PledgeSubject pledgeSubjectLandLease = repositoryPledgeSubject.findById(60L).get();
        PledgeSubject pledgeSubjectLandOwn = repositoryPledgeSubject.findById(1L).get();
        PledgeSubject pledgeSubjectRoom = repositoryPledgeSubject.findById(4L).get();
        PledgeSubject pledgeSubjectSec = repositoryPledgeSubject.findById(1710L).get();
        PledgeSubject pledgeSubjectTBO = repositoryPledgeSubject.findById(58L).get();
        PledgeSubject pledgeSubjectVessel = repositoryPledgeSubject.findById(955L).get();

        PledgeSubjectDto pledgeSubjectDtoAuto = pledgeSubjectConverterDto.toDto(pledgeSubjectAuto);
        PledgeSubjectDto pledgeSubjectDtoEquip = pledgeSubjectConverterDto.toDto(pledgeSubjectEquip);
        PledgeSubjectDto pledgeSubjectDtoBuild = pledgeSubjectConverterDto.toDto(pledgeSubjectBuild);
        PledgeSubjectDto pledgeSubjectDtoLandLease = pledgeSubjectConverterDto.toDto(pledgeSubjectLandLease);
        PledgeSubjectDto pledgeSubjectDtoLandOwn = pledgeSubjectConverterDto.toDto(pledgeSubjectLandOwn);
        PledgeSubjectDto pledgeSubjectDtoRoom = pledgeSubjectConverterDto.toDto(pledgeSubjectRoom);
        PledgeSubjectDto pledgeSubjectDtoSec = pledgeSubjectConverterDto.toDto(pledgeSubjectSec);
        PledgeSubjectDto pledgeSubjectDtoTBO = pledgeSubjectConverterDto.toDto(pledgeSubjectTBO);
        PledgeSubjectDto pledgeSubjectDtoVessel = pledgeSubjectConverterDto.toDto(pledgeSubjectVessel);

//        PledgeSubjectAutoDto pledgeSubjectDto = pledgeSubjectAutoConverter.toDto((PledgeSubjectAuto) pledgeSubjectAuto);
//        System.out.println(pledgeSubjectDto);

        System.out.println(pledgeSubjectDtoAuto);
        System.out.println(pledgeSubjectDtoEquip);
        System.out.println(pledgeSubjectDtoBuild);
        System.out.println(pledgeSubjectDtoLandLease);
        System.out.println(pledgeSubjectDtoLandOwn);
        System.out.println(pledgeSubjectDtoRoom);
        System.out.println(pledgeSubjectDtoSec);
        System.out.println(pledgeSubjectDtoTBO);
        System.out.println(pledgeSubjectDtoVessel);

    }
}