package com.ls.comitte.model.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class MemberResponse {
    private Long memberId;
    private String username;
    private String email;
    private String name;
    private String mobile;
    private String aadharNo;
    private String address;
    private Set<String> roles;
    private LocalDateTime createdTimestamp;
    private LocalDateTime updatedTimestamp;
}
