package com.easou.cas.validation;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class EAssertion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2204893136392011054L;
	private Long userId;

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	private Map<String, Object> attributes = new HashMap<String, Object>();

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public Long getUserId() {
		return this.userId;
	}

	public Object removeAttribute(String key) {
		return this.attributes.remove(key);
	}

	public Object setAttribute(String key, Object value) {
		return this.attributes.put(key, value);
	}
}