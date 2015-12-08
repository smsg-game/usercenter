package com.easou.usercenter.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ui.ModelMap;

public class EucMessage {

	private List<String> msg;	// 返回错误信息
	
	public void addMsg(String message){
		if(null==message || "".equals(message)) {
			return;
		}
		if(null==msg) {
			msg = new ArrayList<String>();
		}
		msg.add(message);
	}
	
	public List<String> getMsg() {
		if(null!=msg && msg.size()>0) {
			return msg;
		} else {
			return null;
		}
	}
	
	public void bindMsg(ModelMap model) {
		if(null!=msg && msg.size()>0) {
			model.addAttribute("msg", msg);
		}
	}
	
	public boolean hasMsg() {
		if(null==msg)
			return false;
		else
			return msg.size()>0;
	}
}
