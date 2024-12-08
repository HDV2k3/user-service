package com.user.identity.repository;

import com.user.identity.repository.entity.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {
    Optional<UserSubscription> findByUserId(Integer userId);

    @Query("SELECT u FROM UserSubscription u WHERE u.isPremium = true AND u.subscriptionEndDate < :currentDate")
    List<UserSubscription> findByIsPremiumTrueAndSubscriptionEndDateBefore(Instant currentDate);
}