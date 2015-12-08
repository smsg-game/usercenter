<!-- touch Version by damon 2012.06.01 -->
<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:directive.include file="includes/top.jsp" />
<div class="loginbg">
			<div class="logintopwarp">
				<div class="loginlogo"><img src="../../images/touch/easoulogo.png" style=" width:136px; height:30px;"></div>
				<div class="slogan">忘记密码</div>
			</div>
			<form action="<c:url value="${requestScope.contextPath}/pass/newpwd${qryString}" />" name="passwdForm" method="post">			
			<div class="loginbox">
					<div class="loginMsg">
						<label class="msgType">
							新密码：
						</label>
						<input  type="password" placeholder="请输入您的新密码"
						   name="newPwd" id="newPwd"  class="msgContent" maxlength="18"/>
					</div>
					<div class="loginMsgh2">
						<label class="msgType">
							确认密码：
						</label>
						<input  type="password" placeholder="请再次输入您的密码"
						   type="password" name="confirm" id="confirm"  class="msgContent" maxlength="18"/>
					</div>
				</div>
                <!-- <input type="hidden" name="username" value="${username}" />  -->
                <input type="hidden" name="answer" value="${answer}" />
				<div class="registrationBoxBottom">
					<input class="registrationButton" id="loginButton" value="确认并登录"
						type="submit">
				</div>
			</form>
			<div class="registrationnavbox">			
                <jsp:directive.include file="includes/eucerr.jsp" />
			</div>
			<div class="bottomrusbox">
				<div class="gobackbtn" onclick=window.history.go(-1);>
					返回
				</div>
			</div>
</div>
<jsp:directive.include file="includes/bottom.jsp" />
