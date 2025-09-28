package com.example.comitte.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "comitte_member_map")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComitteMemberMap {

    @EmbeddedId
    private ComitteMemberId id = new ComitteMemberId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("comitteId")
    @JoinColumn(name = "comitte_id")
    private Comitte comitte;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("memberId")
    @JoinColumn(name = "member_id")
    private Member member;

    private Integer shareCount;

    @CreatedDate
    private LocalDateTime createdTimestamp;

    @LastModifiedDate
    private LocalDateTime updatedTimestamp;
}
