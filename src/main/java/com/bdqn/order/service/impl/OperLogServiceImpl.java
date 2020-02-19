package com.bdqn.order.service.impl;

import com.bdqn.order.mapper.OperLogMapper;
import com.bdqn.order.pojo.OperLog;
import com.bdqn.order.pojo.UserInfo;
import com.bdqn.order.service.OperLogService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

@Service
@RabbitListener(queues = "operLogQueue")
public class OperLogServiceImpl implements OperLogService {

    @Resource
    OperLogMapper operLogMapper;

    @Override
    public int addOperLog(OperLog log) {
        return operLogMapper.insertSelective(log);
    }

    @RabbitHandler
    public void addMqOperLog(Map map) {
        UserInfo u = (UserInfo)map.get("user");

        OperLog log = new OperLog();
        log.setOprType("登录");
        log.setOprName(u.getUserName() + "进行登陆");
        log.setCreateTime(new Date());

        operLogMapper.insertSelective(log);
    }
}
