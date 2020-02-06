package ru.fds.tavrzcms3.service.impl;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fds.tavrzcms3.dictionary.excelproprities.ExcelColumnNum;
import ru.fds.tavrzcms3.domain.Insurance;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.exception.NotFoundException;
import ru.fds.tavrzcms3.fileimport.FileImporter;
import ru.fds.tavrzcms3.fileimport.FileImporterFactory;
import ru.fds.tavrzcms3.repository.RepositoryInsurance;
import ru.fds.tavrzcms3.repository.RepositoryPledgeSubject;
import ru.fds.tavrzcms3.service.InsuranceService;
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
public class InsuranceServiceImpl implements InsuranceService {

    private final RepositoryInsurance repositoryInsurance;
    private final RepositoryPledgeSubject repositoryPledgeSubject;
    private final ValidatorEntity validatorEntity;
    private final ExcelColumnNum excelColumnNum;

    private static final String MSG_WRONG_ID = "Неверный id{";
    private static final String MSG_LINE = "). Строка: ";

    public InsuranceServiceImpl(RepositoryInsurance repositoryInsurance,
                            RepositoryPledgeSubject repositoryPledgeSubject,
                            ValidatorEntity validatorEntity,
                            ExcelColumnNum excelColumnNum) {
        this.repositoryInsurance = repositoryInsurance;
        this.repositoryPledgeSubject = repositoryPledgeSubject;
        this.validatorEntity = validatorEntity;
        this.excelColumnNum = excelColumnNum;
    }

    @Override
    public Optional<Insurance> getInsuranceById(Long insuranceId){
        return repositoryInsurance.findById(insuranceId);
    }

    @Override
    public List<Insurance> getInsurancesByPledgeSubject(Long pledgeSubjectId){
        return repositoryPledgeSubject.findById(pledgeSubjectId)
                .map(pledgeSubject -> repositoryInsurance.findAllByPledgeSubject(pledgeSubject,
                        new Sort(Sort.Direction.DESC, "dateEndInsurance")))
                .orElseThrow(() -> new NotFoundException("Pledge subject not found"));
    }

    @Override
    @Transactional
    public List<Insurance> getNewInsurancesFromFile(File file) throws IOException{
        FileImporter fileImporter = FileImporterFactory.getInstance(file);
        for(int i = 0; i < excelColumnNum.getStartRow(); i++){
            fileImporter.nextLine();
        }
        int countRow = excelColumnNum.getStartRow();

        List<Insurance> insuranceList = new ArrayList<>();

        do{
            countRow += 1;

            Insurance insurance = Insurance.builder()
                .numInsurance(fileImporter.getString(excelColumnNum.getInsuranceNew().getNumInsurance()))
                .dateBeginInsurance(fileImporter.getLocalDate(excelColumnNum.getInsuranceNew().getDateBegin()))
                .dateEndInsurance(fileImporter.getLocalDate(excelColumnNum.getInsuranceNew().getDateEnd()))
                .sumInsured(fileImporter.getBigDecimal(excelColumnNum.getInsuranceNew().getSumInsured()))
                .dateInsuranceContract(fileImporter.getLocalDate(excelColumnNum.getInsuranceNew().getDateInsuranceContract()))
                .paymentOfInsurancePremium(fileImporter.getString(excelColumnNum.getInsuranceNew().getPaymentOfInsurancePremium()))
                .franchiseAmount(fileImporter.getBigDecimal(excelColumnNum.getInsuranceNew().getFranchiseAmount()))
                .pledgeSubject(setPledgeSubjectInNewInsurance(fileImporter, countRow))
                .build();

            Set<ConstraintViolation<Insurance>> violations =  validatorEntity.validateEntity(insurance);
            if(!violations.isEmpty())
                throw new ConstraintViolationException("object " + countRow, violations);

            insuranceList.add(insurance);

        }while (fileImporter.nextLine());

        insuranceList = updateInsertInsurances(insuranceList);

        return insuranceList;
    }

    private PledgeSubject setPledgeSubjectInNewInsurance(FileImporter fileImporter, int countRow) throws IOException {
        if(Objects.isNull(fileImporter.getLong(excelColumnNum.getInsuranceNew().getPledgeSubjectId()))){
            throw new IOException(MSG_WRONG_ID
                    + fileImporter.getLong(excelColumnNum.getInsuranceNew().getPledgeSubjectId())
                    + ") предмета залога. Строка: " + countRow);
        }

        return repositoryPledgeSubject.findById(fileImporter.getLong(excelColumnNum.getInsuranceNew().getPledgeSubjectId()))
                .orElseThrow(() -> new IOException("Предмет залога с таким id отсутствует ("
                        + fileImporter.getLong(excelColumnNum.getInsuranceNew().getPledgeSubjectId())
                        + MSG_LINE + countRow));
    }

