package ru.fds.tavrzcms3.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.LoanAgreement;
import ru.fds.tavrzcms3.domain.PledgeAgreement;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.service.ClientService;
import ru.fds.tavrzcms3.service.LoanAgreementService;
import ru.fds.tavrzcms3.service.PledgeAgreementService;
import ru.fds.tavrzcms3.service.PledgeSubjectService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/search")
public class SearchController {

    private final LoanAgreementService loanAgreementService;
    private final PledgeAgreementService pledgeAgreementService;
    private final PledgeSubjectService pledgeSubjectService;
    private final ClientService clientService;

    private static final String ATTR_RESULT_LIST = "resultList";
    private static final String ATTR_PAGE_NUMBERS = "pageNumbers";
    private static final String ATTR_REQ_PARAM = "reqParam";


    public SearchController(LoanAgreementService loanAgreementService,
                            PledgeAgreementService pledgeAgreementService,
                            PledgeSubjectService pledgeSubjectService,
                            ClientService clientService) {
        this.loanAgreementService = loanAgreementService;
        this.pledgeAgreementService = pledgeAgreementService;
        this.pledgeSubjectService = pledgeSubjectService;
        this.clientService = clientService;
    }

    @GetMapping("/search")
    public String searchPage(){
        return "search/search";
    }

    @GetMapping("/search_results")
    public String searchResultsPage(@RequestParam Map<String, String> reqParam,
                                    Model model){
        switch (reqParam.get("typeOfSearch")){

            case "searchLA":
                Page<LoanAgreement> loanAgreementList = loanAgreementService.getLoanAgreementFromSearch(reqParam);
                model.addAttribute(ATTR_RESULT_LIST, loanAgreementList);

                int totalPagesLA = loanAgreementList.getTotalPages();
                if(totalPagesLA > 0){
                    List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPagesLA).boxed().collect(Collectors.toList());
                    model.addAttribute(ATTR_PAGE_NUMBERS, pageNumbers);
                }

                reqParam.remove("page");
                model.addAttribute(ATTR_REQ_PARAM, reqParam);

                break;

            case "searchPA":
                Page<PledgeAgreement> pledgeAgreementList = pledgeAgreementService.getPledgeAgreementFromSearch(reqParam);
                model.addAttribute(ATTR_RESULT_LIST, pledgeAgreementList);

                int totalPagesPA = pledgeAgreementList.getTotalPages();
                if(totalPagesPA > 0){
                    List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPagesPA).boxed().collect(Collectors.toList());
                    model.addAttribute(ATTR_PAGE_NUMBERS, pageNumbers);
                }

                reqParam.remove("page");
                model.addAttribute(ATTR_REQ_PARAM, reqParam);

                break;

            case "searchPS":

                Page<PledgeSubject> pledgeSubjectList = pledgeSubjectService.getPledgeSubjectsFromSearch(reqParam);
                model.addAttribute(ATTR_RESULT_LIST, pledgeSubjectList);

                int totalPagesPS = pledgeSubjectList.getTotalPages();
                if(totalPagesPS > 0){
                    List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPagesPS).boxed().collect(Collectors.toList());
                    model.addAttribute(ATTR_PAGE_NUMBERS, pageNumbers);
                }

                reqParam.remove("page");
                model.addAttribute(ATTR_REQ_PARAM, reqParam);

                break;

            case "searchClient":

                Page<Client> clientPage = clientService.getClientFromSearch(reqParam);
                model.addAttribute(ATTR_RESULT_LIST, clientPage);

                int totalPagesClient = clientPage.getTotalPages();
                if(totalPagesClient > 0){
                    List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPagesClient).boxed().collect(Collectors.toList());
                    model.addAttribute(ATTR_PAGE_NUMBERS, pageNumbers);
                }

                reqParam.remove("page");
                model.addAttribute(ATTR_REQ_PARAM, reqParam);

                break;

            default:
                throw new IllegalArgumentException("Неверная ссылка");
        }

        return "search/search_results";
    }
}
