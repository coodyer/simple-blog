package com.blog.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.blog.comm.base.BaseController;
import com.blog.model.Setting;
import com.blog.service.SettingService;

@Controller
public class IndexController extends BaseController{

	
	@Resource
	SettingService settingService;
	
	@RequestMapping("index.do")
	public String index(HttpServletResponse res){
		Setting setting=settingService.loadSiteSetting();
		setAttribute("setting", setting);
		return "index";
	}
}
