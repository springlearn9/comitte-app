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
 * Integration tests for BidController endpoints.
 *
 * Notes:
 * - Creates a comitte and a member as prerequisites.
 * - Adjust payloads/field names in TestDataFactory to match your DTOs.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BidControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    private static final String MEMBER_BASE = "/api/members";
    private static final String COMITTE_BASE = "/api/comittes";
    private static final String BID_BASE = "/api/bids";

    @Test
    void lifecycle_create_update_placeBid_listByComitte() throws Exception {
        // Create member
        var memberPayload = TestDataFactory.memberPayload("bid.member@example.com", "Pass123!", "Bid Member");
        var memberRes = mockMvc.perform(post(MEMBER_BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberPayload)))
                .andExpect(status().isCreated())
                .andReturn();
        Long memberId = objectMapper.readTree(memberRes.getResponse().getContentAsString()).get("memberId").asLong();

        // Create comitte
        var comittePayload = TestDataFactory.comittePayload("Bid Comitte", "desc");
        var comRes = mockMvc.perform(post(COMITTE_BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comittePayload)))
                .andExpect(status().isCreated())
                .andReturn();
        Long comitteId = objectMapper.readTree(comRes.getResponse().getContentAsString()).get("comitteId").asLong();

        // Create bid
        var bidPayload = TestDataFactory.bidPayload("Test Bid", comitteId);
        var bidRes = mockMvc.perform(post(BID_BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bidPayload)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();
        Long bidId = objectMapper.readTree(bidRes.getResponse().getContentAsString()).get("id").asLong();

        // Get bid
        mockMvc.perform(get(BID_BASE + "/{bidId}", bidId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bidId));

        // Update bid - change title
        bidPayload.put("title", "Updated Bid Title");
        mockMvc.perform(put(BID_BASE + "/{bidId}", bidId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bidPayload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bidId))
                .andExpect(jsonPath("$.title").value("Updated Bid Title"));

        // Place a bid item
        var bidItem = TestDataFactory.bidItemPayload(100, memberId);
        mockMvc.perform(post(BID_BASE + "/{bidId}/place-bid", bidId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bidItem)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bidId));

        // List bids by comitte
        mockMvc.perform(get(BID_BASE + "/comitte/{comitteId}", comitteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
