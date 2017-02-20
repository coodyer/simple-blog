package com.blog.comm.base;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.blog.comm.util.JdbcUtil;
import com.blog.comm.util.PropertUtil;

@SuppressWarnings("serial")
public class BaseModel implements Serializable{

	@JSONField(serialize=false)  
	public String table(){
		String domainName=this.getClass().getSimpleName();
		String table=JdbcUtil.unParsParaName(domainName);
		return table;
	}

	@Override
	@JSONField(serialize=false)  
	public String toString() {
		return JSON.toJSONString(this);
	}
	@JSONField(serialize=false)  
	public void initModel(){
		Field [] fields=this.getClass().getDeclaredFields();
		try {
			for(Field field:fields){
				if(Number.class.isAssignableFrom(field.getType())){
					PropertUtil.setProperties(this, field.getName(),PropertUtil.parseValue(0, field.getType()));
				}
				if(Date.class.isAssignableFrom(field.getType())){
					PropertUtil.setProperties(this, field.getName(), new Date());
				}
				if(String.class.isAssignableFrom(field.getType())){
					PropertUtil.setProperties(this, field.getName(), "");
				}
			}
		} catch (Exception e) {
		}
	}
}
