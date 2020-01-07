package ru.fds.tavrzcms3.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fds.tavrzcms3.dictionary.TypeOfEncumbrance;
import ru.fds.tavrzcms3.dictionary.excelproprities.ExcelColumnNum;
import ru.fds.tavrzcms3.domain.Encumbrance;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.fileimport.FileImporter;
import ru.fds.tavrzcms3.fileimport.FileImporterFactory;
import ru.fds.tavrzcms3.repository.RepositoryEncumbrance;
import ru.fds.tavrzcms3.repository.RepositoryPledgeSubject;
import ru.fds.tavrzcms3.validate.ValidatorEntity;

import javax.validation.ConstraintViolation;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class EncumbranceService {

    private final RepositoryEncumbrance repositoryEncumbrance;
    private final RepositoryPledgeSubject repositoryPledgeSubject;

    private final ValidatorEntity validatorEntity;
    private final ExcelColumnNum excelColumnNum;

    public EncumbranceService(RepositoryEncumbrance repositoryEncumbrance,
                              RepositoryPledgeSubject repositoryPledgeSubject,
                              ValidatorEntity validatorEntity,
                              ExcelColumnNum excelColumnNum) {
        this.repositoryEncumbrance = repositoryEncumbrance;
        this.repositoryPledgeSubject = repositoryPledgeSubject;
        this.validatorEntity = validatorEntity;
        this.excelColumnNum = excelColumnNum;
    }

    public Optional<Encumbrance> getEncumbranceById(Long encumbranceId){
        return repositoryEncumbrance.findById(encumbranceId);
    }
    public List<Encumbrance> getEncumbranceByPledgeSubject(PledgeSubject pledgeSubject){
        return repositoryEncumbrance.findAllByPledgeSubject(pledgeSubject);
    }

    public List<Encumbrance> getEncumbranceByIds(Collection<Long> ids){
        return repositoryEncumbrance.findAllByEncumbranceIdIn(ids);
    }

    public List<Encumbrance> getNewEncumbranceFromFile(File file) throws IOException{
        FileImporter fileImporter = FileImporterFactory.getInstance(file);
        for(int i = 0; i < excelColumnNum.getStartRow(); i++){
            fileImporter.nextLine();
        }
        int countRow = excelColumnNum.getStartRow();

        List<Encumbrance> encumbranceList = new ArrayList<>();

        do{
            countRow += 1;

            if(Objects.isNull(fileImporter.getLong(excelColumnNum.getEncumbranceNew().getPledgeSubjectId()))){
                throw new IOException("Неверный id{"
                        + fileImporter.getLong(excelColumnNum.getEncumbranceNew().getPledgeSubjectId()) + ") предмета залога.");
            }

            Optional<PledgeSubject> pledgeSubject = repositoryPledgeSubject
                    .findById(fileImporter.getLong(excelColumnNum.getEncumbranceNew().getPledgeSubjectId()));
            if(!pledgeSubject.isPresent()){
                throw new IOException("Предмет залога с таким id отсутствует ("
                        + fileImporter.getLong(excelColumnNum.getEncumbranceNew().getPledgeSubjectId())
                        + "). Строка: " + countRow);
            }

            Encumbrance encumbrance = Encumbrance.builder()
                    .nameEncumbrance(fileImporter.getString(excelColumnNum.getEncumbranceNew().getName()))
                    .typeOfEncumbrance(TypeOfEncumbrance.valueOf(fileImporter.getString(excelColumnNum.getEncumbranceNew().getTypeOfEncumbrance())))
                    .inFavorOfWhom(fileImporter.getString(excelColumnNum.getEncumbranceNew().getInFavorOfWhom()))
                    .dateBegin(fileImporter.getLocalDate(excelColumnNum.getEncumbranceNew().getDateBegin()))
                    .dateEnd(fileImporter.getLocalDate(excelColumnNum.getEncumbranceNew().getDateEnd()))
                    .numOfEncumbrance(fileImporter.getString(excelColumnNum.getEncumbranceNew().getNumOfEncumbrance()))
                    .pledgeSubject(pledgeSubject.get())
                    .build();

            Set<ConstraintViolation<Encumbrance>> violations = validatorEntity.validateEntity(encumbrance);
            if(!violations.isEmpty())
                throw new IOException("В строке:" + countRow + ". " + validatorEntity.getErrorMessage());

            encumbranceList.add(encumbrance);

        }while (fileImporter.nextLine());

        return encumbranceList;
    }

    public List<Encumbrance> getCurrentEncumbranceFromFile(File file) throws IOException{
        FileImporter fileImporter = FileImporterFactory.getInstance(file);
        for(int i = 0; i < excelColumnNum.getStartRow(); i++){
            fileImporter.nextLine();
        }
        int countRow = excelColumnNum.getStartRow();

        List<Encumbrance> encumbranceList = new ArrayList<>();

        do{
            countRow += 1;

            if(Objects.isNull(fileImporter.getLong(excelColumnNum.getEncumbranceUpdate().getEncumbranceId()))){
                throw new IOException("Неверный id{"
                        + fileImporter.getLong(excelColumnNum.getEncumbranceUpdate().getEncumbranceId()) + ") обременения.");
            }

            Optional<Encumbrance> encumbrance = getEncumbranceById(fileImporter
                    .getLong(excelColumnNum.getEncumbranceUpdate().getEncumbranceId()));
            if(!encumbrance.isPresent()){
                throw new IOException("Обременение с таким id отсутствует ("
                        + fileImporter.getLong(excelColumnNum.getEncumbranceUpdate().getEncumbranceId())
                        + "). Строка: " + countRow);
            }

            if(Objects.isNull(fileImporter.getLong(excelColumnNum.getEncumbranceUpdate().getPledgeSubjectId()))){
                throw new IOException("Неверный id{"
                        + fileImporter.getLong(excelColumnNum.getEncumbranceUpdate().getPledgeSubjectId()) + ") предмета залога.");
            }

            Optional<PledgeSubject> pledgeSubject = repositoryPledgeSubject
                    .findById(fileImporter.getLong(excelColumnNum.getEncumbranceUpdate().getPledgeSubjectId()));
            if(!pledgeSubject.isPresent()){
                throw new IOException("Предмет залога с таким id отсутствует ("
                        + fileImporter.getLong(excelColumnNum.getEncumbranceUpdate().getPledgeSubjectId())
                        + "). Строка: " + countRow);
            }

            encumbrance.get().setNameEncumbrance(fileImporter.getString(excelColumnNum.getEncumbranceUpdate().getName()));
            encumbrance.get().setTypeOfEncumbrance(TypeOfEncumbrance.valueOf(fileImporter.getString(excelColumnNum.getEncumbranceUpdate().getTypeOfEncumbrance())));
            encumbrance.get().setInFavorOfWhom(fileImporter.getString(excelColumnNum.getEncumbranceUpdate().getInFavorOfWhom()));
            encumbrance.get().setDateBegin(fileImporter.getLocalDate(excelColumnNum.getEncumbranceUpdate().getDateBegin()));
            encumbrance.get().setDateEnd(fileImporter.getLocalDate(excelColumnNum.getEncumbranceUpdate().getDateEnd()));
            encumbrance.get().setNumOfEncumbrance(fileImporter.getString(excelColumnNum.getEncumbranceUpdate().getNumOfEncumbrance()));
            encumbrance.get().setPledgeSubject(pledgeSubject.get());

            Set<ConstraintViolation<Encumbrance>> violations = validatorEntity.validateEntity(encumbrance.get());
            if(!violations.isEmpty())
                throw new IOException("В строке:" + countRow + ". " + validatorEntity.getErrorMessage());

            encumbranceList.add(encumbrance.get());

        }while (fileImporter.nextLine());

        return encumbranceList;
    }

    @Transactional
    public Encumbrance updateInsertEncumbrance(Encumbrance encumbrance){
        return repositoryEncumbrance.save(encumbrance);
    }

    @Transactional
    public List<Encumbrance> updateInsertEncumbrances(List<Encumbrance> encumbranceList){
        return repositoryEncumbrance.saveAll(encumbranceList);
    }
}
