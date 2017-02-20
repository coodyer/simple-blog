package com.blog.controller.admin;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blog.comm.annotation.Power;
import com.blog.comm.avafinal.CacheFinal;
import com.blog.comm.base.BaseController;
import com.blog.comm.cache.LocalCache;
import com.blog.comm.entity.BeanEntity;
import com.blog.comm.entity.CtBeanEntity;
import com.blog.comm.entity.CtClassEntity;
import com.blog.comm.entity.CtMethodEntity;
import com.blog.comm.entity.MonitorEntity;
import com.blog.comm.entity.MsgEntity;
import com.blog.comm.entity.WsFileEntity;
import com.blog.comm.util.AspectUtil;
import com.blog.comm.util.FileUtils;
import com.blog.comm.util.PrintException;
import com.blog.comm.util.PropertUtil;
import com.blog.comm.util.SimpleUtil;
import com.blog.comm.util.SpringContextHelper;
import com.blog.comm.util.StringUtil;

@Controller
public class DebugController extends BaseController {
	private static final String DIR = "admin/debug/";

	@RequestMapping(value = "/admin/debug/superIndex.do")
	@Power("superIndex")
	public String superIndex(HttpServletRequest req, HttpServletResponse res) {
		return DIR + "base";
	}

	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/admin/debug/fileList.do")
	@Power("fileSetting")
	public String fileList(HttpServletRequest req, HttpServletResponse res) {
		String path = getPara("file");
		if (StringUtil.isNullOrEmpty(path)) {
			path = Thread.currentThread().getContextClassLoader()
					.getResource("").getPath();
		}
		path = new File(path).getPath().replace("\\", "/");
		String basePath = Thread.currentThread().getContextClassLoader()
				.getResource("").getPath();
		basePath = new File(basePath).getPath().replace("\\", "/");
		basePath=URLDecoder.decode(basePath);
		path=URLDecoder.decode(path);
		if (!path.startsWith(basePath)) {
			return DIR + "server_list";
		}
		File[] files = new File(path).listFiles();
		List<WsFileEntity> fileEntitys = FileUtils.parseWsFile(files);
		fileEntitys = PropertUtil.doSeqDesc(fileEntitys, "suffix");
		fileEntitys = PropertUtil.doSeq(fileEntitys, "path");
		fileEntitys = PropertUtil.doSeq(fileEntitys, "type");
		setAttribute("files", fileEntitys);
		String currFile = new File(path).getPath() + "/";
		if (SimpleUtil.isWindows()) {
			currFile = currFile.replace("/", "\\");
		}
		System.out.println(currFile);
		setAttribute("currFile", currFile);
		setAttribute("parentFile", new File(path).getParent());
		return DIR + "server_list";
	}

	@RequestMapping(value = "/admin/debug/fileInfo.do")
	@Power("fileSetting")
	public String fileInfo(HttpServletRequest req, HttpServletResponse res) {
		loadClassEntity();
		return DIR + "server_info";
	}

