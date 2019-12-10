package ru.fds.tavrzcms3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.fds.tavrzcms3.converver.EncumbranceConverterDto;
import ru.fds.tavrzcms3.converver.PledgeSubjectConverterDto;
import ru.fds.tavrzcms3.domain.Encumbrance;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.dto.EncumbranceDto;
import ru.fds.tavrzcms3.dto.PledgeSubjectDto;
import ru.fds.tavrzcms3.service.EncumbranceService;
import ru.fds.tavrzcms3.service.PledgeSubjectService;
import ru.fds.tavrzcms3.validate.ValidatorEntity;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/encumbrance")
public class EncumbranceController {

    private final PledgeSubjectService pledgeSubjectService;
    private final EncumbranceService encumbranceService;

    private final PledgeSubjectConverterDto pledgeSubjectConverterDto;
    private final EncumbranceConverterDto encumbranceConverterDto;

    private final ValidatorEntity validatorEntity;

    private static final String MSG_WRONG_LINK = "Неверная ссылка";

    public EncumbranceController(PledgeSubjectService pledgeSubjectService,
                                 EncumbranceService encumbranceService,
                                 PledgeSubjectConverterDto pledgeSubjectConverterDto,
                                 EncumbranceConverterDto encumbranceConverterDto,
                                 ValidatorEntity validatorEntity) {
        this.pledgeSubjectService = pledgeSubjectService;
        this.encumbranceService = encumbranceService;
        this.pledgeSubjectConverterDto = pledgeSubjectConverterDto;
        this.encumbranceConverterDto = encumbranceConverterDto;
        this.validatorEntity = validatorEntity;
    }

    @GetMapping("/encumbrances")
    public  String encumbrancePage(@RequestParam("pledgeSubjectId") long pledgeSubjectId,
                                   Model model){

        PledgeSubject pledgeSubject = pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId)
                .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK));

        PledgeSubjectDto pledgeSubjectDto = pledgeSubjectConverterDto.toDto(pledgeSubject);

        List<EncumbranceDto> encumbranceList = encumbranceConverterDto
                .toDto(encumbranceService.getEncumbranceByPledgeSubject(pledgeSubject));

        model.addAttribute("pledgeSubject", pledgeSubjectDto);
        model.addAttribute("encumbranceList", encumbranceList);

        return "encumbrance/encumbrances";
    }

    @GetMapping("/card")
    public String encumbranceCardPage(@RequestParam("pledgeSubjectId") long pledgeSubjectId,
                                         Model model){

        EncumbranceDto encumbranceDto = EncumbranceDto.builder()
                .pledgeSubjectId(pledgeSubjectId)
                .build();

        model.addAttribute("encumbranceDto", encumbranceDto);

        return "encumbrance/card";
    }

    @PostMapping("/insert")
    public String encumbranceCardPagePost(@Valid EncumbranceDto encumbranceDto,
                                          BindingResult bindingResult,
                                          Model model){

        if(bindingResult.hasErrors())
            return "encumbrance/card";

        Encumbrance encumbrance = encumbranceConverterDto.toEntity(encumbranceDto);
        Set<ConstraintViolation<Encumbrance>> violations = validatorEntity.validateEntity(encumbrance);
        if(!violations.isEmpty())
            throw new IllegalArgumentException(validatorEntity.getErrorMessage());

        encumbranceService.updateInsertEncumbrance(encumbrance);

        return encumbrancePage(encumbrance.getPledgeSubject().getPledgeSubjectId(), model);
    }
}
