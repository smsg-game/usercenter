<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:directive.include file="includes/top.jsp" />
<c:if test="${not empty pageContext.request.queryString}">
    <c:set var="qryString" value="?${pageContext.request.queryString}" />
</c:if>
<c:if test="${not empty qryString}">
   <c:set var="conSymb" value="&" />
</c:if>
<c:if test="${empty qryString}">
   <c:set var="conSymb" value="?" />
</c:if>
<body>
<header>
账号绑定
<a class="comeBack icon" href="${requestScope.contextPath}/game/touch/userCenter${qryString}"></a>
</header>
<div>
	<ul class="info-account">
		<li  class="moblie">
				<span class="info-account-text">
					<span class="info-account-logo infoIcon fl"></span>
					<ol  class="fl">
						<dt>
						  绑定手机号
						</dt>
						<ds>
						<c:choose>
						 <c:when test="${not empty mobile}">
 	                          ${mobile}
                         </c:when>
                         <c:otherwise>
	                                                                                          遗忘密码时，通过绑定手机号找回<!--, 首次绑定成功后，赠送100e币。 -->
                         </c:otherwise>
						</c:choose>
						</ds>
					</ol>
				</span>
					<c:choose>
						 <c:when test="${not empty mobile}">
 	                         <a class="info-account-button button-disable">已绑定</a>
                         </c:when>
                         <c:otherwise>
	                         <a href="${write}${requestScope.contextPath}/game/touch/bindMobile${qryString}" class="info-account-button button-blue">绑定</a>
                         </c:otherwise>
					</c:choose>
			</li>
			
		<li  class="sina">
				<span class="info-account-text">
					<span class="info-account-logo infoIcon fl"></span>
					<ol  class="fl">
					    <c:choose>
                            <c:when test="${not empty sina}">
   	                            <dt>${sina.nick_name}</dt>
                            </c:when>
                            <c:otherwise>
	                            <dt>新浪微博</dt>
                            </c:otherwise>
                        </c:choose>
						<ds>方便分享你的游戏动态到微博</ds>
					</ol>
				</span>
				<c:choose>
                     <c:when test="${not empty sina}">
   	                     <a href="${requestScope.contextPath}/game/touch/cancelBind${qryString}${conSymb}type=${sina.net_id}&page=center" class="info-account-button button-blue">解除绑定</a>
                     </c:when>
                     <c:otherwise>
	                      <a href="${requestScope.contextPath}/oRedirect${qryString}${conSymb}t=1&page=bind&uv=gt" class="info-account-button button-blue">绑定</a>
                     </c:otherwise>
                 </c:choose>
			</li>
			
		<li  class="tencent">
		
		<span class="info-account-text">
			<span class="info-account-logo infoIcon fl"></span>
				<ol  class="fl">
					<c:choose>
                         <c:when test="${not empty tqq}">
   	                         <dt>${tqq.nick_name}</dt>
                         </c:when>
                         <c:otherwise>
	                         <dt>腾讯微博</dt>
                         </c:otherwise>
                        </c:choose>
						<ds>方便分享你的游戏动态到微博</ds>
					</ol>
				</span>
				<c:choose>
                     <c:when test="${not empty tqq}">
   	                     <a href="${requestScope.contextPath}/game/touch/cancelBind${qryString}${conSymb}type=${tqq.net_id}&page=center" class="info-account-button button-blue">解除绑定</a>
                     </c:when>
                     <c:otherwise>
	                      <a href="${requestScope.contextPath}/oRedirect${qryString}${conSymb}t=2&page=bind&uv=gt" class="info-account-button button-blue">绑定</a>
                     </c:otherwise>
                 </c:choose>
			</li>
	</ul>
</div>	
<script>
$(document).ready(function(){

})
</script>
<jsp:directive.include file="includes/bottom.jsp" />
