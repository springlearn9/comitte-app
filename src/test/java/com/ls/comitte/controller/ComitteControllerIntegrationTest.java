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

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for ComitteController endpoints.
 *
 * These tests exercise the typical lifecycle:
 *  - create a comitte
 *  - fetch the comitte
 *  - update the comitte
 *  - assign members to the comitte
 *  - list comittes for a member
 *  - list bids for the comitte (should return an array)
 *  - delete the comitte
 *
 * Notes:
 * - Adjust TestDataFactory payload keys if your DTOs differ.
 * - Tests use an in-memory H2 DB via the 'test' profile.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ComitteControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    private static final String COMITTE_BASE = "/api/comittes";
    private static final String MEMBER_BASE = "/api/members";

    @BeforeEach
    void beforeEach() {
        // test profile uses create-drop; optionally seed or clear data here if needed
    }

    @Test
    void lifecycle_create_get_update_assign_list_and_delete_comitte() throws Exception {
        // 1) Create a member (to assign later)
        Map<String, Object> memberPayload = TestDataFactory.memberPayload("comitte.member@example.com", "Pass123!", "Comitte Member");
        var memberRes = mockMvc.perform(post(MEMBER_BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberPayload)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.memberId").exists())
                .andReturn();
        Long memberId = objectMapper.readTree(memberRes.getResponse().getContentAsString()).get("memberId").asLong();

        // 2) Create a comitte
        Map<String, Object> comittePayload = TestDataFactory.comittePayload("Integration Comitte", "Comitte for integration tests");
        var createRes = mockMvc.perform(post(COMITTE_BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comittePayload)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.comitteId").exists())
                .andExpect(jsonPath("$.title").value("Integration Comitte"))
                .andReturn();
        Long comitteId = objectMapper.readTree(createRes.getResponse().getContentAsString()).get("comitteId").asLong();

        // 3) Fetch the created comitte
        mockMvc.perform(get(COMITTE_BASE + "/{comitteId}", comitteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comitteId").value(comitteId))
                .andExpect(jsonPath("$.title").value("Integration Comitte"));

        // 4) Update the comitte (change title)
        comittePayload.put("title", "Updated Comitte Title");
        mockMvc.perform(put(COMITTE_BASE + "/{comitteId}", comitteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comittePayload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comitteId").value(comitteId))
                .andExpect(jsonPath("$.title").value("Updated Comitte Title"));

        // 5) Assign the member to the comitte
        var assignPayload = List.of(memberId);
        var assignRes = mockMvc.perform(post(COMITTE_BASE + "/{comitteId}/assign-members", comitteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assignPayload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comitteId").value(comitteId))
                .andReturn();

        // the returned ComitteDto may or may not include members; at minimum assert comitteId present
        // 6) Verify the member's comittes list contains the created comitte
        mockMvc.perform(get(COMITTE_BASE + "/member/{memberId}", memberId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].comitteId").value(comitteId));

        // 7) List bids for the comitte (should return an array, empty if none)
        mockMvc.perform(get(COMITTE_BASE + "/{comitteId}/bids", comitteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        // 8) Delete the comitte
        mockMvc.perform(delete(COMITTE_BASE + "/{comitteId}", comitteId))
                .andExpect(status().isNoContent());

        // 9) Optionally assert GET now returns 4xx (e.g., 404). Depending on service behavior, this may vary.
        // mockMvc.perform(get(COMITTE_BASE + "/{comitteId}", comitteId)).andExpect(status().isNotFound());
    }
}
