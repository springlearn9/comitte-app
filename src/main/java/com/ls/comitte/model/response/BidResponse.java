package com.ls.comitte.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BidResponse(
        Long bidId,
        Long comitteId,
        String comitteName,
        Long ownerId,
        String ownerName,
        Integer comitteNumber,
        Long finalBidderId,
        String finalBidderName,
        Integer finalBidAmt,
        Integer monthlyShare,
        LocalDateTime bidDate,
        LocalDateTime createdTimestamp,
        LocalDateTime updatedTimestamp
) {}
