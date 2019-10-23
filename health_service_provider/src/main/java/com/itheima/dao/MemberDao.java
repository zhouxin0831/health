package com.itheima.dao;

import com.itheima.pojo.Member;

import java.util.Date;
import java.util.List;

public interface MemberDao {
    Member findByTelephone(String telephone);

    void add(Member member);

    Integer findMemberCountByMonth(String month);


    //List<Integer> findMemberCountByMonth(String month);
}
