package com.ls.comitte.model.response;

import com.ls.comitte.model.BidItem;

import java.time.LocalDateTime;
import java.util.List;

public record BidResponse(
        Long bidId,
        Long comitteId,
        Integer comitteNumber,
        Long finalBidder,
        Integer finalBidAmt,
        LocalDateTime bidDate,
        List<BidItem> bidItems,
        List<Long> receiversList,
        LocalDateTime createdTimestamp,
        LocalDateTime updatedTimestamp
) {}
