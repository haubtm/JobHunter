package com.spring.jobhunter.service;

import com.spring.jobhunter.domain.User;
import com.spring.jobhunter.domain.response.ResCreateUserDTO;
import com.spring.jobhunter.domain.response.ResUpdateUserDTO;
import com.spring.jobhunter.domain.response.ResUserDTO;
import com.spring.jobhunter.domain.response.ResultPaginationDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface UserService {
    public ResultPaginationDTO getAllUsers(Specification<User> userSpecification, Pageable pageable);
    public User getUserById(Long id);
    public User saveUser(User user);
    public User updateUser(User user);
    public void deleteUser(Long id);
    User getUserByUsername(String username);
    boolean isEmailExist(String email);
    ResCreateUserDTO convertToResCreateUserDTO(User user);
    ResUserDTO convertToResUserDTO(User user);
    ResUpdateUserDTO convertToResUpdateUserDTO(User user);
    void updateUserToken(String token, String username);
    public User getUserByRefreshTokenAndEmail(String refreshToken, String email);
}
