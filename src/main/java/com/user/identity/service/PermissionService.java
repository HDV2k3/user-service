package com.user.identity.service;

import com.user.identity.dto.request.PermissionRequest;
import com.user.identity.dto.response.PermissionResponse;

import java.util.List;

public interface PermissionService {
    PermissionResponse create(PermissionRequest request);
    List<PermissionResponse> getAll();
    String delete(String permission);
}
