package com.spring.jobhunter.service.impl;

import com.spring.jobhunter.domain.Permission;
import com.spring.jobhunter.domain.response.ResultPaginationDTO;
import com.spring.jobhunter.repository.PermissionRepository;
import com.spring.jobhunter.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public ResultPaginationDTO getAllPermissions(Specification<Permission> permissionSpecification, Pageable pageable) {
        Page<Permission> permissions = permissionRepository.findAll(permissionSpecification, pageable);
        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(permissions.getTotalPages());
        meta.setTotal(permissions.getTotalElements());

        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(permissions.getContent());

        return resultPaginationDTO;
    }

    @Override
    public Permission getPermissionById(long id) {
        return permissionRepository.findById(id).orElse(null);
    }

    @Override
    public boolean isPermissionExist(Permission permission) {
        return permissionRepository.existsByModuleAndApiPathAndMethod(permission.getModule(), permission.getApiPath(), permission.getMethod());
    }

    @Override
    public boolean isSameName(Permission permission) {
        Permission currentPermission = permissionRepository.findById(permission.getId()).orElse(null);
        if (currentPermission != null) {
            if(currentPermission.getName().equals(permission.getName()))
                return true;
        }
        return false;
    }

    @Override
    public Permission create(Permission permission) {
        return permissionRepository.save(permission);
    }

    @Override
    public Permission update(Permission permission) {
        Permission currentPermission = permissionRepository.findById(permission.getId()).orElse(null);
        if (currentPermission != null) {
            currentPermission.setName(permission.getName());
            currentPermission.setModule(permission.getModule());
            currentPermission.setApiPath(permission.getApiPath());
            currentPermission.setMethod(permission.getMethod());

            return permissionRepository.save(currentPermission);
        }
        return null;
    }

    @Override
    public void delete(long id) {
        Permission permission = permissionRepository.findById(id).orElse(null);
        permission.getRoles().forEach(role -> role.getPermissions().remove(permission));
        permissionRepository.delete(permission);
    }
}
