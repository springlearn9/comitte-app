package com.ls.construction.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PaymentDetailsResponse(
        Long paymentId,
        Long projectId,
        String projectName,
        LocalDate paymentDate,
        String details,
        String paymentType,
        String receiverDetails,
        String tags,
        LocalDateTime createdTimestamp,
        LocalDateTime updatedTimestamp
) {
}
