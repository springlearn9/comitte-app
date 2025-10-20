package com.ls.auth.controller;

import com.ls.comitte.test.TestDataFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for authentication endpoints (register, login, refresh, logout).
 *
 * Notes:
 * - Tests use an in-memory H2 DB (application-test.yml) and MockMvc with @SpringBootTest.
 * - Adjust payload keys in TestDataFactory if your DTO field names differ.
 * - These tests assume AuthResponseDto contains at least userId, accessToken and refreshToken fields.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    private static final String REGISTER_URL = "/api/auth/register";
    private static final String LOGIN_URL = "/api/auth/login";
    private static final String REFRESH_URL = "/api/auth/refresh";
    private static final String LOGOUT_URL = "/api/auth/logout";

    @BeforeEach
    void beforeEach() {
        // test profile uses create-drop; optionally clear/seed DB here if needed
    }

    @Test
    void registerShouldReturn201AndResponseBody() throws Exception {
        var payload = TestDataFactory.memberPayload("auth.test.register@example.com", "Password1!", "Auth Register Test");

        mockMvc.perform(post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                // AuthResponseDto: expect userId and at least accessToken (adjust keys if different)
                .andExpect(jsonPath("$.userId").exists())
                .andExpect(jsonPath("$.accessToken").exists());
    }

    @Test
    void loginShouldReturnTokens_whenRegistered() throws Exception {
        // Register first
        var registerPayload = TestDataFactory.memberPayload("auth.test.login@example.com", "Password2!", "Auth Login Test");
        mockMvc.perform(post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerPayload)))
                .andExpect(status().isCreated());

        // Login
        var loginPayload = Map.of(
                "email", "auth.test.login@example.com",
                "password", "Password2!"
        );

        var loginResult = mockMvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginPayload)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists())
                .andReturn();

        // Optional: inspect tokens programmatically
        String body = loginResult.getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(body);
        String refreshToken = node.path("refreshToken").asText();
        // basic sanity check
        assert refreshToken != null && !refreshToken.isEmpty();
    }

    @Test
    void refreshAndLogout_flow_shouldReturnNewTokenAndNoContentOnLogout() throws Exception {
        // Register and login to obtain refresh token
        var registerPayload = TestDataFactory.memberPayload("auth.test.refresh@example.com", "Password3!", "Auth Refresh Test");
        mockMvc.perform(post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerPayload)))
                .andExpect(status().isCreated());

        var loginPayload = Map.of(
                "email", "auth.test.refresh@example.com",
                "password", "Password3!"
        );

        var loginResult = mockMvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginPayload)))
                .andExpect(status().isOk())
                .andReturn();

        String loginBody = loginResult.getResponse().getContentAsString();
        JsonNode loginNode = objectMapper.readTree(loginBody);
        String refreshToken = loginNode.path("refreshToken").asText();

        // Refresh
        var refreshPayload = Map.of("refreshToken", refreshToken);
        mockMvc.perform(post(REFRESH_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshPayload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists());

        // Logout - revoke tokens (uses same refresh token)
        var logoutPayload = Map.of("refreshToken", refreshToken);
        mockMvc.perform(post(LOGOUT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logoutPayload)))
                .andExpect(status().isNoContent());
    }
}
