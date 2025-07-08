package com.sky.controller.admin;

import com.sky.mapper.OrderMapper;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.TurnoverReportVO;
import io.swagger.annotations.Api;
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
    private OrderService orderService;
    /**
     * 获取订单统计数据
     * @param begin 开始日期
     * @param end 结束日期
     * @return 订单统计视图对象
     */
    @GetMapping("/turnoverStatistics")
    public Result<TurnoverReportVO> getTurnOverStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                                          @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("订单统计表: begin={}, end={}", begin, end);
        TurnoverReportVO turnoverReportVo = orderService.getTurnOverStatistics(begin,end);
        return Result.success(turnoverReportVo);

    }

}
