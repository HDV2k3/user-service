package com.user.identity.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role {

    @Id
    @Column(name = "role_name", nullable = false, unique = true)
    String name;

    @Column(name = "description")
    String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "role_permissions",  // Bảng liên kết giữa roles và permissions
            joinColumns = @JoinColumn(name = "role_id"),  // Khóa ngoại từ bảng role_permissions đến bảng roles
            inverseJoinColumns = @JoinColumn(name = "permission_id")  // Khóa ngoại từ bảng role_permissions đến bảng permissions
    )
    Set<Permission> permissions;
}
