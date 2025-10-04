package com.ls.comitte.model.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ComitteMemberMapResponse {
    private Long id;
    private Long comitteId;
    private Long memberId;
    private Integer shareCount;
    private LocalDateTime createdTimestamp;
    private LocalDateTime updatedTimestamp;
}
