package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.RedisConstant;
import com.itheima.dao.SetmealDao;
import com.itheima.entity.PageResult;

import com.itheima.pojo.Setmeal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;
import serivce.SetmealService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service(interfaceClass =SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealDao setmealDao;
    @Autowired
    private JedisPool jedisPool;


    /**
     * 套餐分页
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
        Page<Setmeal> page=setmealDao.findByCcondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 新增套餐
     * @param setmeal
     * @param checkgroupIds
     */
    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        setmealDao.add(setmeal);
        if(checkgroupIds!=null && checkgroupIds.length>0) {
            setSetmealAndChecekGroup(setmeal.getId(),checkgroupIds);
        }
        savePic2Redis(setmeal.getImg());
    }

    /**
     * 通过套餐id查询检查组ID并回写
     * @param id
     * @return
     */
    @Override
    public List<Integer> findCheckGroupIdsBySetmealId(Integer id) {
        return setmealDao.findCheckGroupIdsBySetmealId(id);

    }

    /**
     * 通过套餐ID查询并回写套餐数据
     * @param id
     * @return
     */
    @Override
    public Setmeal findById(Integer id) {
        return setmealDao.findById(id);
    }

    /**
     * 编辑套餐
     * @param setmeal
     * @param checkigroupIds
     */
    @Override
    public void edit(Setmeal setmeal, Integer[] checkigroupIds) {
        setmealDao.deleteBySetmealId(setmeal.getId());
        setSetmealIdAndChecekGroupId(setmeal.getId(),checkigroupIds);
        setmealDao.edit(setmeal,checkigroupIds);
    }

    /**
     * 查询所有
     * @return
     */
    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }

    @Override
    public Setmeal findSetmealDetailById(Integer id) {
        return setmealDao.findSetmealDetailById(id);
    }







    @Override
    public Map<String,Object> findSeteaml() {
        Map resultMap=new HashMap();//返回 controller层的map

        List<Map> list=setmealDao.findSetmealCount();



        List<String> setmealNames=new ArrayList<>();
        List<Map> setmealCount=new ArrayList<>();
        List<Map> totalPrices=new ArrayList<>();

        for (Map map : list) {
            String name = (String) map.get("name");
            Long num = (Long) map.get("num");
            Double price = (Double) map.get("price");


            Map setmealMap=new HashMap();
            setmealMap.put("name",name);
            setmealMap.put("value",num);
            setmealCount.add(setmealMap);


            Map priceMap=new HashMap();
            priceMap.put("name",name);
            priceMap.put("value",price);
            totalPrices.add(priceMap);


        }
        resultMap.put("setmealNames",setmealNames);
        resultMap.put("setmealCount",setmealCount);
        resultMap.put("totalPrices",totalPrices);
        





        return resultMap;
    }










    private void setSetmealIdAndChecekGroupId(Integer setmealId, Integer[] checkigroupIds) {
        if(checkigroupIds!=null && checkigroupIds.length>0){
            for (Integer checkigroupId : checkigroupIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("setmeal_id",setmealId);
                map.put("checkigroup_id",checkigroupId);
                setmealDao.setSetmealIdAndChecekGroupId(map);
            }

        }
    }


    //将图片名称保存到Redis
    private void savePic2Redis(String pic){
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,pic);
    }

    /**
     * 设置中间表
     * @param setmealId
     * @param checkgroupIds
     */
    private void setSetmealAndChecekGroup(Integer setmealId, Integer[] checkgroupIds) {
        for (Integer checkgroupId : checkgroupIds) {
            Map<String, Integer> map = new HashMap<>();
            map.put("setmeal_id",setmealId);
            map.put("checkgroup_id",checkgroupId);
            setmealDao.setSetmealAndChecekGroup(map);
        }
    }


}
