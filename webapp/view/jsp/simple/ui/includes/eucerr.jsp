<c:if test="${not empty msg}">
<c:forEach items="${msg}" var="item" >
${item}<br/>
</c:forEach>
</c:if>