package cn.itcast.service.system.impl;

import cn.itcast.dao.system.UserDao;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    // 注入dao
    @Autowired
    private UserDao userDao;

    @Override
    public PageInfo<User> findByPage(String companyId, int pageNum, int pageSize) {
        // 开启分页
        PageHelper.startPage(pageNum, pageSize);
        // 调用dao查询，并且封装分页参数返回
        return new PageInfo<>(userDao.findAll(companyId));
    }

    @Override
    public User findById(String id) {
        return userDao.findById(id);
    }

    @Override
    public void save(User user) {
        // 设置id值
        user.setId(UUID.randomUUID().toString());
        if (user.getPassword() != null) {
            String encodePwd = new Md5Hash(user.getPassword(), user.getEmail()).toString();
            user.setPassword(encodePwd);
        }

        userDao.save(user);
    }

    @Override
    public void update(User user) {
        userDao.update(user);
    }

    //day05 先删除后 再给用户添加角色
    @Override
    public void updateUserRole(String userId, String[] roleIds) {
        //1.先删除
        userDao.deleteUserRole(userId);
        // 2.在添加在根据角色id添加角色
        for (String roleId : roleIds) {
            userDao.saveUserRole(userId, roleId);
        }
    }

    // day05查询账号是否存在
    @Override
    public User findByEmail(String email) {

        return userDao.findByEmail(email);
    }

    @Override
    public boolean delete(String id) {
        //1. 删除用户前，先根据用户id查询用户角色中间表，是否被引用
        Long count = userDao.findUserRoleByUserId(id);

        //2. 判断
        if (count != null && count > 0) {
            //3. 说明删除的记录被引用，不能删除
            return false;
        } else {
            //4. 可以删除
            userDao.delete(id);
            return true;
        }
    }
}
