package com.user.identity.controller.dto.request;

import com.user.identity.constant.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpgradeSubscriptionRequest {
    @NotNull(message = "Payment amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Payment amount must be greater than 0")
    private BigDecimal paymentAmount;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    // Có thể thêm các thông tin thanh toán khác nếu cần
    private String paymentReference;
}