package com.spring.jobhunter.controller;

import com.spring.jobhunter.domain.Role;
import com.spring.jobhunter.domain.response.ResultPaginationDTO;
import com.spring.jobhunter.service.RoleService;
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
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping("/roles")
    @ApiMessage("Get all roles")
    public ResponseEntity<ResultPaginationDTO> getAllRoles(
            @Filter Specification<Role> roleSpecification, Pageable pageable) {
        return ResponseEntity.ok(roleService.getAllRoles(roleSpecification, pageable));
    }

    @GetMapping("/roles/{id}")
    @ApiMessage("Get role by id")
    public ResponseEntity<Role> getRoleById(@PathVariable("id") long id) throws IdInvalidException {
        Role role = roleService.getRoleById(id);
        if (role == null) {
            throw new IdInvalidException("Role with id = " + id + " not found");
        }
        return ResponseEntity.ok(role);
    }

    @PostMapping("/roles")
    @ApiMessage("Create a new role")
    public ResponseEntity<Role> createRole(@Valid @RequestBody Role role) throws IdInvalidException {
        if (roleService.existByName(role.getName())) {
            throw new IdInvalidException("Role with name" + role.getName() + "already exists");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.create(role));

    }

    @PutMapping("/roles")
    @ApiMessage("Update a role")
    public ResponseEntity<Role> updateRole(@Valid @RequestBody Role role) throws IdInvalidException {
        if (roleService.getRoleById(role.getId()) == null) {
            throw new IdInvalidException("Role with id" + role.getId() + "not found");
        }
//        if (roleService.existByName(role.getName())) {
//            throw new IdInvalidException("Role with name" + role.getName() + "already exists");
//        }
        return ResponseEntity.status(HttpStatus.OK).body(roleService.update(role));
    }

    @DeleteMapping("/roles/{id}")
    @ApiMessage("Delete a role")
    public ResponseEntity<Void> deleteRole(@PathVariable("id") long id) throws IdInvalidException {
        if (roleService.getRoleById(id) == null) {
            throw new IdInvalidException("Role with id" + id + "not found");
        }
        roleService.delete(id);
        return ResponseEntity.ok().body(null);
    }
}
