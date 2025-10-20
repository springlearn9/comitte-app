package com.ls.comitte.controller;

import com.ls.comitte.test.TestDataFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for ComitteMemberMapController endpoints.
 *
 * Notes:
 * - Uses MemberController and ComitteController endpoints to create prerequisite entities.
 * - Adjust payloads in TestDataFactory to match your DTOs.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ComitteMemberMapControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    private static final String MEMBER_BASE = "/api/members";
    private static final String COMITTE_BASE = "/api/comittes";
    private static final String MAP_BASE = "/api/comitte-member-map";

    @Test
    void lifecycle_create_get_update_delete_map() throws Exception {
        // Create member
        var memberPayload = TestDataFactory.memberPayload("map.member@example.com", "Pass1!", "Map Member");
        var memberRes = mockMvc.perform(post(MEMBER_BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberPayload)))
                .andExpect(status().isCreated())
                .andReturn();
        Long memberId = objectMapper.readTree(memberRes.getResponse().getContentAsString()).get("memberId").asLong();

        // Create comitte
        var comittePayload = TestDataFactory.comittePayload("Map Comitte", "desc");
        var comRes = mockMvc.perform(post(COMITTE_BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comittePayload)))
                .andExpect(status().isCreated())
                .andReturn();
        Long comitteId = objectMapper.readTree(comRes.getResponse().getContentAsString()).get("comitteId").asLong();

        // Create mapping
        var mapPayload = TestDataFactory.comitteMemberMapPayload(comitteId, memberId);
        var mapRes = mockMvc.perform(post(MAP_BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mapPayload)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();
        Long mapId = objectMapper.readTree(mapRes.getResponse().getContentAsString()).get("id").asLong();

        // Get mapping
        mockMvc.perform(get(MAP_BASE + "/{id}", mapId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(mapId));

        // Update mapping (example: no-op update for now)
        mockMvc.perform(put(MAP_BASE + "/{id}", mapId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mapPayload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(mapId));

        // Delete mapping
        mockMvc.perform(delete(MAP_BASE + "/{id}", mapId))
                .andExpect(status().isNoContent());
    }
}