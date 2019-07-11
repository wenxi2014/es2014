package cn.itcast.service.system.impl;

import cn.itcast.dao.system.DeptDao;
import cn.itcast.domain.system.Dept;
import cn.itcast.service.system.DeptService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DeptServiceImpl implements DeptService{
    // 注入dao
    @Autowired
    private DeptDao deptDao;

    @Override
    public PageInfo<Dept> findByPage(String companyId, int pageNum, int pageSize) {
        // 开启分页
        PageHelper.startPage(pageNum,pageSize);
        // 调用dao查询，并且封装分页参数返回
        return new PageInfo<>(deptDao.findAll(companyId));
    }

    @Override
    public List<Dept> findAll(String companyId) {
        return deptDao.findAll(companyId);
    }

    @Override
    public void save(Dept dept) {
        // 设置id值
        dept.setId(UUID.randomUUID().toString());
        deptDao.save(dept);
    }

    @Override
    public void update(Dept dept) {
        deptDao.update(dept);
    }

    @Override
    public Dept findById(String id) {
        return deptDao.findById(id);
    }

    @Override
    public boolean delete(String id) {

        //1) 根据删除的部门id，进行查询，如果查到数据。说明有子部门，不能删除。
        Long count = deptDao.findDeptByParentId(id);
        //2) 判断
        if (count != null && count > 0){
            //不能删除
            return false;
        } else {
            // 当前部门没有子部门，可以删除
            deptDao.delete(id);
            return true;
        }
    }
}
