<!-- touch Version by damon 2012.06.01 -->
<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:directive.include file="includes/top.jsp" />
<div class="loginbg">
			<div class="logintopwarp">
				<div class="loginlogo"><img src="../../images/touch/easoulogo.png" style=" width:136px; height:30px;"></div>
				<div class="slogan">修改资料</div>
			</div>
			<div class="information1">
			用户名:&nbsp;&nbsp;${name}
			</div>
			<form:form commandName="formUser" htmlEscape="true">
				<div class="registrationbox">
					<div class="registrationMsg">
						<label class="msgType">
							 昵称：
						</label>
						<form:input path="nickName" id="nickName" cssClass="msgContent" maxlength="20" htmlEscape="true" placeholder="2~16位(中文2位)"/>
					</div>
				</div>
				<div style="width:274px; height:10px; float:left; "></div>
				<div class="registrationbox">
					<div class="registrationMsg">
						<label class="msgType">
							城市：
						</label>
						<form:input path="city" id="city" cssClass="msgContent" maxlength="10" htmlEscape="true" placeholder="例如 深圳" />
					</div>
				</div>
				<div>
						<label class="msgType">
							 &nbsp; &nbsp; &nbsp; &nbsp; 性别：
						</label>
		    			<c:forEach var="ps" items="p">
						<form:select path="sex" name="sex" id="sex" cssClass="selContent" hidefocus="true">
						<option value="10" <c:if test="${formUser.sex==10}">selected</c:if>>-</option>
						<option value="0" <c:if test="${formUser.sex==0}">selected</c:if>>男</option>
						<option value="1" <c:if test="${formUser.sex==1}">selected</c:if>>女</option>
						</form:select>
						<br/>
						</c:forEach>
				</div>
				<div class="registrationBoxBottom">
					<input class="registrationButton" id="loginButton" value="确定"
						type="submit">
				</div>
			<div class="registrationnavbox">
				<form:errors path="*" id="msg" cssClass="errorbox" element="div" />
			</div>
			<input type="hidden" name="d" value="${d}" />
			</form:form>
			<div class="bottomrusbox">
				<div class="gobackbtn" onclick="window.location.href='${redirUrl}'">返回${backName}</div>
			</div>
<jsp:directive.include file="includes/bottom.jsp" />