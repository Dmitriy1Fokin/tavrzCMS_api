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
import ru.fds.tavrzcms3.domain.Encumbrance;
import ru.fds.tavrzcms3.dto.DtoFactory;
import ru.fds.tavrzcms3.dto.EncumbranceDto;
import ru.fds.tavrzcms3.service.EncumbranceService;
import ru.fds.tavrzcms3.service.FilesService;
import ru.fds.tavrzcms3.validate.ValidatorEntity;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/encumbrance")
public class EncumbranceController {

    private final EncumbranceService encumbranceService;
    private final FilesService filesService;
    private final DtoFactory dtoFactory;
    private final ValidatorEntity validatorEntity;

    public EncumbranceController(EncumbranceService encumbranceService,
                                 FilesService filesService,
                                 DtoFactory dtoFactory,
                                 ValidatorEntity validatorEntity) {
        this.encumbranceService = encumbranceService;
        this.filesService = filesService;
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

    @PostMapping("/insert_from_file")
    public List<EncumbranceDto> insertEncumbranceFromFile(@RequestParam("file") MultipartFile file) throws IOException {
        File uploadFile = filesService.uploadFile(file, "encumbrance_new");
        List<Encumbrance> encumbranceList = encumbranceService.getNewEncumbranceFromFile(uploadFile);

        return getPersistentEncumbranceDto(encumbranceList);
    }

    @PutMapping("/update_from_file")
    public List<EncumbranceDto> updateEncumbranceFromFile(@RequestParam("file") MultipartFile file) throws IOException {
        File uploadFile = filesService.uploadFile(file, "encumbrance_update");
        List<Encumbrance> encumbranceList = encumbranceService.getCurrentEncumbranceFromFile(uploadFile);

        return getPersistentEncumbranceDto(encumbranceList);
    }

    private List<EncumbranceDto> getPersistentEncumbranceDto(List<Encumbrance> encumbranceList) {
        for(int i = 0; i < encumbranceList.size(); i++){
            Set<ConstraintViolation<Encumbrance>> violations =  validatorEntity.validateEntity(encumbranceList.get(i));
            if(!violations.isEmpty())
                throw new ConstraintViolationException("object " + (i+1), violations);
        }

        encumbranceList = encumbranceService.updateInsertEncumbrances(encumbranceList);

        return dtoFactory.getEncumbrancesDto(encumbranceList);
    }
}
