package cn.itcast.shiro;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.Test;

public class App2 {
    // (1) md5 加密32位
    @Test
    public void md5(){
        //a. md5加密
        System.out.println("加密：" + new Md5Hash("123456").toString());

        //b. 加盐
        // 创建安全随机数生成器对象
        SecureRandomNumberGenerator srn = new SecureRandomNumberGenerator();
        // 定义加密盐 (md5加密后的32位长度的字符串)
        String salt = srn.nextBytes().toHex();
        System.out.println("随机生成" + salt);
        Md5Hash md5Hash = new Md5Hash("123456",salt);
        System.out.println("加盐加密： " +md5Hash.toString());

    }

}
