package com.user.identity.repository.entity;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "permissions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Permission {

    @Id
    @Column(name = "permission_name", nullable = false, unique = true)
    String name;

    @Column(name = "description")
    String description;
}
