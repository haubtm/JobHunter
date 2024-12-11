package com.spring.jobhunter.service.impl;

import com.spring.jobhunter.domain.User;
import com.spring.jobhunter.domain.dto.Meta;
import com.spring.jobhunter.domain.dto.ResultPaginationDTO;
import com.spring.jobhunter.repository.UserRepository;
import com.spring.jobhunter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public ResultPaginationDTO getAllUsers(Specification<User> userSpecification) {
        List<User> pageUser = userRepository.findAll(userSpecification);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        Meta meta = new Meta();

//        meta.setPage(pageUser.getNumber() + 1);
//        meta.setPageSize(pageUser.getSize());
//
//        meta.setPages(pageUser.getTotalPages());
//        meta.setTotal(pageUser.getTotalElements());

        rs.setMeta(meta);
        rs.setResult(pageUser);
        return rs;
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User saveOrUpdateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByEmail(username);
    }
}
