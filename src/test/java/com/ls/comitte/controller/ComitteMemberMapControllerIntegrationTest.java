package com.ls.comitte.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ls.comitte.model.request.ComitteMemberMapRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for ComitteMemberMapController endpoints.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ComitteMemberMapControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateMapping_Success() throws Exception {
        ComitteMemberMapRequest request = new ComitteMemberMapRequest();
        request.setComitteId(1L);
        request.setMemberId(1L);
        request.setShareCount(2);

        mockMvc.perform(post("/api/comitte-member-map")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void testGetMapping_Success() throws Exception {
        // First create a mapping
        ComitteMemberMapRequest request = new ComitteMemberMapRequest();
        request.setComitteId(1L);
        request.setMemberId(1L);
        request.setShareCount(2);

        String createResponse = mockMvc.perform(post("/api/comitte-member-map")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long mappingId = objectMapper.readTree(createResponse).get("id").asLong();

        // Then retrieve it
        mockMvc.perform(get("/api/comitte-member-map/{id}", mappingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(mappingId));
    }

    @Test
    void testUpdateMapping_Success() throws Exception {
        // First create a mapping
        ComitteMemberMapRequest createRequest = new ComitteMemberMapRequest();
        createRequest.setComitteId(1L);
        createRequest.setMemberId(1L);
        createRequest.setShareCount(2);

        String createResponse = mockMvc.perform(post("/api/comitte-member-map")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long mappingId = objectMapper.readTree(createResponse).get("id").asLong();

        // Then update it
        ComitteMemberMapRequest updateRequest = new ComitteMemberMapRequest();
        updateRequest.setComitteId(1L);
        updateRequest.setMemberId(1L);
        updateRequest.setShareCount(5);

        mockMvc.perform(put("/api/comitte-member-map/{id}", mappingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(mappingId));
    }

    @Test
    void testDeleteMapping_Success() throws Exception {
        // First create a mapping
        ComitteMemberMapRequest request = new ComitteMemberMapRequest();
        request.setComitteId(1L);
        request.setMemberId(1L);
        request.setShareCount(2);

        String createResponse = mockMvc.perform(post("/api/comitte-member-map")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long mappingId = objectMapper.readTree(createResponse).get("id").asLong();

        // Then delete it
        mockMvc.perform(delete("/api/comitte-member-map/{id}", mappingId))
                .andExpect(status().isNoContent());
    }
}
