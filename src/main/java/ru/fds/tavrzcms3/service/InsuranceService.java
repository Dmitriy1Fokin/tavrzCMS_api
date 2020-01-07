package ru.fds.tavrzcms3.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fds.tavrzcms3.dictionary.excelproprities.ExcelColumnNum;
import ru.fds.tavrzcms3.domain.Insurance;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.fileimport.FileImporter;
import ru.fds.tavrzcms3.fileimport.FileImporterFactory;
import ru.fds.tavrzcms3.repository.RepositoryInsurance;
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
public class InsuranceService {

    private final RepositoryInsurance repositoryInsurance;
    private final RepositoryPledgeSubject repositoryPledgeSubject;

    private final ValidatorEntity validatorEntity;
    private final ExcelColumnNum excelColumnNum;

    public InsuranceService(RepositoryInsurance repositoryInsurance,
                            RepositoryPledgeSubject repositoryPledgeSubject,
                            ValidatorEntity validatorEntity,
                            ExcelColumnNum excelColumnNum) {
        this.repositoryInsurance = repositoryInsurance;
        this.repositoryPledgeSubject = repositoryPledgeSubject;
        this.validatorEntity = validatorEntity;
        this.excelColumnNum = excelColumnNum;
    }

    public Optional<Insurance> getInsuranceById(Long insuranceId){
        return repositoryInsurance.findById(insuranceId);
    }

    public List<Insurance> getInsurancesByPledgeSubject(PledgeSubject pledgeSubject){
        Sort sortByDateEnd = new Sort(Sort.Direction.DESC, "dateEndInsurance");
        return repositoryInsurance.findAllByPledgeSubject(pledgeSubject, sortByDateEnd);
    }

    public List<Insurance> getInsurancesByIds(Collection<Long> ids){
        return repositoryInsurance.findAllByInsuranceIdIn(ids);
    }

    public List<Insurance> getNewInsurancesFromFile(File file) throws IOException{
        FileImporter fileImporter = FileImporterFactory.getInstance(file);
        for(int i = 0; i < excelColumnNum.getStartRow(); i++){
            fileImporter.nextLine();
        }
        int countRow = excelColumnNum.getStartRow();

        List<Insurance> insuranceList = new ArrayList<>();

        do{
            countRow += 1;

            if(Objects.isNull(fileImporter.getLong(excelColumnNum.getInsuranceNew().getPledgeSubjectId()))){
                throw new IOException("Неверный id{"
                        + fileImporter.getLong(excelColumnNum.getInsuranceNew().getPledgeSubjectId()) + ") предмета залога.");
            }

            Optional<PledgeSubject> pledgeSubject = repositoryPledgeSubject
                    .findById(fileImporter.getLong(excelColumnNum.getInsuranceNew().getPledgeSubjectId()));
            if(!pledgeSubject.isPresent()){
                throw new IOException("Предмет залога с таким id отсутствует ("
                        + fileImporter.getLong(excelColumnNum.getInsuranceNew().getPledgeSubjectId())
                        + "). Строка: " + countRow);
            }

            Insurance insurance = Insurance.builder()
                .numInsurance(fileImporter.getString(excelColumnNum.getInsuranceNew().getNumInsurance()))
                .dateBeginInsurance(fileImporter.getLocalDate(excelColumnNum.getInsuranceNew().getDateBegin()))
                .dateEndInsurance(fileImporter.getLocalDate(excelColumnNum.getInsuranceNew().getDateEnd()))
                .sumInsured(fileImporter.getBigDecimal(excelColumnNum.getInsuranceNew().getSumInsured()))
                .dateInsuranceContract(fileImporter.getLocalDate(excelColumnNum.getInsuranceNew().getDateInsuranceContract()))
                .paymentOfInsurancePremium(fileImporter.getString(excelColumnNum.getInsuranceNew().getPaymentOfInsurancePremium()))
                .franchiseAmount(fileImporter.getBigDecimal(excelColumnNum.getInsuranceNew().getFranchiseAmount()))
                .pledgeSubject(pledgeSubject.get())
                .build();

            Set<ConstraintViolation<Insurance>> violations = validatorEntity.validateEntity(insurance);
            if(!violations.isEmpty())
                throw new IOException("В строке:" + countRow + ". " + validatorEntity.getErrorMessage());

            insuranceList.add(insurance);

        }while (fileImporter.nextLine());

        return insuranceList;
    }

