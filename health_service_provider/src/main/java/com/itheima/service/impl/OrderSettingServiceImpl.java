package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.OrderSettingDao;
import com.itheima.entity.Result;
import com.itheima.pojo.OrderSetting;
import org.apache.poi.ss.usermodel.DataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import serivce.OrderSettingService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    public void addList(List<OrderSetting> orderSettingList) {
        if (orderSettingList != null && orderSettingList.size() > 0) {
            for (OrderSetting orderSetting : orderSettingList) {
                Date orderDate = orderSetting.getOrderDate();
                //String date = new SimpleDateFormat("yyyy-MM-dd").format(orderDate);
                Long count = orderSettingDao.findCountByOrderDate(orderDate);

                if (count > 0) {
                    orderSettingDao.editNumberByOrderDate(orderSetting);
                } else {
                    orderSettingDao.add(orderSetting);
                }
            }
        }
    }

    /**
     * 根据月份查询具体预约数据
     *
     * @param date
     * @return
     */
    @Override
    public List<Map> getOrderSettingByMonth(String date) {
        String[] strs = date.split("-");
        int day = Integer.parseInt(strs[1]);
        if (day < 10) {
            strs[1] = "0" + day;
        }
        date = strs[0] + "-" + strs[1];
        List<Map> list = orderSettingDao.getDataByMonth(date);
        return list;
    }


    /**
     * 根据日期设置预约的数据
     *
     * @param orderSetting
     */
    @Override
    public void editNumberByDate(OrderSetting orderSetting) {

        Date orderDate = orderSetting.getOrderDate();
        //String date = new SimpleDateFormat("yyyy-MM-dd").format(orderDate);

        //根据时间查询记录
        long count = orderSettingDao.findCountByOrderDate(orderDate);
        //存在，更新
        if (count > 0) {
            orderSettingDao.editNumberByOrderDate(orderSetting);
        }
        //不存在。插入
        else {
            orderSettingDao.add(orderSetting);
        }

    }
}
