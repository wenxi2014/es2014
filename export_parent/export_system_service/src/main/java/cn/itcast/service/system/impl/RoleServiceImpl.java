package cn.itcast.service.system.impl;

import cn.itcast.dao.system.RoleDao;
import cn.itcast.domain.system.Role;
import cn.itcast.service.system.RoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Service
public class RoleServiceImpl implements RoleService{
    // 注入dao
    @Autowired
    private RoleDao roleDao;

    @Override
    public PageInfo<Role> findByPage(String companyId, int pageNum, int pageSize) {
        // 开启分页
        PageHelper.startPage(pageNum,pageSize);
        // 调用dao查询，并且封装分页参数返回
        return new PageInfo<>(roleDao.findAll(companyId));
    }

    @Override
    public Role findById(String id) {
        return roleDao.findById(id);
    }

    @Override
    public void save(Role role) {
        // 设置id值
        role.setId(UUID.randomUUID().toString());
        roleDao.save(role);
    }

    @Override
    public void update(Role role) {
        roleDao.update(role);
    }

    @Override
    public void updateRoleModule(String roleId, String moduleIds) {

        //1) 先删除角色的权限
        roleDao.deleteRoleModuleByRoleId(roleId);

        //2) 再给角色添加权限
        if (moduleIds != null && !"".equals(moduleIds)){
            // 因为多个权限的id是用逗号隔开，所以这里分割字符串
            String[] array = moduleIds.split(",");
            // 遍历
            if (array != null) {
                for (String moduleId : array) {
                    if (moduleId != null && !"".equals(moduleId)) {
                        roleDao.saveRoleModule(roleId, moduleId);
                    }
                }
            }
        }
    }

    @Override
    public List<Role> findAll(String companyId) {
        return roleDao.findAll(companyId);
    }

    @Override
    public List<Role> findUserRole(String userId) {
        return roleDao.findUserRole(userId);
    }

    @Override
    public void delete(String id) {
        roleDao.delete(id);
    }
}
