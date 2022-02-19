package com.spring2go.easyevent.custom;

import com.netflix.graphql.dgs.context.DgsCustomContextBuilderWithRequest;
import com.spring2go.easyevent.entity.UserEntity;
import com.spring2go.easyevent.mapper.UserEntityMapper;
import com.spring2go.easyevent.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Component
@Slf4j
public class AuthContextBuilder implements DgsCustomContextBuilderWithRequest {

    private final UserEntityMapper userEntityMapper;

    static String AUTHORIZATION_HEADER = "Authorization";

    public AuthContextBuilder(UserEntityMapper userEntityMapper) {
        this.userEntityMapper = userEntityMapper;
    }

    @Override
    public Object build(@Nullable Map map, @Nullable HttpHeaders httpHeaders, @Nullable WebRequest webRequest) {
        log.info("Building Auth Context ...");
        AuthContext authContext = new AuthContext();
        if (!httpHeaders.containsKey(AUTHORIZATION_HEADER)) {
            log.info("User is not authenticated");
            return authContext;
        }
        String authorization = httpHeaders.getFirst(AUTHORIZATION_HEADER);
        String token = authorization.replace("Bearer ", "");
        Integer userId;
        try {
            userId = TokenUtil.verifyToken(token);
        } catch (Exception ex) {
            authContext.setTokenInvalid(true);
            return authContext;
        }
        UserEntity userEntity = userEntityMapper.selectById(userId);
        if (userEntity == null) {
            authContext.setTokenInvalid(true);
            return authContext;
        }
        authContext.setUserEntity(userEntity);
        log.info("User is authenticated, userId = {}", userId);
        return authContext;
    }
}
