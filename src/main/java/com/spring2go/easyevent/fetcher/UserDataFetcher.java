package com.spring2go.easyevent.fetcher;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;
import com.spring2go.easyevent.entity.UserEntity;
import com.spring2go.easyevent.mapper.UserEntityMapper;
import com.spring2go.easyevent.type.User;
import com.spring2go.easyevent.type.UserInput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@DgsComponent
@RequiredArgsConstructor
public class UserDataFetcher {
    private final UserEntityMapper userEntityMapper;
    private final PasswordEncoder passwordEncoder;

    @DgsMutation
    public User createUser(@InputArgument UserInput userInput) {

        ensureUserNotExists(userInput);

        UserEntity newUserEntity = new UserEntity();
        newUserEntity.setEmail(userInput.getEmail());
        newUserEntity.setPassword(passwordEncoder.encode(userInput.getPassword()));

        userEntityMapper.insert(newUserEntity);

        User newUser = User.fromEntity(newUserEntity);
        newUser.setPassword(null);

        return newUser;
    }

    private void ensureUserNotExists(UserInput userInput) {
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(UserEntity::getEmail, userInput.getEmail());
        if (userEntityMapper.selectCount(queryWrapper) >= 1) {
            throw new RuntimeException("该email地址已经被使用");
        }
    }
}
