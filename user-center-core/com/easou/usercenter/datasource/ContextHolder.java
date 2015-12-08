package com.easou.usercenter.datasource;

/** 
 * DataSource上下文句柄，通过此类设置需要访问的对应数据源 
 * 
 */ 
@SuppressWarnings("unchecked")
 public class ContextHolder {  
   
	   
	     private static final ThreadLocal contextHolder = new ThreadLocal(); 
	       
	     public static <T> void setContext(T context)  
	     {  
	         contextHolder.set(context);  
	     }  
	       
	     public static <T> T getContext()  
	     {  
	         return (T) contextHolder.get();  
	     }  
	       
	     public static void clearContext()  
	     {  
	         contextHolder.remove();  
	     }  
	     
 }  