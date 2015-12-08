package com.easou.usercenter.web.touch;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.easou.usercenter.web.user.UserBaseController;

public class AjaxBaseController extends UserBaseController {

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
		response.setContentType("text/html;charset=UTF-8");
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		response.setContentType("text/html;charset=UTF-8");
		try {
			if (result != null) {
				response.getWriter().write(result);
			}
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
		return null;
	}

}
