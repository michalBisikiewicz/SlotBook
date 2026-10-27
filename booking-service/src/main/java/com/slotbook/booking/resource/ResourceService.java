package com.slotbook.booking.resource;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ResourceService {
    private final ResourceRepository repository;

    public ResourceService(ResourceRepository repository) {
        this.repository = repository;
    }

    public List<ResourceResponse> getAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    public ResourceResponse getById(UUID id) {
        return repository.findById(id).map(this::toResponse).orElseThrow();
    }

    public ResourceResponse create(ResourceRequest request) {
        Resource saved = repository.save(new Resource(
                request.name(),
                request.type(),
                request.openingTime(),
                request.closingTime(),
                 false));
        return toResponse(saved);
    }

    private ResourceResponse toResponse(Resource resource) {
        return new ResourceResponse(
                resource.getId(),
                resource.getName(),
                resource.getType(),
                resource.getOpeningTime(),
                resource.getClosingTime(),
                resource.isActive());
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }
    public ResourceResponse update(ResourceRequest request, UUID id) {
        Resource resource = repository.findById(id).orElseThrow();
        resource.setName(request.name());
        resource.setType(request.type());
        resource.setOpeningTime(request.openingTime());
        resource.setClosingTime(request.closingTime());
        Resource saved = repository.save(resource);
    return toResponse(saved);
    }
}
