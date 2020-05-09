package com.linkage.ibsource.service.implementation;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.linkage.ibsource.entity.User;
import com.linkage.ibsource.service.AuthService;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class AuthServiceImpl implements AuthService {
    @Override
    public String getToken(User user) {
        String token = "";
        try {
            token = JWT.create()
                    .withAudience(user.getId().toString())
                    .sign(Algorithm.HMAC256(user.getPassword()));
        } catch (UnsupportedEncodingException ignore) {
        }
        return token;
    }
}