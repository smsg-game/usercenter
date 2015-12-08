package com.easou.common.api;

import java.util.ArrayList;
import java.util.List;

/**
 * 返回结果类
 * 
 * @author damon
 * @version 1.0
 * @since 2012.06.20
 * 
 * @param <T>
 * 
 * @author damon 2012.07.03 增加序列化
 */
public class EucApiResult<T> implements java.io.Serializable {

	public static String DEFAULT_VERSION = "1.0";
	// public static int BUS_ERROR_CODE = 2001;
	// public static int SUCCESS_CODE = 0;

	/**
	 * 
	 */
	private static final long serialVersionUID = 3115288119065552092L;

	protected String resultCode;

	private String version;

	private List<JReason> descList = new ArrayList<JReason>();

	private T result;

	public EucApiResult(JBean jbean) throws EucParserException {
		init(jbean);
	}

	public EucApiResult() {
		this.version = DEFAULT_VERSION;
	}

	private void init(JBean jbean) throws EucParserException {
		if (jbean != null) {
			JHead jhead = jbean.getHead();
			if (null == jhead.getRet()) {
				throw new EucParserException("返回码错误");
			}
			if (null != jbean.getDesc()) {
				JDesc jdesc = jbean.getDesc();
				for (int i = 0; i < jdesc.size(); i++) {
					getDescList().add(jdesc.getReason(i));
				}
			}
			setResultCode(jhead.getRet());
			setVersion(jhead.getVersion());
		}
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public List<JReason> getDescList() {
		return descList;
	}

	public void setDescList(List<JReason> descList) {
		this.descList = descList;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

}
