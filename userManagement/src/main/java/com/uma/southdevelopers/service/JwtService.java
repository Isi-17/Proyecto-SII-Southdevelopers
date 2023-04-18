package com.uma.southdevelopers.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    public static final String SECRET = "secreto";

    public String createToken(String username) {
        return JWT.create()
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withSubject(username)
                .sign(Algorithm.HMAC256(SECRET));
    }
}
