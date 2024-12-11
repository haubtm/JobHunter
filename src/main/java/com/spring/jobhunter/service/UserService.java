package com.spring.jobhunter.service;

import com.spring.jobhunter.domain.User;
import com.spring.jobhunter.domain.dto.ResultPaginationDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface UserService {
    public ResultPaginationDTO getAllUsers(Specification<User> userSpecification);
    public User getUserById(Long id);
    public User saveOrUpdateUser(User user);
    public void deleteUser(Long id);
    User getUserByUsername(String username);
}
