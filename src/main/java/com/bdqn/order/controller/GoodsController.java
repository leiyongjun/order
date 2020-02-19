package com.bdqn.order.controller;

import com.bdqn.order.pojo.*;
import com.bdqn.order.service.GoodsService;
import com.bdqn.order.service.LoginService;
import com.bdqn.order.service.OrderService;
import com.bdqn.order.service.PayService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/order/product")
public class GoodsController {

    @Resource
    private GoodsService goodsService;

    @Resource
    private OrderService orderService;

    @Resource
    private LoginService loginService;

    @Resource
    private PayService payService;

    @RequestMapping("/list")
    public Object selectAllGoods(GoodsInfo goodsInfo){

        Map map  = goodsService.getGoodsList(goodsInfo);

        return map;
    }

    @GetMapping(value = "/{id}")
    public Object getInfo(@PathVariable Integer id)
    {
        GoodsInfo goods=goodsService.getGoodsById(id);
        Map map=new HashMap();
        map.put("info",goods.getGoodsInfo());
        return map;
    }

    @RequestMapping(value = "/addOrder/{goodsId}")
    public Object addOrder(@PathVariable Integer goodsId,HttpSession session){
//        Map map = orderService.addOrder(goodsId);
//        if(map.get("retCode").toString().equals("1000")){
//            map.put("result","success");
//        }
        return orderService.addOrder(goodsId);
    }

    @RequestMapping("/allOrder")
    public Object selectAllOrder(HttpSession session){
        Map<String,Object> map=new HashMap<>();
        Integer id=(Integer) session.getAttribute("loginUser");
        List<Order> orderList=orderService.selectOrderByUserId(id);
        map.put("orderList",orderList);
        return map;
    }

    @RequestMapping(value = "/pay/{id}")
    public Object payOrder(@PathVariable Integer id,HttpSession session){

        return orderService.pay(id);
    }
}
