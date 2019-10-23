package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;

import java.util.List;

public interface CheckItemDao {
    /**
     * 新增检查项
     * @param checkItem
     */
    public void add(CheckItem checkItem);

    /**
     * 查询检查项分页
     * @param queryString
     * @return
     */
    Page<CheckItem> selectByCondition(String queryString);

    /**
     * 通过ID查询多张表关联
     * @param id
     * @return
     */
    public long findCountByCheckItemId(Integer id);

    /**
     * 通过ID删除相关检查项
     * @param id
     */
    public void deleteById(Integer id);


    /**
     * 编辑检查项
     * @param checkItem
     */
    void edit(CheckItem checkItem);

    /**
     * 通过ID查询数据库信息回写
     * @param id
     * @return
     */
    CheckItem findById(Integer id);


    /**
     * 通过code查询数据库相关检查项出现的次数
     * @param code
     * @return
     */
    int findByCode(String code);

    /**
     * 通过name查询数据库相关检查项出现的次数
     * @param name
     * @return
     */
    int findByName(String name);

    List<CheckItem> findAll();
}
