package ru.fds.tavrzcms3.fileimport.impl;

import lombok.extern.log4j.Log4j2;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.fds.tavrzcms3.dictionary.FileExtension;
import ru.fds.tavrzcms3.fileimport.FileImporter;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Log4j2
public class ExcelImporter implements FileImporter{
    private final Sheet sheet;
    private Row row;

    public ExcelImporter(InputStream inputStream, FileExtension fileExtension) throws IOException {
        Workbook workbook;
        if(fileExtension == FileExtension.XLSX) {
            workbook = new XSSFWorkbook(inputStream);
            sheet = workbook.getSheetAt(0);
            row = sheet.getRow(0);
        }
        else if(fileExtension == FileExtension.XLS){
            workbook = new HSSFWorkbook(inputStream);
            sheet = workbook.getSheetAt(0);
            row = sheet.getRow(0);
        }
        else
            throw new IOException("Неверный формат файла");

        if(Objects.isNull(row)){
            log.info("EMPTY FILE");
            throw new IOException("Empty file");
        }
    }

    @Override
    public String getString(int positionInLine) {
        if(isCellEmpty(row.getCell(positionInLine)))
            return "";

        if(row.getCell(positionInLine).getCellType() == CellType.NUMERIC){
            return String.valueOf(row.getCell(positionInLine).getNumericCellValue());
        }
        if(row.getCell(positionInLine).getCellType() == CellType.STRING){
            return row.getCell(positionInLine).getStringCellValue();
        }
        if(row.getCell(positionInLine).getCellType() == CellType.FORMULA){
            return row.getCell(positionInLine).getCellFormula();
        }
        if(row.getCell(positionInLine).getCellType() == CellType.BOOLEAN){
            return String.valueOf(row.getCell(positionInLine).getBooleanCellValue());
        }
        if(DateUtil.isCellDateFormatted(row.getCell(positionInLine))){
            return String.valueOf(row.getCell(positionInLine).getDateCellValue());
        }
        return "";
    }

    @Override
    public Integer getInteger(int positionInLine) {
        try {
            if(row.getCell(positionInLine).getCellType() == CellType.NUMERIC){
                return (int)row.getCell(positionInLine).getNumericCellValue();
            }
        }catch (Exception ex){
            log.info(ex);
            return null;
        }

        return null;
    }

    @Override
    public Long getLong(int positionInLine) {
        try {
            if(row.getCell(positionInLine).getCellType() == CellType.NUMERIC){
                return (long)row.getCell(positionInLine).getNumericCellValue();
            }
        }catch (Exception ex){
            log.info(ex);
            return null;
        }
        return null;
    }

    @Override
    public Double getDouble(int positionInLine) {
        try{
            if(row.getCell(positionInLine).getCellType() == CellType.NUMERIC){
                return row.getCell(positionInLine).getNumericCellValue();
            }
        }catch (Exception ex){
            log.info(ex);
            return null;
        }
        return null;
    }

    @Override
    public BigDecimal getBigDecimal(int positionInLine) {
        try {
            if(row.getCell(positionInLine).getCellType() == CellType.NUMERIC){
                return BigDecimal.valueOf(row.getCell(positionInLine).getNumericCellValue());
            }
        }catch (Exception ex){
            log.info(ex);
            return null;
        }
        return null;
    }

    @Override
    public List<Long> getLongList(int positionInLine, String delimiter) {
        try {
                String str = getString(positionInLine);
                str = str.replaceAll("\\s+", "");
                String[] longs = str.split(delimiter);
                List<Long> longList = new ArrayList<>();
                for(String s : longs)
                    longList.add(Double.valueOf(s).longValue());

                return longList;

        }catch (Exception ex){
            log.info(ex);
            return Collections.emptyList();
        }
    }

    @Override
    public LocalDate getLocalDate(int positionInLine) {
        try {
            if(DateUtil.isCellDateFormatted(row.getCell(positionInLine))){
                return row.getCell(positionInLine).getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            }
        }catch (Exception ex){
            log.info(ex);
            return null;
        }
        return null;
    }

    @Override
    public boolean nextLine() {
        row = sheet.getRow(row.getRowNum() + 1);

        return Objects.nonNull(row) && row.getRowNum() <= sheet.getLastRowNum();
    }

    private boolean isCellEmpty(Cell cell){

        return (cell == null) || (cell.getCellType() == CellType.BLANK) || (cell.getCellType() == CellType.STRING && cell.getStringCellValue().trim().isEmpty());
    }
}
