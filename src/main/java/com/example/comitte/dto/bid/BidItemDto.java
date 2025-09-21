package com.example.comitte.dto.bid;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BidItemDto {
    private Long bidder;
    private java.math.BigDecimal amount;
    private LocalDateTime timestamp;
}
