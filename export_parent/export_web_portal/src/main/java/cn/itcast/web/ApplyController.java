package cn.itcast.web;

import cn.itcast.domain.company.Company;
import cn.itcast.service.company.CompanyService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ApplyController {

	/**
	 * @Reference(retries = 0)
	 * 	retries:配置重试次数：关键业务需要设置0
	 */
	@Reference(retries = 0)
	private CompanyService companyService;

	/**
	 * 企业入驻申请，保存
	 */
	@RequestMapping("/apply")
	@ResponseBody
	public String apply(Company company) {
		try{
			company.setState(0);
			companyService.save(company);
			return "1";
		}catch (Exception e) {
			e.printStackTrace();
			return "2";
		}
	}
}
