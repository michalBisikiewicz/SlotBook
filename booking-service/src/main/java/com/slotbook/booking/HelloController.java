package com.slotbook.booking;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api")
public class HelloController {

    @GetMapping("/hello")
    public HelloMessage getHello() {
        // Get the current date and time
        LocalDateTime now = LocalDateTime.now();

        // Define the format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Format the current date and time
        String timestamp = now.format(formatter);
        return new HelloMessage("Hello, how are you?", timestamp);
    }

    public record HelloMessage(String message, String timestamp) { }

}