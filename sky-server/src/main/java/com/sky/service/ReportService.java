package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import java.time.LocalDate;

public interface ReportService {
    /**
     * 获取营业额统计数据
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return 订单统计视图对象
     */
    TurnoverReportVO getTurnOverStatistics(LocalDate begin, LocalDate end);

    /**
     * 获取用户统计数据
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return 用户统计视图对象
     */
    UserReportVO getUserStatistics(LocalDate begin, LocalDate end);

    /**
     * 获取订单统计数据
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return 订单统计视图对象
     */
    OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end);

    /**
     * 获取热销商品Top10统计数据
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return 热销商品Top10统计视图对象
     */
    SalesTop10ReportVO getSaleTop10(LocalDate begin, LocalDate end);
}
