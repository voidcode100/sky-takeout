package com.sky.service;

import com.sky.dto.OrdersDTO;
import com.sky.dto.OrdersSubmitDTO;
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
}
