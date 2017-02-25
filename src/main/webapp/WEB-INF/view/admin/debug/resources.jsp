<%@page import="com.blog.comm.util.DateUtils"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<%@ taglib prefix="fn" uri="/WEB-INF/tld/fn.tld"%>
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
								<div class="widget-title  am-cf">资源列表</div>
								<p class="page-header-description">当前目录:${currFile }</p>
							</div>
							<div class="widget-body  am-fr">
								<div class="am-u-sm-12">
									<table width="100%"
										class="am-table am-table-compact am-table-striped tpl-table-black "
										id="example-r">
										<thead>
											<tr>
												<th>文件名</th>
												<th>大小</th>
												<th>创建时间</th>
											</tr>
										</thead>
										<tbody>
											<tr class="even gradeC">
												<td colspan="3">
													<form method="post" name="fileFormBase" action="resources.${suffix }">
														<input type="hidden" name="file" value="${parentFile}">
														<a href="javascript:document.fileFormBase.submit()"> <img
															width="30px" src="${basePath}admin/assets/img/file.png" />上级目录
														</a>
													</form>
												</td>
											</tr>
											<c:forEach items="${files }" var="file" varStatus="index">
												<tr class="even gradeC">
													<td><c:if test="${file.type==0 }">
															<form method="post" name="fileForm${index.index }" >
																<input type="hidden" name="file" value="${file.path }">
																<a
																	href="javascript:document.fileForm${index.index}.submit()">
																	<img width="30px" src="${basePath}admin/assets/img/file.png" />${fn:replace(file.path, currFile, '')}
																</a>
															</form>
														</c:if> <c:if test="${file.type!=0 }">
															<form method="post" name="fileInfo${index.index }" action="resourcesInfo.${suffix }"
																>
																<input type="hidden" name="file" value="${file.path }">
																<a
																	href="javascript:document.fileInfo${index.index}.submit()">
																	<img width="30px"
																	src="${basePath}admin/assets/img/${file.suffix=='class'?'java':'txt' }.png" />${fn:replace(file.path, currFile, '')}
																</a>
															</form>
														</c:if></td>
													<td>${file.size==null?'-':file.size }</td>
													<td><fmt:formatDate value="${file.time }"
															pattern="yyyy-MM-dd HH:mm:ss" /></td>
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