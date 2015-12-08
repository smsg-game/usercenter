<!-- touch Version by terryyu 2012.12.13 -->
<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:directive.include file="includes/top.jsp" />
<div class="loginbg">
			<div class="logintopwarp">
				<div class="loginlogo"><img src="../../images/touch/easoulogo.png" style=" width:136px; height:30px;"></div>
				<div class="slogan">邮箱注册</div>
			</div>
			<form:form commandName="formEmail" htmlEscape="true">
				<div class="passwordbox">
					<div class="registrationMsg">
						<label class="msgType">
							邮箱：
						</label>
						<form:input path="email" id="email"  placeholder="请您输入您的邮箱地址"
						 cssClass="msgContent" tabindex="1" autocomplete="false" htmlEscape="true" maxlength="50"/>
					</div>
					<div class="registrationMsg">
						<label class="msgType">
							新密码：
						</label>
						<form:password path="password" id="password"  placeholder="6~14位数字或字母" 
						maxlength="14" class="msgContent" tabindex="2"  autocomplete="off" htmlEscape="true" />
					</div>
					<div class="registrationMsg">
						<label class="msgType">
							确认密码：
						</label>
						<form:password path="confirm" id="confirm"  placeholder="6~14位数字或字母" 
						maxlength="14" class="msgContent" tabindex="2"  autocomplete="off" htmlEscape="true" />
					</div>
				</div>
				<div class="registrationBoxBottom">
					<input class="registrationButton" id="loginButton" value="注册"
						type="submit">
				</div>
			<div class="registrationnavbox">
				<form:errors path="*" id="msg" cssClass="errorbox" element="div" />
			</div>
			</form:form>
			<div class="bottomrusbox">
				<div class="gobackbtn" onclick="window.history.go(-1);">
					返回
				</div>
			</div>
<jsp:directive.include file="includes/bottom.jsp" />
