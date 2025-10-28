package com.ls.comitte.model.entity;

import com.ls.auth.model.entity.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "members")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false, unique = true)
    private String email;

    private String mobile;
    private String password;
    private String name;
    private String aadharNo;
    private String address;
    private LocalDate dob;

    @CreatedDate
    @Column(name = "created_timestamp", updatable = false)
    private LocalDateTime createdTimestamp;

    @LastModifiedDate
    @Column(name = "updated_timestamp")
    private LocalDateTime updatedTimestamp;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role_map",
            joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

}
