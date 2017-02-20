package com.blog.controller.admin;

import java.io.IOException;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.blog.comm.base.BaseController;
import com.blog.comm.util.VerificationCodeUtil;
import com.blog.model.Setting;
import com.blog.service.SettingService;

@Controller
public class LoginController extends BaseController{
	@Resource
	SettingService settingService;
	
	@RequestMapping("/admin/login")
	public String login(HttpServletResponse res){
		Setting setting=settingService.loadSiteSetting();
		setAttribute("setting", setting);
		return "/admin/login";
	}
	@RequestMapping(value = "/admin/verCode")
	public void verCode(HttpServletResponse response) throws ServletException,
			IOException {
		String verCode = VerificationCodeUtil.getCodeStr(4);
		setSessionPara("piccode", verCode);
		ServletOutputStream out = response.getOutputStream();
		response.setContentType("image/gif");
		ImageIO.write(VerificationCodeUtil.outCode(120, 42, 4, 28, verCode),
				"png", out);
	}
}
