package com.easou.usercenter.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;

import com.easou.common.api.RequestInfo;
import com.easou.common.constant.LoginType;
import com.easou.common.constant.OUserConstant;
import com.easou.common.constant.SMSType;
import com.easou.common.constant.Way;
import com.easou.common.util.StringUtil;

/**
 * 业务统计日志工具
 * 
 * @author damon
 * @since 2012.05.15
 * @version 1.0
 * 
 */
public class BizLogUtil {
	/** 登陆日志 */
	private static Logger loginLog = Logger.getLogger("biz.loginLog");
	/** 注册日志 */
	private static Logger regLog = Logger.getLogger("biz.regLog");
	/** 短信日志 */
	private static Logger smsLog = Logger.getLogger("biz.smsLog");
	/** 短信日志 */
	private static Logger stLog = Logger.getLogger("biz.stLog");
	/** 短信日志 */
	private static Logger bindLog = Logger.getLogger("biz.bindLog");
	/** 游戏登录日志 */
	private static Logger gLoginLog = Logger.getLogger("biz.gLoginLog");
	
	private static final String BIZ_LOG_ROOT = "/log/resin-uc/bizlog";
	
	private static final SimpleDateFormat LOG_FILE_TIME_MARK_FORMAT = new SimpleDateFormat("yyyyMMddHH");

	private static final DateFormat LOG_TIME_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	/**
	 * 获取当前日志记录时间字符串
	 * 
	 * @return
	 */
	public static String getCurrentLogDate() {
		return LOG_TIME_FORMAT.format(new Date());
	}

