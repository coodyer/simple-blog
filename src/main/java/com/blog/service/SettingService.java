package com.blog.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.blog.comm.annotation.CacheWrite;
import com.blog.comm.avafinal.CacheFinal;
import com.blog.comm.handle.JdbcHandle;
import com.blog.model.Setting;

@Service
public class SettingService {
	@Resource
	JdbcHandle jdbcHandle;
	
	/**
	 * 加载网站设置
	 * @return
	 */
	@CacheWrite(key=CacheFinal.SETTING_INFO,validTime=72000)
	public Setting loadSiteSetting(){
		return jdbcHandle.findBeanFirst(Setting.class);
	}
	
}
