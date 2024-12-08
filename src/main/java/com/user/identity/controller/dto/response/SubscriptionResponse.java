package com.user.identity.controller.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubscriptionResponse {
     Integer remainingFreePosts;
     Boolean isPremium;
     Instant subscriptionStartDate;
     Instant subscriptionEndDate;
}
