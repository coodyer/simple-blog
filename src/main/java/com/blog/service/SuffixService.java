package com.blog.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.blog.comm.annotation.CacheWrite;
import com.blog.comm.avafinal.CacheFinal;
import com.blog.comm.handle.JdbcHandle;
import com.blog.comm.util.PropertUtil;
import com.blog.comm.util.StringUtil;
import com.blog.model.Suffix;
import com.blog.model.SuffixStatic;


@Service
public class SuffixService {

	@Resource
	JdbcHandle jdbcHandle;
	
	@CacheWrite(key=CacheFinal.SPRING_SUFFIXS,validTime=3600)
	public List<String> loadSpringSuffixs(){
		//执行语句：select * from suffix where status in (1,2)
		List<Suffix> suffixs= jdbcHandle.findBean(Suffix.class,"status",new Integer[]{1,2});
		if(StringUtil.isNullOrEmpty(suffixs)){
			return new ArrayList<String>();
		}
		return PropertUtil.getFieldValues(suffixs, "suffix");
	}
	@CacheWrite(key=CacheFinal.DEFAULT_SUFFIXS,validTime=3600)
	public String loadSpringDefaultSuffix(){
		//执行语句：select * from suffix where status =2 limit 1
		Suffix suffix=jdbcHandle.findBeanFirst(Suffix.class,"status",2);
		if(StringUtil.isNullOrEmpty(suffix)){
			return "do";
		}
		return suffix.getSuffix();
	}
	
	@CacheWrite(key=CacheFinal.STA_SUFFIX ,validTime=3600)
	public List<String> loadStaSuffix() {
		//执行语句：select * from suffix_static
		List<SuffixStatic> suffixs= jdbcHandle.findBean(SuffixStatic.class);
		if(StringUtil.isNullOrEmpty(suffixs)){
			return new ArrayList<String>();
		}
		return PropertUtil.getFieldValues(suffixs, "suffix");
	}
}
