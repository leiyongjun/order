package com.bdqn.order.service;

import com.bdqn.order.pojo.Goods;
import com.bdqn.order.pojo.GoodsInfo;

import java.util.List;
import java.util.Map;

public interface GoodsService {
     List<Goods> getGoodsList(Goods goods);

     GoodsInfo getGoodsById(Integer id);

     int getAllCount(Goods goods);

     Map getGoodsList(GoodsInfo goodsinfo);

     /**
      * 修改商品库存
      * @param goodsId
      * @param count
      * @return
      */
     int updateGoodsStock(Integer goodsId, Integer count);
}
