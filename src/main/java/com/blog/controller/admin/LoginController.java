package com.blog.controller.admin;

import java.io.IOException;
import java.util.Date;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.blog.comm.base.BaseController;
import com.blog.comm.entity.MsgEntity;
import com.blog.comm.util.EncryptUtil;
import com.blog.comm.util.RequestUtil;
import com.blog.comm.util.StringUtil;
import com.blog.comm.util.VerificationCodeUtil;
import com.blog.model.MemberInfo;
import com.blog.model.Setting;
import com.blog.service.MemberService;
import com.blog.service.SettingService;

@Controller
public class LoginController extends BaseController {
	@Resource
	SettingService settingService;
	@Resource
	MemberService memberService;

	@RequestMapping("/admin/login")
	public String login(HttpServletResponse res) {
		Setting setting = settingService.loadSiteSetting();
		setAttribute("setting", setting);
		return "/admin/login";
	}

	@RequestMapping("/admin/doLogin")
	@ResponseBody
	public Object doLogin(HttpServletResponse res) {
		String username = getPara("username");
		String password = getPara("password");
		System.out.println(EncryptUtil.customEnCode(password));
		String verCode = getPara("verCode");
		if (StringUtil.isNullOrEmpty(verCode)) {
			return new MsgEntity(3, "验证码为空");
		}
		if (StringUtil.findEmptyIndex(username, password) > -1) {
			return new MsgEntity(1, "用户名或密码为空");
		}
		String sessionCode = RequestUtil.getCode(request);
		setSessionPara("piccode", null);
		if (sessionCode == null || !sessionCode.equals(verCode)) {
			return new MsgEntity(4, "验证码有误");

		}
		MemberInfo member = memberService.loadMemberInfo(username);
		if (StringUtil.isNullOrEmpty(member)) {
			return new MsgEntity(2, "该用户不存在");
		}
		password = EncryptUtil.customEnCode(password);
		if (!password.equals(member.getUserPwd())) {
			return new MsgEntity(3, "密码有误");
		}
		RequestUtil.setUser(request, member);
		setSessionPara("loginTime", new Date());
		return new MsgEntity(0, "登录成功");
	}

	@RequestMapping(value = "/admin/verCode")
	public void verCode(HttpServletResponse response) throws ServletException,
			IOException {
		String verCode = VerificationCodeUtil.getCodeStr(4);
		RequestUtil.setCode(request, verCode);
		ServletOutputStream out = response.getOutputStream();
		response.setContentType("image/gif");
		ImageIO.write(VerificationCodeUtil.outCode(120, 42, 4, 28, verCode),
				"png", out);
	}
}
