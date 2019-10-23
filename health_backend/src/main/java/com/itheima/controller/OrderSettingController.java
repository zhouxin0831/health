package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.OrderSetting;

import com.itheima.util.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import serivce.OrderSettingService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {
    @Reference
    private OrderSettingService orderSettingService;

    //文件上传，实现预约设置数据批量导入
    @RequestMapping("/upload")
    public Result upload(@RequestParam("excelFile") MultipartFile excelFile) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            List<String[]> strings = POIUtils.readExcel(excelFile.getOriginalFilename(), excelFile.getInputStream());
            List<OrderSetting> orderSettingList = new ArrayList<>();
            for (String[] string : strings) {
                OrderSetting orderSetting = new OrderSetting();
                orderSetting.setOrderDate(format.parse(string[0]));
                orderSetting.setNumber(Integer.parseInt(string[1]));
                orderSettingList.add(orderSetting);
            }
            orderSettingService.addList(orderSettingList);
            return new Result(true,MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }

    }


    //根据月份查询对应的预约设置数据
    @RequestMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String date) {//date格式为：yyyy-MM
        try {
            List<Map> list = orderSettingService.getOrderSettingByMonth(date);
            return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }

    //根据日期设置对应的预约设置数据
    @RequestMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting) {

        try {

            orderSettingService.editNumberByDate(orderSetting);
            return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDERSETTING_FAIL);
        }
    }
}
