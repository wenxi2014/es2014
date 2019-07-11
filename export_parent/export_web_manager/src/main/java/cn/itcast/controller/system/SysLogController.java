package cn.itcast.controller.system;

import cn.itcast.controller.BaseController;
import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.SysLog;
import cn.itcast.service.system.ModuleService;
import cn.itcast.service.system.SysLogService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/system/log")
public class SysLogController extends BaseController {

    // 注入service
    @Autowired
    private SysLogService sysLogService;

    /**
     * 1. 分页查询
     */
    @RequestMapping("/list")
    public ModelAndView list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "5") Integer pageSize) {
        //1.1 调用service
        PageInfo<SysLog> pageInfo = sysLogService.findByPage(getLoginCompanyId(), pageNum, pageSize);
        //1.2 返回
        ModelAndView mv = new ModelAndView();
        mv.addObject("pageInfo", pageInfo);
        mv.setViewName("system/log/log-list");
        return mv;
    }
}