package cn.itcast.controller.cargo;

import cn.itcast.controller.BaseController;
import cn.itcast.domain.cargo.Contract;
import cn.itcast.domain.cargo.ContractExample;
import cn.itcast.domain.system.User;
import cn.itcast.service.cargo.ContractProductService;
import cn.itcast.service.cargo.ContractService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

@Controller
@RequestMapping("/cargo/contract")
public class ContractController extends BaseController{
    // 注入service:import com.alibaba.dubbo.config.annotation.Reference;
    @Reference
    private ContractService contractService;
    @Reference
    private ContractProductService contractProductService;

    /**
     * 1.查询所有购销合同
     */
    @RequestMapping("/list")
    public String list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "5") int pageSize) {
        // 构造条件
        ContractExample example = new ContractExample();
        // 构造条件-排序
        example.setOrderByClause("create_time desc");
        ContractExample.Criteria criteria = example.createCriteria();
        // 构造条件-根据公司id查询
        criteria.andCompanyIdEqualTo(getLoginCompanyId());
        /**
         * 细粒度权限控制
         * 用户级别 degree
         *   4-普通员工
         *   3-管理本部门
         *   2-管理所有下属部门和人员
         */
        User user = getLoginUser();
        if (user.getDegree() == 4){
            // 普通用户登陆，只能查看自己创建的购销合同
            criteria.andCreateByEqualTo(user.getId());
        } else if (user.getDegree() == 3){
            // 管理本部门
            criteria.andCreateDeptEqualTo(user.getDeptId());
        } else if (user.getDegree() ==2){
            // 管理所有下属部门和人员
            PageInfo<Contract> pageInfo =
                    contractService.selectByDeptId(user.getDeptId(),pageNum,pageSize);
            request.setAttribute("pageInfo",pageInfo);
            return "cargo/contract/contract-list";
        }

        //1.调用service查询购销合同列表
        PageInfo<Contract> pageInfo =
                contractService.findByPage(example, pageNum, pageSize);
        //2.保存数据
        request.setAttribute("pageInfo",pageInfo);
        //3. 返回
        return "cargo/contract/contract-list";
    }


    /**
     * 2.进入添加页面
     */
    @RequestMapping("/toAdd")
    public String toAdd(){
        return "cargo/contract/contract-add";
    }

    /**
     * 3. 添加或者修改方法
     */
    @RequestMapping("/edit")
    public String edit(Contract contract){
        contract.setCompanyId(getLoginCompanyId());
        contract.setCompanyName(getLoginCompanyName());

        // 3.1 根据主键id判断添加或者修改
        if (StringUtils.isEmpty(contract.getId())){
            // 3.1 执行添加操作
            // A. 指定创建时间
            contract.setCreateTime(new Date());
            // B. 创建人
            contract.setCreateBy(getLoginUser().getId());
            // C. 创建人所属部门
            contract.setCreateDept(getLoginUser().getDeptId());
            contractService.save(contract);
        } else {
            // 3.2 执行修改操作
            contractService.update(contract);
        }
        // 3.3 操作成功，返回到购销合同列表
        return "redirect:/cargo/contract/list.do";
    }

    /**
     * 4. 从contract-list.jsp列表页面，进入到contract-update.jsp页面
     */
    @RequestMapping("/toUpdate")
    public ModelAndView toUpdate(String id){
        //4.1 调用service，根据id查询
        Contract contract = contractService.findById(id);

        //4.2 返回
        ModelAndView mv = new ModelAndView();
        //4.2.1 设置转发后的地址
        mv.setViewName("cargo/contract/contract-update");
        //4.2.2 保存数据
        mv.addObject("contract",contract);
        return mv;
    }

    /**
     * 删除购销合同
     */
    @RequestMapping("/delete")
    public String delete(String id,String contractId) {
        contractProductService.delete(id);
        return "redirect:/cargo/contractProduct/list.do?contractId="+contractId;
    }

    /**
     * 6. 查看
     */
    @RequestMapping("/toView")
    public String toView(String id){
        //6.1 根据id查询
        Contract contract = contractService.findById(id);
        //6.2 保存
        request.setAttribute("contract",contract);
        //6.3 转发
        return "cargo/contract/contract-view";
    }

    /**
     * 7. 提交. 把购销合同状态由0改为1
     */
    @RequestMapping("/submit")
    public String submit(String id){
        //6.1 创建购销合同对象
        Contract contract = new Contract();
        //6.2 设置id
        contract.setId(id);
        //6.3 设置状态
        contract.setState(1);
        //6.4 修改
        contractService.update(contract);
        //6.4 重定向
        return "redirect:/cargo/contract/list.do";
    }

    /**
     * 8. 取消. 把购销合同状态由1改为0
     */
    @RequestMapping("/cancel")
    public String cancel(String id){
        //6.1 创建购销合同对象
        Contract contract = new Contract();
        //6.2 设置id
        contract.setId(id);
        //6.3 设置状态
        contract.setState(0);
        //6.4 修改
        contractService.update(contract);
        //6.4 重定向
        return "redirect:/cargo/contract/list.do";
    }
}