package cn.itcast.service.system;

import cn.itcast.domain.system.Module;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ModuleService {

    //查询全部
    PageInfo<Module> findByPage(int pageNum,int pageSize);

    //根据id查询
    Module findById(String moduleId);

    //根据id删除
    void delete(String moduleId);

    //添加
    void save(Module module);

    //更新
    void update(Module module);

    List<Module> findAll();

    List<Module> findModulesByRoleId(String roleId);

    //根据用户id 查询用户权限
    List<Module> findModulesByUserId(String userId);
}