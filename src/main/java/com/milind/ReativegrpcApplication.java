package com.milind;

import com.milind.webclient.TestWebClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
public class ReativegrpcApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(ReativegrpcApplication.class, args);
        context.getBean(TestWebClient.class).testReactive();
    }

}
