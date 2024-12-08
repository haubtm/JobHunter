package com.spring.jobhunter.controller;

import com.spring.jobhunter.domain.dto.LoginDTO;
import com.spring.jobhunter.util.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    @Autowired
    private AuthenticationManagerBuilder ạuthenticationManagerBuilder;

    @Autowired
    private SecurityUtil securityUtil;

    @PostMapping("/login")
    public ResponseEntity<LoginDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());
        // Xac thuc nguoi dung
        Authentication authentication = ạuthenticationManagerBuilder.getObject().authenticate(authenticationToken);
        //Create token
        securityUtil.createToken(authentication);
        return ResponseEntity.ok().body(loginDTO);
    }
}
