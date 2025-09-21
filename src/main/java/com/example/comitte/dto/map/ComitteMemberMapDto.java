package com.example.comitte.dto.map;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ComitteMemberMapDto {
    private Long id;
    private Long comitteId;
    private Long memberId;
    private Integer shareCount;
    private LocalDateTime createdTimestamp;
    private LocalDateTime updatedTimestamp;
}
