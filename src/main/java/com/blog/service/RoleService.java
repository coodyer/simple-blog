package com.blog.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.blog.comm.annotation.CacheWrite;
import com.blog.comm.avafinal.CacheFinal;
import com.blog.comm.handle.JdbcHandle;
import com.blog.model.MemberRole;

@Service
public class RoleService {

	@Resource
	JdbcHandle jdbcHandle;
	
	@CacheWrite(key=CacheFinal.USER_ROLE_INFO,validTime=600)
	public MemberRole loadRole(Integer roleId){
		return jdbcHandle.findBeanFirst(MemberRole.class,"id",roleId);
	}
	
}
