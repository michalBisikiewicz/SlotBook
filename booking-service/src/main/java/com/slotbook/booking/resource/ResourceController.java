package com.slotbook.booking.resource;



import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public List<ResourceResponse> getResources() {
        return resourceService.getAll();
    }

    @GetMapping("/{id}")
    public ResourceResponse getById(@PathVariable UUID id) {
        return resourceService.getById(id);
    }

    @PostMapping
    public ResponseEntity<ResourceResponse> create(@RequestBody @Valid ResourceRequest request) {
        ResourceResponse savedResponse = resourceService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedResponse);
    }

    @PutMapping("/{id}")
    public ResourceResponse update(@PathVariable UUID id, @RequestBody @Valid ResourceRequest request) {
        return resourceService.update(
                request,
                id
                );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        resourceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
