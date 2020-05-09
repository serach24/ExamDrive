package com.linkage.ibsource.service;

import com.linkage.ibsource.entity.User;

public interface AuthService {
    String getToken(User user);
}
