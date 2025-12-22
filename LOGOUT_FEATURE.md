# Logout and Session Management Feature

## Overview
This feature implements logout functionality with automatic session expiration after 15 seconds of inactivity.

## How It Works

### 1. Session Timeout
- **Default timeout**: 15 seconds of inactivity
- Sessions are tracked per token
- Any API call (except login/register) updates the activity timestamp
- After 15 seconds of no activity, the session automatically expires

### 2. Token Blacklisting
- When a user logs out, their token is added to a blacklist
- Blacklisted tokens are rejected on all subsequent requests
- Expired sessions are automatically blacklisted

## API Endpoints

### Logout
```
POST /api/auth/logout
Headers: Authorization: Bearer <token>
Response: {
  "message": "Logged out successfully",
  "timestamp": 1703234567890
}
```

### Check Session Status
```
GET /api/auth/session-status
Headers: Authorization: Bearer <token>
Response: {
  "active": true,
  "remainingSeconds": 12,
  "message": "Session active"
}
```

## Implementation Details

### Components Created:
1. **TokenBlacklistService** - Manages token blacklist and session tracking
2. **TokenValidationInterceptor** - Intercepts requests to validate tokens
3. **WebMvcConfig** - Registers the interceptor
4. **LogoutResponse** - Response DTO for logout
5. **SessionStatusResponse** - Response DTO for session status

### Workflow:
1. User logs in → Token is generated and activity tracking begins
2. User makes API calls → Activity timestamp is updated on each request
3. If no activity for 15 seconds → Session expires automatically
4. User logs out → Token is blacklisted immediately
5. Any request with blacklisted/expired token → Returns 401 Unauthorized

## Configuration

To change the session timeout, modify the constant in `TokenBlacklistService.java`:
```java
private static final long SESSION_TIMEOUT_SECONDS = 15; // Change this value
```

## Frontend Integration

### Keep Session Alive
Call any API endpoint (e.g., `/api/auth/session-status`) periodically to keep the session active.

### Display Session Timer
Poll `/api/auth/session-status` every few seconds to show remaining time:
```javascript
// Example: Check session every 3 seconds
setInterval(async () => {
  const response = await fetch('/api/auth/session-status', {
    headers: { 'Authorization': `Bearer ${token}` }
  });
  const data = await response.json();
  if (!data.active) {
    // Redirect to login
    window.location.href = '/login';
  } else {
    // Update UI with remaining time
    console.log(`Session expires in ${data.remainingSeconds} seconds`);
  }
}, 3000);
```

### Logout
```javascript
const logout = async () => {
  await fetch('/api/auth/logout', {
    method: 'POST',
    headers: { 'Authorization': `Bearer ${token}` }
  });
  // Clear token and redirect
  localStorage.removeItem('token');
  window.location.href = '/login';
};
```

## Security Notes
- Tokens are kept in memory (ConcurrentHashMap) - cleared on server restart
- For production, consider using Redis for distributed token storage
- Session timeout helps prevent unauthorized access from abandoned sessions
- Always use HTTPS in production to protect tokens in transit
