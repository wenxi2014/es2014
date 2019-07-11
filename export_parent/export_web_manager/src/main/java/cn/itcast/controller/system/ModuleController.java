package cn.itcast.controller.system;

import cn.itcast.controller.BaseController;
import cn.itcast.domain.system.Module;
import cn.itcast.service.system.ModuleService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/system/module")
public class ModuleController extends BaseController {

    // 注入service
    @Autowired
    private ModuleService moduleService;

    /**
     * 1. 分页查询
     */
    @RequestMapping("/list")
    public ModelAndView list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "5") Integer pageSize){
        //1.1 调用service
        PageInfo<Module> pageInfo =
                moduleService.findByPage(pageNum, pageSize);
        //1.2 返回
        ModelAndView mv = new ModelAndView();
        mv.addObject("pageInfo",pageInfo);
        mv.setViewName("system/module/module-list");
        return mv;
    }

    /**
     * 2. 添加（1） 进入添加页面
     */
    @RequestMapping("/toAdd")
    public String toAdd(){
        //2.1 调用service查询所有模块
        List<Module> moduleList = moduleService.findAll();
        //2.2 保存
        request.setAttribute("moduleList",moduleList);
        return "system/module/module-add";
    }

    /**
     * 3. 添加/修改(2) 保存
     */
    @RequestMapping("/edit")
    public String edit(Module module){
        // 3.2 判断
        if (StringUtils.isEmpty(module.getId())){
            //3.1 添加
            moduleService.save(module);
        } else {
            //3.2 修改
            moduleService.update(module);
        }
        return "redirect:/system/module/list.do";
    }

    /**
     * 4. 进入修改页面
     */
    @RequestMapping("/toUpdate")
    public ModelAndView toUpdate(String id){
        //2.1 调用service查询所有模块
        List<Module> moduleList = moduleService.findAll();

        //4.1 根据id查询，回显
        Module module = moduleService.findById(id);
        //4.2 返回
        ModelAndView mv = new ModelAndView();
        mv.setViewName("system/module/module-update");
        mv.addObject("module",module);
        mv.addObject("moduleList",moduleList);
        return mv;
    }

    /**
     * 5. 删除
     */
    @RequestMapping("/delete")
    public String delete(String id){
        moduleService.delete(id);
        return "redirect:/system/module/list.do";
    }

}















