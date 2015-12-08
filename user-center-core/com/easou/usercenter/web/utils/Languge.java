package com.easou.usercenter.web.utils;

public enum Languge {
	BigChinese(0),
	English(1),
	SimpleChinese(2);
	
	private int intValue;
	
	public int intValue(){
		return intValue;
	}
	private Languge(int intValue){
		this.intValue = intValue;
	}
	public static Languge getName(String str){
		if(str==null||str.equals(""))return BigChinese;
		if("English".equals(str)){
			return English;
		}else if("BigChinese".equals(str)){
			return BigChinese;
		}else if("SimpleChinese".equals(str)){
			return SimpleChinese;
		}
		return BigChinese;
	}
}
