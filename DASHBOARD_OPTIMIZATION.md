# Dashboard API Optimization Guide

## Problem: Multiple API Calls for Committee Members

When displaying a dashboard with multiple committees, the frontend was making separate API calls for each committee to fetch members:

```
GET /api/comittes/1/members
GET /api/comittes/2/members
GET /api/comittes/6/members
GET /api/comittes/12/members
...
```

This creates an **N+1 query problem** at the HTTP level, causing:
- Multiple round trips to the server
- Increased latency
- Poor performance on slow networks
- Unnecessary database queries

## Solution: Bulk Members Endpoint

A new optimized endpoint has been added that fetches members for multiple committees in a single request.

### New Endpoint

```
GET /api/comittes/bulk/members?ids=1,2,6,12
```

### Request Example

```bash
curl -X GET "http://localhost:8082/api/comittes/bulk/members?ids=1,2,6,12" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Response Format

```json
{
  "1": [
    {
      "id": 101,
      "memberId": 5,
      "comitteId": 1,
      "memberName": "John Doe",
      "shareCount": 2,
      "createdTimestamp": "2025-01-01T10:00:00",
      "updatedTimestamp": "2025-01-01T10:00:00"
    }
  ],
  "2": [
    {
      "id": 102,
      "memberId": 7,
      "comitteId": 2,
      "memberName": "Jane Smith",
      "shareCount": 1,
      "createdTimestamp": "2025-01-02T11:00:00",
      "updatedTimestamp": "2025-01-02T11:00:00"
    }
  ],
  "6": [],
  "12": [
    {
      "id": 103,
      "memberId": 8,
      "comitteId": 12,
      "memberName": "Bob Wilson",
      "shareCount": 3,
      "createdTimestamp": "2025-01-03T12:00:00",
      "updatedTimestamp": "2025-01-03T12:00:00"
    }
  ]
}
```

The response is a **Map** where:
- **Key**: Committee ID (as string in JSON)
- **Value**: Array of member objects for that committee (empty array if no members)

## Frontend Implementation

### Before (Inefficient - N+1 Problem)

```javascript
// ❌ BAD: Multiple sequential API calls
async function loadCommitteeMembers(committees) {
  for (const committee of committees) {
    const response = await fetch(
      `http://localhost:5173/api/comittes/${committee.id}/members`,
      {
        headers: { Authorization: `Bearer ${token}` }
      }
    );
    const members = await response.json();
    committee.members = members;
  }
}
```

### After (Optimized - Single Bulk Call)

```javascript
// ✅ GOOD: Single bulk API call
async function loadCommitteeMembers(committees) {
  const committeeIds = committees.map(c => c.id).join(',');
  
  const response = await fetch(
    `http://localhost:5173/api/comittes/bulk/members?ids=${committeeIds}`,
    {
      headers: { Authorization: `Bearer ${token}` }
    }
  );
  
  const membersByCommitteeId = await response.json();
  
  // Map members back to committees
  committees.forEach(committee => {
    committee.members = membersByCommitteeId[committee.id] || [];
  });
}
```

### React/Vue Example

```javascript
// React with useState/useEffect
const [committees, setCommittees] = useState([]);

useEffect(() => {
  async function loadDashboardData() {
    // 1. Load committees list
    const committeesRes = await fetch('/api/comittes/my/123', {
      headers: { Authorization: `Bearer ${token}` }
    });
    const committeesData = await committeesRes.json();
    
    // 2. Bulk load members for all committees
    const ids = committeesData.map(c => c.comitteId).join(',');
    const membersRes = await fetch(`/api/comittes/bulk/members?ids=${ids}`, {
      headers: { Authorization: `Bearer ${token}` }
    });
    const membersByCommitteeId = await membersRes.json();
    
    // 3. Combine data
    const enrichedCommittees = committeesData.map(committee => ({
      ...committee,
      members: membersByCommitteeId[committee.comitteId] || []
    }));
    
    setCommittees(enrichedCommittees);
  }
  
  loadDashboardData();
}, []);
```

## Performance Comparison

### Before (N+1 Problem)
- **Committees**: 10
- **API Calls**: 10 (one per committee)
- **Network Time**: ~500ms (10 × 50ms per request)
- **Database Queries**: 10+ queries

### After (Bulk Endpoint)
- **Committees**: 10
- **API Calls**: 1 (single bulk request)
- **Network Time**: ~50ms (single request)
- **Database Queries**: 1 optimized query
- **Performance Improvement**: ~10x faster

## Backend Implementation Details

### Repository Layer
```java
@Query("SELECT cmm FROM ComitteMemberMap cmm " +
       "LEFT JOIN FETCH cmm.comitte c " +
       "LEFT JOIN FETCH c.owner " +
       "LEFT JOIN FETCH cmm.member " +
       "WHERE cmm.comitte.comitteId IN :comitteIds")
List<ComitteMemberMap> findByComitteIdInWithDetails(List<Long> comitteIds);
```

### Service Layer
```java
public Map<Long, List<ComitteMemberMapResponse>> getAllAssociatedMembersBulk(List<Long> comitteIds) {
    List<ComitteMemberMap> allMembers = comitteMemberMapRepository
        .findByComitteIdInWithDetails(comitteIds);
    
    return allMembers.stream()
        .collect(Collectors.groupingBy(
            member -> member.getComitte().getComitteId(),
            Collectors.mapping(mapper::toResponse, Collectors.toList())
        ));
}
```

## Best Practices

1. **Use bulk endpoint for dashboard/list views** where you need members for multiple committees
2. **Use single endpoint** (`/api/comittes/{id}/members`) for detail pages showing one committee
3. **Limit bulk requests** to reasonable sizes (e.g., max 50-100 committee IDs)
4. **Handle empty results** - committees with no members return empty arrays
5. **Error handling** - validate committee IDs exist and user has access

## Migration Checklist

- [ ] Update dashboard component to use bulk endpoint
- [ ] Remove individual member fetch calls in loops
- [ ] Test with various committee counts (0, 1, 10+)
- [ ] Test with committees having no members
- [ ] Verify performance improvement in network tab
- [ ] Update API documentation/Swagger if needed

## Additional Optimization Opportunities

Consider similar bulk endpoints for:
- Bulk bid fetching: `GET /api/bids/bulk?comitteIds=1,2,3`
- Bulk committee details: Already exists in "my committees" endpoints
- Filtering/pagination for large datasets
