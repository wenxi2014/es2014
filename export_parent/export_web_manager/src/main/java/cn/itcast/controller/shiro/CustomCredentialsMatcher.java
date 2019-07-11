package cn.itcast.controller.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.crypto.hash.Md5Hash;


//自定义凭证匹配器
public class CustomCredentialsMatcher extends SimpleCredentialsMatcher {

    /**
     * 自定义的凭证匹配器实现登陆认证验证方法.
     * @param token 封装用户输入的账号密码
     * @param info  认证信息,可以获取数据库正确的用户密码
     * @return
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        //1. 获取输入的账号
        String email = (String) token.getPrincipal();

        //2. 获取输入的密码
        String password = new String((char[]) token.getCredentials());

        //3. 对用户输入的密码加密,加盐
        String encodePwd = new Md5Hash(password,email).toString();

        //4. 获取数据库查询出来的正确的密码  (必须加密,加盐)
        String dbPassword = (String) info.getCredentials();
        return encodePwd.equals(dbPassword);
    }
}
