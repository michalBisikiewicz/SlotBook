package com.slotbook.booking.resource;

import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
public class ResourceService {
    private final ResourceRepository repository;

    public ResourceService(ResourceRepository repository) {
        this.repository = repository;
    }

    public List<Resource> getAll() {
        return repository.findAll();
    }

    public Resource getById(UUID id) {
        return repository.findById(id).orElseThrow();
    }

    public Resource create(String name,ResourceType type, LocalTime openingTime,
                           LocalTime closingTime, boolean active) {
        return repository.save(new Resource(name, type, openingTime, closingTime, active));
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }
    public Resource update(UUID id, String name, ResourceType type,
                           LocalTime openingTime, LocalTime closingTime, boolean active) {
        Resource resource = repository.findById(id).orElseThrow();
        resource.setName(name);
        resource.setType(type);
        resource.setOpeningTime(openingTime);
        resource.setClosingTime(closingTime);
        resource.setActive(active);
        return repository.save(resource);
    }
}
