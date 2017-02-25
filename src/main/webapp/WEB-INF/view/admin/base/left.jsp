<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%
String menuId=request.getParameter("menuId");
request.setAttribute("menuId", menuId);
%>
<!-- 侧边导航栏 -->
<div class="left-sidebar">
	<!-- 用户信息 -->
	<div class="tpl-sidebar-user-panel">
		<div class="tpl-user-panel-slide-toggleable">

			<div class="tpl-user-panel-profile-picture">
				<img src="${basePath }admin/assets/img/user04.png" alt="">
			</div>
			<span class="user-panel-logged-in-text"> <i
				class="am-icon-circle-o am-text-success tpl-user-panel-status-icon"></i>
				禁言小张
			</span> <a href="javascript:;" class="tpl-user-panel-action-link"> <span
				class="am-icon-pencil"></span> 账号设置
			</a>
		</div>
	</div>
	<!-- 菜单 -->
	<ul class="sidebar-nav">
		<c:forEach items="${menus }" var="menu">
			<li class="sidebar-nav-link"><a href="javascript:;"
				class="sidebar-nav-sub-title"> <i
					class="am-icon-table sidebar-nav-link-logo"></i> ${menu.title } <span
					class="am-icon-chevron-down am-fr am-margin-right-sm sidebar-nav-sub-ico"></span>
			</a>
				<ul class="sidebar-nav sidebar-nav-sub" style="display: block;">
					<c:forEach items="${menu.childMenus }" var="chileMenu">
						<li class="sidebar-nav-link">
						<a href="${basePath}${chileMenu.url }.${suffix}?menuId=${chileMenu.id}" ${chileMenu.id==menuId?'class=\'active\'':'' }> <span
								class="am-icon-angle-right sidebar-nav-link-logo"></span> ${chileMenu.title }
						</a></li>
					</c:forEach>
			
				</ul></li>

		</c:forEach>
	</ul>
</div>