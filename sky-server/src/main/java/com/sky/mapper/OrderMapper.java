package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Mapper
public interface OrderMapper {
    /**
     * 插入一条订单记录
     *
     * @param orders 订单信息
     */

    void insert(Orders orders);


    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    /**
     * 分页查询订单
     *
     * @param ordersPageQueryDTO 分页查询参数
     * @return 分页结果
     */
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据订单ID查询订单详情
     *
     * @param id 订单ID
     * @return 订单视图对象
     */
    @Select("select * from orders where id= #{id}")
    Orders getById(long id);

    @Select("select count(*) from orders where status = #{confirmed}")
    Integer countByStatus(Integer confirmed);

    /**
     * 根据订单状态和下单时间查询订单
     *
     * @param status,time 订单号
     */
    @Select("select * from orders where status=#{status} and order_time < #{time}")
    List<Orders> getByStausAndOrderTime(Integer status, LocalDateTime time);

    /**
     * 根据订单状态和下单时间统计订单营业额
     *
     * @param hashMap 订单号
     */
    Double sumByMap(HashMap hashMap);
}
