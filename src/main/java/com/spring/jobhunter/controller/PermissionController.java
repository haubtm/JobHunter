package com.spring.jobhunter.controller;

import com.spring.jobhunter.domain.Permission;
import com.spring.jobhunter.domain.response.ResultPaginationDTO;
import com.spring.jobhunter.service.PermissionService;
import com.spring.jobhunter.util.annotation.ApiMessage;
import com.spring.jobhunter.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    @GetMapping("/permissions")
    @ApiMessage("Get all permissions")
    public ResponseEntity<ResultPaginationDTO> getAllPermissions(
            @Filter Specification<Permission> permissionSpecification, Pageable pageable) {
        return ResponseEntity.ok(permissionService.getAllPermissions(permissionSpecification, pageable));
    }

    @PostMapping("/permissions")
    @ApiMessage("Create a new permission")
    public ResponseEntity<Permission> createPermission(@Valid @RequestBody Permission permission) throws IdInvalidException {
        if(permissionService.isPermissionExist(permission)) {
            throw new IdInvalidException("Permission already exists");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(permissionService.create(permission));
    }

    @PutMapping("/permissions")
    @ApiMessage("Update a permission")
    public ResponseEntity<Permission> updatePermission(@Valid @RequestBody Permission permission) throws IdInvalidException {
        if(permissionService.getPermissionById(permission.getId()) == null) {
            throw new IdInvalidException("Permission not found");
        }
        if(permissionService.isPermissionExist(permission)) {
            if(permissionService.isSameName(permission))
                throw new IdInvalidException("Permission already exists");
        }
        return ResponseEntity.ok().body(permissionService.update(permission));
    }

    @DeleteMapping("/permissions/{id}")
    @ApiMessage("Delete a permission")
    public ResponseEntity<Void> deletePermission(@PathVariable long id) throws IdInvalidException {
        if(permissionService.getPermissionById(id) == null) {
            throw new IdInvalidException("Permission not found");
        }
        permissionService.delete(id);
        return ResponseEntity.ok().body(null);
    }
}
