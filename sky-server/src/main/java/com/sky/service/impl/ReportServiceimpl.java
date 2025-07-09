package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.OrderService;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
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

    @Autowired
    private WorkspaceService workspaceService;

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

    /**
     * 导出报表
     *
     * @param response HttpServletResponse对象，用于设置响应头和输出流
     */
    @Override
    public void export(HttpServletResponse response) {
        //获取近三十天数据
        LocalDate begin = LocalDate.now().minusDays(30);
        LocalDate end = LocalDate.now().minusDays(1);

        BusinessDataVO businessData = workspaceService.getBusinessData(LocalDateTime.of(begin, LocalTime.MIN),
                LocalDateTime.of(end, LocalTime.MAX));
        //获取模板输入流
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("template/excelModel.xlsx");

        //使用POI写入excel
        XSSFWorkbook excel = null;
        try {
            excel = new XSSFWorkbook(inputStream);
            //设置概览数据
            XSSFSheet sheet = excel.getSheet("Sheet1");
            //设置第二行
            sheet.getRow(1).getCell(1).setCellValue("时间从 "+begin+"到 "+end);
            //设置第4行
            XSSFRow row = sheet.getRow(3);
            row.getCell(2).setCellValue(businessData.getTurnover());
            row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
            row.getCell(6).setCellValue(businessData.getNewUsers());

            //设置第5行
            row = sheet.getRow(4);
            row.getCell(2).setCellValue(businessData.getValidOrderCount());
            row.getCell(4).setCellValue(businessData.getUnitPrice());

            //设置每日明细数据
            for(int i=0;i<30;i++)
            {
                LocalDate date = begin.plusDays(i);

                //获取每日营业额
                BusinessDataVO businessVO = workspaceService.getBusinessData(LocalDateTime.of(date, LocalTime.MIN),
                        LocalDateTime.of(date, LocalTime.MAX));

                //设置第7行开始的每日数据
                Integer r = i+7;
                row = sheet.getRow(r);
                row.getCell(1).setCellValue(date.toString());
                row.getCell(2).setCellValue(businessVO.getTurnover());
                row.getCell(3).setCellValue(businessVO.getValidOrderCount());
                row.getCell(4).setCellValue(businessVO.getOrderCompletionRate());
                row.getCell(5).setCellValue(businessVO.getUnitPrice());
                row.getCell(6).setCellValue(businessVO.getNewUsers());
            }

            //将内存中的excel写入到响应输出流
            //获得响应的输出流
            ServletOutputStream outputStream = response.getOutputStream();
            excel.write(outputStream);

            //关闭资源
            inputStream.close();
            outputStream.close();
            excel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
