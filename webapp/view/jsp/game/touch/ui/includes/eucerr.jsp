<c:if test="${not empty msg}">
<div id="msg" class="errorbox">
<c:forEach items="${msg}" var="item" >
${item}<br/>
</c:forEach>
</div>
</c:if>