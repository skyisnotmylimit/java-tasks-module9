package com.epam.micro.sender;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.concurrent.ThreadLocalRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    private static final Logger log = LoggerFactory.getLogger(NotificationController.class);

    private final RabbitTemplate rabbitTemplate;
    private final Counter sentCounter;

    public NotificationController(RabbitTemplate rabbitTemplate, MeterRegistry registry) {
        this.rabbitTemplate = rabbitTemplate;
        this.sentCounter = Counter.builder("sender_notifications_sent_total")
                .description("Notifications pushed to RabbitMQ").register(registry);
        // Additional randomized business metric (task 8)
        Gauge.builder("sender_random_load", () -> ThreadLocalRandom.current().nextDouble(0, 100))
                .description("Randomized load indicator").register(registry);
    }

    @PostMapping("/notification")
    public ResponseEntity<String> send(@RequestBody NotificationRequest request) {
        log.info("POST /notification from user='{}' message='{}'", request.user(), request.message());
        rabbitTemplate.convertAndSend(SenderApplication.QUEUE, request.message());
        sentCounter.increment();
        return ResponseEntity.ok("queued");
    }
}
