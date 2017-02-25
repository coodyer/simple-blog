<%@page import="com.blog.comm.util.DateUtils"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>

<!DOCTYPE html>
<html lang="en">

<jsp:include page="../base/title.jsp" />

<body data-type="widgets">
	<script src="../assets/js/theme.js"></script>
	<div class="am-g tpl-g">
		<!-- 头部 -->
		<jsp:include page="../base/header.jsp" />
		<jsp:include page="../base/left.jsp" />
		<!-- 内容区域 -->
		<div class="tpl-content-wrapper">
			<div class="row-content am-cf">
				<div class="row">
					<div class="am-u-sm-12 am-u-md-12 am-u-lg-12">
						<div class="widget am-cf">
							<div class="widget-head am-cf">
								<div class="widget-title  am-cf">监听列表</div>
							</div>
							<div class="widget-body  am-fr">
								<div class="widget-head am-cf">
									<div class="widget-title  am-cf"></div>
								</div>
								<div class="am-u-sm-12">
									<table width="100%"
										class="am-table am-table-compact am-table-striped tpl-table-black "
										id="example-r">
										<thead>
											<tr>
												<th>方法</th>
												<th>操作</th>
											</tr>
										</thead>
										<tbody>
										<c:if test="${empty keys }">
										<tr class="even gradeC"><td colspan="2"><center>暂无数据</center></td></tr>
										</c:if>
											<c:forEach items="${keys }" var="key">
												<tr class="even gradeC">
													<td>${key}</td>
													<td>
														<div class="tpl-table-black-operation">
															<a href="serverMonitor.${suffix }?key=${key }"
																class=""> <i
																class="am-icon-archive"></i> 查看详情
															</a>
															<a href="javascript:cancelMonitor('${key }')"
																class=""> <i
																class="am-icon-trash"></i> 取消监听
															</a>
														</div>
													</td>
												</tr>
											</c:forEach>
											<!-- more data -->
										</tbody>
									</table>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script src="../assets/js/amazeui.min.js"></script>
	<script src="../assets/js/amazeui.datatables.min.js"></script>
	<script src="../assets/js/dataTables.responsive.min.js"></script>
	<script src="../assets/js/app.js"></script>
	<script>
		function cancelMonitor(key) {
			$.ajax({
				type : "POST",
				dataType : 'json',
				data : "isRun=0&key=" + key,
				url : 'serverDoMonitor.${suffix}',
				timeout : 60000,
				success : function(json) {
					alert(json.msg);
					if (json.code == 0) {
						location.reload(true);
					}
				},
				error : function() {
					alert("系统繁忙");
				}
			});
		}
	</script>
</body>
<style>
.t4 {
	font: 1.2rem 宋体;
	color: #800000;
}
</style>
</html>