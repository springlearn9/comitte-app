package com.ls.comitte.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ComitteRequest {
    @NotNull
    private Long ownerId;
    @NotBlank
    private String comitteName;
    private LocalDate startDate;
    private Integer fullAmount;
    private Integer membersCount;
    private Integer fullShare;
    private Integer dueDateDays;
    private Integer paymentDateDays;
}
