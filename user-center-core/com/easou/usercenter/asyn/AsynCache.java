package com.easou.usercenter.asyn;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
/**
 * 异步缓存
 * 
 * @author damon
 * @since 2012.11.15
 * @param <T>
 */
public class AsynCache<T> {
	
	/**缓存名*/
	private AsynCacheName cacheName;
	/**操作类型*/
	private AsynAction action;
	/**缓存对象*/
	private ConcurrentLinkedQueue<T> cache;
	public AsynCache(AsynCacheName cacheName,AsynAction action){
		cacheName = cacheName;
		action = action;
		cache = new ConcurrentLinkedQueue<T>();  
	}
	
	public AsynCache(AsynCacheName cacheName){
		cacheName = cacheName;
		action = AsynAction.ALL;
		cache = new ConcurrentLinkedQueue<T>();  
	}
	
	public void offer(T t){
		cache.offer(t);
	}
	
	public T poll(){
		return cache.poll();
	}
	
	/**
	 * 一次取size条数据
	 * 
	 * @param size
	 * @return
	 */
	public List<T> poll(int size){
		if(size<1){
			return null;
		}
		List<T> tempList = new ArrayList<T>();
		int count =0;
		while(!cache.isEmpty()&&count++<size){
			tempList.add(cache.poll());
		}
		return tempList;
	}
	
	public boolean isEmpty(){
		return cache.isEmpty();
	}
	
	
	public static void main(String[] mrgs){
		
		AsynCache<String> cache = new AsynCache<String>(AsynCacheName.USER_CACHE);
		cache.offer("1");
		cache.offer("2");
		cache.offer("3");
		cache.offer("1");
		while(!cache.isEmpty()){
			System.out.println(cache.poll());
		}
	}

}
