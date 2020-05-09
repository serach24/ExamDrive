package com.linkage.ibsource.service;

import com.linkage.ibsource.entity.User;


public interface UserService {
    User add(User user);

    User findById(Integer id);

    User findByUsername(String username);

    User findByEmail(String email);

    boolean comparePassword(User user, User userInDataBase);
}