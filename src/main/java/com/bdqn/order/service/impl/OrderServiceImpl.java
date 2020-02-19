package com.bdqn.order.service.impl;

import com.bdqn.order.mapper.OrderMapper;
import com.bdqn.order.pojo.*;
import com.bdqn.order.service.GoodsService;
import com.bdqn.order.service.OrderService;
import com.bdqn.order.service.PayService;
import com.bdqn.order.service.UserService;
import com.bdqn.order.util.redis.RedisUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    GoodsService goodsService;
    @Resource
    UserService userService;
    @Resource
    PayService payService;
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private RedisUtil redisUtil;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Map addOrder(Integer goodsId) {
        Map<String,Object> map=new HashMap<>();
        map.put("retCode","500");
        map.put("retMsg","系统错误,请稍后重试");

        Boolean getLock = redisUtil.getLock(goodsId+"_addOrder", 10 * 1000);
        while(!getLock){
            System.out.println("当前商品有其他人在排队购买,继续等待...");
            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {

            }
            getLock = redisUtil.getLock(goodsId+"_addOrder", 10 * 1000);
            if(getLock){
                System.out.println("获得锁,继续执行");
            }
        }

        // 查询商品 库存校验,扣减库存
        // 数据的脏读脏写在商品实时库存提现的尤为突出
        // 使用锁来解决并发请求时的数据安全问题
        // 使用redis来做锁
        int result = goodsService.updateGoodsStock(goodsId, (-1));
        if(result <= 0){
            map.put("retCode","801");
            map.put("retMsg","商品已售罄,请下次购买");
            return map;
        }

        GoodsInfo goods =goodsService.getGoodsById(goodsId);

        // 从shiro中取出用户数据
        UserInfo userInfo = (UserInfo) SecurityUtils.getSubject().getPrincipal();

        Order order2=new Order();
        order2.setGoodsId(goodsId);
        order2.setOrderPrice(goods.getGoodsPrice());
        order2.setUserId(userInfo.getUserId());
        order2.setOrderStatus("未支付");
        // 添加订单
        if(orderMapper.insertSelective(order2) > 0){
            // 释放redis锁
            redisUtil.releaseLock(goodsId+"_addOrder");
            System.out.println("下单完成,释放锁...");
            map.put("retCode","1000");
            map.put("retMsg","下单成功");
            return map;
        }
        return map;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Map pay(Integer orderId){
        Map map = new HashMap();
        UserInfo userInfo =(UserInfo)SecurityUtils.getSubject().getPrincipal();
        // 校验订单状态
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if(!order.getOrderStatus().equals("未支付")){
            map.put("retCode","802");
            map.put("retMsg","订单状态异常,请检查!");
            return map;
        }
        // 校验用户余额, 扣减用户账户余额
        UserInfo userInfo1 = userService.getUserInfoById(userInfo.getUserId());
        if(userService.updateBalance(userInfo1.getUserId(), order.getOrderPrice().doubleValue()) < 0){
            map.put("retCode","803");
            map.put("retMsg","您的余额不足,请充值");
            return map;
        }
        // 修改订单状态
        Order orderBeUpdate = new Order();
        orderBeUpdate.setOrderId(orderId);
        orderBeUpdate.setOrderStatus("已支付");
        orderMapper.updateByPrimaryKeySelective(orderBeUpdate);
        // 添加支付记录
        Pay pay = new Pay();
        pay.setOrderId(orderId);
        pay.setPayBefore(userInfo1.getUserBalance());
        pay.setPayAfter(userInfo1.getUserBalance().subtract(order.getOrderPrice()));
        pay.setUserId(userInfo.getUserId());
        pay.setPayTime(new Date());
        payService.addPayInfo(pay);

        map.put("retCode","1000");
        map.put("retMsg","支付成功");
        return map;
    }

    @Override
    public List<Order> selectOrderByUserId(Integer id) {
        return orderMapper.selectAllOrder(id);
    }

    @Override
    public Order selectOrderById(Integer id) {
        return orderMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateOrder(Order order) {
        return orderMapper.updateByPrimaryKeySelective(order);
    }

    @Override
    public int cancelOrder() {
        return orderMapper.cancelOrder();
    }
}
