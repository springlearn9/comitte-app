package com.ls.comitte.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ComitteResponse(
        Long comitteId,
        Long ownerId,
        String ownerName,
        String comitteName,
        LocalDate startDate,
        Integer fullAmount,
        Integer membersCount,
        Integer fullShare,
        Integer dueDateDays,
        Integer paymentDateDays,
        Integer bidsCount,
        LocalDateTime createdTimestamp,
        LocalDateTime updatedTimestamp
) {
    // Method to calculate the custom formatted string
    public String getCalculatedComitteName() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMMyy");
        return String.format("%.2fL/ %dM/ %d/ %s",
                fullAmount / 100000.0,
                membersCount,
                fullShare,
                startDate.format(formatter));
    }

    // Method to display bids count ratio as "bidsCount/membersCount"
    public String getBidsRatio() {
        return String.format("%d/%d", 
                bidsCount != null ? bidsCount : 0, 
                membersCount != null ? membersCount : 0);
    }

}
