package cn.itcast.controller.system;

import cn.itcast.controller.BaseController;
import cn.itcast.domain.system.Dept;
import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.Role;
import cn.itcast.service.system.DeptService;
import cn.itcast.service.system.ModuleService;
import cn.itcast.service.system.RoleService;
import cn.itcast.service.system.RoleService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/system/role")
public class RoleController extends BaseController {

    // 注入service
    @Autowired
    private RoleService roleService;
    @Autowired
    private ModuleService moduleService;

    /**
     * 1. 分页查询
     */
    @RequestMapping("/list")
    public ModelAndView list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "5") Integer pageSize){
        //1.1 调用service
        PageInfo<Role> pageInfo =
                roleService.findByPage(getLoginCompanyId(), pageNum, pageSize);
        //1.2 返回
        ModelAndView mv = new ModelAndView();
        mv.addObject("pageInfo",pageInfo);
        mv.setViewName("system/role/role-list");
        return mv;
    }

    /**
     * 2. 添加（1） 进入添加页面
     */
    @RequestMapping("/toAdd")
    public String toAdd(){
        return "system/role/role-add";
    }

    /**
     * 3. 添加/修改(2) 保存
     */
    @RequestMapping("/edit")
    public String edit(Role role){
        // 3.1 设置所属企业信息
        role.setCompanyId(getLoginCompanyId());
        role.setCompanyName(getLoginCompanyName());

        // 3.2 判断
        if (StringUtils.isEmpty(role.getId())){
            //3.1 添加
            roleService.save(role);
        } else {
            //3.2 修改
            roleService.update(role);
        }
        return "redirect:/system/role/list.do";
    }

    /**
     * 4. 进入修改页面
     */
    @RequestMapping("/toUpdate")
    public ModelAndView toUpdate(String id){
        //4.1 根据id查询，回显
        Role role = roleService.findById(id);
        //4.2 返回
        ModelAndView mv = new ModelAndView();
        mv.setViewName("system/role/role-update");
        mv.addObject("role",role);
        return mv;
    }

    /**
     * 5. 删除，异步请求
     */
    @RequestMapping("/delete")
    public String delete(String id){
        roleService.delete(id);
        return "redirect:/system/role/list.do";
    }

    /**
     * 6. 进入角色权限页面:role-module.jsp
     */
    @RequestMapping("/roleModule")
    public String roleModule(String id){
        //6.1 根据角色id查询
        Role role = roleService.findById(id);
        request.setAttribute("role",role);
        //6.2 返回
        return "system/role/role-module";
    }

    /**
     * 7. 在role-module.jsp发送的异步请求
     * 返回的json格式：[{ id:2, pId:0, name:"随意勾选 2", checked:true, open:true},{}]
     */
    @RequestMapping("/getZtreeJson")
    @ResponseBody
    public List<Map<String,Object>> getZtreeJson(String roleId){
        //7.1 查询所有的权限
        List<Module> moduleList = moduleService.findAll();

        //7.2 根据角色id，查询当前角色已经拥有的权限。 为了默认选中。
        List<Module> roleModuleList = moduleService.findModulesByRoleId(roleId);

        //7.3 构造返回的格式数据
        //7.3.1 构造返回的集合
        List<Map<String,Object>> result = new ArrayList<>();
        //7.3.2 遍历所有的权限
        for (Module module : moduleList) {
            //7.3.2 构造map集合 根据ztree格式 遍历 封装已有的权限
            Map<String,Object> map = new HashMap<>();
            map.put("id",module.getId());
            map.put("pId",module.getParentId());
            map.put("name",module.getName());
            map.put("open",true);
            // 设置json的checked属性值。用来默认选中角色已经具有的权限
            if (roleModuleList.contains(module)){
                map.put("checked",true);
            }
            //7.3.3 把map集合添加到result结果集中
            result.add(map);
        }

        return result;
    }

    /**
     * 8. 保存角色权限
     */
    @RequestMapping("/updateRoleModule")
    public String updateRoleModule(String roleId,String moduleIds){
        // 调用service给角色添加权限
        roleService.updateRoleModule(roleId,moduleIds);
        return "redirect:/system/role/list.do";
    }

}















