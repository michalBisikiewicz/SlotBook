package com.slotbook.booking.resource;



import com.slotbook.booking.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(ResourceController.class)
public class ResourceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ResourceService resourceService;

    @Test
    void returnsListOfResourcesAsJson() throws Exception {
        mockMvc.perform(get("/api/resources"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", isA(java.util.List.class)));
    }

    @Test
    void returnsResourceByIdAsJson() throws Exception {
        UUID resourceId = UUID.randomUUID();

        ResourceResponse mockResource = new ResourceResponse(
                resourceId,
                "Court 5",
                ResourceType.COURT,
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                true);

        when(resourceService.getById(resourceId)).thenReturn(mockResource);
        mockMvc.perform(get("/api/resources/{id}", resourceId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Court 5"));
    }

    @Test
    void returnsNotFoundWhenResourceDoesNotExist() throws Exception {
        UUID missingId = UUID.randomUUID();
        when(resourceService.getById(missingId))
                .thenThrow(new ResourceNotFoundException("Resource not found: " + missingId));

        mockMvc.perform(get("/api/resources/{id}", missingId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").value("Resource not found: " + missingId));

    }

    @Test
    void returnsBadRequestWhenIdIsNotValid() throws Exception {
        mockMvc.perform(get("/api/resources/{id}", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"));
    }

    @Test
    void updateReturnsNotFoundWhenResourceDoesNotExist() throws Exception {
        UUID missingId = UUID.randomUUID();
        when(resourceService.update(any(ResourceRequest.class),eq(missingId)))
                .thenThrow(new ResourceNotFoundException("Resource not found: " + missingId));

        String body = """
            {
              "name": "Court Update Test",
              "type": "COURT",
              "openingTime": "10:00:00",
              "closingTime": "13:00:00"
            }
            """;

        mockMvc.perform(put("/api/resources/{id}", missingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").value("Resource not found: " + missingId));
    }

    @Test
    void updateReturnsBadRequestWhenNameIsBlank() throws Exception {
        UUID id = UUID.randomUUID();
        String body = """
            {
              "name": "",
              "type": "COURT",
              "openingTime": "15:00:00",
              "closingTime": "16:00:00"
            }
            """;

        mockMvc.perform(put("/api/resources/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value(containsString("name")));
    }

    @Test
    void createReturnsBadRequestWhenNameIsBlank() throws Exception {
        String body = """
            {
              "name": "",
              "type": "COURT",
              "openingTime": "15:00:00",
              "closingTime": "16:00:00"
            }
            """;

        mockMvc.perform(post("/api/resources")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value(containsString("name")));
    }

    @Test
    void createReturnsBadRequestWhenTypeIsEmptyString() throws Exception {
        String body = """
            {
              "name": "Court 4",
              "type": "",
              "openingTime": "15:00:00",
              "closingTime": "16:00:00"
            }
            """;

        mockMvc.perform(post("/api/resources")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void createReturnsBadRequestWhenTypeIsMissing() throws Exception {
        String body = """
            {
              "name": "Court 4",
              "openingTime": "15:00:00",
              "closingTime": "16:00:00"
            }
            """;

        mockMvc.perform(post("/api/resources")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value(containsString("type")));
    }

    @Test
    void createReturnsBadRequestWhenTypeIsUnknown() throws Exception {
        String body = """
            {
              "name": "Room 4",
              "type": "PRIVATE_ROOM",
              "openingTime": "21:00:00",
              "closingTime": "22:00:00"
            }
            """;

        mockMvc.perform(post("/api/resources")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").value("Malformed or unreadable request body"));
    }

    @Test
    void createReturnsBadRequestWhenJsonIsMalformed() throws Exception {
        String body = """
            {
              "name": "Room 4",
              "type": "ROOM,
              "openingTime": "21:00:00",
              "closingTime": "22:00:00"
            }
            """;

        mockMvc.perform(post("/api/resources")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").value("Malformed or unreadable request body"));
    }

    @Test
    void deleteReturnsNotFoundWhenResourceDoesNotExist() throws Exception {
        UUID missingId = UUID.randomUUID();
        doThrow(new ResourceNotFoundException("Resource not found: " + missingId))
                .when(resourceService).delete(missingId);

        mockMvc.perform(delete("/api/resources/{id}", missingId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void returnsInternalServerErrorWithoutLeakingDetails() throws Exception {
        UUID id = UUID.randomUUID();
        when(resourceService.getById(id))
                .thenThrow(new RuntimeException("db password is hunter2"));

        mockMvc.perform(get("/api/resources/{id}", id))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.detail").value("Unexpected server error"))
                .andExpect(content().string(not(containsString("hunter2"))));
    }

    @Test
    void returnsMethodNotAllowedForPostOnPathWithId() throws Exception {
        mockMvc.perform(post("/api/resources/{id}", UUID.randomUUID()))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"));
    }

    @Test
    void returnsUnsupportedMediaTypeForPlainText() throws Exception {
        mockMvc.perform(post("/api/resources")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("hello"))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"));
    }

    @Test
    void createReturnsBadRequestWhenClosingTimeIsBeforeOpeningTime() throws Exception {
        String body = """
            {
              "name": "Court 1",
              "type": "COURT",
              "openingTime": "12:00:00",
              "closingTime": "11:00:00"
            }
            """;

        mockMvc.perform(post("/api/resources")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").value(containsString("openingTime must be before closingTime")));
    }

    @Test
    void updateReturnsBadRequestWhenClosingTimeIsBeforeOpeningTime() throws Exception {
        String body = """
            {
              "name": "Court 1",
              "type": "COURT",
              "openingTime": "22:00:00",
              "closingTime": "11:00:00"
            }
            """;

        mockMvc.perform(put("/api/resources/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").value(containsString("openingTime must be before closingTime")));
    }
    @Test
    void returnsNotFoundForUnknownPath() throws Exception {
        mockMvc.perform(get("/api/unknownPath"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").value("Endpoint not found"));
    }
}
