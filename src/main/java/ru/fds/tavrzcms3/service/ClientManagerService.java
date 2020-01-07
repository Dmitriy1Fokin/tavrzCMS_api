package ru.fds.tavrzcms3.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fds.tavrzcms3.dictionary.excelproprities.ExcelColumnNum;
import ru.fds.tavrzcms3.domain.ClientManager;
import ru.fds.tavrzcms3.fileimport.FileImporter;
import ru.fds.tavrzcms3.fileimport.FileImporterFactory;
import ru.fds.tavrzcms3.repository.RepositoryClientManager;
import ru.fds.tavrzcms3.validate.ValidatorEntity;

import javax.validation.ConstraintViolation;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class ClientManagerService {

    private final RepositoryClientManager repositoryClientManager;

    private final ValidatorEntity validatorEntity;
    private final ExcelColumnNum excelColumnNum;

    public ClientManagerService(RepositoryClientManager repositoryClientManager,
                                ValidatorEntity validatorEntity,
                                ExcelColumnNum excelColumnNum) {
        this.repositoryClientManager = repositoryClientManager;
        this.validatorEntity = validatorEntity;
        this.excelColumnNum = excelColumnNum;
    }

    public List<ClientManager> getAllClientManager(){
        Sort sortByDateSurname = new Sort(Sort.Direction.ASC, "surname");
        return repositoryClientManager.findAll(sortByDateSurname);
    }

    public Optional<ClientManager> getClientManagerById(long clientManagerId){
        return repositoryClientManager.findById(clientManagerId);
    }

    public List<ClientManager> getNewClientManagersFromFile(File file) throws IOException {
        FileImporter fileImporter = FileImporterFactory.getInstance(file);
        for(int i = 0; i < excelColumnNum.getStartRow(); i++){
            fileImporter.nextLine();
        }
        int countRow = excelColumnNum.getStartRow();

        List<ClientManager> clientManagerList = new ArrayList<>();

        do{
            countRow += 1;

            ClientManager clientManager = ClientManager.builder()
                    .surname(fileImporter.getString(excelColumnNum.getClientManagerNew().getSurname()))
                    .name(fileImporter.getString(excelColumnNum.getClientManagerNew().getName()))
                    .patronymic(fileImporter.getString(excelColumnNum.getClientManagerNew().getPatronymic()))
                    .build();

//            Set<ConstraintViolation<ClientManager>> violations = validatorEntity.validateEntity(clientManager);
//            if(!violations.isEmpty())
//                throw new IOException("В строке:" + countRow + ". " + validatorEntity.getErrorMessage());

            clientManagerList.add(clientManager);

        }while (fileImporter.nextLine());

        return clientManagerList;
    }

    public List<ClientManager> getCurrentClientMAnagersFromFile(File file) throws IOException {
        FileImporter fileImporter = FileImporterFactory.getInstance(file);
        for(int i = 0; i < excelColumnNum.getStartRow(); i++){
            fileImporter.nextLine();
        }
        int countRow = excelColumnNum.getStartRow();

        List<ClientManager> clientManagerList = new ArrayList<>();

        do{
            countRow += 1;

            if(Objects.isNull(fileImporter.getLong(excelColumnNum.getClientManagerUpdate().getClientManagerId()))){
                throw new IOException("Неверный id{"
                        + fileImporter.getLong(excelColumnNum.getClientUpdate().getClientId()) + ") клиентского менеджера.");
            }

            Optional<ClientManager> clientManager = getClientManagerById(fileImporter.getLong(excelColumnNum.getClientManagerUpdate().getClientManagerId()));
            if(!clientManager.isPresent()){
                throw new IOException("Клиентский менеджер с таким id отсутствует ("
                        + fileImporter.getLong(excelColumnNum.getClientManagerUpdate().getClientManagerId())
                        + "). Строка: " + countRow);
            }

            clientManager.get().setSurname(fileImporter.getString(excelColumnNum.getClientManagerUpdate().getSurname()));
            clientManager.get().setName(fileImporter.getString(excelColumnNum.getClientManagerUpdate().getName()));
            clientManager.get().setPatronymic(fileImporter.getString(excelColumnNum.getClientManagerUpdate().getPatronymic()));



            Set<ConstraintViolation<ClientManager>> violations = validatorEntity.validateEntity(clientManager.get());
            if(!violations.isEmpty())
                throw new IOException("В строке:" + countRow + ". " + validatorEntity.getErrorMessage());

            clientManagerList.add(clientManager.get());

        }while (fileImporter.nextLine());

        return clientManagerList;
    }

    @Transactional
    public List<ClientManager> insertClientManagers(List<ClientManager> clientManagerList){
        return repositoryClientManager.saveAll(clientManagerList);
    }
}
