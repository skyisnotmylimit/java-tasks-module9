package com.epam.micro.recipient;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MessageStore {

    private static final Logger log = LoggerFactory.getLogger(MessageStore.class);

    private final RabbitTemplate rabbitTemplate;
    private final List<String> messages = new ArrayList<>();
    private final boolean singleMessagePerCall;

    public MessageStore(RabbitTemplate rabbitTemplate,
                        @Value("${recipient.single-message-per-call:false}") boolean singleMessagePerCall) {
        this.rabbitTemplate = rabbitTemplate;
        this.singleMessagePerCall = singleMessagePerCall;
    }

    @Scheduled(fixedDelayString = "${recipient.poll-interval-seconds:5}000")
    public void collect() {
        int count = 0;
        Object message;
        while ((message = rabbitTemplate.receiveAndConvert(RecipientApplication.QUEUE)) != null) {
            synchronized (messages) {
                messages.add(String.valueOf(message));
            }
            count++;
        }
        log.info("Scheduler: collected {} message(s) from RabbitMQ", count);
    }

    /** Returns stored messages and removes them (only the oldest one in single-message mode). */
    public List<String> drain() {
        synchronized (messages) {
            List<String> result = new ArrayList<>();
            if (singleMessagePerCall) {
                if (!messages.isEmpty()) {
                    result.add(messages.remove(0));
                }
            } else {
                result.addAll(messages);
                messages.clear();
            }
            return result;
        }
    }
}
