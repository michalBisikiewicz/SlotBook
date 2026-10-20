package com.slotbook.booking.resource;

import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.UUID;

@Entity
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    @Enumerated(EnumType.STRING)
    private ResourceType type;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private boolean active;


    protected Resource() {}

    public Resource(String name,ResourceType type, LocalTime openingTime,
                    LocalTime closingTime, boolean active) {
        this.name = name;
        this.type = type;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.active = active;
    }


    public UUID getId() { return id; }
    public String getName() { return name; }
    public ResourceType getType() {return type;}
    public LocalTime getOpeningTime() {return openingTime;}
    public LocalTime getClosingTime() {return closingTime;}
    public boolean isActive() {return active;}

    public void setName(String name) {
        this.name = name;
    }

    public void setType(ResourceType type) {
        this.type = type;
    }

    public void setOpeningTime(LocalTime openingTime) {
        this.openingTime = openingTime;
    }

    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
