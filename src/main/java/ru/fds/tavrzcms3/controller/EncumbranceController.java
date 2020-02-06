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
import ru.fds.tavrzcms3.exception.NotFoundException;
import ru.fds.tavrzcms3.service.EncumbranceService;
import ru.fds.tavrzcms3.service.FilesService;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/encumbrance")
public class EncumbranceController {

    private final EncumbranceService encumbranceService;
    private final FilesService filesService;
    private final DtoFactory dtoFactory;

    public EncumbranceController(EncumbranceService encumbranceService,
                                 FilesService filesService,
                                 DtoFactory dtoFactory) {
        this.encumbranceService = encumbranceService;
        this.filesService = filesService;
        this.dtoFactory = dtoFactory;
    }

    @GetMapping("/{encumbranceId}")
    public EncumbranceDto getEncumbrance(@PathVariable("encumbranceId") Long encumbranceId){
        return encumbranceService.getEncumbranceById(encumbranceId).map(dtoFactory::getEncumbranceDto)
                .orElseThrow(()-> new NotFoundException("Encumbrance not found"));
    }

    @GetMapping("pledge_subject")
    public List<EncumbranceDto> getEncumbrancesByPledgeSubject(@RequestParam("pledgeSubjectId") Long pledgeSubjectId){
        return dtoFactory.getEncumbrancesDto(encumbranceService.getEncumbranceByPledgeSubject(pledgeSubjectId));
    }

    @PostMapping("/insert")
    public EncumbranceDto insertEncumbrance(@Valid @RequestBody EncumbranceDto encumbranceDto){
        Encumbrance encumbrance = encumbranceService
                .updateInsertEncumbrance(dtoFactory.getEncumbranceEntity(encumbranceDto));

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

        return dtoFactory.getEncumbrancesDto(encumbranceList);
    }

    @PutMapping("/update_from_file")
    public List<EncumbranceDto> updateEncumbranceFromFile(@RequestParam("file") MultipartFile file) throws IOException {
        File uploadFile = filesService.uploadFile(file, "encumbrance_update");
        List<Encumbrance> encumbranceList = encumbranceService.getCurrentEncumbranceFromFile(uploadFile);

        return dtoFactory.getEncumbrancesDto(encumbranceList);
    }
}
