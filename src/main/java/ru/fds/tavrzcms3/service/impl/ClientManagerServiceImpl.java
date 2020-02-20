package ru.fds.tavrzcms3.service.impl;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fds.tavrzcms3.dictionary.excelproprities.ExcelColumnNum;
import ru.fds.tavrzcms3.domain.ClientManager;
import ru.fds.tavrzcms3.fileimport.FileImporter;
import ru.fds.tavrzcms3.fileimport.FileImporterFactory;
import ru.fds.tavrzcms3.repository.RepositoryClientManager;
import ru.fds.tavrzcms3.service.ClientManagerService;
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
public class ClientManagerServiceImpl implements ClientManagerService {

    private final RepositoryClientManager repositoryClientManager;
    private final ValidatorEntity validatorEntity;
    private final ExcelColumnNum excelColumnNum;

    public ClientManagerServiceImpl(RepositoryClientManager repositoryClientManager,
                                ValidatorEntity validatorEntity,
                                ExcelColumnNum excelColumnNum) {
        this.repositoryClientManager = repositoryClientManager;
        this.validatorEntity = validatorEntity;
        this.excelColumnNum = excelColumnNum;
    }

    @Override
    public List<ClientManager> getAllClientManager(){
        return repositoryClientManager.findAll(Sort.by(Sort.Direction.ASC, "surname"));
    }

    @Override
    public Optional<ClientManager> getClientManagerById(long clientManagerId){
        return repositoryClientManager.findById(clientManagerId);
    }

    @Override
    public Optional<ClientManager> getClientManagerByClient(Long clientId){
        return repositoryClientManager.findByClient(clientId);
    }

    @Override
    @Transactional
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

            Set<ConstraintViolation<ClientManager>> violations =  validatorEntity.validateEntity(clientManager);
            if(!violations.isEmpty())
                throw new ConstraintViolationException("object " + countRow, violations);

            clientManagerList.add(clientManager);

        }while (fileImporter.nextLine());

        clientManagerList = updateInsertClientManagers(clientManagerList);

        return clientManagerList;
    }

    @Override
    @Transactional
    public List<ClientManager>  getCurrentClientManagersFromFile(File file) throws IOException {
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
                        + fileImporter.getLong(excelColumnNum.getClientUpdate().getClientId()) + ") клиентского менеджера. Строка: " + countRow);
            }

            int finalCountRow = countRow;
            ClientManager clientManager = getClientManagerById(fileImporter.getLong(excelColumnNum.getClientManagerUpdate().getClientManagerId()))
                    .orElseThrow(() ->new IOException("Клиентский менеджер с таким id отсутствует ("
                            + fileImporter.getLong(excelColumnNum.getClientManagerUpdate().getClientManagerId())
                            + "). Строка: " + finalCountRow) );

            clientManager.setSurname(fileImporter.getString(excelColumnNum.getClientManagerUpdate().getSurname()));
            clientManager.setName(fileImporter.getString(excelColumnNum.getClientManagerUpdate().getName()));
            clientManager.setPatronymic(fileImporter.getString(excelColumnNum.getClientManagerUpdate().getPatronymic()));

            Set<ConstraintViolation<ClientManager>> violations =  validatorEntity.validateEntity(clientManager);
            if(!violations.isEmpty())
                throw new ConstraintViolationException("object " + countRow, violations);

            clientManagerList.add(clientManager);

        }while (fileImporter.nextLine());

        clientManagerList = updateInsertClientManagers(clientManagerList);

        return clientManagerList;
    }

    @Override
    @Transactional
    public ClientManager insertUpdateClientManager(ClientManager clientManager){
        return repositoryClientManager.save(clientManager);
    }

    @Override
    @Transactional
    public List<ClientManager> updateInsertClientManagers(List<ClientManager> clientManagerList){
        return repositoryClientManager.saveAll(clientManagerList);
    }
}
