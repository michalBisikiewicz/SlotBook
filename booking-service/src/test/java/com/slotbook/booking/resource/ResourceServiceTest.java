package com.slotbook.booking.resource;

import com.slotbook.booking.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ResourceServiceTest {

    @Mock
    private ResourceRepository resourceRepository;

    @InjectMocks
    private ResourceService resourceService;

    @Test
    void getByIdThrowsWhenResourceDoesNotExist() {
        UUID missingId = UUID.randomUUID();
        when(resourceRepository.findById(missingId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> resourceService.getById(missingId));
    }

    @Test
    void updateThrowsWhenResourceDoesNotExist() {
        UUID missingId = UUID.randomUUID();
        ResourceRequest request = new ResourceRequest("Update Resource Service Test", ResourceType.COURT,
                LocalTime.parse("11:30"),LocalTime.parse("13:30"));
        when(resourceRepository.findById(missingId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> resourceService.update(request, missingId));
    }

    @Test
    void updateChangesFieldsOfExistingResource() {
        UUID id = UUID.randomUUID();
        Resource existing = new Resource("Room 1", ResourceType.ROOM,
                LocalTime.parse("11:30"),LocalTime.parse("13:30"), true);
        when(resourceRepository.findById(id)).thenReturn(Optional.of(existing));
        when(resourceRepository.save(any(Resource.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ResourceRequest request = new ResourceRequest("Court 2", ResourceType.COURT,
                LocalTime.parse("12:30"),LocalTime.parse("14:30"));

        ResourceResponse response = resourceService.update(request, id);

        assertEquals("Court 2", response.name());
        assertEquals(ResourceType.COURT, response.type());
        assertEquals(LocalTime.parse("12:30"), response.openingTime());
        assertEquals(LocalTime.parse("14:30"), response.closingTime());
    }

    @Test
    void deleteThrowsAndDoesNotDeleteWhenResourceDoesNotExist() {
        UUID missingId = UUID.randomUUID();
        when(resourceRepository.existsById(missingId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> resourceService.delete(missingId));

        verify(resourceRepository, never()).deleteById(any());
    }

    @Test
    void deleteRemovesExistingResource() {
        UUID id = UUID.randomUUID();
        when(resourceRepository.existsById(id)).thenReturn(true);

        resourceService.delete(id);

        verify(resourceRepository).deleteById(id);
    }
}
