package com.ohgiraffers.springdatajpa;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.ui.Model;

@SpringBootApplication
public class Chap05SpringDataJpaLectureSourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(Chap05SpringDataJpaLectureSourceApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }


}
