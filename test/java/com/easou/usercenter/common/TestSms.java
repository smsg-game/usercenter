package com.easou.usercenter.common;

import com.easou.usercenter.asyn.helper.SendSmsHelper;

public class TestSms {

	public static void main(String[] args) {
		SendSmsHelper.sendSms("123", "abc");
	}
}
