<!-- touch Version by damon 2012.06.01 -->
<%@ page contentType="text/html; charset=UTF-8"%>
<jsp:directive.include file="includes/top.jsp" />

<body>

<header>
解除绑定
<a class="comeBack icon" href="${requestScope.contextPath}/game/touch/bindAccount${qryString}"></a>
</header>
<ul class="forgotPass-lists">
	<li style="width:50%" class="on" id="oneLi">1.输入手机号、密码</li>
	<li style="width:50%" id="twoLi">2.解除成功</li>
</ul>
<input type="hidden" name="id" id="id" value="${id}" />
<div class="forgotPass-moblie">
	<div class="content-button"><input type="text" name="mobile" id="mobile" class="input" type="text" placeholder="输入您的手机号"  style="width:250px" maxlength="11" value="${mobile}"></div>	
	<div class="content-button"><input type="password" name="password" id="password" class="input" type="text" placeholder="输入密码"  style="width:250px" maxlength="11" value="${mobile}"></div>
	<div class="content-button"><a href="javascript:submitJbMobile();" class="button-blue">解除绑定</a></div>
</div>
<div  class="forgotPass-pass" style="display:none">	
	<div class="content-button"> 解除绑定成功，系统将在3秒后自动跳转到游戏个人中心。</div>
</div>
<script>
function submitJbMobile(){
    var mobile = document.getElementById("mobile").value;
    var password = document.getElementById("password").value;
    var id = document.getElementById("id").value;
     $.ajax({
			type : 'POST',
			url : '${requestScope.contextPath}/game/touch/jbMobile${qryString}',
			data: {"id":id,"mobile":mobile,"password":password},
			success : function(data) {
			     $(".forgotPass-moblie").hide();
			     $(".forgotPass-pass").show();
			     $("#twoLi").addClass("on");
			     $("#oneLi").removeClass("on");
			     setTimeout(function(){
	      				//document.getElementById("tisi").style.display="none";
	      				window.location.href='${requestScope.contextPath}/game/touch/userCenter${qryString}';	      				
	      				//document.getElementById("myform").submit();
	      			},3000);
			},
			error : function(xhr, type) {
			     document.getElementById("popDiv").innerHTML=xhr.responseText;
	      		 document.getElementById("tisi").style.display="";
				 setTimeout(function(){
	      				document.getElementById("tisi").style.display="none";
	      			},3000);
			}
		})
}

</script>
<jsp:directive.include file="includes/bottom.jsp" />