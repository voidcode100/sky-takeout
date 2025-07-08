package com.sky.controller.user;

import com.sky.dto.OrdersDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("userOrderController")
@Slf4j
@Api(tags = "C端-订单相关接口")
@RequestMapping("/user/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 用户提交订单
     *
     * @param ordersSubmitDTO
     * @return 订单视图对象
     */
    @PostMapping("/submit")
    @ApiOperation("用户提交订单")
    public Result<OrderSubmitVO> submitOrder(@RequestBody OrdersSubmitDTO ordersSubmitDTO){
        log.info("用户提交订单: {}", ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submitOrder(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        log.info("生成预支付交易单：{}", orderPaymentVO);
        orderService.paySuccess(ordersPaymentDTO.getOrderNumber());
        return Result.success(orderPaymentVO);
    }

    /**
     * 根据订单号查询订单详情
     *
     * @param ordersPageQueryDTO 订单
     * @return 订单视图对象
     */
    @GetMapping("/historyOrders")
    @ApiOperation("分页查询订单")
    public Result<PageResult> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO)
    {
        log.info("分页查询订单: {}", ordersPageQueryDTO);
        PageResult pageResult=orderService.pageQuery(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 根据订单ID查询订单详情
     *
     * @param id 订单ID
     * @return 订单视图对象
     */
    @GetMapping("orderDetail/{id}")
    @ApiOperation("根据订单ID查询订单详情")
    public Result<OrderVO> getById(@PathVariable Long id)
    {
        log.info("查询订单详情: {}", id);
        OrderVO orderVO=orderService.getById(id);
        return Result.success(orderVO);
    }

    /**
     * 取消订单
     *
     * @param id 订单ID
     * @return 成功结果
     */
    @PutMapping("/cancel/{id}")
    @ApiOperation("取消订单")
    public Result cancelOrder(@PathVariable Long id){
        log.info("用户取消订单: {}", id);
        orderService.cancelOrder(id);
        return Result.success();

    }

    /**
     * 再来一单
     *
     * @param id 订单ID
     * @return 成功结果
     */
    @PostMapping("//repetition/{id}")
    @ApiOperation("再来一单")
    public Result repetitionOrder(@PathVariable Long id){
        log.info("再来一单: {}", id);
        orderService.repetitionOrder(id);
        return Result.success();
    }


}
