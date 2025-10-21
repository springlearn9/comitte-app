package com.ls.comitte.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "comittes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comitte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long comitteId;

    @ManyToOne
    @JoinColumn(name = "ownerId", referencedColumnName = "memberId", nullable = false)
    private Member owner;

    private String comitteName;
    private LocalDate startDate;
    private Integer fullAmount;
    private Integer membersCount;
    private Integer fullShare;
    private Integer dueDateDays;
    private Integer paymentDateDays;
    @CreatedDate
    private LocalDateTime createdTimestamp;
    @LastModifiedDate
    private LocalDateTime updatedTimestamp;
}
