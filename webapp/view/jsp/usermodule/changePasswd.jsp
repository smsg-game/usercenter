<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:directive.include file="includes/top.jsp" />
<title>修改密码</title>
    <!-- Home -->
    <div data-role="page" id="page-repwd">
      <div data-theme="a" data-role="header" style="display:none">
        <h3>
          修改密码
        </h3>
      </div>
      <div data-role="content">
        <form action="" method="post">
          <fieldset class="ui-grid-a">
            <div class="ui-block-a">
              <input type="password" name="newpwd" id="newpwd" placeholder="输入新密码"/>
            </div>
            <div class="ui-block-b">
              <button id="display-pwd" data-theme="b" class="ui-btn-hidden" onclick="displayPwd('newpwd', this);return false">显示密码</button>
            </div>
          </fieldset>
          <button type="submit" data-theme="b" class="ui-btn-hidden" aria-disabled="false" onclick="changePwd('<c:url value="${write}${requestScope.contextPath}/user/mchangepass${qryString}" />');return false;">修改密码</button>
        </form>
      </div>
    </div>

