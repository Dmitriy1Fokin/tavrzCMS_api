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
import ru.fds.tavrzcms3.domain.Insurance;
import ru.fds.tavrzcms3.dto.DtoFactory;
import ru.fds.tavrzcms3.dto.InsuranceDto;
import ru.fds.tavrzcms3.exception.NotFoundException;
import ru.fds.tavrzcms3.service.FilesService;
import ru.fds.tavrzcms3.service.InsuranceService;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/insurance")
public class InsuranceController {

    private final InsuranceService insuranceService;
    private final FilesService filesService;
    private final DtoFactory dtoFactory;


    public InsuranceController(InsuranceService insuranceService,
                               FilesService filesService,
                               DtoFactory dtoFactory) {
        this.insuranceService = insuranceService;
        this.filesService = filesService;
        this.dtoFactory = dtoFactory;
    }

    @GetMapping("/{insuranceId}")
    public InsuranceDto getInsurance(@PathVariable("insuranceId") Long insuranceId){
        return insuranceService.getInsuranceById(insuranceId).map(dtoFactory::getInsuranceDto)
                .orElseThrow(()-> new NotFoundException("Insurance not found"));
    }

    @GetMapping("pledge_subject")
    public List<InsuranceDto> getInsurancesByPledgeSubject(@RequestParam("pledgeSubjectId") Long pledgeSubjectId){
        return dtoFactory.getInsurancesDto(insuranceService.getInsurancesByPledgeSubject(pledgeSubjectId));
    }

    @PostMapping("/insert")
    public InsuranceDto insertInsurance(@Valid @RequestBody InsuranceDto insuranceDto){
        Insurance insurance = insuranceService.updateInsertInsurance(dtoFactory.getInsuranceEntity(insuranceDto));

        return dtoFactory.getInsuranceDto(insurance);
    }

    @PutMapping("/update")
    public InsuranceDto updateInsurance(@Valid @RequestBody InsuranceDto insuranceDto){
        return insertInsurance(insuranceDto);
    }

    @PostMapping("/insert_from_file")
    public List<InsuranceDto> insertInsuranceFromFile(@RequestParam("file") MultipartFile file) throws IOException {
        File uploadFile = filesService.uploadFile(file, "insurance_new");
        List<Insurance> insuranceList = insuranceService.getNewInsurancesFromFile(uploadFile);

        return dtoFactory.getInsurancesDto(insuranceList);
    }

    @PutMapping("/update_from_file")
    public List<InsuranceDto> updateInsuranceFromFile(@RequestParam("file") MultipartFile file) throws IOException {
        File uploadFile = filesService.uploadFile(file, "insurance_update");
        List<Insurance> insuranceList = insuranceService.getCurrentInsurancesFromFile(uploadFile);

        return dtoFactory.getInsurancesDto(insuranceList);
    }
}
