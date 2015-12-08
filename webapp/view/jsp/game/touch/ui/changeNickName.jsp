<!-- touch Version by damon 2012.06.01 -->
<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:directive.include file="includes/top.jsp" />
<body>
<header>
修改昵称
<a class="comeBack icon" href="${requestScope.contextPath}/game/touch/userCenter${qryString}"></a>
</header>
<div class="content-button">
	<input path="nickName" id="nickName" value="${nickName}" class="input" type="text" placeholder="昵称由2-8位中文、数字或字母组成。"  style="width:250px" />
</div>	
<div class="content-button">
	<input type="button" class="button-blue" value="提 交" onclick="javascript:changeNName();">
</div>
<input type="hidden" name="d" id="d" value="${d}" />
<script>
function changeNName(){
    var d = document.getElementById("d").value;
    var nickName = document.getElementById("nickName").value;
     $.ajax({
			type : 'POST',
			url : '${requestScope.contextPath}/game/touch/changeNName${qryString}',
			data: {"d":d,"nickName":nickName},
			success : function(data) {
			     document.getElementById("popDiv").innerHTML=data;
	      		 document.getElementById("tisi").style.display="";
			     setTimeout(function(){
	      				document.getElementById("tisi").style.display="none";
	      				window.location.href='${requestScope.contextPath}/game/touch/userCenter${qryString}';
	      			},1500);
			},
			error : function(xhr, type) {
			     document.getElementById("popDiv").innerHTML=xhr.responseText;
	      		 document.getElementById("tisi").style.display="";
				 setTimeout(function(){
	      				document.getElementById("tisi").style.display="none";
	      			},1500);
			}
		})
}
</script>
</body>
</html>