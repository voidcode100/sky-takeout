package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
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

    /**
     * 根据订单ID查询订单详情
     *
     * @param orderId 订单ID
     * @return 订单详情列表
     */
    @Select("select * from order_detail where order_id = #{id}")
    List<OrderDetail> getByOrderId(Long orderId);
}
