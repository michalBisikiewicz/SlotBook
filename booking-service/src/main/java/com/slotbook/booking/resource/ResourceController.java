package com.slotbook.booking.resource;



import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/resources")
public class ResourceController {

    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @GetMapping
    public List<Resource> getResources() {
        return resourceService.getAll();
    }

    @GetMapping("/{id}")
    public Resource getById(@PathVariable UUID id) {
        return resourceService.getById(id);
    }

    @PostMapping
    public Resource create(@RequestBody Resource resource) {
        return resourceService.create(
                resource.getName(),
                resource.getType(),
                resource.getOpeningTime(),
                resource.getClosingTime(),
                resource.isActive());
    }

    @PutMapping("/{id}")
    public Resource update(@PathVariable UUID id, @RequestBody Resource resource) {
        return resourceService.update(
                id,
                resource.getName(),
                resource.getType(),
                resource.getOpeningTime(),
                resource.getClosingTime(),
                resource.isActive());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        resourceService.delete(id);
    }
}
