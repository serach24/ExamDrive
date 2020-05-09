package com.linkage.ibsource.service.implementation;

import com.linkage.ibsource.dao.UserDao;
import com.linkage.ibsource.entity.User;
import com.linkage.ibsource.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import basic.framework.components.core.utils.Md5s;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public User add(User user){
        String md5Password=Md5s.string2MD5(user.getPassword());
        user.setPassword(md5Password);
        userDao.add(user);
        return findById(user.getId());
    }

    @Override
    public User findById(Integer id) {
        User param = new User();
        param.setId(id);
        return userDao.findOne(param);
    }

    @Override
    public User findByUsername(String username) {
        User param = new User();
        param.setUsername(username);
        return userDao.findOne(param);
    }

    @Override
    public User findByEmail(String email) {
        User param = new User();
        param.setEmail(email);
        return userDao.findByEmail(param);
    }

    @Override
    public boolean comparePassword(User user, User userInDataBase) {
        return Md5s.string2MD5(user.getPassword()).equals(userInDataBase.getPassword());
    }
}