package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.PermissionDao;
import com.itheima.dao.RoleDao;
import com.itheima.dao.UserDao;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import serivce.UserService;

import java.util.Set;


@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;

    @Override
    public User findByUsername(String username) {
        //根据username查询user
        User user= userDao.findByUsername(username);
        if(user==null){
            return null;
        }
        //从user中取出userId
        Integer userId = user.getId();
        //根据userId查询出角色集合
        Set<Role> roles=roleDao.findByUserId(userId);

        if(roles!=null && roles.size()>0){
            //集合中有值，遍历集合
            for (Role role : roles) {
                //根据遍历的角色取出角色Id
                Integer roleId = role.getId();
                //根据权角色id查询权限的集合
                Set<Permission> permissions=permissionDao.findByRoleId(roleId);
                if(permissions!=null && permissions.size()>0){
                    //遍历集合
                    for (Permission permission : permissions) {
                        //给角色设置权限
                        role.setPermissions(permissions);
                    }
                }
                //给user设置角色
                user.setRoles(roles);
            }
        }
        return user;
    }


}
