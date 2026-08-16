package com.ls.construction.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetailsRequest {
    @NotNull
    private Long projectId;
    
    @NotNull
    private LocalDate paymentDate;
    
    private String details;
    private String paymentType;
    private String receiverDetails;
    private String tags;
}
