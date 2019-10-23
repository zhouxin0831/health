package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckGroupDao;
import com.itheima.entity.PageResult;

import com.itheima.pojo.CheckGroup;

import com.itheima.util.MyStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import serivce.CheckGroupService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {
    @Autowired
    private CheckGroupDao checkGroupDao;

    /**
     * 查询所有检查组
     * @return
     */
    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }

    /**
     * 新增检查组
     * @param checkGroup
     * @param checkitemIds
     */
    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {

        //校验 项目编码不重复
        if(checkGroupDao.findByCode(checkGroup.getCode()) >0) {
            throw new RuntimeException("项目编码重复，请更换一个");
        }
        //校验 项目名称不重复
        if(checkGroupDao.findByName(checkGroup.getName()) >0) {
            throw new RuntimeException("项目名称重复，请更换一个");
        }
        checkGroupDao.add(checkGroup);
        setGroupAndCheckItem(checkGroup.getId(),checkitemIds);

    }

    /**
     * 中间表
     * @param checkgroupId
     * @param checkitemIds
     */
    public void setGroupAndCheckItem(Integer checkgroupId, Integer[] checkitemIds) {
        if(checkitemIds!=null && checkitemIds.length>0) {
            for (Integer checkitemId : checkitemIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("checkgroup_id",checkgroupId);
                map.put("checkitem_id",checkitemId);
                checkGroupDao.setGroupAndCheckItem(map);
            }
        }

    }


    /**
     * 查询所有
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        if(currentPage==null || currentPage.intValue()<=0){
            currentPage=1;
        }
        if(pageSize==null || pageSize.intValue()<=0){
            pageSize=10;
        }
        if(MyStringUtils.isNotEmpty(queryString)){
            queryString=queryString.trim();
        } else {
            queryString="";
        }
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckGroup> page=checkGroupDao.seclectByCondition("%"+queryString+"%");
        long total = page.getTotal();
        List<CheckGroup> rows = page.getResult();

        return new PageResult(total,rows);
    }


    /**
     * 删除检查组
     * @param id
     */
    @Override
    public void delete(Integer id) {
        long setmealCount=checkGroupDao.findSetmealCountByCheckGroupId(id);
        if(setmealCount > 0) {
            throw  new RuntimeException("该检查组有关联套餐信息");
        }
        checkGroupDao.deleteCheckItemIdsByCheckGroupIds(id);
        checkGroupDao.deleteById(id);
    }


    /**
     * 根据ID查询检查组
     * @param id
     * @return
     */
    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    /**
     * 根据checkgroupId查询中间表相关联的checkItemId
     * @param id
     * @return
     */
    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    /**
     * 修改检查组
     * @param checkGroup
     * @param checkitemIds
     */
    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {
        //根据id删除中间表中的关联关系
        checkGroupDao.deleteAssociation(checkGroup.getId());
        //插入编辑后的id和checkItemsId
        setCheckGroupAndCheckItem(checkGroup.getId(),checkitemIds);
        //更新表
        checkGroupDao.edit(checkGroup);
    }



    /**
     * 设置中间表新的关联关系
     * @param checkgroupId
     * @param checkitemIds
     */
    public void setCheckGroupAndCheckItem(Integer checkgroupId, Integer[] checkitemIds) {

        if(checkitemIds!=null && checkitemIds.length>0) {
            for (Integer checkitemId : checkitemIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("checkgroup_id",checkgroupId);
                map.put("checkitem_id",checkitemId);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }


}




