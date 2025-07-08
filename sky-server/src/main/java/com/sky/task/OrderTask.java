package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {
    @Autowired
    private OrderMapper orderMapper;
    /**
     * 定时处理超时订单（15min)
     * 每分钟执行一次
     */
    @Scheduled(cron = "0 * * * * ?") // 每分钟执行一次
    public void processTimeOutOrders(){
        log.info("定时处理超时订单: {}", LocalDateTime.now());
        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);
        List<Orders> list = orderMapper.getByStausAndOrderTime(Orders.PENDING_PAYMENT,time);
        for (Orders orders : list) {
            orders.setStatus(Orders.CANCELLED);
            orders.setCancelReason("订单超时，自动取消");
            orders.setCancelTime(LocalDateTime.now());
            orderMapper.update(orders);
        }

    }

    /**
     * 定时处理派送订单
     * 每天执行一次
     */
    @Scheduled(cron = "0 0 1 * * ?") // 每天凌晨1点执行
    public void processDeliveryOrders(){
        log.info("定时处理派送订单: {}", LocalDateTime.now());
        LocalDateTime time=LocalDateTime.now().plusMinutes(-60);
        List<Orders> list = orderMapper.getByStausAndOrderTime(Orders.DELIVERY_IN_PROGRESS,time);
        if(list.size()>0 && list!= null){
            for (Orders orders : list) {
                orders.setStatus(Orders.CONFIRMED);
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders);
            }
        }

    }
}
