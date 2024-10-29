package com.user.identity.service.Impl;

import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import com.user.identity.controller.dto.request.RoleRequest;
import com.user.identity.controller.dto.response.RoleResponse;
import com.user.identity.mapper.RoleMapper;
import com.user.identity.repository.PermissionRepository;
import com.user.identity.repository.RoleRepository;
import com.user.identity.service.RoleService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleServiceImpl implements RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    @Override
    public RoleResponse create(RoleRequest request) {
        var role = roleMapper.toRole(request);

        var permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));

        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    @Override
    public List<RoleResponse> getAll() {
        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
    }

    @Override
    public String delete(String roleId) {
        roleRepository.deleteById(roleId);
        return "Delete Successfully";
    }
}
