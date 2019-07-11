package cn.itcast.controller;

import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.ModuleService;
import cn.itcast.service.system.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class LoginController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private ModuleService moduleService;

    /**
     * 登陆访问页面
     * 1. 访问：http://lcoalhost:8080/index.jsp
     * 2. index.jsp ----> window.location.href = "login.do";
     * 3. 所以，这里定义了LoginController.login()方法。
     * 4. login() ---> /home/main.jsp
     */
 /*   @RequestMapping("/login")
    public String login(String email, String password) {
        // 1. 登陆请求参数校验：email 表示邮箱； password表示密码
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
            // 跳转到登陆页面
            return "forward:/login.jsp";
        }
        // 2. 登陆验证
        // 2.1 调用service，根据邮箱查询用户是否存在。注意：email要唯一。
        User user = userService.findByEmail(email);
        // 2.2 判断
        if (user != null && user.getPassword().equals(password)) {
            //保存登录角色信息到域
            session.setAttribute("loginUser", user);
            // 2.3 根据登陆用户id，查询权限 并放到域
            List<Module> moduleList = moduleService.findModulesByUserId(user.getId());
            session.setAttribute("moduleList",moduleList);
            // 2.3 登陆认证成功,跳转到main.jsp页面
            return "home/main";
        } else {
            //5.登录失败，跳转到登录页面
            request.setAttribute("error", "用户名或者密码错误");
            return "forward:/login.jsp";
        }
    }
*/

    @RequestMapping("/login")
    public String login(String email,String password) {
        try {
            // 1 获得subject
            Subject subject = SecurityUtils.getSubject();
            // 2 构造用户和密码
            UsernamePasswordToken token = new UsernamePasswordToken(email, password);
            //3.借助subject完成用户登录
            subject.login(token);
            //4.通过shiro获取用户对象，保存到session中
            User user = (User)subject.getPrincipal(); //获取安全数据（用户对象）
            session.setAttribute("loginUser",user);
            //5.获取菜单数据
            List<Module> moduleList = moduleService.findModulesByUserId(user.getId());
            session.setAttribute("moduleList",moduleList);

            // 2.3 登陆认证成功,跳转到main.jsp页面
            return "home/main";
        } catch (AuthenticationException e) {
            e.printStackTrace();
            //登录失败跳转页面
            request.setAttribute("error","用户名或者密码错误");
            return "forward:login.jsp";
        }


    }

    /**
     * 05day 注销
     */

    @RequestMapping("/logout")
    public String logout(){
        // 先清空session中登陆用户
      // 没有有shiro前 session.removeAttribute("loginUser");

        // 销毁认证数据
        SecurityUtils.getSubject().logout();
        // 销毁服务端session
        session.invalidate();
        return "forward:/login.jsp";
    }

    /**
     * 显示主页
     * 5. /home/main.jsp --->src="/home.do"
     */
    @RequestMapping("/home")
    public String home() {
        return "home/home";
    }

}
