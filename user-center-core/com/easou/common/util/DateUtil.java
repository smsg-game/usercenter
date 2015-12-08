package com.easou.common.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期方法类
 */
public final class DateUtil {

	/**
	 * 秒数
	 */
	public static final int	        SECOND	         = 1000;
	/**
	 * 分数
	 */
	public static final int	        MINUTE	         = SECOND * 60;
	/**
	 * 时数
	 */
	public static final int	        HOUR	         = MINUTE * 60;
	/**
	 * 天数
	 */
	public static final int	        DAY	            = HOUR * 24;
	/**
	 * 周数
	 */
	public static final int	        WEEK	         = DAY * 7;
	/**
	 * 年数
	 */
	public static final int	        YEAR	         = DAY * 365;	                                        // or
																																				// 366
																																				// ???
	private static SimpleDateFormat	dateFormat	   = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat	datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat	timeFormat	   = new SimpleDateFormat("HH:mm:ss");
	private static SimpleDateFormat	weekFormat	   = new SimpleDateFormat("yyyy年M月d日");
	private static SimpleDateFormat aboutFormat	   = new SimpleDateFormat("HH:mm MM-dd");
	private static final String[]	  WEEKS	         = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };

	/**
	 * private constructor
	 */
	private DateUtil () {}

	/**
	 * 格式化日期
	 * 
	 * 
	 * @param date
	 *           Date
	 * @return String
	 */
	public static String formatDate( Date date ) {
		return dateFormat.format(date);
	}

	/**
	 * 格式化日期
	 * 
	 * 
	 * @param date
	 *           Timestamp
	 * @return String
	 */
	public static String formatDate( Timestamp date ) {
		Date date2 = new Date(date.getTime());
		return dateFormat.format(date2);
	}

	public static Date StringToDate( String strDate ) {
		DateFormat tempformat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		try {
			if ( strDate == null || strDate.equals("null") ) {
				strDate = "0000-00-00 00:00:00";
			}
			date = tempformat.parse(strDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	public static Date StringToTime( String strTime ) {
		DateFormat tempformat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		try {
			if ( strTime == null || strTime.equals("null") ) {
				strTime = "0000-00-00 00:00:00";
			}
			date = tempformat.parse(strTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	public static Date StringToDatetime( String strDatetime ) {
		DateFormat tempformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		try {
			if ( strDatetime == null || strDatetime.equals("null") ) {
				strDatetime = "0000-00-00 00:00:00";
			}
			date = tempformat.parse(strDatetime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 格式化日期
	 * 
	 * 
	 * @param date
	 *           Date
	 * @return String
	 */
	public static String formatTime( Date date ) {
		return timeFormat.format(date);
	}

	/**
	 * 格式化日期
	 * 
	 * 
	 * @param date
	 *           Date
	 * @return String
	 */
	public static String formatDateTime( Date date ) {
		return datetimeFormat.format(date);
	}

	/**
	 * 格式化日期
	 * 
	 * 
	 * @param date
	 *           Date
	 * @return String
	 */
	public static String formatDateTimeWeek( Date date ) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.setFirstDayOfWeek(Calendar.SUNDAY);
		int weeknum = cal.get(Calendar.DAY_OF_WEEK) - 1;
		return weekFormat.format(date) + WEEKS[weeknum];
	}

	/**
	 * 计算年龄
	 * 
	 * @param memberBirthday
	 *           Timestamp
	 * @return byte
	 */
	public static byte getMemberAge( Timestamp memberBirthday ) {
		Calendar birthday = Calendar.getInstance();
		birthday.setTimeInMillis(memberBirthday.getTime());
		Calendar today = Calendar.getInstance();
		int age = today.get(Calendar.YEAR) - birthday.get(Calendar.YEAR);
		birthday.add(Calendar.YEAR, age);
		if ( today.before(birthday) ) {
			age--;
		}
		return (byte) age;
	}

	/**
	 * 当前日期
	 * 
	 * @return Date
	 */
	public static Date getCurrentDate() {
		return new Date(System.currentTimeMillis());
	}

	/**
	 * 生日的年份
	 * 
	 * 
	 * @param birth
	 *           Timestamp
	 * @return int
	 */
	public static int getBirthYear( Timestamp birth ) {
		int birth_year = 0;
		Calendar birthday = Calendar.getInstance();
		birthday.setTimeInMillis(birth.getTime());
		birth_year = birthday.get(Calendar.YEAR);
		return birth_year;
	}

	/**
	 * 生日的月份
	 * 
	 * 
	 * @param birth
	 *           Timestamp
	 * @return int
	 */
	public static int getBirthMonth( Timestamp birth ) {
		int birth_month = 0;
		Calendar birthday = Calendar.getInstance();
		birthday.setTimeInMillis(birth.getTime());
		birth_month = birthday.get(Calendar.MONTH) + 1;
		return birth_month;
	}

	/**
	 * 判断两个时间是否是同一天
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static boolean isSameDate( Date d1 , Date d2 ) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(d1);
		cal2.setTime(d2);
		if ( cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
		      && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) ) return true;
		return false;
	}

	public static void main( String[] args ) throws ParseException {
		Date d1 = Calendar.getInstance().getTime();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		Date d2 = sf.parse("2010-09-20");
		long cur = System.currentTimeMillis();
		long diff = getDayStartMS() - cur;
		diff = diff / 1000;
		System.out.println(diff);
		System.out.println(diff / 3600 + " " + diff / 60);
	}

	/**
	 * 生日的日期
	 * 
	 * 
	 * @param birth
	 *           Timestamp
	 * @return int
	 */
	public static int getBirthDay( Timestamp birth ) {
		int birth_day = 0;
		Calendar birthday = Calendar.getInstance();
		birthday.setTimeInMillis(birth.getTime());
		birth_day = birthday.get(Calendar.DATE);
		return birth_day;
	}

	/**
	 * 图片路径
	 * 
	 * @return String
	 */
	public static String getPicPath() {
		StringBuffer picpath = new StringBuffer(50);
		Calendar date = Calendar.getInstance();
		picpath.append("/");
		picpath.append(date.get(Calendar.DAY_OF_MONTH) % 8);
		picpath.append("/");
		picpath.append(date.get(Calendar.MINUTE));
		picpath.append("/");
		picpath.append(date.get(Calendar.SECOND));
		picpath.append("/");
		return picpath.toString();
	}

	/**
	 * 当前的Timestamp
	 * 
	 * @return Timestamp
	 */
	public static Timestamp getCurrentTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}

	/**
	 * 将 Timestamp 转为 String
	 * 
	 * @param timestamp
	 *           Timestamp
	 * @param displayTime
	 *           是否显示时间部分
	 * @return String
	 */
	public static String timestampToString( Timestamp timestamp , boolean displayTime ) {
		if ( timestamp == null ) {
			return "";
		}
		if ( displayTime ) {
			return timestamp.toString().substring(0, 16);
		}
		return timestamp.toString().substring(0, 10);
	}

	/**
	 * 将 Timestamp 转为 String (包括秒)
	 * 
	 * @param timestamp
	 *           Timestamp
	 * @param displayTime
	 *           是否显示时间部分(包括秒)
	 * @return String
	 */
	public static String timestampToStringSecond( Timestamp timestamp , boolean displayTime ) {
		if ( timestamp == null ) {
			return "";
		}
		if ( displayTime ) {
			return timestamp.toString().substring(0, 19);
		} else {
			return timestamp.toString().substring(0, 10);
		}
	}

	/**
	 * 将 Timestamp 转为 String，不显示时间部分
	 * 
	 * @param timestamp
	 *           Timestamp
	 * @return String
	 */
	public static String timestampToString( Timestamp timestamp ) {
		return timestampToString(timestamp, false);
	}

	/**
	 * add by Toyboy 用于计算加了天数后的日期
	 * 
	 * @param date
	 *           原来的日期
	 * 
	 * @param amount
	 *           要增加的天数
	 * @return date 加了天数后的日期
	 */
	public static Date calculatedays( Date date , int amount ) {
		GregorianCalendar g = new GregorianCalendar();
		g.setGregorianChange(date);
		g.add(GregorianCalendar.DATE, amount);
		Date d = g.getTime();
		return d;
	}

	public static String getCurrentDateTime() {
		String currentTime = "";
		try {
			java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar currentDate = Calendar.getInstance();
			currentTime = format.format(currentDate.getTime());
		} catch (Exception e) {
			return null;
		}
		return currentTime;
	}

	public static String getCurrentDateTimeString() {
		String currentTime = "";
		try {
			java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
			Calendar currentDate = Calendar.getInstance();
			currentTime = format.format(currentDate.getTime());
		} catch (Exception e) {
			return null;
		}
		return currentTime;
	}

	public static String getCurrentDateString() {
		String currentTime = "";
		try {
			java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
			Calendar currentDate = Calendar.getInstance();
			currentTime = format.format(currentDate.getTime());
		} catch (Exception e) {
			return null;
		}
		// System.out.println("fdfdfsfdsfds"+currentTime);
		return currentTime;
	}

	public static String getCurrentYear() {
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy");
		return format.format(new Date());
	}

	/**
	 * 取得每日零辰的时间 返回秒数<br/> 以现在的秒差
	 * 
	 * @return
	 */
	public static int getDayStart() {
		return (int) ((getDayStartMS() - System.currentTimeMillis()) / 1000);
	}

	/**
	 * 取得1970年以来到今夜24点的这毫秒数
	 * 
	 * @return
	 */
	public static long getDayStartMS() {
		Calendar ca = Calendar.getInstance();
		ca.add(Calendar.DAY_OF_MONTH, 1);
		ca.set(Calendar.HOUR_OF_DAY, 0);
		ca.set(Calendar.MINUTE, 0);
		ca.set(Calendar.SECOND, 0);
		return ca.getTimeInMillis();
	}
	
	public static String getAboutTime() {
		return aboutFormat.format(new Date());
	}
}
