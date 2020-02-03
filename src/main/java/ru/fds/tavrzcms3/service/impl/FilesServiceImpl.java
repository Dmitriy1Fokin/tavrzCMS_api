package ru.fds.tavrzcms3.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.fds.tavrzcms3.service.FilesService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Service
public class FilesServiceImpl implements FilesService {

    @Value("${path.excel.import}")
    private String pathExcelImport;


    @Override
    public File uploadFile(MultipartFile file, String prefixName) throws IOException {

        InputStream inputStream = file.getInputStream();

        File inputDir = new File(pathExcelImport);
        String extension = getExtension(Objects.requireNonNull(file.getOriginalFilename()));
        String fileLocation = inputDir
                + File.separator
                + prefixName
                + new SimpleDateFormat("'_'yyyy-MM-dd'_'HH-mm-ss").format(new Date())
                + "." + extension;


        try (FileOutputStream fileOutputStream = new FileOutputStream(fileLocation)){
            int ch;
            while ((ch = inputStream.read()) != -1)
                fileOutputStream.write(ch);

            fileOutputStream.flush();

            return new File(fileLocation);
        }catch (IOException ex){
            throw new IOException(ex.getMessage());
        }
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
