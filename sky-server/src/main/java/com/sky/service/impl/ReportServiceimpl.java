package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
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

@Service
@Slf4j
public class ReportServiceimpl implements ReportService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;
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
}
