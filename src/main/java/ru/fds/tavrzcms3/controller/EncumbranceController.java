package ru.fds.tavrzcms3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.fds.tavrzcms3.domain.Encumbrance;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.service.EncumbranceService;
import ru.fds.tavrzcms3.service.PledgeSubjectService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/encumbrance")
public class EncumbranceController {

    private final PledgeSubjectService pledgeSubjectService;
    private final EncumbranceService encumbranceService;

    private static final String MSG_WRONG_LINK = "Неверная ссылка";

    public EncumbranceController(PledgeSubjectService pledgeSubjectService, EncumbranceService encumbranceService) {
        this.pledgeSubjectService = pledgeSubjectService;
        this.encumbranceService = encumbranceService;
    }

    @GetMapping("/encumbrances")
    public  String encumbrancePage(@RequestParam("pledgeSubjectId") long pledgeSubjectId,
                                   Model model){
        PledgeSubject pledgeSubject = pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId)
                .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK));
        Collection<Encumbrance> encumbranceList = encumbranceService.getEncumbranceByPledgeSubject(pledgeSubject);
        model.addAttribute("pledgeSubject", pledgeSubject);
        model.addAttribute("encumbranceList", encumbranceList);

        return "encumbrance/encumbrances";
    }

    @GetMapping("/card")
    public String encumbranceCardPageGet(@RequestParam("pledgeSubjectId") long pledgeSubjectId,
                                         Model model){

        Encumbrance encumbrance = new Encumbrance();
        PledgeSubject pledgeSubject = pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId)
                .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK));
        encumbrance.setPledgeSubject(pledgeSubject);
        model.addAttribute("encumbrance", encumbrance);

        return "encumbrance/card";
    }

    @PostMapping("/insert")
    public String encumbranceCardPagePost(@Valid Encumbrance encumbrance,
                                          BindingResult bindingResult,
                                          Model model){

        if(bindingResult.hasErrors())
            return "encumbrance/card";

        Encumbrance encumbranceInserted = encumbranceService.updateInsertEncumbrance(encumbrance);
        Collection<Encumbrance> encumbranceList = encumbranceService.getEncumbranceByPledgeSubject(encumbranceInserted.getPledgeSubject());
        model.addAttribute("pledgeSubject", encumbranceInserted.getPledgeSubject());
        model.addAttribute("encumbranceList", encumbranceList);

        return "encumbrance/encumbrances";
    }
}
