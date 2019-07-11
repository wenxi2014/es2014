package cn.itcast.controller.shiro;

import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.ModuleService;
import cn.itcast.service.system.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.aspectj.lang.annotation.Around;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/*
自定义realm
 */
public class AuthRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;
    @Autowired
    private ModuleService moduleService;

    //登录认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken upToken = (UsernamePasswordToken) authenticationToken;
         //2.获取用户出入的邮箱和密码
        String email = upToken.getUsername();
        String password = new String(upToken.getPassword());

         //3.根据邮箱查询用户对象
        User user = userService.findByEmail(email);

        //4. 判断
        if (user == null){
            // 用户名错误. 返回NULL就是用户名错误
            // 异常: UnknownAccountException 账号错误
            return null;
        }
        //5. 返回
        // 参数1: 身份对象.  subject.getPrincipal()获取的就是这里的参数1
        // 参数2: 数据库正确的密码
        // 参数3: realm名称,可以随意定义,this.getName()获取shiro提供的默认名称
        SimpleAuthenticationInfo sai = new SimpleAuthenticationInfo(
                user,user.getPassword(),this.getName());
        return sai;
    }

    //权限访问校验
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //1. 获取认证后的用户身份对象
        User user = (User) principals.getPrimaryPrincipal();
        //2. 根据用户查询权限
        List<Module> moduleList = moduleService.findModulesByUserId(user.getId());

        //3. 返回用户的权限
        SimpleAuthorizationInfo sai = new SimpleAuthorizationInfo();
        //3.1 判断用户的权限集合是否为空
        if (moduleList != null && moduleList.size()>0) {
            for (Module module : moduleList) {
                //3.2 添加用户拥有的权限
                sai.addStringPermission(module.getName());
            }
        }
        return sai;
    }

}
