package com.spring.jobhunter.util;

import com.nimbusds.jose.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Service
public class SecurityUtil {
    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS256;
    @Value("${jwt.base64-secret}")
    private String jwtKey;

    @Value("${jwt.token-validity-in-seconds}")
    private String jwtExpiration;


    public void createToken(Authentication authentication) {

    }
}
