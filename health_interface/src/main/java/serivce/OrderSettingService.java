package serivce;

import com.itheima.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {


    void addList(List<OrderSetting> orderSettingList);

    List<Map> getOrderSettingByMonth(String date);


    void editNumberByDate(OrderSetting orderSetting);
}
