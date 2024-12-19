package com.spring.jobhunter.service;

import com.spring.jobhunter.domain.Permission;
import com.spring.jobhunter.domain.response.ResultPaginationDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface PermissionService {
    ResultPaginationDTO getAllPermissions(Specification<Permission> permissionSpecification, Pageable pageable);
    Permission getPermissionById(long id);
    boolean isPermissionExist(Permission permission);
    boolean isSameName(Permission permission);
    Permission create(Permission permission);
    Permission update(Permission permission);
    void delete(long id);
}
