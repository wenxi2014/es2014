package cn.itcast.controller.cargo;

import cn.itcast.controller.BaseController;
import cn.itcast.domain.cargo.ContractProduct;
import cn.itcast.domain.cargo.ContractProductExample;
import cn.itcast.domain.cargo.Factory;
import cn.itcast.domain.cargo.FactoryExample;
import cn.itcast.service.cargo.ContractProductService;
import cn.itcast.service.cargo.FactoryService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/cargo/contractProduct")
public class ContractProductController extends BaseController{
    // 注入service:import com.alibaba.dubbo.config.annotation.Reference;
    @Reference
    private FactoryService factoryService;
    @Reference
    private ContractProductService contractProductService;

    /**
     * 1.购销合同列表，点击货物进入project-list.jsp货物列表和添加货物的页面
     * 请求地址：http://localhost:8080/cargo/contractProduct/list.do?contractId=1
     * 响应地址：/WEB-INF/pages/cargo/product/product-list.jsp
     */
    @RequestMapping("/list")
    public String list(
            String contractId,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "5") int pageSize){

        //1.1 查询货物的工厂
        FactoryExample factoryExample = new FactoryExample();
        factoryExample.createCriteria().andCtypeEqualTo("货物");
        List<Factory> factoryList = factoryService.findAll(factoryExample);
        //1.2 存储工厂
        request.setAttribute("factoryList",factoryList);

        //1.3 根据购销合同的id，查询购销合同下的所有货物
        ContractProductExample cpExample = new ContractProductExample();
        cpExample.createCriteria().andContractIdEqualTo(contractId);
        PageInfo<ContractProduct> pageInfo =
                contractProductService.findByPage(cpExample, pageNum, pageSize);
        //1.4 存储货物列表
        request.setAttribute("pageInfo",pageInfo);

        //1.5 存储购销合同id  (project-list.jsp是货物列表也是添加货物页面，添加货物要有购销合同id)
        request.setAttribute("contractId",contractId);
        return "cargo/product/product-list";
    }

    /**
     * 2. 保存或者修改货物
     */
    @RequestMapping("/edit")
    public String edit(ContractProduct contractProduct){
        contractProduct.setCompanyId(getLoginCompanyId());
        contractProduct.setCompanyName(getLoginCompanyName());

        // 3.1 根据主键id判断添加或者修改
        if (StringUtils.isEmpty(contractProduct.getId())){
            // 3.1 执行添加操作
            contractProductService.save(contractProduct);
        } else {
            // 3.2 执行修改操作
            contractProductService.update(contractProduct);
        }
        // 3.3 操作成功，返回到购销合同列表
        return "redirect:/cargo/contractProduct/list.do?contractId=" + contractProduct.getContractId();
    }

    /**
     * 3. 进入修改页面
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id){
        // 查询货物的工厂
        FactoryExample factoryExample = new FactoryExample();
        factoryExample.createCriteria().andCtypeEqualTo("货物");
        List<Factory> factoryList = factoryService.findAll(factoryExample);
        request.setAttribute("factoryList",factoryList);

        // 根据购销合同id查询
        ContractProduct contractProduct = contractProductService.findById(id);
        request.setAttribute("contractProduct",contractProduct);

        return "cargo/product/product-update";
    }

    /**
     * 4. 删除货物
     * @param id            根据id删除
     * @param contractId    删除后重定向到货物列表和添加页面（product-list.jsp）
     * @return
     */
    @RequestMapping("/delete")
    public String delete(String id,String contractId){
        contractProductService.delete(id);
        return "redirect:/cargo/contractProduct/list.do?contractId=" + contractId;
    }
}