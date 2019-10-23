package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.qiniu.util.Json;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;
import serivce.MemberService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;


@RestController
@RequestMapping("/member")
public class MemberComtroller {

    @Reference
    private MemberService memberService;

    @Autowired
    private JedisPool jedisPool;



    @RequestMapping("/login")
    public Result login(HttpServletResponse response, @RequestBody Map map) {
        //从map中获取登录手机号和验证码
        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");
        //从缓存中取出验证码
        String codeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        if(codeInRedis==null || !codeInRedis.equals(validateCode)){
            //缓存中没有验证码或者缓存中验证码与前台传过来的验证码不一致，登录失败
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //验证码一致
        //根据手机号判断是否为会员
        Member member=memberService.findByTelephone(telephone);
        if(member==null){
            //不是会员
            member=new Member();
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            memberService.add(member);

        }

        Cookie cookie = new Cookie("login_telephone_member", telephone);
        cookie.setPath("/");
        cookie.setMaxAge(60*60*24*30);
        response.addCookie(cookie);
        String json = JSON.toJSON(member).toString();
        jedisPool.getResource().setex(telephone,60*30,json);


            return new Result(true,MessageConstant.LOGIN_SUCCESS);

    }
}
