package cn.itcast.controller.exception;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义异常
 */
@Component
public class CustomerException implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(
            HttpServletRequest request, HttpServletResponse response,
            Object handler, Exception ex) {
        // 打印异常信息
        ex.printStackTrace();
        // 返回
        ModelAndView mv = new ModelAndView();
        mv.setViewName("error");
        mv.addObject("errorMsg","对不起，我错了！" + ex.getMessage());

        return mv;
    }
}
