package cn.itcast.controller.system;

import cn.itcast.controller.BaseController;
import cn.itcast.domain.company.Company;
import cn.itcast.domain.system.Dept;
import cn.itcast.service.system.DeptService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/system/dept")
public class DeptController extends BaseController {

    // 注入service
    @Autowired
    private DeptService deptService;

    /**
     * 1. 分页查询部门
     */
    @RequestMapping("/list")
    public ModelAndView list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "5") Integer pageSize){
        // 部门所属企业，后期实现完成登陆后，获取当前登陆用户的企业id.
        // 现在先写死
        String companyId = getLoginCompanyId();
        //1.1 调用service
        PageInfo<Dept> pageInfo =
                deptService.findByPage(companyId, pageNum, pageSize);
        //1.2 返回
        ModelAndView mv = new ModelAndView();
        mv.addObject("pageInfo",pageInfo);
        mv.setViewName("system/dept/dept-list");
        return mv;
    }

    /**
     * 2. 添加部门（1） 进入添加页面
     */
    @RequestMapping("/toAdd")
    public ModelAndView toAdd(){
        String companyId = getLoginCompanyId();
        //2.1 查询所有部门
        List<Dept> deptList = deptService.findAll(companyId);

        //2.2 保存
        ModelAndView mv = new ModelAndView();
        mv.setViewName("system/dept/dept-add");
        mv.addObject("deptList",deptList);
        return mv;
    }

    /**
     * 3. 添加/修改部门(2) 保存
     */
    @RequestMapping("/edit")
    public String edit(Dept dept){
        // 3.1 设置所属企业信息
        String companyId = getLoginCompanyId();
        String companyName = getLoginCompanyName();
        dept.setCompanyId(companyId);
        dept.setCompanyName(companyName);

        // 3.2 判断
        if (StringUtils.isEmpty(dept.getId())){
            //3.1 添加
            deptService.save(dept);
        } else {
            //3.2 修改
            deptService.update(dept);
        }
        return "redirect:/system/dept/list.do";
    }

    /**
     * 4. 进入修改页面
     */
    @RequestMapping("/toUpdate")
    public ModelAndView toUpdate(String id){
        String companyId = getLoginCompanyId();

        //4.1 根据id查询部门，回显
        Dept dept = deptService.findById(id);

        //4.2 查询所有部门
        List<Dept> deptList = deptService.findAll(companyId);

        //4.3 返回
        ModelAndView mv = new ModelAndView();
        mv.setViewName("system/dept/dept-update");
        mv.addObject("dept",dept);
        mv.addObject("deptList",deptList);
        return mv;
    }

    /**
     * 5. 删除，异步请求
     */
    @RequestMapping("/delete")
    @ResponseBody       // 方法返回结果转换为json
    public Map<String,Object> delete(String id){
        //5.1 调用serivce删除
        boolean flag = deptService.delete(id);

        //5.2 返回
        Map<String,Object> map = new HashMap<>();
        if (flag){
            map.put("message","删除成功！");
        }else{
            map.put("message","删除失败！当前删除的部门有被外键引用！");
        }
        return map;
    }

}















