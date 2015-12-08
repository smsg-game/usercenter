<!-- touch Version by damon 2012.06.01 -->
<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:directive.include file="includes/top.jsp" />

<body>
		<div class="loginbg">
			<div class="logintopwarp">
				<div class="loginlogo"><img src="../../images/touch/easoulogo.png" style=" width:136px; height:30px;"></div>
				<div class="slogan">绑定手机验证</div>
			</div>
			<div class="information1">
					请输入您在手机收到的验证码
			</div>
			<form:form commandName="formVerify" htmlEscape="true">
				<div class="registrationbox">
					<div class="registrationMsg">
						<label class="msgType">
							 验证码：
						</label>
					    <form:input path="veriCode" id="word" cssClass="msgContent" placeholder="验证码" maxlength="6" autocomplete="off" htmlEscape="true"/>                     
					</div>
				</div>
    			<input type="hidden" name="id" value="${id}" />
    			<input type="hidden" name="mobile" id="mobile" value="${mobile}" />
    			<input type="hidden" name="act" value="verify" />
				<div class="registrationBoxBottom">			
					<input class="registrationButton" id="loginButton" value="确定"
						type="submit">
				</div>
			<div class="registrationnavbox">
				<form:errors path="*" id="msg" cssClass="errorbox" element="div" />
			</div>
			</form:form>
	<div class="bottomrusbox">
		<div class="gobackbtn" onclick="window.location.href='${redirUrl}'">返回${backName}</div>
	</div>
<jsp:directive.include file="includes/bottom.jsp" />