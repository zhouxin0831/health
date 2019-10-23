package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.pojo.Member;
import com.itheima.util.MD5Utils;
import org.apache.poi.ss.formula.functions.Count;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import serivce.MemberService;

import java.text.SimpleDateFormat;
import java.util.*;


@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;

    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    @Override
    public void add(Member member) {
        if(member.getPassword()!=null){
            member.setPassword(MD5Utils.md5(member.getPassword()));
        }
        memberDao.add(member);

    }

    // 根据时间查询会员数量
    @Override
    public Map findMember() {

        Calendar calendar=Calendar.getInstance();//获取当前时间
        calendar.add(Calendar.MONTH,-12);//当前日期前12个月
        List<String> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();

        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH,1);//遍历月数集合，把每个月都添加进去
            list.add(new SimpleDateFormat("yyyy.MM").format(calendar.getTime()));
        }

        map.put("months",list);
        //List<Integer> memberCount=new ArrayList<>();
        List<Integer> memberCount=new ArrayList<>();
        for (String month : list) {
            month=month+".31";
            Integer count= memberDao.findMemberCountByMonth(month);
            System.out.println(count);
            memberCount.add(count);


        }
        map.put("memberCount",memberCount);

        System.out.println(map);


        return map;
    }


}
