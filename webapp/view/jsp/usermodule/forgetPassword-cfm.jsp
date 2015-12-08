<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:directive.include file="includes/top.jsp" />
<title>手机账号找回密码</title>
    <!-- Home -->
    <div data-role="page" id="pwd-recaller-executor">
      <div data-theme="a" data-role="header" style="display:none">
        <h3>
          手机账号找回密码
        </h3>
      </div>
      <div data-role="content">
      <script type="text/javascript">
      function mresetPwd(url){
    	  var newPwd = $("#newPwd").val();
    	  var mobile = $("#phone-number").val();
    	  var veriCode = $("#veriCode").val();
    	  $.ajax({
    			type : 'POST',
    			dataType:"json",
    			data:{newPwd:newPwd,mobile:mobile,veriCode:veriCode},
    			url : url,
    			success : function(data) {
    				if(data.code == 0) {
    					location.href = "${write}/user/usersetting.html${qryString}&m=3";
    				}else {
    					showPrompt.init(data.desc);
    				}
    			},
    			error : function(xhr, type) {
    				showPrompt.init("服务器错误");
    			}
    		});
      }
      </script>
        <form action="<c:url value="${requestScope.contextPath}/user/mresetpass" />" method="post">
          <fieldset class="ui-grid-a">
            <div class="ui-block-a">
              <input type="password" name="newPwd" id="newPwd" placeholder="输入新密码"/>
            </div>
            <div class="ui-block-b">
               <button data-theme="a" href="javascript:void(0)" onclick="displayPwd('newPwd',this);return false;">显示密码</button>
            </div>
            <input type="hidden" id="phone-number" name="mobile" value="${mobile}"/>
          </fieldset>
          <input type="text" name="veriCode" id="veriCode" placeholder="请输入验证码" value="${veriCode}"/>   
          <button type="submit" data-theme="b" class="ui-btn-hidden" aria-disabled="false" onclick="mresetPwd('<c:url value="${requestScope.contextPath}/user/mresetpass" />');return false;">修改密码</button>
        </form>
      </div>
    </div>

