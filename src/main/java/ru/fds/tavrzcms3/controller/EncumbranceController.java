package ru.fds.tavrzcms3.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.fds.tavrzcms3.domain.Encumbrance;
import ru.fds.tavrzcms3.dto.DtoFactory;
import ru.fds.tavrzcms3.dto.EncumbranceDto;
import ru.fds.tavrzcms3.service.EncumbranceService;
import ru.fds.tavrzcms3.validate.ValidatorEntity;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/encumbrance")
public class EncumbranceController {

    private final EncumbranceService encumbranceService;
    private final DtoFactory dtoFactory;
    private final ValidatorEntity validatorEntity;

    public EncumbranceController(EncumbranceService encumbranceService,
                                 DtoFactory dtoFactory,
                                 ValidatorEntity validatorEntity) {
        this.encumbranceService = encumbranceService;
        this.dtoFactory = dtoFactory;
        this.validatorEntity = validatorEntity;
    }

    @GetMapping("/{id}")
    public EncumbranceDto getEncumbrance(@PathVariable Long id){
        Optional<Encumbrance> encumbrance = encumbranceService.getEncumbranceById(id);
        return encumbrance.map(dtoFactory::getEncumbranceDto)
                .orElseThrow(()-> new NullPointerException("Encumbrance not found"));
    }

    @PostMapping("/insert")
    public EncumbranceDto insertEncumbrance(@Valid @RequestBody EncumbranceDto encumbranceDto){
        Encumbrance encumbrance = dtoFactory.getEncumbranceEntity(encumbranceDto);

        Set<ConstraintViolation<Encumbrance>> violations =  validatorEntity.validateEntity(encumbrance);
        if(!violations.isEmpty())
            throw new ConstraintViolationException(violations);

        encumbrance = encumbranceService.updateInsertEncumbrance(encumbrance);
        return dtoFactory.getEncumbranceDto(encumbrance);
    }

    @PutMapping("/update")
    public EncumbranceDto updateEncumbrance(@Valid @RequestBody EncumbranceDto encumbranceDto){
        return insertEncumbrance(encumbranceDto);
    }
}
