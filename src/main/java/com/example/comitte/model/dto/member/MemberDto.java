package com.example.comitte.model.dto.member;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class MemberDto {
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
