package com.spring.jobhunter.service;

import com.spring.jobhunter.domain.User;

import java.util.List;

public interface UserService {
    public List<User> getAllUsers();
    public User getUserById(Long id);
    public User saveOrUpdateUser(User user);
    public void deleteUser(Long id);
}
