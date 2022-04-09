package com.spring2go.easyevent.fetcher;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.netflix.graphql.dgs.*;
import com.netflix.graphql.dgs.context.DgsContext;
import com.spring2go.easyevent.custom.AuthContext;
import com.spring2go.easyevent.entity.EventEntity;
import com.spring2go.easyevent.entity.UserEntity;
import com.spring2go.easyevent.fetcher.dataloader.CreatorsDataLoader;
import com.spring2go.easyevent.mapper.EventEntityMapper;
import com.spring2go.easyevent.mapper.UserEntityMapper;
import com.spring2go.easyevent.type.Event;
import com.spring2go.easyevent.type.EventInput;
import com.spring2go.easyevent.type.User;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dataloader.DataLoader;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@DgsComponent
@RequiredArgsConstructor
public class EventDataFetcher {

    private final EventEntityMapper eventEntityMapper;
    private final UserEntityMapper userEntityMapper;

    @DgsQuery
    public List<Event> events() {
        List<EventEntity> eventEntityList = eventEntityMapper.selectList(new QueryWrapper<>());
        List<Event> eventList = eventEntityList.stream()
                .map(Event::fromEntity).collect(Collectors.toList());

        return eventList;
    }

    @DgsMutation
    public Event createEvent(@InputArgument(name = "eventInput") EventInput input, DataFetchingEnvironment dfe) {
        AuthContext authContext = DgsContext.getCustomContext(dfe);
        authContext.ensureAuthenticated();

        EventEntity newEventEntity = EventEntity.fromEventInput(input);
        newEventEntity.setCreatorId(authContext.getUserEntity().getId());

        eventEntityMapper.insert(newEventEntity);

        Event newEvent = Event.fromEntity(newEventEntity);

        return newEvent;
    }

    @DgsData(parentType = "Event", field = "creator")
    public CompletableFuture<User> creator(DgsDataFetchingEnvironment dfe) {
        Event event = dfe.getSource();
        log.info("Fetching creator wit id: {}", event.getCreatorId());
        DataLoader<Integer, User> dataLoader = dfe.getDataLoader(CreatorsDataLoader.class);

        return dataLoader.load(event.getCreatorId());
    }

//    @DgsData(parentType = "Event", field = "creator")
//    public User creator(DgsDataFetchingEnvironment dfe) {
//        Event event = dfe.getSource();
//        log.info("Fetching creator {}", event.getCreatorId());
//        UserEntity userEntity = userEntityMapper.selectById(event.getCreatorId());
//        User user = User.fromEntity(userEntity);
//        return user;
//    }
}
