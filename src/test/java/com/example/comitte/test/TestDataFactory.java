package com.example.comitte.test;

import java.util.HashMap;
import java.util.Map;

/**
 * Small helper to build generic JSON payload maps for tests.
 * Adapt the keys to match your DTO field names if necessary.
 */
public final class TestDataFactory {
    private TestDataFactory() {}

    public static Map<String, Object> createComitteRequest(String name, Long ownerId) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("ownerId", ownerId);
        return map;
    }

    public static Map<String, Object> createMemberRequest(String name, String mobile, String username) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("mobile", mobile);
        map.put("username", username);
        return map;
    }

    public static Map<String, Object> createBidRequest(Long comitteId, String description) {
        Map<String, Object> map = new HashMap<>();
        map.put("comitteId", comitteId);
        map.put("description", description);
        return map;
    }
}
