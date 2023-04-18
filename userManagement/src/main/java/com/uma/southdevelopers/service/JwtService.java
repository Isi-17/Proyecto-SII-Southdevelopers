package com.uma.southdevelopers.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
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

    public boolean validateToken(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();

        DecodedJWT decodedJWT = verifier.verify(token);

        // TODO
        return true;
    }
}
