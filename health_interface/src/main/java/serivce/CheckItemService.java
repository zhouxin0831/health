package serivce;


import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckItem;

import java.util.List;


public interface CheckItemService {

    /**
     * 新增检查项
     * @param checkItem
     */
    void add(CheckItem checkItem);

    /**
     * 查询检查项分页
     * @param queryString
     * @return
     */
    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);


    /**
     * 通过ID删除相关检查项
     * @param id
     */
    void deleteById(Integer id);

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

    List<CheckItem> findAll();
}
