package com.easou.usercenter.service;

public interface RegisterService {
	
	public String genBookNum(String username);
	public String getBookNum(String username);
	public boolean delBookNum(String username);
}
