package cn.itcast.controller.company;

import cn.itcast.controller.BaseController;
import cn.itcast.domain.company.Company;
import cn.itcast.service.company.CompanyService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/company")
public class CompanyController{


   @Reference
    private CompanyService companyService;

    /**
     * 1.查询所有企业
     */
    @RequestMapping(value = "/list",name = "企业列表")
    public ModelAndView list(){
        //1. 调用service查询
        List<Company> list = companyService.findAll();
        //2. 返回
        ModelAndView mv = new ModelAndView();
        mv.setViewName("company/company-list");
        mv.addObject("list",list);
        return mv;
    }

    /**
     * 2.进入添加页面
     */
    @RequestMapping("/toAdd")
    public String toAdd(){
        return "company/company-add";
    }

    /**
     * 3. 添加或者修改方法
     */
    @RequestMapping("/edit")
    public String edit(Company company){
        // 3.1 根据主键id判断添加或者修改
        if (StringUtils.isEmpty(company.getId())){
            // 3.1 执行添加操作
            companyService.save(company);
        } else {
            // 3.2 执行修改操作
            companyService.update(company);
        }
        // 3.3 操作成功，返回到企业列表
        return "redirect:/company/list.do";
    }

    /**
     * 4. 从company-list.jsp列表页面，进入到company-update.jsp页面
     */
    @RequestMapping("/toUpdate")
    public ModelAndView toUpdate(String id){
        //4.1 调用service，根据id查询
        Company company = companyService.findById(id);

        //4.2 返回
        ModelAndView mv = new ModelAndView();
        //4.2.1 设置转发后的地址
        mv.setViewName("company/company-update");
        //4.2.2 保存数据
        mv.addObject("company",company);
        return mv;
    }

    /**
     * 5. 删除企业
     */
    @RequestMapping("/delete")
    public String delete(String id){
        companyService.delete(id);
        return "redirect:/company/list.do";
    }

}
