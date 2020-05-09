package com.linkage.ibsource.controller;

import com.alibaba.fastjson.JSONObject;
import com.linkage.ibsource.annotation.CurrentUser;
import com.linkage.ibsource.annotation.LoginRequired;
import com.linkage.ibsource.entity.User;
import com.linkage.ibsource.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/signup")
public class UserController{
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("")
    public Object add(@RequestBody User user) {
        JSONObject jsonObject = new JSONObject();
        if (userService.findByUsername(user.getUsername()) != null) {
            jsonObject.put("message","Username has been used");
            return jsonObject;
        }
        if (userService.findByEmail(user.getEmail()) != null) {
            jsonObject.put("message","Email has been used");
            return jsonObject;
        }
        jsonObject.put("user",userService.add(user));
        jsonObject.put("code", 20000);
        return jsonObject;
    }


    //
    @LoginRequired
    @GetMapping("/test")
    public Object testLogin() {
        return "success";
    }

    @GetMapping("/current")
    @LoginRequired
    public Object testCurrentUser(@CurrentUser User user) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 20000);
        jsonObject.put("user", user);
        return jsonObject;
    }
}
