<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:directive.include file="includes/top.jsp" />
<title>绑定手机号</title>
    <!-- Home -->
    <div data-role="page" id="phone-binding-executor">
    	<script type="text/javascript">
    	$(function(){
    		if('${msg}') {
    			showPrompt.init('${msg}');
    		}
    	});
    </script>
      <div data-theme="a" data-role="header" style="display:none">
        <h3>
          绑定手机号
        </h3>
      </div>
      <div data-role="content">
        <form action="<c:url value="${write}${requestScope.contextPath}/user/mbindcfm${qryString}" />" method="post">
          <fieldset class="ui-grid-a">
            <div class="ui-block-a">
              <input type="text" name="mobile" id="mobile" value="${mobile}"/>
            </div>
            <div class="ui-block-b">
               <a data-role="button" data-theme="a" href="javascript:void(0)" onclick="sendSms('<c:url value="${write}${requestScope.contextPath}/user/msendSms${qryString}" />','${id}','${mobile}')">重新下发</a>
            </div>
          </fieldset>
          <input type="text" name="veriCode" id="veriCode" placeholder="请输入验证码"/>   
          <input type="hidden" name="id" value="${id}" />
          <input type="hidden" name="act" value="verify" />
          <button type="submit" data-theme="b" class="ui-btn-hidden" aria-disabled="false">完成绑定</button>
        </form>
      </div>
    </div>

