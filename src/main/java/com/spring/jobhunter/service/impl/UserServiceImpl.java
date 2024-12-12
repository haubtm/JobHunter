package com.spring.jobhunter.service.impl;

import com.spring.jobhunter.domain.Company;
import com.spring.jobhunter.domain.User;
import com.spring.jobhunter.domain.response.ResCreateUserDTO;
import com.spring.jobhunter.domain.response.ResUpdateUserDTO;
import com.spring.jobhunter.domain.response.ResUserDTO;
import com.spring.jobhunter.domain.response.ResultPaginationDTO;
import com.spring.jobhunter.repository.CompanyRepository;
import com.spring.jobhunter.repository.UserRepository;
import com.spring.jobhunter.service.CompanyService;
import com.spring.jobhunter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public ResultPaginationDTO getAllUsers(Specification<User> userSpecification, Pageable pageable) {
        Page<User> pageUser = userRepository.findAll(userSpecification, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(pageUser.getTotalPages());
        meta.setTotal(pageUser.getTotalElements());

        rs.setMeta(meta);

        List<ResUserDTO> listUser = pageUser.getContent()
                        .stream().map(item -> new ResUserDTO(
                                item.getId(),
                                item.getEmail(),
                                item.getUsername(),
                                item.getGender(),
                                item.getAddress(),
                                item.getAge(),
                                item.getCreatedAt(),
                                item.getUpdatedAt(),
                                item.getCompany() != null ? new ResUserDTO.CompanyUser(
                                        item.getCompany().getId(),
                                        item.getCompany().getName()
                                ) : null))
                .collect(Collectors.toList());
        rs.setResult(listUser);
        return rs;
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User saveUser(User user) {
        if (user.getCompany() != null) {
            Company company = companyRepository.findById(user.getCompany().getId()).orElse(null);
            user.setCompany(company);
        }
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        User currentUser = userRepository.findById(user.getId()).orElse(null);
        if (currentUser != null) {
            currentUser.setUsername(user.getUsername());
            currentUser.setGender(user.getGender());
            currentUser.setAddress(user.getAddress());
            currentUser.setAge(user.getAge());

            if (user.getCompany() != null) {
                Company company = companyRepository.findById(user.getCompany().getId()).orElse(null);
                currentUser.setCompany(company);
            }

            currentUser = userRepository.save(currentUser);
        }
        return currentUser;
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByEmail(username);
    }

    @Override
    public boolean isEmailExist(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public ResCreateUserDTO convertToResCreateUserDTO(User user) {
        ResCreateUserDTO res = new ResCreateUserDTO();
        ResCreateUserDTO.CompanyUser companyUser = new ResCreateUserDTO.CompanyUser();

        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setUsername(user.getUsername());
        res.setAge(user.getAge());
        res.setAddress(user.getAddress());
        res.setGender(user.getGender());
        res.setCreatedAt(user.getCreatedAt());

        if (user.getCompany() != null) {
            companyUser.setId(user.getCompany().getId());
            companyUser.setName(user.getCompany().getName());
            res.setCompany(companyUser);
        }
        return res;
    }

    @Override
    public ResUserDTO convertToResUserDTO(User user) {
        ResUserDTO res = new ResUserDTO();
        ResUserDTO.CompanyUser companyUser = new ResUserDTO.CompanyUser();

        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setUsername(user.getUsername());
        res.setAge(user.getAge());
        res.setAddress(user.getAddress());
        res.setGender(user.getGender());
        res.setCreatedAt(user.getCreatedAt());
        res.setUpdatedAt(user.getUpdatedAt());

        if (user.getCompany() != null) {
            companyUser.setId(user.getCompany().getId());
            companyUser.setName(user.getCompany().getName());
            res.setCompany(companyUser);
        }
        return res;
    }

    @Override
    public ResUpdateUserDTO convertToResUpdateUserDTO(User user) {
        ResUpdateUserDTO res = new ResUpdateUserDTO();
        ResUpdateUserDTO.CompanyUser companyUser = new ResUpdateUserDTO.CompanyUser();

        res.setId(user.getId());
        res.setUsername(user.getUsername());
        res.setAge(user.getAge());
        res.setAddress(user.getAddress());
        res.setGender(user.getGender());
        res.setUpdatedAt(user.getUpdatedAt());

        if (user.getCompany() != null) {
            companyUser.setId(user.getCompany().getId());
            companyUser.setName(user.getCompany().getName());
            res.setCompany(companyUser);
        }
        return res;
    }

    @Override
    public void updateUserToken(String token, String username) {
        User user = userRepository.findByEmail(username);
        if (user != null) {
            user.setRefreshToken(token);
            userRepository.save(user);
        }
    }

    @Override
    public User getUserByRefreshTokenAndEmail(String refreshToken, String email) {
        return userRepository.findByRefreshTokenAndEmail(refreshToken, email);
    }
}
