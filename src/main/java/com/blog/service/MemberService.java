package com.blog.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.blog.comm.annotation.CacheWipe;
import com.blog.comm.annotation.CacheWrite;
import com.blog.comm.avafinal.CacheFinal;
import com.blog.comm.entity.Pager;
import com.blog.comm.handle.JdbcHandle;
import com.blog.model.MemberInfo;

@Service
public class MemberService {

	@Resource
	JdbcHandle jdbcHandle;
	
	@CacheWrite(key=CacheFinal.MEMBER_INFO,validTime=600,fields="userName")
	public MemberInfo loadMemberInfo(String userName){
		return (MemberInfo) jdbcHandle.findBeanFirst(MemberInfo.class, "userName", userName);
	}
	@CacheWrite(key=CacheFinal.MEMBER_INFO,validTime=600,fields="memberId")
	public MemberInfo loadMemberInfo(Integer memberId){
		return (MemberInfo) jdbcHandle.findBeanFirst(MemberInfo.class, "id", memberId);
	}
	@CacheWipe(key=CacheFinal.MEMBER_INFO,fields="user.id")
	@CacheWipe(key=CacheFinal.MEMBER_INFO,fields="user.userName")
	public void saveMemberInfo(MemberInfo user){
		jdbcHandle.saveOrUpdateAuto(user);
	}
	
	@CacheWrite(key=CacheFinal.MEMBER_PAGER,validTime=1)
	public Pager loadMemberPager(MemberInfo member,Pager pager){
		if(member==null){
			member=new MemberInfo();
		}
		return jdbcHandle.findPager(member, pager,"id",true);
	}
	
}
