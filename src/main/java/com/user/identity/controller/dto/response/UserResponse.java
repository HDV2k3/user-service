package com.user.identity.controller.dto.response;

import java.time.LocalDate;
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
    String email;
    String firstName;
    String lastName;
    LocalDate dayOfBirth;
    String avatar;
    String verificationToken;
    boolean enabled;
    Set<RoleResponse> roles;
}
