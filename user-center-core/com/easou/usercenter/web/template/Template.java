package com.easou.usercenter.web.template;
/**
 * 模板
 * 
 * @author damon
 * @since 2012.05.30
 * @version 1.0
 */
public enum Template {

	/**
	 * 彩版
	 */
	COLOR("default","c","/default/"),
	/**
	 * 触屏版
	 */
	TOUCH("touch","t","/touch/"),
	/**
	 * 简版
	 */
	SIMPLE("simple","s","/simple/");
	
	/**
	 * 名称
	 */
	public String name;
	
	/**
	 * 版本参数
	 */
	public String wver;
	/**
	 * 路径
	 */
	public String path;
	
	private Template(String name,String wver,String path){
		this.name = name;
		this.wver = wver;
		this.path = path;
	}
	
	/**
	 * 根据版本参数获取模板
	 * 
	 * @param wver
	 * @return
	 */
	public static Template getTemplate(String wver){
		if(wver==null){
			return null;
		}
		String temp = wver.toLowerCase();
		if(COLOR.wver.equals(temp)){
			return COLOR;
		}else if(TOUCH.wver.equals(temp)){
			return TOUCH;
		}else if(SIMPLE.wver.equals(temp)){
			return SIMPLE;
		} else {
			return null;
		}
	}
	
	/**
	 * 根据版本参数获取模板
	 * 
	 * @param name
	 * @return
	 */
	public static Template getTemplateByName(String name){
		if(name==null){
			return null;
		}
		String temp = name.toLowerCase();
		if(COLOR.name.equals(temp)){
			return COLOR;
		}else if(TOUCH.name.equals(temp)){
			return TOUCH;
		}else if(SIMPLE.name.equals(temp)){
			return SIMPLE;
		} else {
			return null;
		}
	}
	
	/**
	 * 检查该路径所属模板
	 * 
	 * @param path
	 * @return
	 */
	public static Template checkTemplate(String path){
		if(path.indexOf(COLOR.path)!=-1){
			return COLOR;
		}else if(path.indexOf(TOUCH.path)!=-1){
			return TOUCH;
		}else if(path.indexOf(SIMPLE.path)!=-1){
			return SIMPLE;
		} else
		return null;
	}
}
