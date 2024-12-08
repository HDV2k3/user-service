package com.user.identity.controller.dto.request;

import java.time.LocalDate;
import java.util.List;
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
    LocalDate dayOfBirth;

}
