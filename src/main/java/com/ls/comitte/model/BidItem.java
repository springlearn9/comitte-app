package com.ls.comitte.model;

import jakarta.persistence.Embeddable;

import java.time.LocalDateTime;

@Embeddable
public record BidItem(
        Long bidder,
        Integer amount,
        LocalDateTime timestamp
) {}
