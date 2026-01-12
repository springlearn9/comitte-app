package com.ls.common.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

/**
 * Embeddable class containing audit metadata fields for tracking entity creation and modification.
 * Can be embedded in any entity using @Embedded annotation.
 * 
 * <p>Requires JPA auditing to be enabled with @EnableJpaAuditing in the main application class.</p>
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditMetadata {

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private Long createdBy;

    @CreatedDate
    @Column(name = "created_timestamp", updatable = false)
    private LocalDateTime createdTimestamp;

    @LastModifiedBy
    @Column(name = "updated_by")
    private Long updatedBy;

    @LastModifiedDate
    @Column(name = "updated_timestamp")
    private LocalDateTime updatedTimestamp;
}
