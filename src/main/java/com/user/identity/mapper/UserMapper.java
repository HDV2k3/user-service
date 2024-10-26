package com.user.identity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.user.identity.dto.request.UserCreationRequest;
import com.user.identity.dto.request.UserUpdateRequest;
import com.user.identity.dto.response.UserResponse;
import com.user.identity.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    @Mapping(target = "id", source = "request.id")
    @Mapping(target = "password", source = "request.password")
    @Mapping(target = "firstName", source = "request.firstName")
    @Mapping(target = "lastName", source = "request.lastName")
    @Mapping(target = "dateOfBirth", source = "request.dateOfBirth")
    @Mapping(target = "roles", source = "request.roles")
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
