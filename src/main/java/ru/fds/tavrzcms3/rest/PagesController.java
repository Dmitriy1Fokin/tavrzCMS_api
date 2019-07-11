package ru.fds.tavrzcms3.rest;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.fds.tavrzcms3.domain.Employee;
import ru.fds.tavrzcms3.repository.RepositoryEmployee;

import java.util.List;


@Controller
public class PagesController {

    private final RepositoryEmployee repositoryEmployee;

    public PagesController(RepositoryEmployee repositoryEmployee) {
        this.repositoryEmployee = repositoryEmployee;
    }

    @GetMapping("/")
    public String listPage(Model model) {
        List<Employee> employees = repositoryEmployee.findAll();
        model.addAttribute("employees", employees);
        return "list";
    }

    @GetMapping("/edit")
    public String editPage(@RequestParam("id") long id, Model model) {
        Employee employee = repositoryEmployee.findById(id).orElseThrow(NotFoundException::new);
        model.addAttribute("employee", employee);
        return "edit";
    }

    @PostMapping("/edit")
    public String changeEmployee(@RequestParam("id") long id, @RequestParam("surname") String suname, @RequestParam("name") String name, Model model){
        Employee emp = repositoryEmployee.findById(id).get();
        emp.setSurname(suname);
        emp.setName(name);
        repositoryEmployee.save(emp);
        System.out.println("Cool!!!!!!!!!!!!!!!!!!!!!!");
        model.addAttribute("employee", emp);
        return "edit";
    }

    @GetMapping("/user")
    public String userPage() {
        //myService.onlyUser();
        return "user";
    }

    @GetMapping("/admin")
    public String adminPage() {
        //myService.onlyAdmin();
        return "admin";
    }

    @GetMapping("/authenticated")
    public String authenticatedPage() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        System.out.println(userDetails.getUsername());
        return "authenticated";
    }

    @GetMapping("/success")
    public String successPage() {
        return "success";
    }
}