package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealDao {
    /**
     * 套餐分页查询
     * @param queryString
     * @return
     */
    Page<Setmeal> findByCcondition(String queryString);

    /**
     * 新增套餐
     * @param setmeal
     */
    void add(Setmeal setmeal);

    /**
     * 设置中间表
     * @param map
     */
    void setSetmealAndChecekGroup(Map<String, Integer> map);


    List<Integer> findCheckGroupIdsBySetmealId(Integer id);

    /**
     * 根据套餐Id查询套餐信息
     * @param id
     * @return
     */
    Setmeal findById(Integer id);

    /**
     * 编辑套餐
     * @param setmeal
     * @param checkigroupIds
     */
    void edit(Setmeal setmeal, Integer[] checkigroupIds);

    void deleteBySetmealId(Integer id);

    void setSetmealIdAndChecekGroupId(Map<String, Integer> map);

    List<Setmeal> findAll();

    Setmeal findSetmealDetailById(Integer id);

    List<Map> findSetmealCount();

}
