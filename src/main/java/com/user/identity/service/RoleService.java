package com.user.identity.service;

import com.user.identity.dto.request.RoleRequest;
import com.user.identity.dto.response.RoleResponse;

import java.util.List;

public interface RoleService {
    RoleResponse create(RoleRequest request);
    List<RoleResponse> getAll();
    String delete(String roleId);
}
