package ru.fds.tavrzcms3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.fds.tavrzcms3.domain.Insurance;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.service.InsuranceService;
import ru.fds.tavrzcms3.service.PledgeSubjectService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/insurance")
public class InsuranceController {

    private final PledgeSubjectService pledgeSubjectService;
    private final InsuranceService insuranceService;

    private static final String MSG_WRONG_LINK = "Неверная ссылка";

    public InsuranceController(PledgeSubjectService pledgeSubjectService, InsuranceService insuranceService) {
        this.pledgeSubjectService = pledgeSubjectService;
        this.insuranceService = insuranceService;
    }

    @GetMapping("/insurances")
    public String insurancesPage(@RequestParam("pledgeSubjectId") long pledgeSubjectId,
                                 Model model){
        PledgeSubject pledgeSubject = pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId)
                .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK));
        List<Insurance> insuranceList = insuranceService.getInsurancesByPledgeSubject(pledgeSubject);
        ArrayList<Insurance> insurances = new ArrayList<>(insuranceList);
        model.addAttribute("pledgeSubject", pledgeSubject);
        model.addAttribute("insuranceList", insuranceList);

        return "insurance/insurances";
    }

    @GetMapping("/card")
    public String insuranceCardGet(@RequestParam("pledgeSubjectId") long pledgeSubjectId,
                                   Model model){
        Insurance insurance = new Insurance();
        PledgeSubject pledgeSubject = pledgeSubjectService.getPledgeSubjectById(pledgeSubjectId)
                .orElseThrow(()-> new IllegalArgumentException(MSG_WRONG_LINK));
        insurance.setPledgeSubject(pledgeSubject);
        model.addAttribute("insurance", insurance);

        return "insurance/card";
    }

    @PostMapping("/insert")
    public String insuranceInsert(@Valid Insurance insurance,
                                  BindingResult bindingResult,
                                  Model model){

        if(bindingResult.hasErrors())
            return "insurance/card";

        Insurance insuranceInserted = insuranceService.updateInsertInsurance(insurance);
        List <Insurance> insuranceList = insuranceService.getInsurancesByPledgeSubject(insuranceInserted.getPledgeSubject());
        ArrayList<Insurance> insurances = new ArrayList<>(insuranceList);
        model.addAttribute("pledgeSubject", insuranceInserted.getPledgeSubject());
        model.addAttribute("insuranceList", insuranceList);

        return "insurance/insurances";
    }


}
