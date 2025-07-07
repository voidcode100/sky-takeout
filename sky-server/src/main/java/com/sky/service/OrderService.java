package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrderService {
    /**
     * 用户提交订单
     *
     * @param ordersSubmitDTODTO 订单数据传输对象
     * @return 订单视图对象
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTODTO);

    /**
     * 根据订单号查询订单详情
     *
     * @param ordersPageQueryDTO 订单
     * @return 分页结果对象
     */
    PageResult pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据订单ID查询订单详情
     *
     * @param id 订单ID
     * @return 订单视图对象
     */
    OrderVO getById(long id);

    /**
     * 根据订单号取消订单
     *
     * @param id 订单号
     * @return 订单数据传输对象
     */
    void cancelOrder(Long id);

    /**
     * 根据订单号重复下单
     *
     * @param id 订单号
     */
    void repetitionOrder(Long id);

    /**
     * 管理端分页查询订单
     *
     * @param ordersPageQueryDTO 订单分页查询数据传输对象
     * @return 分页结果对象
     */
    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 订单统计
     * @return
     */
    OrderStatisticsVO statistics();

    /**
     * 接单
     *
     * @param id 订单ID
     */
    void confirmOrder(Long id);

    /**
     * 拒单
     *
     * @param ordersRejectionDTO 订单ID
     */
    void rejectionOrder(OrdersRejectionDTO ordersRejectionDTO);

    /**
     * 管理端取消订单
     *
     * @param ordersCancelDTO 订单取消数据传输对象
     */
    void adminCancelOrder(OrdersCancelDTO ordersCancelDTO);

    /**
     * 管理端发货
     *
     * @param id 订单ID
     */
    void deliveryOrder(Long id);

    /**
     * 管理端完成订单
     *
     * @param id 订单ID
     */
    void completeOrder(Long id);
}
