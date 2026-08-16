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
@Table(name = "labour_details")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabourDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "projectId", nullable = false)
    private Project project;
    
    @Column(nullable = false)
    private LocalDate labourDate;
    
    private String labourType;
    
    private BigDecimal labourAmount;
    
    @Column(columnDefinition = "TEXT")
    private String details;
    
    private String tags;
    
    @Embedded
    private AuditMetadata audit;
}
