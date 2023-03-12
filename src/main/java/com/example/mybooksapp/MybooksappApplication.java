package com.example.mybooksapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class MybooksappApplication extends SpringBootServletInitializer {

  public static void main(String[] args) {
    SpringApplication.run(MybooksappApplication.class, args);
  }

//  @Override
//  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//    return builder.sources(MybooksappApplication.class);
//  }

}
