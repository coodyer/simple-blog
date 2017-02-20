package com.blog.comm.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UrlPathHelper;

import com.blog.comm.base.BaseModel;
import com.blog.comm.entity.BeanEntity;

/**
 * @remark HTTP工具类。
 * @author WebSOS
 * @email 644556636@qq.com
 * @blog 54sb.org
 */
public class RequestUtil {

	public static final String user_session = "curr_login_user";
	private static final AntPathMatcher MATCHER = new AntPathMatcher();

	public static void setUser(HttpServletRequest request, Object user) {
		request.getSession().setAttribute(user_session, user);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getUser(HttpServletRequest request) {
		return (T) request.getSession().getAttribute(user_session);
	}
	public static HttpServletRequest getRequest(){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();  
		return  request;  
	}
	public static boolean isUserLogin(HttpServletRequest request) {
		Object obj = getUser(request);
		if (!StringUtil.isNullOrEmpty(obj)) {
			return true;
		}
		return false;
	}

	public static void setCode(HttpServletRequest request, Object code) {
		request.getSession().setAttribute("sys_ver_code", code);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getCode(HttpServletRequest request) {
		return (T) request.getSession().getAttribute("sys_ver_code");
	}

	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("Real-IP");
		if (StringUtil.isNullOrEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Real-IP");
		}
		if (StringUtil.isNullOrEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Forwarded-For");
		}
		if (!StringUtil.isNullOrEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
			// 多次反向代理后会有多个IP值，第一个为真实IP。
			int index = ip.indexOf(',');
			if (index != -1) {
				return ip.substring(0, index);
			}
			return ip;

		}
		return request.getRemoteAddr();
	}

	public static String getURLSuffix(HttpServletRequest request) {
		String url = request.getServletPath();
		String[] tab = url.split("\\.");
		if (tab.length > 1) {
			return tab[tab.length - 1];
		}
		return "";
	}

	/**
	 * 获得当的访问路径
	 * 
	 * HttpServletRequest.getRequestURL+"?"+HttpServletRequest.getQueryString
	 * 
	 * @param request
	 * @return
	 */
	public static String getLocation(HttpServletRequest request) {
		UrlPathHelper helper = new UrlPathHelper();
		StringBuffer buff = request.getRequestURL();
		String uri = request.getRequestURI();
		String origUri = helper.getOriginatingRequestUri(request);
		buff.replace(buff.length() - uri.length(), buff.length(), origUri);
		String queryString = helper.getOriginatingQueryString(request);
		if (queryString != null) {
			buff.append("?").append(queryString);
		}
		return buff.toString();
	}


	/**
	 * 根据Request获取Model。排除指定参数
	 * 
	 * @param request
	 *            请求对象
	 * @param obj
	 *            实例化的Model对象
	 * @param paraArgs
	 *            指定参数
	 * @return
	 */
	public static <T> T getBeanRemove(HttpServletRequest request, String paraName,Object obj,
			String... paraArgs) {
		return getBean(request, obj, null, paraName, null, true, paraArgs);
	}

	/**
	 * 根据Request获取Model。接受指定参数
	 * 
	 * @param request
	 *            请求对象
	 * @param obj
	 *            实例化的Model对象
	 * @param paraArgs
	 *            指定参数
	 * @return
	 */
	public static <T> T getBeanAccept(HttpServletRequest request,String paraName, Object obj,
			String... paraArgs) {
		return getBean(request, obj, null, paraName, null, false, paraArgs);
	}

	/**
	 * 根据Request获取Model所有参数
	 * 
	 * @param request
	 *            请求对象
	 * @param obj
	 *            实例化的Model对象
	 * @return
	 */
	public static <T> T getBeanAll(HttpServletRequest request, String paraName,Object obj) {
		return getBean(request, obj, null, paraName, null, true, null);
	}

	@SuppressWarnings("unchecked")
	private static <T> T getBean(HttpServletRequest request, Object obj,
			List<BeanEntity> fields, String baseName, String firstSuffix,
			Boolean isReplace, String[] paraArgs) {
		try {
			if (obj instanceof Class) {
				obj = ((Class<?>) obj).newInstance();
			}
			firstSuffix = StringUtil.isNullOrEmpty(firstSuffix) ? "" : (firstSuffix+".");
			isReplace = StringUtil.isNullOrEmpty(isReplace) ? false : isReplace;
			baseName = StringUtil.isNullOrEmpty(baseName) ? "" : (baseName+".");
			List<String> paras = null;
			if (!StringUtil.isNullOrEmpty(paraArgs)) {
				paras = new ArrayList<String>();
				for (String tmp : paraArgs) {
					if (StringUtil.isNullOrEmpty(tmp)) {
						continue;
					}
					String[] tab = tmp.split("\\.");
					for (String tmpTab : tab) {
						paras.add(tmpTab);
					}
					paras.add(tmp);
				}
			}
			if (StringUtil.isNullOrEmpty(fields)) {
				// 获取对象字段属性
				fields = PropertUtil.getBeanFields(obj);
			}
			firstSuffix = (firstSuffix == null) ? "" : firstSuffix;
			Object childObj, paraValue = null;
			String paraName = null;
			for (BeanEntity entity : fields) {
				try {
					paraName = firstSuffix + baseName + entity.getFieldName();
					if (!StringUtil.isNullOrEmpty(paras)) {
						if (isReplace) {
							if (paras.contains(paraName)) {
								continue;
							}
						}
						if (!isReplace) {
							if (!paras.contains(paraName)) {
								continue;
							}
						}
					}
					if (BaseModel.class.isAssignableFrom(entity.getFieldType())) {
						childObj = entity.getFieldType().newInstance();
						childObj = getBean(request, childObj, null, paraName,
								firstSuffix, isReplace, paraArgs);
						PropertUtil.setProperties(obj, entity.getFieldName(),
								childObj);
						continue;
					}
					paraValue = request.getParameter(paraName);
					if (StringUtil.isNullOrEmpty(paraValue)) {
						continue;
					}
					PropertUtil.setProperties(obj, entity.getFieldName(),
							paraValue);

				} catch (Exception e) {
				}
			}
			return (T) obj;
		} catch (Exception e) {
			PrintException.getErrorStack(e, 0);

		}
		return null;
	}

	public static void keepParas(HttpServletRequest request) {
		Enumeration<String> paras = request.getParameterNames();
		if (StringUtil.isNullOrEmpty(paras)) {
			return;
		}
		while (paras.hasMoreElements()) {
			try {
				String string = (String) paras.nextElement();
				if (StringUtil.isNullOrEmpty(string)) {
					continue;
				}
				request.setAttribute(string.replace(".", "_"),
						request.getParameter(string));
			} catch (Exception e) {
				PrintException.getErrorStack(e, 0);
			}

		}
	}

	public static boolean isFilterExcluded(ServletRequest request,
			String[] exculdeUrls) throws IOException, ServletException {
		if (exculdeUrls != null) {
			String uri = getRequestURI((HttpServletRequest) request);
			for (String url : exculdeUrls) {
				if (MATCHER.match(url, uri)) {
					return true;
				}
			}
		}
		return false;
	}

	public static String getRequestURI(HttpServletRequest request) {
		String uri = request.getRequestURI();
		String projectName = request.getContextPath();
		if (projectName != null && !projectName.trim().equals("")) {
			uri = uri.replace(projectName, "/");
		}
		uri = uri.replace("../", "/");
		while (uri.indexOf("//") > -1) {
			uri = uri.replace("//", "/");
		}
		return uri;
	}

	public static String getRequestCookies(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (StringUtil.isNullOrEmpty(cookies)) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (Cookie cook : cookies) {
			sb.append(cook.getName()).append("=").append(cook.getValue())
					.append(";");
		}
		return sb.toString();
	}

	public static void main(String[] args) {

	}

}
