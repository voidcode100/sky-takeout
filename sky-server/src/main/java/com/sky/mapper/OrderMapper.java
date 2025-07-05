package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Controller;

@Mapper
public interface OrderMapper {
    /**
     * 插入一条订单记录
     *
     * @param orders 订单信息
     */

    void insert(Orders orders);
}
