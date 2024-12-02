package com.user.identity.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "user_subscriptions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserSubscription extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id; // ID của bảng user_subscriptions (khóa chính)

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user; // Tham chiếu đến bảng User (khóa ngoại)

    @Column(name = "remaining_free_posts", nullable = false)
    Integer remainingFreePosts; // Số bài viết miễn phí còn lại

    @Column(name = "is_premium", nullable = false)
    Boolean isPremium; // Trạng thái tài khoản premium (true: premium, false: không)

    @Column(name = "subscription_start_date")
    Instant subscriptionStartDate; // Ngày bắt đầu của gói đăng ký

    @Column(name = "subscription_end_date")
    Instant subscriptionEndDate; // Ngày kết thúc của gói đăng ký

    @Column(name = "subscription_fee")
    BigDecimal subscriptionFee; // Phí của gói đăng ký

    @PrePersist
    public void prePersist() {
        Instant now = Instant.now();
        this.setCreatedDate(now); // Thiết lập ngày tạo
        this.setLastModifiedDate(now); // Thiết lập ngày cập nhật cuối
        this.setCreatedBy("System"); // Thiết lập người tạo mặc định là "System"
        this.setLastModifiedBy("System"); // Thiết lập người cập nhật mặc định là "System"
        this.remainingFreePosts = 5; // Mặc định 5 bài viết miễn phí khi tạo mới
        this.isPremium = false; // Mặc định không phải tài khoản premium
    }

    @PreUpdate
    public void preUpdate() {
        this.setLastModifiedDate(Instant.now()); // Cập nhật ngày cập nhật cuối
        this.setLastModifiedBy("System"); // Cập nhật người cập nhật là "System"
    }
}
