package cn.itcast.dao.system;

import cn.itcast.domain.system.Dept;

import java.util.List;

public interface DeptDao {
    /**
     * 查询全部部门（在service中分页）
     */
    List<Dept> findAll(String companyId);

    /**
     * 根据id查询父部门
     */
    Dept findById(String deptId);

    /**
     * 添加
     * @param dept
     */
    void save(Dept dept);

    /**
     * 修改
     * @param dept
     */
    void update(Dept dept);

    /**
     * 根据删除的部门id，查询是否有子部门
     * @param id
     * @return
     */
    Long findDeptByParentId(String id);

    /**
     * 删除
     * @param id
     */
    void delete(String id);
}
