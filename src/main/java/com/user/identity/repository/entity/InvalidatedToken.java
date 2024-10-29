package com.user.identity.repository.entity;

import java.time.Instant;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "invalidated_tokens")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvalidatedToken {

    @Id
    @Column(name = "token_id", nullable = false, unique = true)
    String id;

    @Column(name = "expiry_time", nullable = false)
    Instant expiryTime;
}
