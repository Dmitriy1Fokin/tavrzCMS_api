package ru.fds.tavrzcms3.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.fds.tavrzcms3.domain.LoanAgreement;
import ru.fds.tavrzcms3.dto.DtoFactory;
import ru.fds.tavrzcms3.dto.LoanAgreementDto;
import ru.fds.tavrzcms3.exception.NotFoundException;
import ru.fds.tavrzcms3.service.FilesService;
import ru.fds.tavrzcms3.service.LoanAgreementService;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/loan_agreement")
public class LoanAgreementController {

    private final LoanAgreementService loanAgreementService;
    private final FilesService filesService;
    private final DtoFactory dtoFactory;


    public LoanAgreementController(LoanAgreementService loanAgreementService,
                                   FilesService filesService,
                                   DtoFactory dtoFactory) {
        this.loanAgreementService = loanAgreementService;
        this.filesService = filesService;
        this.dtoFactory = dtoFactory;
    }

    @GetMapping("/{loanAgreementId}")
    public LoanAgreementDto getLoanAgreement(@PathVariable("loanAgreementId") Long loanAgreementId){
        return loanAgreementService.getLoanAgreementById(loanAgreementId).map(dtoFactory::getLoanAgreementDto)
                .orElseThrow(()-> new NotFoundException("Loan agreement not found"));
    }

    @GetMapping("/current")
    public List<LoanAgreementDto> getLoanAgreements(){
        return dtoFactory.getLoanAgreementsDto(loanAgreementService.getAllCurrentLoanAgreements());
    }

    @GetMapping("/current_la_for_client")
    public List<LoanAgreementDto> getCurrentLoanAgreementsByClient(@RequestParam("clientId") Long clientId){
        return dtoFactory.getLoanAgreementsDto(loanAgreementService.getCurrentLoanAgreementsByLoaner(clientId));
    }

    @GetMapping("/closed_la_for_client")
    public List<LoanAgreementDto> getClosedLoanAgreementsByClient(@RequestParam("clientId") Long clientId){
        return dtoFactory.getLoanAgreementsDto(loanAgreementService.getClosedLoanAgreementsByLoaner(clientId));
    }

    @GetMapping("/current_la_for_employee")
    public List<LoanAgreementDto> getCurrentLoanAgreementByEmployee(@RequestParam("employeeId") Long employeeId){
        return dtoFactory.getLoanAgreementsDto(loanAgreementService.getCurrentLoanAgreementsByEmployee(employeeId));
    }

    @GetMapping("/current_la_for_pledge_agreement")
    public List<LoanAgreementDto> getCurrentLoanAgreementByPledgeAgreement(@RequestParam("pledgeAgreementId") Long pledgeAgreementId){
        return dtoFactory.getLoanAgreementsDto(loanAgreementService
                .getCurrentLoanAgreementsByPledgeAgreement(pledgeAgreementId));
    }

    @GetMapping("/closed_la_for_pledge_agreement")
    public List<LoanAgreementDto> getClosedLoanAgreementByPledgeAgreement(@RequestParam("pledgeAgreementId") Long pledgeAgreementId){
        return dtoFactory.getLoanAgreementsDto(loanAgreementService
                .getClosedLoanAgreementsByPledgeAgreement(pledgeAgreementId));
    }

    @GetMapping("/search")
    public List<LoanAgreementDto> getLoanAgreementBySearchCriteria(@RequestParam Map<String, String> reqParam) throws ReflectiveOperationException {
        List<LoanAgreement> loanAgreementList = loanAgreementService.getLoanAgreementFromSearch(reqParam);

        return dtoFactory.getLoanAgreementsDto(loanAgreementList);
    }

    @PostMapping("/insert")
    public LoanAgreementDto insertLoanAgreement(@Valid @RequestBody LoanAgreementDto loanAgreementDto){
        LoanAgreement loanAgreement = loanAgreementService.insertLoanAgreement(dtoFactory.getLoanAgreementEntity(loanAgreementDto));

        return dtoFactory.getLoanAgreementDto(loanAgreement);
    }

    @PutMapping("/update")
    public LoanAgreementDto updateLoanAgreement(@Valid @RequestBody LoanAgreementDto loanAgreementDto){
        LoanAgreement loanAgreement = loanAgreementService.updateLoanAgreement(dtoFactory.getLoanAgreementEntity(loanAgreementDto));

        return dtoFactory.getLoanAgreementDto(loanAgreement);
    }

    @PostMapping("/insert_from_file")
    public List<LoanAgreementDto> insertLoanAgreementFromFile(@RequestParam("file") MultipartFile file) throws IOException {
        File uploadFile = filesService.uploadFile(file, "loan_agreement_new");
        List<LoanAgreement> loanAgreementList = loanAgreementService.getNewLoanAgreementsFromFile(uploadFile);

        return dtoFactory.getLoanAgreementsDto(loanAgreementList);
    }

    @PutMapping("/update_from_file")
    public List<LoanAgreementDto> updateLoanAgreementFromFile(@RequestParam("file") MultipartFile file) throws IOException {
        File uploadFile = filesService.uploadFile(file, "loan_agreement_update");
        List<LoanAgreement> loanAgreementList = loanAgreementService.getCurrentLoanAgreementsFromFile(uploadFile);

        return dtoFactory.getLoanAgreementsDto(loanAgreementList);
    }
}
