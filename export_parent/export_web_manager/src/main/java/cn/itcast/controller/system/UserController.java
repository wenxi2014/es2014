package cn.itcast.controller.system;

import cn.itcast.controller.BaseController;
import cn.itcast.domain.system.Dept;
import cn.itcast.domain.system.Role;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.DeptService;
import cn.itcast.service.system.RoleService;
import cn.itcast.service.system.UserService;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
@RequestMapping("/system/user")
public class UserController extends BaseController {

    // 注入service
    @Autowired
    private UserService userService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private RoleService roleService;


    /**
     * 1. 分页查询用户
     */
    @RequestMapping("/list")
    @RequiresPermissions("用户管理")
    public ModelAndView list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "5") Integer pageSize) {

        /**
         * shiro权限校验(1)硬编码方式实现
         */
        //SecurityUtils.getSubject().checkPermission("用户管理");

        //1.1 调用service
        PageInfo<User> pageInfo =
                userService.findByPage(getLoginCompanyId(), pageNum, pageSize);
        //1.2 返回
        ModelAndView mv = new ModelAndView();
        mv.addObject("pageInfo", pageInfo);
        mv.setViewName("system/user/user-list");
        return mv;
    }

    /**
     * 2. 添加用户（1） 进入添加页面
     */
    @RequestMapping("/toAdd")
    public ModelAndView toAdd() {
        //2.1 查询所有部门
        List<Dept> deptList = deptService.findAll(getLoginCompanyId());

        //2.2 保存
        ModelAndView mv = new ModelAndView();
        mv.setViewName("system/user/user-add");
        mv.addObject("deptList", deptList);
        return mv;
    }

    /**
     * 3. 添加/修改用户(2) 保存
     */
    @RequestMapping("/edit")
    public String edit(User user) {
        // 3.1 设置所属企业信息
        user.setCompanyId(getLoginCompanyId());
        user.setCompanyName(getLoginCompanyName());

        // 3.2 判断
        if (StringUtils.isEmpty(user.getId())) {
            //3.1 添加
            userService.save(user);
        } else {
            //3.2 修改
            userService.update(user);
        }
        return "redirect:/system/user/list.do";
    }

    /**
     * 4. 进入修改页面
     */
    @RequestMapping("/toUpdate")
    public ModelAndView toUpdate(String id) {
        //4.1 根据id查询用户，回显
        User user = userService.findById(id);

        //4.2 查询所有部门
        List<Dept> deptList = deptService.findAll(getLoginCompanyId());

        //4.3 返回
        ModelAndView mv = new ModelAndView();
        mv.setViewName("system/user/user-update");
        mv.addObject("user", user);
        mv.addObject("deptList", deptList);
        return mv;
    }

    /**
     * 5. 删除，异步请求
     */
    @RequestMapping("/delete")
    @ResponseBody       // 方法返回结果转换为json
    public Map<String, Object> delete(String id) {
        //5.1 调用serivce删除
        boolean flag = userService.delete(id);

        //5.2 返回
        Map<String, Object> map = new HashMap<>();
        if (flag) {
            map.put("message", "删除成功！");
        } else {
            map.put("message", "删除失败！当前删除的部门有被外键引用！");
        }
        return map;
    }

    /**
     * 从用户列表，进入用户角色页面
     */
    @RequestMapping("/roleList")
    public String roleList(String id) {
        //1.根据id查询用户
        User user = userService.findById(id);

        //2.查询所有角色列表
        List<Role> roleList = roleService.findAll(getLoginCompanyId());

        //3.根据用户id查询用户已经具有的所有的角色集合
        List<Role> userRoles = roleService.findUserRole(id);
        //保存角色字符串，如："1,2,3"
        String userRoleStr = "";
        //4 遍历 获得该用户的角色id
        for (Role userRole : userRoles) {
            userRoleStr += userRole.getId() + ",";
        }

        //4. 保存数据
        request.setAttribute("user", user);
        request.setAttribute("roleList", roleList);
        request.setAttribute("userRoleStr", userRoleStr);

        //5.跳转页面
        return "system/user/user-role";
    }

    /**
     * 实现给用户分配角色
     */

    @RequestMapping("/changeRole")
    public String changeRole(String userId,String[] roleIds){
        // 修改用户的角色
        userService.updateUserRole(userId,roleIds);
        // 修改成功，重定向到用户列表
        return "redirect:/system/user/list.do";
    }
}















