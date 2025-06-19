package com.example.ws_back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.*;

@EnableScheduling
@SpringBootApplication
public class WsBackApplication {

  public static void main(String[] args) {
    SpringApplication.run(WsBackApplication.class, args);
  }

  // @Component ← @Controller, @Service, @Repository vs @Mapper
  // @Bean 과 @Component 의 차이?
    // @Component : 내가 스프링빈으로 등록하고 편집할 수 있어야함
    // @Bean : 남이 스프링빈으로 등록한 객체를 객체를 생성하고 편집해야함
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
