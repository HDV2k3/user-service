package com.user.identity.mapper;

import org.mapstruct.Mapper;

import com.user.identity.dto.request.PermissionRequest;
import com.user.identity.dto.response.PermissionResponse;
import com.user.identity.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
