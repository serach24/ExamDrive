package com.linkage.ibsource.controller;

import com.alibaba.fastjson.JSONObject;
import com.linkage.ibsource.entity.User;
import com.linkage.ibsource.service.UserService;
import com.linkage.ibsource.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthController {
    private AuthService authService;
    private UserService userService;

    @Autowired
    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("")
    public Object login(@RequestBody User user) {
        User userInDataBase = userService.findByUsername(user.getUsername());
        JSONObject jsonObject = new JSONObject();
        if (userInDataBase == null) {
            jsonObject.put("message", "User does not exist");
        } else if (!userService.comparePassword(user, userInDataBase)) {
            jsonObject.put("message", "Password is not correct");
        } else {
            String token = authService.getToken(userInDataBase);
            jsonObject.put("token", token);
            jsonObject.put("user", userInDataBase);
            jsonObject.put("code", 20000);
        }
        return jsonObject;
    }
}
