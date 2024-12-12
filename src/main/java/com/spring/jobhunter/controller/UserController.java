package com.spring.jobhunter.controller;

import com.spring.jobhunter.domain.User;
import com.spring.jobhunter.domain.dto.ResCreateUserDTO;
import com.spring.jobhunter.domain.dto.ResUpdateUserDTO;
import com.spring.jobhunter.domain.dto.ResUserDTO;
import com.spring.jobhunter.domain.dto.ResultPaginationDTO;
import com.spring.jobhunter.service.UserService;
import com.spring.jobhunter.util.annotation.ApiMessage;
import com.spring.jobhunter.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String getHelloWorld() {
        return "Hello World";
    }

    @GetMapping("/users")
    @ApiMessage("Get all users")
    public ResponseEntity<ResultPaginationDTO> getAllUser(
            @Filter Specification<User> userSpecification,
            Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers(userSpecification, pageable));
    }

    @GetMapping("/users/{id}")
    @ApiMessage("Get user by id")
    public ResponseEntity<ResUserDTO> getUserById(@PathVariable("id") Long id) throws IdInvalidException {
        User user = userService.getUserById(id);
        if(user == null) {
            throw new IdInvalidException("User with id = " + id + " not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(userService.convertToResUserDTO(user));
    }

    @PostMapping("/users")
    @ApiMessage("Create new user")
    public ResponseEntity<ResCreateUserDTO> saveUser(@Valid @RequestBody User user) throws IdInvalidException {
        boolean isEmailExist = userService.isEmailExist(user.getEmail());
        if (isEmailExist) {
            throw new IdInvalidException("Email " + user.getEmail() + " da ton tai");
        }
        String hashPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);
        User savedUser = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.convertToResCreateUserDTO(savedUser));
    }

    @PutMapping("/users")
    @ApiMessage("Update user")
    public ResponseEntity<ResUpdateUserDTO> updateUser(@RequestBody User user) throws IdInvalidException {
        User updatedUser = userService.updateUser(user);
        if(updatedUser == null) {
            throw new IdInvalidException("User with id = " + user.getId() + " not found");
        }
        return ResponseEntity.ok(userService.convertToResUpdateUserDTO(updatedUser));
    }

    @DeleteMapping("/users/{id}")
    @ApiMessage("Delete user")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) throws IdInvalidException {
        if (userService.getUserById(id) == null) {
            throw new IdInvalidException("User with id = " + id + " not found");
        }
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
