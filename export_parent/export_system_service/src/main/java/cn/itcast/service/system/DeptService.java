package cn.itcast.service.system;

import cn.itcast.domain.system.Dept;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface DeptService {
    /**
     * 分页查询部门
     * @param companyId 当前部门所属企业
     * @param pageNum   当前页
     * @param pageSize  页大小
     * @return
     */
    PageInfo<Dept> findByPage(String companyId,int pageNum,int pageSize);

    /**
     * 根据所属企业，查询所有部门
     * @param companyId
     * @return
     */
    List<Dept> findAll(String companyId);

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
     * 根据id查询部门
     * @param id
     * @return
     */
    Dept findById(String id);

    /**
     * 删除
     * @param id
     * @return 返回true表示删除成功
     */
    boolean delete(String id);
}
