package com.bdqn.order.service;

import com.bdqn.order.pojo.UserInfo;

import java.math.BigDecimal;
import java.util.Map;

public interface UserService {

    /**
     * 用户登陆
     * @param userInfo
     * @return
     */
    public Map doLogin(UserInfo userInfo);

    public UserInfo getUserInfoByName(String userName);

    public UserInfo getUserInfoById(Integer userName);

    public int updateBalance(Integer userId, double balance);
}
