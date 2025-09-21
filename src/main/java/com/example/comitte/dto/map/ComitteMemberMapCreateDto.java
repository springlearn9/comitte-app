package com.example.comitte.dto.map;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class ComitteMemberMapCreateDto {
    @NotNull
    private Long comitteId;
    @NotNull
    private Long memberId;
    private Integer shareCount;
}
