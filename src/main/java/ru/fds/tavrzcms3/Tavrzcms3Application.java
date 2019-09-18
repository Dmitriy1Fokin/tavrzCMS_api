package ru.fds.tavrzcms3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@SpringBootApplication
public class Tavrzcms3Application {

    public static void main(String[] args) {
        SpringApplication.run(Tavrzcms3Application.class);
    }


    @PostConstruct
    public void init(){
    }
}
