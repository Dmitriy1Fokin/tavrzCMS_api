package ru.fds.tavrzcms3.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fds.tavrzcms3.dictionary.excelproprities.ExcelColumnNum;
import ru.fds.tavrzcms3.domain.ClientManager;
import ru.fds.tavrzcms3.fileimport.FileImporter;
import ru.fds.tavrzcms3.fileimport.FileImporterFactory;
import ru.fds.tavrzcms3.repository.RepositoryClientManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ClientManagerService {

    private final RepositoryClientManager repositoryClientManager;

    private final ExcelColumnNum excelColumnNum;

    public ClientManagerService(RepositoryClientManager repositoryClientManager,
                                ExcelColumnNum excelColumnNum) {
        this.repositoryClientManager = repositoryClientManager;
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

        List<ClientManager> clientManagerList = new ArrayList<>();

        do{
            ClientManager clientManager = ClientManager.builder()
                    .surname(fileImporter.getString(excelColumnNum.getClientManagerNew().getSurname()))
                    .name(fileImporter.getString(excelColumnNum.getClientManagerNew().getName()))
                    .patronymic(fileImporter.getString(excelColumnNum.getClientManagerNew().getPatronymic()))
                    .build();

            clientManagerList.add(clientManager);

        }while (fileImporter.nextLine());

        return clientManagerList;
    }

    public List<ClientManager> getCurrentClientManagersFromFile(File file) throws IOException {
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

            Optional<ClientManager> clientManager = getClientManagerById(fileImporter.getLong(excelColumnNum.getClientManagerUpdate().getClientManagerId()));
            if(!clientManager.isPresent()){
                throw new IOException("Клиентский менеджер с таким id отсутствует ("
                        + fileImporter.getLong(excelColumnNum.getClientManagerUpdate().getClientManagerId())
                        + "). Строка: " + countRow);
            }

            clientManager.get().setSurname(fileImporter.getString(excelColumnNum.getClientManagerUpdate().getSurname()));
            clientManager.get().setName(fileImporter.getString(excelColumnNum.getClientManagerUpdate().getName()));
            clientManager.get().setPatronymic(fileImporter.getString(excelColumnNum.getClientManagerUpdate().getPatronymic()));

            clientManagerList.add(clientManager.get());

        }while (fileImporter.nextLine());

        return clientManagerList;
    }

    @Transactional
    public List<ClientManager> insertClientManagers(List<ClientManager> clientManagerList){
        return repositoryClientManager.saveAll(clientManagerList);
    }
}
