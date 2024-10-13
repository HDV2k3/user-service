package com.user.identity.facade;

import com.user.identity.dto.request.RoleRequest;
import com.user.identity.dto.response.RoleResponse;
import com.user.identity.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

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
