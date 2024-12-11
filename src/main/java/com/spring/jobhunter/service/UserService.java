package com.spring.jobhunter.service;

import com.spring.jobhunter.domain.User;
import com.spring.jobhunter.domain.dto.ResultPaginationDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    public ResultPaginationDTO getAllUsers(Pageable pageable);
    public User getUserById(Long id);
    public User saveOrUpdateUser(User user);
    public void deleteUser(Long id);
    User getUserByUsername(String username);
}
