package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Controller;

import java.util.List;

@Mapper
public interface OrderDetailMapper {
    /**
     * 批量插入订单详情
     *
     * @param orderDetails 订单详情列表
     */
    void insertBatch(List<OrderDetail> orderDetails);
}
