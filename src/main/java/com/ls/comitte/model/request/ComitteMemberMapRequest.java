package com.ls.comitte.model.request;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class ComitteMemberMapRequest {
    @NotNull
    private Long comitteId;
    @NotNull
    private Long memberId;
    private Integer shareCount;
}