    @Override
    @Transactional
    public List<Insurance> getCurrentInsurancesFromFile(File file) throws IOException{
        FileImporter fileImporter = FileImporterFactory.getInstance(file);
        for(int i = 0; i < excelColumnNum.getStartRow(); i++){
            fileImporter.nextLine();
        }
        int countRow = excelColumnNum.getStartRow();

        List<Insurance> insuranceList = new ArrayList<>();

        do{
            countRow += 1;

            Insurance insurance = setCurrentInsurance(fileImporter, countRow);

            insurance.setNumInsurance(fileImporter.getString(excelColumnNum.getInsuranceUpdate().getNumInsurance()));
            insurance.setDateBeginInsurance(fileImporter.getLocalDate(excelColumnNum.getInsuranceUpdate().getDateBegin()));
            insurance.setDateEndInsurance(fileImporter.getLocalDate(excelColumnNum.getInsuranceUpdate().getDateEnd()));
            insurance.setSumInsured(fileImporter.getBigDecimal(excelColumnNum.getInsuranceUpdate().getSumInsured()));
            insurance.setDateInsuranceContract(fileImporter.getLocalDate(excelColumnNum.getInsuranceUpdate().getDateInsuranceContract()));
            insurance.setPaymentOfInsurancePremium(fileImporter.getString(excelColumnNum.getInsuranceUpdate().getPaymentOfInsurancePremium()));
            insurance.setFranchiseAmount(fileImporter.getBigDecimal(excelColumnNum.getInsuranceUpdate().getFranchiseAmount()));
            insurance.setPledgeSubject(setPledgeSubjectInCurrentInsurance(fileImporter, countRow));

            Set<ConstraintViolation<Insurance>> violations =  validatorEntity.validateEntity(insurance);
            if(!violations.isEmpty())
                throw new ConstraintViolationException("object " + countRow, violations);

            insuranceList.add(insurance);

        }while (fileImporter.nextLine());

        insuranceList = updateInsertInsurances(insuranceList);

        return insuranceList;
    }

    private Insurance setCurrentInsurance(FileImporter fileImporter, int countRow) throws IOException {
        if(Objects.isNull(fileImporter.getLong(excelColumnNum.getInsuranceUpdate().getInsuranceId()))){
            throw new IOException(MSG_WRONG_ID
                    + fileImporter.getLong(excelColumnNum.getInsuranceUpdate().getInsuranceId())
                    + ") договора страхования. Строка: " + countRow);
        }

        return getInsuranceById(fileImporter.getLong(excelColumnNum.getInsuranceUpdate().getInsuranceId()))
                .orElseThrow(() -> new IOException("Договора страхования с таким id отсутствует ("
                        + fileImporter.getLong(excelColumnNum.getInsuranceUpdate().getInsuranceId())
                        + MSG_LINE + countRow));
    }

    private PledgeSubject setPledgeSubjectInCurrentInsurance(FileImporter fileImporter, int countRow) throws IOException {
        if(Objects.isNull(fileImporter.getLong(excelColumnNum.getInsuranceUpdate().getPledgeSubjectId()))){
            throw new IOException(MSG_WRONG_ID
                    + fileImporter.getLong(excelColumnNum.getInsuranceUpdate().getPledgeSubjectId())
                    + ") предмета залога. Строка: " + countRow);
        }

        return repositoryPledgeSubject
                .findById(fileImporter.getLong(excelColumnNum.getInsuranceUpdate().getPledgeSubjectId()))
                .orElseThrow(() -> new IOException("Предмет залога с таким id отсутствует ("
                        + fileImporter.getLong(excelColumnNum.getInsuranceUpdate().getPledgeSubjectId())
                        + MSG_LINE + countRow));
    }


    @Override
    @Transactional
    public Insurance updateInsertInsurance(Insurance insurance){
        return repositoryInsurance.save(insurance);
    }

    @Override
    @Transactional
    public List<Insurance> updateInsertInsurances(List<Insurance> insuranceList){
        return repositoryInsurance.saveAll(insuranceList);
    }
}
