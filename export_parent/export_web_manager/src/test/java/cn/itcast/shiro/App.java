package cn.itcast.shiro;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.Test;

public class App {
    /**
     * md5加密,虽然不可逆,但是不安全. 因为加密后的密码是固定的
     * 彩虹表
     *   明文             密文
     *    1         c4ca4238a0b923820dcc509a6f75849b
     *
     * 撞库!
     */
    @Test
    public void md5(){
        Md5Hash md5Hash = new Md5Hash("1");
        // c4ca4238a0b923820dcc509a6f75849b
        System.out.println(md5Hash);
    }

    // 加密,加盐,加迭代次数
    @Test
    public void md5Salt(){
        Md5Hash md5Hash = new Md5Hash("1","cxk@qq.com",1000000);
        // 6a015f6432cd87d387a99bc9e22c7b08

        System.out.println(md5Hash);
    }

    // md5加盐就可以. 邮箱作为盐加密
    @Test
    public void result(){
        //e1087d424b213621545713b872420c7b
        Md5Hash md5Hash = new Md5Hash("1","cxk@qq.com");
        System.out.println(md5Hash);
    }
}
