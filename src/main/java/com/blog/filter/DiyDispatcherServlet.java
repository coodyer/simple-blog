package com.blog.filter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.DispatcherServlet;

import com.blog.comm.util.RequestUtil;
import com.blog.comm.util.SpringContextHelper;
import com.blog.service.SuffixService;

@SuppressWarnings("serial")
public class DiyDispatcherServlet extends DispatcherServlet implements Filter{

	public DiyDispatcherServlet(){
		super();
	}
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req=(HttpServletRequest)request;
		HttpServletResponse res=(HttpServletResponse)response;
		SuffixService suffixService=SpringContextHelper.getBean(SuffixService.class);
		List<String> suffixs=suffixService.loadSpringSuffixs();
		List<String> staList = suffixService.loadStaSuffix();
		String suffix=RequestUtil.getURLSuffix(req);
		if (staList.contains(suffix)) {
			res.setHeader("Cache-Control", "max-age=600");
			chain.doFilter(req, res);
			return;
		}
		String defSuffix = suffixService.loadSpringDefaultSuffix();
		req.setAttribute("suffix", defSuffix);
		if(suffixs.contains(suffix)){
			service(req, res);
			return;
		}
		chain.doFilter(req, res);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		super.init(new ServletFilter(filterConfig));
	}
	
	private static class ServletFilter  implements ServletConfig,FilterConfig{

		private FilterConfig filterConfig;
		
		public ServletFilter(FilterConfig filterConfig){
			this.filterConfig=filterConfig;
		}
		@Override
		public String getFilterName() {
			return filterConfig.getFilterName();
		}

		@Override
		public String getInitParameter(String s) {
			return filterConfig.getInitParameter(s);
		}

		@Override
		public Enumeration<String> getInitParameterNames() {
			return filterConfig.getInitParameterNames();
		}

		@Override
		public ServletContext getServletContext() {
			return filterConfig.getServletContext();
		}

		@Override
		public String getServletName() {
			return filterConfig.getFilterName();
		}

	}

}
