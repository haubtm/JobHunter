package com.spring.jobhunter.controller;

import com.spring.jobhunter.domain.User;
import com.spring.jobhunter.domain.request.ReqLoginDTO;
import com.spring.jobhunter.domain.response.ResCreateUserDTO;
import com.spring.jobhunter.domain.response.ResLoginDTO;
import com.spring.jobhunter.service.UserService;
import com.spring.jobhunter.util.SecurityUtil;
import com.spring.jobhunter.util.annotation.ApiMessage;
import com.spring.jobhunter.util.error.IdInvalidException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    @PostMapping("/auth/login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody ReqLoginDTO reqLoginDTO) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(reqLoginDTO.getUsername(), reqLoginDTO.getPassword());
        // Xac thuc nguoi dung
        Authentication authentication = ạuthenticationManagerBuilder.getObject().authenticate(authenticationToken);
        //Create access token

        //Set thong tin nguoi dung dang nhap vao context de su dung sau nay
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResLoginDTO res = new ResLoginDTO();
        User currentUser = userService.getUserByUsername(reqLoginDTO.getUsername());
        if (currentUser != null) {
            ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
                    currentUser.getId(),
                    currentUser.getUsername(),
                    currentUser.getEmail(),
                    currentUser.getRole());
            res.setUser(userLogin);
        }

        String access_token = securityUtil.createAccessToken(authentication.getName(), res);
        res.setAccessToken(access_token);

        //Create refresh token
        String refresh_token = securityUtil.createRefreshToken(reqLoginDTO.getUsername(), res);

        //Update user token
        userService.updateUserToken(refresh_token, reqLoginDTO.getUsername());

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
    public ResponseEntity<ResLoginDTO.UserGetAccount> getAccount() {
        String email = SecurityUtil.getCurrentUserLogin().orElse("");

        User currentUser = userService.getUserByUsername(email);
        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin();
        ResLoginDTO.UserGetAccount userGetAccount = new ResLoginDTO.UserGetAccount();
        if (currentUser != null) {
            userLogin.setId(currentUser.getId());
            userLogin.setUsername(currentUser.getUsername());
            userLogin.setEmail(currentUser.getEmail());
            userGetAccount.setUser(userLogin);
        }
        return ResponseEntity.ok(userGetAccount);
    }

    @GetMapping("/auth/refresh")
    @ApiMessage("Get user by refresh token")
    public ResponseEntity<ResLoginDTO> getRefreshToken(@CookieValue(name = "refresh_token", defaultValue = "noToken") String refresh_token) throws IdInvalidException {
        if (refresh_token.equals("refresh_token")){
            throw new IdInvalidException("Khong co refresh token o cookie");
        }

        Jwt decodedToken = this.securityUtil.checkValidRefreshToken(refresh_token);
        String email = decodedToken.getSubject();
        User currentUser = userService.getUserByRefreshTokenAndEmail(refresh_token, email);
        if(currentUser == null){
            throw new IdInvalidException("Refresh token khong hop le");
        }
        ResLoginDTO res = new ResLoginDTO();
        User currentUserDB = userService.getUserByUsername(email);
        if (currentUserDB != null) {
            ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
                    currentUserDB.getId(),
                    currentUserDB.getUsername(),
                    currentUserDB.getEmail(),
                    currentUserDB.getRole());
            res.setUser(userLogin);
        }

        String access_token = securityUtil.createAccessToken(email, res);
        res.setAccessToken(access_token);

        //Create refresh token
        String new_refresh_token = securityUtil.createRefreshToken(email, res);

        //Update user token
        userService.updateUserToken(refresh_token, email);

        //Set cookies
        ResponseCookie resCookies = ResponseCookie.from("refresh_token", new_refresh_token)
                .httpOnly(true)
                .secure(true)
                .maxAge(refreshTokenExpiration)
                .path("/")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                .body(res);
    }

    @GetMapping("/auth/logout")
    @ApiMessage("Logout")
    public ResponseEntity<Void> logout() throws IdInvalidException {
        String email = SecurityUtil.getCurrentUserLogin().orElse("");
        if (email.equals("")) {
            throw new IdInvalidException("Access Token khong hop le");
        }

        //Update user token
        userService.updateUserToken(null, email);

        //Remove refresh token cookie
        ResponseCookie resCookies = ResponseCookie.from("refresh_token", null)
                .httpOnly(true)
                .secure(true)
                .maxAge(0)
                .path("/")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                .body(null);
    }

    @PostMapping("/auth/register")
    @ApiMessage("Register")
    public ResponseEntity<ResCreateUserDTO> register(@Valid @RequestBody User user) throws IdInvalidException {
        if(userService.isEmailExist(user.getEmail())) {
            throw new IdInvalidException("Email da ton tai");
        }
        String hashPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);
        User savedUser = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.convertToResCreateUserDTO(savedUser));
    }
}
