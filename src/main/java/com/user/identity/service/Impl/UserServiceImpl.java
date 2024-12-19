package com.user.identity.service.Impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.user.identity.constant.BucketConstants;
import com.user.identity.controller.dto.request.IdeaCustomerRequest;
import com.user.identity.controller.dto.request.SupportRequest;
import com.user.identity.controller.dto.response.InfoUserForCount;
import com.user.identity.event.OnRegistrationCompleteEvent;
import com.user.identity.repository.UserSubscriptionRepository;
import com.user.identity.repository.client.PaymentClient;
import com.user.identity.repository.entity.UserSubscription;
import com.user.identity.service.EmailNotificationKafka;
import com.user.identity.service.VerificationTokenService;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.user.identity.constant.PredefinedRole;
import com.user.identity.controller.dto.request.UserCreationRequest;
import com.user.identity.controller.dto.request.UserUpdateRequest;
import com.user.identity.controller.dto.response.UserResponse;
import com.user.identity.repository.entity.Role;
import com.user.identity.repository.entity.User;
import com.user.identity.exception.AppException;
import com.user.identity.exception.ErrorCode;
import com.user.identity.mapper.UserMapper;
import com.user.identity.repository.RoleRepository;
import com.user.identity.repository.UserRepository;
import com.user.identity.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    FirebaseStorageClient firebaseStorageClient;
    PaymentClient paymentClient;
//    ApplicationEventPublisher eventPublisher;
//    UserSubscriptionRepository userSubscriptionRepository;
    EmailNotificationKafka emailNotificationKafka;
    VerificationTokenService tokenService;
    public void validateUserCreationRequest(UserCreationRequest request) {
        if (request.getEmail() == null) {
            throw new AppException(ErrorCode.EMAIL_NULL);
        }
        if (request.getPassword() == null) {
            throw new AppException(ErrorCode.PASSWORD_NULL);
        }
        if (request.getFirstName() == null) {
            throw new AppException(ErrorCode.FIRST_NAME_NULL);
        }
        if (request.getLastName() == null) {
            throw new AppException(ErrorCode.LAST_NAME_NULL);
        }
        if (request.getDayOfBirth() == null) {
            throw new AppException(ErrorCode.DOB_NULL);
        }
    }
    @Override
    @Transactional
    public UserResponse createUser(UserCreationRequest request) {
        // Validate the request
        validateUserCreationRequest(request);

        // Check if the username already exists
        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            log.error("User already existed{}", user.getEmail());
            throw new AppException(ErrorCode.USER_ALREADY_EXISTED);
        });

        // Map the request to the User entity
        User user = userMapper.toUser(request);

        // Encode the password
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Set roles
        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);
        user.setRoles(roles);

        // Save the user first to get the user ID
        user = userRepository.save(user);

//        // Create and save the UserSubscription
//        UserSubscription userSubscription = UserSubscription.builder()
//                .user(user)
//                .remainingFreePosts(5)  // Default 5 free posts
//                .isPremium(false)       // Default not premium
//                .build();
//
//        userSubscriptionRepository.save(userSubscription);

        // Publish registration event
//        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user));
        String token = UUID.randomUUID().toString();
        tokenService.createVerificationToken(user, token);
        try {
            // Send verification email
            emailNotificationKafka.sendVerificationEmail(request, token);
            paymentClient.createPayment(user.getId());
        } catch (Exception e) {
            // Log the error
            log.error("Failed to send verification email to {}: {}", request.getEmail(), e.getMessage());
            userRepository.delete(user);
            // Throw a custom exception or a general runtime exception to indicate failure
            throw new AppException(ErrorCode.SEND_EMAIL_ERROR);
        }
        // Map the saved user to UserResponse and return
        return userMapper.toUserResponse(user);
    }


    @Override
    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByEmail(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }

    @Override
//    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse updateUser(UserUpdateRequest request) {
        if (request.getId() == null) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        User user = userRepository.findById(request.getId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        } else {
            if (user.getPassword() == null) {
                throw new AppException(ErrorCode.INVALID_PASSWORD);
            }
        }
        if (request.getFirstName() != null && !request.getFirstName().isEmpty()) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null && !request.getLastName().isEmpty()) {
            user.setLastName(request.getLastName());
        }
        if (request.getDayOfBirth() != null) {
            user.setDayOfBirth(request.getDayOfBirth());
        }
        return userMapper.toUserResponse(userRepository.save(user));
    }
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse updateRoles(Integer userId, Set<Role> newRoles) {

        // Lấy user từ database
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Cập nhật roles
        user.setRoles(newRoles);

        // Lưu lại trong database
        return userMapper.toUserResponse(userRepository.save(user));
    }


    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteUser(int userId) {
        userRepository.deleteById(userId);
        return "Delete Successfully";
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        log.info("In method get Users");
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    @Override
//    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getUser(int id) {
        return userMapper.toUserResponse(
                userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    @Override
    public UserResponse getMe() {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }

    @Override
    // Phương thức tải ảnh avatar lên Firebase Storage và cập nhật thông tin người dùng
    public String uploadImagesAvatar(int userId, MultipartFile file) {
        try {
            // Kiểm tra người dùng tồn tại trong hệ thống
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

            // Kiểm tra nếu file không rỗng
            if (file == null || file.isEmpty()) {
                throw new AppException(ErrorCode.REQUEST_EMPTY);
            }

            // Tải ảnh lên Firebase Storage và lấy URL công khai của ảnh
            String avatarUrl = firebaseStorageClient.uploadFileToBucket(BucketConstants.BUCKET_NAME.getValue(), BucketConstants.AVATAR_FOLDER.getValue(), file);

            // Cập nhật URL ảnh vào thông tin người dùng
            user.setAvatar(avatarUrl);

            // Lưu lại thông tin người dùng với avatar mới
            userRepository.save(user);

            // Trả về URL của ảnh đã tải lên
            return avatarUrl;

        } catch (Exception e) {
            log.error("Failed to upload avatar for user {}: {}", userId, e.getMessage(), e);
            throw new AppException(ErrorCode.REQUEST_NULL); // Đảm bảo có mã lỗi phù hợp
        }
    }

    @Override
    public InfoUserForCount countUser() {
        // Đếm số lượng người dùng
        int quantityUser = (int) userRepository.count();

        // Lấy danh sách tất cả người dùng và ánh xạ ra chỉ các id
        List<Integer> userId = userRepository.findAll().stream()
                .map(User::getId)  // Lấy id của mỗi User
                .collect(Collectors.toList());

        // Trả về thông tin bao gồm số lượng người dùng và danh sách id
        return InfoUserForCount.builder()
                .quantityUser(quantityUser)
                .id(userId)
                .build();
    }

    @Override
    public void supportEmail(SupportRequest request) {
        emailNotificationKafka.sendVerificationEmailSupport(request);
    }

}
