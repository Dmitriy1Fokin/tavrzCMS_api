package ru.fds.tavrzcms3.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.fds.tavrzcms3.annotation.LogModificationDB;
import ru.fds.tavrzcms3.dictionary.TypeOfClient;
import ru.fds.tavrzcms3.dictionary.TypeOfCollateral;
import ru.fds.tavrzcms3.dto.ClientDto;
import ru.fds.tavrzcms3.dto.ClientManagerDto;
import ru.fds.tavrzcms3.dto.CostHistoryDto;
import ru.fds.tavrzcms3.dto.DtoFactory;
import ru.fds.tavrzcms3.dto.EncumbranceDto;
import ru.fds.tavrzcms3.dto.InsuranceDto;
import ru.fds.tavrzcms3.dto.LoanAgreementDto;
import ru.fds.tavrzcms3.dto.MonitoringDto;
import ru.fds.tavrzcms3.dto.PledgeAgreementDto;
import ru.fds.tavrzcms3.dto.PledgeSubjectDto;
import ru.fds.tavrzcms3.service.*;
import ru.fds.tavrzcms3.domain.*;

import java.io.*;
import java.util.*;

@Log4j2
@Controller
public class MainController {

    private final ClientService clientService;
    private final PledgeAgreementService pledgeAgreementService;
    private final LoanAgreementService loanAgreementService;
    private final PledgeSubjectService pledgeSubjectService;
    private final InsuranceService insuranceService;
    private final EncumbranceService encumbranceService;
    private final MonitoringService monitoringService;
    private final CostHistoryService costHistoryService;
    private final ClientManagerService clientManagerService;
    private final FilesService filesService;

    private final DtoFactory dtoFactory;

    private static final String PAGE_UPDATE = "update";
    private static final String ATTR_MESSAGE_ERROR = "messageError";


    public MainController(ClientService clientService,
                          PledgeAgreementService pledgeAgreementService,
                          LoanAgreementService loanAgreementService,
                          PledgeSubjectService pledgeSubjectService,
                          InsuranceService insuranceService,
                          EncumbranceService encumbranceService,
                          MonitoringService monitoringService,
                          CostHistoryService costHistoryService,
                          ClientManagerService clientManagerService,
                          FilesService filesService,
                          DtoFactory dtoFactory) {
        this.clientService = clientService;
        this.pledgeAgreementService = pledgeAgreementService;
        this.loanAgreementService = loanAgreementService;
        this.pledgeSubjectService = pledgeSubjectService;
        this.insuranceService = insuranceService;
        this.encumbranceService = encumbranceService;
        this.monitoringService = monitoringService;
        this.costHistoryService = costHistoryService;
        this.clientManagerService = clientManagerService;
        this.filesService = filesService;
        this.dtoFactory = dtoFactory;
    }




