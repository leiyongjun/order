package com.bdqn.order.service.impl;

import com.bdqn.order.mapper.UserInfoMapper;
import com.bdqn.order.pojo.UserInfo;
import com.bdqn.order.service.UserService;
import com.bdqn.order.util.Md5Utils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    public UserInfoMapper userInfoMapper;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Override
    public Map doLogin(UserInfo userInfo) {
        Map map = new HashMap();
        map.put("retCode", "1000");
        map.put("retMsg","登陆成功");
        // 对图形验证码进行校验

        // 图片验证码校验通过,执行shiro的密码校验
        Subject subject= SecurityUtils.getSubject();
        try {
            subject.login(new UsernamePasswordToken(userInfo.getUserName(), Md5Utils.hash(userInfo.getUserPwd())));
            map.put("user", subject.getPrincipal());

            rabbitTemplate.convertAndSend("OperLogExchange","OperLogRouting", map);
        }catch (UnknownAccountException un){
            map.put("retCode", "901");
            map.put("retMsg","用户不存在");
        }catch (IncorrectCredentialsException in){
            map.put("retCode", "902");
            map.put("retMsg","密码错误");
        }

        return map;
    }

    public UserInfo getUserInfoByName(String userName){
        return userInfoMapper.selectByName(userName);
    }

    public UserInfo getUserInfoById(Integer userId){
        return userInfoMapper.selectByPrimaryKey(userId);
    }

    public int updateBalance(Integer userId, double amt){
        return userInfoMapper.updateBalance(userId, amt);
    }

}
