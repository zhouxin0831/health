package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import serivce.OrderService;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;


    @Override
    public Result order(Map map) throws Exception {
        String orderDate = (String) map.get("orderDate");//预约日期
        //检查用户预约的日期是否可以进行预约，如果没有设置预约，则无法完成预约
        //Date order_Date = DateUtils.parseString2Date((String) map.get("orderDate"));//预约日期
        OrderSetting orderSetting= orderSettingDao.findByOrderDate(DateUtils.parseString2Date(orderDate));
        if(orderSetting==null){
            //预约日期无法进行预约
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //预约日期可以进行预约
        //检查用户预约的日期预约人数是否已经达到限制人数，如果达到，则无法完成预约

        int number = orderSetting.getNumber();//获取可预约人数
        int reservations = orderSetting.getReservations();//获取已预约人数
        if(reservations>=number){
            //已预约人数大于可预约人数，预约人数已满，无法预约
            return new Result(false,MessageConstant.ORDER_FULL);
        }

        //检查用户是否在当天已经进行过预约，如已预约过，则无法完成预约
        //检查用户是否为会员，为若为会员直接完成预约，如不是，则先注册成会员再进行预约
        String telephone = (String) map.get("telephone");
        Member member=memberDao.findByTelephone(telephone);
        if(member!=null){
            Integer memberId = member.getId();
            //String setmealId = map.get("setmealId");
            String setmealId = (String) map.get("setmealId");
            Date order_Date = DateUtils.parseString2Date(orderDate);//预约日期
            Order order = new Order(memberId,order_Date,null,null,Integer.parseInt(setmealId));
            List<Order> list=orderDao.findByCondition(order);
            if(list!=null && list.size()>0){
                //不能重复预约
                return new Result(false,MessageConstant.HAS_ORDERED);
            }
            //可以预约
            orderSetting.setReservations(orderSetting.getReservations()+1);
            orderSettingDao.editReservationsByOrderDate(orderSetting);



        }
        //不是会员
        if(member==null){
            //新增会员信息
            member=new Member();
            member.setName((String) map.get("name"));
            member.setSex((String) map.get("sex"));
            member.setPhoneNumber(telephone);
            member.setIdCard((String) map.get("idCard"));
            member.setRegTime((Date) map.get("setRegTime"));
            member.setRegTime(new Date());
            memberDao.add(member);

        }
        //保存预约信息到预约表
        /*Order order = new Order(member.getId(),orderDate,(String)map.get("orderType"),Order.ORDERSTATUS_NO,Integer.parseInt((String)map.get("setmealId")));
        orderDao.add(order);

        return new Result(true,MessageConstant.ORDER_SUCCESS,order.getId());*/
        Order order = new Order();
        order.setMemberId(member.getId());//设置会员ID
        order.setOrderDate(DateUtils.parseString2Date(orderDate));//预约日期
        order.setOrderType((String) map.get("orderType"));//预约类型
        order.setOrderStatus(Order.ORDERSTATUS_NO);//到诊状态
        order.setSetmealId(Integer.parseInt((String) map.get("setmealId")));//套餐ID
        orderDao.add(order);

        orderSetting.setReservations(orderSetting.getReservations() + 1);//设置已预约人数+1
        orderSettingDao.editReservationsByOrderDate(orderSetting);

        return new Result(true,MessageConstant.ORDER_SUCCESS,order.getId());
    }
    //根据预约ID查询预约相关信息（体检人姓名、预约日期、套餐名称、预约类型）
    public Map findById(Integer id) throws Exception{
        Map map=orderDao.findById4Detail(id);
        if(map != null){
            //处理日期格式
            Date orderDate = (Date) map.get("orderDate");
            map.put("orderDate",DateUtils.parseDate2String(orderDate));
        }
        return map;
    }
}
