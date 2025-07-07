package com.sky.controller.admin;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("adminOrderController")
@Slf4j
@Api("B端-订单相关接口")
@RequestMapping("/admin/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 分页查询订单
     * @param ordersPageQueryDTO
     * @return
     */
    @GetMapping("/conditionSearch")
    @ApiOperation("分页查询订单")
    public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO){
        log.info("分页查询订单: {}", ordersPageQueryDTO);
         PageResult pageResult = orderService.conditionSearch(ordersPageQueryDTO);
         return Result.success(pageResult);
    }

    /**
     * 订单统计
     * @return 订单视图对象
     */
    @GetMapping("/statistics")
    @ApiOperation("订单统计")
    public Result<OrderStatisticsVO> statistics(){
        log.info("订单统计");
        OrderStatisticsVO orderStatisticsVO = orderService.statistics();
        return Result.success(orderStatisticsVO);
    }

    /**
     * 根据订单ID查询订单详情
     *
     * @param id 订单ID
     * @return 订单视图对象
     */
    @GetMapping("details/{id}")
    @ApiOperation("根据订单ID查询订单详情")
    public Result<OrderVO> getById(@PathVariable long id)
    {
        log.info("查询订单详情: {}", id);
        OrderVO orderVO=orderService.getById(id);
        return Result.success(orderVO);
    }

    /**
     * 根据订单号接单
     *
     * @param ordersDTO 订单号
     * @return 成功结果
     */
    @PutMapping("/confirm")
    @ApiOperation("接单")
    public Result confirmOrder(@RequestBody OrdersDTO ordersDTO){
        log.info("接单: {}", ordersDTO);
        orderService.confirmOrder(ordersDTO.getId());
        return Result.success();
    }

    /**
     * 根据订单号拒单
     *
     * @param ordersRejectionDTO 订单号
     * @return 成功结果
     */
    @PutMapping("/rejection")
    @ApiOperation("接单")
    public Result rejectionOrder(@RequestBody OrdersRejectionDTO ordersRejectionDTO){
        log.info("拒单: {}", ordersRejectionDTO);
        orderService.rejectionOrder(ordersRejectionDTO);
        return Result.success();
    }

    /**
     * 取消订单
     * @param ordersCancelDTO
     * @return
     */
    @PutMapping("/cancel")
    @ApiOperation("取消订单")
    public Result cancelOrder(@RequestBody OrdersCancelDTO ordersCancelDTO){
        log.info("商家取消订单: {}", ordersCancelDTO);
        orderService.adminCancelOrder(ordersCancelDTO);
        return Result.success();
    }

    /**
     * 商家发货
     *
     * @param id 订单ID
     * @return 成功结果
     */
    @PutMapping("/delivery/{id}")
    @ApiOperation("商家发货")
    public Result deliveryOrder(@PathVariable Long id){
        log.info("商家发货: {}", id);
        orderService.deliveryOrder(id);
        return Result.success();
    }

    /**
     * 完成订单
     *
     * @param id 订单ID
     * @return 成功结果
     */
    @PutMapping("complete/{id}")
    @ApiOperation("完成订单")
    public Result completeOrder(@PathVariable Long id){
        log.info("完成订单: {}",id);
        orderService.completeOrder(id);
        return Result.success();
    }
}
