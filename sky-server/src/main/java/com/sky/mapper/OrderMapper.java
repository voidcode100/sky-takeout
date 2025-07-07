package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Controller;

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
}
