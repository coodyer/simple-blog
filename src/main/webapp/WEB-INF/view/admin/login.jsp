<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<html lang="en">
<jsp:include page="base/title.jsp" />
<body data-type="login">
	<script src="assets/js/theme.js"></script>
	<div class="am-g tpl-g">
		<jsp:include page="base/theme.jsp" />
		<div class="tpl-login">
			<div class="tpl-login-content">
				<center><span>后台登录</span></center>
				<form class="am-form tpl-form-line-form">
					<div class="am-form-group">
						<input type="text" class="tpl-form-input" id="user-name"
							placeholder="请输入账号">
					</div>
					<div class="am-form-group">
						<input type="password" class="tpl-form-input" id="user-name"
							placeholder="请输入密码">

					</div>
					<div class="am-form-group">
						<input type="text" class="tpl-form-input left" id="user-name" style="width: 75%"
							placeholder="请输入验证码">
						<img src="verCode.${suffix }" 
						style="cursor: hand;"
						onclick="loadCode()" width="20%" class="right" height="32rem" id="currVerCode">
					</div>
					<div class="am-form-group tpl-login-remember-me">
						<input id="remember-me" type="checkbox"> <label
							for="remember-me"> 记住密码 </label>

					</div>
					<div class="am-form-group">

						<button type="button"
							class="am-btn am-btn-primary  am-btn-block tpl-btn-bg-color-success  tpl-login-btn">提交</button>
					</div>
				</form>
			</div>
		</div>
	</div>
	<script src="assets/js/amazeui.min.js"></script>
	<script src="assets/js/app.js"></script>
</body>
<style>
.left{
	float: left;
}
.right{
	float: right;
}
</style>
<script>
function loadCode(){
	var timestamp = (new Date()).valueOf();
	var imgSrc="verCode.${suffix }?"+timestamp+Math.round(Math.random() * 1000000);
	setTimeout(function() {
			document.getElementById("currVerCode").src = imgSrc;
		}, 0);
	
}
</script>
</html>
