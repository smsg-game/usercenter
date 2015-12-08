package com.easou.usercenter.util.key;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/*******************************************************************************
 * @version 1.0
 * @author xu yi feng
 *
 */
public class FilterUtil {
	private FilterUtil() {
	}

	public enum FilterType {
		NiCKNAME, NAME, ARTICLE
	}

	/***************************************************************************
	 * 0x20-0x7e 英文数字，标点符号
	 * 0x3041-0x3129 日文
	 * 0x4e00-0x9fa5,0xe815-0xe864,0xff90-0xfa2d 中文
	 * 0x1100-0x1159,0x11a8-0x11f9,0x3131-0x321c,0xac00-0xd7a3 韩文
	 * 0xff01-0xff40 全角字符
	 * 0x3001-0x3017 个别符号
	 * 0x2010--0x2027 中文标点符号
	 * @param c
	 * @return
	 */
	public static char isValidChar(char c) {
		if (c == '\n' || c == '\r' || c == '\t') {
			return c;
		}
		if (c <= 0x20) {
			return ' ';
		}
		if (c == 0x28){
			return '（';
		}
		if (c == 0x29){
			return '）';
		}
		if (c > 0x20 && c <=0x7e) {
			return c;
		}
		if (c >= 0x2010 && c <= 0x2027) {
			return c;
		}

		if (c >= 0x3001 && c <= 0x3017) {
			return c;
		}

		if (c >= 0x3041 && c < 0x3129) {
			return c;
		}
		if ((c >= 0x4e00 && c <= 0x9fa5) || (c >= 0xe815 && c <= 0xe864)
				|| (c >= 0xff90 && c <= 0xfa2d)) {
			return c;
		}
		if ((c >= 0x1100 && c <= 0x1159) || (c >= 0x11a8 && c <= 0x11f9)
				|| (c >= 0x3131 && c <= 0x321c) || (c >= 0xac00 && c <= 0xd7a3)) {
			return c;
		}
//		if (c >= 0xff01 && c <= 0xff40) {
//			c=(char) (c - 65248);
//		}

		return c;
	}
	/**
	 * 过滤掉特殊符号
	 * @param content
	 * @return
	 */
	public static String filterChar(String content) {
		StringBuilder newContent = new StringBuilder();
		for (char c : content.toCharArray()) {
			c = isValidChar(c);
			newContent.append(c == ' ' ? "" : c);
		}
		return newContent.toString();
	}
	/**
	 * 拿到数字和字母
	 * @param content
	 * @return
	 */
	public static String getNumberOrLetter(String content){
		StringBuilder newContent = new StringBuilder();
		for (char c : content.toCharArray()) {
			if((c >='0' && c <='9') || (c >='a' && c <='z') || (c >='A' && c <='Z')){
				newContent.append(c);
			}
		}
		return newContent.toString();
	}
	/***
	 * 拿到汉字
	 * @param content
	 * @return
	 */
	public static String getChinese(String content){
		StringBuilder newContent = new StringBuilder();
		for (char c : content.toCharArray()) {
			if ((c >= 0x4e00 && c <= 0x9fa5) || (c >= 0xe815 && c <= 0xe864)
					|| (c >= 0xff90 && c <= 0xfa2d)) {
				newContent.append(c);
			}
		}
		return newContent.toString();
	}
	/***
	 * 是否是网址
	 * @param content
	 * @return
	 */
	public static boolean isHasWebAddr(String content) {
		Pattern p = Pattern.compile("[\\S]{2,}\\.[\\w|-|!]{2,4}");
		Matcher m = p.matcher(content);
		return m.find();
	}
	/***
	 * 过滤掉标记
	 * @param text
	 * @return
	 */
	public static String strip_tags(String text) {
		return strip_tags(text, "");
	}

	public static String strip_tags(String text, String allowedTags) {
		String[] tag_list = allowedTags.split(",");
		Arrays.sort(tag_list);
		final Pattern p = Pattern.compile("<[/!]?([^\\s>]*)\\s*[^>]*>",
				Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(text);
		StringBuffer out = new StringBuffer();
		int lastPos = 0;
		while (m.find()) {
			String tag = m.group(1);
			// if tag not allowed: skip it
			if (Arrays.binarySearch(tag_list, tag) < 0) {
				out.append(text.substring(lastPos, m.start())).append(" ");
			} else {
				out.append(text.substring(lastPos, m.end()));
			}
			lastPos = m.end();
		}
		if (lastPos > 0) {
			out.append(text.substring(lastPos));
			return out.toString().trim();
		} else {
			return text;
		}
	}
	/***************************************************************************
	 * 查找str与regx相匹配的子串,count取匹配的次数,为0时所有,取出匹配的组号数据
	 *
	 * @param str
	 * @param regx
	 * @param count
	 * @return
	 **************************************************************************/
	public static String getMatcherString(String str, String regx, int count,
			int goupNum) {
		if (StringUtils.isEmpty(str) || StringUtils.isEmpty(regx)) {
			return "";
		}
		Pattern p = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(str);
		StringBuffer newStr = new StringBuffer();
		int i = 0;
		while (m.find()) {
			if (goupNum > 0) {
				newStr.append(m.group(goupNum));
			} else {
				newStr.append(m.group());
			}
			if ((++i) == count || count==0) {
				break;
			}
		}
		return newStr.toString();
	}

	public static String getMatcherString(String str, String regx, int count) {
		return getMatcherString(str, regx, count, 0);
	}
	public static void main(String []args){
		String s="老子(女干)你老马()";
		System.out.println(filterChar(s));
		System.out.println(isHasWebAddr("12.12"));
	}
}
