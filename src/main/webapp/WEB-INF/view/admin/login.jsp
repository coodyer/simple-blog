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
				<center>后台登录</center>
				<form class="am-form tpl-form-line-form" id="loginForm" onsubmit="return false">
				<small id="notice" style="color: red" class="right"></small>
					<div class="am-form-group">
						<input type="text" class="tpl-form-input" id="user-name" name="username"
							placeholder="请输入账号" datatype="*4-32"
											errormsg="请输入正确的用户名(4-16位)" sucmsg="输入正确" nullmsg="请输入用户名" >
					</div>
					<div class="am-form-group">
						<input type="password" class="tpl-form-input" id="user-name" name="password"
							placeholder="请输入密码" datatype="*6-16"
											errormsg="请输入正确的密码(6-16位)！" sucmsg="输入正确" nullmsg="请输入密码" >

					</div>
					<div class="am-form-group">
						<input type="text" class="tpl-form-input left" id="user-name" style="width: 75%" name="verCode"
							placeholder="请输入验证码" datatype="n4-6" errormsg="请输入正确的验证码(4位)" sucmsg="输入正确"
														nullmsg="请输入验证码">
						<img src="verCode.${suffix }" 
						style="cursor: hand;"
						onclick="loadCode()" width="20%" class="right" height="32rem" id="currVerCode">
					</div>
					<div class="am-form-group tpl-login-remember-me">
						<input id="remember-me" type="checkbox"> <label
							for="remember-me"> 记住密码 </label>

					</div>
					<div class="am-form-group">
						<button type="submit" 
							class="am-btn am-btn-primary  am-btn-block tpl-btn-bg-color-success  tpl-login-btn">提交</button>
					</div>
				</form>
			</div>
		</div>
	</div>
	<script src="assets/js/amazeui.min.js"></script>
	<script src="assets/js/app.js"></script>
	<script src="http://libs.baidu.com/jquery/1.11.1/jquery.min.js"></script>
	<script src="../assets/js/validform.min.js"></script>
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
$("#loginForm").Validform({
		label : ".lable",
		showAllError : false,
		postonce : true,
		tiptype : function(msg, o, cssctl) {
			var objtip = $("#notice");
			cssctl(objtip, o.type);
			objtip.text(msg);
		},
		beforeSubmit : function(curform) {
			ajaxlogin();
			return false;
		}
	});
	function ajaxlogin() {
		$.ajax({
					target : 'div#notice',
					async : true,
					cache : false,
					type : "POST",
					dataType : 'json',
					data : $("#loginForm").serialize(),
					url : 'doLogin.${suffix}',
					timeout : 60000,
					success : function(json) {
						if(json.code!=0){
							alert(json.msg);
							loadCode();
							return;
						}
						location.href="index.${suffix}";
					},
					error : function() {
						loadCode();
					}
				});
	}
</script>
</html>
