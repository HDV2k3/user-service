package com.user.identity.entity;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "email", unique = true, nullable = false)
    @Email
    String email;

    @Column(nullable = false)
    String password;

    @Column(name = "first_name")
    String firstName;

    @Column(name = "last_name")
    String lastName;

    @Column(name = "day_of_birth",nullable = false)
    LocalDate dayOfBirth;
    String verificationToken;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles", // Tên bảng liên kết giữa users và roles
            joinColumns = @JoinColumn(name = "user_id"), // Khóa ngoại liên kết với user
            inverseJoinColumns = @JoinColumn(name = "role_id") // Khóa ngoại liên kết với role
            )
    Set<Role> roles;
    @Column(nullable = false)
    boolean enabled;
    @PrePersist
    public void prePersist() {
        Instant now = Instant.now();
        this.setCreatedDate(now);
        this.setLastModifiedDate(now);
        this.setCreatedBy("System");
        this.setLastModifiedBy("System");
    }

    @PreUpdate
    public void preUpdate() {
        this.setLastModifiedDate(Instant.now());
        this.setLastModifiedBy("System");
    }
}
