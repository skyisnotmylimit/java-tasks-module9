package com.epam.micro.collector;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "recipient", url = "${recipient.url}")
public interface RecipientClient {

    @GetMapping("/message")
    List<String> getMessages();
}
