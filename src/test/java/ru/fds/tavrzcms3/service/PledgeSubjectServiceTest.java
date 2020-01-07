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
import java.util.List;
import java.util.Set;

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
                .zsDz(BigDecimal.valueOf(100))
                .zsZz(BigDecimal.valueOf(70))
                .rsDz(BigDecimal.valueOf(150))
                .rsZz(BigDecimal.valueOf(100))
                .ss(BigDecimal.valueOf(70))
                .dateMonitoring(LocalDate.now())
                .dateConclusion(LocalDate.now())
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

    @Test
    public void searchPledgeSubject(){
//        SpecificationBuilder builder = new SpecificationBuilderImpl();
//        builder.with("name", ":", "дом", false);

//        Specification<PledgeSubject> specification = builder.build();

//        List<PledgeSubject> pledgeSubjectList = repositoryPledgeSubject.findAll(specification);
//        System.out.println("111111. size:" + pledgeSubjectList.size() + ". " + pledgeSubjectList);


//        PledgeSubject pledgeSubject = PledgeSubject.builder().name("дом").build();
//        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
//        List<PledgeSubject> pledgeSubjectList2 = repositoryPledgeSubject.findAll(Example.of(pledgeSubject, exampleMatcher));
//        System.out.println("2222222. size:" + pledgeSubjectList2.size() + ". " );

        PledgeSubject pledgeSubject = PledgeSubject.builder().build();
        Class pledgeSubjecrClass = PledgeSubject.class;
        Field[] pledgeSubjectFields = pledgeSubjecrClass.getDeclaredFields();
        for(Field field : pledgeSubjectFields){

//                if(field.getType() == Liquidity.class){
//                    System.out.println("Liquidity.class");
//                    Class aClass = field.getType().getSuperclass();
//                    System.out.println(aClass.getName());
//
//                }else if(field.getType() == Date.class){
//                    System.out.println("Date.class");
//
//                }

//                if(field.getType().getSuperclass() == Enum.class){
                if(field.getType() == Liquidity.class){
                    System.out.println(field.getName());
                    try {
                        Method method = field.getType().getMethod("valueOf", String.class);
//                        Method method = field.getType().getMethod("values");

                            Liquidity liquidity = Liquidity.HIGH;
                            System.out.println("liquidity1: " + liquidity);
                            Class aClass = field.getType();
                            Enum anEnum;
//                            = Enum.valueOf(aClass,"HIGH");

                        anEnum =(Enum) method.invoke(aClass, "AVERAGE");
                            System.out.println("liquidity1: " + anEnum);
                            System.out.println("liquidity Class : " + anEnum.getClass().getName());

                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }

        }
//      Liquidity.AVERAGE

        Liquidity.values();

//        for (Field field : pledgeSubjectFields) {
//            Object o = field.getType();
//            System.out.println("Object: " + ((Class) o).getName() + ". Class: " + o.getClass());
//            System.out.println("TYPE: " + field.getType() + " NAME: " + field.getName());
//        }



//        SpecificationNestedAttribute specificationNestedAttribute = new SpecificationNestedAttribute(PledgeSubject.class, PledgeSubjectAuto.class);
//        specificationNestedAttribute.getSpecification("12", 12, ":");

//        SpecificationBuilder builder2 = new SpecificationBuilderImpl();
//        builder2.with("pledgeSubjectBuilding.getCadastralNum()", ":", "78", false);
//
//        Specification<PledgeSubject> specification2 = builder2.build();
//
//        List<PledgeSubject> pledgeSubjectList2 = repositoryPledgeSubject.findAll(specification2);
//        System.out.println("2222222" + pledgeSubjectList2);

    }

    @Test
    public void getNewPledgeSubjectsFromFile(){
        try {
            List<PledgeSubject> pledgeSubjectList = pledgeSubjectService.getNewPledgeSubjectsFromFile(new File("src\\test\\java\\ru\\fds\\tavrzcms3\\testdata\\ps_new_building.xlsx"), TypeOfCollateral.BUILDING);
            System.out.println(pledgeSubjectList);
//            System.out.println(pledgeSubjectList.get(0).getCostHistories().get(0));
//            System.out.println(pledgeSubjectList.get(0).getMonitorings().get(0));

//            pledgeSubjectList = pledgeSubjectService.insertPledgeSubjects(pledgeSubjectList);
//            System.out.println(pledgeSubjectList);
//            System.out.println(pledgeSubjectList.get(0).getCostHistories().get(0));
//            System.out.println(pledgeSubjectList.get(0).getMonitorings().get(0));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void getCurrentPledgeSubjectsFromFile() {
    }
}