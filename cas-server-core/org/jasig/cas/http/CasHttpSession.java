package org.jasig.cas.http;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.util.Itermeration;

public class CasHttpSession implements HttpSession {
	private static final Log log = LogFactory.getLog(CasHttpSession.class);
	private CacheService service = CacheService.getInstance();
	private Map<String, Object> attribute = null;
	private boolean hasChange = false;
	private static final int EFFECTTIME = 30 * 60;

	private String id;
	@Override
	public Object getAttribute(String name) {
		return attribute.get(name);
	}

	public CasHttpSession(HttpServletRequest request) {
		// 判定cookie中是否存在
		id = request.getRequestedSessionId();
		
		if (id == null) {
			
			if (log.isDebugEnabled()) {
				log.debug("不存在session id,开始新生成一个");
			}
			
			id = request.getSession().getId();
			
		}
		// 获取信息
		this.attribute = (Map<String, Object>) service.get(id);
		
		if (log.isDebugEnabled()) {
			log.debug("开始获取session信息,session id为" + id + " 信息为: " + this.attribute + " 地址: " + request.getRequestURL());
		}
		
		if (attribute == null) {
			attribute = new HashMap<String, Object>();
		}
	}

	@Override
	public long getCreationTime() {
		return 0;
	}

	/**
	 * 设置属性值信息
	 * 
	 * @param map
	 *            属性信息
	 */
	protected void setAttributes(Map<String, Object> map) {
		this.attribute = map;
	}

	/**
	 * 获取所有属性信息
	 * 
	 * @return
	 */
	protected Map<String, Object> getAttributes() {
		return this.attribute;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public long getLastAccessedTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxInactiveInterval() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ServletContext getServletContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpSessionContext getSessionContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getValue(String name) {
		// TODO Auto-generated method stub
		return attribute.get(name);
	}

	@Override
	public String[] getValueNames() {
		return attribute.keySet().toArray(new String[attribute.size()]);
	}

	@Override
	public void invalidate() {

	}

	@Override
	public boolean isNew() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void putValue(String name, Object value) {
		this.attribute.put(name, value);
		hasChange = true;
	}

	@Override
	public void removeAttribute(String name) {
		this.attribute.remove(name);
		hasChange = true;
	}

	@Override
	public void removeValue(String name) {
		removeAttribute(name);
	}

	@Override
	public void setAttribute(String name, Object value) {
		putValue(name, value);
	}

	@Override
	public void setMaxInactiveInterval(int arg0) {

	}

	@Override
	public Enumeration getAttributeNames() {
		// TODO Auto-generated method stub
		return new Itermeration(this.attribute.keySet().iterator());
	}

	/**
	 * 将数据添加到缓存
	 */
	protected void saveToCache() {
		if (log.isDebugEnabled()) {
			log.debug("session信息是否有改变:" + this.hasChange);
		}
		if (this.hasChange) {
			if (log.isDebugEnabled()) {
				log.debug("session信息发生改变,开始将值: " + this.attribute + "存储到缓存");
			}
			boolean re = service.add(this.getId(), this.attribute, EFFECTTIME);
			if (log.isDebugEnabled()) {
				if (re) {
					log.debug("缓存id为" + this.getId() + "的session成功!");
				} else {
					log.debug("缓存id为" + this.getId() + "的session失败!");
				}
			}
		}
	}

	public static void main(String[] str) {
		String s = "/login;jsessionid=aYlifO70KzT_5dbXIz";
		int indexa = s.indexOf("?");
		if (indexa > 0)
			System.out.println(s.substring(s.indexOf(";jsessionid=") + 12, s
					.indexOf("?")));
		else {
			System.out.println(s.substring(s.indexOf(";jsessionid=") + 12));
		}
	}
}
