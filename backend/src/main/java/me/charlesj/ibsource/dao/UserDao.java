package com.linkage.ibsource.dao;

import com.linkage.ibsource.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

@Mapper
public interface UserDao {


    @Insert("insert into user(username, password, email, school, realName) " +
            "values (#{username},#{password}, #{email}, #{school}, #{realName})")
    @Options(useGeneratedKeys = true)
    int add(User user);

    @Select("select * from user where ((id is not null AND id=#{id}) OR (username is not null AND username=#{username}))")
    User findOne(User user);

    @Select("select * from user where (email=#{email})")
    User findByEmail(User user);
}
