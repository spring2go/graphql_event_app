package com.spring2go.easyevent.fetcher;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;

import java.util.Arrays;
import java.util.List;

@DgsComponent
public class EventDataFetcher {
    @DgsQuery
    public List<String> events() {
        return Arrays.asList("Swimming", "All night coding", "Movie Watching");
    }

    @DgsMutation
    public String createEvent(@InputArgument String name) {
        return name + " event";
    }
}
