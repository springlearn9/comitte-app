package com.example.comitte.model.dto.comitte;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ComitteDto {
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
