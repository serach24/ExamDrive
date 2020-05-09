package com.linkage.ibsource.config;

import com.linkage.ibsource.annotation.LoginRequired;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.linkage.ibsource.entity.User;
import com.linkage.ibsource.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;

public class AuthInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;

    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        // Pass if not mapped to method
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        // Decide if the interface requires login
        LoginRequired methodAnnotation = method.getAnnotation(LoginRequired.class);
        if (methodAnnotation != null) {
            String token = request.getHeader("token");  // Get token from header
            if (token == null) {
                throw new RuntimeException("No token，please login again");
            }
            Integer userId;
            try {
                userId = Integer.parseInt(JWT.decode(token).getAudience().get(0));  // Get user id from token
            } catch (JWTDecodeException e) {
                throw new RuntimeException("Token is not valid, please login again");
            }
            User user = userService.findById(userId);
            if (user == null) {
                throw new RuntimeException("User does not exist，please login again");
            }
            // Verify token
            try {
                JWTVerifier verifier =  JWT.require(Algorithm.HMAC256(user.getPassword())).build();
                try {
                    verifier.verify(token);
                } catch (JWTVerificationException e) {
                    throw new RuntimeException("Token is not valid, please login again");
                }
            } catch (UnsupportedEncodingException ignore) {}
            request.setAttribute("currentUser", user);
            return true;
        }
        return true;
    }

}
