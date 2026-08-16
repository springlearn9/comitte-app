package com.ls.construction.model.entity;

import com.ls.common.model.AuditMetadata;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "materials")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Material {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long materialId;
    
    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "projectId", nullable = false)
    private Project project;
    
    @Column(nullable = false)
    private LocalDate materialDate;
    
    @Column(columnDefinition = "TEXT")
    private String details;
    
    private String material;
    
    private String materialType;
    
    private String labour;
    
    private String labourType;
    
    private String supplier;
    
    private BigDecimal quantity;
    
    private String unit;
    
    private BigDecimal pricePerUnit;
    
    private BigDecimal amount;
    
    private BigDecimal bhada;
    
    private BigDecimal totalAmount;
    
    private String tags;
    
    private String paymentStatus;
    
    private LocalDate paidDate;
    
    private Long paymentId;
    
    @Column(columnDefinition = "TEXT")
    private String paymentDetails;
    
    @Embedded
    private AuditMetadata audit;
}
