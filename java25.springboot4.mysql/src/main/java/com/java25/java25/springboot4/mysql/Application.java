package com.java25.java25.springboot4.mysql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableJpaAuditing
public class Application {

    @Bean
    public RestTemplate getRestTemplate() {
          return new RestTemplate();
    }    
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        // Moved inside the method to fix the syntax error
        synchronized (Application.class) {
            try {
                Application.class.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }    
    }    
}



//package com.java25.java25.springboot4.mysql;
//
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.Bean;
//import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
//import org.springframework.web.client.RestTemplate;
//
//@SpringBootApplication
//@EnableJpaAuditing
//public class Application {
//
//    @Bean
//    public RestTemplate getRestTemplate() {
//          return new RestTemplate();
//    }    
//    
//    public static void main(String[] args) {
//        SpringApplication.run(Application.class, args);
//    }    
//}
