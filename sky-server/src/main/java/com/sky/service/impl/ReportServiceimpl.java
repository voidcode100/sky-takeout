package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.OrderService;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportServiceimpl implements ReportService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrderService orderService;

    /**
     * 获取订单的营业额统计
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return 营业额统计视图对象
     */
    @Override
    public TurnoverReportVO getTurnOverStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> localDates = new ArrayList<>();
        // 获取开始日期和结束日期之间的所有日期
        localDates.add(begin);
        while(!begin.equals(end)){
            begin=begin.plusDays(1);
            localDates.add(begin);
        }

        //获取每一天的营业额
        List<Double> turnoverList = new ArrayList<>();
        for (LocalDate localDate : localDates) {
            //将LocalDate转换为LocalDateTime
            LocalDateTime localDateTimeMIN = LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime localDateTimeMAX = LocalDateTime.of(localDate, LocalTime.MAX);

            //查询订单表，获取当天的营业额
            HashMap hashMap = new HashMap();
            hashMap.put("begin", localDateTimeMIN);
            hashMap.put("end", localDateTimeMAX);
            hashMap.put("status", Orders.COMPLETED);
            Double turnover = orderMapper.sumByMap(hashMap);
            turnover=turnover==null ? 0.0 : turnover;
            turnoverList.add(turnover);

        }

        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(localDates,","))
                .turnoverList(StringUtils.join(turnoverList,","))
                .build();
    }

    /**
     * 获取用户统计数据
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return 用户统计视图对象
     */
    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> localDates = new ArrayList<>();
        //获取从开始到结束日期之间的所有日期
        localDates.add(begin);
        while(!begin.equals(end))
        {
            begin=begin.plusDays(1);
            localDates.add(begin);
        }

        //获取每一天的新增用户数
        List<Integer> addUserList = new ArrayList<>();
        //获取每一天用户总数
        List<Integer> totalUserList = new ArrayList<>();

        for (LocalDate localDate : localDates) {
            //将LocalDate转换为LocalDateTime
            LocalDateTime localDateTimeMIN = LocalDateTime.of(localDate,LocalTime.MIN);
            LocalDateTime localDateTimeMAX = LocalDateTime.of(localDate,LocalTime.MAX);

            //查询用户表，获取当天的新增用户数
            HashMap hashMap = new HashMap();
            hashMap.put("end",localDateTimeMAX);

            Integer totalUser = userMapper.countByMap(hashMap);

            hashMap.put("begin",localDateTimeMIN);
            Integer addUser = userMapper.countByMap(hashMap);
            addUserList.add(addUser);
            totalUserList.add(totalUser);
        }

        return UserReportVO.builder()
                .dateList(StringUtils.join(localDates,","))
                .totalUserList(StringUtils.join(totalUserList,","))
                .newUserList(StringUtils.join(addUserList,","))
                .build();

    }

    /**
     * 获取订单统计数据
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return 订单统计视图对象
     */
    @Override
    public OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> localDates = new ArrayList<>();
        //获取从开始到结束日期之间的所有日期
        localDates.add(begin);
        while(!begin.equals(end))
        {
            begin=begin.plusDays(1);
            localDates.add(begin);
        }

        //获取每一天的订单数
        List<Integer> orderCountList = new ArrayList<>();
        //获取每一天的有效订单数
        List<Integer> validOrderCountList = new ArrayList<>();

        for (LocalDate localDate : localDates) {
            LocalDateTime localDateTimeMIN = LocalDateTime.of(localDate,LocalTime.MIN);
            LocalDateTime localDateTimeMAX = LocalDateTime.of(localDate,LocalTime.MAX);

            HashMap hashMap = new HashMap();
            hashMap.put("begin", localDateTimeMIN);
            hashMap.put("end", localDateTimeMAX);

            //查询订单表，获取当天的订单数
            Integer orderCount = orderMapper.countByMap(hashMap);

            hashMap.put("status",Orders.COMPLETED);
            //查询订单表，获取当天的有效订单数
            Integer validOrderCount = orderMapper.countByMap(hashMap);

            orderCountList.add(orderCount);
            validOrderCountList.add(validOrderCount);
        }

        //计算总订单数
        Integer totalOrderCount = orderCountList.stream().reduce(Integer::sum).get();
        //计算总有效订单数
        Integer totalValidOrderCount = validOrderCountList.stream().reduce(Integer::sum).get();
        //计算完成率
        Double orderCompleteRate=0.0;
        if(totalOrderCount!=0)
        {
            orderCompleteRate = (double) totalValidOrderCount / totalOrderCount;
        }


        return OrderReportVO.builder()
                .dateList(StringUtils.join(localDates,","))
                .orderCountList(StringUtils.join(orderCountList,","))
                .validOrderCountList(StringUtils.join(validOrderCountList,","))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(totalValidOrderCount)
                .orderCompletionRate(orderCompleteRate)
                .build();

    }

    /**
     * 获取热销商品Top10统计数据
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return 热销商品Top10统计视图对象
     */
    @Override
    public SalesTop10ReportVO getSaleTop10(LocalDate begin, LocalDate end) {
        LocalDateTime localDateTimeMIN = LocalDateTime.of(begin,LocalTime.MIN);
        LocalDateTime localDateTimeMAX = LocalDateTime.of(end,LocalTime.MAX);

        //查询订单表，获取热销商品Top10
        List<GoodsSalesDTO> saleTop10 = orderMapper.getSaleTop10(localDateTimeMIN, localDateTimeMAX);

        List<String> nameList = saleTop10.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        List<Integer> numerList = saleTop10.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());


        return SalesTop10ReportVO.builder()
                .nameList(StringUtils.join(nameList,","))
                .numberList(StringUtils.join(numerList,","))
                .build();
    }
}
