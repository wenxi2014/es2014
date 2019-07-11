package cn.itcast.service.system;

import cn.itcast.domain.system.User;
import com.github.pagehelper.PageInfo;


public interface UserService {

	/**
	 * 根据企业id查询全部
	 * @param companyId 区分不同企业的用户
	 * @param pageNum   分页参数：当前页
	 * @param pageSize  分页参数：页大小
	 * @return 返回PageHelper提供的分页对象
	 */
	PageInfo<User> findByPage(String companyId,int pageNum,int pageSize);

	/**
	 * 根据id查询
	 * @param userId
	 * @return
	 */
    User findById(String userId);

	/**
	 * 根据id删除
     * @param userId
     */
	boolean delete(String userId);

	/**
	 * 保存
	 * @param user
	 */
	void save(User user);

	/**
	 * 更新
	 * @param user
	 */
	void update(User user);

	//实现给你用户分配角色
    void updateUserRole(String userId, String[] roleIds);

    //dat05 通过邮箱账号查询用户对象
	User findByEmail(String email);
}