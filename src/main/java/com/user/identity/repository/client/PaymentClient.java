package com.user.identity.repository.client;

import com.user.identity.configuration.AuthenticationRequestInterceptor;
import com.user.identity.controller.dto.ApiResponse;
import com.user.identity.repository.client.model.UserPaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "payment",url = "${service.url.payment}", configuration = {AuthenticationRequestInterceptor.class})
public interface PaymentClient {
    @PostMapping(value = "/userPayment/create", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<UserPaymentResponse> createPayment(@RequestParam("userId") int userId);
}
