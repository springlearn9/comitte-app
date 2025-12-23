package com.ls.comitte.model.entity;

import com.ls.auth.model.entity.Member;
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
    @JoinColumn(name = "OWNER_ID", referencedColumnName = "memberId", nullable = false)
    private Member owner;

    private String comitteName;
    private LocalDate startDate;
    private Integer fullAmount;
    
    @Column(name = "TOTAL_SHARES")
    private Integer totalShares;
    
    private Integer fullShare;
    private Integer dueDateDays;
    private Integer paymentDateDays;
    
    @Transient
    private Integer bidsCount;
    
    @Transient
    private Integer associatedSharesCount;
    
    @Transient
    private Integer associatedMembersCount;
    
    @CreatedDate
    private LocalDateTime createdTimestamp;
    @LastModifiedDate
    private LocalDateTime updatedTimestamp;

    // All args constructor (needed for Lombok @Builder and JPQL)
    public Comitte(Long comitteId, Member owner, String comitteName, LocalDate startDate, 
                   Integer fullAmount, Integer totalShares, Integer fullShare, 
                   Integer dueDateDays, Integer paymentDateDays, Integer bidsCount,
                   Integer associatedSharesCount, Integer associatedMembersCount,
                   LocalDateTime createdTimestamp, LocalDateTime updatedTimestamp) {
        this.comitteId = comitteId;
        this.owner = owner;
        this.comitteName = comitteName;
        this.startDate = startDate;
        this.fullAmount = fullAmount;
        this.totalShares = totalShares;
        this.fullShare = fullShare;
        this.dueDateDays = dueDateDays;
        this.paymentDateDays = paymentDateDays;
        this.bidsCount = bidsCount;
        this.associatedSharesCount = associatedSharesCount;
        this.associatedMembersCount = associatedMembersCount;
        this.createdTimestamp = createdTimestamp;
        this.updatedTimestamp = updatedTimestamp;
    }
    
}
