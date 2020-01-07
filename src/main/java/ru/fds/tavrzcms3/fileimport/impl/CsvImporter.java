package ru.fds.tavrzcms3.fileimport.impl;

import ru.fds.tavrzcms3.fileimport.FileImporter;

import java.io.Reader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class CsvImporter implements FileImporter {

    private final Reader reader;

    public CsvImporter(Reader reader) {
        this.reader = reader;
    }


    @Override
    public String getString(int positionInLine) {
        return "";
    }

    @Override
    public Integer getInteger(int positionInLine) {
        return null;
    }

    @Override
    public Long getLong(int positionInLine) {
        return null;
    }

    @Override
    public Double getDouble(int positionInLine) {
        return null;
    }

    @Override
    public BigDecimal getBigDecimal(int positionInLine) {
        return null;
    }

    @Override
    public List<Long> getLongList(int positionInLine, String delimeter) {
        return Collections.emptyList();
    }

    @Override
    public LocalDate getLocalDate(int positionInLine) {
        return null;
    }

    @Override
    public boolean nextLine() {
        return false;
    }
}
