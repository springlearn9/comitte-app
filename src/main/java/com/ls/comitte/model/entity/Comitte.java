package com.ls.comitte.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "comittes")
@Data
@NoArgsConstructor
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
    
    @Transient
    private Integer bidsCount;
    
    @CreatedDate
    private LocalDateTime createdTimestamp;
    @LastModifiedDate
    private LocalDateTime updatedTimestamp;

    // All args constructor (needed for Lombok @Builder and JPQL)
    public Comitte(Long comitteId, Member owner, String comitteName, LocalDate startDate, 
                   Integer fullAmount, Integer membersCount, Integer fullShare, 
                   Integer dueDateDays, Integer paymentDateDays, Integer bidsCount,
                   LocalDateTime createdTimestamp, LocalDateTime updatedTimestamp) {
        this.comitteId = comitteId;
        this.owner = owner;
        this.comitteName = comitteName;
        this.startDate = startDate;
        this.fullAmount = fullAmount;
        this.membersCount = membersCount;
        this.fullShare = fullShare;
        this.dueDateDays = dueDateDays;
        this.paymentDateDays = paymentDateDays;
        this.bidsCount = bidsCount;
        this.createdTimestamp = createdTimestamp;
        this.updatedTimestamp = updatedTimestamp;
    }
    
}
