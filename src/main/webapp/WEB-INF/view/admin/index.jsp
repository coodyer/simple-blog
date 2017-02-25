<%@page import="com.blog.comm.util.DateUtils"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>

<!DOCTYPE html>
<html lang="en">

<jsp:include page="base/title.jsp" />

<body data-type="widgets">
	<script src="assets/js/theme.js"></script>
	<div class="am-g tpl-g">
		<!-- 头部 -->
		<jsp:include page="base/header.jsp" />
		<jsp:include page="base/left.jsp" />
		<!-- 内容区域 -->
		<div class="tpl-content-wrapper">
			<div class="row-content am-cf">
				<div class="row">
					<div class="am-u-sm-12 am-u-md-12 am-u-lg-12">
						<div class="widget am-cf">
							<div class="widget-head am-cf">
								<div class="am-u-sm-12 am-u-md-12 am-u-lg-9">
									<div class="page-header-heading">
										<span class="am-icon-home page-header-heading-icon"></span>
										后台首页 <small>simple-blog</small>
									</div>
									<p class="page-header-description">服务器基本运行状态。</p>
								</div>
							</div>
							<div class="widget-body  widget-body-lg am-fr">
								<table width="100%"
									class="am-table am-table-compact am-table-striped tpl-table-black am-table-bordered"
									id="example-r">
									<tbody>
										<tr class="gradeX">
											<td>用户名：<span class="t4">${curr_login_user.userName }</span></td>
											<td>IP地址：<span class="t4">${serverIp }</span></td>
										</tr>
										<tr class="gradeX">
											<td>身份过期：<span class="t4">30 分钟</span></td>
											<td>现在时间：<span class="t4"><%=DateUtils.dateToString(new Date(),
					DateUtils.DATETIME_PATTERN)%></span></td>
										</tr>
										<tr class="gradeX">
											<td>物理路径：<span class="t4"><%=request.getServletContext().getRealPath("")%></span></td>
											<td>上线时间: <span class="t4"><%=DateUtils.dateToString((Date) request.getSession()
					.getAttribute("loginTime"), DateUtils.DATETIME_PATTERN)%></span></td>
										</tr>
										<tr class="gradeX">
											<td>服务器域名：<span class="t4">${basePath }</span></td>
											<td>脚本解释引擎：<span class="t4"><%=response.getHeader("X-Powered-By")%></span></td>
										</tr>
										<tr class="gradeX">
											<td>服务器软件的名称：<span class="t4"><%=response.getHeader("Server")%></span></td>
											<td>浏览器版本：<span class="t4"><%=request.getHeader("User-Agent")%></span></td>
										</tr>
										<tr class="gradeX">
											<td>FSO文本读写:<b>√</b></td>
											<td>数据库使用:<b>√</b></td>
										</tr>
										<tr class="gradeX">
											<td>Jmail组件支持:<b>√</b></td>
											<td>CDONTS组件支持:<b>√</b></td>
										</tr>
									</tbody>
								</table>

							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script src="assets/js/amazeui.min.js"></script>
	<script src="assets/js/amazeui.datatables.min.js"></script>
	<script src="assets/js/dataTables.responsive.min.js"></script>
	<script src="assets/js/app.js"></script>
</body>
<style>
.t4 {
	font: 1.2rem 宋体;
	color: #800000;
}
</style>
</html>