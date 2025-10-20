package com.ls.comitte.test;

import java.util.HashMap;
import java.util.Map;

/**
 * Small helper to build generic JSON payload maps for tests.
 * Adapt the keys to match your DTO field names if necessary.
 */
public final class TestDataFactory {
    private TestDataFactory() {}

    public static Map<String, Object> memberPayload(String email, String password, String name) {
        Map<String, Object> m = new HashMap<>();
        m.put("email", email);
        m.put("password", password);
        m.put("name", name);
        // TODO: add other required fields for MemberCreateDto if any
        return m;
    }

    public static Map<String, Object> comittePayload(String title, String description) {
        Map<String, Object> m = new HashMap<>();
        m.put("title", title);
        m.put("description", description);
        // TODO: add other required fields for ComitteCreateDto if any
        return m;
    }

    public static Map<String, Object> bidPayload(String title, Long comitteId) {
        Map<String, Object> m = new HashMap<>();
        m.put("title", title);
        m.put("comitteId", comitteId);
        // TODO: add other required fields for ComitteBidCreateDto if needed
        return m;
    }

    public static Map<String, Object> bidItemPayload(Number amount, Long bidderId) {
        Map<String, Object> m = new HashMap<>();
        m.put("amount", amount);
        m.put("bidderId", bidderId);
        // TODO: adapt fields to your BidItemDto
        return m;
    }

    public static Map<String, Object> comitteMemberMapPayload(Long comitteId, Long memberId) {
        Map<String, Object> m = new HashMap<>();
        m.put("comitteId", comitteId);
        m.put("memberId", memberId);
        return m;
    }
}