package com.user.identity.repository.client;

import com.user.identity.configuration.AuthenticationRequestInterceptor;
import com.user.identity.controller.dto.ApiResponse;
import com.user.identity.repository.client.model.UserPaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "payment",url = "${service.url.payment}", configuration = {AuthenticationRequestInterceptor.class})
public interface PaymentClient {
    @GetMapping(value = "/userPayment/create/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<UserPaymentResponse> createPayment(@PathVariable("userId") int userId);
}
