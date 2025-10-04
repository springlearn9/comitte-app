package com.ls.comitte.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

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
    private Long comitteId;
    private Integer comitteNumber;
    private Long finalBidder;
    private Integer finalBidAmt;
    private LocalDateTime bidDate;
    @Column(columnDefinition = "jsonb")
    private String bids;
    @Column(columnDefinition = "jsonb")
    private String receiversList;
    @CreatedDate
    private LocalDateTime createdTimestamp;
    @LastModifiedDate
    private LocalDateTime updatedTimestamp;
}
