package com.slotbook.booking.resource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

import static com.slotbook.booking.resource.ResourceType.COURT;
import static com.slotbook.booking.resource.ResourceType.EQUIPMENT;

@Profile("!test")
@Component
public class DataInitializer implements CommandLineRunner {
    private final ResourceRepository repository;

    public DataInitializer(ResourceRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        repository.save(new Resource(
                "Court 1",
                COURT,
                LocalTime.parse("11:00") ,
                LocalTime.parse("12:00") ,
                true
        ));
        repository.save(new Resource(
                "Court 3",
                COURT,
                LocalTime.parse("12:00") ,
                LocalTime.parse("13:00") ,
                true
        ));
        repository.save(new Resource(
                "Bench 1",
                EQUIPMENT,
                LocalTime.parse("10:00") ,
                LocalTime.parse("11:00") ,
                true
        ));
    }
}

