package cn.itcast.dao.system;

import cn.itcast.domain.system.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface UserDao {

	//根据企业id查询全部
	List<User> findAll(String companyId);

	//根据id查询
    User findById(String userId);

	//根据id删除
	void delete(String userId);

	//保存
	void save(User user);

	//更新
	void update(User user);

	// 根据用户id查询用户角色中间表
    Long findUserRoleByUserId(String id);

    //DAY05 先删除用户角色
    void deleteUserRole(String userId);

    //在给用户 添加角色  多个参数用@Param
	void saveUserRole(@Param("userId") String userId,@Param("roleId") String roleId);

	//查询账号是否存在
	User findByEmail(String email);
}