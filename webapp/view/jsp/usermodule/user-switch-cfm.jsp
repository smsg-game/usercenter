<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:directive.include file="includes/top.jsp" />
    <!-- Home -->
    <div data-role="page" id="user-switch-executor">
      <div data-theme="a" data-role="header" style="display:none">
        <h3>
          更换账号
        </h3>
      </div>
      <div data-role="content">
<script type="text/javascript">
$(document).ready(function(){
	$("#login").click(function() {
		var username = $("#username").val();
		var password = $("#password").val();
		if(!username){
			showPrompt.init("用户名不能为空");
			return;
		}
		if(!password){
			showPrompt.init("密码不能为空");
			return;
		}
		$.ajax({
			type : 'POST',
			dataType:"json",
			data:{username:username,password:password},
			url : '${write}${requestScope.contextPath}/mlogin${qryString}',
			success : function(data) {
				if(data == 0) {
					window.location = "${write}/user/usersetting.html${qryString}";
				}else {
					showPrompt.init(data.m);
				}
			},
			error : function(xhr, type) {
			}
		});
	    return false;
	});

});
</script>
        <form action="" method="post">
          <input type="text" name="username" id="username" placeholder="用户名/手机号码"/ tabindex="1">
          <fieldset class="ui-grid-a">
            <div class="ui-block-a">
              <input type="password" name="password" id="password" placeholder="输入新密码"/ tabindex="2">
            </div>
            <div class="ui-block-b">
               <a data-role="button" data-theme="a" href="javascript:void(0)" onclick="displayPwd('password');return false">显示密码</a>
            </div>
          </fieldset>    
          <button type="button" data-theme="b" id="login" class="ui-btn-hidden" aria-disabled="false">登录</button>
        </form>
      </div>
    </div>

