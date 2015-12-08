package com.easou.usercenter.web.ouser;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import weibo4j.util.WeiboConfig;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.easou.common.constant.OUserConstant;
import com.easou.common.util.StringUtil;
import com.easou.usercenter.service.impl.UrlTransService;
import com.qq.open.OpenApiV3;
import com.qq.open.OpensnsException;
import com.tencent.weibo.api.UserAPI;
import com.tencent.weibo.oauthv2.OAuthV2;
import com.tencent.weibo.oauthv2.OAuthV2Client;
import com.tencent.weibo.utils.QHttpClient;

@Controller
@RequestMapping("/")
public class TencentLoginController extends BaseOuserController {
	
	@Autowired
	private UrlTransService urlTransService;

	@RequestMapping("loginTqq")
	public String execute(final HttpServletRequest request,
			final HttpServletResponse response) {
		Map paramMap = getRequestMap(request);
		if(null==paramMap) {
			return "errors";
		}
		String code = (String)paramMap.get("code");
		String openid = (String)paramMap.get("openid");
		String openkey = (String)paramMap.get("openkey");
		String enKey = (String)paramMap.get(OUserConstant.ENCRYPT_SERVICE_TAG);

		if (null == code || "".equals(code)) {
			return "errors";
		}

		OAuthV2 oAuth = new OAuthV2();
		oAuth.setAuthorizeCode(code);
		oAuth.setOpenid(openid);
		oAuth.setOpenkey(openkey);
		oAuth.setClientSecret(WeiboConfig.getValue(OUserConstant.TQQ_CLIENT_SERCRET));
		oAuth.setClientId(WeiboConfig.getValue(OUserConstant.TQQ_CLIENT_ID));
		if(null!=enKey) {
			oAuth.setRedirectUri("http://sso.fantingame.com/loginTqq?" + OUserConstant.ENCRYPT_SERVICE_TAG + "=" + enKey);
		}

		// 换取access token
		oAuth.setGrantType("authorize_code");
		// 自定制http连接管理器
		QHttpClient qHttpClient = new QHttpClient(2, 2, 5000, 5000, null, null);
		OAuthV2Client.setQHttpClient(qHttpClient);
		try {
			OAuthV2Client.accessToken(oAuth);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		qHttpClient.shutdownConnection();
		// 检查是否正确取得access token
		if (oAuth.getStatus() == 3) {
			log.error("Get Access Token failed!");
			return "errors";
		}
		String paramStr = urlTransService.getUrl(enKey);
		Map<String,Object> oParaMap = new HashMap<String,Object>();
		String pf = "qzone";
		oParaMap.put("token",oAuth);
		oParaMap.put("openid",openid);
		oParaMap.put("openkey", openkey);
		oParaMap.put("pf", pf);
		return createdBindUrl(openid, OUserConstant.TQQ_TYPE, paramStr, request, response,oParaMap);
	}

	@Override
	protected String getNickName(Map<String,Object> oParaMap) {
		//TODO 获取昵称
		OpenApiV3 sdk = new OpenApiV3(WeiboConfig.getValue(OUserConstant.TQQ_CLIENT_ID),
				WeiboConfig.getValue(OUserConstant.TQQ_CLIENT_SERCRET));
		sdk.setServerName(WeiboConfig.getValue(OUserConstant.TQQ_API_SERVER));
		String userInfoStr = getQQUserInfoStr(sdk,oParaMap.get("openid").toString(),
				oParaMap.get("openkey").toString(),oParaMap.get("pf").toString());
		 log.debug("result qq userInfo:"+userInfoStr);
		if(StringUtil.isEmpty(userInfoStr)){
    		return null;
    	}
    	//userInfoStr = "{\"ret\": 0,\"is_lost\": 0, \"nickname\": \"梵町无线搜索\"}";
    	JSONObject object = JSONObject.parseObject(userInfoStr);
    	return object.getString("nickname");
	}

	@Override
	protected String getAccessToken(Object token) {
		return ((OAuthV2)token).getOpenid();
	}
	
	 /**
     * 调用UserInfo接口
     *
     */
    public String getQQUserInfoStr(OpenApiV3 sdk, String openid, String openkey, String pf)
    {
        // 指定OpenApi Cgi名字 
        String scriptName = "/v3/user/get_info";

        // 指定HTTP请求协议类型
        String protocol = "http";

        // 填充URL请求参数
        HashMap<String,String> params = new HashMap<String, String>();
        params.put("openid", openid);
        params.put("openkey", openkey);
        params.put("pf", pf);

        try{
            String resp = sdk.api(scriptName, params, protocol);
            log.debug("qq userInfo:"+resp);
            return resp;
            //System.out.println(resp);
        }catch (OpensnsException e){
            System.out.printf("Request Failed. code:%d, msg:%s\n", e.getErrorCode(), e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**获取QQ昵称*/
    public static String getQQNickName(String userInfoStr){
    	if(StringUtil.isEmpty(userInfoStr)){
    		return null;
    	}
    	//userInfoStr = "{\"ret\": 0,\"is_lost\": 0, \"nickname\": \"梵町无线搜索\"}";
    	JSONObject object = JSONObject.parseObject(userInfoStr);
    	return object.getString("nickname");
    }
    
    public static void main(String[] args) {
		System.out.println(getQQNickName(null));
	}
}