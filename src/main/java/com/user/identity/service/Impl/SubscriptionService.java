package com.user.identity.service.Impl;

import com.user.identity.controller.dto.response.SubscriptionResponse;
import com.user.identity.exception.AppException;
import com.user.identity.exception.ErrorCode;
import com.user.identity.repository.UserRepository;
import com.user.identity.repository.UserSubscriptionRepository;
import com.user.identity.repository.entity.UserSubscription;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SubscriptionService {

    private final UserSubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    private static final int SUBSCRIPTION_DURATION_MONTHS = 1; // Thời hạn subscription 1 tháng

    /**
     * Kiểm tra xem user có thể đăng bài không
     */
    public boolean canCreatePost(Integer userId) {
        UserSubscription subscription = subscriptionRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.SUBSCRIPTION_NOT_FOUND));

        return subscription.getIsPremium() || subscription.getRemainingFreePosts() > 0;
    }

    /**
     * Giảm số lượt đăng bài free
     */
    @Transactional
    public void decrementFreePost(Integer userId) {
        UserSubscription subscription = subscriptionRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.SUBSCRIPTION_NOT_FOUND));

        if (!subscription.getIsPremium() && subscription.getRemainingFreePosts() > 0) {
            subscription.setRemainingFreePosts(subscription.getRemainingFreePosts() - 1);
            subscriptionRepository.save(subscription);
        }
    }

    /**
     * Nâng cấp lên tài khoản premium
     * POST /api/v1/subscriptions/upgrade
     * Header: userId: 123
     * Body:
     * {
     *     "paymentAmount": 100000,
     *     "paymentMethod": "MOMO",
     *     "paymentReference": "MOMO123456789"
     * }
     */
    private final BigDecimal MINIMUM_PREMIUM_AMOUNT = new BigDecimal("100000"); // 100,000 VND

    @Transactional
    public void upgradeToPremium(Integer userId, BigDecimal paymentAmount) {
        // Kiểm tra số tiền thanh toán
      try
      {
          log.debug("Attempting to upgrade user {} to premium", userId);
          if (paymentAmount.compareTo(MINIMUM_PREMIUM_AMOUNT) < 0) {
              throw new AppException(ErrorCode.INVALID_PAYMENT_AMOUNT);
          }

          UserSubscription subscription = subscriptionRepository.findByUserId(userId)
                  .orElseThrow(() -> {
                      log.error("No subscription found for user {}", userId);
                      return new AppException(ErrorCode.SUBSCRIPTION_NOT_FOUND);
                  });
          log.info("Found subscription: {}", subscription);

          // Kiểm tra xem user đã là premium chưa
          if (subscription.getIsPremium()) {
              throw new AppException(ErrorCode.ALREADY_PREMIUM);
          }

          try {
              // Xử lý thanh toán ở đây (có thể gọi payment service)
              // processPayment(paymentAmount, request.getPaymentMethod());

              // Cập nhật trạng thái premium
              Instant now = Instant.now();
              subscription.setIsPremium(true);
              subscription.setSubscriptionStartDate(now);
              subscription.setSubscriptionEndDate(now.plus(SUBSCRIPTION_DURATION_MONTHS, ChronoUnit.MONTHS));
              subscription.setSubscriptionFee(paymentAmount);

              subscriptionRepository.save(subscription);
              log.info("User {} upgraded to premium successfully", userId);
          } catch (Exception e) {
              log.error("Error upgrading subscription for user {}: {}", userId, e.getMessage());
              throw new AppException(ErrorCode.SUBSCRIPTION_UPGRADE_FAILED);
          }
      }catch (PersistenceException e)
      {
          log.error("Database error during premium upgrade", e);
          throw new AppException(ErrorCode.SUBSCRIPTION_UPGRADE_FAILED);
      }
    }

    /**
     * Gia hạn tài khoản premium
     */
    @Transactional
    public void renewPremium(Integer userId, BigDecimal paymentAmount) {
        UserSubscription subscription = subscriptionRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.SUBSCRIPTION_NOT_FOUND));

        if (!subscription.getIsPremium()) {
            throw new AppException(ErrorCode.NOT_PREMIUM_USER);
        }

        // Gia hạn thêm 1 tháng từ ngày hết hạn hiện tại
        Instant newEndDate = subscription.getSubscriptionEndDate()
                .plus(SUBSCRIPTION_DURATION_MONTHS, ChronoUnit.MONTHS);
        subscription.setSubscriptionEndDate(newEndDate);
        subscription.setSubscriptionFee(paymentAmount);

        subscriptionRepository.save(subscription);
        log.info("Premium subscription renewed for user {} until {}", userId, newEndDate);
    }

    /**
     * Kiểm tra và cập nhật trạng thái premium (chạy theo schedule)
     */
    @Scheduled(cron = "0 0 0 * * *") // Chạy hàng ngày lúc 00:00
    @Transactional
    public void checkAndUpdatePremiumStatus() {
        Instant now = Instant.now();
        List<UserSubscription> expiredSubscriptions = subscriptionRepository
                .findByIsPremiumTrueAndSubscriptionEndDateBefore(now);

        for (UserSubscription subscription : expiredSubscriptions) {
            subscription.setIsPremium(false);
            subscriptionRepository.save(subscription);
            log.info("Premium status expired for user {}", subscription.getUser().getId());
        }
    }

    /**
     * Lấy thông tin subscription của user
     */
    public SubscriptionResponse getSubscriptionInfo(Integer userId) {
        UserSubscription subscription = subscriptionRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.SUBSCRIPTION_NOT_FOUND));

        return SubscriptionResponse.builder()
                .remainingFreePosts(subscription.getRemainingFreePosts())
                .isPremium(subscription.getIsPremium())
                .subscriptionStartDate(subscription.getSubscriptionStartDate())
                .subscriptionEndDate(subscription.getSubscriptionEndDate())
                .build();
    }

}