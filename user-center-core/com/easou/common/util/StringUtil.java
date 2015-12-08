package com.easou.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * 字符串处理类
 *
 * @author <a href="mailto:arden.emily@gmail.com>arden</a>
 */
public class StringUtil {
	public static DecimalFormat df = new DecimalFormat("0.00");

	// html特殊字符
	static String[] entityNumber = new String[] { "&#161;", "&#162;", "&#163;",
			"&#164;", "&#165;", "&#166;", "&#167;", "&#168;", "&#169;",
			"&#170;", "&#171;", "&#172;", "&#173;", "&#174;", "&#175;",
			"&#176;", "&#177;", "&#178;", "&#179;", "&#180;", "&#181;",
			"&#182;", "&#183;", "&#184;", "&#185;", "&#186;", "&#187;",
			"&#188;", "&#189;", "&#190;", "&#191;", "&#215;", "&#247;",
			"&#192;", "&#193;", "&#194;", "&#195;", "&#196;", "&#197;",
			"&#198;", "&#199;", "&#200;", "&#201;", "&#202;", "&#203;",
			"&#204;", "&#205;", "&#206;", "&#207;", "&#208;", "&#209;",
			"&#210;", "&#211;", "&#212;", "&#213;", "&#214;", "&#216;",
			"&#217;", "&#218;", "&#219;", "&#220;", "&#221;", "&#222;",
			"&#223;", "&#224;", "&#225;", "&#226;", "&#227;", "&#228;",
			"&#229;", "&#230;", "&#231;", "&#232;", "&#233;", "&#234;",
			"&#235;", "&#236;", "&#237;", "&#238;", "&#239;", "&#240;",
			"&#241;", "&#242;", "&#243;", "&#244;", "&#245;", "&#246;",
			"&#248;", "&#249;", "&#250;", "&#251;", "&#252;", "&#253;",
			"&#254;", "&#255;", "&#8704;", "&#8706;", "&#8707;", "&#8709;",
			"&#8711;", "&#8712;", "&#8713;", "&#8715;", "&#8719;", "&#8721;",
			"&#8722;", "&#8727;", "&#8730;", "&#8733;", "&#8734;", "&#8736;",
			"&#8743;", "&#8744;", "&#8745;", "&#8746;", "&#8747;", "&#8756;",
			"&#8764;", "&#8773;", "&#8776;", "&#8800;", "&#8801;", "&#8804;",
			"&#8805;", "&#8834;", "&#8835;", "&#8836;", "&#8838;", "&#8839;",
			"&#8853;", "&#8855;", "&#8869;", "&#8901;", "&#913;", "&#914;",
			"&#915;", "&#916;", "&#917;", "&#918;", "&#919;", "&#920;",
			"&#921;", "&#922;", "&#923;", "&#924;", "&#925;", "&#926;",
			"&#927;", "&#928;", "&#929;", "&#931;", "&#932;", "&#933;",
			"&#934;", "&#935;", "&#936;", "&#937;", "&#945;", "&#946;",
			"&#947;", "&#948;", "&#949;", "&#950;", "&#951;", "&#952;",
			"&#953;", "&#954;", "&#955;", "&#956;", "&#957;", "&#958;",
			"&#959;", "&#960;", "&#961;", "&#962;", "&#963;", "&#964;",
			"&#965;", "&#966;", "&#967;", "&#968;", "&#969;", "&#977;",
			"&#978;", "&#982;", "&#338;", "&#339;", "&#352;", "&#353;",
			"&#376;", "&#402;", "&#710;", "&#732;", "&#8194;", "&#8195;",
			"&#8201;", "&#8204;", "&#8205;", "&#8206;", "&#8207;", "&#8211;",
			"&#8212;", "&#8216;", "&#8217;", "&#8218;", "&#8220;", "&#8221;",
			"&#8222;", "&#8224;", "&#8225;", "&#8226;", "&#8230;", "&#8240;",
			"&#8242;", "&#8243;", "&#8249;", "&#8250;", "&#8254;", "&#8364;",
			"&#8482;", "&#8592;", "&#8593;", "&#8594;", "&#8595;", "&#8596;",
			"&#8629;", "&#8968;", "&#8969;", "&#8970;", "&#8971;", "&#9674;",
			"&#9824;", "&#9827;", "&#9829;", "&#9830;" };
	static char[] chars;
	static {
		chars = new char[entityNumber.length];
		for (int i = 0; i < entityNumber.length; i++) {
			String str = entityNumber[i];
			str = str.replaceAll("&#(\\d{1,4});", "$1");
			chars[i] = (char) Integer.parseInt(str);
		}
	}

