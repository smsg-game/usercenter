<!-- touch Version by damon 2012.06.01 -->
<%@ page contentType="text/html; charset=UTF-8"%>
<jsp:directive.include file="includes/top.jsp" />
<div class="loginbg">
	<div class="logintopwarp">
				<div class="loginlogo"><img src="../../images/touch/easoulogo.png" style=" width:136px; height:30px;"></div>
				<div class="slogan">绑定手机</div>
			</div>
	<form:form commandName="formUser" htmlEscape="true">
  
		<div class="registrationbox">
			<div class="registrationMsg">
				<label class="msgType">
					手机号：
				</label>
				<form:input path="mobile" id="mobile" maxlength="11" cssClass="msgContent" placeholder="请输入要绑定的手机号" autocomplete="off" htmlEscape="true"/>
				<input type="hidden" name="id" value="${id}" />
			</div>
		</div>
		<div class="registrationBoxBottom">
			<input class="registrationButton" id="loginButton" value="绑定"
				type="submit">
		</div>
	<div class="registrationnavbox">
		<form:errors path="*" id="msg" cssClass="errorbox" element="div" />
	</div>
	</form:form>
	<div class="information1">
		系统将以短信方式发送确定信息到新绑定手机上,经确认后修改成功。
	</div>
	<div class="bottomrusbox">
		<div class="gobackbtn" onclick="window.location.href='${redirUrl}'">返回${backName}</div>
</div>
<jsp:directive.include file="includes/bottom.jsp" />