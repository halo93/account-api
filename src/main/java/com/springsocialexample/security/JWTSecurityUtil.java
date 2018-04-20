package com.springsocialexample.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.User;

import java.util.Date;

import static com.springsocialexample.security.SecurityConstants.EXPIRATION_TIME;
import static com.springsocialexample.security.SecurityConstants.SECRET;
import static com.springsocialexample.security.SecurityConstants.TOKEN_PREFIX;

public class JWTSecurityUtil {
    public static String generateApiToken(String username) {
        return TOKEN_PREFIX + Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
                .compact();
    }
}
