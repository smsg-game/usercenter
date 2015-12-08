<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:directive.include file="includes/top.jsp" />
    <!-- Home -->
    <div data-role="page" id="page-face">
    <script type="text/javascript">
    	$(function(){
    		if('${m}') {
    			if('${m}' == '1') {
    				showPrompt.init("手机绑定成功");
    			}else if('${m}' == '2'){
    				showPrompt.init("密码修改成功");
    			}else if('${m}' == '3'){
    				showPrompt.init("找回密码成功，请记住您刚设置的密码");
    			}
    			else {
    				showPrompt.init('${m}');
    			}
    		}
    	});
    </script>
      <div data-theme="a" data-role="header" style="display:none">
        <h3>
          账号管理
        </h3>
        <a href="index.html" data-icon="delete" data-theme="b" class="ui-btn-right" data-iconpos="notext">Cancel</a>
      </div>
      <div data-role="content">
				<ul data-role="listview" data-divider-theme="b" data-inset="true" id="nickNameDiv">
				  <li data-theme="c" data-icon="false">
            <a href="#page-face" data-transition="slide" onclick="setNickName()" id="nickNameA">
							<c:choose>
								<c:when test="${not empty nickName}">
									<span id="nickname-label">昵称</span>${nickName}
								</c:when>
								<c:otherwise>
									设置昵称
								</c:otherwise>
							</c:choose>
            </a>
					</li>
				</ul>

		    <ul data-role="listview" data-divider-theme="b" data-inset="true" id="nickNameText" style="display:none">
          <li data-theme="c" data-icon="false" style="padding:0 14px">
            <div>
	            <span id="nickname-label">昵称</span>
	            <input type="text" name="nickName" value="${nickName}" id="nickNameValue" style="border:none;background:none" data-role="none"/>
            </div>
          </li>
        </ul>
        
        <ul data-role="listview" data-divider-theme="b" data-inset="true">
          <li data-theme="c" data-icon="false">
            
              	<c:choose>
              	<c:when test="${not empty mobile}">
              	<a href="#" data-transition="slide">
              绑定手机号
              <span class="ui-li-count">
              		<span class="font-status" id="mobileBinded" onclick="binded('已绑定手机号')">已绑定</span>
              </span>
              </a>
                </c:when>
                <c:otherwise>
                <a href="<c:url value="${write}${requestScope.contextPath}/user/mbindMobile${qryString}" />" data-transition="slide">
              绑定手机号
              <span class="ui-li-count">
                	<span class="font-striking">推荐</span>
                	</span>
                	</a>
                </c:otherwise>
                </c:choose>
          </li>
          <li data-theme="c">
            <a href="<c:url value="${write}${requestScope.contextPath}/user/mchangepass${qryString}" />" data-transition="slide">
              修改密码
            </a>
          </li>
        </ul>
        <ul data-role="listview" data-divider-theme="b" data-inset="true">
          <li data-theme="c">
          <c:choose>
   			<c:when test="${not empty sina}">
   				<a href="#" tadata-transition="slide">
              绑定新浪微博		<span class="ui-li-count">
            		<span class="font-status" onclick="binded('已绑定新浪微博')">已绑定</span>
            	</span>
            </a>
            </c:when>
            <c:otherwise>
            	<a href="${requestScope.contextPath}/oRedirect?t=1&page=bind&uv=module" target="_self" tadata-transition="slide">
              绑定新浪微博
            </a>
            </c:otherwise>
            </c:choose>
            </span>
          </li>
          <li data-theme="c">
          	<c:choose>
   			<c:when test="${not empty tqq}">
   				<a href="#" tadata-transition="slide">
              绑定腾讯微博		<span class="ui-li-count">
            		<span class="font-status" onclick="binded('已绑定腾讯微博')">已绑定</span>
            	</span>
            </a>
            </c:when>
            <c:otherwise>
            	<a href="${requestScope.contextPath}/oRedirect?t=2&page=bind&uv=module" target="_self" data-transition="slide">
              绑定腾讯微博
            </a>
            </c:otherwise>
            </c:choose>
          </li>
          <li data-theme="c">
          <c:choose>
   			<c:when test="${not empty qq}">
   				<a href="#" tadata-transition="slide">
              绑定QQ号码		<span class="ui-li-count">
            		<span class="font-status" onclick="binded('已绑定QQ号')">已绑定</span>
            	</span>
            </a>
            </c:when>
            <c:otherwise>
            	<a href="${requestScope.contextPath}/oRedirect?t=5&page=bind&uv=module" target="_self" data-transition="slide">
              绑定QQ号码
            </a>
            </c:otherwise>
            </c:choose>
          </li>
          <li data-theme="c">
          	<c:choose>
   			<c:when test="${not empty qq}">
   				<a href="#" tadata-transition="slide">
              绑定人人网		<span class="ui-li-count">
            		<span class="font-status" onclick="binded('已绑定人人网')">已绑定</span>
            	</span>
            </a>
            </c:when>
            <c:otherwise>
            	<a href="${requestScope.contextPath}/oRedirect?t=3&page=bind&uv=module" target="_self" data-transition="slide">
              绑定人人网
            </a>
            </c:otherwise>
            </c:choose>
          </li>
        </ul>
        <ul data-role="listview" data-divider-theme="b" data-inset="true">
          <li data-theme="c">
            <a href="<c:url value="${write}${requestScope.contextPath}/user/mforget${qryString}" />" data-transition="slide">
              手机账号找回密码
            </a>
          </li>
        </ul>
        <a data-role="button" data-theme="a" href="<c:url value="${write}${requestScope.contextPath}/user/switch${qryString}" />">
          更换账号
        </a>
      </div>
    </div>

