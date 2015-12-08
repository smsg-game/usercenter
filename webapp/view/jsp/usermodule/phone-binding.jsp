<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:directive.include file="includes/top.jsp" />
<title>绑定手机号</title>
    <!-- Home -->
    <div data-role="page" id="phone-binding">
      <div data-theme="a" data-role="header" style="display:none">
        <h3>
          绑定手机号
        </h3>
      </div>
      <div data-role="content">
      <script type="text/javascript">
      $(function(){
  			if('${msg}') {
  				showPrompt.init('${msg}');
  			}
  		});
      function mbindMobile(url){
    	  var mobile = $("#mobile").val();
    	  if(mobile == null) {
    		  showPrompt.init("请输入手机号");
    		  return false;
    	  }
    	  var id = $("#userId").val();
    	  $.ajax({
    			type : 'POST',
    			dataType:"json",
    			data:{id:id,mobile:mobile},
    			url : url,
    			async:false,
    			success : function(data) {
    				if(data.code == 0) {
    					$("#mbindForm").submit();
    					return true;
    				} else if(data.code == 1000){
    					showPrompt.init(data.desc);
    					$("#mbindForm").submit();
    					return true;
    				}
    				else {
    					showPrompt.init(data.desc);
    					return false;
    				}
    			},
    			error : function(xhr, type) {
    				showPrompt.init("网络异常，绑定失败");
    			}
    		});
    	  return false;
      }
      </script>
        <form action="<c:url value="${write}${requestScope.contextPath}/user/mbindMobile" />" method="post" id ="mbindForm">
          <input type="text" name="mobile" id="mobile" placeholder="请输入您要绑定的手机号" />
          <input type="hidden" name="id" value="${id}" id="userId"/>
          <button type="submit" data-theme="b" class="ui-btn-hidden" aria-disabled="false" onclick="mbindMobile('<c:url value="${write}${requestScope.contextPath}/user/mbindMobileVerify" />');return false;">绑定手机号</button>
        </form>
      </div>
    </div>
