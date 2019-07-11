package cn.itcast.controller;

import cn.itcast.domain.cargo.Contract;
import cn.itcast.domain.system.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class BaseController {

    @Autowired
    protected HttpServletResponse response;
    @Autowired
    protected HttpServletRequest request;
    @Autowired
    protected HttpSession session;


    /**
     * 获取登陆用户的方法
     */
    public User getLoginUser() {
        return (User) session.getAttribute("loginUser");
    }

    /**
     * 返回当前登陆用户所属公司ID
     *
     * @return
     */
    public String getLoginCompanyId() {
        return getLoginUser().getCompanyId();
    }

    /**
     * 返回当前登陆用户所属公司名称
     *
     * @return
     */
    public String getLoginCompanyName() {
        return getLoginUser().getCompanyName();
    }
}
