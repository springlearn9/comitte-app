package com.example.comitte.dto.bid;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ComitteBidDto {
    private Long id;
    private Long comitteId;
    private Integer comitteNumber;
    private Long finalBidder;
    private BigDecimal finalBidAmt;
    private LocalDateTime bidDate;
    private List<BidItemDto> bids;
    private List<Long> receiversList;
    private LocalDateTime createdTimestamp;
    private LocalDateTime updatedTimestamp;
}
