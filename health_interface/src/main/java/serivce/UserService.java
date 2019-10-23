package serivce;


import com.itheima.pojo.User;

public interface UserService {

    User findByUsername(String username);
}
