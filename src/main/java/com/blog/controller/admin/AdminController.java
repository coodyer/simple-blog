package com.blog.controller.admin;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.blog.comm.annotation.Power;
import com.blog.comm.base.BaseController;
import com.blog.service.IpService;

@Controller
public class AdminController extends BaseController {
	
	@Resource
	IpService ipService;
	
	@RequestMapping(value = "/admin/index")
	@Power("index")
	public String superIndex(HttpServletRequest req, HttpServletResponse res) {
		String ip = ipService.getIp(req.getRequestURL().toString());
		setAttribute("serverIp", ip);
		return  "/admin/index";
	}

}
