package com.example.project4task1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@ServletComponentScan(basePackages = {"com.example.project4task1"})
public class Project4Task1Application {

    public static void main(String[] args) {
        SpringApplication.run(Project4Task1Application.class, args);
    }

}
