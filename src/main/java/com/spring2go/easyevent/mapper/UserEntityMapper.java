package com.spring2go.easyevent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spring2go.easyevent.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserEntityMapper extends BaseMapper<UserEntity> {
}
