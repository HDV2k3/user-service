package com.user.identity.mapper;

import com.user.identity.repository.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.user.identity.controller.dto.request.UserCreationRequest;
import com.user.identity.controller.dto.request.UserUpdateRequest;
import com.user.identity.controller.dto.response.UserResponse;
import com.user.identity.repository.entity.User;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);



    default Set<Role> map(List<String> roles) {
        return roles.stream()
                .map(roleName -> {
                    Role role = new Role();
                    role.setName(roleName);
                    return role;
                })
                .collect(Collectors.toSet());
    }
}