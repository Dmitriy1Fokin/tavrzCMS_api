package ru.fds.tavrzcms3.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fds.tavrzcms3.dictionary.TypeOfEncumbrance;
import ru.fds.tavrzcms3.dictionary.excelproprities.ExcelColumnNum;
import ru.fds.tavrzcms3.domain.Encumbrance;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.exception.NotFoundException;
import ru.fds.tavrzcms3.fileimport.FileImporter;
import ru.fds.tavrzcms3.fileimport.FileImporterFactory;
import ru.fds.tavrzcms3.repository.RepositoryEncumbrance;
import ru.fds.tavrzcms3.repository.RepositoryPledgeSubject;
import ru.fds.tavrzcms3.service.EncumbranceService;
import ru.fds.tavrzcms3.validate.ValidatorEntity;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class EncumbranceServiceImpl implements EncumbranceService {


    private final RepositoryEncumbrance repositoryEncumbrance;
    private final RepositoryPledgeSubject repositoryPledgeSubject;
    private final ValidatorEntity validatorEntity;
    private final ExcelColumnNum excelColumnNum;

    private static final String MSG_WRONG_ID = "Неверный id{";
    private static final String MSG_LINE = "). Строка: ";

    public EncumbranceServiceImpl(RepositoryEncumbrance repositoryEncumbrance,
                              RepositoryPledgeSubject repositoryPledgeSubject,
                              ValidatorEntity validatorEntity,
                              ExcelColumnNum excelColumnNum) {
        this.repositoryEncumbrance = repositoryEncumbrance;
        this.repositoryPledgeSubject = repositoryPledgeSubject;
        this.validatorEntity = validatorEntity;
        this.excelColumnNum = excelColumnNum;
    }

    @Override
    public Optional<Encumbrance> getEncumbranceById(Long encumbranceId){
        return repositoryEncumbrance.findById(encumbranceId);
    }
    @Override public List<Encumbrance> getEncumbranceByPledgeSubject(Long pledgeSubjectId){
        return repositoryPledgeSubject.findById(pledgeSubjectId).map(repositoryEncumbrance::findAllByPledgeSubject)
                .orElseThrow(() -> new NotFoundException("Pledge subject not found"));
    }

    @Override
    @Transactional
    public List<Encumbrance> getNewEncumbranceFromFile(File file) throws IOException{
        FileImporter fileImporter = FileImporterFactory.getInstance(file);
        for(int i = 0; i < excelColumnNum.getStartRow(); i++){
            fileImporter.nextLine();
        }
        int countRow = excelColumnNum.getStartRow();

        List<Encumbrance> encumbranceList = new ArrayList<>();

        do{
            countRow += 1;

            Encumbrance encumbrance = Encumbrance.builder()
                    .nameEncumbrance(fileImporter.getString(excelColumnNum.getEncumbranceNew().getName()))
                    .typeOfEncumbrance(TypeOfEncumbrance.valueOfString(fileImporter
                            .getString(excelColumnNum.getEncumbranceNew().getTypeOfEncumbrance())).orElse(null))
                    .inFavorOfWhom(fileImporter.getString(excelColumnNum.getEncumbranceNew().getInFavorOfWhom()))
                    .dateBegin(fileImporter.getLocalDate(excelColumnNum.getEncumbranceNew().getDateBegin()))
                    .dateEnd(fileImporter.getLocalDate(excelColumnNum.getEncumbranceNew().getDateEnd()))
                    .numOfEncumbrance(fileImporter.getString(excelColumnNum.getEncumbranceNew().getNumOfEncumbrance()))
                    .pledgeSubject(setPledgeSubjectInNewEncumbrance(fileImporter, countRow))
                    .build();

            Set<ConstraintViolation<Encumbrance>> violations =  validatorEntity.validateEntity(encumbrance);
            if(!violations.isEmpty())
                throw new ConstraintViolationException("object " + countRow, violations);

            encumbranceList.add(encumbrance);

        }while (fileImporter.nextLine());

        encumbranceList = updateInsertEncumbrances(encumbranceList);

        return encumbranceList;
    }

    private PledgeSubject setPledgeSubjectInNewEncumbrance(FileImporter fileImporter, int countRow) throws IOException {
        if(Objects.isNull(fileImporter.getLong(excelColumnNum.getEncumbranceNew().getPledgeSubjectId()))){
            throw new IOException(MSG_WRONG_ID
                    + fileImporter.getLong(excelColumnNum.getEncumbranceNew().getPledgeSubjectId())
                    + ") предмета залога. Строка: " + countRow);
        }

        return repositoryPledgeSubject.findById(fileImporter.getLong(excelColumnNum.getEncumbranceNew().getPledgeSubjectId()))
                .orElseThrow(() -> new IOException("Предмет залога с таким id отсутствует ("
                        + fileImporter.getLong(excelColumnNum.getEncumbranceNew().getPledgeSubjectId())
                        + MSG_LINE + countRow));
    }

    @Override
    @Transactional
    public List<Encumbrance> getCurrentEncumbranceFromFile(File file) throws IOException{
        FileImporter fileImporter = FileImporterFactory.getInstance(file);
        for(int i = 0; i < excelColumnNum.getStartRow(); i++){
            fileImporter.nextLine();
        }
        int countRow = excelColumnNum.getStartRow();

        List<Encumbrance> encumbranceList = new ArrayList<>();

        do{
            countRow += 1;

            Encumbrance encumbrance = setCurrentEncumbrance(fileImporter, countRow);

            encumbrance.setNameEncumbrance(fileImporter.getString(excelColumnNum.getEncumbranceUpdate().getName()));
            encumbrance.setTypeOfEncumbrance(TypeOfEncumbrance.valueOfString(fileImporter
                    .getString(excelColumnNum.getEncumbranceUpdate().getTypeOfEncumbrance())).orElse(null));
            encumbrance.setInFavorOfWhom(fileImporter.getString(excelColumnNum.getEncumbranceUpdate().getInFavorOfWhom()));
            encumbrance.setDateBegin(fileImporter.getLocalDate(excelColumnNum.getEncumbranceUpdate().getDateBegin()));
            encumbrance.setDateEnd(fileImporter.getLocalDate(excelColumnNum.getEncumbranceUpdate().getDateEnd()));
            encumbrance.setNumOfEncumbrance(fileImporter.getString(excelColumnNum.getEncumbranceUpdate().getNumOfEncumbrance()));
            encumbrance.setPledgeSubject(setPledgeSubjectInCurrentEncumbrance(fileImporter, countRow));

            Set<ConstraintViolation<Encumbrance>> violations =  validatorEntity.validateEntity(encumbrance);
            if(!violations.isEmpty())
                throw new ConstraintViolationException("object " + countRow, violations);

            encumbranceList.add(encumbrance);

        }while (fileImporter.nextLine());

        encumbranceList = updateInsertEncumbrances(encumbranceList);

        return encumbranceList;
    }

    private Encumbrance setCurrentEncumbrance(FileImporter fileImporter, int countRow) throws IOException {
        if(Objects.isNull(fileImporter.getLong(excelColumnNum.getEncumbranceUpdate().getEncumbranceId()))){
            throw new IOException(MSG_WRONG_ID
                    + fileImporter.getLong(excelColumnNum.getEncumbranceUpdate().getEncumbranceId())
                    + ") обременения. Строка: " + countRow);
        }

        return getEncumbranceById(fileImporter.getLong(excelColumnNum.getEncumbranceUpdate().getEncumbranceId()))
                .orElseThrow(() -> new IOException("Обременение с таким id отсутствует ("
                        + fileImporter.getLong(excelColumnNum.getEncumbranceUpdate().getEncumbranceId())
                        + MSG_LINE + countRow));
    }

    private PledgeSubject setPledgeSubjectInCurrentEncumbrance(FileImporter fileImporter, int countRow) throws IOException {
        if(Objects.isNull(fileImporter.getLong(excelColumnNum.getEncumbranceUpdate().getPledgeSubjectId()))){
            throw new IOException(MSG_WRONG_ID
                    + fileImporter.getLong(excelColumnNum.getEncumbranceUpdate().getPledgeSubjectId())
                    + ") предмета залога. Строка: " + countRow);
        }

        return repositoryPledgeSubject.findById(fileImporter.getLong(excelColumnNum.getEncumbranceUpdate().getPledgeSubjectId()))
                .orElseThrow(() -> new IOException("Предмет залога с таким id отсутствует ("
                        + fileImporter.getLong(excelColumnNum.getEncumbranceUpdate().getPledgeSubjectId())
                        + MSG_LINE + countRow));
    }

    @Override
    @Transactional
    public Encumbrance updateInsertEncumbrance(Encumbrance encumbrance){
        return repositoryEncumbrance.save(encumbrance);
    }

    @Override
    @Transactional
    public List<Encumbrance> updateInsertEncumbrances(List<Encumbrance> encumbranceList){
        return repositoryEncumbrance.saveAll(encumbranceList);
    }
}
