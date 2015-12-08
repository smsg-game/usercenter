<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:directive.include file="includes/top.jsp" />
    <!-- Home -->
    <div data-role="page" id="user-switch">
    <script type="text/javascript">
function logout(url) {
	$.ajax({
		type : 'GET',
		url : url,
		success : function(data) {
			window.location = "${write}/user/usersetting.html${qryString}";
		},
		error : function(xhr, type) {
		}
	});
}
</script>
      <div data-theme="a" data-role="header" style="display:none">
        <h3>
          更换账号
        </h3>
      </div>
      <div data-role="content">
        <p>您保存在手机上的用户登录信息将被<strong class="font-striking">清空</strong>！</p>
        <p id="last-para">如果您没有绑定手机号或者第三方账号，您的账号信息将无法找回！</p>
        <a data-role="button" data-theme="a" href="<c:url value="${write}${requestScope.contextPath}/user/logout${qryString}" />" >更换账号</a>
        <a data-role="button" data-theme="a" href="/user/usersetting.html">取消</a>
      </div>
    </div>

