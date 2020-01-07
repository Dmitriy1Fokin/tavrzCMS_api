package ru.fds.tavrzcms3.service;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.fds.tavrzcms3.dictionary.StatusOfAgreement;
import ru.fds.tavrzcms3.dictionary.TypeOfPledgeAgreement;
import ru.fds.tavrzcms3.domain.embedded.ClientIndividual;
import ru.fds.tavrzcms3.domain.embedded.ClientLegalEntity;
import ru.fds.tavrzcms3.domain.LoanAgreement;
import ru.fds.tavrzcms3.domain.PledgeAgreement;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class FilesService {

    @Value("${path.excel.import}")
    private String pathExcelImport;


    public File uploadFile(MultipartFile file, String prefixName) throws IOException{

        InputStream inputStream = file.getInputStream();

        File inputDir = new File(pathExcelImport);
        String extension = getExtension(file.getOriginalFilename());
        String fileLocation = inputDir
                + File.separator
                + prefixName
                + new SimpleDateFormat("'_'yyyy-MM-dd'_'HH-mm-ss").format(new Date())
                + "." + extension;

        FileOutputStream fileOutputStream = new FileOutputStream(fileLocation);
        int ch = 0;
        while ((ch = inputStream.read()) != -1)
            fileOutputStream.write(ch);

        fileOutputStream.flush();
        fileOutputStream.close();

        return new File(fileLocation);
    }

    private String getExtension(String fileName){
        String extension = "";

        int i = fileName.lastIndexOf('.');
        int p = fileName.lastIndexOf(File.separator);

        if (i > p) {
            extension = fileName.substring(i+1);
        }

        return extension;
    }
}