	@RequestMapping(value = "/admin/debug/monitorList.do")
	@Power("monitorSetting")
	public String monitorList(HttpServletRequest req, HttpServletResponse res) {
		/**
		 * 加载我的监听列表
		 */
		List<String> keys = LocalCache.getKeysFuzz(CacheFinal.SYSTEM_RUN_INFO);
		setAttribute("keys", keys);
		return DIR + "monitor_list";
	}
	@RequestMapping(value = "/admin/debug/modifyField.do")
	@Power("fileSetting")
	@ResponseBody
	public Object modifyField(HttpServletRequest req, HttpServletResponse res) {
		CtClassEntity clazz=loadClassEntity();
		String fieldName=getPara("fieldName");
		String value=getPara("fieldValue");
		Object bean=null;
		try {
			bean = SpringContextHelper.getBean(clazz.getSourceClass());
		} catch (Exception e) {
			// TODO: handle exception
		}
		for(CtBeanEntity field:clazz.getFields()){
			try {
				System.out.println(field.getFieldName()+":"+fieldName);
				if(field.getFieldName().equals(fieldName)){
					Field sourceField=field.getSourceField();
					sourceField.setAccessible(true);
					try {
						Field modifiersField = Field.class.getDeclaredField("modifiers");
						modifiersField.setAccessible(true);
						modifiersField.set(sourceField, sourceField.getModifiers() & ~Modifier.FINAL);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if(field.getIsStatic()==true){
						bean=null;
					}
					sourceField.set(bean, PropertUtil.parseValue(value, field.getFieldType()));
					return new MsgEntity(-1,"操作成功");
				}
			} catch (Exception e) {
				e.printStackTrace();
				return  new MsgEntity(-1,"修改失败");
			}
		}
		return new MsgEntity(-1,"字段不存在");
	}

	@SuppressWarnings("deprecation")
	private CtClassEntity loadClassEntity() {
		String path = getPara("file");
		keepParas();
		if (StringUtil.isNullOrEmpty(path)) {
			return null;
		}
		String basePath = Thread.currentThread().getContextClassLoader()
				.getResource("").getPath();
		basePath = new File(basePath).getPath().replace("\\", "/") + "/";
		basePath=URLDecoder.decode(basePath);
		path = path.replace("\\", "/");
		path=URLDecoder.decode(path);
		if (!path.startsWith(basePath)) {
			return null;
		}
		while (path.contains("../")) {
			path = path.replace("../", "");
		}
		if (!path.endsWith(".class")) {
			File file = new File(path);
			if (file.length() < 1048576) {
				String info = FileUtils.readFile(path);
				setAttribute("context", info);
			}
			return null;
		}
		try {
			String packet = path.replace(basePath, "");
			packet = packet.replace("/", ".");
			packet = packet.replace(".class", "");
			Class<?> clazz = Class.forName(packet);
			CtClassEntity classInfo = SimpleUtil.getClassEntity(clazz);
			setAttribute("classInfo", classInfo);
			return classInfo;
		} catch (Exception e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/admin/debug/serverMonitor.do")
	@Power("monitorSetting")
	public String serverMonitor(HttpServletRequest req, HttpServletResponse res) {
		keepParas();
		String key = getPara("key");
		Method sourceMethod = SimpleUtil.getMethodByKey(key);
		CtClassEntity classInfo = SimpleUtil.getClassEntity(PropertUtil
				.getClass(sourceMethod));
		CtMethodEntity method = (CtMethodEntity) PropertUtil.getByList(
				classInfo.getMethods(), "key", key);
		setAttribute("method", method);
		setAttribute("isRun", 0);
		if (LocalCache.contains(key)) {
			setAttribute("isRun", 1);
			List<MonitorEntity> monitors = (List<MonitorEntity>) LocalCache
					.getCache(key);
			monitors = PropertUtil.doSeqDesc(monitors, "runTime");
			setAttribute("monitors", monitors);
		}
		setAttribute("classInfo", classInfo);
		/**
		 * 初始化方法参数
		 */
		Object obj = SimpleUtil.initMethodParas(method.getSourceMethod());
		String initParas = JSON.toJSONString(obj);
		setAttribute("initParas", initParas);
		return DIR + "server_monitor";
	}

	@RequestMapping(value = "/admin/debug/serverDebug.do")
	@Power("fileSetting")
	public void serverDebug(HttpServletRequest req, HttpServletResponse res) {
		keepParas();
		String key = getPara("key");
		String runData = getPara("input");
		Method method = SimpleUtil.getMethodByKey(key);
		if (method == null) {
			printMsg(res, new MsgEntity(-1, "方法未找到"));
			return;
		}
		method.setAccessible(true);
		Object[] paras = null;
		if (!StringUtil.isNullOrEmpty(runData)) {
			paras = JSON.parseObject(runData, Object[].class);
		}
		List<BeanEntity> entitys = PropertUtil.getMethodParas(method);
		if (!StringUtil.isNullOrEmpty(paras)) {
			if (paras.length != entitys.size()) {
				printMsg(res, new MsgEntity(-1, "参数数量有误"));
				return;
			}
			for (int i = 0; i < paras.length; i++) {
				BeanEntity entity = entitys.get(i);
				Object value = null;
				try {
					value = PropertUtil.parseValue(paras[i],
							entity.getFieldType());
					if (JSONObject.class.isAssignableFrom(value.getClass())) {
						value = JSON.parseObject(paras[i].toString(),
								entity.getFieldType());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				paras[i] = value;
			}
		}
		try {
			AspectUtil.createDebugKey(key);
			Class<?> clazz = PropertUtil.getClass(method);
			if (Modifier.isStatic(method.getModifiers())) {
				Object result = method.invoke(null, paras);
				printMsg(res,
						new MsgEntity(0, "操作成功", JSON.toJSONString(result)));
				return;
			}
			Object bean = SpringContextHelper.getBean(clazz);
			if (!StringUtil.isNullOrEmpty(bean)) {
				Class<?> sourceClass = SimpleUtil.getMethodClassByKey(key);
				if (!sourceClass.getName().equals(bean.getClass().getName())) {
					bean = sourceClass.cast(bean);
					PropertUtil.setProperties(method, "clazz", sourceClass);
				}
				Object result = method.invoke(bean, paras);
				printMsg(res,
						new MsgEntity(0, "操作成功", JSON.toJSONString(result)));
				return;
			}
			bean = clazz.newInstance();
			Class<?> sourceClass = SimpleUtil.getMethodClassByKey(key);
			if (!sourceClass.getName().equals(bean.getClass().getName())) {
				bean = sourceClass.cast(bean);
				PropertUtil.setProperties(method, "clazz", sourceClass);
			}
			Object result = method.invoke(bean, paras);
			printMsg(res, new MsgEntity(0, "操作成功", JSON.toJSONString(result)));
			return;
		} catch (Exception e) {
			e.printStackTrace();
			printMsg(
					res,
					new MsgEntity(-1, "执行出错", PrintException
							.getErrorStack(e, 0)));
			return;
		} finally {
			AspectUtil.cleanDebugKey();
		}
	}

	@RequestMapping(value = "/admin/debug/serverDoMonitor.do")
	@Power("fileSetting")
	public void serverDoMonitor(HttpServletRequest req, HttpServletResponse res) {
		String key = getPara("key");
		Integer isRun = getParaInteger("isRun");
		keepParas();
		if (StringUtil.findNull(key, isRun) > -1) {
			printMsg(res, new MsgEntity(-1, "参数有误"));
			return;
		}
		Class<?> clazz = PropertUtil.getClass(SimpleUtil
				.getMethodByKey(key));
		if (StringUtil.isNullOrEmpty(clazz)) {
			printMsg(res, new MsgEntity(-1, "该方法不能监听"));
			return;
		}
		Object bean = SpringContextHelper.getBean(clazz);
		if (StringUtil.isNullOrEmpty(bean)) {
			printMsg(res, new MsgEntity(-1, "该方法不能监听,未找到Bean"));
			return;
		}
		if (1 == isRun) {
			LocalCache.setCache(key, new ArrayList<MonitorEntity>());
		} else {
			LocalCache.delCache(key);
		}
		printMsg(res, new MsgEntity(0, "操作成功"));
		return;
	}

	@RequestMapping(value = "/admin/debug/cacheSetting.do")
	@Power("cacheSetting")
	public String cacheSetting(HttpServletRequest req, HttpServletResponse res) {
		List<CtBeanEntity> entitys = SimpleUtil.getBeanFields(CacheFinal.class);
		setAttribute("entitys", entitys);
		return DIR + "cache_list";
	}

	@RequestMapping(value = "/admin/debug/cacheClean.do")
	@Power("cacheSetting")
	public void cacheClean(HttpServletRequest req, HttpServletResponse res) {
		String key = getPara("key");
		if (StringUtil.isNullOrEmpty(key)) {
			printMsg(res, new MsgEntity(-1, "参数有误"));
			return;
		}
		LocalCache.delCacheFuzzy(key);
		printMsg(res, new MsgEntity(0, "操作成功"));
		return;
	}
}
