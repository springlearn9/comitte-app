package com.ls.auth.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "authorities")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AUTHORITY_ID")
    private Long authorityId;

    @Column(name = "AUTHORITY_NAME", unique = true, nullable = false, length = 200)
    private String authorityName;

    @Column(name = "DETAILS", length = 500)
    private String details;
}
