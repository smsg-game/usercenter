package com.easou.common.api;

public class JBean implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2713093618520553591L;
	private JHead head;
	private JBody body;
	private JDesc desc;
	public JHead getHead() {
		return head;
	}
	public void setHead(JHead head) {
		this.head = head;
	}
	public JBody getBody() {
		return body;
	}
	public void setBody(JBody body) {
		this.body = body;
	}
	public JDesc getDesc() {
		return desc;
	}
	public void setDesc(JDesc desc) {
		this.desc = desc;
	}
}