	/**
	 * 登录日志
	 * 
	 * @param info
	 * @param eaId
	 * @param userName
	 * @param loginWay
	 * @param result
	 * @param loginType
	 * @param st
	 * @param tgt
	 * @param resultDesc
	 * @param extType
	 * @param qn
	 * @param appId
	 * @param registDate
	 */
	public static void loginLog(RequestInfo info, String eaId, String userName, Way loginWay, int result, LoginType loginType, 
			String st,String tgt, String resultDesc, String extType, String qn, String appId, Date registDate) {
		
		FileAppender appender = (FileAppender)loginLog.getAppender("loginLog");
		appender.setFile(BIZ_LOG_ROOT + "/loginLog/loginLog.log." + LOG_FILE_TIME_MARK_FORMAT.format(new Date()));
		appender.activateOptions();
		LogBuffer buffer = new LogBuffer(LogSize.L_SIZE);
		try {
			String logType = "";
			if (loginType == LoginType.AUTO) {							// 自动
				logType = LogConstant.LOGIN_TYPE_AUTO;
			} else if (loginType == LoginType.EXT) {				// 外部
				logType = LogConstant.LOGIN_TYPE_EXTERNAL_USER;
			} else if (loginType == LoginType.DEFAULT) {		// 用户名
				logType = LogConstant.LOGIN_TYPE_NAME;
			} else if (loginType == LoginType.TGC) {
				logType = LogConstant.LOGIN_TYPE_TGC;
			}
			buffer.append(null == info.getAgent() ? "" : info.getAgent()); 		// login agent
			buffer.append(null == info.getSource() ? "" : info.getSource()); 	// channel
			buffer.append(result);
			buffer.append(null == info.getUid() ? "" : info.getUid());
			buffer.append(null == info.getEsid() ? "" : info.getEsid());
			buffer.append(logType);
			if (loginWay == Way.PAGE) {
				buffer.append(LogConstant.WAY_PAGE);
			} else if (loginWay == Way.INTERFACE) {
				buffer.append(LogConstant.WAY_INTERFACE);
			}
			buffer.append(eaId);
			
			// 用户名为空，用ID号代替
			if (StringUtil.isEmpty(userName) && StringUtil.isNotEmpty(eaId)) {
				userName = "ID_" + eaId;
			} else {
				try {
					userName = new String(userName.getBytes(), "utf-8").replace("{]", "");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			buffer.append(userName);
			buffer.append(null == qn ? "" : qn); // redirectUrl
			buffer.append(st);
			buffer.append(tgt);
			buffer.append(resultDesc);
			buffer.append(extType);
			buffer.append(appId);
			buffer.append(registDate == null ? "" : LOG_TIME_FORMAT.format(registDate));
			
			loginLog.warn(buffer.formatLog());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 登录日志 for api2
	 * @param eaId				用户ID
	 * @param userName		用户名
	 * @param loginWay		方式
	 * @param result			结果
	 * @param loginType		类型
	 * @param st					service ticket
	 * @param tgt					ticket grant ticket
	 * @param resultDesc	结果详情
	 * @param extType			外部类型
	 * @param registDate	当前用户注册时间(登录成功)
	 * @param qn					qn
	 * @param appId				appId
	 * @param source			source
	 * @param agent				agent
	 */
	public static void loginLogNew(String eaId, String userName, Way loginWay,
			int result, LoginType loginType, String st,String tgt, String resultDesc, String extType, Date registDate, String qn, String appId, String source, String agent) {
		FileAppender appender = (FileAppender)loginLog.getAppender("loginLog");
		appender.setFile(BIZ_LOG_ROOT + "/loginLog/loginLog.log." + LOG_FILE_TIME_MARK_FORMAT.format(new Date()));
		appender.activateOptions();
		LogBuffer buffer = new LogBuffer(LogSize.L_SIZE);
		try {
			if ((userName == null || "".equals(userName))
					&& (eaId != null && !"".equals(eaId))) {// 用户名为空，用ID号代替
				userName = "ID_" + eaId;
			} else {
				try {
					userName = new String(userName.getBytes(), "utf-8").replace("{]", "");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			String logType = "";
			if (loginType == LoginType.AUTO) {	//自动
				logType = LogConstant.LOGIN_TYPE_AUTO;
			} else if (loginType == LoginType.EXT) {	//外部
				logType = LogConstant.LOGIN_TYPE_EXTERNAL_USER;
			} else if (loginType == LoginType.DEFAULT) {	//用户名
				logType = LogConstant.LOGIN_TYPE_NAME;
			} else if (loginType == LoginType.TGC) {
				logType = LogConstant.LOGIN_TYPE_TGC;
			}
			
			buffer.append(agent);		// 2:agent
			buffer.append(source);	// 3:source
			buffer.append(result);	// 4:结果值
			buffer.append("");			// 5:uid
			buffer.append("");			// 6:esid
			buffer.append(logType);	// 7:类型(自动，用户名...)
			if (loginWay == Way.PAGE) {	// 8:登录方式(接口，页面)
				buffer.append(LogConstant.WAY_PAGE);
			} else if (loginWay == Way.INTERFACE) {
				buffer.append(LogConstant.WAY_INTERFACE);
			}
			buffer.append(eaId);				// 9:用户id
			buffer.append(userName);		// 10:登录用户名
			buffer.append(null==qn?"":qn);	//11:qn
			buffer.append(st);					// 12:service ticket
			buffer.append(tgt);					// 13:ticket grant ticket
			buffer.append(resultDesc);	// 14:结果详情(密码错等)
			buffer.append(extType);		// 15:外部登录类型(新浪等)
			buffer.append(appId);			// 16:appId
			String regTime="";
			if (null != registDate) {
				regTime = LOG_TIME_FORMAT.format(registDate);
			}
			buffer.append(regTime);		// 17:登录成功的用户的注册时间
			loginLog.warn(buffer.formatLog());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	

	/**
	 * 注册日志
	 * 
	 * @param userName
	 *            注册名（手机号或昵称）
	 * @param registResult
	 *            注册结果（100成功,200用户名已存在,300后台异常失败,
	 *            400未激活用户登陆，700外部用户登录失败【710新浪微博{711\712}、720腾讯微博】）
	 * @param registType
	 *            注册类型（100手机注册,200 登录名注册,700外部用户注册【710新浪微博、720腾讯微博】）
	 * @param redirectUrl
	 *            重定向地址（可为空）
	 * @param extType
	 *            外部类型(710新浪微博、720腾讯微博)
	 * @param resultDesc
	 *            结果描述
	 * @param channel
	 *            渠道
	 * @param esid
	 *            梵町ID
	 *  @param appAgent
	 *  		   客户端agent
	 */
	public static void registLog(RequestInfo info, String userName, Long eaId, Way registWay,
			String registType, int registResult, String extType, String resultDesc, String action, String qn, String appId) {
		FileAppender appender = (FileAppender)regLog.getAppender("regLog");
		appender.setFile(BIZ_LOG_ROOT+"/regLog/regLog.log."+LOG_FILE_TIME_MARK_FORMAT.format(new Date()));
		appender.activateOptions();
		LogBuffer buffer = new LogBuffer(LogSize.L_SIZE);
		try {
			buffer.append(null==info.getAgent()?"":info.getAgent());	// register agent
			buffer.append(null==info.getSource()?"":info.getSource());	// channel
			buffer.append(registResult);
			buffer.append(null==info.getUid()?"":info.getUid());
			buffer.append(null==info.getEsid()?"":info.getEsid());
			buffer.append(action);
			buffer.append(registType);
			if (registWay == Way.PAGE) {
				buffer.append(LogConstant.WAY_PAGE);
			} else if (registWay == Way.INTERFACE) {
				buffer.append(LogConstant.WAY_INTERFACE);
			} else if (registWay == Way.SMS) {
				buffer.append(LogConstant.WAY_SMS);
			}
			buffer.append(userName);
			buffer.append(null==qn?"":qn);	// redirectUrl
			buffer.append(extType);
			buffer.append(resultDesc);
			if(null==eaId) {
				buffer.append("");
			} else {
				buffer.append(String.valueOf(eaId));
			}
			buffer.append(appId);
			regLog.warn(buffer.formatLog());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 * 注册日志 for api2 等
	 * @param userName	用户名
	 * @param eaId				用户id
	 * @param registWay		方式
	 * @param registType	类型
	 * @param registResult	结果
	 * @param extType		外部类型
	 * @param resultDesc	结果详情
	 * @param action			动作
	 * @param qn					qn
	 * @param appId			appId
	 * @param source			source
	 * @param agent			agent
	 */
	public static void registLogNew(String userName, Long eaId, Way registWay,
			String registType, int registResult, String extType, String resultDesc, String action, String qn, String appId, String source, String agent) {
		FileAppender appender = (FileAppender)regLog.getAppender("regLog");
		appender.setFile(BIZ_LOG_ROOT+"/regLog/regLog.log."+LOG_FILE_TIME_MARK_FORMAT.format(new Date()));
		appender.activateOptions();
		LogBuffer buffer = new LogBuffer(LogSize.L_SIZE);
		try {
			buffer.append(agent);	//2: agent
			buffer.append(source);	//3: source
			buffer.append(registResult);	//4: 注册结果
			buffer.append("");			//5: uid
			buffer.append("");			//6: esid
			buffer.append(action);	//7: 当前动作()
			buffer.append(registType);	//8: 注册类型(手机，用户)
			if (registWay == Way.PAGE) {	//9: 注册方式(接口，页面)
				buffer.append(LogConstant.WAY_PAGE);
			} else if (registWay == Way.INTERFACE) {
				buffer.append(LogConstant.WAY_INTERFACE);
			} else if (registWay == Way.SMS) {
				buffer.append(LogConstant.WAY_SMS);
			}
			buffer.append("");		//10: 用户名
			buffer.append(null==qn?"":qn);	//11: qn 
			buffer.append(extType);				//12: 外部类型(新浪等)
			buffer.append(resultDesc);			//13: 结果详情
			if(null==eaId) {			//14: 注册成功的用户id
				buffer.append("");
			} else {
				buffer.append(String.valueOf(eaId));
			}
			buffer.append(appId);	//15: appId
			regLog.warn(buffer.formatLog());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 短信日志
	 * 
	 * @param userName
	 *            用户名，如没用户名，则用手机号
	 * @param msisdn
	 *            手机号
	 * @param smsType
	 *            短信类型（100注册；110忘记密码）
	 * @param status
	 *            发送结果
	 * @param msg
	 *            短信内容
	 */
	public static void smsLog(String uid, String msisdn, String status,
			String msg, String channel, String esid, String result,
			SMSType type, long delayTime) {
		FileAppender appender = (FileAppender)smsLog.getAppender("smsLog");
		appender.setFile(BIZ_LOG_ROOT+"/smsLog/smsLog.log."+LOG_FILE_TIME_MARK_FORMAT.format(new Date()));
		appender.activateOptions();
		LogBuffer buffer = new LogBuffer(LogSize.M_SIZE);
		try {
			buffer.append("");
			buffer.append(channel != null ? channel : "");
			buffer.append(result);
			buffer.append(uid != null? uid: "");
			buffer.append(esid != null ? esid : "");
			buffer.append(type != null ? type.value : "");
			buffer.append(msisdn);
			buffer.append(status);
			buffer.append(msg);
			buffer.append(delayTime >= 0 ? String.valueOf(delayTime) : "");
			smsLog.warn(buffer.formatLog());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 记录ST验证
	 * 
	 * @param st
	 *            ST
	 * @param status
	 *            验证结果（100为成功，200为失败）
	 */
	public static void stLog(String uid, String channel, int result,
			String esid, String st, String status) {
		FileAppender appender = (FileAppender)stLog.getAppender("stLog");
		appender.setFile(BIZ_LOG_ROOT+"/stLog/stLog.log."+LOG_FILE_TIME_MARK_FORMAT.format(new Date()));
		appender.activateOptions();
		LogBuffer buffer = new LogBuffer(LogSize.S_SIZE);
		try {
			buffer.append("");
			buffer.append(channel);
			buffer.append(result);
			buffer.append(uid);
			buffer.append(esid);
			buffer.append(st);
			buffer.append(status);
			stLog.warn(buffer.formatLog());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 用户绑定手机号日志
	 * 
	 * @param eaId
	 *            梵町ID
	 * @param cerMsisdn
	 *            当前手机号
	 * @param reMsisdn
	 *            重新绑定的手机号
	 * @param way
	 *            登录方式
	 * @param action
	 *            绑定动作（200绑定请求、300绑定验证）
	 * @param result
	 *            结果 200：成功下发短信【绑定请求】 201：下发短信失败【绑定请求】 300：绑定成功 【绑定验证】
	 *            301：验证码失效【绑定验证】 302：验证码失效【绑定验证】 901：手机号已经被绑定【绑定请求、绑定验证】
	 *            902：手机号非法【绑定请求、绑定验证】
	 * @param backUrl
	 *            回调地址
	 */
	public static void bindLog(String uid, String eaId, String cerMsisdn,
			String reMsisdn, Way way, BindActivity activity, String veriCode,
			int result, String backUrl, String channel, String esid,
			String resultDesc) {
		FileAppender appender = (FileAppender)bindLog.getAppender("bindLog");
		appender.setFile(BIZ_LOG_ROOT+"/bindLog/bindLog.log."+LOG_FILE_TIME_MARK_FORMAT.format(new Date()));
		appender.activateOptions();
		LogBuffer buffer = new LogBuffer(LogSize.L_SIZE);
		try {
			buffer.append("");				// 版本
			buffer.append(channel);	// 渠道
			buffer.append(result);
			buffer.append(uid);
			buffer.append(esid);
			buffer.append(activity.type);
			if (way == Way.PAGE) {
				buffer.append(LogConstant.WAY_PAGE);
			} else if (way == Way.INTERFACE) {
				buffer.append(LogConstant.WAY_INTERFACE);
			}
			buffer.append(eaId);
			buffer.append(cerMsisdn != null ? cerMsisdn : "");
			buffer.append(reMsisdn != null ? reMsisdn : "");
			buffer.append(veriCode != null ? veriCode : "");
			buffer.append(backUrl != null ? backUrl : "");
			buffer.append(resultDesc != null ? resultDesc : "");
			bindLog.warn(buffer.formatLog());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 统计游戏登录人数
	 * 
	 * @param qn
	 *            渠道
	 * @param appId
	 *            应用ID(游戏ID)
	 * @param userId
	 *            用户ID
	 * @param registerTime
	 *            注册时间
	 * @param loginTime
	 *            登录时间
	 */
	public static void gLoginLog(String qn, String appId, Long userId,
			int result, Date registerTime, Date loginTime, String appAgent) {
		FileAppender appender = (FileAppender)gLoginLog.getAppender("gLoginLog");
		appender.setFile(BIZ_LOG_ROOT+"/gLoginLog/gLoginLog.log."+LOG_FILE_TIME_MARK_FORMAT.format(new Date()));
		appender.activateOptions();
		LogBuffer buffer = new LogBuffer(LogSize.M_SIZE);
		try {
			buffer.append(appAgent);
			buffer.append(qn != null ? qn : "null");
			buffer.append(appId);
			buffer.append(String.valueOf(userId));
			buffer.append(result);
			buffer.append(LOG_TIME_FORMAT.format(registerTime));
			buffer.append(LOG_TIME_FORMAT.format(loginTime));
			// TODO 如增加其它属性，请在这里添加

			gLoginLog.warn(buffer.formatLog());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据外部注册类型，获取对应的日志类型
	 * 
	 * @param loginType
	 *     日志类型
	 * @return
	 */
	public static String getExtLogLoginType(String extType){
		if(OUserConstant.SINA_TYPE.equals(extType)){
			return LogConstant.REGIS_TYPE_EXTERNAL_USER_SINA_WEIBO;
		}else if(OUserConstant.RENREN_TYPE.equals(extType)){
			return LogConstant.REGIS_TYPE_EXTERNAL_USER_RENREN_WEIBO;
		}else if(OUserConstant.TQQ_TYPE.equals(extType)){
			return LogConstant.REGIS_TYPE_EXTERNAL_USER_TENCENT_WEIBO;
		}else if(OUserConstant.QQ_TYPE.equals(extType)){
			return LogConstant.REGIS_TYPE_EXTERNAL_USER_QQ;
		}else{
			try {
				throw new Exception("no found the loginType["+extType+"]'s log type....");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}