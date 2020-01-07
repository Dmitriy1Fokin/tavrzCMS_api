package ru.fds.tavrzcms3.fileimport;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface FileImporter {

    String getString(int positionInLine);
    Integer getInteger(int positionInLine);
    Long getLong(int positionInLine);
    Double getDouble(int positionInLine);
    BigDecimal getBigDecimal(int positionInLine);
    List<Long> getLongList(int positionInLine, String delimiter);
    LocalDate getLocalDate(int positionInLine);
    boolean nextLine();

}
