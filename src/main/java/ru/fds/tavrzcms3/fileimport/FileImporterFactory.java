package ru.fds.tavrzcms3.fileimport;

import ru.fds.tavrzcms3.dictionary.FileExtension;
import ru.fds.tavrzcms3.fileimport.impl.CsvImporter;
import ru.fds.tavrzcms3.fileimport.impl.ExcelImporter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

public class FileImporterFactory {

    private FileImporterFactory(){}

    public static FileImporter getInstance(File file) throws IOException {
        FileExtension extension = getExtension(file.getName());

        if(extension == FileExtension.XLSX || extension == FileExtension.XLS){
            return new ExcelImporter(new FileInputStream(file), extension);
        }else if(extension == FileExtension.CSV || extension == FileExtension.NONE || extension == FileExtension.TXT){
            return new CsvImporter(new FileReader(file));
        }else{
            return null;
        }

    }

    private static FileExtension getExtension(String fileName){

        int i = fileName.lastIndexOf('.');
        int p = fileName.lastIndexOf(File.separator);

        if (i > p) {
            String extensionString = fileName.substring(i+1);
            if(extensionString.equalsIgnoreCase(FileExtension.XLSX.name())){
                return FileExtension.XLSX;
            }else if(extensionString.equalsIgnoreCase(FileExtension.XLS.name())){
                return FileExtension.XLS;
            }else if(extensionString.equalsIgnoreCase(FileExtension.CSV.name())){
                return FileExtension.CSV;
            }else if(extensionString.equalsIgnoreCase(FileExtension.TXT.name())){
                return FileExtension.TXT;
            }else {
                return FileExtension.UNKNOWN;
            }

        }else {
            return FileExtension.NONE;
        }
    }
}
