package ru.fds.tavrzcms3.controller;

import org.springframework.web.bind.annotation.*;
import ru.fds.tavrzcms3.domain.LoanAgreement;
import ru.fds.tavrzcms3.dto.DtoFactory;
import ru.fds.tavrzcms3.dto.LoanAgreementDto;
import ru.fds.tavrzcms3.service.LoanAgreementService;
import ru.fds.tavrzcms3.validate.ValidatorEntity;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/loan_agreement")
public class LoanAgreementController {

    private final LoanAgreementService loanAgreementService;
    private final DtoFactory dtoFactory;
    private final ValidatorEntity validatorEntity;


    public LoanAgreementController(LoanAgreementService loanAgreementService,
                                   DtoFactory dtoFactory,
                                   ValidatorEntity validatorEntity) {
        this.loanAgreementService = loanAgreementService;
        this.dtoFactory = dtoFactory;
        this.validatorEntity = validatorEntity;
    }

    @GetMapping("/{id}")
    public LoanAgreementDto getLoanAgreement(@PathVariable Long id){
        Optional<LoanAgreement> loanAgreement = loanAgreementService.getLoanAgreementById(id);
        return loanAgreement.map(dtoFactory::getLoanAgreementDto)
                .orElseThrow(()-> new NullPointerException("Loan agreement not found"));
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

    @PostMapping("/insert")
    public LoanAgreementDto insertLoanAgreement(@Valid @RequestBody LoanAgreementDto loanAgreementDto){
        LoanAgreement loanAgreement = dtoFactory.getLoanAgreementEntity(loanAgreementDto);

        Set<ConstraintViolation<LoanAgreement>> violations =  validatorEntity.validateEntity(loanAgreement);
        if(!violations.isEmpty())
            throw new ConstraintViolationException(violations);

        loanAgreement = loanAgreementService.updateInsertLoanAgreement(loanAgreement);
        return dtoFactory.getLoanAgreementDto(loanAgreement);
    }

    @PutMapping("/update")
    public LoanAgreementDto updateLoanAgreement(@Valid @RequestBody LoanAgreementDto loanAgreementDto){
        return insertLoanAgreement(loanAgreementDto);
    }
}
