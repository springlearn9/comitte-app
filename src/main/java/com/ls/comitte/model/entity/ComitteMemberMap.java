package com.ls.comitte.model.entity;

import com.ls.auth.model.entity.Member;
import com.ls.common.model.AuditMetadata;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "comitte_member_map")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComitteMemberMap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMITTE_ID", referencedColumnName = "comitteId")
    private Comitte comitte;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", referencedColumnName = "memberId")
    private Member member;
    
    private Integer shareCount;
    
    @Embedded
    private AuditMetadata audit;
}
