package com.user.identity.service.Impl;

import java.util.HashSet;
import java.util.List;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.user.identity.constant.PredefinedRole;
import com.user.identity.dto.request.UserCreationRequest;
import com.user.identity.dto.request.UserUpdateRequest;
import com.user.identity.dto.response.UserResponse;
import com.user.identity.entity.Role;
import com.user.identity.entity.User;
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

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    /// @Autowired
    @Override
    public UserResponse createUser(UserCreationRequest request) {
        // Check if the username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        // Map the request to the User entity
        User user = userMapper.toUser(request);

        // Encode the password
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Set roles
        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);
        user.setRoles(roles);

        // Save the user
        user = userRepository.save(user);

        // Map the saved user to UserResponse and return
        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }

    @Override
    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse updateUser(int userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

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
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getUser(int id) {
        return userMapper.toUserResponse(
                userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }
}
