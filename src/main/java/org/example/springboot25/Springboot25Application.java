package org.example.springboot25;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Locale;

@SpringBootApplication
public class Springboot25Application {

    public static void main(String[] args) {
        //Locale.setDefault(Locale.ENGLISH);
        SpringApplication.run(Springboot25Application.class, args);
    }

}
