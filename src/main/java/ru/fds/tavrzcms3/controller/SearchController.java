package ru.fds.tavrzcms3.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.LoanAgreement;
import ru.fds.tavrzcms3.domain.PledgeAgreement;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.dto.ClientDto;
import ru.fds.tavrzcms3.dto.DtoFactory;
import ru.fds.tavrzcms3.dto.LoanAgreementDto;
import ru.fds.tavrzcms3.dto.PledgeAgreementDto;
import ru.fds.tavrzcms3.dto.PledgeSubjectDto;
import ru.fds.tavrzcms3.service.ClientService;
import ru.fds.tavrzcms3.service.LoanAgreementService;
import ru.fds.tavrzcms3.service.PledgeAgreementService;
import ru.fds.tavrzcms3.service.PledgeSubjectService;

import java.util.Collections;
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

    private final DtoFactory dtoFactory;

    private static final String ATTR_RESULT_LIST = "resultList";
    private static final String ATTR_PAGE_NUMBERS = "pageNumbers";
    private static final String ATTR_REQ_PARAM = "reqParam";


    public SearchController(LoanAgreementService loanAgreementService,
                            PledgeAgreementService pledgeAgreementService,
                            PledgeSubjectService pledgeSubjectService,
                            ClientService clientService, DtoFactory dtoFactory) {
        this.loanAgreementService = loanAgreementService;
        this.pledgeAgreementService = pledgeAgreementService;
        this.pledgeSubjectService = pledgeSubjectService;
        this.clientService = clientService;
        this.dtoFactory = dtoFactory;
    }

    @GetMapping("/search")
    public String searchPage(){
        return "search/search";
    }

    @GetMapping("/search_results")
    public String searchResultsPage(@RequestParam Map<String, String> reqParam,
                                    Model model){

        int currentPage = Integer.parseInt(reqParam.get("page"));
        int pageSize = Integer.parseInt(reqParam.get("size"));
        int startItem = currentPage * pageSize;

        switch (reqParam.get("typeOfSearch")){

            case "searchLA":

                List<LoanAgreement> loanAgreementList = loanAgreementService.getLoanAgreementFromSearch(reqParam);

                List<LoanAgreement> loanAgreementListForPage;
                if(loanAgreementList.size() < startItem){
                    loanAgreementListForPage = Collections.emptyList();
                }else {
                    int toIndex = Math.min(startItem+pageSize, loanAgreementList.size());
                    loanAgreementListForPage = loanAgreementList.subList(startItem, toIndex);
                }

                Page<LoanAgreementDto> loanAgreementDtoPage = new PageImpl<>(
                        dtoFactory.getLoanAgreementsDto(loanAgreementListForPage),
                        PageRequest.of(currentPage, pageSize),
                        loanAgreementList.size());



                model.addAttribute(ATTR_RESULT_LIST, loanAgreementDtoPage);

                int totalPagesLA = loanAgreementDtoPage.getTotalPages();
                if(totalPagesLA > 0){
                    List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPagesLA).boxed().collect(Collectors.toList());
                    model.addAttribute(ATTR_PAGE_NUMBERS, pageNumbers);
                }

                reqParam.remove("page");
                model.addAttribute(ATTR_REQ_PARAM, reqParam);

                break;

            case "searchPA":

                List<PledgeAgreement> pledgeAgreementList = pledgeAgreementService.getPledgeAgreementFromSearch(reqParam);

                List<PledgeAgreement> pledgeAgreementListForPage;
                if(pledgeAgreementList.size() < startItem){
                    pledgeAgreementListForPage = Collections.emptyList();
                }else {
                    int toIndex = Math.min(startItem+pageSize, pledgeAgreementList.size());
                    pledgeAgreementListForPage = pledgeAgreementList.subList(startItem, toIndex);
                }

                Page<PledgeAgreementDto> pledgeAgreementDtoPage = new PageImpl<>(
                        dtoFactory.getPledgeAgreementsDto(pledgeAgreementListForPage),
                        PageRequest.of(currentPage, pageSize),
                        pledgeAgreementList.size());

                model.addAttribute(ATTR_RESULT_LIST, pledgeAgreementDtoPage);

                int totalPagesPA = pledgeAgreementDtoPage.getTotalPages();
                if(totalPagesPA > 0){
                    List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPagesPA).boxed().collect(Collectors.toList());
                    model.addAttribute(ATTR_PAGE_NUMBERS, pageNumbers);
                }

                reqParam.remove("page");
                model.addAttribute(ATTR_REQ_PARAM, reqParam);

                break;

            case "searchPS":

                List<PledgeSubject> pledgeSubjectList = pledgeSubjectService.getPledgeSubjectsFromSearch(reqParam);

                List<PledgeSubject> pledgeSubjectListForPage;
                if(pledgeSubjectList.size() < startItem){
                    pledgeSubjectListForPage = Collections.emptyList();
                }else {
                    int toIndex = Math.min(startItem+pageSize, pledgeSubjectList.size());
                    pledgeSubjectListForPage = pledgeSubjectList.subList(startItem, toIndex);
                }

                Page<PledgeSubjectDto> pledgeSubjectDtoPage = new PageImpl<>(
                        dtoFactory.getPledgeSubjectsDto(pledgeSubjectListForPage),
                        PageRequest.of(currentPage, pageSize),
                        pledgeSubjectList.size());

                model.addAttribute(ATTR_RESULT_LIST, pledgeSubjectDtoPage);

                int totalPagesPS = pledgeSubjectDtoPage.getTotalPages();
                if(totalPagesPS > 0){
                    List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPagesPS).boxed().collect(Collectors.toList());
                    model.addAttribute(ATTR_PAGE_NUMBERS, pageNumbers);
                }

                reqParam.remove("page");
                model.addAttribute(ATTR_REQ_PARAM, reqParam);

                break;

            case "searchClient":

//                List<Client> clientList = clientService.getClientFromSearch(reqParam);
//
//                List<Client> clientLissForPage;
//                if(clientList.size() < startItem){
//                    clientLissForPage = Collections.emptyList();
//                }else {
//                    int toIndex = Math.min(startItem+pageSize, clientList.size());
//                    clientLissForPage = clientList.subList(startItem, toIndex);
//                }
//
//                Page<ClientDto> clientDtoPage = new PageImpl<>(
//                        dtoFactory.getC
//                )
//
//                model.addAttribute(ATTR_RESULT_LIST, clientPage);
//
//                int totalPagesClient = clientPage.getTotalPages();
//                if(totalPagesClient > 0){
//                    List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPagesClient).boxed().collect(Collectors.toList());
//                    model.addAttribute(ATTR_PAGE_NUMBERS, pageNumbers);
//                }
//
//                reqParam.remove("page");
//                model.addAttribute(ATTR_REQ_PARAM, reqParam);

                break;

            default:
                throw new IllegalArgumentException("Неверная ссылка");
        }

        return "search/search_results";
    }
}
