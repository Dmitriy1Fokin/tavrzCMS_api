package ru.fds.tavrzcms3.service;

import ru.fds.tavrzcms3.domain.Encumbrance;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface EncumbranceService {
    Optional<Encumbrance> getEncumbranceById(Long encumbranceId);
    List<Encumbrance> getEncumbranceByPledgeSubject(Long pledgeSubjectId);
    List<Encumbrance> getNewEncumbranceFromFile(File file) throws IOException;
    List<Encumbrance> getCurrentEncumbranceFromFile(File file) throws IOException;
    Encumbrance updateInsertEncumbrance(Encumbrance encumbrance);
    List<Encumbrance> updateInsertEncumbrances(List<Encumbrance> encumbranceList);
}
