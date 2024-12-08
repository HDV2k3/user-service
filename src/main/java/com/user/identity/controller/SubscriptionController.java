package com.user.identity.controller;

import com.user.identity.controller.dto.ApiResponse;
import com.user.identity.controller.dto.request.UpgradeSubscriptionRequest;
import com.user.identity.controller.dto.response.SubscriptionResponse;
import com.user.identity.service.Impl.SubscriptionService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @GetMapping("/check-post-permission")
    public ApiResponse<Boolean> canCreatePost(@RequestHeader("userId") Integer userId) {
        return ApiResponse.success(subscriptionService.canCreatePost(userId));
    }

    @PostMapping("/decrement-free-post")
    public ApiResponse<String> decrementFreePost(@RequestHeader("userId") Integer userId) {
        subscriptionService.decrementFreePost(userId);
        return ApiResponse.success("Success");
    }
    @PostMapping("/upgrade")
    public ApiResponse<SubscriptionResponse> upgradeToPremium(
            @RequestBody UpgradeSubscriptionRequest request,
            @RequestHeader("userId") Integer userId) {
        subscriptionService.upgradeToPremium(userId, request.getPaymentAmount());
        return ApiResponse.success(subscriptionService.getSubscriptionInfo(userId));
    }

    @PostMapping("/renew")
    public ApiResponse<SubscriptionResponse> renewPremium(
            @RequestBody UpgradeSubscriptionRequest request,
            @RequestHeader("userId") Integer userId) {
        subscriptionService.renewPremium(userId, request.getPaymentAmount());
        return ApiResponse.success(subscriptionService.getSubscriptionInfo(userId));
    }
}
