package com.slotbook.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(HelloController.class)
class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void returnsHelloMessageAsJson() throws Exception {
        mockMvc.perform(get("/api/hello"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Hello, how are you?"))
                .andExpect(jsonPath("$.timestamp").exists());
        }
    }
