package com.spring2go.easyevent.fetcher;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import com.spring2go.easyevent.entity.EventEntity;
import com.spring2go.easyevent.entity.UserEntity;
import com.spring2go.easyevent.mapper.EventEntityMapper;
import com.spring2go.easyevent.mapper.UserEntityMapper;
import com.spring2go.easyevent.type.Event;
import com.spring2go.easyevent.type.EventInput;
import com.spring2go.easyevent.type.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
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
                .map(eventEntity -> {
                    Event event = Event.fromEntity(eventEntity);
                    populateEventWithUser(event, eventEntity.getCreatorId());
                    return event;
                }).collect(Collectors.toList());

        return eventList;
    }

    @DgsMutation
    public Event createEvent(@InputArgument(name = "eventInput") EventInput input) {
        EventEntity newEventEntity = EventEntity.fromEventInput(input);

        eventEntityMapper.insert(newEventEntity);

        Event newEvent = Event.fromEntity(newEventEntity);

        populateEventWithUser(newEvent, newEventEntity.getCreatorId());

        return newEvent;
    }

    private void populateEventWithUser(Event event, Integer userId) {
        UserEntity userEntity = userEntityMapper.selectById(userId);
        User user = User.fromEntity(userEntity);
        event.setCreator(user);
    }


}
