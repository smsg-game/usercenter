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
					<p>第三步</p>
  					<p>重新设置您的密码<p>
			</div>	
			<form:form commandName="formEmail" htmlEscape="true">
				<div class="loginbox">
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
	           	<br/>
	           	<div class="registrationBoxBottom">
	           	    <input type="hidden" name="secert" value="${secert}" />
					<input class="registrationButton" id="loginButton" value="重设密码"
						type="submit">
				</div>	
				<div class="registrationnavbox">
						<jsp:directive.include file="includes/eucerr.jsp" />
	           		</div>
		    </form:form>		 
			
			<div class="bottomrusbox">
				<div class="gobackbtn" onclick="window.history.go(-1);">
					返回
				</div>
			</div>			
			
<jsp:directive.include file="includes/bottom.jsp" />