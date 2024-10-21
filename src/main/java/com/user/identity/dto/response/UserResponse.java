package com.user.identity.dto.response;

import java.time.Instant;
import java.util.Set;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    int id;
    String username;
    String firstName;
    String lastName;
    Instant dob;
    Set<RoleResponse> roles;
}
