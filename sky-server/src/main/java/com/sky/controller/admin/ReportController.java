package com.sky.controller.admin;

import com.sky.mapper.OrderMapper;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@Api("B端-报表相关接口")
@Slf4j
@RequestMapping("/admin/report")
public class ReportController {

    @Autowired
    private ReportService reportService;
    /**
     * 获取营业额统计数据
     * @param begin 开始日期
     * @param end 结束日期
     * @return 订单统计视图对象
     */
    @GetMapping("/turnoverStatistics")
    public Result<TurnoverReportVO> getTurnOverStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                                          @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("营业额统计表: begin={}, end={}", begin, end);
        TurnoverReportVO turnoverReportVo = reportService.getTurnOverStatistics(begin,end);
        return Result.success(turnoverReportVo);

    }

    /**
     * 获取用户统计数据
     * @param begin 开始日期
     * @param end 结束日期
     * @return 用户统计视图对象
     */
    @GetMapping("/userStatistics")
    @ApiOperation("获取用户统计数据")
    public Result<UserReportVO> getUserStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                                  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("用户统计表: begin={}, end={}", begin, end);
        UserReportVO userReportVO = reportService.getUserStatistics(begin,end);
        return Result.success(userReportVO);

    }

    /**
     * 获取订单统计数据
     * @param begin 开始日期
     * @param end 结束日期
     * @return 用户统计视图对象
     */
    @GetMapping("/ordersStatistics")
    @ApiOperation("获取用户统计数据")
    public Result<OrderReportVO> getOrderStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                                   @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("用户统计表: begin={}, end={}", begin, end);
        OrderReportVO orderReportVO = reportService.getOrderStatistics(begin,end);
        return Result.success(orderReportVO);

    }


}
