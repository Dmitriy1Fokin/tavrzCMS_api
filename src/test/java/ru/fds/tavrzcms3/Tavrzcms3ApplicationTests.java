package ru.fds.tavrzcms3;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.fds.tavrzcms3.dictionary.TypeOfAuto;
import ru.fds.tavrzcms3.dictionary.TypeOfCollateral;
import ru.fds.tavrzcms3.dictionary.TypeOfSecurities;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Tavrzcms3ApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void testTypeOfAuto(){
        TypeOfAuto[] typeOfAuto = TypeOfAuto.values();
        for (TypeOfAuto t : typeOfAuto)
            System.out.println(t.ordinal() + ", " + t.toString() + ", " + t.getTranslate());
    }

    @Test
    public void testTypeOfSecurities(){
        TypeOfSecurities[] typeOfSecurities = TypeOfSecurities.values();
        for (TypeOfSecurities t : typeOfSecurities){
            System.out.println(t.ordinal() + ", " + t.name() + ", " + t.getTranslate());
        }
    }

    @Test
    public void testTypeOfCollateral(){
        TypeOfCollateral[] typeOfCollaterals = TypeOfCollateral.values();
        for(TypeOfCollateral t : typeOfCollaterals)
            System.out.println(t.ordinal() + ", " + t.name() + ", " + t.getTranslate());
    }

}
