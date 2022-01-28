package com.spring2go.easyevent.type;

import com.spring2go.easyevent.entity.UserEntity;
import lombok.Data;

@Data
public class User {
    private String email;
    private String password;

    public static User fromEntity(UserEntity userEntity) {
        User user = new User();
        user.setEmail(userEntity.getEmail());
        return user;
    }
}
