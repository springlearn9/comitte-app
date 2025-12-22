package com.ls.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service for managing token blacklist and session activity tracking.
 * Implements token invalidation and automatic session timeout.
 */
@Service
@Slf4j
public class TokenBlacklistService {
    
    // Map to store blacklisted tokens
    private final Map<String, Instant> blacklistedTokens = new ConcurrentHashMap<>();
    
    // Map to track last activity time for each token
    private final Map<String, Instant> tokenLastActivity = new ConcurrentHashMap<>();
    
    // Session timeout in seconds (configurable, default 300 seconds )
    private static final long SESSION_TIMEOUT_SECONDS = 300;
    
    /**
     * Add a token to the blacklist (logout)
     */
    public void blacklistToken(String token) {
        blacklistedTokens.put(token, Instant.now());
        tokenLastActivity.remove(token);
        log.info("Token blacklisted successfully");
    }
    
    /**
     * Check if a token is blacklisted
     */
    public boolean isTokenBlacklisted(String token) {
        // Clean up expired blacklisted tokens (tokens that have been blacklisted for more than 1 hour)
        cleanupExpiredBlacklistedTokens();
        return blacklistedTokens.containsKey(token);
    }
    
    /**
     * Update the last activity time for a token
     */
    public void updateActivity(String token) {
        tokenLastActivity.put(token, Instant.now());
    }
    
    /**
     * Check if a token has expired due to inactivity
     */
    public boolean isSessionExpired(String token) {
        Instant lastActivity = tokenLastActivity.get(token);
        if (lastActivity == null) {
            // No activity recorded, consider it as first access
            updateActivity(token);
            return false;
        }
        
        Instant now = Instant.now();
        long secondsSinceLastActivity = now.getEpochSecond() - lastActivity.getEpochSecond();
        
        if (secondsSinceLastActivity > SESSION_TIMEOUT_SECONDS) {
            log.info("Session expired due to inactivity: {} seconds", secondsSinceLastActivity);
            // Automatically blacklist the expired token
            blacklistToken(token);
            return true;
        }
        
        return false;
    }
    
    /**
     * Get the session timeout in seconds
     */
    public long getSessionTimeoutSeconds() {
        return SESSION_TIMEOUT_SECONDS;
    }
    
    /**
     * Remove tokens that have been blacklisted for more than 1 hour
     */
    private void cleanupExpiredBlacklistedTokens() {
        Instant oneHourAgo = Instant.now().minusSeconds(3600);
        blacklistedTokens.entrySet().removeIf(entry -> entry.getValue().isBefore(oneHourAgo));
    }
    
    /**
     * Get remaining time before session expires (in seconds)
     */
    public long getRemainingSessionTime(String token) {
        Instant lastActivity = tokenLastActivity.get(token);
        if (lastActivity == null) {
            return SESSION_TIMEOUT_SECONDS;
        }
        
        Instant now = Instant.now();
        long secondsSinceLastActivity = now.getEpochSecond() - lastActivity.getEpochSecond();
        long remaining = SESSION_TIMEOUT_SECONDS - secondsSinceLastActivity;
        
        return Math.max(0, remaining);
    }
}
