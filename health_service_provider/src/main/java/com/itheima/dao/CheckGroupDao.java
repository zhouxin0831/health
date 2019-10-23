package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;

import java.util.List;
import java.util.Map;

public interface CheckGroupDao {

    /**
     * 查询所有检查组
     * @return
     */
    List<CheckGroup> findAll();

    /**
     * 新增检查组
     * @param checkGroup
     */
    void add(CheckGroup checkGroup);

    /**
     * 设置中间表关联
     * @param map
     */
    void setGroupAndCheckItem(Map map);

    /**
     * 检查组分页
     * @param queryString
     * @return
     */
    Page<CheckGroup> seclectByCondition(String queryString);

    /**
     * 校验项目编码
     * @param code
     * @return
     */
    int findByCode(String code);

    /**
     * 校验项目名称
     * @param name
     * @return
     */
    int findByName(String name);

    /**
     * 通过检查组id查询是否该检查组与套餐绑定
     * @param id
     * @return
     */
    long findSetmealCountByCheckGroupId(Integer id);

    /**
     * 根据id删除中间表
     * @param id
     */
    void deleteCheckItemIdsByCheckGroupIds(Integer id);

    /**
     * 根据id删除检查组
     * @param id
     */
    void deleteById(Integer id);

    /**
     * 通过id查询检查组
     * @param id
     * @return
     */
    CheckGroup findById(Integer id);

    /**
     * 通过id查询中检查组
     * @param id
     * @return
     */
    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    /**
     * 根据id删除原有关联关系
     * @param id
     */
    void deleteAssociation(Integer id);



    /**
     * 设置中间表新的关联关系
     * @param map
     */
    void setCheckGroupAndCheckItem(Map<String, Integer> map);

    /**
     * 修改检查组
     * @param checkGroup
     */
    void edit(CheckGroup checkGroup);


}


