package com.blog.model;
// default package


import java.util.Date;

import com.blog.comm.base.BaseModel;

/**
 * MemberInfo entity. @author MyEclipse Persistence Tools
 */

@SuppressWarnings("serial")
public class MemberInfo extends BaseModel{

	// Fields

	private Integer id;
	private String userName;
	private String userPwd;
	private Integer roleId;
	private Integer status;
	private Date createTime;
	private Date updateTime;

	
	
	// Constructors

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	/** default constructor */
	public MemberInfo() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}


	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}