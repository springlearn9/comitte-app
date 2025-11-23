package com.ls.comitte.model.entity;

import com.ls.auth.model.entity.Member;
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comitte_id", referencedColumnName = "comitteId")
    private Comitte comitte;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "memberId")
    private Member member;
    
    private Integer shareCount;
    
    @CreatedDate
    private LocalDateTime createdTimestamp;
    
    @LastModifiedDate
    private LocalDateTime updatedTimestamp;
}
