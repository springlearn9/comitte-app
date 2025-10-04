package com.ls.comitte.model.response;

import com.ls.comitte.model.BidItem;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BidResponse {
    private Long id;
    private Long comitteId;
    private Integer comitteNumber;
    private Long finalBidder;
    private Integer finalBidAmt;
    private LocalDateTime bidDate;
    private List<BidItem> bidItems;
    private List<Long> receiversList;
    private LocalDateTime createdTimestamp;
    private LocalDateTime updatedTimestamp;
}