    @LogModificationDB
    @PostMapping("/upload")
    public String importEntityFromExcel(@AuthenticationPrincipal User user,
                                        @RequestParam("file") Optional<MultipartFile> file,
                                        @RequestParam("whatUpload") Optional<String> whatUpload,
                                        Model model){

        try {
            File uploadFile =  filesService.uploadFile(file.orElseThrow(() -> new IOException("Файл не найден.")), "legal_entity");

            if(whatUpload.isPresent()){
                if(whatUpload.get().equals("clientLegalEntity")){
                    List<Client> clientList = clientService.getNewClientsFromFile(uploadFile, TypeOfClient.LEGAL_ENTITY);
                    clientList = clientService.updateInsertClients(clientList);
                    List<ClientDto> clientDtoList = dtoFactory.getClientsDto(clientList);

                    model.addAttribute("clientDtoList", clientDtoList);

                }else if(whatUpload.get().equals("clientIndividual")){
                    List<Client> clientList = clientService.getNewClientsFromFile(uploadFile, TypeOfClient.INDIVIDUAL);
                    clientList = clientService.updateInsertClients(clientList);
                    List<ClientDto> clientDtoList = dtoFactory.getClientsDto(clientList);

                    model.addAttribute("clientDtoList", clientDtoList);

                }else if(whatUpload.get().equals("pledgeAgreement")){
                    List<PledgeAgreement> pledgeAgreementList = pledgeAgreementService.getNewPledgeAgreementsFromFile(uploadFile);
                    pledgeAgreementList = pledgeAgreementService.updateInsertPledgeAgreements(pledgeAgreementList);
                    List<PledgeAgreementDto> pledgeAgreementDtoList = dtoFactory.getPledgeAgreementsDto(pledgeAgreementList);

                    model.addAttribute("pledgeAgreementDtoList", pledgeAgreementDtoList);

                }else if(whatUpload.get().equals("loanAgreement")){
                    List<LoanAgreement> loanAgreementList = loanAgreementService.getNewLoanAgreementsFromFile(uploadFile);
                    loanAgreementList = loanAgreementService.updateInsertLoanAgreements(loanAgreementList);
                    List<LoanAgreementDto> loanAgreementDtoList = dtoFactory.getLoanAgreementsDto(loanAgreementList);

                    model.addAttribute("loanAgreementDtoList", loanAgreementDtoList);

                }else if(whatUpload.get().equals("psBuilding")){
                    List<PledgeSubject> pledgeSubjectList = pledgeSubjectService.getNewPledgeSubjectsFromFile(uploadFile, TypeOfCollateral.BUILDING);
                    pledgeSubjectList = pledgeSubjectService.insertPledgeSubjects(pledgeSubjectList);
                    List<PledgeSubjectDto> pledgeSubjectDtoList = dtoFactory.getPledgeSubjectsDto(pledgeSubjectList);

                    model.addAttribute("pledgeSubjectDtoList", pledgeSubjectDtoList);

                }else if(whatUpload.get().equals("psRoom")){
                    List<PledgeSubject> pledgeSubjectList = pledgeSubjectService.getNewPledgeSubjectsFromFile(uploadFile, TypeOfCollateral.PREMISE);
                    pledgeSubjectList = pledgeSubjectService.insertPledgeSubjects(pledgeSubjectList);
                    List<PledgeSubjectDto> pledgeSubjectDtoList = dtoFactory.getPledgeSubjectsDto(pledgeSubjectList);

                    model.addAttribute("pledgeSubjectDtoList", pledgeSubjectDtoList);

                }else if(whatUpload.get().equals("psLandOwnership")){
                    List<PledgeSubject> pledgeSubjectList = pledgeSubjectService.getNewPledgeSubjectsFromFile(uploadFile, TypeOfCollateral.LAND_OWNERSHIP);
                    pledgeSubjectList = pledgeSubjectService.insertPledgeSubjects(pledgeSubjectList);
                    List<PledgeSubjectDto> pledgeSubjectDtoList = dtoFactory.getPledgeSubjectsDto(pledgeSubjectList);

                    model.addAttribute("pledgeSubjectDtoList", pledgeSubjectDtoList);

                }else if(whatUpload.get().equals("psLandLease")){
                    List<PledgeSubject> pledgeSubjectList = pledgeSubjectService.getNewPledgeSubjectsFromFile(uploadFile, TypeOfCollateral.LAND_LEASE);
                    pledgeSubjectList = pledgeSubjectService.insertPledgeSubjects(pledgeSubjectList);
                    List<PledgeSubjectDto> pledgeSubjectDtoList = dtoFactory.getPledgeSubjectsDto(pledgeSubjectList);

                    model.addAttribute("pledgeSubjectDtoList", pledgeSubjectDtoList);

                }else if(whatUpload.get().equals("psAuto")){
                    List<PledgeSubject> pledgeSubjectList = pledgeSubjectService.getNewPledgeSubjectsFromFile(uploadFile, TypeOfCollateral.AUTO);
                    pledgeSubjectList = pledgeSubjectService.insertPledgeSubjects(pledgeSubjectList);
                    List<PledgeSubjectDto> pledgeSubjectDtoList = dtoFactory.getPledgeSubjectsDto(pledgeSubjectList);

                    model.addAttribute("pledgeSubjectDtoList", pledgeSubjectDtoList);

                }else if(whatUpload.get().equals("psEquipment")){
                    List<PledgeSubject> pledgeSubjectList = pledgeSubjectService.getNewPledgeSubjectsFromFile(uploadFile, TypeOfCollateral.EQUIPMENT);
                    pledgeSubjectList = pledgeSubjectService.insertPledgeSubjects(pledgeSubjectList);
                    List<PledgeSubjectDto> pledgeSubjectDtoList = dtoFactory.getPledgeSubjectsDto(pledgeSubjectList);

                    model.addAttribute("pledgeSubjectDtoList", pledgeSubjectDtoList);

                }else if(whatUpload.get().equals("psTBO")){
                    List<PledgeSubject> pledgeSubjectList = pledgeSubjectService.getNewPledgeSubjectsFromFile(uploadFile, TypeOfCollateral.TBO);
                    pledgeSubjectList = pledgeSubjectService.insertPledgeSubjects(pledgeSubjectList);
                    List<PledgeSubjectDto> pledgeSubjectDtoList = dtoFactory.getPledgeSubjectsDto(pledgeSubjectList);

                    model.addAttribute("pledgeSubjectDtoList", pledgeSubjectDtoList);

                }else if(whatUpload.get().equals("psSecurities")){
                    List<PledgeSubject> pledgeSubjectList = pledgeSubjectService.getNewPledgeSubjectsFromFile(uploadFile, TypeOfCollateral.SECURITIES);
                    pledgeSubjectList = pledgeSubjectService.insertPledgeSubjects(pledgeSubjectList);
                    List<PledgeSubjectDto> pledgeSubjectDtoList = dtoFactory.getPledgeSubjectsDto(pledgeSubjectList);

                    model.addAttribute("pledgeSubjectDtoList", pledgeSubjectDtoList);

                }else if(whatUpload.get().equals("psVessel")){
                    List<PledgeSubject> pledgeSubjectList = pledgeSubjectService.getNewPledgeSubjectsFromFile(uploadFile, TypeOfCollateral.VESSEL);
                    pledgeSubjectList = pledgeSubjectService.insertPledgeSubjects(pledgeSubjectList);
                    List<PledgeSubjectDto> pledgeSubjectDtoList = dtoFactory.getPledgeSubjectsDto(pledgeSubjectList);

                    model.addAttribute("pledgeSubjectDtoList", pledgeSubjectDtoList);

                }else if(whatUpload.get().equals("insurance")){
                    List<Insurance> insuranceList = insuranceService.getNewInsurancesFromFile(uploadFile);
                    insuranceList = insuranceService.updateInsertInsurances(insuranceList);
                    List<InsuranceDto> insuranceDtoList = dtoFactory.getInsurancesDto(insuranceList);

                    model.addAttribute("insuranceDtoList", insuranceDtoList);

                }else if(whatUpload.get().equals("encumbrance")){
                    List<Encumbrance> encumbranceList = encumbranceService.getNewEncumbranceFromFile(uploadFile);
                    encumbranceList = encumbranceService.updateInsertEncumbrances(encumbranceList);
                    List<EncumbranceDto> encumbranceDtoList = dtoFactory.getEncumbrancesDto(encumbranceList);

                    model.addAttribute("encumbranceDtoList", encumbranceDtoList);

                }else if(whatUpload.get().equals("costHistory")){
                    List<CostHistory> costHistoryList = costHistoryService.getNewCostHistoriesFromFile(uploadFile);
                    costHistoryList = costHistoryService.insertCostHistories(costHistoryList);
                    List<CostHistoryDto> costHistoryDtoList = dtoFactory.getCostHistoriesDto(costHistoryList);

                    model.addAttribute("costHistoryDtoList", costHistoryDtoList);

                }else if(whatUpload.get().equals("monitoring")){
                    List<Monitoring> monitoringList = monitoringService.getNewMonitoringsFromFile(uploadFile);
                    monitoringList = monitoringService.insertMonitoringsInPledgeSubject(monitoringList);
                    List<MonitoringDto> monitoringDtoList = dtoFactory.getMonitoringsDto(monitoringList);

                    model.addAttribute("monitoringDtoList", monitoringDtoList);

                }else if(whatUpload.get().equals("clientManager")){
                    List<ClientManager> clientManagerList = clientManagerService.getNewClientManagersFromFile(uploadFile);
                    clientManagerList = clientManagerService.insertClientManagers(clientManagerList);
                    List<ClientManagerDto> clientManagerDtoList = dtoFactory.getClientManagersDto(clientManagerList);

                    model.addAttribute("clientManagerDtoList", clientManagerDtoList);

                }

                model.addAttribute("messageSuccess", true);
                model.addAttribute("whatUpload", whatUpload.get());

                return PAGE_UPDATE;


            }else {
                model.addAttribute(ATTR_MESSAGE_ERROR, "Ошибка импорта.");
                return PAGE_UPDATE;
            }

        }catch (IOException ioe){
            log.info(ioe.getMessage());
            model.addAttribute(ATTR_MESSAGE_ERROR, ioe.getMessage());
            return PAGE_UPDATE;

        }
    }
}
