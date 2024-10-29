package com.user.identity.service;

import java.util.List;

import com.user.identity.controller.dto.request.PermissionRequest;
import com.user.identity.controller.dto.response.PermissionResponse;

public interface PermissionService {
    PermissionResponse create(PermissionRequest request);

    List<PermissionResponse> getAll();

    String delete(String permission);
}
