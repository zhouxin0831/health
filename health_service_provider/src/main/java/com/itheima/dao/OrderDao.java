package com.itheima.dao;

import com.itheima.pojo.Order;

import java.util.List;
import java.util.Map;

public interface OrderDao {
    List<Order> findByCondition(Order order);
    //新增预约信息
    void add(Order order);

    Map findById4Detail(Integer id);


}
