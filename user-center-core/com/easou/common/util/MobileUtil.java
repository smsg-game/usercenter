package com.easou.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MobileUtil {
	/**
	 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
	 * 
	 * 　　联通：130、131、132、152、155、156、185、186
	 * 
	 * 　　电信：133、153、180、189、（1349卫通）
	 * 
	 * 
	 * 判定是否为手机号码
	 * 
	 * @param mStr
	 *            手机号字符串
	 * @return
	 */
	public static boolean isMobile(String mStr) {
		Pattern p = Pattern
				.compile("1[3458]\\d{9}");
		Matcher m = p.matcher(mStr);
		return m.matches();
	}
	
	public static String secertString(String mobile) {
		try {
		if(null!=mobile && mobile.trim().length()>2) {
			StringBuffer sb=new StringBuffer();
			String _mobile = mobile.trim();
			int length = _mobile.length();
			sb.append(_mobile.substring(0, 2)).append("******").append(_mobile.substring(length-2));
			return sb.toString();
		} else {
			return mobile;
		}
		} catch(Exception e) {
			return null;
		}
	}

}
