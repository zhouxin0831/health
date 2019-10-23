package com.itheima.dao;

import com.itheima.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {


    void editNumberByOrderDate(OrderSetting orderSetting);

    void add(OrderSetting orderSetting);


    List<Map> getDataByMonth(String date);

    Long findCountByOrderDate(Date date);

    //通过日期查找预约设置
    OrderSetting findByOrderDate(Date orderDate);

    //修改预约人数
    void editReservationsByOrderDate(OrderSetting orderSetting);


}
