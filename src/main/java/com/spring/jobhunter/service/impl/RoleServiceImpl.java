package com.spring.jobhunter.service.impl;

import com.spring.jobhunter.domain.Permission;
import com.spring.jobhunter.domain.Role;
import com.spring.jobhunter.domain.response.ResultPaginationDTO;
import com.spring.jobhunter.repository.PermissionRepository;
import com.spring.jobhunter.repository.RoleRepository;
import com.spring.jobhunter.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public ResultPaginationDTO getAllRoles(Specification<Role> roleSpecification, Pageable pageable) {
        Page<Role> roles = roleRepository.findAll(roleSpecification, pageable);
        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(roles.getTotalPages());
        meta.setTotal(roles.getTotalElements());

        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(roles.getContent());
        return resultPaginationDTO;
    }

    @Override
    public Role getRoleById(long id) {
        return roleRepository.findById(id).orElse(null);
    }

    @Override
    public boolean existByName(String name) {
        return roleRepository.existsByName(name);
    }

    @Override
    public Role create(Role role) {
        if(role.getPermissions() != null) {
            List<Long> reqPermission = role.getPermissions()
                    .stream().map(x -> x.getId()).collect(Collectors.toList());
            List<Permission> currentPermissions = permissionRepository.findByIdIn(reqPermission);
            role.setPermissions(currentPermissions);
        }
        return roleRepository.save(role);
    }

    @Override
    public Role update(Role role) {
        Role currentRole = roleRepository.findById(role.getId()).orElse(null);
        if(role.getPermissions() != null){
            List<Long> reqPermission = role.getPermissions()
                    .stream().map(x -> x.getId()).collect(Collectors.toList());
            List<Permission> currentPermissions = permissionRepository.findByIdIn(reqPermission);
            role.setPermissions(currentPermissions);
        }
        if (currentRole != null) {
            currentRole.setName(role.getName());
            currentRole.setDescription(role.getDescription());
            currentRole.setActive(role.isActive());
            currentRole.setPermissions(role.getPermissions());

            return roleRepository.save(currentRole);
        }
        return null;
    }

    @Override
    public void delete(long id) {
        roleRepository.deleteById(id);
    }
}
