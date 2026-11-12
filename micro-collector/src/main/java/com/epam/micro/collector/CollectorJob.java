package com.epam.micro.collector;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CollectorJob {

    private static final Logger log = LoggerFactory.getLogger(CollectorJob.class);

    private final RecipientClient client;
    private final SavedMessageRepository repository;

    public CollectorJob(RecipientClient client, SavedMessageRepository repository) {
        this.client = client;
        this.repository = repository;
    }

    @Scheduled(fixedDelayString = "${collector.interval-seconds:10}000", initialDelay = 10000)
    public void collect() {
        try {
            List<String> messages = client.getMessages();
            log.info("Scheduler: received {} message(s): {}", messages.size(), messages);
            messages.forEach(m -> repository.save(new SavedMessage(m)));
        } catch (Exception e) {
            log.warn("Scheduler: could not call micro-recipient: {}", e.getMessage());
        }
    }
}
