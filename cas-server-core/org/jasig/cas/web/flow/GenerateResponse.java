package org.jasig.cas.web.flow;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jasig.cas.authentication.principal.Response;
import org.jasig.cas.web.support.WebUtils;
import org.springframework.webflow.execution.RequestContext;

public class GenerateResponse {
	 public Response getResponse( final RequestContext context ) {
		 	String orgUrl = "";

		 	final Map<String, String> parameters = new HashMap<String, String>();
		 	
		 	
	        final HttpServletRequest request = WebUtils.getHttpServletRequest(context);
	        String queryStr = request.getQueryString();
	        if(null!=queryStr) {
	        	orgUrl = "/user/userCenter?" + queryStr;
	        } else {
	        	orgUrl = "/user/userCenter";
	        }
	        
//	        context.getFlowScope().put("responseUrl", orgUrl);
 
	        Response ret =  Response.getRedirectResponse(orgUrl, parameters);
	        return ret;
	    }
}