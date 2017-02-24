<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<html lang="en">
<jsp:include page="base/title.jsp" />
<body data-type="index">
	<script src="assets/js/theme.js"></script>
	<div class="am-g tpl-g">
		<jsp:include page="base/header.jsp" />
		<jsp:include page="base/left.jsp" />

		<!-- 内容区域 -->
		<div class="tpl-content-wrapper"></div>
	</div>
	<script src="assets/js/amazeui.min.js"></script>
	<script src="assets/js/amazeui.datatables.min.js"></script>
	<script src="assets/js/dataTables.responsive.min.js"></script>
	<script src="assets/js/app.js"></script>
</body>

</html>