package cn.itcast.controller.utils;

import cn.itcast.domain.system.SysLog;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.SysLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;

/**
 * 需求：访问控制器的所有方法，自动记录日志。
 */
@Component
@Aspect     // 指定当前类为切面类
public class LogAspect {

    @Autowired
    private SysLogService sysLogService;
    // 注入request对象
    @Autowired
    private HttpServletRequest request;

    /**
     *  使用环绕通知对controller方法进行增强：自动记录日志  
     */
    @Around("execution(* cn.itcast.controller.*.*.*(..))")
    public Object insertLog(ProceedingJoinPoint pjp) {
        // 初始化日志日志
        SysLog sysLog = new SysLog();
        sysLog.setId(UUID.randomUUID().toString());
        sysLog.setTime(new Date());

        // 获取当前执行的方法名称
        sysLog.setMethod(pjp.getSignature().getName());
        // 获取当前执行的方法所在类全名
        sysLog.setAction(pjp.getTarget().getClass().getName());
        // 获取来访者的ip
        sysLog.setIp(request.getRemoteAddr());

        // 获取session对象，从session中获取登陆用户
        User user = (User) request.getSession().getAttribute("loginUser");
        sysLog.setUserName(user.getUserName());
        sysLog.setCompanyId(user.getCompanyId());
        sysLog.setCompanyName(user.getCompanyName());

        try {
            //1. 放行，执行方法
            Object retV = pjp.proceed();
            //2. 记录日志
            sysLogService.save(sysLog);
            return retV;
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }
}
