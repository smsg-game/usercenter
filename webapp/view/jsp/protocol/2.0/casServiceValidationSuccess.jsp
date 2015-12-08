<%@ page session="false"%><%@ taglib prefix="c"
	uri="http://java.sun.com/jsp/jstl/core"%><%@ taglib
	uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="org.jasig.cas.validation.Assertion"%>
<%@ page import="org.jasig.cas.authentication.Authentication"%>
<%@ page import="org.jasig.cas.authentication.principal.Principal"%>
<%@ page import="com.easou.cas.authenticateion.ThirdPrincipal"%>
<%
	Assertion ass = ((Assertion) request.getAttribute("assertion"));
	Authentication au = ass.getChainedAuthentications().get(
			ass.getChainedAuthentications().size() - 1);
	Principal pri = au.getPrincipal();
%>
<cas:serviceResponse xmlns:cas='http://www.yale.edu/tp/cas'>
	<cas:authenticationSuccess>
		<cas:user>${fn:escapeXml(assertion.chainedAuthentications[fn:length(assertion.chainedAuthentications)-1].principal.id)}</cas:user>
		<cas:attributes>
		<%
			if (pri instanceof ThirdPrincipal) {
						ThirdPrincipal spri = (ThirdPrincipal) pri;
		%>
		<cas:oid><%=spri.getAttributes().get("oid")%></cas:oid>
		<cas:accCode><%=spri.getAttributes().get("accCode")%></cas:accCode>
		<%
			}
		%>
		<%
			Integer maxAge=(Integer)pri.getAttributes().get("maxAge");
			if (maxAge!=null){
		 %>
		 <cas:acookieAge><%=maxAge%></cas:acookieAge>
		 <%} %>
		</cas:attributes>
		<c:if test="${not empty pgtIou}">
			<cas:proxyGrantingTicket>${pgtIou}</cas:proxyGrantingTicket>
		</c:if>
		<c:if test="${fn:length(assertion.chainedAuthentications) > 1}">
			<cas:proxies>
				<c:forEach var="proxy" items="${assertion.chainedAuthentications}"
					varStatus="loopStatus" begin="0"
					end="${fn:length(assertion.chainedAuthentications)-2}" step="1">
					<cas:proxy>${fn:escapeXml(proxy.principal.id)}</cas:proxy>
				</c:forEach>
			</cas:proxies>
		</c:if>
	</cas:authenticationSuccess>
</cas:serviceResponse>
