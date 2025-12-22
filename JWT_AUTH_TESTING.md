# JWT Authentication & Session Management Testing Guide

## What Changed?

### ✅ Fixed Issues:
1. **Proper JWT Authentication** - All protected endpoints now require a valid JWT token
2. **Session Timeout** - 15-second inactivity timeout is enforced
3. **Token Blacklisting** - Logged out tokens are immediately invalidated
4. **Browser Access Protection** - Direct browser access (without token) returns 401 Unauthorized

### 🔒 Security Implementation:
- **JwtAuthenticationFilter**: Validates every request before Spring Security
- **Token Validation**: Checks blacklist, expiry, and session timeout
- **Authentication Required**: All endpoints (except public ones) require `Authorization: Bearer <token>` header

---

## Testing the System

### 1. **Login** ✅
```bash
POST http://localhost:8082/api/auth/login
Content-Type: application/json

{
  "usernameOrEmail": "sanjay.tyagi",
  "password": "test123"
}

# Response:
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "tokenType": "Bearer",
  "expiresIn": 360000000,
  "member": { ... }
}
```

### 2. **Access Protected Endpoint (WITH Token)** ✅
```bash
GET http://localhost:8082/api/comittes/my/6
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...

# Response: 200 OK with committee data
```

### 3. **Access Protected Endpoint (WITHOUT Token)** 🚫
```bash
# From browser address bar OR without Authorization header:
GET http://localhost:8082/api/comittes/my/6

# Response: 401 Unauthorized
{
  "error": "Authentication required. Please provide a valid token."
}
```

### 4. **Test Session Timeout** ⏱️
```bash
# Step 1: Login and get token
POST http://localhost:8082/api/auth/login

# Step 2: Use token immediately (works fine)
GET http://localhost:8082/api/comittes/my/6
Authorization: Bearer <token>
# Response: 200 OK

# Step 3: Wait 16+ seconds (no activity)

# Step 4: Try to use token again
GET http://localhost:8082/api/comittes/my/6
Authorization: Bearer <token>
# Response: 401 Unauthorized
{
  "error": "Session expired due to inactivity. Please login again."
}
```

### 5. **Check Session Status** 📊
```bash
GET http://localhost:8082/api/auth/session-status
Authorization: Bearer <token>

# Response (if active):
{
  "active": true,
  "remainingSeconds": 12,
  "message": "Session active"
}

# Response (if expired):
{
  "active": false,
  "remainingSeconds": 0,
  "message": "Session expired"
}
```

### 6. **Logout** 🚪
```bash
POST http://localhost:8082/api/auth/logout
Authorization: Bearer <token>

# Response:
{
  "message": "Logged out successfully",
  "timestamp": 1703234567890
}

# After logout, any request with this token:
GET http://localhost:8082/api/comittes/my/6
Authorization: Bearer <token>
# Response: 401 Unauthorized
{
  "error": "Token has been invalidated. Please login again."
}
```

---

## Public Endpoints (No Authentication Required)

These endpoints work **without** a token:
- `/api/auth/login`
- `/api/auth/register`
- `/api/password/**` (password reset)
- `/swagger-ui/**` (API documentation)
- `/api-docs/**` (OpenAPI specs)

---

## Protected Endpoints (Require Authentication)

All other endpoints require `Authorization: Bearer <token>` header:
- `/api/comittes/**`
- `/api/members/**`
- `/api/bids/**`
- `/api/comitte-member-map/**`
- `/api/auth/logout`
- `/api/auth/session-status`

---

## Testing with Postman/Insomnia

### Setup:
1. **Login** to get your token
2. **Copy** the token from response
3. **Add to Authorization**:
   - Type: `Bearer Token`
   - Token: `<paste your token>`
4. **Make requests** - token will auto-expire after 15 seconds of inactivity

### Keep Session Alive:
- Make ANY API call within 15 seconds to reset the timer
- Check `/api/auth/session-status` periodically to see remaining time

---

## Testing from Browser

### ❌ Won't Work (Returns 401):
```
http://localhost:8082/api/comittes/my/6
```
**Reason**: Browser address bar doesn't send `Authorization` header

### ✅ Will Work:
1. Use browser **developer tools console**:
```javascript
fetch('http://localhost:8082/api/comittes/my/6', {
  headers: {
    'Authorization': 'Bearer <your-token-here>'
  }
})
.then(r => r.json())
.then(console.log);
```

2. Or use a **browser extension** like:
   - ModHeader (Chrome/Edge)
   - Modify Header Value (Firefox)

---

## Server Logs to Watch

### Successful Authentication:
```
INFO - User 'sanjay.tyagi' authenticated successfully for path: /api/comittes/my/6
```

### Session Expired:
```
INFO - Session expired due to inactivity: 17 seconds
WARN - Expired session attempted to access: /api/comittes/my/6
```

### Blacklisted Token (After Logout):
```
INFO - Token blacklisted successfully
WARN - Blacklisted token attempted to access: /api/comittes/my/6
```

### Missing Token:
```
# No specific log - returns 401 immediately in filter
```

---

## Frontend Integration

### React/Vue/Angular Example:
```javascript
// Store token after login
const loginResponse = await fetch('/api/auth/login', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ usernameOrEmail, password })
});
const { token } = await loginResponse.json();
localStorage.setItem('token', token);

// Use token in all requests
const committees = await fetch('/api/comittes/my/6', {
  headers: {
    'Authorization': `Bearer ${localStorage.getItem('token')}`
  }
});

// Handle 401 errors
if (committees.status === 401) {
  // Redirect to login
  localStorage.removeItem('token');
  window.location.href = '/login';
}

// Keep session alive with periodic ping
setInterval(async () => {
  const response = await fetch('/api/auth/session-status', {
    headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` }
  });
  const status = await response.json();
  if (!status.active) {
    alert('Session expired. Please login again.');
    window.location.href = '/login';
  }
}, 10000); // Check every 10 seconds
```

---

## Configuration

To change session timeout, edit `TokenBlacklistService.java`:
```java
private static final long SESSION_TIMEOUT_SECONDS = 15; // Change this
```

Then restart the application.

---

## Troubleshooting

### "I'm still getting data from browser without token"
- Check if you're accessing a **public endpoint** (login, register, password reset)
- Clear browser cache and try again
- Verify the JWT filter is running (check server logs on startup)

### "Token expires immediately"
- Check system clock (JWT uses timestamps)
- Verify `jwt.expiration-ms` in `application.yml`
- Check for exceptions in `extractUsername()` method

### "Session doesn't expire after 15 seconds"
- Ensure you're **not making any requests** during the 15 seconds
- Each request resets the timer
- Check `TokenBlacklistService` logs

### "After logout, token still works"
- Restart the application (blacklist is in-memory)
- Check if logout endpoint actually called `blacklistToken()`
- Verify `JwtAuthenticationFilter` checks blacklist before validation

---

## Next Steps

1. **Test thoroughly** with Postman/Insomnia
2. **Integrate** with frontend application
3. **Monitor** server logs for authentication issues
4. **Adjust** session timeout as needed
5. **Consider** Redis for production (distributed blacklist)
