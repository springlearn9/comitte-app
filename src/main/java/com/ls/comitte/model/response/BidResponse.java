package com.ls.comitte.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ls.comitte.model.BidItem;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BidResponse(
        Long bidId,
        Long comitteId,
        String comitteName,
        Integer comitteNumber,
        Long finalBidderId,
        String finalBidderName,
        Integer finalBidAmt,
        LocalDateTime bidDate,
        List<BidItem> bidItems,
        List<Long> receiversList,
        LocalDateTime createdTimestamp,
        LocalDateTime updatedTimestamp
) {}
