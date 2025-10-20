package com.ls.comitte.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ls.comitte.model.request.MemberRequest;
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
 * Integration tests for MemberController endpoints.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class MemberControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateMember_Success() throws Exception {
        MemberRequest request = new MemberRequest();
        request.setUsername("testuser");
        request.setName("Test User");
        request.setEmail("test@example.com");
        request.setMobile("1234567890");
        request.setPassword("password123");

        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.memberId").exists())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    void testGetMember_Success() throws Exception {
        // First create a member
        MemberRequest request = new MemberRequest();
        request.setUsername("testuser");
        request.setName("Test User");
        request.setEmail("test@example.com");
        request.setMobile("1234567890");
        request.setPassword("password123");

        String createResponse = mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long memberId = objectMapper.readTree(createResponse).get("memberId").asLong();

        // Then retrieve it
        mockMvc.perform(get("/api/members/{memberId}", memberId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(memberId))
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    void testUpdateMember_Success() throws Exception {
        // First create a member
        MemberRequest createRequest = new MemberRequest();
        createRequest.setUsername("testuser");
        createRequest.setName("Original Name");
        createRequest.setEmail("test@example.com");
        createRequest.setMobile("1234567890");
        createRequest.setPassword("password123");

        String createResponse = mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long memberId = objectMapper.readTree(createResponse).get("memberId").asLong();

        // Then update it
        MemberRequest updateRequest = new MemberRequest();
        updateRequest.setUsername("testuser");
        updateRequest.setName("Updated Name");
        updateRequest.setEmail("updated@example.com");
        updateRequest.setMobile("9876543210");
        updateRequest.setPassword("newpassword123");

        mockMvc.perform(put("/api/members/{memberId}", memberId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.email").value("updated@example.com"));
    }

    @Test
    void testDeleteMember_Success() throws Exception {
        // First create a member
        MemberRequest request = new MemberRequest();
        request.setUsername("testuser");
        request.setName("Test User");
        request.setEmail("test@example.com");
        request.setMobile("1234567890");
        request.setPassword("password123");

        String createResponse = mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long memberId = objectMapper.readTree(createResponse).get("memberId").asLong();

        // Then delete it
        mockMvc.perform(delete("/api/members/{memberId}", memberId))
                .andExpect(status().isNoContent());
    }

    @Test
    void testSearchMembers_ByName() throws Exception {
        mockMvc.perform(get("/api/members/search")
                        .param("name", "Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testSearchMembers_ByMobile() throws Exception {
        mockMvc.perform(get("/api/members/search")
                        .param("mobile", "1234567890"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testSearchMembers_ByUsername() throws Exception {
        mockMvc.perform(get("/api/members/search")
                        .param("username", "testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testSearchMembers_NoParameters() throws Exception {
        mockMvc.perform(get("/api/members/search"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
