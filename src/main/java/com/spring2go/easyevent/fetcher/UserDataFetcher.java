package com.spring2go.easyevent.fetcher;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.netflix.graphql.dgs.*;
import com.spring2go.easyevent.entity.EventEntity;
import com.spring2go.easyevent.entity.UserEntity;
import com.spring2go.easyevent.mapper.EventEntityMapper;
import com.spring2go.easyevent.mapper.UserEntityMapper;
import com.spring2go.easyevent.type.Event;
import com.spring2go.easyevent.type.User;
import com.spring2go.easyevent.type.UserInput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@DgsComponent
@RequiredArgsConstructor
public class UserDataFetcher {
    private final UserEntityMapper userEntityMapper;
    private final EventEntityMapper eventEntityMapper;
    private final PasswordEncoder passwordEncoder;

    @DgsQuery
    public List<User> users() {
        List<UserEntity> userEntityList = userEntityMapper.selectList(null);
        List<User> userList = userEntityList.stream()
                .map(User::fromEntity)
                .collect(Collectors.toList());
        return userList;
    }

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

    @DgsData(parentType = "User", field = "createdEvents")
    public List<Event> createdEvents(DgsDataFetchingEnvironment dfe) {
        User user = dfe.getSource();
        QueryWrapper<EventEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EventEntity::getCreatorId, user.getId());
        List<EventEntity> eventEntityList = eventEntityMapper.selectList(queryWrapper);
        List<Event> eventList = eventEntityList.stream()
                .map(Event::fromEntity)
                .collect(Collectors.toList());
        return eventList;
    }

    private void ensureUserNotExists(UserInput userInput) {
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(UserEntity::getEmail, userInput.getEmail());
        if (userEntityMapper.selectCount(queryWrapper) >= 1) {
            throw new RuntimeException("该email地址已经被使用");
        }
    }
}
