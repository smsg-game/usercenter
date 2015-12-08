<%@ page language="java"  session="false" %>
<%
	final String queryString = request.getQueryString();
	final String url = request.getContextPath() + "/user/userCenter"
			+ (queryString != null ? "?" + queryString : "");
	response.sendRedirect(response.encodeURL(url));
%>
