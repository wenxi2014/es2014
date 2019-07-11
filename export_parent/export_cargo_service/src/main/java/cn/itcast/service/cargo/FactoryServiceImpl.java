package cn.itcast.service.cargo;

import cn.itcast.dao.cargo.FactoryDao;
import cn.itcast.domain.cargo.Factory;
import cn.itcast.domain.cargo.FactoryExample;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Service
public class FactoryServiceImpl implements FactoryService{

    @Autowired
    private FactoryDao factoryDao;

    /**
     * 分页查询
     *
     * @param contractExample
     * @param pageNum
     * @param pageSize
     */
    @Override
    public PageInfo<Factory> findByPage(FactoryExample contractExample, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Factory> list = factoryDao.selectByExample(contractExample);
        PageInfo<Factory> pageInfo = new PageInfo<>(list);
            return pageInfo;
    }

    /**
     * 查询所有
     *
     * @param factoryExample
     */
    @Override
    public List<Factory> findAll(FactoryExample factoryExample) {

        return factoryDao.selectByExample(factoryExample);
    }


    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @Override
    public Factory findById(String id) {
        return factoryDao.selectByPrimaryKey(id);
    }

    /**
     * 新增
     *
     * @param factory
     */
    @Override
    public void save(Factory factory) {
           factoryDao.insertSelective(factory);
    }

    /**
     * 修改
     *
     * @param factory
     */
    @Override
    public void update(Factory factory) {
           factoryDao.updateByPrimaryKeySelective(factory);
    }

    /**
     * 删除部门
     *
     * @param id
     */
    @Override
    public void delete(String id) {
         factoryDao.deleteByPrimaryKey(id);
    }
}
