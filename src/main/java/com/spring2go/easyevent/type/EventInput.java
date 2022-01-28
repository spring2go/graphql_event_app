package com.spring2go.easyevent.type;

import lombok.Data;

@Data
public class EventInput {
    private String title;
    private String description;
    private Float price;
    private String date;
    private Integer creatorId;
}
