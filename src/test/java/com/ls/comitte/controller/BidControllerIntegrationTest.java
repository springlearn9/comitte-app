package com.ls.comitte.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ls.comitte.model.entity.Bid;
import com.ls.comitte.model.entity.Comitte;
import com.ls.comitte.model.entity.Member;
import com.ls.comitte.repository.BidRepository;
import com.ls.comitte.repository.ComitteRepository;
import com.ls.comitte.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class BidControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private ComitteRepository comitteRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Comitte testComitte;
    private Member testMember;

    @BeforeEach
    void setUp() {
        bidRepository.deleteAll();
        comitteRepository.deleteAll();
        memberRepository.deleteAll();

        testMember = Member.builder()
                .username("testmember")
                .email("testmember@example.com")
                .name("Test Member")
                .mobile("1234567890")
                .password("password")
                .build();
        testMember = memberRepository.save(testMember);

        testComitte = Comitte.builder()
                .ownerId(testMember.getMemberId())
                .comitteName("Test Committee")
                .startDate(LocalDate.of(2025, 1, 1))
                .fullAmount(100000)
                .membersCount(10)
                .fullShare(10000)
                .dueDateDays(5)
                .paymentDateDays(10)
                .build();
        testComitte = comitteRepository.save(testComitte);
    }

    @Test
    @WithMockUser
    void testCreateBid() throws Exception {
        Map<String, Object> bidRequest = createBidRequest(testComitte.getComitteId());

        mockMvc.perform(post("/api/bids")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bidRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.bidId").exists())
                .andExpect(jsonPath("$.comitteId").value(testComitte.getComitteId()))
                .andExpect(jsonPath("$.comitteNumber").value(1));
    }

    @Test
    @WithMockUser
    void testGetBid() throws Exception {
        Bid bid = createAndSaveBid(testComitte.getComitteId());

        mockMvc.perform(get("/api/bids/{bidId}", bid.getBidId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bidId").value(bid.getBidId()))
                .andExpect(jsonPath("$.comitteId").value(testComitte.getComitteId()))
                .andExpect(jsonPath("$.comitteNumber").value(1));
    }

    @Test
    @WithMockUser
    void testUpdateBid() throws Exception {
        Bid bid = createAndSaveBid(testComitte.getComitteId());

        Map<String, Object> updateRequest = createBidRequest(testComitte.getComitteId());
        updateRequest.put("comitteNumber", 2);
        updateRequest.put("finalBidder", testMember.getMemberId());
        updateRequest.put("finalBidAmt", 5000);

        mockMvc.perform(put("/api/bids/{bidId}", bid.getBidId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bidId").value(bid.getBidId()))
                .andExpect(jsonPath("$.comitteNumber").value(2))
                .andExpect(jsonPath("$.finalBidder").value(testMember.getMemberId()))
                .andExpect(jsonPath("$.finalBidAmt").value(5000));
    }

    @Test
    @WithMockUser
    void testDeleteBid() throws Exception {
        Bid bid = createAndSaveBid(testComitte.getComitteId());

        mockMvc.perform(delete("/api/bids/{bidId}", bid.getBidId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void testPlaceBid() throws Exception {
        Bid bid = createAndSaveBid(testComitte.getComitteId());

        Map<String, Object> bidItem = new HashMap<>();
        bidItem.put("bidder", testMember.getMemberId());
        bidItem.put("amount", 5000);
        bidItem.put("timestamp", LocalDateTime.now().toString());

        mockMvc.perform(post("/api/bids/{bidId}/place-bid", bid.getBidId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bidItem)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bidId").value(bid.getBidId()));
    }

    @Test
    @WithMockUser
    void testGetBidHistory() throws Exception {
        Bid bid = createAndSaveBid(testComitte.getComitteId());

        mockMvc.perform(get("/api/bids/{bidId}/history", bid.getBidId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bidId").value(bid.getBidId()))
                .andExpect(jsonPath("$.comitteId").value(testComitte.getComitteId()));
    }

    @Test
    @WithMockUser
    void testGetBidsByComitteId() throws Exception {
        createAndSaveBid(testComitte.getComitteId());
        createAndSaveBid(testComitte.getComitteId());

        mockMvc.perform(get("/api/bids/comitte/{comitteId}", testComitte.getComitteId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].comitteId").value(testComitte.getComitteId()))
                .andExpect(jsonPath("$[1].comitteId").value(testComitte.getComitteId()));
    }

    private Map<String, Object> createBidRequest(Long comitteId) {
        Map<String, Object> bid = new HashMap<>();
        bid.put("comitteId", comitteId);
        bid.put("comitteNumber", 1);
        bid.put("finalBidder", null);
        bid.put("finalBidAmt", null);
        bid.put("bidDate", LocalDateTime.now().toString());
        return bid;
    }

    private Bid createAndSaveBid(Long comitteId) {
        Bid bid = Bid.builder()
                .comitteId(comitteId)
                .comitteNumber(1)
                .bidDate(LocalDateTime.now())
                .build();
        return bidRepository.save(bid);
    }
}
