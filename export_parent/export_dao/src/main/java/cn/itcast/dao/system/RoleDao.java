package cn.itcast.dao.system;

import cn.itcast.domain.system.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface RoleDao {

    //根据id查询
    Role findById(String id);

    //查询全部
    List<Role> findAll(String companyId);

	//根据id删除
    void delete(String id);

	//添加
    void save(Role role);

	//更新
    void update(Role role);

    void deleteRoleModuleByRoleId(String roleId);

    void saveRoleModule(@Param("roleId") String roleId, @Param("moduleId") String moduleId);

    //根据用户id查询用户角色
    List<Role> findUserRole(String userId);
}