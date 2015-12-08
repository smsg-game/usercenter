package com.easou.usercenter.web.ouser;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.easou.cas.authenticateion.ThirdPartUserInfo;
import com.easou.cas.validation.EAssertion;
import com.easou.common.api.RequestInfo;
import com.easou.common.constant.OUserConstant;
import com.easou.common.constant.Way;
import com.easou.common.util.StringUtil;
import com.easou.usercenter.entity.EucUser;
import com.easou.usercenter.entity.OUser;
import com.easou.usercenter.service.EucUserService;
import com.easou.usercenter.service.OUserService;
import com.easou.usercenter.service.impl.OutUserBindService;
import com.easou.usercenter.util.BizLogUtil;
import com.easou.usercenter.util.ConditionUtil;
import com.easou.usercenter.util.Constant;
import com.easou.usercenter.util.LogConstant;
import com.easou.usercenter.web.user.UserBaseController;

public abstract class BaseOuserController extends UserBaseController {

	@Autowired
	protected OutUserBindService ouserService;

	@Autowired
	protected EucUserService eucUserService;
	
	@Resource(name = "oUserService")
	private OUserService oUserService;

	public void setOuserService(OutUserBindService ouserService) {
		this.ouserService = ouserService;
	}

	protected String createdBindUrl(String thirdId, String type, String paramStr, final HttpServletRequest request,
			final HttpServletResponse response,Map<String,Object> oParaMap) {

		ThirdPartUserInfo tinfo = new ThirdPartUserInfo();
		tinfo.setThirdId(thirdId);
		tinfo.setAccToken(getAccessToken(oParaMap.get("token")));
		tinfo.setType(type);
	    //参数
	    Map<String,String> paramMap = getParam(paramStr);
		//业务来源 如果值等于 center说明是从个人中心跳转过来的
		String page = paramMap!=null?paramMap.get("page"):null;
		//业务来源 如果值等于 gt说明是游戏触屏版
		String v = paramMap!=null?paramMap.get("uv"):null;
		OUser ouser = ouserService.queryOUserByExtIdAndOType(thirdId, type);

	    if(page!=null
	    		&&"bind".equals(page)){//由绑定页面跳转过来
	    	paramMap.remove("page");
	    	if(paramMap.get("enService")!=null){
	    		paramMap.remove("enService");
	    	}
	    	if(paramMap.get("code")!=null){
	    	    paramMap.remove("code");
	    	}
    	    //重新生成参数串
    	    paramStr=getQueryParam(paramMap);
	    	if(ouser!=null){//已绑定过用户
	    		request.setAttribute("redirUrl", getBindUrl(v)+ paramStr);
	    		request.setAttribute("retMessage", "关联失败，该用户已经关联了其他梵町帐号，系统将在3 秒后返回...");
	    		//return "errors";
	    		return "result";
	    		//return "result";
	    	}
	    	EAssertion ass = getLoginArrest(request);
	    	if(ass!=null){
				String nickName = null;
				try {
					nickName = getNickName(oParaMap);
				} catch (Exception e) {
					log.warn("取不到昵称",e);
				}
				if(null!=nickName && !"".equals(nickName.trim())) {
					tinfo.setNickName(nickName);
				}else{
					tinfo.setNickName("es");
				}
	    		ouserService.bindingUser(ass.getUserId(), type, thirdId,tinfo.getNickName());
	    		return  "redirect:"+getBindUrl(v)+ paramStr;
	    	}else{//错误提示
	    		request.setAttribute("redirUrl", getBindUrl(v)+ paramStr);
	    		request.setAttribute("retMessage", "关联失败，无效关联请求，系统将在3 秒后返回...");
				return "result";
	    	}
	    }

	    //重新生成参数串
	    paramStr=getQueryParam(paramMap);
		if (null == ouser) {
			log.debug("未绑定外部用户");
			String nickName = null;
			try {
				nickName = getNickName(oParaMap);
			} catch (Exception e) {
				log.warn("取不到昵称", e);
			}
			if(null!=nickName && !"".equals(nickName.trim())) {
				tinfo.setNickName(nickName);
			}else{
				tinfo.setNickName("es");
			}			
			return registAndBind(tinfo, request, paramStr,v, paramMap);
		} else {
			log.debug("已有绑定用户，直接登录");
			EucUser eucUser = eucUserService.queryUserInfoById(ouser.getEa_id()
					+ "");
			if (null != eucUser) {
//				BizLogUtil.registLog(username, LogConstant.REGIS_RESULT_SUCCESS, LogConstant.LOGIN_TYPE_EXTERNAL_USER_SINA_WEIBO, service);
				tinfo.setEasouId(ouser.getEa_id()+"");
				tinfo.setRegistTime(eucUser.getRegisterTime());
				request.setAttribute(OUserConstant.THIRDPART_INFO_SESSION, tinfo);
//				String redirect = "forward:/extLogin" + paramStr;
				String redirect = "forward:/extLogin" + paramStr;
				log.debug(redirect);
				return redirect;
			} else {
				return getRedirectLoginUrl(v) + paramStr;
			}
		}
	}
	
