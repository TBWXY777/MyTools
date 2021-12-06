package com.tubai.study_html;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@MapperScan(basePackages = {"com.tubai.study_html.mapper"})
public class StudyHtmlApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudyHtmlApplication.class, args);
    }

}
