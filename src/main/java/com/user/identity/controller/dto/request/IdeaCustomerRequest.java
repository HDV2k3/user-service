package com.user.identity.controller.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IdeaCustomerRequest {
    String title;
    String category;
    String description;
}
