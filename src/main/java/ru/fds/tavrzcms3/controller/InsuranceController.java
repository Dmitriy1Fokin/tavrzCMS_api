package ru.fds.tavrzcms3.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.fds.tavrzcms3.annotation.LogModificationDB;
import ru.fds.tavrzcms3.domain.Insurance;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.dto.DtoFactory;
import ru.fds.tavrzcms3.dto.InsuranceDto;
import ru.fds.tavrzcms3.dto.PledgeSubjectDto;
import ru.fds.tavrzcms3.service.InsuranceService;
import ru.fds.tavrzcms3.service.PledgeSubjectService;
import ru.fds.tavrzcms3.validate.ValidatorEntity;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/insurance")
public class InsuranceController {

    private final PledgeSubjectService pledgeSubjectService;
    private final InsuranceService insuranceService;

    private final DtoFactory dtoFactory;

    private final ValidatorEntity validatorEntity;

    private static final String MSG_WRONG_LINK = "Неверная ссылка";

    public InsuranceController(PledgeSubjectService pledgeSubjectService,
                               InsuranceService insuranceService,
                               DtoFactory dtoFactory,
                               ValidatorEntity validatorEntity) {
        this.pledgeSubjectService = pledgeSubjectService;
        this.insuranceService = insuranceService;
        this.dtoFactory = dtoFactory;
        this.validatorEntity = validatorEntity;
    }

    @GetMapping("/insurances")
    public String insurancesPage(@RequestParam("pledgeSubjectId") long pledgeSubjectId,
                                 Model model){

        PledgeSubject pledgeSubject = pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId)
                .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK));

        PledgeSubjectDto pledgeSubjectDto = dtoFactory.getPledgeSubjectDto(pledgeSubject);

        List<InsuranceDto> insuranceList = dtoFactory
                .getInsurancesDto(insuranceService.getInsurancesByPledgeSubject(pledgeSubject));

        model.addAttribute("pledgeSubject", pledgeSubjectDto);
        model.addAttribute("insuranceList", insuranceList);

        return "insurance/insurances";
    }

    @GetMapping("/card")
    public String insuranceCard(@RequestParam("pledgeSubjectId") long pledgeSubjectId,
                                   Model model){

        InsuranceDto insuranceDto = InsuranceDto.builder()
                .pledgeSubjectId(pledgeSubjectId)
                .build();

        model.addAttribute("insuranceDto", insuranceDto);

        return "insurance/card";
    }

    @LogModificationDB
    @PostMapping("/insert")
    public String insuranceInsert(@AuthenticationPrincipal User user,
                                  @Valid InsuranceDto insuranceDto,
                                  BindingResult bindingResult,
                                  Model model){

        if(bindingResult.hasErrors())
            return "insurance/card";

        Insurance insurance = dtoFactory.getInsuranceEntity(insuranceDto);
        Set<ConstraintViolation<Insurance>> violations = validatorEntity.validateEntity(insurance);
        if(!violations.isEmpty())
            throw new IllegalArgumentException(validatorEntity.getErrorMessage());

        insuranceService.updateInsertInsurance(insurance);

        return insurancesPage(insurance.getPledgeSubject().getPledgeSubjectId(), model);
    }


}
