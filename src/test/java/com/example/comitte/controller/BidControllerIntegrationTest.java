package com.example.comitte.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ls.comitte.model.BidItem;
import com.ls.comitte.model.request.BidRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for BidController endpoints.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class BidControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateBid_Success() throws Exception {
        BidRequest request = new BidRequest();
        request.setComitteId(1L);
        request.setComitteNumber(1);
        request.setBidDate(LocalDateTime.now());
        request.setBidItems(new ArrayList<>());
        request.setReceiversList(List.of(1L, 2L));

        mockMvc.perform(post("/api/bids")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.bidId").exists());
    }

    @Test
    void testCreateBid_InvalidRequest() throws Exception {
        BidRequest request = new BidRequest();
        // Missing required comitteId

        mockMvc.perform(post("/api/bids")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetBid_Success() throws Exception {
        // First create a bid
        BidRequest request = new BidRequest();
        request.setComitteId(1L);
        request.setComitteNumber(1);
        request.setBidDate(LocalDateTime.now());
        request.setBidItems(new ArrayList<>());
        request.setReceiversList(List.of(1L, 2L));

        String createResponse = mockMvc.perform(post("/api/bids")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long bidId = objectMapper.readTree(createResponse).get("bidId").asLong();

        // Then retrieve it
        mockMvc.perform(get("/api/bids/{bidId}", bidId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bidId").value(bidId));
    }

    @Test
    void testUpdateBid_Success() throws Exception {
        // First create a bid
        BidRequest createRequest = new BidRequest();
        createRequest.setComitteId(1L);
        createRequest.setComitteNumber(1);
        createRequest.setBidDate(LocalDateTime.now());
        createRequest.setBidItems(new ArrayList<>());
        createRequest.setReceiversList(List.of(1L, 2L));

        String createResponse = mockMvc.perform(post("/api/bids")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long bidId = objectMapper.readTree(createResponse).get("bidId").asLong();

        // Then update it
        BidRequest updateRequest = new BidRequest();
        updateRequest.setComitteId(1L);
        updateRequest.setComitteNumber(2);
        updateRequest.setFinalBidAmt(5000);
        updateRequest.setBidDate(LocalDateTime.now());
        updateRequest.setBidItems(new ArrayList<>());
        updateRequest.setReceiversList(List.of(1L, 2L, 3L));

        mockMvc.perform(put("/api/bids/{bidId}", bidId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bidId").value(bidId));
    }

    @Test
    void testDeleteBid_Success() throws Exception {
        // First create a bid
        BidRequest request = new BidRequest();
        request.setComitteId(1L);
        request.setComitteNumber(1);
        request.setBidDate(LocalDateTime.now());
        request.setBidItems(new ArrayList<>());
        request.setReceiversList(List.of(1L, 2L));

        String createResponse = mockMvc.perform(post("/api/bids")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long bidId = objectMapper.readTree(createResponse).get("bidId").asLong();

        // Then delete it
        mockMvc.perform(delete("/api/bids/{bidId}", bidId))
                .andExpect(status().isNoContent());
    }

    @Test
    void testPlaceBid_Success() throws Exception {
        // First create a bid
        BidRequest request = new BidRequest();
        request.setComitteId(1L);
        request.setComitteNumber(1);
        request.setBidDate(LocalDateTime.now());
        request.setBidItems(new ArrayList<>());
        request.setReceiversList(List.of(1L, 2L));

        String createResponse = mockMvc.perform(post("/api/bids")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long bidId = objectMapper.readTree(createResponse).get("bidId").asLong();

        // Place a bid
        BidItem bidItem = new BidItem();
        bidItem.setMemberId(1L);
        bidItem.setBidAmount(8000);

        mockMvc.perform(post("/api/bids/{bidId}/place-bid", bidId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bidItem)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bidId").value(bidId));
    }

    @Test
    void testGetBidHistory() throws Exception {
        // First create a bid
        BidRequest request = new BidRequest();
        request.setComitteId(1L);
        request.setComitteNumber(1);
        request.setBidDate(LocalDateTime.now());
        request.setBidItems(new ArrayList<>());
        request.setReceiversList(List.of(1L, 2L));

        String createResponse = mockMvc.perform(post("/api/bids")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long bidId = objectMapper.readTree(createResponse).get("bidId").asLong();

        // Get history
        mockMvc.perform(get("/api/bids/{bidId}/history", bidId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bidId").value(bidId));
    }

    @Test
    void testGetBidsByComitteId() throws Exception {
        Long comitteId = 1L;

        mockMvc.perform(get("/api/bids/comitte/{comitteId}", comitteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
