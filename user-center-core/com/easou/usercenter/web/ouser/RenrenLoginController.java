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
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.easou.common.api.EucHttpClient;
import com.easou.common.constant.OUserConstant;
import com.easou.common.util.StringUtil;
import com.easou.usercenter.service.impl.UrlTransService;

@Controller
@RequestMapping("/")
public class RenrenLoginController extends BaseOuserController {

	private static Logger LOG = Logger.getLogger(RenrenLoginController.class);
	
	@Autowired
	private UrlTransService urlTransService;
	
	@RequestMapping("loginRenren")
	public String execute(ModelMap model,final HttpServletRequest request,
			final HttpServletResponse response) {
		Map paramMap = getRequestMap(request);
		if(!authorized(request, response, paramMap)){//非法第三方登录请求
	    	model.addAttribute("redirUrl", "/login");
			model.addAttribute("retMessage", "非法操作，系统将在3 秒后返回...");
			return "result";
	    }
		String code = (String)paramMap.get("code");
		String v = paramMap.get("uv")!=null?paramMap.get("uv").toString():null;
		//String state = (String)paramMap.get("state");
		//删除对应参数
		if (null == code || "".equals(code)) {
			return getRedirectLoginUrl(v);
		}else{//删除对应参数
			paramMap.remove("code");
			paramMap.remove("state");
		}
		//String code = (String)paramMap.get("code");
		String enKey = (String)paramMap.get(OUserConstant.ENCRYPT_SERVICE_TAG);
		if(!StringUtil.isEmpty(enKey)){
			paramMap.remove(OUserConstant.ENCRYPT_SERVICE_TAG);
		}
		String token = null;
		String error = null;
		final String ERROR = "invalid_grant";
		//paramMap.remove("code");	
		//paramMap.remove(OUserConstant.ENCRYPT_SERVICE_TAG);

		try {
			HttpPost httpRequest = new HttpPost(
					WeiboConfig.getValue(OUserConstant.RENREN_OAUTH_ACCESS_TOKEN_URL));
			List<BasicNameValuePair> listParameter = new ArrayList<BasicNameValuePair>();
			listParameter.add(new BasicNameValuePair("client_id", WeiboConfig
					.getValue(OUserConstant.RENREN_CLIENT_ID)));
			listParameter.add(new BasicNameValuePair("client_secret",
					WeiboConfig.getValue(OUserConstant.RENREN_CLIENT_SERCRET)));
			if(null!=enKey) {
				listParameter.add(new BasicNameValuePair("redirect_uri",
						WeiboConfig.getValue(OUserConstant.RENREN_REDIRECT_URI)
								+ "?enService=" + enKey));
			} else {
				listParameter
						.add(new BasicNameValuePair("redirect_uri", WeiboConfig
								.getValue(OUserConstant.RENREN_REDIRECT_URI)));
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
			JSONObject jsonObj = JSON.parseObject(accessline.toString());
			// 获取token
			token = jsonObj.getString("access_token");
			if(token==null){//获取不到token
				error = jsonObj.getString("error");
			}
			int expires = jsonObj.getIntValue("expires_in");
			// 开发者已经向开放平台申请了生命周期是永久的AccessToken,返回的参数refresh_token不为空
			if (jsonObj.containsKey("refresh_token")) {
				String refreshToken = jsonObj.getString("refresh_token")
						.toString();
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
        //获取人人网ID
		String openid = getSessionKeyInfo(token);
		if(openid==null){
            LOG.error("error for renren weibo,openid is null...");			
			return "errors";
		}
		Map<String,Object> oParaMap = new HashMap<String,Object>();
		oParaMap.put("token", token);
		return createdBindUrl(openid, OUserConstant.RENREN_TYPE, paramStr, request, response,oParaMap);
	}
	
	/**
	 * 根据token获取session key
	 * 
	 * @param token
	 * @return
	 */
	private String getSessionKeyInfo(String accessToken) {
		try {
		HttpPost httpRequest = new HttpPost(
				WeiboConfig.getValue(OUserConstant.RENREN_API_SESSIONKEY_URL));
		List<BasicNameValuePair> listParameter = new ArrayList<BasicNameValuePair>();
		listParameter.add(new BasicNameValuePair("oauth_token", accessToken));
		UrlEncodedFormEntity form = new UrlEncodedFormEntity(listParameter,
				"utf-8");
		httpRequest.setEntity(form);
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
				return getRenrenUserId(sessionkeyline.toString());
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
	private String getRenrenUserId(String obj) {
		try {
			JSONObject renrenUserinfo =JSON.parseObject(obj);
			JSONObject sessionkey_obj = JSON.parseObject(renrenUserinfo
					.getString("user"));
			return sessionkey_obj.getString("id");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
	}

	@Override
	protected String getNickName(Map<String,Object> oParaMap) {
		// TODO 根据token对象(或字符)获得昵称
		String nickName = null;
		String url = WeiboConfig.getValue("renren.apiServerURL");
		Map<String,String> paraMap = new HashMap<String,String>();
		paraMap.put("v", "1.0");
		paraMap.put("access_token",oParaMap.get("token").toString());
		paraMap.put("format","JSON");
		paraMap.put("method", "users.getInfo");
		//paraMap.put("uids",oParaMap.get("uids").toString());
		String result = EucHttpClient.httpPost(url, paraMap);
		//LOG.info(result);
		JSONArray jsonArray = JSONObject.parseArray(result);
		for(int i=0;i<jsonArray.size();i++){
			JSONObject jsonObject = (JSONObject)jsonArray.get(i);
			if(jsonObject.get("name")!=null){
				nickName = jsonObject.get("name").toString();
				break;
			}
		}
		return nickName;
	}

	@Override
	protected String getAccessToken(Object token) {
		return (String)token;
	}
	
	public static void main(String[] args) {
		String json = "[{\"uid\":331426556,\"tinyurl\":\"http://hdn.xnimg.cn/photos/hdn521/20100717/0205/h_tiny_4C5U_0eba0000b57a2f76.jpg\",\"vip\":1,\"sex\":1,\"name\":\"周浩鑫\",\"star\":0,\"headurl\":\"http://hdn.xnimg.cn/photos/hdn521/20100717/0205/h_head_cgjU_0eba0000b57a2f76.jpg\",\"zidou\":0}]";
		JSONArray jsonArray = JSONObject.parseArray(json);
		for(int i=0;i<jsonArray.size();i++){
			JSONObject jsonObject = (JSONObject)jsonArray.get(i);
			System.out.println(jsonObject.get("name"));
			break;
		}
	}

	
}