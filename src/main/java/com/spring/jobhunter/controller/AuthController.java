package com.spring.jobhunter.controller;

import com.spring.jobhunter.domain.User;
import com.spring.jobhunter.domain.dto.LoginDTO;
import com.spring.jobhunter.domain.dto.ResLoginDTO;
import com.spring.jobhunter.service.UserService;
import com.spring.jobhunter.util.SecurityUtil;
import com.spring.jobhunter.util.annotation.ApiMessage;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    @Autowired
    private AuthenticationManagerBuilder ạuthenticationManagerBuilder;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private UserService userService;

    @Value("${jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    @PostMapping("/auth/login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());
        // Xac thuc nguoi dung
        Authentication authentication = ạuthenticationManagerBuilder.getObject().authenticate(authenticationToken);
        //Create access token

        //Set thong tin nguoi dung dang nhap vao context de su dung sau nay
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResLoginDTO res = new ResLoginDTO();
        User currentUser = userService.getUserByUsername(loginDTO.getUsername());
        if (currentUser != null) {
            ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(currentUser.getId(), currentUser.getUsername(), currentUser.getEmail());
            res.setUser(userLogin);
        }

        String access_token = securityUtil.createAccessToken(authentication, res.getUser());
        res.setAccessToken(access_token);

        //Create refresh token
        String refresh_token = securityUtil.createRefreshToken(loginDTO.getUsername(), res);

        //Update user token
        userService.updateUserToken(refresh_token, loginDTO.getUsername());

        //Set cookies
        ResponseCookie resCookies = ResponseCookie.from("refresh_token", refresh_token)
                .httpOnly(true)
                .secure(true)
                .maxAge(refreshTokenExpiration)
                .path("/")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                .body(res);
    }

    @GetMapping("/auth/account")
    @ApiMessage("Get account")
    public ResponseEntity<ResLoginDTO.UserLogin> getAccount() {
        String email = SecurityUtil.getCurrentUserLogin().orElse("");

        User currentUser = userService.getUserByUsername(email);
        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin();
        if (currentUser != null) {
            userLogin.setId(currentUser.getId());
            userLogin.setUsername(currentUser.getUsername());
            userLogin.setEmail(currentUser.getEmail());
        }
        return ResponseEntity.ok(userLogin);
    }
}
