package com.epam.micro.recipient;

import org.springframework.amqp.core.Queue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RecipientApplication {

    public static final String QUEUE = "notifications";

    public static void main(String[] args) {
        SpringApplication.run(RecipientApplication.class, args);
    }

    @Bean
    public Queue notificationsQueue() {
        return new Queue(QUEUE, true);
    }
}