    public List<Insurance> getCurrentInsurancesFromFile(File file) throws IOException{
        FileImporter fileImporter = FileImporterFactory.getInstance(file);
        for(int i = 0; i < excelColumnNum.getStartRow(); i++){
            fileImporter.nextLine();
        }
        int countRow = excelColumnNum.getStartRow();

        List<Insurance> insuranceList = new ArrayList<>();

        do{
            countRow += 1;

            if(Objects.isNull(fileImporter.getLong(excelColumnNum.getInsuranceUpdate().getInsuranceId()))){
                throw new IOException("Неверный id{"
                        + fileImporter.getLong(excelColumnNum.getInsuranceUpdate().getInsuranceId()) + ") договора страхования.");
            }

            Optional<Insurance> insurance = getInsuranceById(fileImporter
                    .getLong(excelColumnNum.getInsuranceUpdate().getInsuranceId()));
            if(!insurance.isPresent()){
                throw new IOException("Договора страхования с таким id отсутствует ("
                        + fileImporter.getLong(excelColumnNum.getInsuranceUpdate().getInsuranceId())
                        + "). Строка: " + countRow);
            }

            if(Objects.isNull(fileImporter.getLong(excelColumnNum.getInsuranceUpdate().getPledgeSubjectId()))){
                throw new IOException("Неверный id{"
                        + fileImporter.getLong(excelColumnNum.getInsuranceUpdate().getPledgeSubjectId()) + ") предмета залога.");
            }

            Optional<PledgeSubject> pledgeSubject = repositoryPledgeSubject
                    .findById(fileImporter.getLong(excelColumnNum.getInsuranceUpdate().getPledgeSubjectId()));
            if(!pledgeSubject.isPresent()){
                throw new IOException("Предмет залога с таким id отсутствует ("
                        + fileImporter.getLong(excelColumnNum.getInsuranceUpdate().getPledgeSubjectId())
                        + "). Строка: " + countRow);
            }

            insurance.get().setNumInsurance(fileImporter.getString(excelColumnNum.getInsuranceUpdate().getNumInsurance()));
            insurance.get().setDateBeginInsurance(fileImporter.getLocalDate(excelColumnNum.getInsuranceUpdate().getDateBegin()));
            insurance.get().setDateEndInsurance(fileImporter.getLocalDate(excelColumnNum.getInsuranceUpdate().getDateEnd()));
            insurance.get().setSumInsured(fileImporter.getBigDecimal(excelColumnNum.getInsuranceUpdate().getSumInsured()));
            insurance.get().setDateInsuranceContract(fileImporter.getLocalDate(excelColumnNum.getInsuranceUpdate().getDateInsuranceContract()));
            insurance.get().setPaymentOfInsurancePremium(fileImporter.getString(excelColumnNum.getInsuranceUpdate().getPaymentOfInsurancePremium()));
            insurance.get().setFranchiseAmount(fileImporter.getBigDecimal(excelColumnNum.getInsuranceUpdate().getFranchiseAmount()));
            insurance.get().setPledgeSubject(pledgeSubject.get());

            Set<ConstraintViolation<Insurance>> violations = validatorEntity.validateEntity(insurance.get());
            if(!violations.isEmpty())
                throw new IOException("В строке:" + countRow + ". " + validatorEntity.getErrorMessage());

            insuranceList.add(insurance.get());

        }while (fileImporter.nextLine());

        return insuranceList;
    }

    @Transactional
    public Insurance updateInsertInsurance(Insurance insurance){
        return repositoryInsurance.save(insurance);
    }

    @Transactional
    public List<Insurance> updateInsertInsurances(List<Insurance> insuranceList){
        return repositoryInsurance.saveAll(insuranceList);
    }
}
