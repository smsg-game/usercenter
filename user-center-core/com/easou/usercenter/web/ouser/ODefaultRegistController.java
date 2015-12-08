package com.easou.usercenter.web.ouser;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.easou.cas.authenticateion.ThirdPartUserInfo;
import com.easou.common.api.RequestInfo;
import com.easou.common.constant.LoginType;
import com.easou.common.constant.OUserConstant;
import com.easou.common.constant.Way;
import com.easou.usercenter.entity.EucUser;
import com.easou.usercenter.entity.OUser;
import com.easou.usercenter.service.OUserService;
import com.easou.usercenter.util.BizLogUtil;
import com.easou.usercenter.util.LogConstant;
import com.easou.usercenter.web.BaseController;

@Controller
public class ODefaultRegistController extends BaseController {

//	@Resource(name = "oUserService")
//	private OUserService oUserService;
//
//	@RequestMapping("/oDefRegist")
//	public String execute(final HttpServletRequest request,
//			final HttpServletResponse response) {
//		try {
//			String service = request.getParameter("service")!=null?
//					request.getParameter("service"):"";
//			RequestInfo info=fetchRequestInfo(request);
//
//			ThirdPartUserInfo tinfo = (ThirdPartUserInfo) request.getSession()
//					.getAttribute(OUserConstant.THIRDPART_INFO_SESSION);
//			request.removeAttribute(OUserConstant.THIRDPART_INFO_SESSION);
//			if (null == tinfo) {
//				log.debug("不是外部用户注册");
//				return "redirect:/login" + getQueryString(request);
//			} else {
//				if (log.isDebugEnabled()) {
//					log.debug("外部用户信息: " + tinfo.getThirdId() + " 网站类型: "
//							+ tinfo.getType());
//				}
//				//对应的日志类型
//			    String logType = getLogLoginType(tinfo.getType());
//			    
//				OUser ouser = oUserService.queryBindinInfo(tinfo.getThirdId(),
//						tinfo.getType());
//				if (null != ouser) {
//					log.debug("外部用户已存在，非法操作");
//					BizLogUtil
//							.registLog(info,"",null,Way.PAGE,LogConstant.REGIS_TYPE_EXTERNAL_USER,
//									LogConstant.RESULT_FAILURE,logType,
//									LogConstant.REGIS_RESULT_EXTERNAL_USER_SINA_BIND_EXIST,
//									LogConstant.REGIS_ACTION_MSISDN_VALIDATE);
//					return "redirect:/login" + getQueryString(request);
//				}
//				try {
//					EucUser eucUser = oUserService
//							.insertDefaultUserAndBindRegist(tinfo);
//					// 保存梵町ID号
//					// ((ThirdPartUserInfo) request.getSession().getAttribute(
//					// OUserConstant.THIRDPART_INFO_SESSION))
//					// .setEasouId(String.valueOf(eucUser.getId()));
//					// request.getSession().removeAttribute("uinfo");
//					if (null != eucUser) {
//						if (log.isDebugEnabled()) {
//							log.debug("第三方用户注册成功, 新用户id" + eucUser.getId());
//						}
//						tinfo.setEasouId(eucUser.getId() + "");
//						request.setAttribute(
//								OUserConstant.THIRDPART_INFO_SESSION, tinfo);
//						BizLogUtil.registLog(info, eucUser.getName(),
//								eucUser.getId(), Way.PAGE,
//								LogConstant.REGIS_TYPE_EXTERNAL_USER,
//								LogConstant.RESULT_SUCCESS, logType,
//								LogConstant.REGIS_RESULT_SUCCESS,
//								LogConstant.REGIS_ACTION_MSISDN_VALIDATE);
//						return "forward:/extLogin" + getQueryString(request);
//					} else {
//						// 注册失败
//						BizLogUtil.registLog(info, "", null, Way.PAGE,
//								LogConstant.REGIS_TYPE_EXTERNAL_USER,
//								LogConstant.RESULT_FAILURE, logType,
//								LogConstant.REGIS_RESULT_EXCEPTION,
//								LogConstant.REGIS_ACTION_MSISDN_VALIDATE);
//						return "redirect:/login" + getQueryString(request);
//					}
//				} catch (Exception e) {
//					log.error("绑定注册用户失败", e);
//					BizLogUtil.registLog(info, "", null, Way.PAGE,
//							LogConstant.REGIS_TYPE_EXTERNAL_USER,
//							LogConstant.RESULT_FAILURE,logType,
//							LogConstant.REGIS_RESULT_EXCEPTION,
//							LogConstant.REGIS_ACTION_MSISDN_VALIDATE);
//					return "redirect:/login" + getQueryString(request);
//				}
//			}
//		} catch (Exception e) {
//			log.error(e, e);
//			return "errors";
//		}
//	}
//	
//	/**
//	 * 根据外部注册类型，获取对应的日志类型
//	 * 
//	 * @param loginType
//	 *     日志类型
//	 * @return
//	 */
//	private String getLogLoginType(String loginType){
//		LoginType type = LoginType.getLoginType(loginType); 
//		if(type==LoginType.WEIBO_SINA){
//			return LogConstant.REGIS_TYPE_EXTERNAL_USER_SINA_WEIBO;
//		}else if(type==LoginType.WEIBO_RENREN){
//			return LogConstant.REGIS_TYPE_EXTERNAL_USER_RENREN_WEIBO;
//		}else if(type==LoginType.WEIBO_TQQ){
//			return LogConstant.REGIS_TYPE_EXTERNAL_USER_TENCENT_WEIBO;
//		}else if(type==LoginType.QQ){
//			return LogConstant.REGIS_TYPE_EXTERNAL_USER_QQ;
//		}else{
//			try {
//				throw new Exception("no found the loginType["+loginType+"]'s log type....");
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		return null;
//	}
}
