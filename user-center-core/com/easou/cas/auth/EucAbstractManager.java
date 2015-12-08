package com.easou.cas.auth;

abstract class EucAbstractManager{	
	
	protected static String RESULT_JSON ="json";
	
	protected static String RESULT_TYPE ="resultType";

	/**
	 * 校验url的合法性
	 * 
	 * @param url
	 * @return
	 */
	protected boolean verify(String url){
		if(url==null||"".equals(url)){
			return false;
		}
		return true;
	}

}
