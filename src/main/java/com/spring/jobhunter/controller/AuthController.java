package com.spring.jobhunter.controller;

import com.spring.jobhunter.domain.dto.LoginDTO;
import com.spring.jobhunter.domain.dto.ResLoginDTO;
import com.spring.jobhunter.util.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    @Autowired
    private AuthenticationManagerBuilder ạuthenticationManagerBuilder;

    @Autowired
    private SecurityUtil securityUtil;

    @PostMapping("/login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());
        // Xac thuc nguoi dung
        Authentication authentication = ạuthenticationManagerBuilder.getObject().authenticate(authenticationToken);
        //Create token
        String access_token = securityUtil.createToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResLoginDTO resLoginDTO = new ResLoginDTO();
        resLoginDTO.setAccessToken(access_token);
        return ResponseEntity.ok().body(resLoginDTO);
    }
}
