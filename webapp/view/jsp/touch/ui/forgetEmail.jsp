<!-- touch Version by Terryyu 2012.12.13 -->
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
					一、请填写绑定邮箱
			</div>
			 <form:form commandName="formEmail" htmlEscape="true">
				<div class="registrationbox">
					<div class="registrationMsg">
						<label class="msgType">
							邮箱：
						</label>
						<form:input path="email" id="email"  placeholder="请您输入您的邮箱地址"
						 cssClass="msgContent" tabindex="1" autocomplete="false" htmlEscape="true" maxlength="50"/>
					</div>
				</div>
				<div class="registrationBoxBottom">
					
					<input class="registrationButton" id="loginButton" value="下一步"
						type="submit">
				</div>	
			</form:form>
			<div class="registrationnavbox">
				<jsp:directive.include file="includes/eucerr.jsp" />
            </div>
			<div class="bottomrusbox">
				<div class="gobackbtn" onclick="window.history.go(-1);">
					返回
				</div>
			</div>			
			
<jsp:directive.include file="includes/bottom.jsp" />