	/**
	 * 解析string串里的参数值
	 * 
	 * @param paramStr
	 * @return
	 */
	private Map<String,String> getParam(String paramStr){
		if(StringUtil.isEmpty(paramStr)||paramStr.length()==1){//空字符串或没有参数值
			return null;
		}else{
		    Map<String,String> paramMap = new HashMap<String,String>();
		    if(paramStr.startsWith("?")){
		    	paramStr = paramStr.substring(1);
		    }
		    String[] temps = paramStr.split("&");
		    if(temps!=null&&temps.length>0){
		    	for(String temp:temps){
		    		String[] p = temp.split("=");
		    		if(p.length==2){
		    			paramMap.put(p[0], p[1]);
		    		}
		    	}
		    }
			return paramMap;
		}
	}

	/**
	 * 验证合法性
	 * 
	 * @param request
	 * @param response
	 * @param paramMap
	 * @return
	 */
	protected boolean authorized(final HttpServletRequest request,
			final HttpServletResponse response,@SuppressWarnings("rawtypes") Map paramMap){

		String state = paramMap.get("state")!=null?(String)paramMap.get("state"):null;
		if(null!=state){//
			String vState = request.getSession().getAttribute("state")!=null
					?(String)request.getSession().getAttribute("state"):null;
					if(state.equals(vState)){//合法
						return true;
					}else{//非法
						return false;
					}		
		}else{//非法
			return false;
		}
	}
	
	/**
	 * 为第三方登录的用户注册一个新用户并绑定
	 * 
	 * @param tinfo
	 * @param request
	 * @param paramMap
	 * @return
	 */
	private String registAndBind(ThirdPartUserInfo tinfo,
			HttpServletRequest request, String paramStr,String v, Map<String, String> paramMap) {
		try {
			RequestInfo info=new RequestInfo();
			String qn="";
			String appId="";
			info.setUid(ConditionUtil.getUid(request));
			info.setAgent(ConditionUtil.getAppAgent(request));
			if(null!=paramMap) {
				info.setEsid(paramMap.get(Constant.ESID_TAG));
				info.setSource(paramMap.get(Constant.CHANNEL_TAG));
				qn=paramMap.get(Constant.QN_TAG);
				appId =paramMap.get(Constant.GAMEID_TAG);	// 进入游戏的用户
				if(appId==null) {
					appId = paramMap.get(Constant.APPID_TAG);	// 以后取消掉游戏id，统一叫appId
				}
			}
			if (log.isDebugEnabled()) {
				log.debug("外部用户信息: " + tinfo.getThirdId() + " 网站类型: "
						+ tinfo.getType());
			}
			// 对应的日志类型
			String logType = BizLogUtil.getExtLogLoginType(tinfo.getType());
			try {
				EucUser eucUser = oUserService
						.insertDefaultUserAndBindRegist(tinfo);
				if (null != eucUser) {
					if (log.isDebugEnabled()) {
						log.debug("第三方用户注册成功, 新用户id" + eucUser.getId());
					}
					tinfo.setEasouId(eucUser.getId() + "");
					tinfo.setRegistTime(eucUser.getRegisterTime());
					request.setAttribute(OUserConstant.THIRDPART_INFO_SESSION,
							tinfo);
					BizLogUtil.registLog(info, eucUser.getName(), eucUser.getId(), Way.PAGE,
							LogConstant.REGIS_TYPE_EXTERNAL_USER,
							LogConstant.RESULT_SUCCESS, logType,
							LogConstant.REGIS_RESULT_SUCCESS,
							LogConstant.REGIS_ACTION_MSISDN_VALIDATE, qn, appId);
					return "forward:/extLogin" + paramStr;
				} else {
					BizLogUtil.registLog(info, "", null, Way.PAGE,
							LogConstant.REGIS_TYPE_EXTERNAL_USER,
							LogConstant.RESULT_FAILURE, logType,
							LogConstant.REGIS_RESULT_EXCEPTION,
							LogConstant.REGIS_ACTION_MSISDN_VALIDATE, qn, appId);
					return getRedirectLoginUrl(v) + paramStr;
				}
			} catch (Exception e) {
				log.error("绑定注册用户失败", e);
				BizLogUtil.registLog(info, "", null, Way.PAGE,
						LogConstant.REGIS_TYPE_EXTERNAL_USER,
						LogConstant.RESULT_FAILURE, logType,
						LogConstant.REGIS_RESULT_EXCEPTION,
						LogConstant.REGIS_ACTION_MSISDN_VALIDATE, qn, appId);
				return getRedirectLoginUrl(v) + paramStr;
			}
		} catch (Exception e) {
			log.error(e, e);
			return "errors";
		}
	}
	
    /**
     * 获取登录路径
     * 
     * @return
     */
	protected String getRedirectLoginUrl(String v){
		if(v!=null&&"gt".equals(v)){
			return "redirect:/game/touch/gtLogin";
		}else{
			return "redirect:/login";
		}
	}
	
	/**
	 * 获取绑定主页面
	 * 
	 * @param v
	 * @return
	 */
	protected String getBindUrl(String v){
		if(v != null && "module".equals(v)) {
			return "/user/usersetting.html";
		}
		if(v!=null&&"gt".equals(v)){
			return "/game/touch/bindAccount";
		}else{
			return "/user/sbindUser";
		}
	}
	
	//protected abstract String getNickName(Object token);
	protected abstract String getNickName(Map<String,Object> paraMap);
	
	protected abstract String getAccessToken(Object token);
}