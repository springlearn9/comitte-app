package com.ls.comitte.model.request;

import lombok.Data;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BidRequest {
    @NotNull
    private Long comitteId;
    private Integer comitteNumber;
    private Long finalBidder;
    private Integer finalBidAmt;
    private LocalDateTime bidDate;
    private List<Long> receiversList;
}
