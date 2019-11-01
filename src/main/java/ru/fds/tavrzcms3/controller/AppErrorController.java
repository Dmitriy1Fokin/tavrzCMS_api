package ru.fds.tavrzcms3.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AppErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {

        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
        model.addAttribute("status", statusCode);
        String message = "";
        if(exception != null){
            message = exception.getMessage();
        }
        model.addAttribute("message", message);
        return "error";
    }



    @Override
    public String getErrorPath() {
        return "/error";
    }
}
