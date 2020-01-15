package ru.fds.tavrzcms3.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.fds.tavrzcms3.domain.Insurance;
import ru.fds.tavrzcms3.dto.DtoFactory;
import ru.fds.tavrzcms3.dto.InsuranceDto;
import ru.fds.tavrzcms3.service.InsuranceService;
import ru.fds.tavrzcms3.validate.ValidatorEntity;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/insurance")
public class InsuranceController {

    private final InsuranceService insuranceService;
    private final DtoFactory dtoFactory;
    private final ValidatorEntity validatorEntity;


    public InsuranceController(InsuranceService insuranceService,
                               DtoFactory dtoFactory,
                               ValidatorEntity validatorEntity) {
        this.insuranceService = insuranceService;
        this.dtoFactory = dtoFactory;
        this.validatorEntity = validatorEntity;
    }

    @GetMapping("/{id}")
    public InsuranceDto getInsurance(@PathVariable Long id){
        Optional<Insurance> insurance = insuranceService.getInsuranceById(id);
        return insurance.map(dtoFactory::getInsuranceDto)
                .orElseThrow(()-> new NullPointerException("Insurance not found"));
    }

    @PostMapping("/insert")
    public InsuranceDto insertInsurance(@Valid @RequestBody InsuranceDto insuranceDto){
        Insurance insurance = dtoFactory.getInsuranceEntity(insuranceDto);

        Set<ConstraintViolation<Insurance>> violations =  validatorEntity.validateEntity(insurance);
        if(!violations.isEmpty())
            throw new ConstraintViolationException(violations);

        insurance = insuranceService.updateInsertInsurance(insurance);
        return dtoFactory.getInsuranceDto(insurance);
    }

    @PutMapping("/update")
    public InsuranceDto updateInsurance(@Valid @RequestBody InsuranceDto insuranceDto){
        return insertInsurance(insuranceDto);
    }
}
