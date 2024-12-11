package com.spring.jobhunter.controller;

import com.spring.jobhunter.domain.User;
import com.spring.jobhunter.domain.dto.ResultPaginationDTO;
import com.spring.jobhunter.service.UserService;
import com.spring.jobhunter.util.error.IdInvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
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
    public ResponseEntity<ResultPaginationDTO> getUser(
            @RequestParam("current") Optional<String> current,
            @RequestParam("pageSize") Optional<String> pageSize
            ) {
        String sCurrent = current.orElse("1");
        String sPageSize = pageSize.orElse("10");
        Pageable pageable = PageRequest.of(Integer.parseInt(sCurrent) - 1, Integer.parseInt(sPageSize));
        ResultPaginationDTO rs = userService.getAllUsers(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(rs);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PostMapping("/users")
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        String hashPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);
        User savedUser = userService.saveOrUpdateUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        User updatedUser = userService.saveOrUpdateUser(user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) throws IdInvalidException {
        if (id >= 1500 ){
            throw new IdInvalidException("Id khong lon hon 1500");
        }
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully");
    }
}
