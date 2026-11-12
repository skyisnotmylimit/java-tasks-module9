package com.epam.micro.sender;

import org.springframework.amqp.core.Queue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SenderApplication {

    public static final String QUEUE = "notifications";

    public static void main(String[] args) {
        SpringApplication.run(SenderApplication.class, args);
    }

    @Bean
    public Queue notificationsQueue() {
        return new Queue(QUEUE, true);
    }
}
