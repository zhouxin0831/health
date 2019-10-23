package serivce;

import com.itheima.entity.PageResult;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealService {
    /**
     * 套餐分页
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    /**
     * 新增套餐
     * @param setmeal
     * @param checkgroupIds
     */
    void add(Setmeal setmeal, Integer[] checkgroupIds);

    /**
     * 通过套餐id查询检查组ID
     * @param id
     * @return
     */
    List<Integer> findCheckGroupIdsBySetmealId(Integer id);

    /**
     * 修改时通过套餐id查询数据回写数据
     * @param id
     * @return
     */
    Setmeal findById(Integer id);


    /**
     * 修改套餐
     * @param setmeal
     * @param checkigroupIds
     */
    void edit(Setmeal setmeal, Integer[] checkigroupIds);


    /**
     * 查询套餐列表
     * @return
     */

    List<Setmeal> findAll();


    Setmeal findSetmealDetailById(Integer id);


    Map<String, Object> findSeteaml();

}
