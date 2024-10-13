package com.user.identity.service;

import java.util.List;

import com.user.identity.dto.request.RoleRequest;
import com.user.identity.dto.response.RoleResponse;

public interface RoleService {
    RoleResponse create(RoleRequest request);

    List<RoleResponse> getAll();

    String delete(String roleId);
}
