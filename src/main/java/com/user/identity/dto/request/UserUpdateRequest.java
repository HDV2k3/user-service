package com.user.identity.dto.request;

import java.time.Instant;
import java.util.List;

import com.user.identity.validator.DobConstraint;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    Integer  id;
    String password;
    String firstName;
    String lastName;

    @DobConstraint(min = 18, message = "INVALID_DOB")
    Instant dateOfBirth;

    List<String> roles;
}
