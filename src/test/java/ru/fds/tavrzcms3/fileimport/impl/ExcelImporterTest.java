package ru.fds.tavrzcms3.fileimport.impl;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.fds.tavrzcms3.dictionary.FileExtension;
import ru.fds.tavrzcms3.fileimport.FileImporter;
import ru.fds.tavrzcms3.fileimport.FileImporterFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log4j2
public class ExcelImporterTest {

    @Test
    public void excelImporterTest(){
        File file = new File("D:\\workSpace\\data\\testExcelImporter.xlsx");
        try {
//            FileImporter fileImporter = FileImporterFactory.getInstance(file);
            FileImporter fileImporter = new ExcelImporter(new FileInputStream(file), FileExtension.XLSX);
            List<List<Object>> listList = new ArrayList<>();

            do {
                List<Object> objectList = new ArrayList<>();

                String str = fileImporter.getString(0);
                objectList.add(str);

                Integer integer = fileImporter.getInteger(1);
                objectList.add(integer);

                Long aLong = fileImporter.getLong(2);
                objectList.add(aLong);

                Double aDouble = fileImporter.getDouble(3);
                objectList.add(aDouble);

                BigDecimal bigDecimal = fileImporter.getBigDecimal(4);
                objectList.add(bigDecimal);

                List<Long> longList = fileImporter.getLongList(5, ";");
                objectList.add(longList);

                LocalDate localDate = fileImporter.getLocalDate(6);
                objectList.add(localDate);

//                log.info(objectList);
                listList.add(objectList);

            }while (fileImporter.nextLine());

            for(List list : listList){
                System.out.println(list);
            }

            assertEquals( 5, listList.size());

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}