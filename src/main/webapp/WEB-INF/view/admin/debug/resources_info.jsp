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
								<div class="widget-title  am-cf">文件内容</div>
								<p class="page-header-description">当前文件：${file }</p>
							</div>
							<div id="collapse-panel-4" class="am-panel-bd am-collapse am-in">
								<div class="am-g">
									<fieldset>
										<c:if test="${!empty context}">
											<form class="am-form am-form-horizontal"
												onsubmit="return false">
												<div class="am-form-group">
													<label for="doc-vld-ta-2"><small>文件内容：</small></label>
													<textarea id="doc-vld-ta-2" name="remark" rows="25">${context}</textarea>
												</div>
											</form>
										</c:if>
										<c:if test="${!empty classInfo }">
											<div class="am-form-group" id="classInfo">
												<c:forEach items="${classInfo.annotations }"
													var="annotation">
													<span class="annotation">@${annotation.annotationType }
														<c:if test="${!empty annotation.fields }">
																(${annotation.fields })
															</c:if>

													</span>
													<br>
												</c:forEach>
												<label for="doc-vld-ta-2"><small><span
														class="pub">${classInfo.modifier}&nbsp;${field.isFinal?'final':'' }&nbsp;${classInfo.isAbstract?'abstract':''}${classInfo.isInterface?'interface':''}${classInfo.isEnum?'enum':''}</span>&nbsp;${classInfo.name}
														<c:if test="${!empty classInfo.superClass }">&nbsp;<b
																class="pub">extends</b>&nbsp;${classInfo.superClass }</c:if> <c:if
															test="${!empty classInfo.interfaces }">&nbsp;<b
																class="pub">implements</b>&nbsp;
												<c:forEach items="${classInfo.interfaces }" var="interf"
																varStatus="index">
												${interf.name }
												<c:if
																	test="${index.index+1!=fn:length(classInfo.interfaces) }">,</c:if>
															</c:forEach>
														</c:if> </small></label>
												<c:if test="${!empty classInfo.enumInfo }">
													<table class="am-table am-table-bordered">
														<c:forEach items="${classInfo.enumInfo }" var="enumInfo">
															<tr>
																<td><span class="blue">${enumInfo.key }</span>(${enumInfo.value.getMap() })
																</td>
															</tr>
														</c:forEach>
													</table>
												</c:if>
												<table class="am-table am-table-bordered">
													<thead>
														<tr>
															<th>字段列表</th>
														</tr>
													</thead>
													<tbody>
														<c:if test="${empty classInfo.fields}">
															<tr>
																<td colspan="1" style="text-align:center ">暂无字段</td>
															</tr>
														</c:if>
														<c:forEach items="${classInfo.fields}" var="field"
															varStatus="index">
															<tr>
																<td>
																	<form id="fieldForm${index.index }"
																		onsubmit="return false">
																		<div style="float: left">
																			<c:forEach items="${field.annotations }"
																				var="annotation">
																				<span class="annotation">@${annotation.annotationType }
																					<c:if test="${!empty annotation.fields }">
																(${annotation.fields })
															</c:if>
																				</span>
																				<br>
																			</c:forEach>
																			<b class="pub">${field.modifier }&nbsp;${field.isStatic?'static':'' }&nbsp;${field.isFinal?'final':'' }&nbsp;</b>
																			${field.fieldType.name}&nbsp;&nbsp;<span class="para">${field.fieldName }</span>
																			&nbsp;=&nbsp; <input class="blue am-monospace"
																				style="border-style:none" name="fieldValue"
																				value="${field.fieldValue==null?'null':field.fieldValue }">
																		</div>
																		<input type="hidden" name="file" value="${file }">
																		<input type="hidden" name="fieldName"
																			value="${field.fieldName }"> <input
																			type="button" onclick="saveField(${index.index})"
																			value="保存"
																			class="am-btn am-btn-default am-btn-xs right">
																	</form>
																</td>
															</tr>
														</c:forEach>
													</tbody>
												</table>
												<table class="am-table
								 am-table-bordered">
													<thead>
														<tr>
															<th>方法列表</th>
														</tr>
													</thead>
													<tbody>
														<c:if test="${empty classInfo.methods}">
															<tr>
																<td colspan="1" style="text-align:center ">暂无方法</td>
															</tr>
														</c:if>
														<c:forEach items="${classInfo.methods}" var="method"
															varStatus="seq">
															<tr>
																<td>
																	<form name="form${seq.index+1 }"
																		action="serverMonitor.${suffix}" method="POST">
																		<input type="hidden" name="file" value="${file }"><input
																			type="hidden" name="key" value="${method.key }">
																		<c:forEach items="${method.annotations }"
																			var="annotation">
																			<span class="annotation">@${annotation.annotationType }
																				<c:if test="${!empty annotation.fields }">
																(${annotation.fields })
															</c:if>
																			</span>
																			<br>
																		</c:forEach>
																		<b class="pub">${method.modifier }&nbsp;${method.isStatic?'static':'' }&nbsp;${method.isFinal?'final':'' }&nbsp;
																			${method.isAbstract?'abstract':'' }&nbsp;${method.isSynchronized?'synchronized':'' }</b>&nbsp;${method.returnType.name }&nbsp;${method.name }(
																		<c:forEach items="${method.paramsType }" var="para"
																			varStatus="index">
																			<c:forEach items="${para.annotations }"
																				var="annotation">
																				<span class="annotation">@${annotation.annotationType }
																					<c:if test="${!empty annotation.fields }">
																(${annotation.fields })
															</c:if>
																				</span>
															&nbsp;
														</c:forEach> 
														${para.fieldType }&nbsp; <span class="para">${para.fieldName }</span>
																			<c:if
																				test="${index.index+1!=fn:length(method.paramsType) }">,</c:if>
																		</c:forEach>
																		); <a
																			href="javascript:document.form${seq.index+1 }.submit()"
																			class="am-btn am-btn-default am-btn-xs right">方法监听</a>
																	</form>
																</td>
															</tr>
														</c:forEach>
													</tbody>
												</table>
											</div>
										</c:if>
									</fieldset>
									</div>
								</div>
							<fieldset>
								<button type="button" class="am-btn am-btn-default am-fr right"
									onclick="javascript:history.back()">返回</button>
							</fieldset>
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
		function saveField(index) {
			var formName = "#fieldForm" + index;
			$.ajax({
				async : true,
				cache : false,
				type : "POST",
				dataType : 'json',
				data : $(formName).serialize(),
				url : 'modifyField.${suffix}',
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
	</script>
</body>
<style>
.t4 {
	font: 1.2rem 宋体;
	color: #800000;
}

.right {
	float: right;
}

.annotation {
	color: #949494;
}

.blue {
	color: blue;
}

.pub {
	color: #B74040;
}

.para {
	color: #776A6A;
}

body {
	font-size: 12px;
	word-break: break-all;
}

static.am-list-border>li {
	padding: 0.2rem;
}

.am-form input[type=number], .am-form input[type=search], .am-form input[type=text],
	.am-form input[type=password], .am-form input[type=datetime], .am-form input[type=datetime-local],
	.am-form input[type=date], .am-form input[type=month], .am-form input[type=time],
	.am-form input[type=week], .am-form input[type=email], .am-form input[type=url],
	.am-form input[type=tel], .am-form input[type=color], .am-form select,
	.am-form textarea, .am-form-field {
	font-size: 1.4rem;
}

td {
	max-width: 500px;
	word-break: break-all;
	min-width: 100px;
}

.admin-content {
	height: 80%;
}

footer {
	text-align: right;
	width: 100%;
	height: 25px;
}

hr, ol, p, pre, ul {
	margin: 0 0 0.6rem;
}

.am-padding {
	padding: 1.2rem;
}

.am-list, .am-topbar {
	margin-bottom: 0.6rem;
}

.am-panel {
	margin-bottom: 1px;
}

body, pre {
	line-height: 1.0;
}
</style>
</html>