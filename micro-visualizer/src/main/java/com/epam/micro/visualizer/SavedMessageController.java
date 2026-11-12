package com.epam.micro.visualizer;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SavedMessageController {

    private static final Logger log = LoggerFactory.getLogger(SavedMessageController.class);

    private final SavedMessageRepository repository;

    public SavedMessageController(SavedMessageRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/saved-messages")
    public List<SavedMessage> savedMessages() {
        List<SavedMessage> all = repository.findAll();
        log.info("GET /saved-messages returned {} row(s)", all.size());
        return all;
    }
}
