package com.easou.usercenter.util;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import java.util.List;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.params.*;

public class Http
{
	public static String post(String url,String postContent)
	{
		String result="";
		HttpClient httpclient=null;
		try
		{
			httpclient = new DefaultHttpClient();
			HttpParams params = httpclient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 20000);
		    HttpConnectionParams.setSoTimeout(params, 20000);
    		HttpPost httppost = new HttpPost(url);
    		StringEntity reqEntity = new StringEntity(postContent,"gbk");
			// ��������
			reqEntity.setContentType("application/x-www-form-urlencoded");

			// ������������
			httppost.setEntity(reqEntity);
			// ִ��
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			if (entity != null)
			{
				result = EntityUtils.toString(entity);
			}
		}
		catch (Exception e)
		{

		}
		finally
		{
			httpclient.getConnectionManager().shutdown();
		}
		return result;
	}

	public static String postByPair(String url, List<? extends org.apache.http.NameValuePair> data) throws ParseException, IOException
	{
		org.apache.http.client.HttpClient client = new DefaultHttpClient();
		HttpPost httpost = new HttpPost(url);
		httpost.setEntity(new UrlEncodedFormEntity(data, HTTP.UTF_8));
		try
		{
			HttpResponse response = client.execute(httpost);
			HttpEntity entity = response.getEntity();
			System.out.println("<< Response: " + response.getStatusLine());
			if (entity != null) { return EntityUtils.toString(entity); }
			return null;
		}
		finally
		{
			client.getConnectionManager().shutdown();
		}
	}

	public static String get(String url) throws ClientProtocolException, IOException
	{
		org.apache.http.client.HttpClient client = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);
		try
		{
			return client.execute(httpget, new BasicResponseHandler());
		}
		finally
		{
			client.getConnectionManager().shutdown();
		}
	}	
}

