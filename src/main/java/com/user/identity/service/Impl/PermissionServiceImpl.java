package com.user.identity.service.Impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.user.identity.dto.request.PermissionRequest;
import com.user.identity.dto.response.PermissionResponse;
import com.user.identity.entity.Permission;
import com.user.identity.mapper.PermissionMapper;
import com.user.identity.repository.PermissionRepository;
import com.user.identity.service.PermissionService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionServiceImpl implements PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    @Override
    public PermissionResponse create(PermissionRequest request) {
        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    @Override
    public List<PermissionResponse> getAll() {
        var permissions = permissionRepository.findAll();
        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    @Override
    public String delete(String permissionId) {
        permissionRepository.deleteById(permissionId);
        return "Delete Successfully";
    }
}
