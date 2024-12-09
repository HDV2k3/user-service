package com.user.identity.event.kafka;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationEvent {
    String chanel;
    String recipient;
    String templateCode;
    Map<String, Objects> param;
    String subject;
    String body;

}
