package com.easou.usercenter.web.ouser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import weibo4j.util.WeiboConfig;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.easou.common.api.EucHttpClient;
import com.easou.common.constant.OUserConstant;
import com.easou.common.util.StringUtil;
import com.easou.usercenter.service.impl.UrlTransService;
@Controller
@RequestMapping("/")
public class QQLoginController extends BaseOuserController {

	private static Logger LOG = Logger.getLogger(QQLoginController.class);
	
	@Autowired
	private UrlTransService urlTransService;
	
	@RequestMapping("loginQQ")
	public String execute(ModelMap model,final HttpServletRequest request,
			final HttpServletResponse response) {
		Map paramMap = getRequestMap(request);
	    if(!authorized(request, response, paramMap)){//非法第三方登录请求
	    	model.addAttribute("redirUrl", "/login");
			model.addAttribute("retMessage", "非法操作，系统将在3 秒后返回...");
			return "result";
	    }
		
		String code = (String)paramMap.get("code");
		//String state = (String)paramMap.get("state");
		//删除对应参数
		if (null == code || "".equals(code)) {
			return "errors";
		}else{//删除对应参数
			paramMap.remove("code");
			paramMap.remove("state");
		}

//		String client_secret ="124c382428e7a120ae0ecce382667dc3";
//		String client_id = "100288904";
//		String redirect_uri = "http%3A%2F%2Fsso.fantingame.com%2FloginQQ%3F";
		String enKey = (String)paramMap.get(OUserConstant.ENCRYPT_SERVICE_TAG);
		if(!StringUtil.isEmpty(enKey)){
			paramMap.remove(OUserConstant.ENCRYPT_SERVICE_TAG);
		}
		String token = null;
		String expires = null;
		String error = null;
		final String ERROR = "invalid_grant";
		//paramMap.remove("code");	
		//paramMap.remove(OUserConstant.ENCRYPT_SERVICE_TAG);

		try {
			HttpPost httpRequest = new HttpPost(
					WeiboConfig.getValue(OUserConstant.QQ_OAUTH_ACCESS_TOKEN_URL));
			List<BasicNameValuePair> listParameter = new ArrayList<BasicNameValuePair>();
			listParameter.add(new BasicNameValuePair("client_id", WeiboConfig
					.getValue(OUserConstant.QQ_CLIENT_ID)));
			listParameter.add(new BasicNameValuePair("client_secret",
					WeiboConfig
					.getValue(OUserConstant.QQ_CLIENT_SERCRET)));
			if(null!=enKey) {
				listParameter.add(new BasicNameValuePair("redirect_uri",
						WeiboConfig.getValue(OUserConstant.QQ_REDIRECT_URI)
								+ "?enService=" + enKey));
			} else {
				listParameter
						.add(new BasicNameValuePair("redirect_uri", WeiboConfig
								.getValue(OUserConstant.QQ_REDIRECT_URI)));
			}
			listParameter.add(new BasicNameValuePair("grant_type",
					"authorization_code"));
			listParameter.add(new BasicNameValuePair("code", code));
			UrlEncodedFormEntity form = new UrlEncodedFormEntity(listParameter,
					"utf-8");
			httpRequest.setEntity(form);

			HttpClient client = new DefaultHttpClient();
			// 发送请求
			HttpResponse httpResponse = client.execute(httpRequest);
			StringBuffer accessline = new StringBuffer();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					httpResponse.getEntity().getContent(),"utf-8"));
			String line;
			while ((line = in.readLine()) != null) {
				accessline.append(line);
			}
			if(accessline.length()<1){
				return "errors";
			}
			String[] strs = accessline.toString().split("&");
			if(strs==null||strs.length<1){//无返回结果
				return "errors";
			}else{
				for(String temp:strs){
					if(temp.indexOf("access_token=")!=-1){
						token = temp.substring(temp.indexOf("=")+1);					    
					}else if(temp.indexOf("expires_in=")!=-1){
						expires =temp.substring(temp.indexOf("=")+1);		
					}
				}
			}
		} catch (JSONException e) {
				// TODO Auto-generated catch block
				//System.out.println("你的应用没有获得永久授权");
				e.printStackTrace();
			} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String paramStr = urlTransService.getUrl(enKey);
		if(token==null){
			if(code!=null&&ERROR.equals(error)){//验证过，重新验证一次
				String redirect = "forward:/oRedirect?";
				if(paramStr!=null){
					redirect = redirect + paramStr + "t=3";
				}else{
					redirect = redirect +"t=3";
				}
				return redirect;
			}
            LOG.error("error for renren weibo,token is null...");			
			return "errors";
		}
        //获取qq的 openID
		String openid = getSessionKeyInfo(token);
		if(openid==null){
            LOG.error("error for renren weibo,openid is null...");			
			return "errors";
		}
		Map<String,Object> oParaMap = new HashMap<String,Object>();
		oParaMap.put("token", token);
		oParaMap.put("openid", openid);
		oParaMap.put("clientid", WeiboConfig
					.getValue(OUserConstant.QQ_CLIENT_ID));
		return createdBindUrl(openid, OUserConstant.QQ_TYPE, paramStr, request, response,oParaMap);
	}
	/**
	 * 根据token获取session key
	 * 
	 * @param token
	 * @return
	 */
	private String getSessionKeyInfo(String accessToken) {
		try {
			String url = WeiboConfig.getValue(OUserConstant.QQ_API_SESSIONKEY_URL)+"?access_token="+accessToken;
			HttpGet httpRequest = new HttpGet(url);
		HttpClient client = new DefaultHttpClient();
		// 发送请求
		HttpResponse httpResponse = client.execute(httpRequest);
		BufferedReader in = new BufferedReader(new InputStreamReader(
				httpResponse.getEntity().getContent(),"utf-8"));
		StatusLine statusLine = httpResponse.getStatusLine();
		int statusCode = statusLine.getStatusCode();
		StringBuffer sessionkeyline = new StringBuffer();
		String line;
			while ((line = in.readLine()) != null) {
				sessionkeyline.append(line);
			}
			// 判断当前状态码是否是200
			if (statusCode == HttpStatus.SC_OK) {
				return getQQOpenId(sessionkeyline.toString());
			} else {// 有可能是accesstoken过期或是网络有问题
				return null;
			}
		}catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
	}

	/**
	 * 从json串中获取user id
	 * 
	 * @param obj
	 * @return
	 */
	private String getQQOpenId(String obj) {
		try {
			//callback( {"client_id":"YOUR_APPID","openid":"YOUR_OPENID"} )
			String result = obj.substring(9,obj.length()-2).trim();
			JSONObject qqOpeninfo =JSON.parseObject(result);
			return qqOpeninfo.getString("openid");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
	}
	@Override
	protected String getNickName(Map<String,Object> oParaMap) {
		// TODO 通过token对象(或字符)获得昵称
		String buffer = WeiboConfig.getValue("qq.apiGetUserInfoURL");
		Map<String,String> paraMap =  new HashMap<String,String>();
		paraMap.put("access_token",oParaMap.get("token").toString());
		paraMap.put("oauth_consumer_key",oParaMap.get("clientid").toString());
		paraMap.put("openid",oParaMap.get("openid").toString());
		LOG.info(buffer);
		String result = EucHttpClient.httpGet(buffer, paraMap);
		LOG.info(result);
	    if(StringUtil.isEmpty(result)){
	    	return null;
	    }
	    JSONObject object = JSONObject.parseObject(result);
	    return object.get("nickname")!=null?object.get("nickname").toString():null;
	}
	@Override
	protected String getAccessToken(Object token) {
		return (String)token;
	}
	
	
}
