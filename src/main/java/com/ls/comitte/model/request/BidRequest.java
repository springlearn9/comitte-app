package com.ls.comitte.model.request;

import lombok.Data;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BidRequest {
    @NotNull
    private Long comitteId;
    // comitteNumber is auto-calculated based on existing bids for the committee\n    // First bid = 1, second bid = 2, etc. (per committee)
    private Long finalBidder;
    private Integer finalBidAmt;
    private LocalDateTime bidDate;
    private List<Long> receiversList;
}
