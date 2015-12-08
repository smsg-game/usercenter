package com.easou.usercenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EucResult<T> implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4950571318798322864L;
	
	public static String DEFAULT_VERSION = "1.0";
	public static int BUS_ERROR_CODE = 2001;
	public static int SUCCESS_CODE = 0;

	protected int resultCode;

	private String version;

	private List<ErrorDesc> descList;

	private T result;
	
	public <T> EucResult<T> buildBusErrorResult(){
		EucResult<T> result = new EucResult<T>();
		result.setResultCode(BUS_ERROR_CODE);
		return result;
	}
	
	/**
	 * 
	 * @param descCode
	 * @param desc
	 * @return
	 */
	public static <T> EucResult<T> buildBusErrorResult(Integer descCode,String desc){
		EucResult<T> result = new EucResult<T>();
		result.setResultCode(BUS_ERROR_CODE);
		result.addErrorDesc(descCode, desc);
		return result;
	}
	
	public static <T> EucResult<T> buildBusErrorResult(ErrorDesc desc){
		EucResult<T> result = new EucResult<T>();
		result.setResultCode(BUS_ERROR_CODE);
		result.getDescList().add(desc);
		return result;
	}
	
	/**
	 * 成功结果
	 * 
	 * @param t
	 * @return
	 */
	public static <T> EucResult<T> buildSuccResult(T t){
		EucResult<T> result = new EucResult<T>();
		result.setResultCode(SUCCESS_CODE);
		result.setResult(t);
		return result;
	}

	private EucResult() {
		this.version = DEFAULT_VERSION;
	}
	
	public EucResult(int code){
		super();
		setResultCode(code);
	}

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	public List<ErrorDesc> getDescList() {
		return descList;
	}

	public void setDescList(List<ErrorDesc> descList) {
		this.descList = descList;
	}

	/**
	 * 增加错误描述
	 * 
	 * @param descCode
	 *      错误编码
	 * @param desc
	 *      错误描述
	 */
	public void addErrorDesc(Integer descCode,String desc){
		if(descList==null){
			descList = new ArrayList<ErrorDesc>();
		}
		descList.add(new ErrorDesc(descCode,desc));
	}

}