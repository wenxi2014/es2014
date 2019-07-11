package cn.itcast.dao.cargo;

import cn.itcast.domain.cargo.Factory;
import cn.itcast.domain.cargo.FactoryExample;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:spring/applicationContext-*.xml")
public class FactoryDaoTest {

    @Autowired
    private FactoryDao factoryDao;

    /**
     * insert()  普通的插入
     * 执行SQL：
     * insert into co_factory (id, ctype, full_name, factory_name, contacts, phone,
     * mobile, fax, address, inspector, remark, order_no, state, create_by,
     * create_dept, create_time, update_by, update_time ) 、
     * values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )
     *
     * insertSelective() 动态插入, 生成动态SQL。根据对象属性如果有值才执行插入
     * insert into co_factory ( id, factory_name, create_time, update_time )
     * values ( ?, ?, ?, ? )
     */
    @Test
    public void save(){
        Factory factory = new Factory();
        factory.setId(UUID.randomUUID().toString());
        factory.setFactoryName("test..");
        factory.setCreateTime(new Date());
        factory.setUpdateTime(new Date());
        //factoryDao.insert(factory);
        factoryDao.insertSelective(factory);
    }

    @Test
    public void update(){
        // 全部列都修改
        factoryDao.updateByPrimaryKey(null);
        // 动态更新：根据对象属性值，有值才修改
        factoryDao.updateByPrimaryKeySelective(null);
    }

    @Test
    public void delete(){
        factoryDao.deleteByPrimaryKey("d8a4d646-6515-45b4-ae68-81874c826455");
    }

    /**
     * 最终的SQL
     * select * from co_factory WHERE ( factory_name = ? and id = ? ) order by id desc
     */
    @Test
    public void find(){
        // 创建条件对象
        FactoryExample factoryExample = new FactoryExample();
        factoryExample.setOrderByClause("id desc");
        FactoryExample.Criteria criteria = factoryExample.createCriteria();
        // 条件： factory_name='民鑫'
        criteria.andFactoryNameEqualTo("民鑫");
        criteria.andIdEqualTo("");
        List<Factory> factoryList = factoryDao.selectByExample(factoryExample);
        System.out.println(factoryList);
    }
}
