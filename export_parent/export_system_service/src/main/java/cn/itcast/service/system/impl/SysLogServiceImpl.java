package cn.itcast.service.system.impl;

import cn.itcast.dao.system.SysLogDao;
import cn.itcast.domain.system.SysLog;
import cn.itcast.service.system.SysLogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SysLogServiceImpl implements SysLogService{
    // 注入dao
    @Autowired
    private SysLogDao sysLogDao;

    @Override
    public PageInfo<SysLog> findByPage(String companyId, int pageNum, int pageSize) {
        // 开启分页
        PageHelper.startPage(pageNum,pageSize);
        // 调用dao查询，并且封装分页参数返回
        return new PageInfo<>(sysLogDao.findAll(companyId));
    }

    @Override
    public void save(SysLog log) {
        log.setId(UUID.randomUUID().toString());
        sysLogDao.save(log);
    }
}
