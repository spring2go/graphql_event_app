package com.spring2go.easyevent.fetcher.dataloader;

import com.netflix.graphql.dgs.DgsDataLoader;
import com.spring2go.easyevent.mapper.UserEntityMapper;
import com.spring2go.easyevent.type.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dataloader.BatchLoader;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@DgsDataLoader(name="creators")
@RequiredArgsConstructor
@Slf4j
public class CreatorsDataLoader implements BatchLoader<Integer, User> {
    private final UserEntityMapper userEntityMapper;

    @Override
    public CompletionStage<List<User>> load(List<Integer> userIds) {
        log.info("CreatorsDataLoader loading userIds: {}", userIds);
        return CompletableFuture.supplyAsync(
                () -> userEntityMapper.selectBatchIds(userIds)
                        .stream().map(userEntity ->  User.fromEntity(userEntity))
                        .collect(Collectors.toList())
        );
    }
}
