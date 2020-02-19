package com.bdqn.order.mapper;

import com.bdqn.order.pojo.UserInfo;

import java.math.BigDecimal;

public interface UserInfoMapper {
    int deleteByPrimaryKey(Integer userId);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    UserInfo selectByPrimaryKey(Integer userId);
    UserInfo selectByName(String userName);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);

    public int updateBalance(Integer userId, double amt);
}