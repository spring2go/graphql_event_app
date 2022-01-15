package com.spring2go.easyevent.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.spring2go.easyevent.type.EventInput;
import com.spring2go.easyevent.util.DateUtil;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "event")
public class EventEntity {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String title;
    private String description;
    private Float price;
    private Date date;

    public static EventEntity fromEventInput(EventInput input) {
        EventEntity eventEntity = new EventEntity();
        eventEntity.setTitle(input.getTitle());
        eventEntity.setDescription(input.getDescription());
        eventEntity.setPrice(input.getPrice());
        eventEntity.setDate(DateUtil.convertISOStringToDate(input.getDate()));
        return eventEntity;
    }
}
