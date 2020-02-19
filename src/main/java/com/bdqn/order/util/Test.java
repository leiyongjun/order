package com.bdqn.order.util;

import com.bdqn.order.pojo.Goods;
import org.springframework.beans.BeanUtils;

import java.util.Vector;

public class Test extends Object {

    public static void main(String[] args) throws InterruptedException {
        Goods g1 = new Goods();
        g1.setGoodsId(123);
        g1.setGoodsName("方天画戟");

        Goods g2 = g1;
        //BeanUtils.copyProperties(g1, g2);
        g1.setGoodsName("瓶泰国");

        System.out.println(g1.getClass());
        System.out.println(g1.hashCode());
        System.out.println(g2.hashCode());

        Thread.currentThread().sleep(2000);

        System.out.println(g2.getGoodsName());

        Vector v = new Vector();
        v.add(g1);
        v.add(g2);

    }

}
