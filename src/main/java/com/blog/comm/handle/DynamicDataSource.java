package com.blog.comm.handle;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.blog.comm.util.AspectUtil;
import com.blog.comm.util.StringUtil;

/**
 * 动态数据源
 * @author wangzh
 *
 */
public class DynamicDataSource extends AbstractRoutingDataSource  {
	@Override
	protected Object determineCurrentLookupKey() {
		String template=AspectUtil.getCurrDBTemplate();
		if(StringUtil.isNullOrEmpty(template)){
			return "defaultTargetDataSource";
		}
		return template;
		//通过此处获得当前数据源 
	}
}
