<!-- touch Version by damon 2012.06.01 -->
<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:directive.include file="includes/top.jsp" />
<div class="loginbg">
			<div class="logintopwarp">
				<div class="loginlogo"><img src="../../images/touch/easoulogo.png" style=" width:136px; height:30px;"></div>
				<div class="slogan">重置密码</div>
			</div>
			<form action="<c:url value="${requestScope.contextPath}/pass/resetpass${qryString}" />" id="myform" method="post">
				<div class="passwordbox">
					<div class="registrationMsg">
						<label class="msgType">
							新密码：
						</label>
						<input type="password" name="newPwd" id="newPwd" placeholder="请输入您的新密码" class="msgContent" maxlength="18" type="password"/>
					</div>
					<div class="registrationMsg">
						<label class="msgType">
							确认密码：
						</label>
						<input type="password" name="confirm" class="msgContent" id="confirm" placeholder="请输入确认密码" class="msgContent" maxlength="18"/>
					</div>
					<div class="registrationMsg">
						<label class="msgType">
							验证码：
						</label>
						<input type="text" name="veriCode" id="veriCode" placeholder="验证码" class="msgContent" maxlength="6" value="${veriCode}"/>
					</div>
				</div>
				<input type="hidden" name="mobile" value="${mobile}"/>
				<div class="registrationBoxBottom">
					<input class="registrationButton" id="loginButton" value="确认并登录"
						type="submit">
				</div>
			</form>
			<div class="registrationnavbox">			
                <jsp:directive.include file="includes/eucerr.jsp" />
			</div>
			<div class="bottomrusbox">
				<div class="gobackbtn" onclick=window.history.go(-1);>返回</div>
			</div>
<jsp:directive.include file="includes/bottom.jsp" />