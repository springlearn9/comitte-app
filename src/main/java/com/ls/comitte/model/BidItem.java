package com.ls.comitte.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BidItem {
    private Long bidder;
    private Integer amount;
    private LocalDateTime timestamp;
}
