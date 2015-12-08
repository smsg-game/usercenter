package com.easou.cas.auth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.easou.common.api.CodeConstant;
import com.easou.common.api.EucApiResult;
import com.easou.common.api.EucParserException;
import com.easou.common.api.JReason;
import com.easou.common.api.JUser;
import com.easou.common.api.RequestInfo;

/**
 * serviceTicket校验处理类
 * 
 * @author damon
 * @version 1.0
 *
 */
public class EucValidateServiceTicketManager extends EucAbstractManager{
	
	protected static final Log LOG = LogFactory.getLog(EucValidateServiceTicketManager.class);
	
	public static final int SUCCESS_CODE = 0;

	

	
	/**
	 * 校验参数
	 * 
	 * @param validateUrl
	 * @param service
	 * @param serviceTicket
	 * @return
	 * @throws EucParserException
	 */
	protected boolean verify(String validateUrl,String service,String serviceTicket)throws EucParserException{
		if(!verify(validateUrl)){
			throw new EucParserException("validateUrl parameter is Illegal");
		}
		if(serviceTicket==null||"".equals(serviceTicket)){
			throw new EucParserException("serviceTicket parameter's value is ["+serviceTicket+"]");
		}
		if(service==null||"".equals(service)){
			throw new EucParserException("service parameter's value is ["+service+"]");
		}
		return true;
	}
	

	/**
	 * 组装验证地址
	 * 
	 * @param serviceTicket
	 * @param service
	 * @param validateUrl
	 * @return
	 */
	protected String buildValidateUrl(String validateUrl,String service,
			String serviceTicket,String resultType,RequestInfo info){
		StringBuffer validateBuffer = new StringBuffer();
		if(validateUrl.indexOf("?")!=-1){
			validateBuffer.append(validateUrl).append("&");
		}else{
		   validateBuffer.append(validateUrl).append("?");
		}
		validateBuffer.append("ticket=").append(serviceTicket).append("&");
		if(resultType!=null){
		    validateBuffer.append(RESULT_TYPE).append("=").append(resultType).append("&");
		}else{//默认返回json
			 validateBuffer.append(RESULT_TYPE).append("=").append(RESULT_JSON).append("&");
		}
		if(info!=null){
		   validateBuffer.append(info.paraToString()).append("&");
		}
		try {
			validateBuffer.append("service=").append(URLEncoder.encode(service,"utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(LOG.isDebugEnabled()){
			LOG.debug("build validate url["+validateBuffer.toString()+"]");
		}
		return validateBuffer.toString();
		
	}
	
	/**
	 * 发送验证请求
	 * 
	 * @param validateUrl
	 * @param service
	 * @param serviceTicket
	 * @return
	 */
	protected EucApiResult<EucAuthResult> sendValidateRequest(String validateUrl){
		
		try {
			HttpPost httpRequest = new HttpPost(validateUrl);
			//List<BasicNameValuePair> listParameter = new ArrayList<BasicNameValuePair>();
			//listParameter.add(new BasicNameValuePair("ticket",serviceTicket));
			//listParameter.add(new BasicNameValuePair("service",service));
			//UrlEncodedFormEntity form = new UrlEncodedFormEntity(listParameter,
			//		"utf-8");
			//httpRequest.setEntity(form);

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
			//JSONObject jsonObj = JSON.parseObject(accessline.toString());
			//TODO 解析JSON
			JSONObject result = JSON.parseObject(accessline.toString());
			EucApiResult<EucAuthResult> eucApiResult = new EucApiResult<EucAuthResult>();
			String code = result.getString("code");
			eucApiResult.setResultCode(code);
			//if(code==SUCCESS_CODE){//成功返回
			if(CodeConstant.OK.equals(code)){//成功返回
			    EucToken token = result.getObject("token",EucToken.class);			
			    JUser user = result.getObject("user", JUser.class);
			    boolean isRegist = result.getBooleanValue("isRegist");
			    //返回新的esid
			    String esid = result.getString("esid");
			    EucAuthResult authResult = new EucAuthResult(token, user,esid,isRegist);
			    eucApiResult.setResult(authResult);
			}else if(result.get("desc")!=null){//失败返回			
				List<JReason> descList = new ArrayList<JReason>();
				descList.add(result.getObject("desc", JReason.class));
				eucApiResult.setDescList(descList);
			}else{
				LOG.warn("no user info or error info in result,please check...");
			}
			return eucApiResult;
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
		return null;
	}
	
	/**
	 * 校验url的合法性
	 * 
	 * @param url
	 * @return
	 */
	protected boolean verify(String url){
		if(url==null||"".equals(url)){
			return false;
		}
		return true;
	}
	
	/**
	 * 验证Service票据
	 * 
	 * @param serviceTicket
	 * @param secertKey
	 * @param validateUrl
	 * 
	 * @return
	 *     EucApiResult<EucAuthResult>
	 */
	public EucApiResult<EucAuthResult> validateServiceTicket(String validateUrl,String service,String serviceTicket,RequestInfo info)throws EucParserException{
		//校验参数
		verify(validateUrl,service, serviceTicket);
		//生成请求URL
		String vUrl = buildValidateUrl(validateUrl,service, serviceTicket,null,null);
		//通过HTTP进行校验，并返回结果进行解析
		EucApiResult<EucAuthResult> apiResult = sendValidateRequest(vUrl);
		return apiResult;
	}

}
