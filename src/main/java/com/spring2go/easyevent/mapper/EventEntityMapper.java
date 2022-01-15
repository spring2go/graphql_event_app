package com.spring2go.easyevent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spring2go.easyevent.entity.EventEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EventEntityMapper extends BaseMapper<EventEntity> {
}
