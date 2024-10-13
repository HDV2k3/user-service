package com.user.identity.facade;

import java.util.List;

import org.springframework.stereotype.Service;

import com.user.identity.dto.request.PermissionRequest;
import com.user.identity.dto.response.PermissionResponse;
import com.user.identity.service.PermissionService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionFacade {
    PermissionService permissionService;

    /**
     * Creates a new permission based on the provided request.
     *
     * @param request the permission request containing details for the new permission
     * @return PermissionResponse containing the created permission details
     */
    public PermissionResponse create(PermissionRequest request) {
        return permissionService.create(request);
    }

    /**
     * Retrieves a list of all permissions available in the system.
     *
     * @return a list of PermissionResponse objects representing all permissions
     */
    public List<PermissionResponse> getAll() {
        return permissionService.getAll();
    }

    /**
     * Deletes a specific permission identified by its ID.
     *
     * @param permissionId the ID of the permission to delete
     */
    public String delete(String permissionId) {
        permissionService.delete(permissionId);
        return "Delete Successfully";
    }
}
