package com.example.bloglv4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // 타임스탬프를 사용하기 위한 어노테이션
@SpringBootApplication
public class BlogLv4Application {

    public static void main(String[] args) {
        SpringApplication.run(BlogLv4Application.class, args);
    }

}
