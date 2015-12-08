<!-- touch Version by damon 2012.06.01 -->
<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:directive.include file="includes/top.jsp" />
<body>
<header>
忘记密码
<a class="comeBack icon" onclick="javascript:history.go(-1)"></a>
</header>
<ul class="forgotPass-lists">
	<li style="width:40%" class="on" id="oneLi">1.输入手机号</li>
	<li style="width:60%" id="twoLi">2.输入验证码，重置密码</li>
</ul>
<form action="<c:url value="${requestScope.contextPath}/game/touch/resetSucc${qryString}" />" id="myform" method="post">
<div class="forgotPass-moblie">
	<div class="content-button"><input type="text" name="mobile" id="mobile" class="input" type="text" placeholder="输入您的手机号"  style="width:250px" maxlength="11" value="${mobile}"></div>	
	<div class="content-button"><a href="javascript:next();" class="button-blue">下一步</a></div>
</div>
<div  class="forgotPass-pass" style="display:none">
    <div class="content-button"><input type="password" name="newPwd" id="newPwd" class="input"  placeholder="输入新密码"  style="width:250px"></div>	
    <div class="content-button"><input type="password" name="confirm" id="confirm" class="input"  placeholder="再次输入新密码"  style="width:250px"></div>	
	<div class="content-button"><input name="veriCode" id="veriCode" class="input" type="text" placeholder="输入验证码"  style="width:250px"></div>	
	<div class="content-button"><a href="javascript:resetpass();" class="button-blue">提 交</a></div>
</div>
</form>
<script>
$(document).ready(function(){
	var li = $(".forgotPass-lists li");
	li.live("click",function(){
		var n = li.index($(this));
		if(n === 0){
			$(".forgotPass-moblie").show();
			$(".forgotPass-pass").hide();
		}else{
			$(".forgotPass-moblie").hide();
			$(".forgotPass-pass").show();
		}
		
		$(this).addClass("on").siblings().removeClass("on")
	})
})

function next(){
    var mobile = document.getElementById("mobile").value;
     $.ajax({
			type : 'POST',
			url : '${requestScope.contextPath}/game/touch/findbym${qryString}',
			data: {"mobile":mobile},
			success : function(data) {
			     $(".forgotPass-moblie").hide();
			     $(".forgotPass-pass").show();
			     $("#twoLi").addClass("on");
			     $("#oneLi").removeClass("on");
			     document.getElementById("popDiv").innerHTML=data;
	      		 setTimeout(function(){
	      				document.getElementById("tisi").style.display="none";
	      			},3000);
			},
			error : function(xhr, type) {
			     var msg = xhr.responseText;
			     document.getElementById("popDiv").innerHTML=msg;
	      		 document.getElementById("tisi").style.display="";
				 setTimeout(function(){
	      				document.getElementById("tisi").style.display="none";
	      				if(msg=="验证码已发送，请在手机上查看"){
	      				    $(".forgotPass-moblie").hide();
			                $(".forgotPass-pass").show();
			                $("#twoLi").addClass("on");
			                $("#oneLi").removeClass("on");
	      				}
	      			},3000);
			}
		})
}

function resetpass(){
    var mobile = document.getElementById("mobile").value;
    var newPwd = document.getElementById("newPwd").value;
    var confirm = document.getElementById("confirm").value;
    var veriCode = document.getElementById("veriCode").value;
     $.ajax({
			type : 'POST',
			url : '${requestScope.contextPath}/game/touch/resetpass${qryString}',
			data: {"mobile":mobile,"veriCode":veriCode,"newPwd":newPwd,"confirm":confirm},
			success : function(data) {
			     document.getElementById("popDiv").innerHTML=data;
	      		 document.getElementById("tisi").style.display="";
			     setTimeout(function(){
	      				document.getElementById("tisi").style.display="none";
	      				//window.location.href='${requestScope.contextPath}/game/touch/resetpass${qryString}';	      				
	      				document.getElementById("myform").submit();
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