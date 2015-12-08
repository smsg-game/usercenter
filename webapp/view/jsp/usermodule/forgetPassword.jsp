<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:directive.include file="includes/top.jsp" />
<title>手机账号找回密码</title>
    <!-- Home -->
    <div data-role="page" id="pwd-recaller">
      <div data-theme="a" data-role="header" style="display:none">
        <h3>
          手机账号找回密码
        </h3>
      </div>
      <div data-role="content">
      <script type="text/javascript">
      function getPwd(url){
    	  var mobile = $("#phone-number").val();
    	  $.ajax({
    			type : 'POST',
    			dataType:"json",
    			data:{mobile:mobile},
    			url : url,
    			async:false,
    			success : function(data) {
    				if(data.code == 0) {
    					//location.href = "${write}/user/mresetpass?mobile="+mobile;
    					$("#mresetForm").submit();
    					return true;
    				}else if(data.code == 1004){
    					showPrompt.init(data.desc);
    					setTimeout(function(){
    						//location.href = "${write}/user/mresetpass?mobile="+mobile;
    						$("#mresetForm").submit();
    						return true;
    					},2000);
    				}else {
    					showPrompt.init(data.desc);
    					return false;
    				}
    			},
    			error : function(xhr, type) {
    				showPrompt.init("服务器错误");
    			}
    		});
    	  return false;
      }
      </script>
        <form action="<c:url value="${requestScope.contextPath}/user/mresetpass" />" method="get" id="mresetForm">
          <input type="tel" name="mobile" id="phone-number" placeholder="输入您需要找回密码的手机号"/>
          <button type="submit" data-theme="b" class="ui-btn-hidden" aria-disabled="false" onclick="getPwd('<c:url value="${requestScope.contextPath}/user/mfindbym" />');return false;">找回密码</button>
        </form>
      </div>
    </div>
