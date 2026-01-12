package com.ls.comitte.model.entity;

import com.ls.auth.model.entity.Member;
import com.ls.common.model.AuditMetadata;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bids")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bidId;
    
    @ManyToOne
    @JoinColumn(name = "COMITTE_ID", referencedColumnName = "comitteId")
    private Comitte comitte;
    
    private Integer comitteNumber;
    
    @ManyToOne
    @JoinColumn(name = "FINAL_BIDDER", referencedColumnName = "memberId")
    private Member finalBidder;
    
    private Integer finalBidAmt;
    private LocalDateTime bidDate;

    @Transient
    private Integer monthlyShare;

    @Embedded
    private AuditMetadata audit;
}
