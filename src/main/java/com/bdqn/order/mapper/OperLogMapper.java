package com.bdqn.order.mapper;

import com.bdqn.order.pojo.OperLog;

public interface OperLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OperLog record);

    int insertSelective(OperLog record);

    OperLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OperLog record);

    int updateByPrimaryKey(OperLog record);
}