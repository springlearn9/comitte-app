package com.ls.comitte.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ls.comitte.model.entity.Member;
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

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class MemberControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
    }

    @Test
    @WithMockUser
    void testCreateMember() throws Exception {
        Map<String, Object> memberRequest = createMemberRequest("testuser1");

        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.memberId").exists())
                .andExpect(jsonPath("$.username").value("testuser1"))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("testuser1@example.com"));
    }

    @Test
    @WithMockUser
    void testGetMember() throws Exception {
        Member member = createAndSaveMember("getuser");

        mockMvc.perform(get("/api/members/{memberId}", member.getMemberId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(member.getMemberId()))
                .andExpect(jsonPath("$.username").value("getuser"))
                .andExpect(jsonPath("$.name").value("Test User"));
    }

    @Test
    @WithMockUser
    void testUpdateMember() throws Exception {
        Member member = createAndSaveMember("updateuser");

        Map<String, Object> updateRequest = createMemberRequest("updateuser");
        updateRequest.put("name", "Updated Name");
        updateRequest.put("mobile", "9876543210");

        mockMvc.perform(put("/api/members/{memberId}", member.getMemberId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(member.getMemberId()))
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.mobile").value("9876543210"));
    }

    @Test
    @WithMockUser
    void testDeleteMember() throws Exception {
        Member member = createAndSaveMember("deleteuser");

        mockMvc.perform(delete("/api/members/{memberId}", member.getMemberId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void testSearchMembersByName() throws Exception {
        createAndSaveMember("user1");
        createAndSaveMember("user2");

        mockMvc.perform(get("/api/members/search")
                        .param("name", "Test User"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
    }

    @Test
    @WithMockUser
    void testSearchMembersByUsername() throws Exception {
        createAndSaveMember("searchuser");

        mockMvc.perform(get("/api/members/search")
                        .param("username", "searchuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username").value("searchuser"));
    }

    @Test
    @WithMockUser
    void testSearchMembersByMobile() throws Exception {
        Member member = createAndSaveMember("mobileuser");

        mockMvc.perform(get("/api/members/search")
                        .param("mobile", "1234567890"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    private Map<String, Object> createMemberRequest(String username) {
        Map<String, Object> member = new HashMap<>();
        member.put("username", username);
        member.put("email", username + "@example.com");
        member.put("name", "Test User");
        member.put("mobile", "1234567890");
        member.put("aadharNo", "123456789012");
        member.put("password", "password123");
        member.put("address", "123 Test Street");
        return member;
    }

    private Member createAndSaveMember(String username) {
        Member member = Member.builder()
                .username(username)
                .email(username + "@example.com")
                .name("Test User")
                .mobile("1234567890")
                .aadharNo("123456789012")
                .password("password123")
                .address("123 Test Street")
                .build();
        return memberRepository.save(member);
    }
}
