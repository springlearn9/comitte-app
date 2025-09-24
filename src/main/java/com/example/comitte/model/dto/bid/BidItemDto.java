package com.example.comitte.model.dto.bid;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BidItemDto {
    private Long bidder;
    private Integer amount;
    private LocalDateTime timestamp;
}
