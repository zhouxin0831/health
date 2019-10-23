package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.util.HealthSmsUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;
import serivce.OrderService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private JedisPool jedisPool;
    @Reference
    private OrderService orderService;

    @RequestMapping("/submit")
    public Result submitOrder(@RequestBody Map map) {
        String telephone = (String) map.get("telephone");
        //根据手机号码从redis缓存中取出验证码
        String codeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        String validateCode = (String) map.get("validateCode");
        //判断验证码
        if (codeInRedis == null || !codeInRedis.equals(validateCode)) {
            //缓存中不存在验证码或者缓存中验证码与前台传过来的验证码不等
            //验证失败
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //验证成功
        //调用预约
        //将申请方式添加进map
        Result result = null;
        try {
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            result = orderService.order(map);

        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }
        if (result.isFlag()) {
            //预约成功，发送短信
            map.put("validateCode",validateCode);
            String jsonCode = JSON.toJSONString(map);
            HealthSmsUtils.sendSms(telephone, HealthSmsUtils.VALIDATE_CODE, jsonCode, HealthSmsUtils.SIGN_NAME);

        }


        return result;


    }


    //根据预约ID查询预约相关信息
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Map map=orderService.findById(id);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