	/**
	 * 64位编码字符
	 */
	final static char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
			'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
			'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
			'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',
			'Z', '-', '_', };

	public static String delBr(String str) {
		String endStr = str;
		if (str.endsWith("<br/")) {
			endStr = str.substring(0, str.length() - 4);
		} else if (str.endsWith("<br")) {
			endStr = str.substring(0, str.length() - 3);
		} else if (str.endsWith("<b")) {
			endStr = str.substring(0, str.length() - 2);
		} else if (str.endsWith("<")) {
			endStr = str.substring(0, str.length() - 1);
		}

		if (endStr.startsWith("br/>")) {
			return endStr.substring(4, endStr.length());
		} else if (endStr.startsWith("r/>")) {
			return endStr.substring(3, endStr.length());
		} else if (endStr.startsWith("/>")) {
			return endStr.substring(2, endStr.length());
		} else if (endStr.startsWith(">")) {
			return endStr.substring(1, endStr.length());
		}
		return endStr;

	}

	/**
	 * 获得正确的完整的URL
	 *
	 * @param str
	 *            原来的url
	 * @return
	 */
	public static String getTrueUrl(String str) {
		str = str.trim();// ȥ��ո�;
		if (str.startsWith("http://")) {
			return str;
		} else {
			return "http://" + str;
		}
	}

	public static String getLenStr(String str, int len) {
		if (str.length() > len) {
			return str.substring(0, len) + "...";
		} else {
			return str;
		}
	}

	/**
	 *
	 * @param str
	 *            String
	 * @return String
	 */

	public static String isoToGBK(String str) {
		if (str == null) {
			return "";
		}
		try {
			byte[] bytes = str.getBytes("iso-8859-1");
			String destStr = new String(bytes, "GBK");
			return destStr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 转换指定字符串的编码
	 *
	 * @param str
	 * @param fromEncoding
	 * @param toEncoding
	 * @return
	 */
	public static String convert(String str, String fromEncoding,
			String toEncoding) {
		if (str == null) {
			return "";
		}
		try {
			byte[] bytes = str.getBytes(fromEncoding);
			String destStr = new String(bytes, toEncoding);
			return destStr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

//	private static String StringToUnicode(String text) {
//		String result = "";
//		int input;
//		InputStreamReader isr = new InputStreamReader(
//				new StringBufferInputStream(text));
//		try {
//			isr = new InputStreamReader(new StringBufferInputStream(text),
//					"GBK");
//		} catch (UnsupportedEncodingException e) {
//			return "-1";
//		}
//		try {
//			while ((input = isr.read()) != -1) {
//				result = result + "&#x" + Integer.toHexString(input) + ";";
//			}
//		} catch (java.io.IOException e) {
//			return "-2";
//		}
//		try {
//			isr.close();
//		} catch (java.io.IOException e) {
//			return "-3";
//		}
//		return result;
//	}

	public static String toUnicode(java.lang.String text) {
		if (text == null)
			return "";
		char chars[] = text.toCharArray();
		java.lang.StringBuffer sb = new StringBuffer();
		int length = chars.length;
		for (int i = 0; i < length; i++) {
			int s = chars[i];
			sb.append("&#");
			sb.append(s);
			sb.append(";");
		}

		return sb.toString();
	}

	/**
	 * 检测字符串里是否有中文字符
	 *
	 * @param str
	 * @return
	 */
	public static boolean chinese(String str) {
		if (str != null && str.matches(".*[\u4e00-\u9fa5].*")) // 中文
			return true;
		return false;

	}

	/**
	 * 检测输入的邮政编码是否合法
	 *
	 * @param code
	 * @return
	 */
	public static boolean isPostCode(String code) {
		if (code == null) {
			return false;
		}
		String regex = "[1-9]\\d{5}";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(code);
		boolean validate = m.matches();
		return validate;
	}

	/**
	 * 检测字符串是否为空，或者空字符串
	 *
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return StringUtils.isEmpty(str);
	}
	
	/**
   * Refer to {@link StringUtils#isNotEmpty(String)}.
   *
   * @param str the String to check, may be null
   * @return <code>true</code> if the String is not empty and not null
   */
	public static boolean isNotEmpty(String str) {
		return StringUtils.isNotEmpty(str);
	}

	/**
	 * 字符串是否是"nul"字符串
	 *
	 * @param str
	 * @return
	 */
	public static boolean isNull(String str) {
		if (null == str && "null".equals(str)) {
			return true;
		}
		return false;
	}

	/**
	 * 将"null"字符串或者null值转换成""
	 *
	 * @param str
	 * @return
	 */
	public static String nullStringToEmptyString(String str) {
		if (str == null) {
			str = "";
		}
		if (str.equals("null")) {
			str = "";
		}
		return str;
	}

	/**
	 * 将"null"字符串或者null值转换成""
	 *
	 * @param str
	 * @return
	 */
	public static String nullStringToSetString(String str) {
		if (isEmpty(str)) {
			str = "设置";
		}
		if (str == null) {
			str = "设置";
		}
		if (str.equals("null")) {
			str = "设置";
		}
		return str;
	}

	/**
	 * 将"null"字符串或者null值转换成""
	 *
	 * @param str
	 * @return
	 */
	public static String nullStringToUnknowString(String str) {
		if (str == null) {
			str = "未知";
		}
		if (str.equals("null")) {
			str = "未知";
		}
		return str;
	}

	/**
	 * 屏掉WML不支持的代码
	 *
	 * @param str
	 * @return
	 */
	public static String wmlEncode(String str) {
		return wmlEncode(str, false);
	}

	public static String wmlEncode(String str, boolean filtercommon) {
		if (str == null || "".equals(str.trim()))
			return "";
		for (int i = 0; i < chars.length; i++) {
			if (str.contains("" + chars[i])) {// 特殊字符转义
				str = str.replaceAll("" + chars[i], entityNumber[i]);
			}
		}
		// 保留字符转义
		str = str.replaceAll("||||", ".");
		str = str.replaceAll("&", "&amp;");
		str = str.replaceAll("<", "&lt;");
		str = str.replaceAll(">", "&gt;");
		str = str.replaceAll("'", "&apos;");
		str = str.replaceAll("\"", "&quot;");
		if (!filtercommon)
			str = str.replaceAll("-", "&shy;");
		// 内码不进行转义
		str = str.replaceAll("&amp;#(\\d{1,4});", "&#$1;");
		str = str.replaceAll("\\$", "\\$\\$");
		return str;
	}

	/**
	 * 提取URL里的域名
	 *
	 * @param url
	 * @return
	 */
	public static String obtainDomain(String url) {
		String[] domains = { ".com", ".net", ".org", ".cn", ".com.cn", ".tv",
				".cc", ".info", ".net.cn", ".org.cn", ".us", ".biz", ".in",
				".gov.cn", ".name" };
		String correctUrl = "";
		if (url == null || url.equals("null") || url.equals("")
				|| !url.startsWith("http://")) {
			correctUrl = "";
		} else {
			// int startIndex = url.indexOf("http://");
			int startIndex = "http://".length();
			int endIndex = -1;
			for (String s : domains) {
				endIndex = url.indexOf(s);
				if (endIndex != -1) {
					endIndex += s.length();
					break;
				}
			}
			if (endIndex != -1) {
				int length = url.length();
				if (length > endIndex) {
					// 检查合法性
					String domain = url.substring(startIndex, endIndex + 1);
					if (domain.endsWith("/") || domain.endsWith("?")
							|| domain.endsWith(":") || domain.endsWith(".")) {
						domain = url.substring(startIndex, endIndex);
						correctUrl = domain;
					} else {
						// 不合法
						// System.out.println("URL 不合法");
						correctUrl = "";
					}
				} else {
					String domain = url.substring(startIndex, endIndex);
					correctUrl = domain;
				}
			}
		}
		return correctUrl;
	}

	/**
	 * 将字节转换成16进制
	 *
	 * @param b
	 *            byte[]
	 * @return String
	 */
	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
		}
		return hs.toUpperCase();
	}

	/**
	 * 是否是数字
	 *
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str) {
		str = StringUtil.nullStringToEmptyString(str);
		String regex = "\\d+";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		boolean validate = m.matches();
		return validate;
	}

	public static String filterUa(String ua) {
		ua = ua.replaceAll("\"", "");
		ua = ua.replaceAll(";", "");
		return ua;
	}

	public static boolean isLetterOrNumber(String str) {
		if (str == null) {
			return false;
		} else {
			String regex = "\\w+";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(str);
			boolean validate = m.matches();
			return validate;
		}
	}

	public static boolean isLetterStart(String str) {
		if (str == null) {
			return false;
		} else {
			String regex = "[a-zA-Z]{1,}\\w+";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(str);
			boolean validate = m.matches();
			return validate;
		}
	}

	public static boolean isNumberStart(String str) {
		if (str == null) {
			return false;
		} else {
			String regex = "\\d{1,}[^\\s]+";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(str);
			boolean validate = m.matches();
			return validate;
		}
	}
	
	public static String resetUrl(String str, String esid) {
		StringBuffer resultStr = new StringBuffer(str);
		resultStr.append(str.contains("?") ? "&amp;esid=" + esid : "?esid="
				+ esid);
		// resultStr.append(str.contains("?")?"&amp;esid=$${esid}"
		// :"?esid=$${esid}");
		return resultStr.toString();
	}

	public static Long StringToLong(String str) {
		if (StringUtils.isEmpty(str)) {
			return null;
		}
		return str.trim().matches("\\d{1,14}") ? Long.parseLong(str.trim()) : 0;
	}

	public static Integer StringToInt(String str) {
		if (StringUtils.isEmpty(str)) {
			return null;
		}
		return str.trim().matches("\\d{1,7}") ? Integer.parseInt(str.trim())
				: 0;
	}

	public static double getPercent(String x, String y) {
		return Double.parseDouble(x) / Double.parseDouble(y);
	}

	public static String doubleToString(String str) {
		int temp = ((int) Double.parseDouble(str));
		return temp + "";
	}

	public static boolean Contains(String str, String str1) {
		if (StringUtils.isNotEmpty(str)) {
			return str.contains(str1);
		} else {
			return false;
		}
	}

	public static int random(int start, int end) {
		return (int) (Math.random() * (double) end) + start;
	}

	public static int random(int end) {
		return random(1, end);
	}

	public static long getSerial(String password) {
		long serial = random(4094);
		if (null == password || "".equals(password.trim())) {
			return serial;
		}
		if (password.length() == 8) {
			serial = Long.valueOf(password).longValue() % 4095L;
		}
		if (password.length() > 13) {
			serial = Long.valueOf(password.substring(13)).longValue() % 4095L;
		}
		return serial;
	}

	public static boolean isXMLCharacter(int c) {

		if (c <= 0xD7FF) {

			if (c >= 0x20)
				return true;

			else {

				if (c == '\n')
					return true;

				if (c == '\r')
					return true;

				if (c == '\t')
					return true;

				return false;

			}

		}

		if (c < 0xE000)
			return false;
		if (c <= 0xFFFD)
			return true;

		if (c < 0x10000)
			return false;
		if (c <= 0x10FFFF)
			return true;

		return false;
	}

	public static String strFilter(String str) {
		if (str == null) {
			return "";
		}
		StringBuilder newStr = new StringBuilder();
		for (char a : str.toCharArray()) {
			if (isXMLCharacter(a)) {
				newStr.append(a);
			}
		}
		return newStr.toString();
	}

	/**
	 * 验证email格式
	 *
	 * @param str
	 * @return
	 */
	public static boolean isEmail(String str) {
		if (StringUtils.isEmpty(str)) {
			return false;
		}
		String regex = "\\w+(\\.\\w+)*@\\w+(\\.\\w+)+";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		boolean validate = m.matches();
		return validate;
	}

	public static String HexToChar(String source) {
		if (StringUtils.isEmpty(source)) {
			return null;
		}
		String flag = "&#";
		if (null == source || "".equals(source)) {
			return "";
		}

		if (source.indexOf(flag) >= 0) {
			StringBuffer newValue = new StringBuffer();
			StringTokenizer st = new StringTokenizer(source, ";");
			while (st.hasMoreElements()) {
				String value = "" + st.nextElement();
				if ("".equals(value)) {
					continue;
				}
				int flagPosition = value.indexOf(flag);

				if (flagPosition >= 0) {//
					if (flagPosition > 0) {// 标志位前还有非unicode字符
						String others = value.substring(0, flagPosition);
						newValue.append(others);
					}
					value = value.substring(flagPosition + flag.length());

					boolean is16Int = false;
					if (value.startsWith("x")) {
						value = value.replace("x", "");
						is16Int = true;
					}

					try {
						int OctalInt = 0;
						if (is16Int) {
							OctalInt = Integer.parseInt(value, 16);
						} else {
							OctalInt = Integer.parseInt(value);
						}
						newValue.append(String.valueOf((char) OctalInt));
					} catch (NumberFormatException e) {// 过滤unicode编码中包含的&nbsp;等符号
						// add by norby
						continue;
					}
				} else {
					newValue.append(value);
				}
			}

			source = newValue.toString();
		}
		return source;
	}

	public static String convertTime(String timeLong) {
		if (StringUtils.isEmpty(timeLong)) {
			return null;
		}
		String time = "";
		if (timeLong != null && !"".equals(timeLong)) {
			int num = Integer.parseInt(timeLong);
			String scond = "" + (num / 60f);
			if ((num / 60f) > 0.1) {
				int bound = scond.length() < 3 ? scond.length() : 3;
				time = scond.substring(0, bound);
			}
		}
		return time;
	}

	public boolean isBlank(String arg0) {
		return StringUtils.isBlank(arg0);
	}

	public static String urlEncode(String str) {
		if (str != null && !"".equals(str)) {
			try {
				return URLEncoder.encode(str, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	public static String toSubString(final String str, final Integer len) {
		if (str.length() < len)
			return str;
		else {
			String subString = str.substring(0, len);
			int chineseLen = subString.getBytes().length - subString.length();
			int subLen = chineseLen + 2 * (len - chineseLen);
			if (chineseLen == len) {
				return str.substring(0, len);
			} else {
				if (subLen > str.length())
					return str;
				else
					return str.substring(0, subLen);
			}
		}

	}

	public static String getStringByArrayIndex(String[] str, int index) {
		return (String) str[index];
	}

	/**
	 * 把10进制的数字转换成64进制
	 *
	 * @param number
	 * @param shift
	 * @return
	 */
	public static String CompressNumber(long number) {
		char[] buf = new char[64];
		int charPos = 64;
		int radix = 1 << 6;
		long mask = radix - 1;
		do {
			buf[--charPos] = digits[(int) (number & mask)];
			number >>>= 6;
		} while (number != 0);
		return new String(buf, charPos, (64 - charPos));
	}

	/**
	 * 把 64进制的字符串转换成10进制
	 *
	 * @param decompStr
	 * @return
	 */
	public static long UnCompressNumber(String decompStr) {
		long result = 0;
		for (int i = decompStr.length() - 1; i >= 0; i--) {
			if (i == decompStr.length() - 1) {
				result += getCharIndexNum(decompStr.charAt(i));
				continue;
			}
			for (int j = 0; j < digits.length; j++) {
				if (decompStr.charAt(i) == digits[j]) {
					result += ((long) j) << 6 * (decompStr.length() - 1 - i);
				}
			}
		}
		return result;
	}

	private static long getCharIndexNum(char ch) {
		int num = ((int) ch);
		if (num >= 48 && num <= 57) {
			return num - 48;
		} else if (num >= 97 && num <= 122) {
			return num - 87;
		} else if (num >= 65 && num <= 90) {
			return num - 29;
		} else if (num == 43) {
			return 62;
		} else if (num == 47) {
			return 63;
		}
		return 0;
	}
	
	/**
     * 读取配置文件信息
     * 
     * @param proFile
     *            properties文件名
     * 
     * @return
     */
    public static Properties getProperties(String proFile) {
        Properties properties = new Properties();
        InputStream input = StringUtil.class.getResourceAsStream("/" + proFile);

        if (input != null) {
            try {
                properties.load(input);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            } finally {
                if (input != null)
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        } else {
            throw new RuntimeException(proFile + " not found. Please make sure the file is in the classpath");
        }
        return properties;
    }
    
    public static boolean isHalfChar(String str) {
        if (str == null) {
            return false;
        } else {
            String regex = "[\u0000-\u00FF]+";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(str);
            boolean validate = m.matches();
            return validate;
        }
    }
    
    /**
     * @descreption 判断昵称只包含合法字符
     * @param str
     * @return
     */
    public static boolean isLetterNumberChinese(String str) {
        str = StringUtil.nullStringToEmptyString(str);
        String regex = "[\\w&;_\u4e00-\u9fa5]+";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        boolean validate = m.matches();
        return validate;
    }

    /**
     * @descreption 只包含合法字符
     * @param str
     * @return
     */
    public static boolean isValidate(String str) {
        str = StringUtil.nullStringToEmptyString(str);
        String regex = "^[\\w_]+";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        boolean validate = m.matches();
        return validate;
    }
	
	public static int uniLength(String value) {
		int valueLength = 0;
		// 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
		for (int i = 0; i < value.length(); i++) {
			// 获取一个字符
			char c = value.charAt(i);
			// 判断是否为中文字符
			if (isChinese(c)) {
				// 中文字符长度为2
				valueLength += 2;
			} else {
				// 其他字符长度为1
				valueLength += 1;
			}
		}
		// 进位取整
		return valueLength;
	}
	
	// GENERAL_PUNCTUATION 判断中文的“号
	// CJK_SYMBOLS_AND_PUNCTUATION 判断中文的。号
	// HALFWIDTH_AND_FULLWIDTH_FORMS 判断中文的，号
	private static final boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}
}
