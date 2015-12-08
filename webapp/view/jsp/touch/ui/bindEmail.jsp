<!-- touch Version by terryyu 2012.12.13 -->
<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:directive.include file="includes/top.jsp" />
<div class="loginbg">
			<div class="logintopwarp">
				<div class="loginlogo"><img src="../../images/touch/easoulogo.png" style=" width:136px; height:30px;"></div>
				<div class="slogan">梵町个人中心</div>
			</div>
			
			<form:form commandName="formEmail" htmlEscape="true">
				<div class="loginbox">
				<div class="registrationMsg">
						<label class="msgType">
							新邮箱：
						</label>
						<form:input path="email" id="email"  placeholder="输入您的新邮箱地址"
						 cssClass="msgContent" tabindex="1" autocomplete="false" htmlEscape="true" maxlength="50"/>
					</div>		
					<div class="registrationMsg">
						<label class="msgType">
							登录密码：
						</label>
						<form:input cssClass="msgContent" placeholder="输入密码" type="password"
							id="password" tabindex="2" path="password"  htmlEscape="true" autocomplete="off" />
					</div>										
				</div>
				
				<div class="registrationBoxBottom">
				    <input type="hidden" name="id" value="${id}" /-->
					<input class="registrationButton" id="loginButton" value="修改邮箱"
						type="submit">
				</div>
				
			<div class="registrationnavbox">
				<form:errors path="*" id="msg" cssClass="errorbox" element="div" />
			</div>
			</form:form>
			
			<div class="information1">
					提示：如已设置,请返回
			</div>
			
			<div class="bottomrusbox">
					<div class="gobackbtn" onclick="window.location.href='${redirUrl}'">返回${backName}</div>
			</div>
			<div class="bottomrusbox">
				<div class="logoutbtn" onclick="window.location.href='${read}/logout${qryString}'">退出登录</div>
			</div>			
						
<jsp:directive.include file="includes/bottom.jsp" />

