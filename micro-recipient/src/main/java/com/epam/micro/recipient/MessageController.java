package com.epam.micro.recipient;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

    private static final Logger log = LoggerFactory.getLogger(MessageController.class);

    private final MessageStore store;

    public MessageController(MessageStore store) {
        this.store = store;
    }

    @GetMapping("/message")
    public List<String> messages() {
        List<String> result = store.drain();
        log.info("GET /message returned {} message(s)", result.size());
        return result;
    }
}
