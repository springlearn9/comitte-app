package com.example.comitte.test;

import java.util.HashMap;
import java.util.Map;

public final class TestDataFactory {
    private TestDataFactory() {}

    public static Map<String, Object> createMemberRequest() {
        Map<String, Object> member = new HashMap<>();
        member.put("username", "testuser");
        member.put("email", "test@example.com");
        member.put("name", "Test User");
        member.put("mobile", "1234567890");
        member.put("aadharNo", "123456789012");
        member.put("password", "password123");
        member.put("address", "123 Test Street");
        return member;
    }

    public static Map<String, Object> createComitteRequest(Long ownerId) {
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

    public static Map<String, Object> createBidRequest(Long comitteId) {
        Map<String, Object> bid = new HashMap<>();
        bid.put("comitteId", comitteId);
        bid.put("comitteNumber", 1);
        bid.put("finalBidder", null);
        bid.put("finalBidAmt", null);
        bid.put("bidDate", "2025-01-15T10:00:00");
        return bid;
    }
}
