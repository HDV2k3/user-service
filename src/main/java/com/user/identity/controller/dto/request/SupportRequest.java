package com.user.identity.controller.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SupportRequest {
    String fullName;
    String email;
    String subject;
    String content;
}
