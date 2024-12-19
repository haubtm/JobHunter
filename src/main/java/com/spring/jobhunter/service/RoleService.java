package com.spring.jobhunter.service;

import com.spring.jobhunter.domain.Role;
import com.spring.jobhunter.domain.response.ResultPaginationDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface RoleService {
    ResultPaginationDTO getAllRoles(Specification<Role> roleSpecification, Pageable pageable);
    Role getRoleById(long id);
    boolean existByName(String name);
    Role create(Role role);
    Role update(Role role);
    void delete(long id);
}
