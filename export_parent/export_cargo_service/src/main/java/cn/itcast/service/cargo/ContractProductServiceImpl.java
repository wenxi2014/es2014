package cn.itcast.service.cargo;

import cn.itcast.dao.cargo.ContractDao;
import cn.itcast.dao.cargo.ContractProductDao;
import cn.itcast.dao.cargo.ExtCproductDao;
import cn.itcast.domain.cargo.*;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

@Service
public class ContractProductServiceImpl implements ContractProductService {

    @Autowired
    private ContractProductDao contractProductDao;
    @Autowired
    private ContractDao contractDao;
    @Autowired
    private ExtCproductDao extCproductDao;

    /**
     * 分页查询
     *
     * @param contractExample 分页查询的参数
     * @param pageNum         当前页
     * @param pageSize        页大小
     * @return
     */
    @Override
    public PageInfo<ContractProduct> findByPage(
            ContractProductExample contractExample, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ContractProduct> list = contractProductDao.selectByExample(contractExample);
        PageInfo<ContractProduct> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    /**
     * 查询所有
     *
     * @param contractProductExample
     */
    @Override
    public List<ContractProduct> findAll(ContractProductExample contractProductExample) {
        return contractProductDao.selectByExample(contractProductExample);
    }

    /**
     * 根据id查询
     *
     * @param id
     */
    @Override
    public ContractProduct findById(String id) {
        return contractProductDao.selectByPrimaryKey(id);
    }

    /**
     * 新增
     *
     * @param contractProduct
     */
    @Override
    public void save(ContractProduct contractProduct) {
        //1. 计算货物的金额 = 单价 * 数量
        Double amount = 0d;
        if (contractProduct.getPrice() != null && contractProduct.getCnumber()!=null) {
            amount = contractProduct.getPrice() * contractProduct.getCnumber();
        }
        //2. 设置货物金额
        contractProduct.setAmount(amount);

        //3. 保存货物
        contractProduct.setId(UUID.randomUUID().toString());
        contractProductDao.insertSelective(contractProduct);

        //4. 修改购销合同的总金额 += 货物金额
        Contract contract = contractDao.selectByPrimaryKey(contractProduct.getContractId());
        contract.setTotalAmount(contract.getTotalAmount() + amount);
        contract.setProNum(contract.getProNum() + 1);
        //5. 修改购销合同
        contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public void update(ContractProduct contractProduct) {
        //1.获取修改之前的金额
        ContractProduct oldCp = contractProductDao.selectByPrimaryKey(contractProduct.getId());
        //2.计算修改之后的总金额
        double amount = 0d;
        if(contractProduct.getCnumber() != null && contractProduct.getPrice() != null) {
            amount = contractProduct.getCnumber() * contractProduct.getPrice();
        }
        //3.设置货物修改之后的总金额
        contractProduct.setAmount(amount);
        //4.更新货物
        contractProductDao.updateByPrimaryKeySelective(contractProduct);
        //5.根据货物所属的购销合同id查询购销合同
        Contract contract = contractDao.selectByPrimaryKey(oldCp.getContractId());
        //6.计算购销合同的总金额     总金额 - 修改之前的金额  + 修改之后的金额
        contract.setTotalAmount(contract.getTotalAmount() - oldCp.getAmount() + amount);
        //7.更新购销合同
        contractDao.updateByPrimaryKeySelective(contract);
    }
    // 删除货物
    @Override
    public void delete(String id) {
        //1. 根据货物id查询
        ContractProduct contractProduct =
                contractProductDao.selectByPrimaryKey(id);
        //2. 获取货物金额
        double cpAmount = contractProduct.getAmount();

        //3. 根据货物id，查询货物下的所有附件
        ExtCproductExample extCproductExample = new ExtCproductExample();
        ExtCproductExample.Criteria criteria = extCproductExample.createCriteria();
        criteria.andContractProductIdEqualTo(id);
        List<ExtCproduct> extCproducts = extCproductDao.selectByExample(extCproductExample);

        //4. 累加附件金额、删除附件
        double extcAmount = 0d;
        if (extCproducts != null && extCproducts.size()>0) {
            for (ExtCproduct extCproduct : extCproducts) {
                // 累加附件金额
                extcAmount += extCproduct.getAmount();
                // 删除附件
                extCproductDao.deleteByPrimaryKey(extCproduct.getId());
            }
        }

        //5. 查询购销合同
        Contract contract = contractDao.selectByPrimaryKey(contractProduct.getContractId());

        //6. 修改购销合同： 货物数量减1
        contract.setProNum(contract.getProNum() - 1);
        //7. 修改购销合同： 减去附件数量
        contract.setExtNum(contract.getExtNum() - extCproducts.size());
        //8. 修改购销合同： 减去货物金额、附件金额
        contract.setTotalAmount(contract.getTotalAmount() - cpAmount - extcAmount);

        //9. 修改购销合同
        contractDao.updateByPrimaryKeySelective(contract);

        //10. 删除货物
        contractProductDao.deleteByPrimaryKey(id);
    }
}