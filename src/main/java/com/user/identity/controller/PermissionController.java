package com.user.identity.controller;

import com.user.identity.dto.ApiResponse;
import com.user.identity.dto.request.PermissionRequest;
import com.user.identity.dto.response.PermissionResponse;
import com.user.identity.facade.PermissionFacade;
import com.user.identity.service.Impl.PermissionServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "Permission Controller")

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionController {
    PermissionFacade permissionFacade;

    @PostMapping
    ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest request) {
      var result = permissionFacade.create(request);
      return  ApiResponse.success(result);
    }

    @GetMapping
    ApiResponse<List<PermissionResponse>> getAll() {
      var result = permissionFacade.getAll();
      return ApiResponse.success(result);
    }

    @DeleteMapping("/{permission}")
    ApiResponse<String> delete(@PathVariable String permission) {
       var result = permissionFacade.delete(permission);
        return ApiResponse.success(result);
    }
}
