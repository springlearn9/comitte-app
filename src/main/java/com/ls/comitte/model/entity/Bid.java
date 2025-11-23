package com.ls.comitte.model.entity;

import com.ls.auth.model.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bids")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bidId;
    
    @ManyToOne
    @JoinColumn(name = "comitteId", referencedColumnName = "comitteId")
    private Comitte comitte;
    
    private Integer comitteNumber;
    
    @ManyToOne
    @JoinColumn(name = "finalBidder", referencedColumnName = "memberId")
    private Member finalBidder;
    
    private Integer finalBidAmt;
    private LocalDateTime bidDate;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(columnDefinition = "jsonb")
    private List<Long> receiversList;

    @Transient
    private Integer monthlyShare;

    @CreatedDate
    private LocalDateTime createdTimestamp;

    @LastModifiedDate
    private LocalDateTime updatedTimestamp;
}
