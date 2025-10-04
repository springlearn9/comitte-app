package com.ls.comitte.model.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ComitteResponse {
    private Long comitteId;
    private Long ownerId;
    private String comitteName;
    private LocalDate startDate;
    private Integer fullAmount;
    private Integer membersCount;
    private Integer fullShare;
    private Integer dueDateDays;
    private Integer paymentDateDays;
    private LocalDateTime createdTimestamp;
    private LocalDateTime updatedTimestamp;
}
