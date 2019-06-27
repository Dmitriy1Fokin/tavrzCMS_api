package ru.fds.tavrzcms3.rest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.fds.tavrzcms3.domain.Employee;
import ru.fds.tavrzcms3.repository.RepositoryEmployee;

import java.util.List;

@Controller
public class EmployeeController {

    private final RepositoryEmployee repositoryEmployee;

    public EmployeeController(RepositoryEmployee repositoryEmployee){
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
}
