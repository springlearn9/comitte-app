package com.example.comitte.dto.bid;

import lombok.Data;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ComitteBidCreateDto {
    @NotNull
    private Long comitteId;
    private Integer comitteNumber;
    private Long finalBidder;
    private BigDecimal finalBidAmt;
    private LocalDateTime bidDate;
    private List<BidItemDto> bids;
    private List<Long> receiversList;
}
