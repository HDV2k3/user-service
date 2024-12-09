package com.user.identity.repository;

import com.user.identity.controller.dto.ApiResponse;
import com.user.identity.repository.client.PaymentClient;
import com.user.identity.repository.client.model.UserPaymentResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserPaymentRepository {
    PaymentClient paymentClient;
    public UserPaymentResponse createPayment(int userId) {
        UserPaymentResponse result = null;
        ApiResponse<UserPaymentResponse> clientResponse = paymentClient.createPayment(userId);
        if (ObjectUtils.isNotEmpty(clientResponse)) {
            result = clientResponse.getData();
        }
        return result;
    }
}
