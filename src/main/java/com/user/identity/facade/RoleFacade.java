package com.user.identity.facade;

import java.util.List;

import org.springframework.stereotype.Service;

import com.user.identity.controller.dto.request.RoleRequest;
import com.user.identity.controller.dto.response.RoleResponse;
import com.user.identity.service.RoleService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleFacade {
    RoleService roleService;

    /**
     * Creates a new role based on the provided request.
     *
     * @param request the role request containing details for the new role
     * @return RoleResponse containing the created role details
     */
    public RoleResponse create(RoleRequest request) {
        return roleService.create(request);
    }

    /**
     * Retrieves a list of all roles available in the system.
     *
     * @return a list of RoleResponse objects representing all roles
     */
    public List<RoleResponse> getAll() {
        return roleService.getAll();
    }

    /**
     * Deletes a specific role identified by its ID.
     *
     * @param roleId the ID of the role to delete
     */
    public String delete(String roleId) {
        roleService.delete(roleId);
        return "Delete Successfully";
    }
}
