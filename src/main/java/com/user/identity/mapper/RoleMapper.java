package com.user.identity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.user.identity.dto.request.RoleRequest;
import com.user.identity.dto.response.RoleResponse;
import com.user.identity.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
