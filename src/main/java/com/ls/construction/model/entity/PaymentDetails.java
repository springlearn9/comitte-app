package com.ls.construction.model.entity;

import com.ls.common.model.AuditMetadata;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Table(name = "payment_details")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;
    
    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "projectId", nullable = false)
    private Project project;
    
    @Column(nullable = false)
    private LocalDate paymentDate;
    
    @Column(columnDefinition = "TEXT")
    private String details;
    
    private String paymentType;
    
    @Column(columnDefinition = "TEXT")
    private String receiverDetails;
    
    private String tags;
    
    @Embedded
    private AuditMetadata audit;
}
