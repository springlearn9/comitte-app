package com.ls.comitte.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ls.comitte.model.entity.Comitte;
import com.ls.comitte.model.entity.Member;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ComitteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ComitteRepository comitteRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member testOwner;

    @BeforeEach
    void setUp() {
        comitteRepository.deleteAll();
        memberRepository.deleteAll();
        
        testOwner = Member.builder()
                .username("owner1")
                .email("owner1@example.com")
                .name("Test Owner")
                .mobile("1234567890")
                .password("password")
                .build();
        testOwner = memberRepository.save(testOwner);
    }

    @Test
    @WithMockUser
    void testCreateComitte() throws Exception {
        Map<String, Object> comitteRequest = createComitteRequest(testOwner.getMemberId());

        mockMvc.perform(post("/api/comittes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comitteRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.comitteId").exists())
                .andExpect(jsonPath("$.comitteName").value("Test Committee"))
                .andExpect(jsonPath("$.ownerId").value(testOwner.getMemberId()))
                .andExpect(jsonPath("$.fullAmount").value(100000))
                .andExpect(jsonPath("$.membersCount").value(10));
    }

    @Test
    @WithMockUser
    void testGetComitte() throws Exception {
        Comitte comitte = createAndSaveComitte(testOwner.getMemberId());

        mockMvc.perform(get("/api/comittes/{comitteId}", comitte.getComitteId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comitteId").value(comitte.getComitteId()))
                .andExpect(jsonPath("$.comitteName").value("Test Committee"))
                .andExpect(jsonPath("$.ownerId").value(testOwner.getMemberId()));
    }

    @Test
    @WithMockUser
    void testUpdateComitte() throws Exception {
        Comitte comitte = createAndSaveComitte(testOwner.getMemberId());

        Map<String, Object> updateRequest = createComitteRequest(testOwner.getMemberId());
        updateRequest.put("comitteName", "Updated Committee");
        updateRequest.put("fullAmount", 200000);

        mockMvc.perform(put("/api/comittes/{comitteId}", comitte.getComitteId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comitteId").value(comitte.getComitteId()))
                .andExpect(jsonPath("$.comitteName").value("Updated Committee"))
                .andExpect(jsonPath("$.fullAmount").value(200000));
    }

    @Test
    @WithMockUser
    void testDeleteComitte() throws Exception {
        Comitte comitte = createAndSaveComitte(testOwner.getMemberId());

        mockMvc.perform(delete("/api/comittes/{comitteId}", comitte.getComitteId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void testGetOwnerComittes() throws Exception {
        createAndSaveComitte(testOwner.getMemberId());
        createAndSaveComitte(testOwner.getMemberId());

        mockMvc.perform(get("/api/comittes/owner/{ownerId}", testOwner.getMemberId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].ownerId").value(testOwner.getMemberId()))
                .andExpect(jsonPath("$[1].ownerId").value(testOwner.getMemberId()));
    }

    @Test
    @WithMockUser
    void testAssignMembers() throws Exception {
        Comitte comitte = createAndSaveComitte(testOwner.getMemberId());
        
        Member member1 = memberRepository.save(Member.builder()
                .username("member1")
                .email("member1@example.com")
                .name("Member 1")
                .mobile("1111111111")
                .password("password")
                .build());
        
        Member member2 = memberRepository.save(Member.builder()
                .username("member2")
                .email("member2@example.com")
                .name("Member 2")
                .mobile("2222222222")
                .password("password")
                .build());

        List<Long> memberIds = List.of(member1.getMemberId(), member2.getMemberId());

        mockMvc.perform(post("/api/comittes/{comitteId}/assign-members", comitte.getComitteId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberIds)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comitteId").value(comitte.getComitteId()));
    }

    @Test
    @WithMockUser
    void testGetAllAssociatedMembers() throws Exception {
        Comitte comitte = createAndSaveComitte(testOwner.getMemberId());

        mockMvc.perform(get("/api/comittes/{comitteId}/members", comitte.getComitteId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser
    void testGetBidsByComitteId() throws Exception {
        Comitte comitte = createAndSaveComitte(testOwner.getMemberId());

        mockMvc.perform(get("/api/comittes/{comitteId}/bids", comitte.getComitteId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    private Map<String, Object> createComitteRequest(Long ownerId) {
        Map<String, Object> comitte = new HashMap<>();
        comitte.put("ownerId", ownerId);
        comitte.put("comitteName", "Test Committee");
        comitte.put("startDate", "2025-01-01");
        comitte.put("fullAmount", 100000);
        comitte.put("membersCount", 10);
        comitte.put("fullShare", 10000);
        comitte.put("dueDateDays", 5);
        comitte.put("paymentDateDays", 10);
        return comitte;
    }

    private Comitte createAndSaveComitte(Long ownerId) {
        Comitte comitte = Comitte.builder()
                .ownerId(ownerId)
                .comitteName("Test Committee")
                .startDate(LocalDate.of(2025, 1, 1))
                .fullAmount(100000)
                .membersCount(10)
                .fullShare(10000)
                .dueDateDays(5)
                .paymentDateDays(10)
                .build();
        return comitteRepository.save(comitte);
    }
}
