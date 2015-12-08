<!-- touch Version by damon 2012.06.01 -->
<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:directive.include file="includes/top.jsp" />
<!-- 
<jsp:directive.include file="includes/eucbar.jsp" />
 -->
<div class="loginbg">
			<div class="logintopwarp">
				<div class="loginlogo"><img src="../../images/touch/easoulogo.png" style=" width:136px; height:30px;"></div>
				<div class="slogan">忘记密码</div>
			</div>
			<div class="information1">
					一、请输入您注册或绑定的手机号，您将会收到一个验证码
			</div>
			 <form action="<c:url value="${requestScope.contextPath}/pass/findbym${qryString}" />" id="FindMForm" method="post">
				<div class="registrationbox">
					<div class="registrationMsg">
						<label class="msgType">
							手机号：
						</label>
						<input type="text" name="mobile" id="mobile" class="msgContent" placeholder="请输入您的手机号" maxlength="11" value="${mobile}"/>
					</div>
				</div>
				<div class="registrationBoxBottom">
					<label class="nextAutoLogin">
						*获得验证码后您就可以重置您的密码
					</label>
					<input class="registrationButton" id="loginButton" value="获取验证码"
						type="submit">
				</div>	
			</form>
			<div class="information1">
			<!--p>或</p>
    		<p>二、<a href='${requestScope.contextPath}/pass/findbyem${qryString}" />'>通过邮箱找回</a></p-->
            </div>

			<div class="registrationnavbox">
				<jsp:directive.include file="includes/eucerr.jsp" />
            </div>
			<div class="bottomrusbox">
				<div class="gobackbtn" onclick="window.history.go(-1);">
					返回
				</div>
			</div>
<jsp:directive.include file="includes/bottom.jsp" />