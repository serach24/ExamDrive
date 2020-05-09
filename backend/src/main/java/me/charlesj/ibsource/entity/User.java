package com.linkage.ibsource.entity;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name="user")
public class User {
    private Integer id;

    private String type = "user";

    private String username;

    private String password;

    private String email;

    private String school;

    private String realName;
}
