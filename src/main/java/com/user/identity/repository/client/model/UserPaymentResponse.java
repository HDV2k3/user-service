package com.user.identity.repository.client.model;


import com.user.identity.controller.dto.response.UserResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserPaymentResponse {
    String id;
    UserResponse userResponse;
    Double balance;
    Instant createDate;
    Instant lastModifiedDate;
    String createdBy;
    String modifiedBy;
}
