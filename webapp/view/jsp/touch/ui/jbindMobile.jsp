<!-- touch Version by damon 2012.06.01 -->
<%@ page contentType="text/html; charset=UTF-8"%>
<jsp:directive.include file="includes/top.jsp" />
<div class="loginbg">
	<div class="logintopwarp">
				<div class="loginlogo"><img src="../../images/touch/easoulogo.png" style=" width:136px; height:30px;"></div>
				<div class="slogan">解除绑定</div>
			</div>
	<form:form commandName="formUser" htmlEscape="true">
		<div class="loginbox">
			<div class="registrationMsg">
				<label class="msgType">
					原手机号：
				</label>
				<form:input path="mobile" id="mobile" maxlength="11" cssClass="msgContent" placeholder="输入原手机号" autocomplete="off" htmlEscape="true"/>
			</div>
			<div class="registrationMsg">
				<label class="msgType">
					密码：
				</label>
				<form:password path="password" id="password"  placeholder="请输入登录密码" 
						 class="msgContent" maxlength="20" htmlEscape="true"  type="password"/>
			</div>
		</div>
		<input type="hidden" name="id" value="${id}" />
		<div class="registrationBoxBottom">
			<input class="registrationButton" id="loginButton" value="解除绑定"
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