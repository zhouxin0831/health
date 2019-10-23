package com.itheima.controller;

import com.alibaba.fastjson.JSON;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.util.HealthSmsUtils;
import com.itheima.util.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    @Autowired
    private JedisPool jedisPool;

    //生成4位注册验证码
    @RequestMapping("/send4Order")
    public Result send4Order(String telephone){
        //随机生成4位验证码
        Integer code = ValidateCodeUtils.generateValidateCode(4);
        Map map = new HashMap<>();
        map.put("code",code);
        String jsonCode = JSON.toJSONString(map);
        //发送短信验证码
        HealthSmsUtils.sendSms(telephone,HealthSmsUtils.VALIDATE_CODE,jsonCode,HealthSmsUtils.SIGN_NAME);
        //验证码存入redis
        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_ORDER,30*24*60*5*60,code.toString());
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    //手机快捷登录6位验证码
    @RequestMapping("/send6Login")
    public Result send6Login(String telephone){
        //随机生成6位验证码
        Integer code = ValidateCodeUtils.generateValidateCode(6);
        Map map = new HashMap<>();
        map.put("code",code);
        String jsonCode = JSON.toJSONString(map);
        //发送短信验证码
        HealthSmsUtils.sendSms(telephone,HealthSmsUtils.VALIDATE_CODE,jsonCode,HealthSmsUtils.SIGN_NAME);
        //验证码存入redis
        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_LOGIN,30*24*60*5*60,code.toString());
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
