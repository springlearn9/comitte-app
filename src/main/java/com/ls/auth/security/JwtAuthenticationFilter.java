package com.ls.auth.security;

import com.ls.auth.service.AuthService;
import com.ls.auth.service.TokenBlacklistService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JWT Authentication Filter that validates tokens on every request.
 * This filter runs before Spring Security's authentication process.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthService authService;
    private final TokenBlacklistService tokenBlacklistService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        // Skip filter for public endpoints
        String path = request.getRequestURI();
        if (isPublicEndpoint(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Extract JWT token from Authorization header
            String authHeader = request.getHeader("Authorization");
            
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                // No token provided for protected endpoint
                sendUnauthorizedResponse(response, "Authentication required. Please provide a valid token.");
                return;
            }

            String token = authHeader.substring(7);

            // Check if token is blacklisted
            if (tokenBlacklistService.isTokenBlacklisted(token)) {
                log.warn("Blacklisted token attempted to access: {}", path);
                sendUnauthorizedResponse(response, "Token has been invalidated. Please login again.");
                return;
            }

            // Check if session expired due to inactivity
            if (tokenBlacklistService.isSessionExpired(token)) {
                log.warn("Expired session attempted to access: {}", path);
                // Automatically blacklist expired session
                tokenBlacklistService.blacklistToken(token);
                sendUnauthorizedResponse(response, "Session expired due to inactivity. Please login again.");
                return;
            }

            // Validate and extract claims from JWT
            String username = authService.extractUsername(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Validate token
                if (authService.validateToken(token, username)) {
                    // Get roles from database
                    List<String> roles = authService.getRolesByUsername(username);
                    
                    // Create authentication token with authorities
                    List<SimpleGrantedAuthority> authorities = roles.stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Set authentication in security context
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    // Update last activity time
                    tokenBlacklistService.updateActivity(token);
                    
                    log.debug("User '{}' authenticated successfully for path: {}", username, path);
                } else {
                    sendUnauthorizedResponse(response, "Invalid or expired token.");
                    return;
                }
            }

        } catch (Exception e) {
            log.error("JWT authentication error: {}", e.getMessage());
            sendUnauthorizedResponse(response, "Authentication failed: " + e.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Check if the endpoint is public and doesn't require authentication
     */
    private boolean isPublicEndpoint(String path) {
        return path.startsWith("/api/auth/login")
                || path.startsWith("/api/auth/register")
                || path.startsWith("/api/password/")
                || path.startsWith("/swagger-ui/")
                || path.startsWith("/api-docs/")
                || path.startsWith("/v3/api-docs/")
                || path.equals("/error");
    }

    /**
     * Send unauthorized response with JSON error message
     */
    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(String.format("{\"error\":\"%s\"}", message));
    }
}
