package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import com.itheima.dao.CheckItemDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckItem;

import com.itheima.util.MyStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import serivce.CheckItemService;

import java.util.List;

@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {
    @Autowired
    private CheckItemDao checkItemDao;

    /**
     * 新增检查项
     * @param checkItem
     */
    @Override
    public void add(CheckItem checkItem) {
        //校验 项目编码不能重复
        if(checkItemDao.findByCode(checkItem.getCode()) >0) {
            throw new RuntimeException("项目编码重复，请更换一个");
        }
        //校验 项目名称不能重复
        if(checkItemDao.findByName(checkItem.getName()) >0) {
            throw new RuntimeException("项目名称重复，请更换一个");
        }

        checkItemDao.add(checkItem);
    }



    /**
     * 分页检查项
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {

        //判断页码为空时或者页码<0时
        if(currentPage==null || currentPage.intValue()<=0) {
            currentPage=1;
        }

        if(pageSize==null || pageSize.intValue()<=0) {
            pageSize=10;
        }

        if(MyStringUtils.isNotEmpty(queryString)){
            queryString=queryString.trim();
        } else {
            queryString="";
        }

        PageHelper.startPage(currentPage,pageSize);

        Page<CheckItem> page = checkItemDao.selectByCondition("%"+queryString+"%");
        long total = page.getTotal();
        List<CheckItem> rows = page.getResult();
        return new PageResult(total,rows);
    }


    /**
     * 删除检查项
     * @param id
     */
    @Override
    public void deleteById(Integer id) {
        long count = checkItemDao.findCountByCheckItemId(id);
        if(count>0){
            //删除失败
            new RuntimeException();
        }
        //删除成功
        checkItemDao.deleteById(id);

    }

    /**
     * 编辑检查项
     * @param checkItem
     */

    @Override
    public void edit(CheckItem checkItem) {
        //校验 项目编码不能重复
        /*if(checkItemDao.findByCode(checkItem.getCode()) >0) {
            throw new RuntimeException("项目编码重复，请更换一个");
        }*/
        //校验 项目名称不能重复
        if(checkItemDao.findByName(checkItem.getName()) >0) {
            throw new RuntimeException("项目名称重复，请更换一个");
        }
        checkItemDao.edit(checkItem);
    }

    /**
     * 通过ID回写检查项
     * @param id
     * @return
     */
    @Override
    public CheckItem findById(Integer id) {
        CheckItem checkItem=checkItemDao.findById(id);
        return checkItem;
    }

    /**
     * 查询所有
     * @return
     */
    @Override
    public List<CheckItem> findAll() {

        return checkItemDao.findAll();
    }


}