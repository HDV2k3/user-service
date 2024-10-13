package com.user.identity.entity;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.*;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "users") // Đảm bảo bảng 'users' trong cơ sở dữ liệu có tên đúng
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(name = "username", unique = true, nullable = false) // Đảm bảo cột này không null
    String username;

    @Column(nullable = false) // Đảm bảo mật khẩu không null
    String password;

    @Column(name = "first_name")
    String firstName;

    @Column(name = "last_name")
    String lastName;

    @Column(name = "dob")
    Instant dob;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles", // Tên bảng liên kết giữa users và roles
            joinColumns = @JoinColumn(name = "user_id"), // Khóa ngoại liên kết với user
            inverseJoinColumns = @JoinColumn(name = "role_id") // Khóa ngoại liên kết với role
            )
    Set<Role> roles;

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
