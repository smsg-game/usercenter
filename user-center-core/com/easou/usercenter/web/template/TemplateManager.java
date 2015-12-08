package com.easou.usercenter.web.template;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.easou.usercenter.config.SSOConfig;

/**
 * 模板管理类
 * 根据返回的路径和模板参数修改返回的页面
 * 使之返回对应的模板页面
 * 
 * @author damon
 * @since 2012.05.30
 * @version 1.0
 * 
 */
public class TemplateManager {

	protected final Log LOG = LogFactory.getLog(TemplateManager.class);

	private static String DEFAULT_TEMPLATE = "template.default";

	private static TemplateManager instance;
	/**
	 * 默认模板
	 */
	private Template defaultTemplate;

	private static String WVER = "wver";

	/**
	 * 单例方法
	 * 
	 * @return
	 */
	public static TemplateManager getInstance() {
		if (instance == null) {
			instance = new TemplateManager();
		}
		return instance;
	}

	/**
	 * 私有构造函数
	 */
	private TemplateManager() {
		// 初始化参数
		defaultTemplate = Template.getTemplateByName(SSOConfig
				.getProperty(DEFAULT_TEMPLATE));
	}

	/**
	 * 根据主题获取路径
	 * 
	 * @param request
	 * @param path
	 * @return
	 */
	public String getRequestDispatcherPath(HttpServletRequest request,
			String path) {
		String wver = request.getParameter(WVER);
		Template newTemplate = Template.getTemplate(wver);
		if (LOG.isDebugEnabled()) {
			LOG.debug("get the template[" + newTemplate
					+ "] by the parameter wver[" + wver + "]");
		}
		String local = request.getRealPath("/");
		String temp = request.getLocalAddr();
		if (checkSourceIsExist(local, path)) {// 判断文件是否存在
			return getRequestDispatcherPath(path, newTemplate);
		}

		return null;
	}

	/**
	 * 根据模板获取路径
	 * 
	 * @param path
	 *            路径
	 * @param template
	 *            模板类型
	 * @return
	 */
	protected String getRequestDispatcherPath(String path, Template newTemplate) {
		Template formerTemplate = Template.checkTemplate(path);
		if (formerTemplate == null) {// 如果不是模板页面，直接返回
			return path;
		}
		if(newTemplate == null){//新模板为空
			if(formerTemplate == defaultTemplate){
				if(LOG.isDebugEnabled()){
					LOG.debug("former path[" + path + "] is belong the default template["+defaultTemplate+"]");
				}
				return path;
			}
			return changeDefaultDispatcherPath(path, formerTemplate);
		}
		if (newTemplate != formerTemplate) {// 转换成新的模板路径
			return changeDispatcherPath(path, formerTemplate, newTemplate);

		}
		return path;
	}

	/**
	 * 更改模板路径
	 * 
	 * @param path
	 *            路径
	 * @param formerTemplate
	 *            原模板
	 * @param newTemplate
	 *            新模板
	 * @return
	 */
	protected String changeDispatcherPath(String path, Template formerTemplate,
			Template newTemplate) {
		String newPath = path.replaceFirst(formerTemplate.path,newTemplate.path
				);
		if (LOG.isDebugEnabled()) {
			LOG.debug("former path[" + path + "] change to new path[" + newPath
					+ "]");
		}
		return newPath;
	}
	
	/**
	 * 更改模板路径
	 * 
	 * @param path
	 *            路径
	 * @param formerTemplate
	 *            原模板
	 * @param newTemplate
	 *            新模板
	 * @return
	 */
	protected String changeDefaultDispatcherPath(String path, Template formerTemplate) {
		if(defaultTemplate == null)
			return path;
		if (LOG.isDebugEnabled()) {
			LOG.debug("former template[" + formerTemplate 
					+ "] change to default template[" + defaultTemplate
					+ "]");
		}
		return changeDispatcherPath(path, formerTemplate, defaultTemplate);
	}

	/**
	 * 判断资源是否存在 暂时没有实现此方法 此方法需要去读本地资源
	 * 
	 * @param local
	 * @param path
	 * @return
	 */
	protected boolean checkSourceIsExist(String local, String path) {
		return true;
	}

}
