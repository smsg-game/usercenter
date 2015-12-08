package com.easou.usercenter;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;



public class AjaxBaseController{

	private static Logger LOG = Logger.getLogger(AjaxBaseController.class);

	/**
	 * 返回操作结果
	 * 
	 * @return
	 * @throws IOException
	 */
	protected String renderJSONFailure(HttpServletResponse response,
			String result) {
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		response.setContentType("text/json;charset=UTF-8");
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			LOG.error(e,e);
		}
		return null;
	}

	/**
	 * 返回操作结果
	 * 
	 * @return
	 * @throws IOException
	 */
	protected String renderJSONSuccess(HttpServletResponse response,
			String result) {
		response.setContentType("text/json;charset=UTF-8");
		try {
			if (result != null) {
				response.getWriter().write(result);
			}
		} catch (IOException e) {
			LOG.error(e, e);
		}
		return null;
	}

}
