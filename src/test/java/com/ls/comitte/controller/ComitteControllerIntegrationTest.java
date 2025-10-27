package com.ls.comitte.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ls.comitte.model.request.ComitteRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for ComitteController endpoints.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ComitteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateComitte_Success() throws Exception {
        ComitteRequest request = new ComitteRequest(
                1L,
                "Test Committee",
                LocalDate.now(),
                10000,
                10,
                1000,
                5,
                10
        );

        mockMvc.perform(post("/api/comittes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.comitteId").exists())
                .andExpect(jsonPath("$.comitteName").value("Test Committee"));
    }

    @Test
    void testGetComitte_Success() throws Exception {
        // First create a committee
        ComitteRequest request = new ComitteRequest(
                1L,
                "Test Committee",
                LocalDate.now(),
                10000,
                10,
                1000,
                5,
                10
        );

        String createResponse = mockMvc.perform(post("/api/comittes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long comitteId = objectMapper.readTree(createResponse).get("comitteId").asLong();

        // Then retrieve it
        mockMvc.perform(get("/api/comittes/{comitteId}", comitteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comitteId").value(comitteId))
                .andExpect(jsonPath("$.comitteName").value("Test Committee"));
    }

    @Test
    void testUpdateComitte_Success() throws Exception {
        // First create a committee
        ComitteRequest createRequest = new ComitteRequest(
                1L,
                "Original Committee",
                LocalDate.now(),
                10000,
                10,
                1000,
                5,
                10
        );

        String createResponse = mockMvc.perform(post("/api/comittes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long comitteId = objectMapper.readTree(createResponse).get("comitteId").asLong();

        // Then update it
        ComitteRequest updateRequest = new ComitteRequest(
                1L,
                "Updated Committee",
                LocalDate.now(),
                20000,
                15,
                2000,
                7,
                14
        );

        mockMvc.perform(put("/api/comittes/{comitteId}", comitteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comitteName").value("Updated Committee"))
                .andExpect(jsonPath("$.fullAmount").value(20000));
    }

    @Test
    void testDeleteComitte_Success() throws Exception {
        // First create a committee
        ComitteRequest request = new ComitteRequest(
                1L,
                "Committee to Delete",
                LocalDate.now(),
                10000,
                10,
                1000,
                5,
                10
        );

        String createResponse = mockMvc.perform(post("/api/comittes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long comitteId = objectMapper.readTree(createResponse).get("comitteId").asLong();

        // Then delete it
        mockMvc.perform(delete("/api/comittes/{comitteId}", comitteId))
                .andExpect(status().isNoContent());
    }

    @Test
    void testAssignMembers_Success() throws Exception {
        // First create a committee
        ComitteRequest request = new ComitteRequest(
                1L,
                "Test Committee",
                LocalDate.now(),
                10000,
                10,
                1000,
                5,
                10
        );

        String createResponse = mockMvc.perform(post("/api/comittes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long comitteId = objectMapper.readTree(createResponse).get("comitteId").asLong();

        // Assign members
        List<Long> memberIds = List.of(1L, 2L, 3L);

        mockMvc.perform(post("/api/comittes/{comitteId}/assign-members", comitteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberIds)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comitteId").value(comitteId));
    }

    @Test
    void testGetMemberComittes() throws Exception {
        Long memberId = 1L;

        mockMvc.perform(get("/api/comittes/member/{memberId}", memberId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetOwnerComittes() throws Exception {
        Long ownerId = 1L;

        mockMvc.perform(get("/api/comittes/owner/{ownerId}", ownerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetBidsByComitteId() throws Exception {
        // First create a committee
        ComitteRequest request = new ComitteRequest(
                1L,
                "Test Committee",
                LocalDate.now(),
                10000,
                10,
                1000,
                5,
                10
        );

        String createResponse = mockMvc.perform(post("/api/comittes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long comitteId = objectMapper.readTree(createResponse).get("comitteId").asLong();

        mockMvc.perform(get("/api/comittes/{comitteId}/bids", comitteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetAllAssociatedMembers() throws Exception {
        // First create a committee
        ComitteRequest request = new ComitteRequest(
                1L,
                "Test Committee",
                LocalDate.now(),
                10000,
                10,
                1000,
                5,
                10
        );

        String createResponse = mockMvc.perform(post("/api/comittes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long comitteId = objectMapper.readTree(createResponse).get("comitteId").asLong();

        mockMvc.perform(get("/api/comittes/{comitteId}/members", comitteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
