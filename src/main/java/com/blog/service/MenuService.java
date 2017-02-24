package com.blog.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.blog.comm.annotation.CacheWrite;
import com.blog.comm.annotation.DeBug;
import com.blog.comm.avafinal.CacheFinal;
import com.blog.comm.handle.JdbcHandle;
import com.blog.comm.util.PropertUtil;
import com.blog.comm.util.StringUtil;
import com.blog.model.MemberRole;
import com.blog.model.SysMenus;
import com.blog.service.schema.MenuSchema;


@Service
public class MenuService {
	
	@Resource
	RoleService roleService;

	@Resource
	JdbcHandle jdbcHandle;
	
	
	@CacheWrite(key=CacheFinal.MENUS_LIST,validTime=600,fields="roleId")
	@DeBug
	public List<MenuSchema> loadMenus(Integer roleId){
		List<SysMenus> menus=loadSourceMenus(roleId);
		return parseMenus(menus);
	}
	@CacheWrite(key=CacheFinal.MENUS_SOURCES,validTime=600,fields="roleId")
	public List<SysMenus> loadSourceMenus(Integer roleId){
		MemberRole role=roleService.loadRole(roleId);
		Integer[] menuIds=StringUtil.splitByMosaicIntegers(role.getMenus(), ","); 
		List<SysMenus> menus=jdbcHandle.findBean(SysMenus.class,"id",menuIds);
		return menus;
	}
	@CacheWrite(key=CacheFinal.MENUS_LIST,validTime=7200)
	@DeBug
	public List<MenuSchema> loadAllMenus(){
		List<SysMenus> menus=jdbcHandle.findBean(SysMenus.class);
		return parseMenus(menus);
	}
	@CacheWrite(key=CacheFinal.MENUS_LIST,validTime=7200)
	public List<MenuSchema> parseMenus(List<SysMenus> menus){
		List<MenuSchema> schemas=new ArrayList<MenuSchema>();
		for(SysMenus menu:menus){
			if(menu.getType()!=0){
				continue;
			}
			List<MenuSchema> childSchemas=new ArrayList<MenuSchema>();
			for(SysMenus childMenu:menus){
				if(childMenu.getType()!=1){
					continue;
				}
				if(childMenu.getUpId().intValue()==menu.getId().intValue()){
					MenuSchema schema=new MenuSchema();
					BeanUtils.copyProperties(childMenu, schema);
					childSchemas.add(schema);
				}
			}
			childSchemas=PropertUtil.doSeq(childSchemas, "seq");
			MenuSchema schema=new MenuSchema();
			BeanUtils.copyProperties(menu, schema);
			schema.setChildMenus(childSchemas);
			schemas.add(schema);
		}
		schemas=PropertUtil.doSeq(schemas, "seq");
		return schemas;
	}
	
	public static void main(String[] args) {
		Integer [] ints=new Integer[47];
		for(int i=1;i<48;i++){
			ints[i-1]=i;
		}
		System.out.println(StringUtil.collectionMosaic(ints, ","));
	}
}
