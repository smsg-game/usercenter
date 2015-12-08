package com.easou.usercenter.web.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.view.JstlView;

import com.easou.usercenter.web.template.TemplateManager;
/**
 * 继承JstlView
 * 主要是为了支持多主题和模板
 * 
 * @author damon
 * @since 2012.05.30
 * @version 1.0
 */
public class SJstlView extends JstlView {
	
	protected final Log LOG = LogFactory
	.getLog(JstlView.class);
	
	/**
	 * 重载此方法，重载的目的是返回对应模板页面
	 */
	protected String prepareForRendering(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		String path = super.prepareForRendering(request, response);
		//页面新路径
		String newPath = TemplateManager.getInstance().getRequestDispatcherPath(request, path);
		this.setUrl(newPath);
		return newPath;
	}

}
